/*
 * ================================================================
 * 基干系统：上海金仕达卫宁软件有限公司 Web::Java DB基干系统 (Patrasche 3.0)
 * 
 * 业务系统：框架基盘
 * 
 * 
 * $Id: MapUtils.java,v 1.6 2016/05/16 09:00:00 yuanfeng Exp $
 * ================================================================
 */
package com.winning.utils;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;


public class MapUtils {

    @SuppressWarnings("rawtypes")
    public static <T> T get(Map map, Object key) {
        return get(map, key, null);
    }


    @SuppressWarnings({
        "rawtypes", "unchecked"
    })
    public static <T> T get(Map map, Object key, T def) {
        if (map.containsKey(key)) {
            if (def == null)
                return (T) map.get(key);
            if (map.get(key).getClass().isAssignableFrom(def.getClass()))
                return (T) map.get(key);
        }
        return def;
    }


    @SuppressWarnings({
        "unchecked", "rawtypes"
    })
    public static <K, V> Map newMap(Object... args) {
        Map map = new HashMap(args.length / 2);
        int i = 0;
        while (i < args.length) {
            map.put(args[i++], args[i++]);
        }
        return map;
    }


    @SuppressWarnings({
        "rawtypes", "unchecked"
    })
    public static LinkedHashMap newLinkedHashMap(Object... args) {
        LinkedHashMap map = new LinkedHashMap();
        int i = 0;
        while (i < args.length) {
            map.put(args[i++], args[i++]);
        }
        return map;
    }


    @SuppressWarnings({
        "rawtypes", "unchecked"
    })
    public static void copyMap(Map desc, Map orgi) {
        for (Object entry : orgi.entrySet()) {
            desc.put(((Map.Entry) entry).getKey(),
                    ((Map.Entry) entry).getValue());
        }
    }


    public static boolean isEmpty(Map<?, ?> map) {
        return map == null || map.isEmpty();
    }


    public static boolean isNotEmpty(Map<?, ?> map) {
        return !isEmpty(map);
    }
}


/* Copyright (C) 2016, 上海金仕达卫宁软件科技有限公司 Project, All Rights Reserved. */