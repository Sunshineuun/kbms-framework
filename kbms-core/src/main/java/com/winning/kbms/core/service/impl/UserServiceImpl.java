package com.winning.kbms.core.service.impl;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.winning.domain.kbms.core.User;
import com.winning.kbms.core.exceptions.ServiceException;
import com.winning.kbms.core.exceptions.ValidationException;
import com.winning.kbms.core.service.UserService;
import com.winning.kbms.core.service.WsUserService;
import com.winning.kbms.core.utils.ApplicationContextUtils;
import com.winning.kbms.core.utils.ContextUtils;
import com.winning.mybatis.support.Criteria;
import com.winning.utils.MapUtils;
import com.winning.utils.RandomUIDUtils;

@Service("userService")
public class UserServiceImpl extends AbstractManagementService<User> implements UserService {

	@Override
	@Transactional
	public void add(User user) throws ServiceException {
		User u = this.getUserByUsername(user.getUserName());
		if (u != null)
			throw new ValidationException("用户名已经存在！");

		user.setPassword(ContextUtils.encrypt(user.getUserName(), user.getPassword()));
		super.add(user);
	}

	@Override
	public User getUserByUsername(String username) {
		return this.queryOneByCriteria(Criteria.create().andEqualTo("userName", username));
	}

	@Override
	public User getUserByKey(String key) {
		return this.queryOneByCriteria(Criteria.create().andEqualTo("wsKey", key));
	}

	public void updatePassword(String username, String password) {
		password = ContextUtils.encrypt(username, password);
		getDao().update("updatePassword", MapUtils.newMap("userName", username, "password", password));
	}

	public void deleteUserRolesByUserName(String userName) {
		getDao().delete("deleteUserRolesByUserName", userName);
	}

	@Transactional
	public void grantRoleToUser(String userName, String[] roleIds) {
		deleteUserRolesByUserName(userName);
		//wfm修改  由于临床主管需要新增用户权限  为解决其中可能出现的临床部主管包含赋值管理员的权限bug  特此加入限制
		//先获取当前的操作用户对象   
		User user = ContextUtils.getCurrUser();
		for (String roleId : roleIds) {
			try {
				//临床部主管只能授予临床部人员的角色   临床部角色ID：R00001
				if(!StringUtils.equals("R00001", roleId) && StringUtils.equals("临床部主管",user.getRoles().get(0).getRoleName()))
					throw new ValidationException("抱歉！您只能授予临床部人员的权限！");
				getDao().insert("grantRoleToUser",
						MapUtils.newMap("id", RandomUIDUtils.getUUID(), "userName", userName, "roleId", roleId));
			} catch (NullPointerException e) {
				throw new ValidationException("抱歉！您只能授予临床部人员的权限！");
			}
		}
	}

	@Override
	public String changeWsKey(String userName) {
		String newWsKey = RandomUIDUtils.getUUID();
		getDao().update("changeWsKey", MapUtils.newMap("wsKey", newWsKey, "userName", userName));
		ApplicationContextUtils.getBean("wsUserService", WsUserService.class).clearCacheByUserName(userName);
		ContextUtils.getCurrUser().setWsKey(newWsKey);
		return newWsKey;
	}
}
