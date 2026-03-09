package com.lc.usercenter.model.vo;

import lombok.Data;

import java.util.List;

/**
* @author chun0
* @since 2026/3/9 18:58
* @version 1.0
*/
@Data
public class UserPageResultVO {

    /**
     * 总记录数
     */
    private Long total;
    /**
     * 用户信息VO列表
     */
    private List<UserInfoVO> userInfoVOList;
}
