/*
 * ================================================================
 * 基干系统：上海金仕达卫宁软件有限公司 Web::Java DB基干系统 (Patrasche 3.0)
 * 
 * 业务系统：框架基盘
 * 
 * 
 * $Id: ListUtils.java,v 1.6 2016/05/16 09:00:00 yuanfeng Exp $
 * ================================================================
 */
package com.winning.utils;

import java.util.ArrayList;
import java.util.List;


public class ListUtils {
    
    
    /**
     * 去除空值
     */
    public static List<Object> removeNull(List<Object> list) {
        if (list == null || list.isEmpty())
            return list;

        List<Object> newlist = new ArrayList<Object>();
        for (Object obj : list) {
            if (obj != null)
                newlist.add(obj);
        }
        return newlist;
    }
}

/* Copyright (C) 2016, 上海金仕达卫宁软件科技有限公司 Project, All Rights Reserved. */