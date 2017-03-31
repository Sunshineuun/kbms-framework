package com.winning.kbms.core.interceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.UnauthorizedException;
import org.apache.shiro.subject.Subject;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.winning.domain.kbms.core.User;
import com.winning.kbms.core.service.UserService;
import com.winning.kbms.core.utils.ContextUtils;

public class WsAuthorizingInterceptor implements HandlerInterceptor {
	@Resource(name = "userService")
	private UserService userService;

	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,
			Exception exception) throws Exception {
	}

	public void postHandle(HttpServletRequest arg0, HttpServletResponse arg1, Object handler, ModelAndView modelAndView) {
	}

	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		String key = request.getParameter("key");
		if (StringUtils.isEmpty(key)) {
			if (!SecurityUtils.getSubject().isAuthenticated())
				throw new UnauthorizedException("您没有访问此页面的权限！");

			return true;
		} else {
			if (SecurityUtils.getSubject().isAuthenticated()) {
				if (key.equals(ContextUtils.getCurrUser().getWsKey())) {
					return true;
				} else {
					return loginByKey(key);
				}
			} else {
				return loginByKey(key);
			}
		}
	}

	private boolean loginByKey(String key) throws Exception {
		User user = userService.getUserByKey(key);

		if (user == null)
			throw new UnknownAccountException("key为" + key + "的用户不存在！");

		Subject currUser = SecurityUtils.getSubject();
		UsernamePasswordToken token = new UsernamePasswordToken(user.getUserName(), user.getPassword());
		try {
			currUser.login(token);
			return true;
		} catch (UnknownAccountException e) {
			throw new UnknownAccountException("用户不存在！");
		} catch (IncorrectCredentialsException e) {
			throw new IncorrectCredentialsException("用户密码不正确！");
		} catch (LockedAccountException e) {
			throw new LockedAccountException("用户已经被锁！");
		} catch (AuthenticationException e) {
			throw new AuthenticationException("登录失败！");
		}
	}
}
