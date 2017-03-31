/*
 * ================================================================
 * 基干系统：上海金仕达卫宁软件有限公司 Web::Java DB基干系统 (Patrasche 3.0)
 * 
 * 业务系统：框架基盘
 * 
 * 
 * $Id: PathUtils.java,v 1.6 2016/05/16 09:00:00 yuanfeng Exp $
 * ================================================================
 */
package com.winning.utils.project;

import java.io.File;
import java.io.IOException;
import com.winning.utils.PropertiesUtils;


/**
 * <h1>框架基盘</h1><br>
 * 
 * <b>NOTE : 功能概述。</b>
 * <p>
 * 文件路径值获取工具
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
public class PathUtils {

    /**
     * 当前系统所在web服务中的绝对路径的根目录
     */
    static public String EXTERN_REAL_PATH = "";

    public static String WEBAPP_REAL_PATH="";
    
    public static String ftpFile="1";
    
    /**
     * 获取系统外部文件的根目录（所有系统使用外部的文件在这个目录中）
     * 
     * @author xch
     */
    {
        String basePath = null;
        String extern_path = null;
        String backDir = null;
        File file = null;

        if (EXTERN_REAL_PATH == null || EXTERN_REAL_PATH.trim().length() == 0) {
            basePath =
                    new PathUtils().getClass().getResource(File.separator)
                            .getPath();
            extern_path =
                    PropertiesUtils.propertiesMap.get("config").get(
                            "EXTERN_REAL_PATH");
            backDir = "../../../../../";

            try {
                file = new File(basePath, backDir + extern_path);

                if (!file.exists()) {
                    file.mkdir();
                }
                EXTERN_REAL_PATH = file.getCanonicalPath();
            }
            catch (IOException ioException) {
                ioException.printStackTrace();
            }

        }
        /* GC回收 */
        file = null;
        basePath = null;
        extern_path = null;
        backDir = null;

    }

}

/* Copyright (C) 2016, 上海金仕达卫宁软件科技有限公司 Project, All Rights Reserved. */