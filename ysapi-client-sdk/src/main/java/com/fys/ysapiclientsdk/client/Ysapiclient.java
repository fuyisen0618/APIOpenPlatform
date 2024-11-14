package com.fys.ysapiclientsdk.client;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.fys.ysapiclientsdk.model.User;
import com.fys.ysapiclientsdk.utils.SignUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author fys
 * @version 1.0
 * @description: TODO
 * @date 2024/11/6 11:09
 */

public class Ysapiclient {
    private String accessKey;
    private String secretKey;
    
    private static final String GATEWAY_HOST = "http://localhost:8090";

    public Ysapiclient(String accessKey, String secretKey) {
        this.accessKey = accessKey;
        this.secretKey = secretKey;
    }

    private Map<String,String> getHeaderMap(String body){
        HashMap<String, String> hashMap = new HashMap<String,String>();
        hashMap.put("accessKey",accessKey);
        hashMap.put("body",body);
        hashMap.put("nonce",RandomUtil.randomNumbers(4));
        hashMap.put("time",String.valueOf(System.currentTimeMillis()));
        hashMap.put("sign", SignUtils.genSign(body,secretKey));
        return hashMap;
    }

    public String getNameByGet(String name) {
        //可以单独传入http参数，这样参数会自动做URL编码，拼接在URL中
        HashMap<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("name", name);
        String result= HttpUtil.get(GATEWAY_HOST+"/api/name/get", paramMap);
        return result;
    }

    public String getNameByPost(String name) {
        HashMap<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("name", name);
        String result= HttpUtil.post(GATEWAY_HOST+"/api/name/post", paramMap);
        return result;
    }

    public String getUserNameByPost(User user) {
        String json = JSONUtil.toJsonStr(user);
        HttpResponse rep = HttpRequest.post(GATEWAY_HOST+"/api/name/user")
                .addHeaders(getHeaderMap(json))
                .body(json)
                .execute();
        System.out.println(rep.getStatus());
        String result = rep.body();
        System.out.println(result);
        return result;
    }
}
