package com.winning.kbms.core.service.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import net.sf.ehcache.Ehcache;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.Cache;
import org.springframework.cache.Cache.ValueWrapper;
import org.springframework.stereotype.Service;

import com.winning.domain.kbms.core.Authority;
import com.winning.domain.kbms.core.User;
import com.winning.domain.kbms.core.WsUser;
import com.winning.kbms.core.service.AuthoritzationService;
import com.winning.kbms.core.service.UserService;
import com.winning.kbms.core.service.WsUserService;

@Service("wsUserService")
public class WsUserServiceImpl implements WsUserService {

	@Autowired(required = false)
	@Qualifier("wsUserCache")
	private Cache wsUserCache;

	@Resource(name = "userService")
	private UserService userService;

	@Resource(name = "authoritzationService")
	private AuthoritzationService authoritzationService;

	public WsUser getWsUserByKey(String key) {
		if (wsUserCache != null) {
			ValueWrapper valueWrapper = wsUserCache.get(key);
			if (valueWrapper != null)
				return (WsUser) valueWrapper.get();

			WsUser user = createWsUser(key);

			if (user == null)
				return null;

			wsUserCache.put(key, user);
			return user;
		} else {
			return createWsUser(key);
		}
	}

	protected WsUser createWsUser(String key) {
		User user = userService.getUserByKey(key);

		if (user == null)
			return null;

		WsUser wsUser = new WsUser();
		wsUser.setUserName(user.getUserName());
		wsUser.setRealName(user.getRealName());
		wsUser.setWsKey(key);
		Set<String> authSet = new HashSet<String>();
		List<Authority> userAuthorities = authoritzationService.getWsUserAuthorityByUserName(user.getUserName());
		for (Authority authority : userAuthorities) {
			authSet.add(authority.toString());
		}
		List<Authority> roleAuthorities = authoritzationService.getWsRoleAuthorityByUserName(user.getUserName());
		for (Authority authority : roleAuthorities) {
			authSet.add(authority.toString());
		}
		wsUser.setAuthorities(authSet);
		return wsUser;
	}

	public void clearCache() {
		if (wsUserCache != null)
			wsUserCache.clear();
	}

	@SuppressWarnings("unchecked")
	public void clearCacheByUserName(String userName) {
		if (wsUserCache == null)
			return;

		Object obj = wsUserCache.getNativeCache();
		if (obj instanceof Ehcache) {
			Ehcache ehcache = (Ehcache) obj;
			List<String> keys = ehcache.getKeys();
			WsUser user = null;
			for (String key : keys) {
				user = (WsUser) ehcache.get(key).getObjectValue();
				if (StringUtils.equals(user.getUserName(), userName)) {
					ehcache.remove(key);
				}
			}
		} else {
			wsUserCache.clear();
		}
	}

	public void clearCacheByWsKey(String wsKey) {
		if (wsUserCache == null)
			return;

		Object obj = wsUserCache.getNativeCache();
		if (obj instanceof Ehcache) {
			Ehcache ehcache = (Ehcache) obj;
			ehcache.remove(wsKey);
		} else {
			wsUserCache.clear();
		}
	}
}
