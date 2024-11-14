package com.fys.ysapicommon.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.fys.ysapicommon.model.entity.InterfaceInfo;

/**
* @author fys
* @description 针对表【interface_info(接口信息)】的数据库操作Service
* @createDate 2024-11-05 11:03:16
*/
public interface InnerInterfaceInfoService {

    /**
     *查询接口是否存在
     * @param path
     * @param method
     **/
    InterfaceInfo getInterfaceInfo(String path,String method);
}
