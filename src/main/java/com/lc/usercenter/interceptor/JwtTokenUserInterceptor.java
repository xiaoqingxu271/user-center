package com.lc.usercenter.interceptor;

import com.lc.usercenter.common.context.BaseContext;
import com.lc.usercenter.common.properties.JwtProperties;
import com.lc.usercenter.constant.JwtClaimsConstant;
import com.lc.usercenter.constant.RedisConstant;
import com.lc.usercenter.exception.BusinessException;
import com.lc.usercenter.exception.ErrorCode;
import com.lc.usercenter.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * jwt令牌校验的拦截器
 */
@Component
@Slf4j
public class JwtTokenUserInterceptor implements HandlerInterceptor {

    @Resource
    private JwtProperties jwtProperties;
    @Resource
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 校验jwt
     *
     * @param request  请求对象
     * @param response 响应对象
     * @param handler  处理器对象
     * @return 如果校验通过则返回true，否则返回false
     * @throws Exception 可能抛出的异常
     */
    public boolean preHandle(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler) throws Exception {
        //判断当前拦截到的是Controller的方法还是其他资源
        if (!(handler instanceof HandlerMethod)) {
            //当前拦截到的不是动态方法，直接放行
            return true;
        }

        //1、从请求头中获取令牌
        String token = request.getHeader(jwtProperties.getTokenName());


        //2、校验令牌
        try {
            log.info("jwt校验:{}", token);
            Claims claims = JwtUtil.parseJWT(jwtProperties.getSecretKey(), token);
            Long userId = Long.valueOf(claims.get(JwtClaimsConstant.USER_ID).toString());
            log.info("当前用户id：{}", userId);
            // 3、将当前登录用户的id保存到ThreadLocal
            BaseContext.setCurrentId(userId);
            // 4、校验Redis中是否存在
            String redisToken = stringRedisTemplate.opsForValue().get(RedisConstant.redisTokenKey + userId);
            if (redisToken == null || !redisToken.equals(token)) {
                // 5、如果Redis中不存在，抛出没有权限异常
                throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
            }
            return true;
        } catch (Exception ex) {
            //4、如果校验失败，抛出没有权限异常
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
    }

}
