package com.lc.usercenter.controller;

import com.lc.usercenter.annotation.RequireRole;
import com.lc.usercenter.common.BaseResponse;
import com.lc.usercenter.model.dto.ResetPasswordDTO;
import com.lc.usercenter.model.dto.UserInfoDTO;
import com.lc.usercenter.model.dto.UserPageQueryDTO;
import com.lc.usercenter.model.enums.UserRoleEnum;
import com.lc.usercenter.model.vo.UserInfoVO;
import com.lc.usercenter.model.vo.UserPageResultVO;
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
     * 获取当前用户信息
     *
     * @return 当前用户信息VO
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

    /**
     * 获取用户分页列表
     *
     * @param userPageQueryDTO 用户分页查询DTO
     * @return 用户分页结果VO
     */
    @PostMapping("/page")
    @ApiOperation("获取用户分页列表")
    @RequireRole(value = UserRoleEnum.ADMIN)
    public BaseResponse<UserPageResultVO> getUserPage(@RequestBody UserPageQueryDTO userPageQueryDTO) {
        return userService.getUserPage(userPageQueryDTO);
    }

    /**
     * 根据ID获取用户信息
     *
     * @param id 用户ID
     * @return 用户信息VO
     */
    @GetMapping("/{id}")
    @ApiOperation("根据ID获取用户信息")
    @RequireRole(value = UserRoleEnum.ADMIN)
    public BaseResponse<UserInfoVO> getUserInfoById(@PathVariable Long id) {
        return userService.getUserInfoById(id);
    }


    /**
     * 启用/禁用用户
     *
     * @param id     用户ID
     * @param status 状态值（0-启用 1-禁用）
     * @return 操作结果
     */
    @PostMapping("/startOrUp/{id}/{status}")
    @ApiOperation("启用/禁用用户")
    @RequireRole(value = UserRoleEnum.ADMIN)
    public BaseResponse<String> startOrUpUser(@PathVariable Long id, @PathVariable Integer status) {
        return userService.startOrUp(id, status);
    }

    /**
     * 删除用户（逻辑删除）
     *
     * @param ids 用户ID数组
     * @return 删除结果
     */
    @DeleteMapping("/delete/{ids}")
    @ApiOperation("删除用户（逻辑删除）")
    @RequireRole(value = UserRoleEnum.ADMIN)
    public BaseResponse<String> deleteUser(@PathVariable String ids) {
        return userService.deleteUserBatch(ids);
    }

}
