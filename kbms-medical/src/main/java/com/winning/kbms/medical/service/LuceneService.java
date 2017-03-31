package com.winning.kbms.medical.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.lucene.queryparser.classic.ParseException;

import com.winning.kbms.medical.entity.LinstrFileInfo;
import com.winning.kbms.medical.vo.Pager;

public interface LuceneService
{
    public List <LinstrFileInfo> findLinstrFileWithLucene (String arg) throws IOException, ParseException;// 药品提示

    public Pager findLinstrFileInfoList (String arg, String type, int startPage, int maxRows) throws IOException,
                                                                                             ParseException;

    public Pager findLinstrFileInfoListForMMAP (String mc, String tymc, String pzwh, String jx, int startPage,
                                                int maxRows) throws IOException, ParseException;

    public Map <String, Object> findLinstrFileInfoWithId (String id) throws IOException;

    public List <LinstrFileInfo> findLinstrFileInfoWithTerms (String field, String[] params);

    public Pager findLinstrFileInfosWithFileds (int startPage, int maxRows, String param, String... fields)
                                                                                                           throws IOException,
                                                                                                           ParseException;
    public  List <LinstrFileInfo> searchLinstrFileInfoByParams (Map <String, String>params, int num) throws IOException, ParseException;
}
