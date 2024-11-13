package com.fys.project.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fys.project.annotation.AuthCheck;
import com.fys.project.common.BaseResponse;
import com.fys.project.common.DeleteRequest;
import com.fys.project.common.ErrorCode;
import com.fys.project.common.ResultUtils;
import com.fys.project.constant.CommonConstant;
import com.fys.project.constant.UserConstant;
import com.fys.project.exception.BusinessException;
import com.fys.project.model.dto.interfaceInfo.InterfaceInfoQueryRequest;
import com.fys.project.model.dto.userInterfaceInfo.UserInterfaceInfoAddRequest;
import com.fys.project.model.dto.userInterfaceInfo.UserInterfaceInfoQueryRequest;
import com.fys.project.model.dto.userInterfaceInfo.UserInterfaceInfoUpdateRequest;
import com.fys.ysapicommon.model.entity.User;
import com.fys.project.service.UserInterfaceInfoService;
import com.fys.project.service.UserService;
import com.fys.ysapicommon.model.entity.UserInterfaceInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 接口管理
 *
 * @author fys
 */
@RestController
@RequestMapping("/userInterfaceInfo")
@Slf4j
public class UserInterfaceInfoController {

    @Resource
    private UserInterfaceInfoService userInterfaceinfoService;

    @Resource
    private UserService userService;

    // region 增删改查

    /**
     * 创建
     *
     * @param userInterfaceinfoAddRequest
     * @param request
     * @return
     */
    @PostMapping("/add")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Long> addInterfaceInfo(@RequestBody UserInterfaceInfoAddRequest userInterfaceinfoAddRequest, HttpServletRequest request) {
        if (userInterfaceinfoAddRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        UserInterfaceInfo userInterfaceInfo = new UserInterfaceInfo();
        BeanUtils.copyProperties(userInterfaceinfoAddRequest, userInterfaceInfo);
        // 校验
        userInterfaceinfoService.validUserInterfaceInfo(userInterfaceInfo, true);
        User loginUser = userService.getLoginUser(request);
        userInterfaceInfo.setUserId(loginUser.getId());
        boolean result = userInterfaceinfoService.save(userInterfaceInfo);
        if (!result) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR);
        }
        long newInterfaceInfoId = userInterfaceInfo.getId();
        return ResultUtils.success(newInterfaceInfoId);
    }

    /**
     * 删除
     *
     * @param deleteRequest
     * @param request
     * @return
     */
    @PostMapping("/delete")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> deleteInterfaceInfo(@RequestBody DeleteRequest deleteRequest, HttpServletRequest request) {
        if (deleteRequest == null || deleteRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = userService.getLoginUser(request);
        long id = deleteRequest.getId();
        // 判断是否存在
        UserInterfaceInfo oldInterfaceInfo = userInterfaceinfoService.getById(id);
        if (oldInterfaceInfo == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        // 仅本人或管理员可删除
        if (!oldInterfaceInfo.getUserId().equals(user.getId()) && !userService.isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        boolean b = userInterfaceinfoService.removeById(id);
        return ResultUtils.success(b);
    }

    /**
     * 更新
     *
     * @param userInterfaceinfoUpdateRequest
     * @param request
     * @return
     */
    @PostMapping("/update")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> updateInterfaceInfo(@RequestBody UserInterfaceInfoUpdateRequest userInterfaceinfoUpdateRequest,
                                            HttpServletRequest request) {
        if (userInterfaceinfoUpdateRequest == null || userInterfaceinfoUpdateRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        UserInterfaceInfo userInterfaceInfo = new UserInterfaceInfo();
        BeanUtils.copyProperties(userInterfaceinfoUpdateRequest, userInterfaceInfo);
        // 参数校验
        userInterfaceinfoService.validUserInterfaceInfo(userInterfaceInfo, false);
        User user = userService.getLoginUser(request);
        long id = userInterfaceinfoUpdateRequest.getId();
        // 判断是否存在
        UserInterfaceInfo oldInterfaceInfo = userInterfaceinfoService.getById(id);
        if (oldInterfaceInfo == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        // 仅本人或管理员可修改
        if (!oldInterfaceInfo.getUserId().equals(user.getId()) && !userService.isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        boolean result = userInterfaceinfoService.updateById(userInterfaceInfo);
        return ResultUtils.success(result);
    }

    /**
     * 根据 id 获取
     *
     * @param id
     * @return
     */
    @GetMapping("/get")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<UserInterfaceInfo> getInterfaceInfoById(long id) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        UserInterfaceInfo userInterfaceInfo = userInterfaceinfoService.getById(id);
        return ResultUtils.success(userInterfaceInfo);
    }

    /**
     * 获取列表（仅管理员可使用）
     *
     * @param userInterfaceinfoQueryRequest
     * @return
     */
    @AuthCheck(mustRole = "admin")
    @GetMapping("/list")
    public BaseResponse<List<UserInterfaceInfo>> listInterfaceInfo(InterfaceInfoQueryRequest userInterfaceinfoQueryRequest) {
        UserInterfaceInfo userInterfaceinfoQuery = new UserInterfaceInfo();
        if (userInterfaceinfoQueryRequest != null) {
            BeanUtils.copyProperties(userInterfaceinfoQueryRequest, userInterfaceinfoQuery);
        }
        QueryWrapper<UserInterfaceInfo> queryWrapper = new QueryWrapper<>(userInterfaceinfoQuery);
        List<UserInterfaceInfo> userInterfaceinfoList = userInterfaceinfoService.list(queryWrapper);
        return ResultUtils.success(userInterfaceinfoList);
    }

    /**
     * 分页获取列表
     *
     * @param userInterfaceinfoQueryRequest
     * @param request
     * @return
     */
    @GetMapping("/list/page")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Page<UserInterfaceInfo>> listInterfaceInfoByPage(UserInterfaceInfoQueryRequest userInterfaceinfoQueryRequest, HttpServletRequest request) {
        if (userInterfaceinfoQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        UserInterfaceInfo userInterfaceinfoQuery = new UserInterfaceInfo();
        BeanUtils.copyProperties(userInterfaceinfoQueryRequest, userInterfaceinfoQuery);
        long current = userInterfaceinfoQueryRequest.getCurrent();
        long size = userInterfaceinfoQueryRequest.getPageSize();
        String sortField = userInterfaceinfoQueryRequest.getSortField();
        String sortOrder = userInterfaceinfoQueryRequest.getSortOrder();
        // 限制爬虫
        if (size > 50) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        QueryWrapper<UserInterfaceInfo> queryWrapper = new QueryWrapper<>(userInterfaceinfoQuery);
        queryWrapper.orderBy(StringUtils.isNotBlank(sortField),
                sortOrder.equals(CommonConstant.SORT_ORDER_ASC), sortField);
        Page<UserInterfaceInfo> userInterfaceinfoPage = userInterfaceinfoService.page(new Page<>(current, size), queryWrapper);
        return ResultUtils.success(userInterfaceinfoPage);
    }

    // endregion


}
