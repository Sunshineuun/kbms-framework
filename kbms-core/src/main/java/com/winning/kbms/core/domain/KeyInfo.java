package com.winning.kbms.core.domain;

import org.springframework.util.Assert;

import com.winning.kbms.core.dao.BaseDao;
import com.winning.kbms.core.utils.ApplicationContextUtils;

public class KeyInfo
{
    private int keyMax;
    private int keyMin;
    private int poolSize;
    private int nextKey;
    private String keyName;

    public KeyInfo (int poolSize, String keyName)
    {
        Assert.notNull (keyName);

        this.poolSize = poolSize;
        this.keyName = keyName;
        initDB ();
        retrieveFormDB ();
    }

    private void initDB ()
    {
        String countSql = "SELECT COUNT(1) FROM KBMS_KEYS WHERE KEY_NAME = '" + keyName + "'";
        BaseDao baseDao = ApplicationContextUtils.getBean ("kbmsBaseDao");
        int count = baseDao.countBySql (countSql);
        if (count > 0)
            return;

        String insertSql = "INSERT INTO KBMS_KEYS(KEY_NAME,KEY_VALUE) values ('" + keyName + "',0)";
        baseDao.insertBySql (insertSql);
    }

    public int getKeyMax ()
    {
        return keyMax;
    }

    public void setKeyMax (int keyMax)
    {
        this.keyMax = keyMax;
    }

    public int getKeyMin ()
    {
        return keyMin;
    }

    public void setKeyMin (int keyMin)
    {
        this.keyMin = keyMin;
    }

    public int getPoolSize ()
    {
        return poolSize;
    }

    public void setPoolSize (int poolSize)
    {
        this.poolSize = poolSize;
    }

    public int getNextKey ()
    {
        synchronized (this)
        {
            if (nextKey > keyMax)
            {
                retrieveFormDB ();
            }
            return nextKey++;
        }
    }

    public int getNextStringKey ()
    {
        if (nextKey > keyMax)
        {
            retrieveFormDB ();
        }
        return nextKey++;
    }

    public String getKeyName ()
    {
        return keyName;
    }

    public void setKeyName (String keyName)
    {
        this.keyName = keyName;
    }

    private void retrieveFormDB ()
    {
        String updateSql = "UPDATE KBMS_KEYS SET KEY_VALUE = KEY_VALUE + " + poolSize + " WHERE KEY_NAME = '" + keyName
                           + "'";
        String selectSql = "SELECT KEY_VALUE FROM KBMS_KEYS WHERE KEY_NAME = '" + keyName + "'";

        BaseDao baseDao = ApplicationContextUtils.getBean ("kbmsBaseDao");
        baseDao.updateBySql (updateSql);
        int keyFormDB = baseDao.countBySql (selectSql);
        keyMax = keyFormDB;
        keyMin = keyFormDB - poolSize + 1;
        nextKey = keyMin;
    }
}
