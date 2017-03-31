package com.winning.kbms.core.redis;

public interface RedisDataSource <T>
{
    public void returnResource (T resource, boolean broken);

    public T getRedisClient ();
}
