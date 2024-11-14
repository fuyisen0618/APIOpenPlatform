package com.fys.ysapiinterface;

import com.fys.ysapiclientsdk.client.Ysapiclient;
import com.fys.ysapiclientsdk.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
class YsapiInterfaceApplicationTests {

    @Resource
    private Ysapiclient ysapiclient;

    @Test
    void contextLoads() {
        User user = new User();
        user.setUsername("fuys");
        String userNameByPost = ysapiclient.getUserNameByPost(user);
        System.out.println(userNameByPost);
    }

}
