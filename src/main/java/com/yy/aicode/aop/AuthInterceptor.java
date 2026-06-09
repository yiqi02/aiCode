package com.yy.aicode.aop;

/*
*切面类 定义切面
 */

import com.yy.aicode.annotation.AuthCheck;
import com.yy.aicode.exception.BusinessException;
import com.yy.aicode.exception.ErrorCode;
import com.yy.aicode.model.entity.enums.UserRoleEnum;
import com.yy.aicode.model.entity.User;
import com.yy.aicode.service.UserService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Aspect //定义这是一个切面
@Component
public class AuthInterceptor {

    //进行校验 先获取当前用户的登录信息
    @Resource
    private UserService userService;


    /**
     * 执行拦截
     *定义一个切点 某个请求调用方法时就能够出触发这个切面 先判断在推进
     * around环绕通知 指定只对annotation注解方法进行拦截 拦截代码的范围
     * @param joinPoint 切入点
     * @param authCheck 权限校验注解
     * @return
     * @throws Throwable
     */

    @Around("@annotation(authCheck)")
    public Object doInterceptor(ProceedingJoinPoint joinPoint, AuthCheck authCheck) throws Throwable {

        //先从注解拿到一个权限 是这个注解所需要的权限
        String mustRole = authCheck.mustRole();
        //获取到所有的请求属性 检验当前用户有没有所需要的权限
        RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
        //获取severlet请求
        HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
        //获取当前登录的用户
        User loginUser = userService.getLoginUser(request);
        //通过枚举类转换上述注解中用户所必须需要的权限
        UserRoleEnum mustRoleEnum = UserRoleEnum.getEnumByValue(mustRole);
        //从用户枚举类中获取角色属性判断是否需要权限 为空不需要
        if (mustRoleEnum == null){
            return joinPoint.proceed();
        }
        //以下方法必须有mustRole权限才能通过
        UserRoleEnum userRoleEnum = UserRoleEnum.getEnumByValue(loginUser.getUserRole());
        //判断这个用户角色是否为空 为空则拒绝
        if(userRoleEnum == null){
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        //如果权限要求必须是管理员 此时用户不是管理员 则返回一个异常
        if(UserRoleEnum.ADMIN.equals(mustRoleEnum) && !UserRoleEnum.ADMIN.equals(userRoleEnum)){
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }

        //否则通过放行 因为只有两个角色
        return joinPoint.proceed();
    }
}
