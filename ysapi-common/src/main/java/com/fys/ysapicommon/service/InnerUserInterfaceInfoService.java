package com.fys.ysapicommon.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.fys.ysapicommon.model.entity.UserInterfaceInfo;

/**
* @author fys
* @description 针对表【user_interface_info(用户调用接口关系)】的数据库操作Service
* @createDate 2024-11-09 15:50:26
*/
public interface InnerUserInterfaceInfoService {

    /**
     *统计接口调用次数
     **/
    boolean  invokeCount(long interfaceInfoId,long userId);
    boolean existInvokeCount(long interfaceInfoId,long userId);
}
