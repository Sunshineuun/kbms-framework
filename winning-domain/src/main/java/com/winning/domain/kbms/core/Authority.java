package com.winning.domain.kbms.core;

public class Authority extends ModifyDomain
{
    private static final long serialVersionUID = 1L;
    private String menuId;
    private String authParentCode;
    private String authChildCode;
    private int authType;

    public String getAuthParentCode ()
    {
        return authParentCode;
    }

    public void setAuthParentCode (String authParentCode)
    {
        this.authParentCode = authParentCode;
    }

    public String getAuthChildCode ()
    {
        return authChildCode;
    }

    public void setAuthChildCode (String authChildCode)
    {
        this.authChildCode = authChildCode;
    }

    public String getMenuId ()
    {
        return menuId;
    }

    public void setMenuId (String menuId)
    {
        this.menuId = menuId;
    }

    public int getAuthType ()
    {
        return authType;
    }

    public void setAuthType (int authType)
    {
        this.authType = authType;
    }

    @Override
    public String toString ()
    {
        return getAuthParentCode () + ":" + getAuthChildCode ();
    }

}
