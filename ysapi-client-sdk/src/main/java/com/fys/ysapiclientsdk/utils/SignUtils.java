package com.fys.ysapiclientsdk.utils;

import cn.hutool.crypto.digest.DigestUtil;

/**
 * @Description 签名工具
 * @Date 2024-11-06 15:51
 * @Author fys
 */

public class SignUtils {
    /**
     * @description:生成签名
     * @date: 2024/11/7 13:27
     * @param: body 请求信息
     * @param: SecretKey 密钥
     * @return: java.lang.String
     **/
    public static String genSign(String body,String SecretKey){
        return DigestUtil.sha256Hex(body+"."+ SecretKey);
    }

    /**
     * @description:时间戳校验
     * @date: 2024/11/7 13:28
     * @param: diff 有效时间
     * @param: timestamp 时间戳
     * @return: boolean
     **/
    public static boolean validTimeStamp(Long diff,Long timestamp){
        Long time=System.currentTimeMillis();
        long diffence=Math.abs(time-timestamp);
        return diffence<=diff;
    }
}
