package com.lc.usercenter.model.enums;

import lombok.Getter;

/**
 * 角色枚举（统一管理角色标识，避免硬编码）
 */
@Getter
public enum UserRoleEnum {
    /** 普通用户 */
    USER("USER", "普通用户"),
    /** 管理员 */
    ADMIN("ADMIN", "管理员");

    private final String code;
    private final String desc;

    UserRoleEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    // 工具方法：根据code获取枚举
    public static UserRoleEnum getByCode(String code) {
        for (UserRoleEnum role : values()) {
            if (role.code.equals(code)) {
                return role;
            }
        }
        return USER; // 默认普通用户
    }

}