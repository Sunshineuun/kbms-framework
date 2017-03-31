package com.winning.kbms.core.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.winning.domain.kbms.core.Dictionary;
import com.winning.kbms.core.domain.JResult;
import com.winning.kbms.core.service.DictionaryService;
import com.winning.kbms.core.service.ManagementService;

/**
 * 
 * @author gang.liu
 * @date 2013-1-12
 */
@Controller
@RequestMapping("/dictionary")
public class DictionaryController extends AbstractManagementController<Dictionary> {
	@Autowired
	private DictionaryService dictionaryService;

	@Override
	protected ManagementService<Dictionary> getManagementService() {
		return dictionaryService;
	}

	@ResponseBody
	@RequestMapping("/getDictionaryByTypeCode")
	public String getDictionaryByTypeCode(@RequestParam("typeCode") String typeCode,
			@RequestParam(value = "hasEmpty", required = false) boolean hasEmpty) {
		if (hasEmpty) {
			return wrapWithEmpty(dictionaryService.getDictionaryByTypeCode(typeCode));
		}
		return wrap(dictionaryService.getDictionaryByTypeCode(typeCode));
	}

	@ResponseBody
	@RequestMapping("/searchDictionaryOnQueryId")
	public JResult searchDictionaryOnQueryId(@RequestParam("queryId") String queryId, @RequestParam("limit") int limit,
			@RequestParam(value = "hasEmpty", required = false) boolean hasEmpty) {
		Map<String, String> map = dictionaryService.searchDictionaryOnQueryId(queryId, limit,
				this.getRequestParameters());
		List<String[]> list = new ArrayList<String[]>(map.size());
		for (Map.Entry<String, String> entry : map.entrySet()) {
			list.add(new String[] { entry.getKey(), entry.getValue() });
		}

		if (hasEmpty)
			list.add(new String[] { "", "" });
		
		return new JResult(true, list);
	}

	@ResponseBody
	@RequestMapping("/getDictionaryOnQueryId")
	public String getDictionaryOnQueryId(@RequestParam("queryId") String queryId,
			@RequestParam(value = "hasEmpty", required = false) boolean hasEmpty) {
		if (hasEmpty) {
			return wrapWithEmpty(dictionaryService.getDictionaryOnQueryId(queryId));
		}
		return wrap(dictionaryService.getDictionaryOnQueryId(queryId));
	}

	// @ResponseBody
	// @RequestMapping ("/getDictionaryOnQueryIdByParams")
	// public Map <String, String> getDictionaryOnQueryIdByParams (@RequestParam
	// ("queryId") String queryId)
	// {
	// return dictionaryService.getDictionaryOnQueryIdByParams (queryId,
	// this.getRequestParameters ());
	// }

	@ResponseBody
	@RequestMapping("/loadDictionary")
	public Map<String, String> loadDictionary(
			@RequestParam(value = "typeCodeStr", required = false) String typeCodeStr,
			@RequestParam(value = "queryIdStr", required = false) String queryIdStr) {
		Map<String, String> result = new HashMap<String, String>();

		if (StringUtils.isNotEmpty(queryIdStr)) {
			String[] queryIds = queryIdStr.split(",");
			for (String queryId : queryIds) {
				result.put(queryId, this.getDictionaryOnQueryId(queryId, false));
			}
		}

		if (StringUtils.isNotEmpty(typeCodeStr)) {
			String[] typeCodes = typeCodeStr.split(",");
			for (String typeCode : typeCodes) {
				result.put(typeCode, this.getDictionaryByTypeCode(typeCode, false));
			}
		}

		return result;
	}

	private String wrapWithEmpty(Map<String, String> map) {
		if (map.isEmpty())
			return "[[\"\",\"\"]]";

		StringBuilder sb = new StringBuilder();
		String str = null;
		sb.append("[").append("[\"\",\"\"],");
		for (Map.Entry<String, String> entry : map.entrySet()) {
			sb.append("[\"").append(entry.getKey()).append("\",\"").append(entry.getValue()).append("\"],");
		}
		if (sb.length() > 1) {
			str = sb.substring(0, sb.length() - 1);
		}
		return str + "]";
	}

	private String wrap(Map<String, String> map) {
		if (map.isEmpty())
			return "[]";

		StringBuilder sb = new StringBuilder();
		String str = null;
		sb.append("[");
		for (Map.Entry<String, String> entry : map.entrySet()) {
			sb.append("[\"").append(entry.getKey()).append("\",\"").append(entry.getValue()).append("\"],");
		}
		if (sb.length() > 1) {
			str = sb.substring(0, sb.length() - 1);
		}
		return str + "]";
	}
}
