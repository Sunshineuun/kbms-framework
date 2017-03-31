package com.winning.domain.kbms.core;

import java.util.Set;

public class WsUser extends User {
	private static final long serialVersionUID = 1L;
	private Set<String> authorities;

	public Set<String> getAuthorities() {
		return authorities;
	}

	public void setAuthorities(Set<String> authorities) {
		this.authorities = authorities;
	}
}
