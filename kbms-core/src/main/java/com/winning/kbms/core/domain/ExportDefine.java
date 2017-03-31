package com.winning.kbms.core.domain;

import com.winning.annotations.enums.DictType;
import com.winning.domain.BaseDomain;

public class ExportDefine extends BaseDomain
{
    private static final long serialVersionUID = 1L;
    private String key; // 对应jqgrid中字段属性jsonmap
    private String name; // 对应jqgrid中字段属性name
    private String title;
    private String dictionaryCode;
    private DictType dictionaryType;

    public ExportDefine ()
    {
    }

    public ExportDefine (String key, String name, String title, String dictionaryCode, DictType dictionaryType)
    {
        this.key = key;
        this.name = name;
        this.title = title;
        this.dictionaryCode = dictionaryCode;
        this.dictionaryType = dictionaryType;
    }

    public ExportDefine (String key, String name, String title)
    {
        this.key = key;
        this.name = name;
        this.title = title;
    }

    public ExportDefine (String key, String title)
    {
        this.key = key;
        this.name = key;
        this.title = title;
    }

    public String getName ()
    {
        return name;
    }

    public void setName (String name)
    {
        this.name = name;
    }

    public String getKey ()
    {
        return key;
    }

    public void setKey (String key)
    {
        this.key = key;
    }

    public String getTitle ()
    {
        return title;
    }

    public void setTitle (String title)
    {
        this.title = title;
    }

    public String getDictionaryCode ()
    {
        return dictionaryCode;
    }

    public void setDictionaryCode (String dictionaryCode)
    {
        this.dictionaryCode = dictionaryCode;
    }

    public DictType getDictionaryType ()
    {
        return dictionaryType;
    }

    public void setDictionaryType (DictType dictionaryType)
    {
        this.dictionaryType = dictionaryType;
    }
}
