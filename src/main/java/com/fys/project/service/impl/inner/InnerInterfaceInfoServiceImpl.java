package com.fys.project.service.impl.inner;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fys.project.common.ErrorCode;
import com.fys.project.exception.BusinessException;
import com.fys.project.mapper.InterfaceInfoMapper;
import com.fys.ysapicommon.model.entity.InterfaceInfo;
import com.fys.ysapicommon.service.InnerInterfaceInfoService;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboService;

import javax.annotation.Resource;

/**
 * @Description 公共接口服务实现类
 * @Date 2024-11-13 13:47
 * @Author fys
 */

@DubboService
public class InnerInterfaceInfoServiceImpl implements InnerInterfaceInfoService {

    @Resource
    private InterfaceInfoMapper interfaceInfoMapper;
    /**
     *根据接口路径和方法名查询接口是否存在
     * @param url
     * @param method
     **/
    @Override
    public InterfaceInfo getInterfaceInfo(String url, String method) {
        if (StringUtils.isAnyBlank(url, method)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        QueryWrapper<InterfaceInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(InterfaceInfo::getUrl,url).eq(InterfaceInfo::getMethod,method);
        return interfaceInfoMapper.selectOne(queryWrapper);
    }
}
