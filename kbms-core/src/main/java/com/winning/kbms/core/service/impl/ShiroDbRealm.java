package com.winning.kbms.core.service.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import com.winning.domain.kbms.core.Authority;
import com.winning.domain.kbms.core.Role;
import com.winning.domain.kbms.core.User;
import com.winning.kbms.core.service.AuthoritzationService;
import com.winning.kbms.core.service.RoleService;
import com.winning.kbms.core.service.UserService;

public class ShiroDbRealm extends AuthorizingRealm {
	@Resource(name = "userService")
	private UserService userService;

	@Resource(name = "roleService")
	private RoleService roleService;

	@Resource(name = "authoritzationService")
	private AuthoritzationService authoritzationService;

	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		User user = (User) principals.getPrimaryPrincipal();
		SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
		List<Role> roleList = roleService.getRoleByUserName(user.getUserName());
		Set<String> roleSet = new HashSet<String>();
		for (Role role : roleList) {
			if (role.isEnable())
				roleSet.add(role.getId());
		}

		Set<String> authSet = new HashSet<String>();
		List<Authority> userAuthorities = authoritzationService.getUserAuthorityByUserName(user.getUserName());
		for (Authority authority : userAuthorities) {
			authSet.add(authority.toString());
		}
		List<Authority> roleAuthorities = authoritzationService.getRoleAuthorityByUserName(user.getUserName());
		for (Authority authority : roleAuthorities) {
			authSet.add(authority.toString());
		}
		info.setRoles(roleSet);
		info.setStringPermissions(authSet);

		return info;
	}

	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		UsernamePasswordToken upToken = (UsernamePasswordToken) token;
		User user = userService.getUserByUsername(upToken.getUsername());
		if (user != null) {
			List<Role> roleList = roleService.getRoleByUserName(user.getUserName());
			user.setRoles(roleList);
			return new SimpleAuthenticationInfo(user, user.getPassword(), getName());
		}
		return null;
	}

	public void clearCachedAuthorizationInfo(String userName) {
		for (Object obj : this.getAuthorizationCache().keys()) {
			if (StringUtils.equals(userName, obj.toString()))
				this.getAuthorizationCache().remove(obj);
		}
	}

	public void clearCachedAuthorizationInfo() {
		this.getAuthorizationCache().clear();
	}

	public String getAuthorizationCacheName() {
		return "kbmsAuthorizationCache";
	}

}
