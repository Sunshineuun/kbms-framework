package com.winning.domain.kbms.core;

import com.winning.annotations.mybatis.Column;
import com.winning.annotations.mybatis.Table;

@Table(value = "KBMS_ROLE", resultMapId = "RoleMap")
public class Role extends ModifyDomain {
	private static final long serialVersionUID = 1L;

	// 角色名称
	private String roleName;

	// 是否可用
	private boolean enable;

	@Column("ROLE_NAME")
	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	@Column(value = "enable")
	public boolean isEnable() {
		return enable;
	}

	public void setEnable(String enable) {
		if ("0".equals(enable))
			this.enable = Boolean.FALSE;
		else
			this.enable = Boolean.TRUE;
	}

}
