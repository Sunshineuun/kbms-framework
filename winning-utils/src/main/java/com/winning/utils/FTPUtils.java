/*
 * ================================================================
 * 基干系统：上海金仕达卫宁软件有限公司 Web::Java DB基干系统 (Patrasche 3.0)
 * 
 * 业务系统：框架基盘
 * 
 * 
 * $Id: FTPUtils.java,v 1.6 2016/05/16 09:00:00 yuanfeng Exp $
 * ================================================================
 */
package com.winning.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.apache.commons.net.ftp.FTPClient;

public class FTPUtils {
	/**
	 * 
	 * @param host 主机
	 * @param userName 用户名
	 * @param pwd  密码
	 * @param srcFile  上传文件
	 * @param folder   上传文件夹
	 * @param encoding  上传的编码格式
	 * @param destFileName 上传存放的文件名
	 */
	public static void upload(String host,String userName,String pwd,File srcFile,String folder,String encoding,String destFileName){
		FTPClient ftpClient = new FTPClient(); 
        FileInputStream fis = null; 

        try { 
            ftpClient.connect(host); 
            ftpClient.login(userName, pwd); 

            fis = new FileInputStream(srcFile); 
            //设置上传目录 
            ftpClient.changeWorkingDirectory(folder); 
            ftpClient.setBufferSize(1024); 
            ftpClient.setControlEncoding(encoding); 
            
            
            //设置文件类型（二进制） 
            ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE); 
            //使用源文件名称进行保存
            ftpClient.storeFile(destFileName, fis); 
        } catch (IOException e) { 
            e.printStackTrace(); 
            throw new RuntimeException("FTP客户端出错！", e); 
        } finally { 
            IOUtils.closeQuietly(fis); 
            try { 
                ftpClient.disconnect(); 
            } catch (IOException e) { 
                e.printStackTrace(); 
                throw new RuntimeException("关闭FTP连接发生异常！", e); 
            } 
        } 
	}

	/**
	 * 
	 * @param host 主机
	 * @param userName 用户名
	 * @param pwd  密码
	 * @param srcFile  上传文件
	 */
	public static void upload(String host,String userName,String pwd,File srcFile){
		if(srcFile == null || !srcFile.isFile()){
			throw new RuntimeException("上传文件为空或错误！");
		}
		upload(host,userName,pwd,srcFile,"/","UTF-8",srcFile.getName());
	}
	

	/**
	 * 
	 * @param host 主机
	 * @param userName 用户名
	 * @param pwd  密码
	 * @param remoteFileName 需要下载的文件名（含地址）
	 * @param destFileName 下载后存放名称
	 */
	 public static void download(String host,String userName,String pwd,String remoteFileName,String destFileName) { 
	        FTPClient ftpClient = new FTPClient(); 
	        FileOutputStream fos = null; 

	        try { 
	            ftpClient.connect(host); 
	            ftpClient.login(userName, pwd); 

	            fos = new FileOutputStream(destFileName); 

	            ftpClient.setBufferSize(1024); 
	            //设置文件类型（二进制） 
	            ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE); 
	            ftpClient.retrieveFile(remoteFileName, fos); 
	        } catch (IOException e) { 
	            e.printStackTrace(); 
	            throw new RuntimeException("FTP客户端出错！", e); 
	        } finally { 
	            IOUtils.closeQuietly(fos); 
	            try { 
	                ftpClient.disconnect(); 
	            } catch (IOException e) { 
	                e.printStackTrace(); 
	                throw new RuntimeException("关闭FTP连接发生异常！", e); 
	            } 
	        } 
	    } 
}

/* Copyright (C) 2016, 上海金仕达卫宁软件科技有限公司 Project, All Rights Reserved. */