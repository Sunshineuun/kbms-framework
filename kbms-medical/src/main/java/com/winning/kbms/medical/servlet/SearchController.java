package com.winning.kbms.medical.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.winning.kbms.core.utils.JackJson;
import com.winning.kbms.medical.entity.LinstrFileInfo;
import com.winning.kbms.medical.service.LuceneService;
import com.winning.kbms.medical.serviceImpl.DrugServiceImpl;
import com.winning.kbms.medical.vo.Pager;

@Controller
@RequestMapping("/ws/drugList")
public class SearchController {
    @Autowired
    private LuceneService luceneService;

    // @Resource(name = "dynamicSqlDao")
    // DynamicSqlDao dynamicSqlDao;

    @RequestMapping("findLinstrFileInfos.do")
    public String findLinstrFileInfosWithId(HttpServletRequest request,
            HttpServletResponse response) throws IOException, ParseException {
        String parName = null;
        if (!StringUtils.isEmpty(request.getParameter("q"))) {
            parName = URLDecoder.decode(request.getParameter("q"), "UTF-8");
        }
        List<LinstrFileInfo> lfis = luceneService
                .findLinstrFileWithLucene(parName);
        response.setContentType("text/html; charset=UTF-8");
        String jsonStr = JackJson.fromObjectToJson(lfis);
        PrintWriter print = null;
        print = response.getWriter();
        print.write(jsonStr);
        print.close();
        return null;
    }

    @RequestMapping("findLinstrFileInfoList.do")
    public String findLinstrFileInfoList(HttpServletRequest request)
            throws IOException, ParseException {

        String parName = null;
        if (!StringUtils.isEmpty(request.getParameter("q"))) {
            parName = URLDecoder.decode(request.getParameter("q"), "UTF-8");
        }
        if (parName == null)
            return "search";
        if ("".equals(parName))
            return "search";

        String type = request.getParameter("type");
        String starPageStr = "0";
        String maxRowsStr = "10";
        if (!StringUtils.isEmpty(request.getParameter("startPage"))) {
        	starPageStr = request.getParameter("startPage");
        }
        if (!StringUtils.isEmpty(request.getParameter("maxRows"))) {
        	maxRowsStr = request.getParameter("maxRows");
        }
        
        Integer startPage = Integer.parseInt(starPageStr);
        Integer maxRows = Integer.parseInt(maxRowsStr);
        
        Pager pager = luceneService.findLinstrFileInfoList(parName, type,
                startPage, maxRows);
        if (pager.getTotalRows() < 1) {
            return "error";
        }
        request.setAttribute("pager", pager);
        request.setAttribute("type", type);
        request.setAttribute("parName", parName);
        if ("02".equals(type)) {
            return "DrugSearchPages02";
        }
        return "DrugSearchPages";
    }

    @RequestMapping("findLinstrFileInfoListForMMAP.do")
    public String findLinstrFileInfoListForMMAP(HttpServletRequest request)
            throws IOException, ParseException {
        String mc = null;
        String tymc = null;
        String jx = null;
        String pzwh = null;
        if (!StringUtils.isEmpty(request.getParameter("mc"))) {
            mc = URLDecoder.decode(request.getParameter("mc"), "UTF-8");
        }
        if (!StringUtils.isEmpty(request.getParameter("tymc"))) {
            tymc = URLDecoder.decode(request.getParameter("tymc"), "UTF-8");
        }

        if (!StringUtils.isEmpty(request.getParameter("pzwh"))) {
            pzwh = URLDecoder.decode(request.getParameter("pzwh"), "UTF-8");
        }

        if (!StringUtils.isEmpty(request.getParameter("jx"))) {
            jx = URLDecoder.decode(request.getParameter("jx"), "UTF-8");
        }

        Integer startPage = null;
        Integer maxRows = null;

        if (request.getParameter("startPage") == null
                || request.getParameter("maxRows") == null) {
            startPage = 1;
            maxRows = 10;
        } else {
            startPage = Integer.parseInt(request.getParameter("startPage"));
            maxRows = Integer.parseInt(request.getParameter("maxRows"));
        }

        Pager pager = luceneService.findLinstrFileInfoListForMMAP(mc, tymc,
                pzwh, jx, startPage, maxRows);
        if (pager.getTotalRows() < 1) {
            return "error";
        }
        request.setAttribute("results", pager.getResults());
        request.setAttribute("type", "00");
        request.setAttribute("mc", mc);
        request.setAttribute("tymc", tymc);
        request.setAttribute("pzwh", pzwh);
        request.setAttribute("jx", jx);

        if (!StringUtils.isEmpty(pzwh)) {
            LinstrFileInfo info = (LinstrFileInfo) (pager.getResults().get(0));
            if (info != null && info.getApproveDocNo() != null) {
                if (info.getApproveDocNo().indexOf(pzwh) >= 0) {
                    Map<String, Object> drugMap = DrugServiceImpl
                            .generateMap(info);
                    request.setAttribute("drugMap", drugMap);
                    return "DrugSearch";
                }
            }
        }

        return "DrugSearchPagesMMAP";
    }

    @RequestMapping("testServlet.do")
    public String testServlet(HttpServletRequest request) throws IOException,
            ParseException {
        String mc = null;
        String id = request.getParameter("id");
        String ruleType = request.getParameter("ruleType");
        List<LinstrFileInfo> results = null;
        Map<String, String> params = new HashMap<String, String>();
        if (!StringUtils.isEmpty(id)) {
            Map<String, Object> drugMap = luceneService
                    .findLinstrFileInfoWithId(id);
            request.setAttribute("drugMap", drugMap);
            return "DrugSearchMMAP";
        }
        if (!StringUtils.isEmpty(request.getParameter("mc"))) {
            mc = URLDecoder.decode(request.getParameter("mc"), "UTF-8");
            params.put("productNameCn", mc);
        }
        if (!StringUtils.isEmpty(ruleType)) {
            params.put("ruleType", ruleType);
        }
        results = luceneService.searchLinstrFileInfoByParams(params, 10);
        request.setAttribute("results", results);
        return "DrugSearchPagesMMAP";
    }

    @RequestMapping("DrugSearchMMAP.do")
    public String findLinstrFileInfoMMAPWithId(HttpServletRequest request,
            HttpServletResponse response) throws IOException {
        String id = request.getParameter("id");
        Map<String, Object> drugMap = luceneService
                .findLinstrFileInfoWithId(id);
        request.setAttribute("drugMap", drugMap);
        return "medical/DrugPage";
    }
    
    @RequestMapping("DrugSearchJSON.do")
    public String findLinstrFileInfoJSONWithId(HttpServletRequest request,
            HttpServletResponse response) throws IOException {
        String id = request.getParameter("id");
        Map<String, Object> drugMap = luceneService
                .findLinstrFileInfoWithId(id);
        response.setContentType("text/html; charset=UTF-8");
        String jsonStr = JackJson.fromObjectToJson(drugMap);
        PrintWriter print = null;
        print = response.getWriter();
        print.write(jsonStr);
        print.close();
        return null;
    }

    @RequestMapping("DrugSearchTerms.do")
    @RequiresPermissions("medicalSearch:showMedical")
    public String findItemsWithTerms(HttpServletRequest request)
            throws IOException, ParseException {
        String mc = null;
        String ids = request.getParameter("ids");
        String ruleType = request.getParameter("ruleType");
        List<LinstrFileInfo> results = null;
        Map<String, String> params = new HashMap<String, String>();
        if (!StringUtils.isEmpty(ids)) {
            String[] idStrs = ids.split("@");
            results = luceneService.findLinstrFileInfoWithTerms("id", idStrs);
            if (results == null || results.size() < 1) {
                return "errorpage/NoResult";
            }
            if (results != null && results.size() > 1) {
                request.setAttribute("results", results);
                return "medical/DrugList";
            } else {
                Map<String, Object> drugMap = DrugServiceImpl
                        .generateMap(results.get(0));
                request.setAttribute("drugMap", drugMap);
                return "medical/DrugPage";
            }
        }
        if (!StringUtils.isEmpty(request.getParameter("mc"))) {
            mc = request.getParameter("mc");
            mc = formatStr(mc);
            mc = new String(mc.getBytes("ISO-8859-1"), "UTF-8");
            params.put("productNameCn", mc);
        }
        if (!StringUtils.isEmpty(ruleType)) {
            params.put("ruleType", ruleType);
        }
        results = luceneService.searchLinstrFileInfoByParams(params, 10);
        request.setAttribute("results", results);
        return "medical/DrugList";
    }

    @RequestMapping("SearchDrugByCode.do")
    // @RequiresPermissions("medicalSearch:showMedical")
    public String searchDrugByCodeController(HttpServletRequest request)
            throws IOException, ParseException {
        // String mc = null;
        // String ids = request.getParameter("ids");
        // String ruleType = request.getParameter("ruleType");
        // String xmdm = request.getParameter("xmdm");
        // String ake001 = request.getParameter("ake001");
        // List<LinstrFileInfo> results = null;
        // Map<String, String> params = new HashMap<String, String>();
        // if (!StringUtils.isBlank(xmdm))
        // {
        // List<Map<String, Object>> mappings = dynamicSqlDao
        // .executeSelectListDynamic(
        // " SELECT DRUG_ID FROM KBMS_DRUG_LIST_MAPPING WHERE DRUG_CODE =#{drugCode,jdbcType=VARCHAR} ",
        // MapUtils.newMap("drugCode", StringUtils.trim(xmdm)));
        // if (mappings != null && mappings.size() > 0)
        // {
        // ids = (String) mappings.get(0).get("DRUG_ID");
        // }
        // }
        // if (!StringUtils.isBlank(ake001))
        // {
        // List<Map<String, Object>> mappings = dynamicSqlDao
        // .executeSelectListDynamic(
        // " SELECT DRUG_ID FROM KBMS_DRUG_LIST_MAPPING WHERE DRUG_CODE =#{drugCode,jdbcType=VARCHAR} ",
        // MapUtils.newMap("drugCode", StringUtils.trim(ake001)));
        // if (mappings != null && mappings.size() > 0)
        // {
        // ids = (String) mappings.get(0).get("DRUG_ID");
        // }
        // }
        // if (!StringUtils.isEmpty(ids))
        // {
        // String[] idStrs = ids.split("@");
        // results = luceneService.findLinstrFileInfoWithTerms("id", idStrs);
        // if(results==null||results.size()<1){
        // return "errorpage/NoResult";
        // }
        // if (results != null && results.size() > 1)
        // {
        // request.setAttribute("results", results);
        // return "medical/DrugList";
        // }
        // else if (results != null && results.size() > 0)
        // {
        // Map<String, Object> drugMap = DrugServiceImpl
        // .generateMap(results.get(0));
        // request.setAttribute("drugMap", drugMap);
        // return "medical/DrugPage";
        // }
        // }
        //
        // if (!StringUtils.isEmpty(request.getParameter("mc")))
        // {
        // mc = request.getParameter("mc");
        // mc = new String(mc.getBytes("ISO-8859-1"), "UTF-8");
        // params.put("productNameCn", mc);
        // }
        // if (!StringUtils.isEmpty(ruleType))
        // {
        // params.put("ruleType", ruleType);
        // }
        // results = luceneService.searchLinstrFileInfoByParams(params, 10);
        // request.setAttribute("results", results);
        // if(results==null||results.size()<1){
        // return "errorpage/NoResult";
        // }
        return "medical/DrugList";
    }

    private String formatStr(String str) {
        if (StringUtils.isBlank(str))
            return "";
        boolean check = true;
        while (check) {
            if (str.indexOf("*") == 0 || str.indexOf("?") == 0) {
                str = str.substring(1);
                continue;
            }
            check = false;
        }
        return str;
    }
}
