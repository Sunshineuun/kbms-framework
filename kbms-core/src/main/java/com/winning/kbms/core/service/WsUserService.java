package com.winning.kbms.core.service;

import com.winning.domain.kbms.core.WsUser;

public interface WsUserService
{

    public WsUser getWsUserByKey (String key);

    public void clearCacheByUserName (String userName);
    
    public void clearCache ();
    
    public void clearCacheByWsKey (String wsKey);

}