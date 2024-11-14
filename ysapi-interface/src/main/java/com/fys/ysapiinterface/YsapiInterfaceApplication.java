package com.fys.ysapiinterface;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication(scanBasePackages = "com.fys.ysapiinterface")
public class YsapiInterfaceApplication {

    public static void main(String[] args) {
        SpringApplication.run(YsapiInterfaceApplication.class, args);
    }

}
