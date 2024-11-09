package com.fys.project.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fys.project.model.entity.InterfaceInfo;

/**
* @author fys
* @description 针对表【interface_info(接口信息)】的数据库操作Service
* @createDate 2024-11-05 11:03:16
*/
public interface InterfaceInfoService extends IService<InterfaceInfo> {
    void validInterfaceInfo(InterfaceInfo interfaceInfo, boolean add);
}
