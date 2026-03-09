package com.lc.usercenter.model.dto;

import com.lc.usercenter.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
* @author chun0
* @since 2026/3/9 18:54
* @version 1.0
*/
@EqualsAndHashCode(callSuper = true)
@Data
public class UserPageQueryDTO extends PageRequest {
    /**
     * 账号（唯一）
     */
    private String account;

    /**
     * 用户名/昵称
     */
    private String username;

    /**
     * 账号状态（0-启用 1-禁用）
     */
    private Integer status;


}
