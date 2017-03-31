package com.winning.kbms.core.commons;

import java.util.HashMap;
import java.util.Map;

import com.winning.kbms.core.utils.ContextUtils;

public class ProgressBarInfo
{
    public static boolean checkExist (String _key)
    {
        if (ContextUtils.getSession ().getAttribute (_key) != null)
            return true;
        return false;
    }

    @SuppressWarnings ("unchecked")
    public static Map <String, Object> getProgressBarInfo (String _key)
    {
        return (Map <String, Object>) ContextUtils.getSession ().getAttribute (_key);
    }

    public static void initProgressBarInfo (String _key)
    {
        ContextUtils.getSession ().setAttribute (_key, new HashMap <String, Object> ());
    }

    public static void setProgressBarInfo (String _key, Object... objs)
    {
        Map <String, Object> info = getProgressBarInfo (_key);
        int i = 0;
        while (i < objs.length)
        {
            info.put ((String) objs[i++], objs[i++]);
        }
    }

    public static void removeProgressBarInfo (String _key)
    {
        ContextUtils.getSession ().removeAttribute (_key);
    }
}
