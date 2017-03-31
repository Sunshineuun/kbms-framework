package com.winning.kbms.core.utils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.winning.kbms.core.domain.KeyInfo;

public class KeyGenerator
{
    private static final Map <String, KeyInfo> keygens = new ConcurrentHashMap <String, KeyInfo> (10);
    private static final int POOL_SIZE = 2;

    private KeyGenerator ()
    {
    }

    public static KeyInfo getKeyInfo (String keyName)
    {
        if (keygens.containsKey (keyName))
        {
            return keygens.get (keyName);
        }
        else
        {
            synchronized (keyName)
            {
                if (!keygens.containsKey (keyName))
                {
                    KeyInfo keyInfo = new KeyInfo (POOL_SIZE, keyName);
                    keygens.put (keyName, keyInfo);
                    return keyInfo;
                }
                else
                {
                    return keygens.get (keyName);
                }
            }
        }
    }

    public static int getNextKey (String keyName)
    {
        return getKeyInfo (keyName).getNextKey ();
    }
}
