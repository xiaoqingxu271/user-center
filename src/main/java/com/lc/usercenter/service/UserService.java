package com.lc.usercenter.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lc.usercenter.common.BaseResponse;
import com.lc.usercenter.model.dto.ResetPasswordDTO;
import com.lc.usercenter.model.dto.UserInfoDTO;
import com.lc.usercenter.model.dto.UserRegisterDTO;
import com.lc.usercenter.model.entity.User;
import com.lc.usercenter.model.vo.UserInfoVO;
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

    /**
     * 获取个人用户信息
     * @return 用户信息VO
     */
    BaseResponse<UserInfoVO> getCurrentUserInfo();

    /**
     * 更新当前用户信息
     * @param userInfoDTO 用户信息DTO
     * @return 更新后的用户信息VO
     */
    BaseResponse<String> updateCurrentUserInfo(UserInfoDTO userInfoDTO);

    /**
     * 修改当前用户密码
     */
     BaseResponse<String> resetPassword(ResetPasswordDTO resetPasswordDTO);
}
