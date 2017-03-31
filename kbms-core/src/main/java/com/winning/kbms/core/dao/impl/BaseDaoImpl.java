package com.winning.kbms.core.dao.impl;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.winning.kbms.core.dao.BaseDao;
import com.winning.mybatis.ObjectSession;
import com.winning.mybatis.support.Criteria;

public class BaseDaoImpl implements BaseDao {
	@Autowired
	@Qualifier("objectSession")
	private ObjectSession objectSession;

	public SqlSession getSqlSession() {
		return objectSession.getSqlSession();
	}

	@Override
	public <T> int insert(String _mybaitsId, T obj) {
		return getSqlSession().insert(_mybaitsId, obj);
	}

	@Override
	public <T> int update(String _mybaitsId, T obj) {
		return getSqlSession().update(_mybaitsId, obj);
	}

	@Override
	public <T> int delete(String _mybaitsId, T obj) {
		return getSqlSession().delete(_mybaitsId, obj);
	}

	@Override
	public <T> List<T> query(String _mybaitsId, Object obj) {
		return getSqlSession().selectList(_mybaitsId, obj);
	}

	@Override
	public <T> List<T> query(String _mybaitsId, Object obj, int offset, int limit) {
		return getSqlSession().selectList(_mybaitsId, obj, new RowBounds(offset, limit));
	}

	@Override
	public <T> T queryOne(String _mybaitsId, Object obj) {
		return getSqlSession().selectOne(_mybaitsId, obj);
	}

	@Override
	public int count(String _mybaitsId, Object obj) {
		return getSqlSession().selectOne(_mybaitsId, obj);
	}

	@Override
	public int count(String _mybaitsId) {
		return getSqlSession().selectOne(_mybaitsId);
	}

	@Override
	public int countBySql(String sql) {
		return objectSession.executeSelectCountDynamic(sql);
	}

	@Override
	public <T> int insertBySql(String sql, Map<String, Object> _params) {
		return objectSession.executeInsertDynamic(sql, _params);
	}

	@Override
	public <T> int updateBySql(String sql, Map<String, Object> _params) {
		return objectSession.executeUpdateDynamic(sql, _params);
	}

	@Override
	public <T> int deleteBySql(String sql, Map<String, Object> _params) {
		return objectSession.executeDeleteDynamic(sql, _params);
	}

	@Override
	public <K, V> List<Map<K, V>> queryBySql(String sql, Map<String, Object> _params) {
		return objectSession.executeSelectListDynamic(sql, _params);
	}

	@Override
	public <K, V> List<Map<K, V>> queryBySql(String sql, Map<String, Object> _params, int offset, int limit) {
		return objectSession.executeSelectListDynamic(sql, offset, limit, _params);
	}

	@Override
	public <K, V> Map<K, V> queryOneBySql(String sql, Map<String, Object> _params) {
		return objectSession.executeSelectOneDynamic(sql, _params);
	}

	@Override
	public int countBySql(String sql, Map<String, Object> _params) {
		return objectSession.executeSelectCountDynamic(sql, _params);
	}

	@Override
	public <T> int insert(String _mybaitsId) {
		return insert(_mybaitsId, null);
	}

	@Override
	public <T> int update(String _mybaitsId) {
		return update(_mybaitsId, null);
	}

	@Override
	public <T> int delete(String _mybaitsId) {
		return delete(_mybaitsId);
	}

	@Override
	public <T> List<T> query(String _mybaitsId) {
		return query(_mybaitsId, null);
	}

	@Override
	public <T> int insertBySql(String sql) {
		return objectSession.executeInsertDynamic(sql);
	}

	@Override
	public <T> int updateBySql(String sql) {
		return objectSession.executeUpdateDynamic(sql);
	}

	@Override
	public <T> int deleteBySql(String sql) {
		return objectSession.executeDeleteDynamic(sql);
	}

	@Override
	public <K, V> List<Map<K, V>> queryBySql(String sql) {
		return objectSession.executeSelectListDynamic(sql);
	}

	public <T> T getById(Serializable id, Class<T> clazz) {
		return objectSession.getById(id, clazz);
	}

	public <T> List<T> getAll(Class<T> clazz) {
		return objectSession.getAll(clazz);
	}

	public <T> List<T> queryByCriteria(Criteria criteria, Class<T> clazz) {
		return objectSession.queryByCriteria(criteria, clazz);
	}

	public <T> void add(T obj) {
		objectSession.insert(obj);
	}

	public <T> void deleteById(Serializable id, Class<T> clazz) {
		objectSession.deleteById(id, clazz);
	}

	public <T> void deleteByCriteria(Criteria criteria, Class<?> clazz) {
		objectSession.deleteByCriteria(criteria, clazz);
	}

	public <T> void update(T obj) {
		objectSession.update(obj);
	}

	@Override
	public <T> T queryOneByCriteria(Criteria criteria, Class<T> clazz) {
		List<T> list = this.queryByCriteria(criteria, clazz);
		if (list == null || list.isEmpty())
			return null;
		return list.get(0);
	}

	@Override
	public <T> int countByCriteria(Criteria criteria, Class<T> clazz) {
		return objectSession.countByCriteria(criteria, clazz);
	}

	@Override
	public <T> List<T> queryBySql(String sql, Class<T> clazz, Map<String, Object> _params) {
		return objectSession.queryBySql(sql, clazz, _params);
	}

	/**
	 * 
	 * @author:qiushengming
	 * @param _params
	 * @param sql
	 * @return:void
	 * @date:2016-8-2 下午4:04:43
	 */
	@Override
	public <T> void submit(Class<?> baseDomain, String _sql, Map<String, Object> _params) {
		objectSession.executeSelectListDynamic(_sql, _params);
	}

	 /**
     * 
     * @author:lifeng
     * @param sql
     * @return:void
     * @date:2016-10-26
     */
    @Override
    public <T> void logicDelete(Class<T> clazz, String _sql, Map<String, Object> _params) {
        objectSession.executeSelectListDynamic(_sql,_params);
    }

    @Override
    public <T> void logicReduction(Class<T> clazz, String _sql, Map<String, Object> _params) {
        objectSession.executeSelectListDynamic(_sql,_params);
    }

}
