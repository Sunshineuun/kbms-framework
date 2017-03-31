package com.winning.mybatis;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;

public interface DynamicSqlDao
{

	public abstract int executeSelectCountDynamic(String sql, Object params);

	public abstract int executeSelectCountDynamic(String sql);

	public abstract <K, V> List<Map<K, V>> executeSelectListDynamic(String sql, Object params);

	public abstract <K, V> List<Map<K, V>> executeSelectListDynamic(String sql);

	public abstract <K, V> Map<K, V> executeSelectOneDynamic(String sql, Object params);

	public abstract <K, V> Map<K, V> executeSelectOneDynamic(String sql);

	public abstract int executeUpdateDynamic(String sql, Object params);

	public abstract int executeUpdateDynamic(String sql);

	public abstract int executeInsertDynamic(String sql, Object params);

	public abstract int executeInsertDynamic(String sql);

	public abstract int executeDeleteDynamic(String sql, Object params);

	public abstract int executeDeleteDynamic(String sql);

	public abstract <K, V> List<Map<K, V>> executeSelectListDynamic(String sql, int offset, int limit,
			Object params);
	
	public SqlSession getSqlSession();

}