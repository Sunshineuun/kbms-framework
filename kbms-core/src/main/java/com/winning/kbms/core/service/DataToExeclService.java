package com.winning.kbms.core.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import com.winning.kbms.core.domain.ExportDefine;
import com.winning.kbms.core.domain.ExportTitle;

/**
 *
 * @author gang.liu
 * @date   2013-1-17
 */
public interface DataToExeclService {

    public SXSSFWorkbook createSXSSFWorkbook(int rowaccess);

    public SXSSFWorkbook createSXSSFWorkbook();

    public HSSFWorkbook createHSSFWorkbook();

    public Sheet createSheet(Workbook wb);

    public HSSFSheet writeData(ExportTitle exportTitle , List<Map<String, Object>> records, HSSFSheet s);

    public SXSSFSheet writeData(List<ExportDefine> exportDefines, List<Map<String, Object>> records, SXSSFSheet s,
            int startRowNum, SXSSFWorkbook wb) throws IOException;
    
    public SXSSFSheet writeTitles(List<ExportDefine> exportDefines, SXSSFWorkbook wb);

}