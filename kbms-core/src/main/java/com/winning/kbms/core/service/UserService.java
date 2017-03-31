package com.winning.kbms.core.service;

import org.springframework.transaction.annotation.Transactional;

import com.winning.domain.kbms.core.User;

public interface UserService extends ManagementService<User> {

	public User getUserByUsername(String username);

	public void updatePassword(String username, String password);

	@Transactional
	public void grantRoleToUser(String userName, String[] roleIds);

	public User getUserByKey(String key);

	public String changeWsKey(String userName);

}