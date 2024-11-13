package com.fys.project.service.impl.inner;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fys.project.common.ErrorCode;
import com.fys.project.exception.BusinessException;
import com.fys.project.service.UserInterfaceInfoService;
import com.fys.ysapicommon.model.entity.UserInterfaceInfo;
import com.fys.ysapicommon.service.InnerUserInterfaceInfoService;
import org.apache.dubbo.config.annotation.DubboService;

import javax.annotation.Resource;

/**
 * @Description 公共用户调用接口服务实现类
 * @Date 2024-11-13 13:51
 * @Author fys
 */

@DubboService
public class InnerUserInterfaceInfoServiceImpl implements InnerUserInterfaceInfoService {
    @Resource
    UserInterfaceInfoService userInterfaceInfoService;
    /**
     *用户调用接口次数+1
     * @param interfaceInfoId
     * @param userId
     **/
    @Override
    public boolean invokeCount(long interfaceInfoId, long userId) {
        return userInterfaceInfoService.invokeCount(interfaceInfoId,userId);
    }

    @Override
    public boolean existInvokeCount(long interfaceInfoId,long userId) {
        return userInterfaceInfoService.existInvokeCount(interfaceInfoId,userId);
    }
}
