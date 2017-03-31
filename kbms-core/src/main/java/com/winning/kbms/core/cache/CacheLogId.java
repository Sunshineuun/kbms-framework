package com.winning.kbms.core.cache;

import java.util.HashMap;
import java.util.Map;


public class CacheLogId {
    public static Map<String, Map<String, String>> logMap = new HashMap<String, Map<String, String>>();

    public static Map<String, String> getValue(String key) {
        return logMap.get(key);
    }
    
    public static void add(String key, Map<String, String> value) {
        logMap.put(key, value);
    }


    public static void remove(String key) {
        logMap.remove(key);
    }
}
