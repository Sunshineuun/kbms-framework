package com.winning.kbms.core.redis;

import java.io.Serializable;

public interface RedisClientTemplete <K extends Serializable, V extends Serializable>
{
    public V getObj (K key);

    public void disconnect ();
}
