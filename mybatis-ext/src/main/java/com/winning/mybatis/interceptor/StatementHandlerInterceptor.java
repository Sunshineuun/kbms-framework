package com.winning.mybatis.interceptor;

import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.ibatis.executor.resultset.ResultSetHandler;
import org.apache.ibatis.executor.statement.PreparedStatementHandler;
import org.apache.ibatis.executor.statement.SimpleStatementHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.session.RowBounds;

import com.winning.utils.ReflectionUtils;

@Intercepts({
		@Signature(type = StatementHandler.class, method = "prepare", args = { Connection.class }),
		@Signature(type = StatementHandler.class, method = "parameterize", args = { Statement.class }),
		@Signature(type = ResultSetHandler.class, method = "handleResultSets", args = { Statement.class }) })
public class StatementHandlerInterceptor extends
		AbstractStatementHandlerInterceptor implements Interceptor
{

	public Object intercept(Invocation invocation) throws Throwable
	{
		Method m = invocation.getMethod();
		if ("prepare".equals(m.getName()))
		{ // 一个负责生成Statement
			return prepare(invocation);
		}
		else if ("parameterize".equals(m.getName()))
		{ // 一个负责设置参数
			return parameterize(invocation);
		}
		else if ("handleResultSets".equals(m.getName()))
		{// handleResultSets
			return handleResultSets(invocation);
		}
		return invocation.proceed();
	}

	public Object plugin(Object target)
	{
		return Plugin.wrap(target, this);
	}

	public void setProperties(Properties properties)
	{
	}

	@SuppressWarnings("unchecked")
	private boolean isCountSql(BoundSql boundSql)
	{
		Object parameterObject = boundSql.getParameterObject();
		if (parameterObject instanceof Map)
		{
			Map<String, Object> map = (Map<String, Object>) parameterObject;
			if (map.containsKey(Dialect.ROWS_COUNT))
				return true;
		}
		return false;
	}

	private Object prepare(Invocation invocation) throws Throwable
	{
		StatementHandler statement = getStatementHandler(invocation);
		if (statement instanceof SimpleStatementHandler
				|| statement instanceof PreparedStatementHandler)
		{
			BoundSql boundSql = statement.getBoundSql();
			String sql = boundSql.getSql();
			RowBounds rowBounds = getRowBounds(statement);
			if (hasBounds(rowBounds))
			{
				if (statement instanceof SimpleStatementHandler)
					sql = dialect.getLimitString(sql, rowBounds.getOffset(),
							rowBounds.getLimit());
				if (statement instanceof PreparedStatementHandler)
					sql = dialect
							.getLimitString(sql, rowBounds.getOffset() > 0);
			}
			if (isCountSql(boundSql))
			{// 计算总数SQL
				sql = dialect.getCountString(sql);
			}
			
			ReflectionUtils.setFieldValue(boundSql, "sql", sql);
		}
		return invocation.proceed();
	}

	private Object parameterize(Invocation invocation) throws Throwable
	{
		Statement statement = (Statement) invocation.getArgs()[0];
		if (statement instanceof PreparedStatement)
		{
			PreparedStatement ps = (PreparedStatement) statement;
			StatementHandler statementHandler = getStatementHandler(invocation);
			RowBounds rowBounds = getRowBounds(statementHandler);
			BoundSql boundSql = statementHandler.getBoundSql();
			if (hasBounds(rowBounds))
			{
				List<ParameterMapping> pms = boundSql.getParameterMappings();
				int parameterSize = pms.size();
				dialect.setLimitParamters(ps, parameterSize,
						rowBounds.getOffset(), rowBounds.getLimit());
			}
		}
		return invocation.proceed();
	}

	private Object handleResultSets(Invocation invocation) throws Throwable
	{
		ResultSetHandler resultSet = (ResultSetHandler) invocation.getTarget();
		// 不用浪费性能做属性存在判断
		RowBounds rowBounds = (RowBounds) ReflectionUtils.getFieldValue(resultSet,
				"rowBounds");
		if (rowBounds.getLimit() > 0
				&& rowBounds.getLimit() < RowBounds.NO_ROW_LIMIT)
		{
			// 强制不允许游标分页
		    ReflectionUtils.setFieldValue(resultSet, "rowBounds", RowBounds.DEFAULT);
		}
		return invocation.proceed();
	}

}