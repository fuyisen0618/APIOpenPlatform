package com.fys.ysapiinterface.controller;


import com.fys.ysapiclientsdk.model.User;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * 名称 API
 *
 * @author fys
 */
@RestController
@RequestMapping("name")
public class NameController {
    //时间差（秒）
//    private final Long diff = 1000000L;

    @GetMapping("/get")
    public String getNameByGet(String name) {
        return "GET 你的名字是" + name;
    }

    @PostMapping("/post")
    public String getNameByPost(@RequestParam String name) {
        return "POST 你的名字是" + name;
    }

    @PostMapping("/user")
    public String getUserNameByPost(@RequestBody User user, HttpServletRequest request) {
//        String accessKsy = request.getHeader("accessKey");
//        String body = request.getHeader("body");
//        String time = request.getHeader("time");
//        String nonce = request.getHeader("nonce");
//        String sign = request.getHeader("sign");
//        if (!accessKsy.equals("fys")){
//            throw new RuntimeException("无权限");
//        }
//
//
//        if (!sign.equals(SignUtils.genSign(body,"qwertyuiop"))){
//            throw new RuntimeException("无权限");
//        }
//        if (Long.valueOf(nonce)>10000){
//            throw new RuntimeException("无权限");
//        }
//        if (!SignUtils.validTimeStamp(diff,Long.valueOf(time))){
//            throw new RuntimeException("无权限");
//        }
        String result = "POST 用户名字是" + user.getUsername();
        return result;
    }
}
