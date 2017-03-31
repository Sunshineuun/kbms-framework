/*
 * ================================================================
 * 基干系统：上海金仕达卫宁软件有限公司 Web::Java DB基干系统 (Patrasche 3.0)
 * 
 * 业务系统：框架基盘
 * 
 * 
 * $Id: DecimalUtils.java,v 1.6 2016/05/16 09:00:00 yuanfeng Exp $
 * ================================================================
 */
package com.winning.utils;

import java.text.DecimalFormat;

/**
 * 数字格式化
 * 
 * @author gang.liu
 * @date 2013-4-3
 */
public class DecimalUtils {
    private final static String DEFAULT_PATTERN = "###,###.##";

    public static String moneyFormat(String str, String pattern) {
        DecimalFormat formater = new DecimalFormat(pattern);
        double num = Double.parseDouble(str);
        return formater.format(num);
    }

    public static String moneyFormat(double num, String pattern) {
        DecimalFormat formater = new DecimalFormat(pattern);
        return formater.format(num);
    }

    public static String moneyFormat(int num, String pattern) {
        DecimalFormat formater = new DecimalFormat(pattern);
        return formater.format(num);
    }

    public static String moneyFormat(String str) {
        DecimalFormat formater = new DecimalFormat(DEFAULT_PATTERN);
        double num = Double.parseDouble(str);
        return formater.format(num);
    }

    public static String moneyFormat(double num) {
        DecimalFormat formater = new DecimalFormat(DEFAULT_PATTERN);
        return formater.format(num);
    }

    public static String moneyFormat(int num) {
        DecimalFormat formater = new DecimalFormat(DEFAULT_PATTERN);
        return formater.format(num);
    }
}

/* Copyright (C) 2016, 上海金仕达卫宁软件科技有限公司 Project, All Rights Reserved. */