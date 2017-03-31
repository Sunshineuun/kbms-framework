package com.winning.kbms.core.service;

import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Workbook;

import com.winning.kbms.core.domain.ExportDefine;

public interface GenerateWorkbook
{
    public Workbook generateWorkbook (PagingService pagingService, List <ExportDefine> exportDefines,
                                      Map <String, Object> params) throws Exception;
}
