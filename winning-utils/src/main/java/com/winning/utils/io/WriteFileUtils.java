/*
 * ================================================================
 * 基干系统：上海金仕达卫宁软件有限公司 Web::Java DB基干系统 (Patrasche 3.0)
 * 
 * 业务系统：框架基盘
 * 
 * 
 * $Id: WriteFileUtils.java,v 1.6 2016/05/16 09:00:00 yuanfeng Exp $
 * ================================================================
 */
package com.winning.utils.io;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.io.Writer;


public class WriteFileUtils {
    private static String encoding = System.getProperties().getProperty(
            "file.encoding");


    public WriteFileUtils() {
        super();
    }


    public static void writeFile(String fileName, String content)
        throws UnsupportedEncodingException, IOException {
        writeFile(fileName, content, encoding);
    }


    public static void writeFile(String fileName, String content,
        String encoding) throws UnsupportedEncodingException, IOException {
        writeFile(fileName, content.getBytes(encoding));
    }


    public static void writeFile(String fileName, boolean append, String content)
        throws UnsupportedEncodingException, IOException {
        writeFile(fileName, append, content, encoding);
    }


    public static void writeFile(String fileName, boolean append,
        String content, String encoding)
        throws UnsupportedEncodingException, IOException {
        writeFile(fileName, append, content.getBytes(encoding));
    }


    public static void writeFile(String fileName, byte[] bs) throws IOException {
        writeFile(fileName, false, bs);
    }


    public static void writeFile(String fileName, boolean append, byte[] bs)
        throws IOException {
        writeFile(new FileOutputStream(fileName, append), bs);
    }


    public static void writeFile(OutputStream out, String content)
        throws UnsupportedEncodingException, IOException {
        writeFile(out, content, encoding);
    }


    public static void writeFile(OutputStream out, String content,
        String encoding) throws UnsupportedEncodingException, IOException {
        writeFile(out, content.getBytes(encoding));
    }


    public static void writeFile(OutputStream out, byte[] bs)
        throws IOException {
        try {
            out.write(bs);
            out.flush();
        }
        finally {
            out.close();
        }
    }


    public static void writeFile(Writer out, String content)
        throws UnsupportedEncodingException, IOException {
        writeFile(out, content, encoding);
    }


    public static void writeFile(Writer out, String content, String encoding)
        throws UnsupportedEncodingException, IOException {
        writeFile(out,
                new String(content.getBytes(encoding), encoding).toCharArray());
    }


    public static void writeFile(Writer out, char[] cs) throws IOException {
        try {
            out.write(cs);
            out.flush();
        }
        finally {
            out.close();
        }
    }


    public static void main(String args[]) {
        String temp = "中文测试";
        try {
            WriteFileUtils.writeFile("d:\\a.txt", temp);
            WriteFileUtils.writeFile("d:\\b.txt", temp, "UTF-8");
        }
        catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }

    }
}

/* Copyright (C) 2016, 上海金仕达卫宁软件科技有限公司 Project, All Rights Reserved. */