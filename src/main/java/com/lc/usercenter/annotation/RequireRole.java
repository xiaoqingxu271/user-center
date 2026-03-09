package com.lc.usercenter.annotation;

import com.lc.usercenter.model.enums.UserRoleEnum;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 自定义权限注解：标注需要角色校验的接口方法
 */
@Target({ElementType.METHOD}) // 仅作用于方法
@Retention(RetentionPolicy.RUNTIME) // 运行时生效（AOP可解析）
public @interface RequireRole {
    /**
     * 需要的角色列表（支持多个，如{ADMIN, USER}）
     */
    UserRoleEnum[] value() default {UserRoleEnum.USER};

    /**
     * 无权限时的提示信息
     */
    String message() default "无操作权限，请联系管理员";
}