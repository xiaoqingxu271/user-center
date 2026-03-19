package com.lc.usercenter.model.vo;

import lombok.Data;

/**
* @author chun0
* @since 2026/3/7 13:04
* @version 1.0
*/
@Data
public class UserVO {
    /**
     * 主键ID
     */
    private Long id;

    /**
     * 用户名/昵称
     */
    private String username;

    /**
     * 头像URL
     */
    private String avatar;

    /**
     * 角色标识（USER-普通用户，ADMIN-管理员）
     */
    private String role;
}
