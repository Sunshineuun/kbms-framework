/*
 * ================================================================
 * 基干系统：上海金仕达卫宁软件有限公司 Web::Java DB基干系统 (Patrasche 3.0)
 * 
 * 业务系统：框架基盘
 * 
 * 
 * $Id: CharsetUtils.java,v 1.6 2016/05/16 09:00:00 yuanfeng Exp $
 * ================================================================
 */
package com.winning.utils;

import java.io.UnsupportedEncodingException;
import java.util.Locale;

/**
 * @version 1.0 字符集常用工具
 */
public class CharsetUtils
{
    /**
     * ISO转UTF8
     * 
     * @param str
     * @return
     */
    public static String getISO2UTF (String str)
    {
        return convertEncoding (str, "ISO-8859-1", "UTF-8");
    }

    public static String getUTF2ISO (String str)
    {
        return convertEncoding (str, "UTF-8", "ISO-8859-1");
    }

    /**
     * 转换编码方式
     * 
     * @param str
     * @param old_charset
     * @param new_charset
     * @return
     */
    public static String convertEncoding (String str, String old_charset, String new_charset)
    {
        try
        {
            return new String (str.getBytes (old_charset), new_charset);
        }
        catch (UnsupportedEncodingException e)
        {
            e.printStackTrace ();
            return "";
        }
    }

    /**
     * 获得系统的charset
     * 
     * @return
     */
    public static String getDefaultCharset ()
    {
        Locale locale = Locale.getDefault ();
        String country = locale.getCountry ();
        if (country.equals ("CN"))
        {
            return "GBK";
        }
        else if (country.endsWith ("TW") || country.endsWith ("HK"))
        {
            return "BIG5";
        }
        else
        {
            return "UTF-8";
        }
    }

    public static String getEncoding (String str)
    {
        String encode = "UTF-8";
        try
        {
            if (str.equals (new String (str.getBytes (encode), encode)))
            {
                return encode;
            }
        }
        catch (Exception e)
        {
        }
        encode = "GB2312";
        try
        {
            if (str.equals (new String (str.getBytes (encode), encode)))
            {
                return encode;
            }
        }
        catch (Exception e)
        {
        }
        encode = "GBK";
        try
        {
            if (str.equals (new String (str.getBytes (encode), encode)))
            {
                return encode;
            }
        }
        catch (Exception e)
        {
        }
        encode = "ISO-8859-1";
        try
        {
            if (str.equals (new String (str.getBytes (encode), encode)))
            {
                return encode;
            }
        }
        catch (Exception e)
        {
        }
        return "";
    }

}

/* Copyright (C) 2016, 上海金仕达卫宁软件科技有限公司 Project, All Rights Reserved. */