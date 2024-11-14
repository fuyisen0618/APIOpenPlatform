package com.fys.ysapicommon.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.fys.ysapicommon.model.entity.User;

/**
 * 用户服务
 *
 * @author fys
 */
public interface InnerUserService {

    /**
     *查询是否已分配给用户密钥
     **/
    User getInvokeUser(String accessKey);
}
