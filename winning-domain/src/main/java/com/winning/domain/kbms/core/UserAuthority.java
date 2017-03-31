package com.winning.domain.kbms.core;

public class UserAuthority extends Authority
{
    private static final long serialVersionUID = 1L;
    private String userName;

    public String getUserName ()
    {
        return userName;
    }

    public void setUserName (String userName)
    {
        this.userName = userName;
    }
}
