package com.lc.usercenter.aop;

import com.lc.usercenter.annotation.RequireRole;
import com.lc.usercenter.common.context.BaseContext;
import com.lc.usercenter.constant.UserConstant;
import com.lc.usercenter.exception.BusinessException;
import com.lc.usercenter.exception.ErrorCode;
import com.lc.usercenter.model.entity.User;
import com.lc.usercenter.model.enums.UserRoleEnum;
import com.lc.usercenter.service.UserService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 角色权限校验切面: 校验接口方法是否被标注了 @RequireRole 注解，
 */
@Aspect
@Component
public class RoleAuthAspect {

    @Resource
    private UserService userService;

    /**
     * 校验接口方法是否被标注了 @RequireRole 注解
     */
    @Pointcut("@annotation(com.lc.usercenter.annotation.RequireRole)")
    public void roleAuthPointcut(){}

    @Around("roleAuthPointcut()")
    public Object checkRoleAuth(ProceedingJoinPoint joinPoint) throws Throwable {
        // 获取方法签名
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        // 获取方法上的 @RequireRole 注解
        RequireRole requireRole = signature.getMethod().getAnnotation(RequireRole.class);
        UserRoleEnum[] requiredRoles = requireRole.value();
        // 获取当前用户
        Long currentId = BaseContext.getCurrentId();
        User user = userService.getCurrentUserById(currentId);
        // 校验用户角色是否符合要求
        boolean hasPermission = false;
        UserRoleEnum userRole = UserRoleEnum.getByCode(user.getRole());
        for (UserRoleEnum requiredRole : requiredRoles) {
            if (userRole == requiredRole) {
                // 角色匹配成功，继续执行方法
                hasPermission = true;
                break;
            }
        }
        if (!hasPermission) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR, requireRole.message());
        }
        // 权限校验通过
        return joinPoint.proceed();
    }
}
