package com.fys.project.service.impl.inner;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fys.project.common.ErrorCode;
import com.fys.project.exception.BusinessException;
import com.fys.project.mapper.UserInterfaceInfoMapper;
import com.fys.project.mapper.UserMapper;
import com.fys.ysapicommon.model.entity.User;
import com.fys.ysapicommon.service.InnerUserService;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboService;

import javax.annotation.Resource;

/**
 * @Description 公共用户服务实现类
 * @Date 2024-11-13 13:52
 * @Author fys
 */

@DubboService
public class InnerUserServiceImpl implements InnerUserService {

    @Resource
    private UserMapper userMapper;

    /**
     *根据密钥获取用户信息
     * @param accessKey
     **/
    @Override
    public User getInvokeUser(String accessKey) {
        if(StringUtils.isAnyBlank(accessKey)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("accessKey",accessKey);
        return userMapper.selectOne(queryWrapper);
    }
}
