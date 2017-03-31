/*
 * ================================================================
 * 基干系统：上海金仕达卫宁软件有限公司 Web::Java DB基干系统 (Patrasche 3.0)
 * 
 * 业务系统：框架基盘
 * 
 * 
 * $Id: CamelCaseUtils.java,v 1.6 2016/05/16 09:00:00 yuanfeng Exp $
 * ================================================================
 */
package com.winning.utils;

/**
 * <h1>框架基盘</h1><br>
 * 
 * <b>NOTE : 功能概述。</b>
 * <p>
 * CamelCaseUtils.toCamelCase("hello_world") == "helloWorld" CamelCaseUtils.
 * toCapitalizeCamelCase("hello_world") == "HelloWorld"
 * CamelCaseUtils.toUnderScoreCase("helloWorld") = "hello_world"
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
public class CamelCaseUtils {

    /** 固定文字｛＿｝ */
    private static final char SEPARATOR = '_';


    /**
     * 驼峰命名转、下划线命名样式（全部小写字符）
     * 
     * @param s
     *        需要变换字符（原始字符）
     * @return <br>
     *         正常：返回下划线命名样式字符<br>
     *         异常：返回null<br>
     *         用法：<br>
     *         CamelCaseUtils.toUnderScoreCase("helloWorld") = "hello_world"
     * @author
     */
    public static String toUnderScoreCase(String s) {
        boolean upperCase = false;
        boolean hasLowerCase = false;
        StringBuilder sb = new StringBuilder();

        /* 前期判定 */
        if (s == null) {
            return null;
        }

        /* 判断是否存在大写字符 */
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (Character.isLowerCase(c)) {
                hasLowerCase = true;
            }
        }
        if (!hasLowerCase) {
            return s;
        }

        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);

            boolean nextUpperCase = true;

            if (i < (s.length() - 1)) {
                nextUpperCase = Character.isUpperCase(s.charAt(i + 1));
            }

            if ((i > 0) && Character.isUpperCase(c)) {
                if (!upperCase || !nextUpperCase) {
                    sb.append(SEPARATOR);
                }
                upperCase = true;
            }
            else {
                upperCase = false;
            }

            sb.append(Character.toLowerCase(c));
        }

        return sb.toString();
    }


    /**
     * 去下划线、驼峰命名
     * 
     * @param s
     *        需要变换字符（原始字符）
     * @return <br>
     * @author
     */
    public static String toCamelCase(String s) {
        if (s == null) {
            return null;
        }

        s = s.toLowerCase();

        StringBuilder sb = new StringBuilder(s.length());
        boolean upperCase = false;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);

            if (c == SEPARATOR) {
                upperCase = true;
            }
            else if (upperCase) {
                sb.append(Character.toUpperCase(c));
                upperCase = false;
            }
            else {
                sb.append(c);
            }
        }

        return sb.toString();
    }


    /**
     * 首字符大写样式的驼峰命名
     * 
     * @param s
     *        需要变换字符（原始字符）
     * @return <br>
     * @author
     */
    public static String toCapitalizeCamelCase(String s) {
        if (s == null) {
            return null;
        }
        s = toCamelCase(s);
        return s.substring(0, 1).toUpperCase() + s.substring(1);
    }


    /**
     * 测试用代码 （建议使用 JUnit）
     * 
     * @param args
     * @author
     */
    public static void main(String[] args) {
        System.out.println(CamelCaseUtils.toCapitalizeCamelCase("HELLO_WORLD"));
    }
}

/* Copyright (C) 2016, 上海金仕达卫宁软件科技有限公司 Project, All Rights Reserved. */