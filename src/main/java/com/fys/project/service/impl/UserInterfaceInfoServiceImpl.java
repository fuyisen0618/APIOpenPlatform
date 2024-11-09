package com.fys.project.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fys.project.common.ErrorCode;
import com.fys.project.exception.BusinessException;
import com.fys.project.mapper.UserInterfaceInfoMapper;
import com.fys.project.model.entity.UserInterfaceInfo;
import com.fys.project.service.UserInterfaceInfoService;
import org.springframework.stereotype.Service;


/**
* @author fys
* @description 针对表【user_interface_info(用户调用接口关系)】的数据库操作Service实现
* @createDate 2024-11-09 15:50:26
*/
@Service
public class UserInterfaceInfoServiceImpl extends ServiceImpl<UserInterfaceInfoMapper, UserInterfaceInfo>
    implements UserInterfaceInfoService {
    public void validUserInterfaceInfo(UserInterfaceInfo userInterfaceInfo, boolean add) {
        Long id = userInterfaceInfo.getId();
        Long userId = userInterfaceInfo.getUserId();
        Integer leftNum = userInterfaceInfo.getLeftNum();
        if (userInterfaceInfo == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        if (add) {
            if (id<=0 || userId<=0) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR,"接口或用户不存在");
            }
        }
        if (leftNum<0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "名称过长");
        }
    }

    @Override
    public boolean invokeCount(long interfaceInfoId, long userId) {
        if(interfaceInfoId<=0 || userId<=0){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        UpdateWrapper<UserInterfaceInfo> updateWrapper = new UpdateWrapper<>();
        updateWrapper.lambda().eq(UserInterfaceInfo::getInterfaceInfoId,interfaceInfoId).eq(UserInterfaceInfo::getUserId,userId);
        updateWrapper.setSql("leftNum=leftNum-1, totalNum=totalNum+1");
        return this.update(updateWrapper);
    }
}




