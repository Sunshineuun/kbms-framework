package com.winning.kbms.core.utils;

import com.winning.domain.kbms.core.WsUser;

public class SecurityUtils
{
    private final static ThreadLocal <WsUser> userThreadLocal = new ThreadLocal <WsUser> ();

    public static WsUser getCurrUser ()
    {
        return userThreadLocal.get ();
    }

    public static void setCurrUser (WsUser user)
    {
        userThreadLocal.set (user);
    }

    public static void removeCurrUser ()
    {
        userThreadLocal.remove ();
    }

    public static boolean hasPermission (String permission)
    {
        return userThreadLocal.get ().getAuthorities ().contains (permission);
    }
}
