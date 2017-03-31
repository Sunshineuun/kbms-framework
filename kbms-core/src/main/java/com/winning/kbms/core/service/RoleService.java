package com.winning.kbms.core.service;

import java.util.List;

import com.winning.domain.kbms.core.Role;

public interface RoleService extends ManagementService <Role>
{
    public List <Role> getRoleByUserName (String userName);
}