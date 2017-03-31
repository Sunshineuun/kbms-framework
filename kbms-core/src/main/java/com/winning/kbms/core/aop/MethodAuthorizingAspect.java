package com.winning.kbms.core.aop;

import org.apache.shiro.authc.AuthenticationException;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import com.winning.kbms.core.annotations.Permission;
import com.winning.kbms.core.utils.SecurityUtils;

@Aspect
@Component
public class MethodAuthorizingAspect
{
    @Before ("@annotation(com.winning.kbms.core.annotations.Permission) && @annotation(permission)")
    public void beforeAccessMethed (Permission permission)
    {
        if (!SecurityUtils.hasPermission (permission.value ()))
            throw new AuthenticationException ("您没有访问权限！");
    }
}
