package com.lc.usercenter.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lc.usercenter.common.BaseResponse;
import com.lc.usercenter.common.context.BaseContext;
import com.lc.usercenter.common.properties.JwtProperties;
import com.lc.usercenter.constant.JwtClaimsConstant;
import com.lc.usercenter.constant.RedisConstant;
import com.lc.usercenter.constant.UserConstant;
import com.lc.usercenter.exception.ErrorCode;
import com.lc.usercenter.exception.ThrowUtils;
import com.lc.usercenter.mapper.UserMapper;
import com.lc.usercenter.model.dto.ResetPasswordDTO;
import com.lc.usercenter.model.dto.UserInfoDTO;
import com.lc.usercenter.model.dto.UserRegisterDTO;
import com.lc.usercenter.model.entity.User;
import com.lc.usercenter.model.vo.UserInfoVO;
import com.lc.usercenter.model.vo.UserLoginVO;
import com.lc.usercenter.model.vo.UserRegisterVO;
import com.lc.usercenter.model.vo.UserVO;
import com.lc.usercenter.service.UserService;
import com.lc.usercenter.utils.JwtUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author chun0
 * @description 针对表【user(用户表)】的数据库操作Service实现
 * @createDate 2026-03-06 14:58:00
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
        implements UserService {

    @Resource
    private UserMapper userMapper;
    @Resource
    private JwtProperties jwtProperties;
    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public BaseResponse<UserRegisterVO> register(UserRegisterDTO userRegisterDTO) {
        // 参数校验
        parameterCheck(userRegisterDTO);
        // 封装实体类
        String account = userRegisterDTO.getAccount();
        String password = userRegisterDTO.getPassword();
        String username = userRegisterDTO.getUsername();
        User user = new User();
        user.setAccount(account);
        // 密码MD5加密
        String md5Password = SecureUtil.md5(password);
        user.setPassword(md5Password);
        // 看用户名是否设置
        if (StrUtil.isEmpty(username)) {
            // 设置一个默认值
            username = UserConstant.NEW_USER_PREFIX + account;
        }
        user.setUsername(username);
        // 设置默认头像
        user.setAvatar(UserConstant.DEFAULT_AVATAR);
        // 插入数据库
        int result = userMapper.insert(user);
        ThrowUtils.throwIf(result <= 0, ErrorCode.SYSTEM_ERROR);
        // 封装返回值
        return new BaseResponse<>(ErrorCode.SUCCESS.getCode(), new UserRegisterVO(user.getId()),
                ErrorCode.SUCCESS.getMessage());
    }

    @Override
    public BaseResponse<UserLoginVO> login(UserRegisterDTO userRegisterDTO) {
        // 校验参数
        parameterCheck(userRegisterDTO);
        // 从数据库查询用户
        String account = userRegisterDTO.getAccount();
        String password = userRegisterDTO.getPassword();
        String md5Password = SecureUtil.md5(password);
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        // 设置查询条件
        userQueryWrapper.eq("account", account);
        userQueryWrapper.eq("password", md5Password);
        User user = userMapper.selectOne(userQueryWrapper);
        // 校验用户是否存在
        ThrowUtils.throwIf(user == null, ErrorCode.PARAMS_ERROR, UserConstant.USER_NOT_REGISTERED);
        // 登录成功，生成token，封装返回对象
        UserLoginVO userLoginVO = new UserLoginVO();
        Map<String, Object> claims = new HashMap<>();
        claims.put(JwtClaimsConstant.USER_ID, user.getId());
        String token = JwtUtil.createJWT(
                jwtProperties.getSecretKey(), jwtProperties.getTtl(), claims);
        // 登录成功，将token加入redis
        stringRedisTemplate.opsForValue().set(
                RedisConstant.redisTokenKey + user.getId(),
                token,
                jwtProperties.getTtl(),
                TimeUnit.MILLISECONDS
        );
        userLoginVO.setToken(token);
        UserVO userVO = new UserVO();
        userVO.setId(user.getId());
        userVO.setUsername(user.getUsername());
        userVO.setAvatar(user.getAvatar());
        userLoginVO.setUserInfo(userVO);
        return new BaseResponse<UserLoginVO>(ErrorCode.SUCCESS.getCode(), userLoginVO, ErrorCode.SUCCESS.getMessage());
    }

    @Override
    public BaseResponse<String> logout() {
        // 获取当前登录用户的id
        Long userId = BaseContext.getCurrentId();
        // 退出登录，删除redis中的token
        stringRedisTemplate.delete(RedisConstant.redisTokenKey + userId);
        // 清除ThreadLocal中的当前用户id
        BaseContext.removeCurrentId();
        return new BaseResponse<>(ErrorCode.SUCCESS.getCode(), ErrorCode.SUCCESS.getMessage());
    }

    @Override
    public BaseResponse<UserInfoVO> getCurrentUserInfo() {
        // 获取当前登录用户的id
        Long userId = BaseContext.getCurrentId();
        ThrowUtils.throwIf(ObjectUtil.isEmpty(userId), ErrorCode.PARAMS_ERROR, UserConstant.NOT_LOGIN);
        // 查询数据库
        User user = userMapper.selectById(userId);
        // 校验用户是否存在
        ThrowUtils.throwIf(ObjectUtil.isEmpty(user), ErrorCode.SYSTEM_ERROR);
        // 封装返回值
        UserInfoVO userInfoVO = new UserInfoVO();
        BeanUtil.copyProperties(user, userInfoVO);
        // 返回结果
        return new BaseResponse<>(ErrorCode.SUCCESS.getCode(), userInfoVO, ErrorCode.SUCCESS.getMessage());
    }

    @Override
    public BaseResponse<String> updateCurrentUserInfo(UserInfoDTO userInfoDTO) {
        // 参数校验
        ThrowUtils.throwIf(ObjectUtil.isEmpty(userInfoDTO), ErrorCode.PARAMS_ERROR);
        // 获取当前用户id
        Long userId = BaseContext.getCurrentId();
        // 构造更新的lambdaUpdateWrapper
        LambdaUpdateWrapper<User> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.set(StrUtil.isNotEmpty(userInfoDTO.getUsername()), User::getUsername, userInfoDTO.getUsername());
        lambdaUpdateWrapper.set(ObjectUtil.isNotEmpty(userInfoDTO.getGender()), User::getGender, userInfoDTO.getGender());
        lambdaUpdateWrapper.set(StrUtil.isNotEmpty(userInfoDTO.getAvatar()), User::getAvatar, userInfoDTO.getAvatar());
        lambdaUpdateWrapper.eq(ObjectUtil.isNotEmpty(userId), User::getId, userId);
        // 更新数据库
        int result = userMapper.update(lambdaUpdateWrapper);
        ThrowUtils.throwIf(result <= 0, ErrorCode.SYSTEM_ERROR);
        // 返回结果
        return new BaseResponse<>(ErrorCode.SUCCESS.getCode(), ErrorCode.SUCCESS.getMessage());
    }

    @Override
    public BaseResponse<String> resetPassword(ResetPasswordDTO resetPasswordDTO) {
        // 参数校验
        ThrowUtils.throwIf(ObjectUtil.isEmpty(resetPasswordDTO), ErrorCode.PARAMS_ERROR);
        // 获取当前登录用户的id
        Long userId = BaseContext.getCurrentId();
        // 校验旧密码是否正确
        User user = userMapper.selectById(userId);
        ThrowUtils.throwIf(ObjectUtil.isEmpty(user), ErrorCode.SYSTEM_ERROR);
        String oldPassword = resetPasswordDTO.getOldPassword();
        String md5OldPassword = SecureUtil.md5(oldPassword);
        ThrowUtils.throwIf(!md5OldPassword.equals(user.getPassword()), ErrorCode.PARAMS_ERROR, UserConstant.OLD_PASSWORD_ERROR);
        // 校验新密码格式
        String newPassword = resetPasswordDTO.getNewPassword();
        ThrowUtils.throwIf(StrUtil.isEmpty(newPassword), ErrorCode.PARAMS_ERROR);
        // 校验新密码是否与旧密码相同
        ThrowUtils.throwIf(newPassword.equals(oldPassword), ErrorCode.PARAMS_ERROR, UserConstant.NEW_PASSWORD_CANNOT_BE_OLD_PASSWORD);
        // 校验新密码长度是否符合要求
        ThrowUtils.throwIf(newPassword.length() < 6 || newPassword.length() > 8, ErrorCode.PARAMS_ERROR, UserConstant.PASSWORD_LENGTH_NOT_IN_RANGE);
        // 更新数据库
        // 构造更新的lambdaUpdateWrapper
        LambdaUpdateWrapper<User> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.set(User::getPassword, SecureUtil.md5(newPassword));
        lambdaUpdateWrapper.eq(ObjectUtil.isNotEmpty(userId), User::getId, userId);
        // 更新数据库
        int result = userMapper.update(lambdaUpdateWrapper);
        ThrowUtils.throwIf(result <= 0, ErrorCode.SYSTEM_ERROR);
        // 删除redis中的token
        stringRedisTemplate.delete(RedisConstant.redisTokenKey + userId);
        // 返回结果
        return new BaseResponse<>(ErrorCode.SUCCESS.getCode(), ErrorCode.SUCCESS.getMessage());
    }

    /**
     * 校验用户注册和登录参数
     *
     * @param userRegisterDTO 用户注册DTO
     */
    private void parameterCheck(UserRegisterDTO userRegisterDTO) {
        String account = userRegisterDTO.getAccount();
        String password = userRegisterDTO.getPassword();
        String username = userRegisterDTO.getUsername();
        // 账号和密码不能为空
        ThrowUtils.throwIf(StrUtil.isEmpty(account) || StrUtil.isEmpty(password), ErrorCode.PARAMS_ERROR,
                UserConstant.ACCOUNT_OR_PASSWORD_NOT_NULL);
        // 账号不能不含特殊字符
        ThrowUtils.throwIf(!account.matches(UserConstant.ACCOUNT_OR_PASSWORD_REGEX) || !password.matches(UserConstant.ACCOUNT_OR_PASSWORD_REGEX), ErrorCode.PARAMS_ERROR, UserConstant.ACCOUNT_OR_PASSWORD_NOT_CONTAIN_SPECIAL_CHAR);
        // 账号长度为11位
        ThrowUtils.throwIf(account.length() != 11, ErrorCode.PARAMS_ERROR, UserConstant.ACCOUNT_LENGTH_NOT_IN_RANGE);
        // 密码长度为6~8位
        ThrowUtils.throwIf(password.length() < 6 || password.length() > 8, ErrorCode.PARAMS_ERROR,
                UserConstant.PASSWORD_LENGTH_NOT_IN_RANGE);
        // 账号不为空的情况下，用户名长度为6~8位
        ThrowUtils.throwIf(!StrUtil.isEmpty(username) && (username.length() < 6 || username.length() > 8),
                ErrorCode.PARAMS_ERROR, UserConstant.USERNAME_LENGTH_NOT_IN_RANGE);
    }
}




