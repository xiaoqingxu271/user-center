package com.lc.usercenter.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lc.usercenter.common.BaseResponse;
import com.lc.usercenter.common.ResultUtils;
import com.lc.usercenter.common.context.BaseContext;
import com.lc.usercenter.common.properties.JwtProperties;
import com.lc.usercenter.constant.JwtClaimsConstant;
import com.lc.usercenter.constant.RedisConstant;
import com.lc.usercenter.constant.UserConstant;
import com.lc.usercenter.exception.BusinessException;
import com.lc.usercenter.exception.ErrorCode;
import com.lc.usercenter.exception.ThrowUtils;
import com.lc.usercenter.mapper.UserMapper;
import com.lc.usercenter.model.dto.*;
import com.lc.usercenter.model.entity.User;
import com.lc.usercenter.model.vo.*;
import com.lc.usercenter.service.UserService;
import com.lc.usercenter.utils.JwtUtil;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

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
        return ResultUtils.success(new UserRegisterVO(user.getId()));
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
        User user = userMapper.selectOne(userQueryWrapper);
        // 校验用户是否存在
        ThrowUtils.throwIf(user == null, ErrorCode.PARAMS_ERROR, UserConstant.USER_NOT_REGISTERED);
        // 校验密码是否正确
        ThrowUtils.throwIf(!user.getPassword().equals(md5Password), ErrorCode.PARAMS_ERROR, UserConstant.PASSWORD_ERROR);
        // 校验账号状态
        ThrowUtils.throwIf(user.getStatus() == 1, ErrorCode.PARAMS_ERROR, UserConstant.USER_DISABLED);
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
        return ResultUtils.success(userLoginVO);
    }

    @Override
    public BaseResponse<String> logout() {
        // 获取当前登录用户的id
        Long userId = BaseContext.getCurrentId();
        // 退出登录，删除redis中的token
        stringRedisTemplate.delete(RedisConstant.redisTokenKey + userId);
        // 清除ThreadLocal中的当前用户id
        BaseContext.removeCurrentId();
        return ResultUtils.success(ErrorCode.SUCCESS.getMessage());
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
        return ResultUtils.success(userInfoVO);
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
        return ResultUtils.success(ErrorCode.SUCCESS.getMessage());
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
        return ResultUtils.success(ErrorCode.SUCCESS.getMessage());
    }

    @Override
    public User getCurrentUserById(Long id) {
        // 校验参数非空
        ThrowUtils.throwIf(ObjectUtil.isEmpty(id), ErrorCode.PARAMS_ERROR, UserConstant.NOT_LOGIN);
        return userMapper.selectById(id);
    }

    @Override
    public BaseResponse<UserPageResultVO> getUserPage(UserPageQueryDTO userPageQueryDTO) {
        // 校验参数非空
        ThrowUtils.throwIf(ObjectUtil.isEmpty(userPageQueryDTO), ErrorCode.PARAMS_ERROR);
        // 获取当前页数和每页记录数
        int currentPage = userPageQueryDTO.getCurrent();
        int pageSize = userPageQueryDTO.getPageSize();
        // 构造分页条件
        String account = userPageQueryDTO.getAccount();
        String username = userPageQueryDTO.getUsername();
        Integer status = userPageQueryDTO.getStatus();
        String sortField = userPageQueryDTO.getSortField();
        String sortOrder = userPageQueryDTO.getSortOrder();
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.like(StrUtil.isNotEmpty(account), "account", account);
        userQueryWrapper.like(StrUtil.isNotEmpty(username), "username", username);
        userQueryWrapper.eq(ObjectUtil.isNotEmpty(status), "status", status);
        userQueryWrapper.orderBy(StrUtil.isNotEmpty(sortField), sortOrder.equalsIgnoreCase("asc"), sortField);
        Page<User> userPage = userMapper.selectPage(new Page<>(currentPage, pageSize), userQueryWrapper);
        // 封装对象
        UserPageResultVO userPageResultVO = new UserPageResultVO();
        userPageResultVO.setTotal(userPage.getTotal());
        // 转换为VO列表
        List<UserInfoVO> userInfoVOList = userPage.getRecords().stream()
                .map(user -> BeanUtil.copyProperties(user, UserInfoVO.class))
                .collect(Collectors.toList());
        userPageResultVO.setUserInfoVOList(userInfoVOList);
        return ResultUtils.success(userPageResultVO);
    }

    @Override
    public BaseResponse<UserInfoVO> getUserInfoById(Long id) {
        // 校验参数非空
        ThrowUtils.throwIf(ObjectUtil.isEmpty(id), ErrorCode.PARAMS_ERROR);
        // 查询数据库
        User user = userMapper.selectById(id);
        // 封装VO对象
        UserInfoVO userInfoVO = BeanUtil.copyProperties(user, UserInfoVO.class);
        return ResultUtils.success(userInfoVO);
    }

    @Override
    public BaseResponse<String> modifyUser(ModifyUserDTO modifyUserDTO) {
        // 校验参数非空
        ThrowUtils.throwIf(ObjectUtil.isEmpty(modifyUserDTO), ErrorCode.PARAMS_ERROR);
        // 构造出查询条件
        Long id = modifyUserDTO.getId();
        String avatar = modifyUserDTO.getAvatar();
        Integer gender = modifyUserDTO.getGender();
        String username = modifyUserDTO.getUsername();
        // 获取当前用户
        Long currentId = BaseContext.getCurrentId();
        LambdaUpdateWrapper<User> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.set(ObjectUtil.isNotEmpty(avatar), User::getAvatar, avatar);
        lambdaUpdateWrapper.set(ObjectUtil.isNotEmpty(gender), User::getGender, gender);
        lambdaUpdateWrapper.set(ObjectUtil.isNotEmpty(username), User::getUsername, username);
        lambdaUpdateWrapper.set(ObjectUtil.isNotEmpty(currentId), User::getUpdateUserId, currentId);
        lambdaUpdateWrapper.eq(ObjectUtil.isNotEmpty(id), User::getId, id);
        // 更新数据库
        int result = userMapper.update(lambdaUpdateWrapper);
        ThrowUtils.throwIf(result <= 0, ErrorCode.SYSTEM_ERROR);
        // 返回结果
        return ResultUtils.success(ErrorCode.SUCCESS.getMessage());
    }

    @Override
    public BaseResponse<String> addUser(AddUserDTO addUserDTO) {
        // 校验参数非空
        ThrowUtils.throwIf(ObjectUtil.isEmpty(addUserDTO), ErrorCode.PARAMS_ERROR);
        String account = addUserDTO.getAccount();
        ThrowUtils.throwIf(StrUtil.isEmpty(account), ErrorCode.PARAMS_ERROR);
        String password = addUserDTO.getPassword();
        String role = addUserDTO.getRole();
        String username = addUserDTO.getUsername();
        Integer gender = addUserDTO.getGender();
        String avatar = addUserDTO.getAvatar();
        // 设置默认值
        password = StrUtil.isEmpty(password) ? UserConstant.DEFAULT_PASSWORD : password;
        username = StrUtil.isEmpty(username) ? UserConstant.NEW_USER_PREFIX + account : username;
        avatar = StrUtil.isEmpty(avatar) ? UserConstant.DEFAULT_AVATAR : avatar;
        // 构造插入条件
        User user = new User();
        user.setAccount(account);
        user.setPassword(SecureUtil.md5(password));
        user.setUsername(username);
        user.setGender(gender);
        user.setAvatar(avatar);
        user.setRole(role);
        user.setCreateUserId(BaseContext.getCurrentId());
        // 插入数据库
        int result = 0;
        try {
            result = userMapper.insert(user);
        } catch (DuplicateKeyException e) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, UserConstant.ACCOUNT_EXISTS);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        ThrowUtils.throwIf(result <= 0, ErrorCode.SYSTEM_ERROR);
        // 返回结果
        return ResultUtils.success(ErrorCode.SUCCESS.getMessage());
    }

    @Override
    public BaseResponse<String> startOrUp(Long id, Integer status) {
        // 校验参数非空
        ThrowUtils.throwIf(ObjectUtil.isEmpty(id) || ObjectUtil.isEmpty(status), ErrorCode.PARAMS_ERROR);
        // 校验状态值是否合法
        ThrowUtils.throwIf(status != 0 && status != 1, ErrorCode.PARAMS_ERROR, UserConstant.STATUS_VALUE_NOT_VALID);
        // 构造更新的查询条件
        LambdaUpdateWrapper<User> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        // 动态改变状态值
        status = status == 0 ? 1 : 0;
        lambdaUpdateWrapper.set(User::getStatus, status);
        lambdaUpdateWrapper.eq(User::getId, id);
        // 更新数据库
        int result = userMapper.update(lambdaUpdateWrapper);
        ThrowUtils.throwIf(result <= 0, ErrorCode.SYSTEM_ERROR);
        if (status == 1) {
            // 禁用账号时, 从redis中删除token
            stringRedisTemplate.delete(RedisConstant.redisTokenKey + id);
        }
        // 返回结果
        return ResultUtils.success(ErrorCode.SUCCESS.getMessage());
    }

    @Override
    public BaseResponse<String> deleteUserBatch(String ids) {
        // 校验参数非空
        ThrowUtils.throwIf(ObjectUtil.isEmpty(ids), ErrorCode.PARAMS_ERROR);
        // 将ids转成Long集合
        List<Long> idList = Arrays.stream(ids.split(",")).map(Long::valueOf).collect(Collectors.toList());
        // 查询数据库
        List<User> userList = userMapper.selectByIds(idList);
        // 校验用户是否存在
        ThrowUtils.throwIf(userList.size() != idList.size(), ErrorCode.PARAMS_ERROR);
        // 构造更新的查询条件
        LambdaUpdateWrapper<User> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.set(User::getIsDeleted, 1);
        lambdaUpdateWrapper.in(User::getId, idList);
        // 更新数据库
        int result = userMapper.update(lambdaUpdateWrapper);
        // 校验更新是否成功
        ThrowUtils.throwIf(result <= 0, ErrorCode.SYSTEM_ERROR);
        // 构造key
        List<String> tokenKeyList = idList.stream()
                .map(id -> RedisConstant.redisTokenKey + id)
                .collect(Collectors.toList());
        // 从redis中删除上述用户的token
        stringRedisTemplate.delete(tokenKeyList);
        // 返回结果
        return ResultUtils.success(ErrorCode.SUCCESS.getMessage());
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




