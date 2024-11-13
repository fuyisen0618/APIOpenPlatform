package com.fys.ysapiclientsdk;

import com.fys.ysapiclientsdk.client.Ysapiclient;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @Description TODO
 * @Date 2024-11-07 14:01
 * @Author fys
 */
@Configuration
@ComponentScan
@ConfigurationProperties("ysapi-client")
@Data
public class YsApiClientConfig {
    private String accessKey;
    private String secretKey;

    @Bean
    public Ysapiclient ysapiclient(){
        return new Ysapiclient(accessKey,secretKey);
    }
}
