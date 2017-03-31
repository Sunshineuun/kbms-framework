package com.winning.kbms.core.cache;

import javax.annotation.Resource;

import org.apache.shiro.ShiroException;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.util.Destroyable;
import org.apache.shiro.util.Initializable;
import org.springframework.cache.support.CompositeCacheManager;

public class ShiroCacheManager implements CacheManager, Initializable, Destroyable {
	@Resource(name = "compositeCacheManager")
	private CompositeCacheManager compositeCacheManager;

	@Override
	public void destroy() throws Exception {
	}

	@Override
	public void init() throws ShiroException {
	}

	@Override
	public <K, V> Cache<K, V> getCache(String name) throws CacheException {
		org.springframework.cache.Cache cache = compositeCacheManager.getCache(name);
		return new ShiroCache<K, V>(cache);
	}

}
