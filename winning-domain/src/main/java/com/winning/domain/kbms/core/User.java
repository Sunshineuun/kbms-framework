package com.winning.domain.kbms.core;

import java.util.List;

import org.apache.ibatis.type.JdbcType;

import com.winning.annotations.mybatis.Column;
import com.winning.annotations.mybatis.Exclude;
import com.winning.annotations.mybatis.Table;

@Table(value = "KBMS_USER", resultMapId = "UserMap")
public class User extends ModifyDomain {
	private static final long serialVersionUID = 1L;

	// 用户名
	private String userName;

	// 访问WebService的密匙
	private String wsKey;

	// 密码
	private String password;

	// 真实姓名
	private String realName;

	// 登录时间(YYYY-MM-DD hh:mm:ss)
	private String loginTime;

	// 是否被锁(YYYY-MM-DD)
	private boolean locked;

	private List<Role> roles;

	// 失效日期
	private String expiredDate;

	// 注销时间(YYYY-MM-DD hh:mm:ss)
	private String logoutTime;

	@Column("USER_NAME")
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Column("REAL_NAME")
	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	@Column(value = "LOGIN_TIME", isAdd = false, isUpdate = false)
	public String getLoginTime() {
		return loginTime;
	}

	public void setLoginTime(String loginTime) {
		this.loginTime = loginTime;
	}

	@Column(value = "LOCKED", jdbcType = JdbcType.BOOLEAN, resultHandler = "com.winning.kbms.core.commons.BooleanResultHandler")
	public boolean isLocked() {
		return locked;
	}

	public void setLocked(boolean locked) {
		this.locked = locked;
	}

	@Column("EXPIRED_DATE")
	public String getExpiredDate() {
		return expiredDate;
	}

	public void setExpiredDate(String expiredDate) {
		this.expiredDate = expiredDate;
	}

	@Column(value = "LOGOUT_TIME", isAdd = false, isUpdate = false)
	public String getLogoutTime() {
		return logoutTime;
	}

	public void setLogoutTime(String logoutTime) {
		this.logoutTime = logoutTime;
	}

	@Exclude
	public List<Role> getRoles() {
		return roles;
	}

	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}

	@Column(value = "WS_KEY", isAdd = false, isUpdate = false)
	public String getWsKey() {
		return wsKey;
	}

	public void setWsKey(String wsKey) {
		this.wsKey = wsKey;
	}

	@Override
	public String toString() {
		return this.userName;
	}
}
