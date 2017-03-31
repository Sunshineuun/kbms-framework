package com.winning.kbms.core.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.winning.domain.kbms.core.Authority;
import com.winning.domain.kbms.core.RoleAuthority;
import com.winning.domain.kbms.core.UserAuthority;
import com.winning.kbms.core.dao.BaseDao;
import com.winning.kbms.core.dao.impl.BatchSqlDaoHelper;
import com.winning.kbms.core.service.AuthoritzationService;
import com.winning.kbms.core.service.WsUserService;
import com.winning.kbms.core.utils.ApplicationContextUtils;

@Service ("authoritzationService")
public class AuthoritzationServiceImpl implements AuthoritzationService
{
    @Autowired
    @Qualifier ("kbmsBaseDao")
    private BaseDao baseDao;

    public BaseDao getDao ()
    {
        return baseDao;
    }

    public void authorizeToUser (String userName, List <UserAuthority> userAuthorities)
    {
        removeUserAuthorityByUserName (userName);
        BatchSqlDaoHelper.addList (userAuthorities, "addUserAuthority");
        ApplicationContextUtils.getBean ("shiroDbRealm", ShiroDbRealm.class).clearCachedAuthorizationInfo (userName);
        ApplicationContextUtils.getBean ("wsUserService", WsUserService.class).clearCacheByUserName (userName);
    }

    protected void removeUserAuthorityByUserName (String username)
    {
        getDao ().delete ("removeUserAuthorityByUserName", username);
    }

    @Override
    public List <Authority> getUserAuthorityByUserName (String userName)
    {
        return getDao ().query ("getUserAuthorityByUserName", userName);
    }

    @Override
    public List <Authority> getWsUserAuthorityByUserName (String userName)
    {
        return getDao ().query ("getWsUserAuthorityByUserName", userName);
    }

    @Override
    public List <Authority> getRoleAuthorityByUserName (String userName)
    {
        return getDao ().query ("getRoleAuthorityByUserName", userName);
    }

    @Override
    public List <Authority> getWsRoleAuthorityByUserName (String userName)
    {
        return getDao ().query ("getWsRoleAuthorityByUserName", userName);
    }

    protected void removeRoleAuthorityByRoleId (String roleId)
    {
        getDao ().delete ("removeRoleAuthorityByRoleId", roleId);
    }

    @Override
    public void authorizeToRole (String roleId, List <RoleAuthority> roleAuthorities)
    {
        removeRoleAuthorityByRoleId (roleId);
        BatchSqlDaoHelper.addList (roleAuthorities, "addRoleAuthority");
        ApplicationContextUtils.getBean ("shiroDbRealm", ShiroDbRealm.class).clearCachedAuthorizationInfo ();
        ApplicationContextUtils.getBean ("wsUserService", WsUserService.class).clearCache ();
    }
}
