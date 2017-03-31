/*
 * ================================================================
 * 基干系统：上海金仕达卫宁软件有限公司 Web::Java DB基干系统 (Patrasche 3.0)
 * 
 * 业务系统：框架基盘
 * 
 * 
 * $Id: DESPlusUtils.java,v 1.6 2016/05/16 09:00:00 yuanfeng Exp $
 * ================================================================
 */
package com.winning.utils;

import java.security.Key;
import java.security.Security;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.Cipher;

public class DESPlusUtils
{
    private static String strDefaultKey = "winning";
    private Cipher encryptCipher = null;
    private Cipher decryptCipher = null;
    public final static Map <String, DESPlusUtils> cacheDESPlusUtilsMap = new HashMap <String, DESPlusUtils> ();

    public static DESPlusUtils getInstance (String strKey) throws Exception
    {
        if (!cacheDESPlusUtilsMap.containsKey (strKey))
        {
            cacheDESPlusUtilsMap.put (strKey, new DESPlusUtils (strKey));
        }
        return cacheDESPlusUtilsMap.get (strKey);
    }

    public static DESPlusUtils getDefaultInstance () throws Exception
    {
        return getInstance (strDefaultKey);
    }

    /**
     * 将byte数组转换为表示16进制值的字符串， 如：byte[]{8,18}转换为：0813， 和public static byte[]
     * hexStr2ByteArr(String strIn) 互为可逆的转换过程
     * 
     * @param arrB 需要转换的byte数组
     * @return 转换后的字符串
     * @throws Exception 本方法不处理任何异常，所有异常全部抛出
     */
    public static String byteArr2HexStr (byte[] arrB) throws Exception
    {
        int iLen = arrB.length;
        // 每个byte用两个字符才能表示，所以字符串的长度是数组长度的两倍
        StringBuffer sb = new StringBuffer (iLen * 2);
        for (int i = 0; i < iLen; i++)
        {
            int intTmp = arrB[i];
            // 把负数转换为正数
            while (intTmp < 0)
            {
                intTmp = intTmp + 256;
            }
            // 小于0F的数需要在前面补0
            if (intTmp < 16)
            {
                sb.append ("0");
            }
            sb.append (Integer.toString (intTmp, 16));
        }
        return sb.toString ();
    }

    /**
     * 将表示16进制值的字符串转换为byte数组， 和public static String byteArr2HexStr(byte[] arrB)
     * 互为可逆的转换过程
     * 
     * @param strIn 需要转换的字符串
     * @return 转换后的byte数组
     * @throws Exception 本方法不处理任何异常，所有异常全部抛出
     */
    public static byte[] hexStr2ByteArr (String strIn) throws Exception
    {
        byte[] arrB = strIn.getBytes ();
        int iLen = arrB.length;

        // 两个字符表示一个字节，所以字节数组长度是字符串长度除以2
        byte[] arrOut = new byte[iLen / 2];
        for (int i = 0; i < iLen; i = i + 2)
        {
            String strTmp = new String (arrB, i, 2);
            arrOut[i / 2] = (byte) Integer.parseInt (strTmp, 16);
        }
        return arrOut;
    }

    /**
     * 默认构造方法，使用默认密钥
     * 
     * @throws Exception
     */
    public DESPlusUtils () throws Exception
    {
        this (strDefaultKey);
    }

    /**
     * 指定密钥构造方法
     * 
     * @param strKey 指定的密钥
     * @throws Exception
     */
    public DESPlusUtils (String strKey) throws Exception
    {
        Security.addProvider (new com.sun.crypto.provider.SunJCE ());
        Key key = getKey (strKey.getBytes ());

        encryptCipher = Cipher.getInstance ("DES");
        encryptCipher.init (Cipher.ENCRYPT_MODE, key);

        decryptCipher = Cipher.getInstance ("DES");
        decryptCipher.init (Cipher.DECRYPT_MODE, key);
    }

    /**
     * 加密字节数组
     * 
     * @param arrB 需加密的字节数组
     * @return 加密后的字节数组
     * @throws Exception
     */
    public byte[] encrypt (byte[] arrB) throws Exception
    {
        return encryptCipher.doFinal (arrB);
    }

    /**
     * 加密字符串
     * 
     * @param strIn 需加密的字符串
     * @return 加密后的字符串
     * @throws Exception
     */
    public String encrypt (String strIn) throws Exception
    {
        return byteArr2HexStr (encrypt (strIn.getBytes ()));
    }

    /**
     * 解密字节数组
     * 
     * @param arrB 需解密的字节数组
     * @return 解密后的字节数组
     * @throws Exception
     */
    public byte[] decrypt (byte[] arrB) throws Exception
    {
        return decryptCipher.doFinal (arrB);
    }

    /**
     * 解密字符串
     * 
     * @param strIn 需解密的字符串
     * @return 解密后的字符串
     * @throws Exception
     */
    public String decrypt (String strIn) throws Exception
    {
        return new String (decrypt (hexStr2ByteArr (strIn)));
    }

    /**
     * 从指定字符串生成密钥，密钥所需的字节数组长度为8位 不足8位时后面补0，超出8位只取前8位
     * 
     * @param arrBTmp 构成该字符串的字节数组
     * @return 生成的密钥
     * @throws java.lang.Exception
     */
    private Key getKey (byte[] arrBTmp) throws Exception
    {
        // 创建一个空的8位字节数组（默认值为0）
        byte[] arrB = new byte[8];

        // 将原始字节数组转换为8位
        for (int i = 0; i < arrBTmp.length && i < arrB.length; i++)
        {
            arrB[i] = arrBTmp[i];
        }

        // 生成密钥
        Key key = new javax.crypto.spec.SecretKeySpec (arrB, "DES");

        return key;
    }

    /**
     * 测试用默认的密钥加密解密和指定密钥加密解密
     * 
     * @param args
     * @throws Exception
     */
    public static void main (String[] args) throws Exception
    {
        // 使用默认的密钥
        // DESPlusUtils desPlus = new DESPlusUtils();
        // String e = desPlus.encrypt("admin");
        // System.out.println(e);
        // String d = desPlus.decrypt(e);
        // System.out.println(d);

        // 使用新密钥
        DESPlusUtils desPlus2 = new DESPlusUtils ("winning");
         String e2 = desPlus2.encrypt("测试");
         System.out.println(e2);
        String s = null;
        long start = Calendar.getInstance ().getTimeInMillis ();
        for (int i = 10; i > 0; i--)
        {
            s = desPlus2.decrypt ("ea09ccd728a37f299554d922d91ca6fef3ce232d223732daeba6a3e00fc582bdd834cede5fd2319f0df8975714b090fc116638f8b82128fee02279798f0dee69");
        }
        System.out.println (Calendar.getInstance ().getTimeInMillis () - start);
        System.out.println (s);

    }
}

/* Copyright (C) 2016, 上海金仕达卫宁软件科技有限公司 Project, All Rights Reserved. */