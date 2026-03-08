package com.lc.usercenter.model.dto;

import lombok.Data;

/**
* @author chun0
* @since 2026/3/8 17:05
* @version 1.0
*/
@Data
public class UserInfoDTO {
    /**
     * 用户名/昵称
     */
    private String username;

    /**
     * 性别（0-男 1-女）
     */
    private Integer gender;

    /**
     * 头像URL
     */
    private String avatar;

}
