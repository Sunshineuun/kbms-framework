package com.winning.mybatis.interceptor;

import java.sql.Connection;
import java.util.Properties;

import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.factory.DefaultObjectFactory;
import org.apache.ibatis.reflection.factory.ObjectFactory;
import org.apache.ibatis.reflection.wrapper.DefaultObjectWrapperFactory;
import org.apache.ibatis.reflection.wrapper.ObjectWrapperFactory;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Intercepts (
{ @Signature (type = StatementHandler.class, method = "prepare", args =
{ Connection.class }) })
public final class PagingInterceptor implements Interceptor
{
    private final Logger logger = LoggerFactory.getLogger (getClass ());
    private final ObjectFactory DEFAULT_OBJECT_FACTORY = new DefaultObjectFactory ();
    private final ObjectWrapperFactory DEFAULT_OBJECT_WRAPPER_FACTORY = new DefaultObjectWrapperFactory ();
    private String dialect = "oracle";

    @Override
    public Object intercept (Invocation invocation) throws Throwable
    {
        StatementHandler statementHandler = (StatementHandler) invocation.getTarget ();
        MetaObject metaStatementHandler = MetaObject.forObject (statementHandler, DEFAULT_OBJECT_FACTORY,
                                                                DEFAULT_OBJECT_WRAPPER_FACTORY);

        // 分离代理对象链(由于目标类可能被多个拦截器拦截，从而形成多次代理，通过下面的两次循环可以分离出最原始的的目标类)
        while (metaStatementHandler.hasGetter ("h"))
        {
            Object object = metaStatementHandler.getValue ("h");
            metaStatementHandler = MetaObject.forObject (object, DEFAULT_OBJECT_FACTORY, DEFAULT_OBJECT_WRAPPER_FACTORY);
        }

        // 分离最后一个代理对象的目标类
        while (metaStatementHandler.hasGetter ("target"))
        {
            Object object = metaStatementHandler.getValue ("target");
            metaStatementHandler = MetaObject.forObject (object, DEFAULT_OBJECT_FACTORY, DEFAULT_OBJECT_WRAPPER_FACTORY);
        }

        BoundSql boundSql = (BoundSql) metaStatementHandler.getValue ("delegate.boundSql");
        RowBounds rowBounds = (RowBounds) metaStatementHandler.getValue ("delegate.rowBounds");

        if (rowBounds != null && rowBounds != RowBounds.DEFAULT)
        {
            String sql = boundSql.getSql ();

            String pageSql = buildPageSql (sql, rowBounds);
            metaStatementHandler.setValue ("delegate.boundSql.sql", pageSql);

            metaStatementHandler.setValue ("delegate.rowBounds.offset", RowBounds.NO_ROW_OFFSET);
            metaStatementHandler.setValue ("delegate.rowBounds.limit", RowBounds.NO_ROW_LIMIT);
        }
        return invocation.proceed ();
    }

    /**
     * 根据数据库类型，生成特定的分页sql
     * 
     * @param sql
     * @param page
     * @return
     */
    private String buildPageSql (String sql, RowBounds rowBounds)
    {
        if (rowBounds != null)
        {
            StringBuilder pageSql = new StringBuilder ();
            if ("mysql".equals (dialect))
            {
                pageSql = buildPageSqlForMysql (sql, rowBounds);
            }
            else if ("oracle".equals (dialect))
            {
                pageSql = buildPageSqlForOracle (sql, rowBounds);
            }
            else
            {
                return sql;
            }
            return pageSql.toString ();
        }
        else
        {
            return sql;
        }
    }

    /**
     * mysql的分页语句
     * 
     * @param sql
     * @param page
     * @return String
     */
    public StringBuilder buildPageSqlForMysql (String sql, RowBounds rowBounds)
    {
        StringBuilder pageSql = new StringBuilder (100);
        pageSql.append (sql);
        pageSql.append (" limit " + rowBounds.getOffset () + "," + rowBounds.getLimit ());

        logger.debug ("分页查询SQL：{}", pageSql);
        return pageSql;
    }

    /**
     * 参考hibernate的实现完成oracle的分页
     * 
     * @param sql
     * @param page
     * @return String
     */
    public StringBuilder buildPageSqlForOracle (String sql, RowBounds rowBounds)
    {
        StringBuilder pageSql = new StringBuilder (100);
        String beginrow = String.valueOf (rowBounds.getOffset ());
        String endrow = String.valueOf (rowBounds.getOffset () + rowBounds.getLimit ());

        pageSql.append ("select * from ( select temp.*, rownum row_id from ( ");
        pageSql.append (sql);
        pageSql.append (" ) temp where rownum <= ").append (endrow);
        pageSql.append (") where row_id > ").append (beginrow);

        logger.debug ("分页查询SQL：{}", pageSql);
        return pageSql;
    }

    @Override
    public Object plugin (Object target)
    {
        // 当目标类是StatementHandler类型时，才包装目标类，否者直接返回目标本身,减少目标被代理的次数
        if (target instanceof StatementHandler)
        {
            return Plugin.wrap (target, this);
        }
        else
        {
            return target;
        }
    }

    @Override
    public void setProperties (Properties properties)
    {
    }

}
