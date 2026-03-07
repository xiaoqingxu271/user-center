package com.lc.usercenter.controller;

import cn.hutool.core.util.ObjectUtil;
import com.lc.usercenter.common.BaseResponse;
import com.lc.usercenter.exception.ErrorCode;
import com.lc.usercenter.exception.ThrowUtils;
import com.lc.usercenter.model.dto.UserRegisterDTO;
import com.lc.usercenter.model.vo.UserLoginVO;
import com.lc.usercenter.model.vo.UserRegisterVO;
import com.lc.usercenter.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
* @author chun0
* @since 2026/3/6 16:02
* @version 1.0
*/
@RestController
@RequestMapping("/user/authentication")
@Api(tags = "用户认证接口")
public class UserAuthenticationController {

    @Resource
    private UserService userService;

    /**
     * 用户注册
     * @param userRegisterDTO 用户注册DTO
     * @return 注册成功后的用户VO
     */
    @PostMapping("/register")
    @ApiOperation("用户注册")
    public BaseResponse<UserRegisterVO> register(UserRegisterDTO userRegisterDTO) {
        // 参数校验
        ThrowUtils.throwIf(ObjectUtil.isEmpty(userRegisterDTO), ErrorCode.PARAMS_ERROR);
        return userService.register(userRegisterDTO);
    }

    /**
     * 用户登录
     * @param userRegisterDTO 用户登录DTO
     * @return 登录成功后的用户VO
     */
    @PostMapping("/login")
    @ApiOperation("用户登录")
    public BaseResponse<UserLoginVO> login(UserRegisterDTO userRegisterDTO) {
        // 参数校验
        ThrowUtils.throwIf(ObjectUtil.isEmpty(userRegisterDTO), ErrorCode.PARAMS_ERROR);
        return userService.login(userRegisterDTO);
    }

    /**
     * 用户退出登录
     * @return 退出登录成功后的提示信息
     */
    @PostMapping("/logout")
    @ApiOperation("用户退出登录")
    public BaseResponse<String> logout() {
        return userService.logout();
    }
}
