package com.winning.kbms.core.dao.impl;

import java.io.Serializable;
import java.util.Collection;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.session.mgt.eis.AbstractSessionDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.winning.kbms.core.repositorys.ShiroSessionRepository;

/**
 * 重新实现Shiro的session管理，使用通过缓存管理session,达到session共享的目的
 * 
 * @author liugang
 * 
 */
public class CustomShiroSessionDaoImpl extends AbstractSessionDAO
{
    private static final Logger logger = LoggerFactory.getLogger (CustomShiroSessionDaoImpl.class);

    private ShiroSessionRepository shiroSessionRepository;

    public ShiroSessionRepository getShiroSessionRepository ()
    {
        return shiroSessionRepository;
    }

    public void setShiroSessionRepository (ShiroSessionRepository shiroSessionRepository)
    {
        this.shiroSessionRepository = shiroSessionRepository;
    }

    @Override
    public void update (Session session) throws UnknownSessionException
    {
        shiroSessionRepository.saveSession (session);
    }

    @Override
    public void delete (Session session)
    {
        if (session == null)
        {
            logger.error ("删除的 session 为空！");
            return;
        }

        shiroSessionRepository.deleteSession (session.getId ());
    }

    @Override
    public Collection <Session> getActiveSessions ()
    {
        return shiroSessionRepository.getAllSessions ();
    }

    @Override
    protected Serializable doCreate (Session session)
    {
        Serializable sessionId = this.generateSessionId (session);
        this.assignSessionId (session, sessionId);
        shiroSessionRepository.saveSession (session);
        return sessionId;
    }

    @Override
    protected Session doReadSession (Serializable sessionId)
    {
        return shiroSessionRepository.getSession (sessionId);
    }

}
