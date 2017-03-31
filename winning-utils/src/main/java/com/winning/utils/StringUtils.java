/*
 * ================================================================
 * 基干系统：上海金仕达卫宁软件有限公司 Web::Java DB基干系统 (Patrasche 3.0)
 * 
 * 业务系统：框架基盘
 * 
 * 
 * $Id: StringUtils.java,v 1.6 2016/05/16 09:00:00 yuanfeng Exp $
 * ================================================================
 */
package com.winning.utils;

public class StringUtils {
    /**
     * To convert the first character of string to upper case 将字符串的首字母变为大写
     * 
     * @param srcString
     *        the source string
     * @return the result after converted
     */
    public static String toUpperCaseInitial(String srcString) {
        if (org.apache.commons.lang.StringUtils.isEmpty(srcString))
            return "";
        StringBuilder sb = new StringBuilder();
        sb.append(Character.toUpperCase(srcString.charAt(0)));
        sb.append(srcString.substring(1));

        return sb.toString();

    }


    /**
     * To convert the first character of string to lower case 将字符串的首字母变为小写
     * 
     * @param srcString
     *        the source string
     * @return the result after converted
     */
    public static String toLowerCaseInitial(String srcString) {
        if (org.apache.commons.lang.StringUtils.isEmpty(srcString))
            return "";
        StringBuilder sb = new StringBuilder();
        sb.append(Character.toLowerCase(srcString.charAt(0)));
        sb.append(srcString.substring(1));

        return sb.toString();

    }


    public static boolean equalsIgnoreNull(String str1, String str2) {
        if (str1 == null || str2 == null) {
            return false;
        }
        return str1.equals(str2);
    }
}

/* Copyright (C) 2016, 上海金仕达卫宁软件科技有限公司 Project, All Rights Reserved. */
