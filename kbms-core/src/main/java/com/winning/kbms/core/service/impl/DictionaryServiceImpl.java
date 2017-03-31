package com.winning.kbms.core.service.impl;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.winning.domain.kbms.core.Dictionary;
import com.winning.kbms.core.exceptions.ServiceException;
import com.winning.kbms.core.service.DictionaryService;
import com.winning.mybatis.support.Criteria;

/**
 * 数据字典管理
 * 
 * @author gang.liu
 * @date 2013-1-12
 */
@Service("dictionaryService")
public class DictionaryServiceImpl extends AbstractManagementService<Dictionary> implements DictionaryService {
	private static final String LABEL = "LABEL";
	private static final String VALUE = "VALUE";

	@Caching(cacheable = {
			@Cacheable(value = "dictionaryCache", key = "'searchBZJX'", condition = "#queryId eq 'searchBZJX'"),
			@Cacheable(value = "dictionaryCache", key = "'searchSJJX'", condition = "#queryId eq 'searchSJJX'") })
	public Map<String, String> getDictionaryOnQueryId(String queryId) {
		Map<String, String> result = new LinkedHashMap<String, String>();
		List<Map<String, Object>> selectList = getDao().query(queryId);
		for (Map<String, Object> map : selectList) {
			if (map == null)
				continue;

			String value = map.get(VALUE) == null ? "" : map.get(VALUE) + "";
			String label = map.get(LABEL) == null ? "" : map.get(LABEL) + "";
			result.put(value, label);
		}
		return result;
	}

	@Cacheable(value = "dictionaryCache", key = "#typeCode")
	public Map<String, String> getDictionaryByTypeCode(String typeCode) {
		Map<String, String> result = new LinkedHashMap<String, String>();
		List<Map<String, Object>> selectList = getDao().query("getDictionaryByTypeCode", typeCode);
		for (Map<String, Object> map : selectList) {
			if (map == null)
				continue;

			String value = map.get(VALUE) == null ? "" : map.get(VALUE) + "";
			String label = map.get(LABEL) == null ? "" : map.get(LABEL) + "";
			result.put(value, label);
		}
		return result;
	}

	@Override
	public Map<String, String> getDictionaryOnQueryIdByParams(String queryId, Map<String, Object> params) {
		Map<String, String> result = new LinkedHashMap<String, String>();
		List<Map<String, String>> selectList = getDao().query(queryId, params);
		for (Map<String, String> map : selectList) {
			if (map == null)
				continue;

			result.put(map.get(VALUE), map.get(LABEL));
		}
		return result;
	}

	@Override
	public Map<String, String> searchDictionaryOnQueryId(String queryId, int limit, Map<String, Object> params) {
		Map<String, String> result = new LinkedHashMap<String, String>();
		List<Map<String, String>> selectList = getDao().query(queryId, params, 0, limit);
		for (Map<String, String> map : selectList) {
			if (map == null)
				continue;

			result.put(map.get(VALUE), map.get(LABEL));
		}
		return result;
	}

	@Override
	@Transactional
	@CacheEvict(value = "dictionaryCache", key = "#dictionary?.typeCode")
	public void add(Dictionary dictionary) throws ServiceException {
		super.add(dictionary);
	}

	@Override
	@Transactional
	@CacheEvict(value = "dictionaryCache", key = "#dictionary?.typeCode")
	public void update(Dictionary dictionary) throws ServiceException {
		super.update(dictionary);
	}

	@Override
	protected String getSearchSql(Map<String, Object> params) throws Exception {
		return "select * from (select * from kbms_dictionary where type_code=#{typeCode,jdbcType=VARCHAR} order by view_order)";
	}

	@Override
	protected String getCountSql(Map<String, Object> params) throws Exception {
		return "select count(1) from (select * from kbms_dictionary where type_code=#{typeCode,jdbcType=VARCHAR})";
	}

	@CacheEvict(value = "dictionaryCache", key = "#typeCode")
	public void deleteDictionaryByTypeCode(String typeCode) {
		this.deleteByCriteria(Criteria.create().andEqualTo("typeCode", typeCode));
	}
}
