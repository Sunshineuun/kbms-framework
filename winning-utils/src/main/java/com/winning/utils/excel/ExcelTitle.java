/*
 * ================================================================
 * 基干系统：上海金仕达卫宁软件有限公司 Web::Java DB基干系统 (Patrasche 3.0)
 * 
 * 业务系统：框架基盘
 * 
 * 
 * $Id: ExcelTitle.java,v 1.6 2016/05/16 09:00:00 yuanfeng Exp $
 * ================================================================
 */
package com.winning.utils.excel;

import org.apache.poi.hssf.usermodel.HSSFCell;

public class ExcelTitle {
	private String columnName;
	private String columnKeyName ;
	private int columnType = HSSFCell.CELL_TYPE_STRING;
	
	/**
	 * @return the columnName
	 */
	public String getColumnName() {
		return columnName;
	}
	/**
	 * @param columnName the columnName to set
	 */
	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}
	/**
	 * @return the columnKeyName
	 */
	public String getColumnKeyName() {
		return columnKeyName;
	}
	/**
	 * @param columnKeyName the columnKeyName to set
	 */
	public void setColumnKeyName(String columnKeyName) {
		this.columnKeyName = columnKeyName;
	}
	/**
	 * @return the columnType
	 */
	public int getColumnType() {
		return columnType;
	}
	/**
	 * @param columnType the columnType to set
	 */
	public void setColumnType(int columnType) {
		this.columnType = columnType;
	}
	
	
}

/* Copyright (C) 2016, 上海金仕达卫宁软件科技有限公司 Project, All Rights Reserved. */