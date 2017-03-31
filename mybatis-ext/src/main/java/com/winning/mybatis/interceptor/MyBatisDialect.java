package com.winning.mybatis.interceptor;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class MyBatisDialect extends Dialect
{
	public String getLimitString(String sql, boolean hasOffset)
	{
		sql = sql.trim();
		String forUpdateClause = null;
		boolean isForUpdate = false;
		int forUpdateIndex = sql.toLowerCase().lastIndexOf("for update");
		if (forUpdateIndex > -1)
		{
			forUpdateClause = sql.substring(forUpdateIndex);
			sql = sql.substring(0, forUpdateIndex - 1);
			isForUpdate = true;
		}
		StringBuffer pagingSelect = new StringBuffer(sql.length() + 100);
		if (hasOffset)
			pagingSelect
					.append("select * from ( select row_.*, rownum rownum_ from ( ");
		else
			pagingSelect.append("select * from ( ");
		pagingSelect.append(sql);
		if (hasOffset)
			pagingSelect.append(" ) row_ where rownum <= ?) where rownum_ > ?");
		else
			pagingSelect.append(" ) where rownum <= ?");
		if (isForUpdate)
		{
			pagingSelect.append(" ");
			pagingSelect.append(forUpdateClause);
		}
		return pagingSelect.toString();
	}

	public String getLimitString(String sql, int offset, int limit)
	{// 50 ,50
		boolean hasOffset = offset > 0;
		sql = sql.trim();
		String forUpdateClause = null;
		boolean isForUpdate = false;
		int forUpdateIndex = sql.toLowerCase().lastIndexOf("for update");
		if (forUpdateIndex > -1)
		{
			forUpdateClause = sql.substring(forUpdateIndex);
			sql = sql.substring(0, forUpdateIndex - 1);
			isForUpdate = true;
		}
		StringBuffer pagingSelect = new StringBuffer(sql.length() + 100);
		if (hasOffset)
			pagingSelect
					.append("select * from ( select row_.*, rownum rownum_ from ( ");
		else
			pagingSelect.append("select * from ( ");
		pagingSelect.append(sql);
		if (hasOffset)
		{
			int rowsEnd = offset + limit;
			pagingSelect.append(" ) row_ where rownum <= " + rowsEnd
					+ " ) where rownum_ > " + offset + " ");
		}
		else
			pagingSelect.append(" ) where rownum <= " + limit + "");
		if (isForUpdate)
		{
			pagingSelect.append(" ");
			pagingSelect.append(forUpdateClause);
		}
		return pagingSelect.toString();
	}

	@Override
	public void setLimitParamters(PreparedStatement ps, int parameterSize,
			int offset, int limit) throws SQLException
	{
		int rowsEnd = offset + limit;
		log.debug("parameterSize :" + parameterSize);
		log.debug("offset :" + offset);
		log.debug("limit :" + limit);
		log.debug("rowsEnd :" + rowsEnd);
		ps.setInt(1 + parameterSize, rowsEnd);
		if (offset > 0)
			ps.setInt(2 + parameterSize, offset);
	}

}
