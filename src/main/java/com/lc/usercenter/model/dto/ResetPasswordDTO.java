package com.lc.usercenter.model.dto;

import lombok.Data;

/**
* @author chun0
* @since 2026/3/8 17:49
* @version 1.0
*/
@Data
public class ResetPasswordDTO {
    /**
     * 旧密码
     */
    private String oldPassword;

    /**
     * 新密码
     */
    private String newPassword;
}
