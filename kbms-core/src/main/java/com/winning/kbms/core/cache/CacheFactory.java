package com.winning.kbms.core.cache;

import javax.annotation.Resource;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.cache.Cache;
import org.springframework.cache.support.CompositeCacheManager;
import org.springframework.util.Assert;

public final class CacheFactory implements FactoryBean<Cache>, InitializingBean {
	@Resource(name = "compositeCacheManager")
	private CompositeCacheManager compositeCacheManager;

	private String cacheName;

	public String getCacheName() {
		return cacheName;
	}

	public void setCacheName(String cacheName) {
		this.cacheName = cacheName;
	}

	@Override
	public Cache getObject() throws Exception {
		return compositeCacheManager.getCache(getCacheName());
	}

	@Override
	public Class<?> getObjectType() {
		return Cache.class;
	}

	@Override
	public boolean isSingleton() {
		return true;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		Assert.notNull(cacheName, "缓存名称不能为空！");
	}

}
