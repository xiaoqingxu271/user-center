package com.lc.usercenter.model.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author chun0
 * @version 1.0
 * @since 2026/3/10 11:53
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class AddUserDTO extends UserInfoDTO {
    /**
     * 账号
     */
    private String account;

    /**
     * 密码
     */
    private String password;

    /**
     * 角色标识（USER-普通用户，ADMIN-管理员）
     */
    private String role;

}
