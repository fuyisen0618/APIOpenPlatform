package com.fys.ysapigateway;

import com.fys.ysapiclientsdk.utils.SignUtils;
import com.fys.ysapicommon.model.entity.InterfaceInfo;
import com.fys.ysapicommon.model.entity.User;
import com.fys.ysapicommon.service.InnerInterfaceInfoService;
import com.fys.ysapicommon.service.InnerUserInterfaceInfoService;
import com.fys.ysapicommon.service.InnerUserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.reactivestreams.Publisher;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @Description 全局过滤
 * @Date 2024-11-11 13:35
 * @Author fys
 */

@Slf4j
@Component
public class CustomGlobalFilter implements GlobalFilter, Ordered {
    private final List<String> ID_WHITE_LIST = Arrays.asList("127.0.0.1");

    private final String INTERFACE_HOST = "http://localhost:8123";
    private final long DIFF = 1000*60*5L;

    @DubboReference
    InnerInterfaceInfoService innerInterfaceInfoService;
    @DubboReference
    InnerUserService innerUserService;
    @DubboReference
    InnerUserInterfaceInfoService innerUserInterfaceInfoService;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String url = INTERFACE_HOST+request.getPath().value();
        String method = request.getMethodValue();
        log.info("请求唯一标识："+request.getId());
        log.info("请求路径："+url);
        log.info("请求方法："+method);
        log.info("请求参数："+request.getQueryParams());
        String hostString = request.getRemoteAddress().getHostString();
        log.info("请求来源地址："+hostString);
        ServerHttpResponse response = exchange.getResponse();
        if(!ID_WHITE_LIST.contains(hostString)){
            return handleNoAuth(response);
        }
        HttpHeaders headers = request.getHeaders();
        String accessKsy = headers.getFirst("accessKey");
        String body = headers.getFirst("body");
        String time = headers.getFirst("time");
        String nonce = headers.getFirst("nonce");
        String sign = headers.getFirst("sign");
        User invokeUser=null;
        try {
            invokeUser = innerUserService.getInvokeUser(accessKsy);
        } catch (Exception e) {
            log.info("getInvokeUser error",e);
        }
        if (invokeUser==null){
            return handleNoAuth(response);
        }
        String secretKey = invokeUser.getSecretKey();
        if (!sign.equals(SignUtils.genSign(body,secretKey))){
            return handleNoAuth(response);
        }
        if (Long.valueOf(nonce)>10000){
            return handleNoAuth(response);
        }
        if (!SignUtils.validTimeStamp(DIFF,Long.valueOf(time))){
            return handleNoAuth(response);
        }
        // 调用模拟接口，验证是否存在
        InterfaceInfo interfaceInfo=null;
        try {
            interfaceInfo = innerInterfaceInfoService.getInterfaceInfo(url, method);
        } catch (Exception e) {
            log.info("getInterfaceInfo error",e);
        }
        if (interfaceInfo==null){
            return handleNoAuth(response);
        }
        Long interfaceInfoId = interfaceInfo.getId();
        Long userId = invokeUser.getId();
        //验证是否还有调用次数
        if(!innerUserInterfaceInfoService.existInvokeCount(interfaceInfoId,userId)){
            return handleNoAuth(response);
        }
        log.info("响应："+response.getStatusCode());
        return handlResponse(exchange,chain,interfaceInfoId,userId);
    }

    public Mono<Void> handleNoAuth(ServerHttpResponse response){
        response.setStatusCode(HttpStatus.FORBIDDEN);
        return response.setComplete();
    }


    public Mono<Void> handleInvokeError(ServerHttpResponse response){
        response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
        return response.setComplete();
    }

    @Override
    public int getOrder() {
        return -1;
    }

    public Mono<Void> handlResponse(ServerWebExchange exchange, GatewayFilterChain chain, Long interfaceInfoId, Long userId){
        try {
            ServerHttpResponse originalResponse = exchange.getResponse();
            DataBufferFactory bufferFactory = originalResponse.bufferFactory();

            HttpStatus statusCode = originalResponse.getStatusCode();

            if(statusCode == HttpStatus.OK){
                ServerHttpResponseDecorator decoratedResponse = new ServerHttpResponseDecorator(originalResponse) {

                    @Override
                    public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {
                        if (body instanceof Flux) {
                            Flux<? extends DataBuffer> fluxBody = Flux.from(body);
                            //
                            return super.writeWith(fluxBody.map(dataBuffer -> {
                                // 调用invokeCount(),使用接口次数加一
                                try {
                                    innerUserInterfaceInfoService.invokeCount(interfaceInfoId,userId);
                                } catch (Exception e) {
                                    log.info("invokeCount error",e);
                                }
                                byte[] content = new byte[dataBuffer.readableByteCount()];
                                dataBuffer.read(content);
                                DataBufferUtils.release(dataBuffer);//释放掉内存
                                // 构建日志
                                StringBuilder sb2 = new StringBuilder(200);
                                List<Object> rspArgs = new ArrayList<>();
                                rspArgs.add(originalResponse.getStatusCode());
                                //rspArgs.add(requestUrl);
                                String data = new String(content, StandardCharsets.UTF_8);//data
                                sb2.append(data);
                                log.info("响应数据："+data);
                                return bufferFactory.wrap(content);
                            }));
                        } else {
                            log.error("<--- {} 响应code异常", getStatusCode());
                        }
                        return super.writeWith(body);
                    }
                };
                return chain.filter(exchange.mutate().response(decoratedResponse).build());
            }
            return chain.filter(exchange);//降级处理返回数据
        }catch (Exception e){
            log.error("网关处理相应异常\n" + e);
            return chain.filter(exchange);
        }

    }
}
