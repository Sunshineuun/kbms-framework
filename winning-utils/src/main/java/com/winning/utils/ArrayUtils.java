/*
 * ================================================================
 * 基干系统：上海金仕达卫宁软件有限公司 Web::Java DB基干系统 (Patrasche 3.0)
 * 
 * 业务系统：框架基盘
 * 
 * 
 * $Id: ArrayUtils.java,v 1.6 2016/05/16 09:00:00 yuanfeng Exp $
 * ================================================================
 */
package com.winning.utils;

import java.lang.reflect.Array;


public class ArrayUtils {
    /**
     * 字符串是否存在于数组中
     * 
     * @param str
     *        字符串
     * @param strs
     *        字符串数组
     * @return 返回是否存在
     */
    public static boolean oneMatchSome(String str, String[] strs) {
        if (str == null) {
            for (String matchStr : strs) {
                if (matchStr == null)
                    return true;
            }
        }

        for (String matchStr : strs) {
            if (str.equals(matchStr))
                return true;
        }

        return false;
    }


    @SuppressWarnings({
        "unchecked"
    })
    public static <T> T[] removeNull(Object[] objs1, Class<T> clazz) {
        T[] objs = (T[]) objs1;
        int i = 0;
        for (T obj : objs) {
            if (obj != null)
                i++;
        }

        T[] newArray = (T[]) Array.newInstance(clazz, i);
        int j = 0;
        for (T obj : objs) {
            if (obj != null)
                newArray[j++] = obj;
        }
        return newArray;
    }
}

/* Copyright (C) 2016, 上海金仕达卫宁软件科技有限公司 Project, All Rights Reserved. */
