package com.ngsliuji.ngsaicodemother.aop;


import com.ngsliuji.ngsaicodemother.annotation.AuthCheck;
import com.ngsliuji.ngsaicodemother.exception.BusinessException;
import com.ngsliuji.ngsaicodemother.exception.ErrorCode;
import com.ngsliuji.ngsaicodemother.model.entity.User;
import com.ngsliuji.ngsaicodemother.model.enums.UserRoleEnum;
import com.ngsliuji.ngsaicodemother.service.UserService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Aspect// 切面
@Component // 组件
public class AuthInterceptor {
    @Resource
    private UserService userService;
    /*@Around 环绕拦截方法
     * @Around("@annotation(authCheck)") 拦截指定注解
     * ProceedingJoinPoint joinPoint 拦截方法
     * AuthCheck authCheck 注解
     */

    /**
     * 执行拦截
     *
     * @param joinPoint 切入点
     * @param authCheck 权限校验注解
     * @return
     * @throws Throwable
     */
    @Around("@annotation(authCheck)")
    public Object doInterceptor(ProceedingJoinPoint joinPoint, AuthCheck authCheck) throws Throwable {
        String mustRole = authCheck.mustRole();

        RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
        HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
        //获取当前登录用户
        User loginUser = userService.getLoginUser(request);
        UserRoleEnum mustRoleEnum = UserRoleEnum.getEnumByValue(mustRole);
        //不需要权限直接放行
        if (mustRoleEnum == null) {
            return joinPoint.proceed();//放行,让程序继续执行
        }
        // 必须有该权限才通过
        UserRoleEnum userRileEnum = UserRoleEnum.getEnumByValue(loginUser.getUserRole());
        if(userRileEnum == null){
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        //要求必须有管理员权限
        if(UserRoleEnum.ADMIN.equals(userRileEnum)&& !UserRoleEnum.ADMIN.equals(mustRoleEnum)){
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }

        //有权限放行
        return joinPoint.proceed();

    }
}
