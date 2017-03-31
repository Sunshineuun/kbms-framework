package com.winning.kbms.core.service;

import java.util.Map;

import com.winning.domain.kbms.core.Dictionary;

/**
 * 数据字典查询服务
 * 
 * @author gang.liu
 * @date 2013-1-12
 */
public interface DictionaryService extends ManagementService <Dictionary>
{

    /**
     * 根据sql语句标识查询出label与value ，比如查询机构对应的值与名称
     * 
     * @param queryId
     * @return
     */
    public Map <String, String> getDictionaryOnQueryId (String queryId);

    /**
     * 根据sql语句标识查询出label与value ，比如查询机构对应的值与名称，但有用户限制
     * 
     * @param queryId
     * @return
     */
    public Map <String, String> getDictionaryOnQueryIdByParams (String queryId, Map <String, Object> params);

    public Map <String, String> getDictionaryByTypeCode (String typeCode);

    public Map <String, String> searchDictionaryOnQueryId (String queryId, int limit, Map <String, Object> params);

    public void deleteDictionaryByTypeCode (String typeCode);

}