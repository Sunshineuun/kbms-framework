/*
 * ================================================================
 * 基干系统：上海金仕达卫宁软件有限公司 Web::Java DB基干系统 (Patrasche 3.0)
 * 
 * 业务系统：框架基盘
 * 
 * 
 * $Id: CookieUtils.java,v 1.6 2016/05/16 09:00:00 yuanfeng Exp $
 * ================================================================
 */
package com.winning.utils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CookieUtils {
    /**
     * 默认cookie保存时间一年
     */
    public static final int COOKIE_LIFE_YEAR = 60 * 60 * 24 * 365;

    /**
     * 保存cookie
     * 
     * @param key
     * @param value
     * @param time
     * @param response
     */
    public static void saveCookie(String key, String value, int life, HttpServletRequest request,
            HttpServletResponse response) {
        String domain = request.getContextPath() == null || request.getContextPath().length() == 0 ? "/" : request
                .getContextPath();
        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(life);
        cookie.setPath(domain);
        response.addHeader(
                "P3P",
                "CP=\"CAO DSP COR CUR ADM DEV TAI PSA PSD IVAi IVDi CONi TELo OTPi OUR DELi SAMi OTRi UNRi PUBi IND PHY ONL UNI PUR FIN COM NAV INT DEM CNT STA POL HEA PRE GOV\""); // 跨iframe
        response.addCookie(cookie);
    }

    /**
     * 查询指定key的cookie数据
     * 
     * @param key
     * @param request
     * @return,
     */
    public static String getCookieByKey(String key, HttpServletRequest request) {
        Cookie cookies[] = request.getCookies();
        String info = "";
        if (cookies != null && cookies.length > 0) {
            for (int i = 0; i < cookies.length; i++) {
                Cookie c = cookies[i];
                if (c.getName().equalsIgnoreCase(key)) {
                    info = c.getValue();
                    break;
                }
            }
        }
        return info;
    }

    /**
     * 指定key清除cookie
     * 
     * @param key
     * @param request
     */
    public static void removeCookie(String key, HttpServletRequest request, HttpServletResponse response) {
        String domain = request.getContextPath() == null || request.getContextPath().length() == 0 ? "/" : request
                .getContextPath();
        Cookie cookie = new Cookie(key, null);
        cookie.setMaxAge(0);
        cookie.setPath(domain);
        response.addHeader(
                "P3P",
                "CP=\"CAO DSP COR CUR ADM DEV TAI PSA PSD IVAi IVDi CONi TELo OTPi OUR DELi SAMi OTRi UNRi PUBi IND PHY ONL UNI PUR FIN COM NAV INT DEM CNT STA POL HEA PRE GOV\""); // 跨iframe
        response.addCookie(cookie);
    }
}

/* Copyright (C) 2016, 上海金仕达卫宁软件科技有限公司 Project, All Rights Reserved. */