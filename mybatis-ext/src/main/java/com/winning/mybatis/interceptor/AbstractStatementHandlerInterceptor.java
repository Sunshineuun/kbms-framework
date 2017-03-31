package com.winning.mybatis.interceptor;

import org.apache.ibatis.executor.statement.RoutingStatementHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.session.RowBounds;

import com.winning.utils.ReflectionUtils;

public abstract class AbstractStatementHandlerInterceptor implements Interceptor
{
    protected Dialect dialect;

    public void setDialect (Dialect dialect)
    {
        this.dialect = dialect;
    }

    protected StatementHandler getStatementHandler (Invocation invocation)
    {
        StatementHandler statement = (StatementHandler) invocation.getTarget ();
        if (statement instanceof RoutingStatementHandler)
        {
            statement = (StatementHandler) ReflectionUtils.getFieldValue (statement, "delegate");
        }
        return statement;
    }

    protected RowBounds getRowBounds (StatementHandler statement)
    {
        return (RowBounds) ReflectionUtils.getFieldValue (statement, "rowBounds");
    }

    protected boolean hasBounds (RowBounds rowBounds)
    {
        return (rowBounds != null && rowBounds.getLimit () > 0 && rowBounds.getLimit () < RowBounds.NO_ROW_LIMIT);
    }

}