/*
 * ================================================================
 * 基干系统：上海金仕达卫宁软件有限公司 Web::Java DB基干系统 (Patrasche 3.0)
 * 
 * 业务系统：框架基盘
 * 
 * 
 * $Id: RandomUIDUtils.java,v 1.6 2016/05/16 09:00:00 yuanfeng Exp $
 * ================================================================
 */
package com.winning.utils;

import org.apache.commons.lang.RandomStringUtils;
import java.text.SimpleDateFormat;
import java.util.UUID;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;


/**
 * <h1>框架基盘</h1><br>
 * 
 * <b>NOTE : 功能概述。</b>
 * <p>
 * 省略 ... 省略 ... 省略 ...
 * </p>
 * 
 * 
 * <p>
 * $Revision: 1.6 $<br>
 * $Date: 2016/04/18 15:56:16 $
 * </p>
 * 
 * @since JVM : J2SDK 1.5.0 : Servlet 2.3/JSP1.2 : Struts 2.0 : SpringMVC 2.0
 * @since Patrasche 3.0
 * 
 * @author 袁峰 yuanfeng ( Shanghai Kinstar Winning Soft）<br>
 */
public class RandomUIDUtils {

    /** 文字序列号 */
    final private static char[] STRPREFIX = {
        '0','1','2','3','4','5','6','7','8','9',
        'a','b','c','d','e','f','g','h','j','k',
        'l','m','n','o','p','q','r','s','t','u',
        'v','w','x','y','z'
    };

    /** 数字序列号 */
    final private static char[] INTPREFIX = {
        '0', '1', '2', '3', '4', '5', '6', '7', '8', '9'
    };

    /** 随机数起始种子 */
    private static int n = 0;

    /** 日期时间格式 01 */
    final private static String DATETIME_PARTTEN_01 = "yyyyMMddHHmmss";
    
    /** 日期时间格式 02 */
    final private static String DATETIME_PARTTEN_02 = "yyMMddHHmmss";
    
    /**
     * 取得指定长度的随机数字序列号
     * 
     * @param length
     *        随机号长度
     * @return 指定长度的随机数字序列号
     */
    private static String getRandomStr(int length) {
        return RandomStringUtils.randomNumeric(length);
    }


    /**
     * 获取20位的String ID 格式：<br>
     * 4位年份+2位月份+2位日期+2位小时+2位分钟+2位秒数+6位随机数
     * 
     * @return 20位UID代码（文字）
     * 
     */
    public static String getUID20() {
        StringBuffer sb = new StringBuffer();
        // 4位年份+2位月份+2位日期+2位小时+2位分钟+2位秒数+6位流水号或6位随机数
        SimpleDateFormat formatter = new SimpleDateFormat(DATETIME_PARTTEN_01);
        sb.append(formatter.format(new Date())).append(getRandomStr(6));

        return sb.toString();
    }


    /**
     * 返回15-16位的long类型主键 格式：<br>
     * 2位年份+2位月份+2位日期+2位小时+2位分钟+2位秒数+4位随机数
     * 
     * @return UID代码（数字）
     * 
     */
    public static long getLongUID() {
        StringBuffer sb = new StringBuffer();

        /* 2位年份+2位月份+2位日期+2位小时+2位分钟+2位秒数 */
        SimpleDateFormat formatter = new SimpleDateFormat(DATETIME_PARTTEN_02);

        /* 4位流水号或4位随机数 */
        sb.append(formatter.format(new Date())).append(getRandomStr(4));

        /* GC回收 */
        formatter = null;

        return Long.parseLong(sb.toString());
    }


    /**
     * 返回16位的数字串 （线程安全）
     * 
     * @return 返回16位随机UID<br>
     *         getTimeInMillis = 13位时间<br>
     */
    public static synchronized String getUID() {
        if (n == 999)
            n = 100;

        StringBuffer sb = new StringBuffer();
        sb.append(Calendar.getInstance().getTimeInMillis()).append(n++);

        return sb.toString();
    }


    /**
     * 生成唯一序列号（UUID）
     * 
     * @return 唯一序列号（UUID）<br>
     *         例如：8d8e1d36-b4fd-493d-8a97-3ab32c13fd6d
     * 
     */
    public static String getUUID() {
        return UUID.randomUUID().toString();
    }


    /**
     * 取得N位（0-9）范围内的随机序列号。<br>
     * 此UID保证不唯一性
     * 
     * @param size
     *        范围文字长度
     * @return N位随机序列号
     */
    public static String getNumberUID(int size) {
        int count = 0;
        StringBuffer uid = new StringBuffer();
        Random r = new Random();
        while (count < size) {
            int i = Math.abs(r.nextInt(10));
            if (i >= 0 && i < INTPREFIX.length) {
                uid.append(INTPREFIX[i]);
                count++;
            }
        }
        /* GC回收 */
        r = null;
        /* 处理完了 */
        return uid.toString();
    }


    /**
     * 取得N位（0-9、a-z）范围内的随机序列号。<br>
     * 此UID保证不唯一性
     * 
     * @param size
     *        范围文字长度
     * @return N位随机序列号
     */
    public static String getStrUID(int size) {
        int count = 0;

        StringBuffer uid = new StringBuffer();
        Random r = new Random();

        while (count < size) {
            int i = Math.abs(r.nextInt(36));
            if (i >= 0 && i < STRPREFIX.length) {
                uid.append(STRPREFIX[i]);
                count++;
            }
        }
        /* GC回收 */
        r = null;
        /* 处理完了 */
        return uid.toString();
    }
}

/* Copyright (C) 2016, 上海金仕达卫宁软件科技有限公司 Project, All Rights Reserved. */