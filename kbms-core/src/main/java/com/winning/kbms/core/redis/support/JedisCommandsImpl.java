package com.winning.kbms.core.redis.support;

import java.net.URI;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisShardInfo;

import com.winning.kbms.core.redis.JedisCommands;

public class JedisCommandsImpl extends Jedis implements JedisCommands
{
    public JedisCommandsImpl (final String host)
    {
        super (host);
    }

    public JedisCommandsImpl (final String host, final int port)
    {
        super (host, port);
    }

    public JedisCommandsImpl (final String host, final int port, final int timeout)
    {
        super (host, port, timeout);
    }

    public JedisCommandsImpl (JedisShardInfo shardInfo)
    {
        super (shardInfo);
    }

    public JedisCommandsImpl (URI uri)
    {
        super (uri);
    }
}
