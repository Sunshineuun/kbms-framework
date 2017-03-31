/*
 * ================================================================
 * 基干系统：上海金仕达卫宁软件有限公司 Web::Java DB基干系统 (Patrasche 3.0)
 * 
 * 业务系统：框架基盘
 * 
 * 
 * $Id: MathUtils.java,v 1.6 2016/05/16 09:00:00 yuanfeng Exp $
 * ================================================================
 */
package com.winning.utils;

import java.math.BigDecimal;


/**
 * 提供精确的浮点计算
 * 
 */
public class MathUtils {

    // 默认除法运算精度
    private static final int DEF_DIV_SCALE = 10;

    private static final int DEFAUTL_SCALE = 2;


    // 这个类不能实例化
    private MathUtils() {
    }


    /**
     * 提供精确的加法运算。
     * 
     * @param v1
     *        被加数
     * @param v2
     *        加数
     * @return 两个参数的和
     */
    public static double add(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.add(b2).doubleValue();
    }


    /**
     * 提供精确的减法运算。
     * 
     * @param v1
     *        被减数
     * @param v2
     *        减数
     * @return 两个参数的差
     */
    public static double sub(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.subtract(b2).doubleValue();
    }


    /**
     * 提供精确的乘法运算。
     * 
     * @param v1
     *        被乘数
     * @param v2
     *        乘数
     * @return 两个参数的积
     */
    public static double mul(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.multiply(b2).doubleValue();
    }


    /**
     * 提供（相对）精确的除法运算，当发生除不尽的情况时，精确到 小数点以后10位，以后的数字四舍五入。
     * 
     * @param v1
     *        被除数
     * @param v2
     *        除数
     * @return 两个参数的商
     */
    public static double div(double v1, double v2) {
        return div(v1, v2, DEF_DIV_SCALE);
    }


    /**
     * 提供（相对）精确的除法运算。当发生除不尽的情况时，由scale参数指 定精度，以后的数字四舍五入。
     * 
     * @param v1
     *        被除数
     * @param v2
     *        除数
     * @param scale
     *        表示表示需要精确到小数点以后几位。
     * @return 两个参数的商
     */
    public static double div(double v1, double v2, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException(
                    "The scale must be a positive integer or zero");
        }
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }


    /**
     * 提供精确的小数位四舍五入处理。
     * 
     * @param v
     *        需要四舍五入的数字
     * @param scale
     *        小数点后保留几位
     * @return 四舍五入后的结果
     */
    public static double round(double v, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException(
                    "The scale must be a positive integer or zero");
        }
        BigDecimal b = new BigDecimal(Double.toString(v));
        BigDecimal one = new BigDecimal("1");
        return b.divide(one, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }


    /**
     * 提供精确的小数位四舍五入处理。默认两位小数
     * 
     * @param v
     *        需要四舍五入的数字
     * @return 四舍五入后的结果
     */
    public static double roundDefault(double v) {
        return round(v, DEFAUTL_SCALE);
    }


    /**
     * 小数转化成百分比。默认两位小数
     * 
     * @param v
     *        需要转化成百分比的数字
     * @return 转化成百分比的结果
     */
    public static String percent(double v) {
        return round(100 * v, DEFAUTL_SCALE) + "%";
    }


    /**
     * 取两个数中较小的数
     * 
     * @param v1
     *        参数1
     * @param v2
     *        参数2
     * @return 较小的数
     */
    public static double getSmaller(double v1, double v2) {
        return sub(v1, v2) > 0 ? v2 : v1;
    }


    /**
     * 取两个数中较大的数
     * 
     * @param v1
     *        参数1
     * @param v2
     *        参数2
     * @return 较大的数
     */
    public static double getBigger(double v1, double v2) {
        return sub(v1, v2) > 0 ? v1 : v2;
    }
}


/* Copyright (C) 2016, 上海金仕达卫宁软件科技有限公司 Project, All Rights Reserved. */