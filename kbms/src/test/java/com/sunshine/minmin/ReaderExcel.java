/*
 * ==============================================
 * (C)2016 Shanghai KingstarWinning Corporation. All rights reserved.
 * 项目名称：kbms 
 * 系统名称： @TODO
 * 文件名称： ReaderExcel.java
 * 注意事项：
 *
 *
 * $Id: ReaderExcel.java,v 1.6 2017年3月8日下午5:37:58 yuan Exp $
 * ==============================================
 */
package com.sunshine.minmin;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.winning.utils.excel.ExcelUtils;

/** 
 * <p>Class类功能定义的说明性内容。（请以句号“。”结尾、段中换行请使用“<br/>”符号）</p> 
 * <p>说明: </p> 
 * <p>备注: </p> 
 * 
 * @version 1.0
 * @author 公司名 : 上海金仕达卫宁软件科技有限公司（Shanghai KingStar WinningSoft LTD.） <br />
 * 变更履历 <br />
 *
 */
public class ReaderExcel {

	private static final Logger logger = LoggerFactory.getLogger(ReaderExcel.class);
	
	private List<String> titleRecord = new ArrayList<String>();
	
	public List<Map<String, String>> readImportRecordsFromExcel(InputStream is) {
		List<Map<String, String>> result = new ArrayList<Map<String, String>>();
		Workbook workbook = null;
		try {
			workbook = WorkbookFactory.create(is);
			Sheet sheet = workbook.getSheetAt(0);
			int rowNum = sheet.getLastRowNum();
			for(int i=0; i < sheet.getRow(0).getLastCellNum(); i++){
				titleRecord.add((String) ExcelUtils.getContent(sheet, 0, i, ExcelUtils.TYPE_STRING));
			}
			for (int i = 1; i <= rowNum; i++) { // 第一行是表头
				Map<String, String> record = this.readRecord(sheet, i, sheet.getRow(i));
				if (!record.isEmpty()) {
					result.add(record);
				} else {
					continue;
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return result;
	}
	
	private Map<String, String> readRecord(Sheet sheet, int rowNum, Row row) {
		Map<String, String> record = new HashMap<String, String>();
		for (int j = 0; j < row.getLastCellNum(); j++) {
			String colData = null;
			colData = (String) ExcelUtils.getContent(sheet, rowNum, j, ExcelUtils.TYPE_STRING);
			record.put(titleRecord.get(j), colData);
		}
		return record;
	}
}

/* Copyright (C) 2016, 上海金仕达卫宁软件科技有限公司 Project, All Rights Reserved. */