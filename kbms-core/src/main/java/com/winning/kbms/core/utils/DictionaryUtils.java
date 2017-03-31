package com.winning.kbms.core.utils;

import java.util.Map;
import java.util.WeakHashMap;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import com.winning.kbms.core.service.DictionaryService;

@Component
public class DictionaryUtils implements ApplicationContextAware
{
    private final static Map <String, Map <String, String>> cacheMap = new WeakHashMap <String, Map <String, String>> ();

    private static DictionaryService dictionaryService;

    public static String getValue (String typeCode, String key)
    {
        Map <String, String> dictOptions = null;
        while ((dictOptions = getDictionaryOnWeakHashMap (typeCode)) != null)
        {
            String value = dictOptions.get (key);
            if (value == null)
                break;

            return value;
        }
        return key;
    }

    public static String getValueByQueryId (String queryId, String key)
    {
        Map <String, String> dictOptions = null;
        while ((dictOptions = getDictionaryByQueryIdOnWeakHashMap (queryId)) != null)
        {
            String value = dictOptions.get (key);
            if (value == null)
                break;

            return value;
        }
        return key;
    }

    private static Map <String, String> getDictionaryByQueryIdOnWeakHashMap (String queryId)
    {
        if (cacheMap.containsKey (queryId))
            return cacheMap.get (queryId);

        Map <String, String> map = getDictionaryOnQueryId (queryId);
        cacheMap.put (queryId, map);
        return map;
    }

    public static Map <String, String> getDictionaryOnQueryId (String queryId)
    {
        return dictionaryService.getDictionaryOnQueryId (queryId);
    }

    public static Map <String, String> getDictionaryByTypeCode (String typeCode)
    {
        return dictionaryService.getDictionaryByTypeCode (typeCode);
    }

    private static Map <String, String> getDictionaryOnWeakHashMap (String typeCode)
    {
        if (cacheMap.containsKey (typeCode))
            return cacheMap.get (typeCode);

        Map <String, String> map = getDictionaryByTypeCode (typeCode);
        cacheMap.put (typeCode, map);
        return map;
    }

    @Override
    public void setApplicationContext (ApplicationContext applicationContext) throws BeansException
    {
        dictionaryService = (DictionaryService) applicationContext.getBean ("dictionaryService");
    }
}