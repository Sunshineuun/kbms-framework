package com.winning.mybatis.utils;

import java.io.IOException;
import java.io.Reader;
import java.util.Properties;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.winning.mybatis.DynamicSqlDao;
import com.winning.mybatis.support.DynamicSqlDaoImpl;

public class MyBatisUtils
{
    private final static Logger logger = LoggerFactory.getLogger (MyBatisUtils.class);

    public static SqlSessionFactory getSqlSessionFactory (String driver, String url, String username, String password)
    {
        SqlSessionFactory sqlSessionFactory = null;
        try
        {
            Properties properties = new Properties ();
            properties.setProperty ("driver", driver);
            properties.setProperty ("url", url);
            properties.setProperty ("username", username);
            properties.setProperty ("password", password);
            Reader reader = Resources.getResourceAsReader ("dynamicSqlMap/configruation.xml");
            sqlSessionFactory = new SqlSessionFactoryBuilder ().build (reader, properties);
        }
        catch (IOException e)
        {
            logger.error ("动态连接数据库出错！", e);
        }
        return sqlSessionFactory;
    }

    public static DynamicSqlDao generateDynamicSqlDao (String driver, String url, String username, String password)
    {
        SqlSessionFactory sqlSessionFactory = getSqlSessionFactory (driver, url, username, password);
        SqlSession sqlSession = sqlSessionFactory.openSession ();
        return new DynamicSqlDaoImpl (sqlSession);
    }
}
