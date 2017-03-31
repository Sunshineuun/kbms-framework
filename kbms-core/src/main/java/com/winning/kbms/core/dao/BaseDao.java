package com.winning.kbms.core.dao;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.winning.mybatis.support.Criteria;

public interface BaseDao {
	/**
	 * 单表插入记录
	 * 
	 * @param obj
	 */
	public <T> int insert(String _mybaitsId, T obj);

	/**
	 * 单表插入记录
	 */
	public <T> int insert(String _mybaitsId);

	/**
	 * 更新单表
	 * 
	 * @param obj
	 */
	public <T> int update(String _mybaitsId, T obj);

	/**
	 * 更新单表
	 */
	public <T> int update(String _mybaitsId);

	/**
	 * 删除记录
	 * @param _mybaitsId
	 * @param obj
	 */
	public <T> int delete(String _mybaitsId, T obj);

	/**
	 * 删除记录
	 */
	public <T> int delete(String _mybaitsId);

	/**
	 * 
	 * 返回相关列表信息
	 * 
	 * @param _mybaitsId
	 *            mybatis中对应业务标识
	 * @return List<T>
	 */
	public <T> List<T> query(String _mybaitsId);

	/**
	 * 查询相关列表信息
	 * 
	 * @param <T>
	 *            返回数据
	 * @param _mybaitsId
	 *            mybatis中对应业务标识
	 * @param _params
	 * @return List<T>
	 */
	public <T> List<T> query(String _mybaitsId, Object _params);

	/**
	 * 
	 * 返回分页查询信息
	 * 
	 * @param <T>
	 * @param _mybaitsId
	 *            mybatis中对应业务标识
	 * @param _params
	 * @param offset
	 * @param limit
	 * @return List<T>
	 */
	public <T> List<T> query(String _mybaitsId, Object _params, int offset, int limit);

	/**
	 * 查询单个数据
	 * 
	 * @param _mybaitsId
	 * @param _params
	 * @return T
	 */
	public <T> T queryOne(String _mybaitsId, Object _params);

	/**
	 * 查询个数
	 * 
	 * @param _mybaitsId
	 * @return int
	 */
	public int count(String _mybaitsId);

	/**
	 * 查询个数
	 * 
	 * @param _mybaitsId
	 * @param _params
	 * @return int
	 */
	public int count(String _mybaitsId, Object _params);

	/**
	 * 单表插入记录
	 * 
	 * @param _params
	 * @param sql
	 * @return int 
	 */
	public <T> int insertBySql(String sql, Map<String, Object> _params);

	/**
	 * 单表插入记录
	 */
	public <T> int insertBySql(String sql);

	/**
	 * 更新单表
	 */
	public <T> int updateBySql(String sql);

	/**
	 * 更新单表
	 * 
	 * @param _params
	 * @param sql
	 * @return int
	 */
	public <T> int updateBySql(String sql, Map<String, Object> _params);

	/**
	 * 删除记录
	 */
	public <T> int deleteBySql(String sql);

	/**
	 * 删除记录
	 * 
	 * @param _params
	 * @param sql
	 * @return int
	 */
	public <T> int deleteBySql(String sql, Map<String, Object> _params);

	/**
	 * 
	 * 返回相关列表信息
	 * 
	 * @param sql
	 * @return List<Map<K, V>>
	 */
	public <K, V> List<Map<K, V>> queryBySql(String sql);

	/**
	 * 
	 * 返回相关列表信息
	 * 
	 * @param sql
	 * @param _params
	 * @return ist<Map<K, V>>
	 */
	public <K, V> List<Map<K, V>> queryBySql(String sql, Map<String, Object> _params);

	public <T> List<T> queryBySql(String sql, Class<T> clazz, Map<String, Object> _params);

	/**
	 * 
	 * 返回分页查询信息
	 * 
	 * @param sql
	 * @param _params
	 * @param offset
	 * @param limit
	 * @return List<Map<K, V>>
	 */
	public <K, V> List<Map<K, V>> queryBySql(String sql, Map<String, Object> _params, int offset, int limit);

	/**
	 * 查询单个数据
	 * 
	 * @param sql
	 * @param _params
	 * @return Map<K, V>
	 */
	public <K, V> Map<K, V> queryOneBySql(String sql, Map<String, Object> _params);

	/**
	 * 查询个数
	 * 
	 * @param sql
	 * @param _params
	 * @return int
	 */
	public int countBySql(String sql, Map<String, Object> _params);

	/**
	 * 查询个数
	 * 
	 * @param sql
	 * @return int
	 */
	public int countBySql(String sql);

	public <T> T getById(Serializable id, Class<T> clazz);

	public <T> List<T> getAll(Class<T> clazz);

	public <T> List<T> queryByCriteria(Criteria criteria, Class<T> clazz);

	public <T> int countByCriteria(Criteria criteria, Class<T> clazz);

	public <T> T queryOneByCriteria(Criteria criteria, Class<T> clazz);

	public <T> void add(T obj);

	public <T> void deleteById(Serializable id, Class<T> clazz);

	public <T> void deleteByCriteria(Criteria criteria, Class<?> clazz);

	public <T> void update(T obj);
	
	/**
	 * kbms系统数据提交用到的方法
	 * @author:qiushengming
	 * @param obj
	 * @return:void
	 * @date:2016-8-2 下午4:04:43
	 */
	public <T> void submit(Class<?> baseDomain, String _sql, Map<String, Object> _params);
	
	
	 /**
     * 逻辑删除
     * @author:李峰
     * @param obj
     * @date:2016-10-26
     */
	public <T> void logicDelete(Class<T> clazz, String _sql, Map<String, Object> _params);
	
	public <T> void logicReduction(Class<T> clazz, String _sql, Map<String, Object> _params);
	
	
}
