/*
 * ================================================================
 * 基干系统：上海金仕达卫宁软件有限公司 Web::Java DB基干系统 (Patrasche 3.0)
 * 
 * 业务系统：框架基盘
 * 
 * 
 * $Id: FileUtils.java,v 1.6 2016/05/16 09:00:00 yuanfeng Exp $
 * ================================================================
 */
package com.winning.utils;

import java.io.File;

/**
 * @标题: FileUtils.java
 * @包名: com.winning.utils
 * @描述: TODO
 * @作者: gang.liu
 * @时间: Mar 24, 2014 9:11:14 AM
 * @版权: (c) 2014, 卫宁软件科技有限公司
 */
public class FileUtils
{
    /**
     * @作者: gang.liu
     * @时间: Mar 24, 2014 9:18:55 AM
     * @描述: 删除文件夹
     * @param folder
     * @备注:
     */
    public static void deleteFolder (File folder)
    {
        if (folder == null || !folder.exists ())
            return;

        File[] files = folder.listFiles ();
        for (File file : files)
        {
            if (file.isFile ())
                file.delete ();
            else
                deleteFolder (file);
        }
        folder.delete ();
    }

    /**
     * @作者: gang.liu
     * @时间: Mar 24, 2014 9:18:55 AM
     * @描述: 删除文件夹
     * @param folder
     * @备注:
     */
    public static void deleteFolder (String folderPath)
    {
        File folder = new File (folderPath);
        deleteFolder (folder);
    }

    /**
     * @作者: gang.liu
     * @时间: Mar 24, 2014 9:18:55 AM
     * @描述: 删除文件夹下的所有子文件
     * @param folder
     * @备注:
     */
    public static void deleteSubFiles (String folderPath)
    {
        File folder = new File (folderPath);
        File[] files = folder.listFiles ();
        for (File file : files)
        {
            if (file.isFile ())
                file.delete ();
            else
                deleteFolder (file);
        }
    }
}


/* Copyright (C) 2016, 上海金仕达卫宁软件科技有限公司 Project, All Rights Reserved. */