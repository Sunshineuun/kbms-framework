package com.winning.kbms.core.service;

import java.util.List;

import com.winning.domain.kbms.core.Authority;
import com.winning.domain.kbms.core.RoleAuthority;
import com.winning.domain.kbms.core.UserAuthority;

public interface AuthoritzationService
{
    public void authorizeToUser (String userName, List <UserAuthority> userAuthorities);

    public void authorizeToRole (String roleId, List <RoleAuthority> userAuthorities);

    public List <Authority> getUserAuthorityByUserName (String userName);

    public List <Authority> getRoleAuthorityByUserName (String userName);
    
    public List <Authority> getWsUserAuthorityByUserName (String userName);
    
    public List <Authority> getWsRoleAuthorityByUserName (String userName);
}