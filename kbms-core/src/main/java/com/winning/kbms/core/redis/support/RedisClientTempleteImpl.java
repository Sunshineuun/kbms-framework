package com.winning.kbms.core.redis.support;

import static java.lang.reflect.Proxy.newProxyInstance;

import java.io.Serializable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import org.apache.commons.lang.SerializationUtils;
import org.springframework.util.Assert;

import redis.clients.jedis.Jedis;

import com.winning.kbms.core.redis.JedisCommands;
import com.winning.kbms.core.redis.RedisClientTemplete;
import com.winning.kbms.core.redis.RedisDataSource;

public class RedisClientTempleteImpl <K extends Serializable, V extends Serializable> implements
                                                                                      RedisClientTemplete <K, V>
{
    private final RedisDataSource <Jedis> redisDataSource;

    private final JedisCommands jedisCommandsProxy;

    public RedisClientTempleteImpl (RedisDataSource <Jedis> redisDataSource)
    {
        Assert.notNull (redisDataSource, "Redis 数据源不能为空！");

        this.redisDataSource = redisDataSource;
        this.jedisCommandsProxy = (JedisCommands) newProxyInstance (redisDataSource.getRedisClient ().getClass ()
                                                                                   .getClassLoader (), new Class[]
        { JedisCommands.class }, new ResourceProxy ());
    }

    @Override
    public void disconnect ()
    {
        redisDataSource.getRedisClient ().disconnect ();
    }

    public JedisCommands getJedisCommands ()
    {
        return jedisCommandsProxy;
    }

    @SuppressWarnings ("unchecked")
    public V getObj (K key)
    {
        byte[] bytes = getJedisCommands ().get (SerializationUtils.serialize (key));
        if (bytes == null)
            return null;
        return (V) SerializationUtils.deserialize (bytes);
    }

    private class ResourceProxy implements InvocationHandler
    {
        @Override
        public Object invoke (Object proxy, Method method, Object[] args) throws Throwable
        {
            boolean broken = false;
            Jedis redisClient = redisDataSource.getRedisClient ();
            try
            {
                return method.invoke (redisClient, args);
            }
            catch (Throwable e)
            {
                broken = true;
                throw e;
            }
            finally
            {
                redisDataSource.returnResource (redisClient, broken);
            }
        }
    }

}
