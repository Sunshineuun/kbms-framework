package com.winning.kbms.core.repositorys;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;

import org.apache.shiro.session.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.Cache;
import org.springframework.cache.Cache.ValueWrapper;

public class CacheShiroSessionRepository implements ShiroSessionRepository
{
    private static final Logger logger = LoggerFactory.getLogger (CacheShiroSessionRepository.class);

    @Autowired
    @Qualifier ("sessionCache")
    private Cache sessionCache;

    @Override
    public void saveSession (Session session)
    {
        if (session == null || session.getId () == null)
        {
            logger.error ("session 或 sessionId 为空！");
            return;
        }
        sessionCache.put (session.getId (), session);
    }

    @Override
    public void deleteSession (Serializable sessionId)
    {
        if (sessionId == null)
        {
            logger.error ("sessionId 为空！");
            return;
        }

        sessionCache.evict (sessionId);
    }

    @Override
    public Session getSession (Serializable sessionId)
    {
        if (sessionId == null)
        {
            logger.error ("sessionId 为空！");
            return null;
        }

        ValueWrapper vw = sessionCache.get (sessionId);
        if (vw == null)
            return null;

        return (Session) vw.get ();
    }

    @Override
    public Collection <Session> getAllSessions ()
    {
        Object nativeCache = sessionCache.getNativeCache ();
        Set <Session> sessionSet = new HashSet <Session> ();
        if (nativeCache instanceof Ehcache)
        {
            Ehcache ehcache = (Ehcache) nativeCache;

            Map <Object, Element> map = ehcache.getAll (ehcache.getKeys ());
            for (Map.Entry <Object, Element> entry : map.entrySet ())
            {
                sessionSet.add ((Session) entry.getValue ().getObjectValue ());
            }
        }
        else
        {
            logger.warn ("sessionCache 不确定类型！");
        }
        return sessionSet;
    }
}
