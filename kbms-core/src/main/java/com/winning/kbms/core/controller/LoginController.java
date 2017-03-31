package com.winning.kbms.core.controller;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.winning.kbms.core.domain.JResult;
import com.winning.kbms.core.utils.ContextUtils;

@Controller
@RequestMapping("/login")
public class LoginController extends BaseController {
	@RequestMapping("/login.do")
	@ResponseBody
	public JResult login(@RequestParam("username") String username, @RequestParam("password") String password) {
		Subject currUser = SecurityUtils.getSubject();
		if (!currUser.isAuthenticated()) {
			String pwd = ContextUtils.encrypt(username, password);
			UsernamePasswordToken token = new UsernamePasswordToken(username, pwd);

			try {
				currUser.login(token);
			} catch (UnknownAccountException e) {
				return new JResult(false, "用户不存在！");
			} catch (IncorrectCredentialsException e) {
				return new JResult(false, "用户密码不正确！");
			} catch (LockedAccountException e) {
				return new JResult(false, "用户已经被锁！");
			} catch (AuthenticationException e) {
				return new JResult(false, "登录失败！");
			}
		}
		return new JResult(true, "登录成功！");
	}

	@RequestMapping("/showHow.do")
	public String showHow() {
		String username = "admin";
		String password = "dev123";
		Subject currUser = SecurityUtils.getSubject();
		if (!currUser.isAuthenticated()) {
			String pwd = ContextUtils.encrypt(username, password);
			UsernamePasswordToken token = new UsernamePasswordToken(username, pwd);

			try {
				currUser.login(token);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return this.forward("/forward/show_how.do");
	}

	@RequestMapping("/logout.do")
	public String logout() {
		Subject currUser = SecurityUtils.getSubject();
		currUser.logout();
		return redirect("/login.do");
	}
}
