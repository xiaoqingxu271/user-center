package com.lc.usercenter.model.dto;

import lombok.Data;

/**
* @author chun0
* @since 2026/3/6 15:04
* @version 1.0
*/
@Data
public class UserRegisterDTO {
    /**
     * 账号（唯一）
     */
    private String account;

    /**
     * 密码（加密存储）
     */
    private String password;

    /**
     * 用户名/昵称
     */
    private String username;
}
