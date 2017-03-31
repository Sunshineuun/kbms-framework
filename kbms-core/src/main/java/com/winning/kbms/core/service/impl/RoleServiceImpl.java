package com.winning.kbms.core.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.winning.domain.kbms.core.Role;
import com.winning.kbms.core.exceptions.ServiceException;
import com.winning.kbms.core.exceptions.ValidationException;
import com.winning.kbms.core.service.RoleService;

@Service("roleService")
public class RoleServiceImpl extends AbstractManagementService<Role> implements RoleService {
	@Override
	@Transactional
	public void add(Role role) throws ServiceException {
		Role r = this.getById(role.getId());
		if (r != null)
			throw new ValidationException("角色编码已经存在！");

		super.add(role);
	}

	@Override
	public List<Role> getRoleByUserName(String userName) {
		return getDao().query("getRoleByUserName", userName);
	}
}
