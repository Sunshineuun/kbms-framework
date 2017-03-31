/*
 * ================================================================
 * 基干系统：上海金仕达卫宁软件有限公司 Web::Java DB基干系统 (Patrasche 3.0)
 * 
 * 业务系统：框架基盘
 * 
 * 
 * $Id: ReadExcelException.java,v 1.6 2016/05/16 09:00:00 yuanfeng Exp $
 * ================================================================
 */
package com.winning.execptions;

public class ReadExcelException extends RuntimeException{
    private static final long serialVersionUID = -3909738823399671238L;

    public ReadExcelException(){
		
	}
	
	public ReadExcelException(Exception e){
		super(e);
	}
	
	public ReadExcelException(String msg){
		super(msg);
	}
}

/* Copyright (C) 2016, 上海金仕达卫宁软件科技有限公司 Project, All Rights Reserved. */