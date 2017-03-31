package com.winning.kbms.core.cache;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import net.sf.ehcache.Ehcache;

import org.apache.commons.collections.CollectionUtils;
import org.apache.shiro.cache.CacheException;
import org.springframework.cache.Cache;
import org.springframework.cache.Cache.ValueWrapper;

public class ShiroCache<K, V> implements org.apache.shiro.cache.Cache<K, V> {
	private Cache cache;

	public ShiroCache(Cache cache) {
		this.cache = cache;
	}

	@Override
	@SuppressWarnings("unchecked")
	public V get(K key) throws CacheException {
		try {
			ValueWrapper vw = cache.get(key);
			if (vw != null)
				return (V) vw.get();

			return null;
		} catch (Throwable t) {
			throw new CacheException(t);
		}
	}

	@Override
	public V put(K key, V value) throws CacheException {
		try {
			V preious = get(key);
			cache.put(key, value);
			return preious;
		} catch (Throwable t) {
			throw new CacheException(t);
		}
	}

	@Override
	public V remove(K key) throws CacheException {
		try {
			V preious = get(key);
			cache.evict(key);
			return preious;
		} catch (Throwable t) {
			throw new CacheException(t);
		}
	}

	@Override
	public void clear() throws CacheException {
		try {
			cache.clear();
		} catch (Throwable t) {
			throw new CacheException(t);
		}
	}

	@Override
	public int size() {
		Object nativeCache = cache.getNativeCache();
		if (nativeCache instanceof Ehcache)
			return ((Ehcache) nativeCache).getSize();
		return 0;
	}

	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Set<K> keys() {
		Object nativeCache = cache.getNativeCache();
		if (nativeCache instanceof Ehcache) {
			List<V> keys = ((Ehcache) nativeCache).getKeys();
			if (CollectionUtils.isNotEmpty(keys))
				return Collections.unmodifiableSet(new LinkedHashSet(keys));
		}
		return Collections.emptySet();
	}

	@Override
	public Collection<V> values() {
		Set<K> keys = keys();
		if (CollectionUtils.isNotEmpty(keys)) {
			List<V> values = new ArrayList<V>(keys.size());
			for (K key : keys) {
				values.add(get(key));
			}
			return Collections.unmodifiableList(values);
		}
		return Collections.emptyList();
	}

}
