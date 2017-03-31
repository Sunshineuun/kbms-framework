package com.winning.mybatis.utils;

import org.apache.ibatis.jdbc.SQL;

public class SqlBuilder extends SQL
{
	public static SqlBuilder newSqlBuilder()
	{
		return new SqlBuilder();
	}
}
