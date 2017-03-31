/*
 * ================================================================
 * 基干系统：上海金仕达卫宁软件有限公司 Web::Java DB基干系统 (Patrasche 3.0)
 * 
 * 业务系统：框架基盘
 * 
 * 
 * $Id: ReadFileUtils.java,v 1.6 2016/05/16 09:00:00 yuanfeng Exp $
 * ================================================================
 */
package com.winning.utils.io;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class ReadFileUtils {
private final static String encoding = System.getProperties().getProperty("file.encoding");
	
	public static byte[] readByteFile(String fileName) throws IOException {
		return readByteFile(new File(fileName));
	}

	public static byte[] readByteFile(File file) throws IOException {
		byte[] result = null;
		ByteArrayOutputStream out = new ByteArrayOutputStream();

		FileInputStream input = new FileInputStream(file);
		int count = 0;
		byte[] b = new byte[1024];
		
		try {
			while (input.available() > 0) {
				count = input.read(b);
				if (count != -1) {
					out.write(b, 0, count);
				}
			}
			
			result = out.toByteArray();
		} finally {
			input.close();
			out.close();
		}
		
		return result;
	}
	
	public static String readCharFile(String fileName) throws IOException {
		return readCharFile(new File(fileName));
	}
	
	public static String readCharFile(String fileName,String encoding) throws UnsupportedEncodingException, IOException{
		return readCharFile(new File(fileName),encoding);
	}
	
	public static String readCharFile(File file) throws UnsupportedEncodingException, IOException{
		return readCharFile(file,encoding);
	}
	
	public static String readCharFile(File file,String encoding) throws UnsupportedEncodingException, IOException{
		return new String(readByteFile(file),encoding);
	}
}

/* Copyright (C) 2016, 上海金仕达卫宁软件科技有限公司 Project, All Rights Reserved. */