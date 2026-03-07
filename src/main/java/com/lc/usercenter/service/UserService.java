package com.lc.usercenter.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lc.usercenter.common.BaseResponse;
import com.lc.usercenter.model.dto.UserRegisterDTO;
import com.lc.usercenter.model.entity.User;
import com.lc.usercenter.model.vo.UserLoginVO;
import com.lc.usercenter.model.vo.UserRegisterVO;

/**
* @author chun0
* @description 针对表【user(用户表)】的数据库操作Service
* @createDate 2026-03-06 14:58:00
*/
public interface UserService extends IService<User> {

    /**
     * 用户注册
     * @param userRegisterDTO 用户注册DTO
     * @return 用户VO
     */
    BaseResponse<UserRegisterVO> register(UserRegisterDTO userRegisterDTO);

    /**
     * 用户登录
     * @param userRegisterDTO 用户登录DTO
     * @return 用户登录VO
     */
    BaseResponse<UserLoginVO> login(UserRegisterDTO userRegisterDTO);

    /**
     * 退出登录
     * @return 用户成功退出登录
     */
    BaseResponse<String> logout();
}
