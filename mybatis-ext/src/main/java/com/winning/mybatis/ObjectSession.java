package com.winning.mybatis;

import java.io.Serializable;
import java.util.List;

import com.winning.mybatis.support.Criteria;

public interface ObjectSession extends DynamicSqlDao {
	public <T> T getById(Serializable id, Class<T> clazz);

	public <T> List<T> getAll(Class<T> clazz);

	public <T> List<T> queryByCriteria(Criteria criteria, Class<T> clazz);

	public <T> List<T> queryBySql(String sql, Class<T> clazz, Object params);

	public <T> int countByCriteria(Criteria criteria, Class<T> clazz);

	public <T> void insert(T obj);

	public <T> void deleteById(Serializable id, Class<T> clazz);

	public <T> void deleteByCriteria(Criteria criteria, Class<?> clazz);

	public <T> void update(T obj);
}
