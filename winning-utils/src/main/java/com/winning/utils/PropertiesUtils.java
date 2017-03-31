/*
 * ================================================================
 * 基干系统：上海金仕达卫宁软件有限公司 Web::Java DB基干系统 (Patrasche 3.0)
 * 
 * 业务系统：框架基盘
 * 
 * 
 * $Id: PropertiesUtils.java,v 1.6 2016/05/16 09:00:00 yuanfeng Exp $
 * ================================================================
 */
package com.winning.utils;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import org.apache.commons.io.IOUtils;


/**
 * <h1>框架基盘</h1><br>
 * 
 * <b>NOTE : 功能概述。</b>
 * <p>
 * 资源文件Properties信息获取方法工具
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
public class PropertiesUtils {

    public static Map<String, Map<String, String>> propertiesMap =
            new HashMap<String, Map<String, String>>();

    /** Properties属性文件固定文字 */
    private static String OWN_PROPERTIES = "winning_config.properties";

    /** 固定文字 {／} */
    final private static String SLASH = "/";

    /** 固定文字 {空} */
    final private static String BLANK = "";

    /**
     * 静态构造方法
     */
    static {
        Properties props = new Properties();
        InputStream in = null;

        try {
            in =
                    PropertiesUtils.class.getResourceAsStream(SLASH
                            + OWN_PROPERTIES);
            props.load(in);
            Map<String, String> propertyMap = new HashMap<String, String>();

            for (Object key : props.keySet()) {
                String keyStr = key.toString();
                String valueStr = props.getProperty(keyStr);
                propertyMap.put(keyStr, valueStr);
            }

            propertiesMap.put("winning_config", propertyMap);

            String propertiesPath =
                    propertiesMap.get("winning_config").get("PROPERTIES_PATH");

            if (propertiesPath != null && !BLANK.equals(propertiesPath)) {

                String[] propertiesArray = propertiesPath.split(",");
                if (propertiesArray.length > 0) {
                    for (int i = 0; i < propertiesArray.length; i++) {
                        String path = propertiesArray[i];
                        parseProperties(path);
                    }
                }
            }
        }
        catch (Exception e) {
            IOUtils.closeQuietly(in);
        }
        finally {
            IOUtils.closeQuietly(in);
        }

    }


    /**
     * 
     * @param propertiesPath
     */
    public static void parseProperties(String propertiesPath) {
        String fileName = propertiesPath;
        
        if (propertiesPath.indexOf(SLASH) != -1) {
            fileName =
                    propertiesPath
                            .substring(propertiesPath.lastIndexOf(SLASH) + 1);
            fileName = fileName.replace(".properties", BLANK);
        }

        Properties props = new Properties();
        InputStream in = null;

        try {
            in =
                    PropertiesUtils.class.getResourceAsStream(SLASH
                            + propertiesPath);

            props.load(in);
            Map<String, String> propertyMap = new HashMap<String, String>();
            for (Object key : props.keySet()) {
                String keyStr = key.toString();
                String valueStr = props.getProperty(keyStr);
                propertyMap.put(keyStr, valueStr);
            }
            propertiesMap.put(fileName, propertyMap);
        }
        catch (Exception e) {
            IOUtils.closeQuietly(in);
        }
        finally {
            IOUtils.closeQuietly(in);
        }
    }

}

/* Copyright (C) 2016, 上海金仕达卫宁软件科技有限公司 Project, All Rights Reserved. */