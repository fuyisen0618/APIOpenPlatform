package com.fys.project.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fys.project.annotation.AuthCheck;
import com.fys.project.common.BaseResponse;
import com.fys.project.common.ErrorCode;
import com.fys.project.common.ResultUtils;
import com.fys.project.exception.BusinessException;
import com.fys.project.mapper.UserInterfaceInfoMapper;
import com.fys.project.model.vo.InterfaceInfoVO;
import com.fys.project.service.InterfaceInfoService;
import com.fys.ysapicommon.model.entity.InterfaceInfo;
import com.fys.ysapicommon.model.entity.UserInterfaceInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Description 统计通用次数
 * @Date 2024-11-14 14:00
 * @Author fys
 */

@RestController
@RequestMapping("/analysis")
@Slf4j
public class AnalysisController {
    @Resource
    UserInterfaceInfoMapper userInterfaceInfoMapper;
    @Resource
    InterfaceInfoService interfaceInfoService;
    @GetMapping("/top/interface/invoke")
    @AuthCheck(mustRole = "admin")
    public BaseResponse<List<InterfaceInfoVO>> listTopInvokeInterfaceInfo(){
        List<UserInterfaceInfo> userInterfaceInfos = userInterfaceInfoMapper.ListTopInvokeInterfaceInfo(3);
        QueryWrapper<InterfaceInfo> queryWrapper = new QueryWrapper<>();
        Map<Long,List<UserInterfaceInfo>> interfaceInfoIdObjMap = userInterfaceInfos.stream()
                .collect(Collectors.groupingBy(UserInterfaceInfo::getInterfaceInfoId));
        queryWrapper.in("id",interfaceInfoIdObjMap.keySet());
        List<InterfaceInfo> list = interfaceInfoService.list(queryWrapper);
        if(CollectionUtils.isEmpty(list)){
            throw new BusinessException(ErrorCode.SYSTEM_ERROR);
        }
        List<InterfaceInfoVO> interfaceInfoVOS = list.stream().map(interfaceInfo -> {
            InterfaceInfoVO interfaceInfoVO = new InterfaceInfoVO();
            BeanUtils.copyProperties(interfaceInfo,interfaceInfoVO);
            int totalNum=interfaceInfoIdObjMap.get(interfaceInfo.getId()).get(0).getTotalNum();
            interfaceInfoVO.setTotalNum(totalNum);
            return interfaceInfoVO;
        }).collect(Collectors.toList());
        return ResultUtils.success(interfaceInfoVOS);
    }
}
