/*
 * ================================================================
 * 基干系统：上海金仕达卫宁软件有限公司 Web::Java DB基干系统 (Patrasche 3.0)
 * 
 * 业务系统：框架基盘
 * 
 * 
 * $Id: ResourcesHelper.java,v 1.6 2016/05/16 09:00:00 yuanfeng Exp $
 * ================================================================
 */
package com.winning.utils;

import java.io.IOException;
import java.io.InputStream;


/**
 * ResourcesHelper is Resources Helper.
 * 
 */
public class ResourcesHelper extends Object {

    /**
     * private constructor
     * 
     */
    private ResourcesHelper() {
    }


    /**
     * getClassLoader 获取加载类
     * 
     * @return a ClassLoader 加载类
     */
    private static ClassLoader getClassLoader() {
        return Thread.currentThread().getContextClassLoader();
    }


    /**
     * Returns a resource on the classpath as a Stream object 获取资源
     * 
     * @param resource
     *        资源 The resource to find
     * @return The resource 返回获取的资源
     * @throws IOException
     *         If the resource cannot be found or read
     */
    public static InputStream getResourceAsStream(String resource)
        throws IOException {
        return getResourceAsStream(getClassLoader(), resource);
    }


    /**
     * Returns a resource on the classpath as a Stream object 获取资源
     * 
     * @param loader
     *        The classloader used to load the resource
     * @param resource
     *        资源 The resource to find
     * @return The resource 返回获取的资源
     * @throws IOException
     *         If the resource cannot be found or read
     */
    public static InputStream getResourceAsStream(ClassLoader loader,
        String resource) throws IOException {
        InputStream in = null;
        if (loader != null)
            in = loader.getResourceAsStream(resource);
        if (in == null)
            in = ClassLoader.getSystemResourceAsStream(resource);
        if (in == null)
            throw new IOException("Could not find resource " + resource);
        return in;
    }

}


/* Copyright (C) 2016, 上海金仕达卫宁软件科技有限公司 Project, All Rights Reserved. */