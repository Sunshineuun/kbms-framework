package com.winning.kbms.core.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.winning.domain.kbms.core.User;
import com.winning.domain.kbms.core.UserAuthority;
import com.winning.kbms.core.domain.JResult;
import com.winning.kbms.core.service.AuthoritzationService;
import com.winning.kbms.core.service.ManagementService;
import com.winning.kbms.core.service.UserService;
import com.winning.kbms.core.utils.ContextUtils;
import com.winning.kbms.core.utils.JackJson;
import com.winning.utils.RandomUIDUtils;

@Controller
@RequestMapping("/user")
public class UserController extends AbstractManagementController<User> {
	@Resource(name = "userService")
	private UserService userService;

	@Resource(name = "authoritzationService")
	private AuthoritzationService authoritzationService;

	@Override
	protected ManagementService<User> getManagementService() {
		return userService;
	}

	@RequestMapping("/updatePassword.do")
	@ResponseBody
	public JResult updatePassword(@RequestParam("username") String username, @RequestParam("password") String password) {
		userService.updatePassword(username, password);
		return new JResult(true, "密码修改成功！");
	}

	@RequestMapping("/updateCurrUserPassword.do")
	@ResponseBody
	public JResult updateCurrUserPassword(@RequestParam("password") String password,
			@RequestParam("oldPassword") String oldPassword) {
		if (!this.getCurrUser().getPassword()
				.equals(ContextUtils.encrypt(this.getCurrUser().getUserName(), oldPassword))) {
			return new JResult(false, "旧密码输入错误！");
		}
		userService.updatePassword(this.getCurrUser().getUserName(), password);
		return new JResult(true, "密码修改成功！");
	}

	@RequestMapping("/authorizeToUser.do")
	@ResponseBody
	public JResult authorizeToUser(@RequestParam("userName") String userName,
			@RequestParam("jsonNodeStr") String jsonNodeStr) {
		authoritzationService.authorizeToUser(userName, assembleUserAuthoritys(userName, jsonNodeStr));
		return new JResult(true, "");
	}

	@RequestMapping("/grantRoleToUser.do")
	@ResponseBody
	public JResult grantRoleToUser(@RequestParam("userName") String userName, @RequestParam("roleIds") String roleIds) {
		userService.grantRoleToUser(userName, roleIds.split(","));
		return new JResult(true, "");
	}

	@RequestMapping("/changeWsKey.do")
	@ResponseBody
	public JResult changeWsKey(@RequestParam("userName") String userName) {
		String wsKey = userService.changeWsKey(userName);
		return new JResult(true, wsKey);
	}

	private List<UserAuthority> assembleUserAuthoritys(String userName, String jsonNodeStr) {
		List<Map<String, String>> nodes = JackJson.fromJsonToObject(jsonNodeStr,
				new TypeReference<List<Map<String, String>>>() {
				});
		List<UserAuthority> userAuthoritys = new ArrayList<UserAuthority>();
		for (Map<String, String> node : nodes) {
			UserAuthority userAuthority = new UserAuthority();
			userAuthority.setId(RandomUIDUtils.getUUID());
			userAuthority.setMenuId(node.get("id"));
			// 判断是否是功能节点
			if ("2".equals(node.get("type")) || "3".equals(node.get("type"))) {
				userAuthority.setAuthChildCode(node.get("authCode"));
				userAuthority.setAuthType("2".equals(node.get("type")) ? 1 : 2);
				String parentId = node.get("parentId");
				for (Map<String, String> map : nodes) {
					if (StringUtils.equals(parentId, map.get("id"))) {
						userAuthority.setAuthParentCode(map.get("authCode"));
						break;
					}
				}
			} else {
				if (StringUtils.isNotEmpty(node.get("authCode"))) {
					userAuthority.setAuthParentCode(node.get("authCode"));
					userAuthority.setAuthChildCode("read");
				}
			}
			userAuthority.setUserName(userName);
			userAuthoritys.add(userAuthority);
		}
		return userAuthoritys;
	}

}
