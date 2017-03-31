package com.winning.kbms.core.service.impl;

import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.winning.kbms.core.domain.ExportDefine;
import com.winning.kbms.core.service.DataToExeclService;
import com.winning.kbms.core.service.GenerateWorkbook;
import com.winning.kbms.core.service.PagingService;

@Component ("simpleGenerateWorkbook")
public class SimpleGenerateWorkbook implements GenerateWorkbook
{
    protected final int SEARCH_SIZE = 10000;

    @Autowired
    private DataToExeclService dataToExeclService;

    @Override
    public Workbook generateWorkbook (PagingService pagingService, List <ExportDefine> exportDefines,
                                      Map <String, Object> params) throws Exception
    {
        SXSSFWorkbook wb = dataToExeclService.createSXSSFWorkbook ();
        SXSSFSheet s = dataToExeclService.writeTitles (exportDefines, wb);
        int startRow = 1;
        List <Map <String, Object>> result = null;
        int i = 1;
        do
        {
            if (startRow + SEARCH_SIZE >= 60000)
            {
                s = dataToExeclService.writeTitles (exportDefines, wb);
                startRow = 1;
            }
            result = this.getExportData (pagingService, exportDefines, i, SEARCH_SIZE, params);
            dataToExeclService.writeData (exportDefines, result, s, startRow, wb);

            startRow += SEARCH_SIZE;

            i++;
        }
        while (result.size () == SEARCH_SIZE);
        return wb;
    }

    /**
     * 获取导出数据
     * 
     * @return
     * @throws Exception
     */
    protected List <Map <String, Object>> getExportData (PagingService pagingService,
                                                         List <ExportDefine> exportDefines, int start, int limit,
                                                         Map <String, Object> params) throws Exception
    {
        return pagingService.findExportData (params, start, limit, exportDefines);
    }

}
