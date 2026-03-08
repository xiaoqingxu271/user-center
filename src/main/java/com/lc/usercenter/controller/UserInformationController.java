package com.lc.usercenter.controller;

import com.lc.usercenter.common.BaseResponse;
import com.lc.usercenter.model.dto.ResetPasswordDTO;
import com.lc.usercenter.model.dto.UserInfoDTO;
import com.lc.usercenter.model.vo.UserInfoVO;
import com.lc.usercenter.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author chun0
 * @version 1.0
 * @since 2026/3/8 16:21
 */
@RestController
@RequestMapping("/user/information")
@Api(tags = "用户信息接口")
public class UserInformationController {
    @Resource
    private UserService userService;

    /**
     *
     * @return
     */
    @GetMapping("/get")
    @ApiOperation("获取当前用户信息")
    public BaseResponse<UserInfoVO> getCurrentUserInfo() {
        return userService.getCurrentUserInfo();
    }

    /**
     * 更新当前用户信息
     *
     * @param userInfoDTO 用户信息DTO
     * @return 更新后的用户信息VO
     */
    @PutMapping("/update")
    @ApiOperation("更新当前用户信息")
    public BaseResponse<String> updateCurrentUserInfo(@RequestBody UserInfoDTO userInfoDTO) {
        return userService.updateCurrentUserInfo(userInfoDTO);
    }

    /**
     * 重置当前用户密码
     *
     * @param resetPasswordDTO 重置密码DTO
     * @return 重置结果
     */
    @PostMapping("/resetPassword")
    @ApiOperation("重置当前用户密码")
    public BaseResponse<String> resetPassword(@RequestBody ResetPasswordDTO resetPasswordDTO) {
        return userService.resetPassword(resetPasswordDTO);
    }

}
