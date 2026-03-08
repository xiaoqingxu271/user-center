package com.lc.usercenter.constant;

/**
 * @author chun0
 * @version 1.0
 * @since 2026/3/7 13:22
 */
public class UserConstant {
    /**
     * 账号或密码不能为空
     */
    public static final String ACCOUNT_OR_PASSWORD_NOT_NULL = "账号或密码不能为空";
    /**
     * 账号或密码不能含有特殊字符
     */
    public static final String ACCOUNT_OR_PASSWORD_NOT_CONTAIN_SPECIAL_CHAR = "账号或密码不能含有特殊字符";
    /**
     * 账号或密码正则表达式
     */
    public static final String ACCOUNT_OR_PASSWORD_REGEX = "^[a-zA-Z0-9_]+$";
    /**
     * 密码长度为6~8位
     */
    public static final String PASSWORD_LENGTH_NOT_IN_RANGE = "密码长度为6~8位";
    /**
     * 账号长度为11位
     */
    public static final String ACCOUNT_LENGTH_NOT_IN_RANGE = "账号长度为11位";
    /**
     * 用户名长度为6~8位
     */
    public static final String USERNAME_LENGTH_NOT_IN_RANGE = "用户名长度为6~8位";
    /**
     * 新用户前缀
     */
    public static final String NEW_USER_PREFIX = "新用户";
    /**
     * 默认头像URL
     */
    public static final String DEFAULT_AVATAR = "https://www.codefather.cn/logo.png";
    /**
     * 用户未注册
     */
    public static final String USER_NOT_REGISTERED = "用户未注册";
    /**
     * 用户未登录
     */
    public static final String NOT_LOGIN = "用户未登录";
    /**
     * 旧密码错误
     */
    public static final String OLD_PASSWORD_ERROR = "旧密码错误";
    /**
     * 新密码不能与旧密码相同
     */
    public static final String NEW_PASSWORD_CANNOT_BE_OLD_PASSWORD = "新密码不能与旧密码相同";
}
