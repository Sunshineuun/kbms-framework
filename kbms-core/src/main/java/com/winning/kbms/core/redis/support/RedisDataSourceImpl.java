package com.winning.kbms.core.redis.support;

import org.springframework.util.Assert;

import redis.clients.util.Pool;

import com.winning.kbms.core.redis.RedisDataSource;

public class RedisDataSourceImpl <T> implements RedisDataSource <T>
{
    private final Pool <T> pool;

    public RedisDataSourceImpl (Pool <T> pool)
    {
        Assert.notNull (pool, "Redis 连接池不能为空！");

        this.pool = pool;
    }

    @Override
    public void returnResource (T resource, boolean broken)
    {
        if (broken)
        {
            pool.returnBrokenResource (resource);
        }
        else
        {
            pool.returnResource (resource);
        }
    }

    @Override
    public T getRedisClient ()
    {
        return pool.getResource ();
    }

}
