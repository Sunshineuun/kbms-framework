package com.winning.domain.kbms.core;

public class RoleAuthority extends Authority
{
    private static final long serialVersionUID = 1L;
    private String roleId;

    public String getRoleId ()
    {
        return roleId;
    }

    public void setRoleId (String roleId)
    {
        this.roleId = roleId;
    }

}
