package com.winning.kbms.core.service;

import java.util.List;
import java.util.Map;

import com.winning.kbms.core.domain.ExportDefine;
import com.winning.kbms.core.domain.Page;
import com.winning.kbms.core.exceptions.ServiceException;

public interface PagingService
{

    @SuppressWarnings ("rawtypes")
    public Page findOnPage (Map <String, Object> params, final Page page) throws Exception;

    /**
     * 获取导出数据
     * 
     * @param map
     * @return
     * @throws ServiceException
     */
    public List <Map <String, Object>> findExportData (Map <String, Object> params, int start, int limit,
                                                       List <ExportDefine> exportDefines) throws Exception;

    public List <ExportDefine> getExportDefines (Map<String, Object> params);

}
