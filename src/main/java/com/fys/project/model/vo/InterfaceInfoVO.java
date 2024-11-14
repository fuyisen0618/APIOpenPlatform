package com.fys.project.model.vo;

import com.fys.ysapicommon.model.entity.InterfaceInfo;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 接口信息封装视图
 *
 * @TableName user_interface_info
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class InterfaceInfoVO extends InterfaceInfo {
    /**
     *  调用次数
     */
    private Integer totalNum;

    private static final long serialVersionUID = 1L;
}