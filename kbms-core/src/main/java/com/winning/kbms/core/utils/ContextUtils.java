package com.winning.kbms.core.utils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.winning.domain.kbms.core.User;

public final class ContextUtils
{
    public static String hasPermission (String... permissions)
    {
        boolean[] perms = SecurityUtils.getSubject ().isPermitted (permissions);
        StringBuilder sb = new StringBuilder ();
        sb.append ("{");
        String[] splitStr = null;
        for (int i = 0; i < permissions.length; i++)
        {
            splitStr = permissions[i].split (":");
            sb.append (splitStr[splitStr.length - 1]).append (":").append (perms[i]);
            if (i != permissions.length - 1)
                sb.append (",");
        }
        sb.append ("}");
        return sb.toString ();
    }

    public static User getCurrUser ()
    {
        return (User) SecurityUtils.getSubject ().getPrincipal ();
    }

    public static HttpServletRequest getRequest ()
    {
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes ()).getRequest ();
    }

    public static HttpSession getSession ()
    {
        return getRequest ().getSession ();
    }

    public static String getCurrUserJsonString ()
    {
        return JackJson.fromObjectToJson (SecurityUtils.getSubject ().getPrincipal ());
    }

    public static String getCurrUserName ()
    {
        return getCurrUser ().getUserName ();
    }

    public static String encrypt (String username, String password)
    {
        return new Sha256Hash (password, username, 1024).toBase64 ();
    }
}
