package com.winning.mybatis.interceptor;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class Dialect
{
    protected Logger log = LoggerFactory.getLogger (Dialect.class);
    public static String ROWS_COUNT = "ROWS_COUNT";
    public static String OFFSET = "OFFSET";
    public static String LIMIT = "LIMIT";

    public String getLimitString (String query, int offset, int limit)
    {
        return getLimitString (query, offset > 0);
    }

    public String getLimitString (String query, boolean hasOffset)
    {
        throw new UnsupportedOperationException ("paged queries not supported");
    }

    protected String getCountString (String sql)
    {
        StringBuffer pagingSelect = new StringBuffer (sql.length () + 100);
        pagingSelect.append ("select count(1) " + ROWS_COUNT + " from ( ");
        pagingSelect.append (sql);
        pagingSelect.append (" ) ");
        return pagingSelect.toString ();
    }

    public abstract void setLimitParamters (PreparedStatement ps, int parameterSize, int offset, int limit)
                                                                                                           throws SQLException;

}