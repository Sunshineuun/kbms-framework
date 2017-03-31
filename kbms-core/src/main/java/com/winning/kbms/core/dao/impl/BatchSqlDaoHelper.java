package com.winning.kbms.core.dao.impl;

import java.util.List;

import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import com.winning.kbms.core.utils.ApplicationContextUtils;
import com.winning.mybatis.DynamicSqlDao;
import com.winning.mybatis.support.DynamicSqlDaoImpl;

public final class BatchSqlDaoHelper <T>
{
    private SqlSession sqlSession = null;
    private int commitCount = 1000;
    private int index = 0;
    private boolean execBegin = false;
    private SqlSessionFactory sqlSessionFactory;
    private DynamicSqlDao dynamicSqlDao;

    public BatchSqlDaoHelper (int commitCount)
    {
        this (null, commitCount);
    }

    public BatchSqlDaoHelper (SqlSessionFactory sqlSessionFactory, int commitCount)
    {
        this.sqlSessionFactory = sqlSessionFactory;

        if (commitCount > 0)
            this.commitCount = commitCount;
    }

    public void begin ()
    {
        execBegin = true;

        if (sqlSessionFactory == null)
            sqlSessionFactory = ApplicationContextUtils.getBean ("sqlSessionFactory", SqlSessionFactory.class);

        sqlSession = sqlSessionFactory.openSession (ExecutorType.BATCH, false);
        dynamicSqlDao = new DynamicSqlDaoImpl (sqlSession);
    }

    public void add (T obj, String statement) throws Exception
    {
        if (!execBegin)
            throw new Exception ("必须先执行begin方法！");

        try
        {
            sqlSession.insert (statement, obj);
            if (++index % commitCount == 0)
            {
                sqlSession.commit ();
                sqlSession.clearCache ();
            }
        }
        catch (Exception e)
        {
            sqlSession.rollback ();
            throw e;
        }
    }

    public void add (String sql, T obj) throws Exception
    {
        if (!execBegin)
            throw new Exception ("必须先执行begin方法！");

        try
        {
            dynamicSqlDao.executeInsertDynamic (sql, obj);
            if (++index % commitCount == 0)
            {
                sqlSession.commit ();
                sqlSession.clearCache ();
            }
        }
        catch (Exception e)
        {
            sqlSession.rollback ();
            throw e;
        }
    }

    public void update (T obj, String statement) throws Exception
    {
        if (!execBegin)
            throw new Exception ("必须先执行begin方法！");

        try
        {
            sqlSession.update (statement, obj);
            if (++index % commitCount == 0)
            {
                sqlSession.commit ();
                sqlSession.clearCache ();
            }
        }
        catch (Exception e)
        {
            sqlSession.rollback ();
            throw e;
        }
    }

    public void update (String sql, T obj) throws Exception
    {
        if (!execBegin)
            throw new Exception ("必须先执行begin方法！");

        try
        {
            dynamicSqlDao.executeUpdateDynamic (sql, obj);
            if (++index % commitCount == 0)
            {
                sqlSession.commit ();
                sqlSession.clearCache ();
            }
        }
        catch (Exception e)
        {
            sqlSession.rollback ();
            throw e;
        }
    }

    public void delete (T obj, String statement) throws Exception
    {
        if (!execBegin)
            throw new Exception ("必须先执行begin方法！");

        try
        {
            sqlSession.delete (statement, obj);
            if (++index % commitCount == 0)
            {
                sqlSession.commit ();
                sqlSession.clearCache ();
            }
        }
        catch (Exception e)
        {
            sqlSession.rollback ();
            throw e;
        }
    }

    public void delete (String sql, T obj) throws Exception
    {
        if (!execBegin)
            throw new Exception ("必须先执行begin方法！");

        try
        {
            dynamicSqlDao.executeDeleteDynamic (sql, obj);
            if (++index % commitCount == 0)
            {
                sqlSession.commit ();
                sqlSession.clearCache ();
            }
        }
        catch (Exception e)
        {
            sqlSession.rollback ();
            throw e;
        }
    }

    public void end ()
    {
        if (index % commitCount != 0)
        {
            sqlSession.commit ();
            sqlSession.clearCache ();
        }
        sqlSession.close ();
    }

    @SuppressWarnings ("rawtypes")
    public static void addList (List list, String statement)
    {
        SqlSessionFactory sqlSessionFactory = ApplicationContextUtils.getBean ("sqlSessionFactory",
                                                                               SqlSessionFactory.class);
        SqlSession sqlSession = sqlSessionFactory.openSession (ExecutorType.BATCH, false);
        try
        {
            for (int i = 0; i < list.size (); i++)
            {
                sqlSession.insert (statement, list.get (i));
                if (i % 1000 == 0 || i == list.size () - 1)
                {
                    sqlSession.commit ();
                    sqlSession.clearCache ();
                }
            }
        }
        finally
        {
            sqlSession.close ();
        }
    }

    @SuppressWarnings ("rawtypes")
    public static void updateList (List list, String statement)
    {
        SqlSessionFactory sqlSessionFactory = ApplicationContextUtils.getBean ("sqlSessionFactory",
                                                                               SqlSessionFactory.class);
        SqlSession sqlSession = sqlSessionFactory.openSession (ExecutorType.BATCH, false);
        try
        {
            for (int i = 0; i < list.size (); i++)
            {
                sqlSession.update (statement, list.get (i));
                if (i % 1000 == 0 || i == list.size () - 1)
                {
                    sqlSession.commit ();
                    sqlSession.clearCache ();
                }
            }
        }
        finally
        {
            sqlSession.close ();
        }
    }

    @SuppressWarnings ("rawtypes")
    public static void deleteList (List list, String statement)
    {
        SqlSessionFactory sqlSessionFactory = ApplicationContextUtils.getBean ("sqlSessionFactory",
                                                                               SqlSessionFactory.class);
        SqlSession sqlSession = sqlSessionFactory.openSession (ExecutorType.BATCH, false);
        try
        {
            for (int i = 0; i < list.size (); i++)
            {
                sqlSession.delete (statement, list.get (i));
                if (i % 1000 == 0 || i == list.size () - 1)
                {
                    sqlSession.commit ();
                    sqlSession.clearCache ();
                }
            }
        }
        finally
        {
            sqlSession.close ();
        }
    }
}
