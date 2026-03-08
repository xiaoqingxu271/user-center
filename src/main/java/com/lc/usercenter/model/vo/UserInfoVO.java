package com.lc.usercenter.model.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * @author chun0
 * @version 1.0
 * @since 2026/3/8 16:05
 */
@Data
public class UserInfoVO extends UserVO {
    /**
     * 账号（唯一）
     */
    private String account;

    /**
     * 性别（0-男 1-女）
     */
    private Integer gender;

    /**
     * 账号状态（0-启用 1-禁用）
     */
    private Integer status;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

}
