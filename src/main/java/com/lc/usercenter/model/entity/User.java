package com.lc.usercenter.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 用户表
 * @TableName user
 */
@TableName(value ="user")
@Data
public class User {
    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

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

    /**
     * 性别（0-未知 1-男 2-女）
     */
    private Integer gender;

    /**
     * 头像URL
     */
    private String avatar;

    /**
     * 账号状态（0-启用 1-禁用）
     */
    private Integer status;

    /**
     * 角色标识（USER-普通用户，ADMIN-管理员）
     */
    private String role;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 创建人ID
     */
    private Long createUserId;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 更新人ID
     */
    private Long updateUserId;

    /**
     * 是否删除（0-未删除 1-已删除）
     */
    private Integer isDeleted;
}