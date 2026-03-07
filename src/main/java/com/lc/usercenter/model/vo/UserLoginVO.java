package com.lc.usercenter.model.vo;

import lombok.Data;

/**
* @author chun0
* @since 2026/3/7 13:02
* @version 1.0
*/
@Data
public class UserLoginVO {
    /**
    * 登录凭证（JWT）
    */
    private String token;

    /**
     * 用户信息
     */
    private UserVO userInfo;
}
