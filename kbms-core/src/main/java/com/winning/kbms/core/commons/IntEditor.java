package com.winning.kbms.core.commons;

import java.beans.PropertyEditorSupport;

import org.apache.commons.lang.StringUtils;

/**
 * 
 * @author gang.liu
 * @date 2013-4-26
 */
public final class IntEditor extends PropertyEditorSupport
{
    @Override
    public void setAsText (String text) throws IllegalArgumentException
    {
        if (StringUtils.isEmpty (text))
            text = "0";

        setValue (Integer.parseInt (text));
    }

    @Override
    public String getAsText ()
    {
        if (getValue () == null)
            return null;

        return getValue ().toString ();
    }
}
