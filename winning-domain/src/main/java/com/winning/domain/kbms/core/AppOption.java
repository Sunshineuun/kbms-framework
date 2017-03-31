package com.winning.domain.kbms.core;

import java.util.Date;

import com.winning.annotations.mybatis.Exclude;
import com.winning.annotations.mybatis.Table;

@Table (value = "APP_OPTION", resultMapId = "AppOptionMap")
public class AppOption extends ModifyDomain
{
    private static final long serialVersionUID = 1L;
    private String lbdm;
    private String lbmc;
    private String dm;
    private String mc;
    private String cs;
    private String bz;
    private String yxbz;
    private Date gxsj;
    private String pdm;
    private String xtlb;

    public String getXtlb ()
    {
        return xtlb;
    }

    public void setXtlb (String xtlb)
    {
        this.xtlb = xtlb;
    }

    public String getPdm ()
    {
        return pdm;
    }

    public void setPdm (String pdm)
    {
        this.pdm = pdm;
    }

    public String getLbmc ()
    {
        return lbmc;
    }

    public void setLbmc (String lbmc)
    {
        this.lbmc = lbmc;
    }

    public String getLbdm ()
    {
        return lbdm;
    }

    public void setLbdm (String lbdm)
    {
        this.lbdm = lbdm;
    }

    public String getDm ()
    {
        return dm;
    }

    public void setDm (String dm)
    {
        this.dm = dm;
    }

    public String getMc ()
    {
        return mc;
    }

    public void setMc (String mc)
    {
        this.mc = mc;
    }

    public String getCs ()
    {
        return cs;
    }

    public void setCs (String cs)
    {
        this.cs = cs;
    }

    public String getBz ()
    {
        return bz;
    }

    public void setBz (String bz)
    {
        this.bz = bz;
    }

    public String getYxbz ()
    {
        return yxbz;
    }

    public void setYxbz (String yxbz)
    {
        this.yxbz = yxbz;
    }

    public Date getGxsj ()
    {
        return gxsj;
    }

    public void setGxsj (Date gxsj)
    {
        this.gxsj = gxsj;
    }

    @Override
    @Exclude
    public String getUpdateBy ()
    {
        return super.getUpdateBy ();
    }

    @Override
    @Exclude
    public Date getUpdateTime ()
    {
        return super.getUpdateTime ();
    }

}
