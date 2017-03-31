/*
 * ================================================================
 * 基干系统：上海金仕达卫宁软件有限公司 Web::Java DB基干系统 (Patrasche 3.0)
 * 
 * 业务系统：框架基盘
 * 
 * 
 * $Id: MatcherUtils.java,v 1.6 2016/05/16 09:00:00 yuanfeng Exp $
 * ================================================================
 */
package com.winning.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * 
 * @author gang.liu
 * 
 */
public class MatcherUtils {

    public static Matcher getMatcherByInsensitive(String str, String regx) {
        Pattern pattern = Pattern.compile(regx, Pattern.CASE_INSENSITIVE);
        return pattern.matcher(str);
    }


    public static Matcher getMatcher(String str, String regx) {
        Pattern pattern = Pattern.compile(regx);
        return pattern.matcher(str);
    }


    public static boolean find(String str, String regx) {
        Pattern pattern = Pattern.compile(regx);
        Matcher matcher = pattern.matcher(str);
        return matcher.find();
    }

}

/* Copyright (C) 2016, 上海金仕达卫宁软件科技有限公司 Project, All Rights Reserved. */