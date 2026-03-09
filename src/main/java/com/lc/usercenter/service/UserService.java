package com.lc.usercenter.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lc.usercenter.common.BaseResponse;
import com.lc.usercenter.model.dto.ResetPasswordDTO;
import com.lc.usercenter.model.dto.UserInfoDTO;
import com.lc.usercenter.model.dto.UserPageQueryDTO;
import com.lc.usercenter.model.dto.UserRegisterDTO;
import com.lc.usercenter.model.entity.User;
import com.lc.usercenter.model.vo.UserInfoVO;
import com.lc.usercenter.model.vo.UserLoginVO;
import com.lc.usercenter.model.vo.UserPageResultVO;
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

     /**
      * 根据ID获取当前用户信息
      * @return 当前用户
      */
     User getCurrentUserById(Long id);

     /**
      * 获取用户分页列表
      * @param userPageQueryDTO 用户分页查询DTO
      * @return 用户分页列表VO
      */
     BaseResponse<UserPageResultVO> getUserPage(UserPageQueryDTO userPageQueryDTO);


     /**
      * 根据ID获取用户信息
      * @param id 用户ID
      * @return 用户信息VO
      */
     BaseResponse<UserInfoVO> getUserInfoById(Long id);

    /**
     * 启用/禁用用户账号
     * @param id 用户ID
     * @param status 账号状态（0-启用 1-禁用）
     * @return 操作结果
     */
     BaseResponse<String> startOrUp(Long id, Integer status);

     /**
      * 批量删除用户账号
      * @param ids 用户ID列表
      * @return 操作结果
      */
     BaseResponse<String> deleteUserBatch(String ids);
}
