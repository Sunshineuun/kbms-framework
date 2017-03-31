package com.winning.kbms.core.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.codehaus.jackson.type.TypeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.winning.kbms.core.domain.ExportDefine;
import com.winning.kbms.core.domain.JResult;
import com.winning.kbms.core.domain.Page;
import com.winning.kbms.core.exceptions.ServiceException;
import com.winning.kbms.core.service.GenerateWorkbook;
import com.winning.kbms.core.service.PagingService;
import com.winning.kbms.core.utils.ApplicationContextUtils;
import com.winning.kbms.core.utils.JackJson;

@Controller
@RequestMapping("/query")
public class QueryController extends BaseController {
	private final Logger logger = LoggerFactory.getLogger(getClass());

	protected static final String TOTAL_COUNT = "totalCount";
	protected static final String EXPORT_DEFINES = "exportDefines";
	protected static final String GROUP_HEADERS = "groupHeaders";
	protected static final String COMPLEX_GROUP_HEADERS = "complexGroupHeaders";
	protected static final String FOOTER_DATA = "footerData";

	@Autowired
	@Qualifier("simpleGenerateWorkbook")
	private GenerateWorkbook generateWorkbook;

	public Object getServiceBean(String beanName) {
		return ApplicationContextUtils.getBean(beanName);
	}

	protected PagingService getPagingService(String beanName) {
		return (PagingService) getServiceBean(beanName);
	}

	/**
	 * 获取列表
	 * 
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("/{beanName}/list.do")
	public JResult list(@PathVariable("beanName") String beanName, Page<?> page) throws Exception {
		Map<String, Object> param = super.getRequestParameters();
		PagingService queryService = this.getPagingService(beanName);
		page = queryService.findOnPage(param, page);
		return new JResult(true, page);
	}

	/**
	 * 导出Excel
	 * 
	 * @throws Exception
	 */
	@RequestMapping("/{beanName}/exportSXSSF")
	public void exportSXSSF(@PathVariable("beanName") String beanName,
			@RequestParam(value = "caption", required = false) String caption, HttpServletResponse response)
			throws Exception {
		ServletOutputStream out = null;
		Map<String, Object> params = getRequestParameters();
		try {
			if (StringUtils.isBlank(caption))
				caption = "export";

			response.setContentType("application/unknown;charset=GBK");
			response.setHeader("Content-Disposition", "attachment;filename="
					+ new String(caption.getBytes("GBK"), "ISO8859-1") + ".xlsx");
			response.setCharacterEncoding("GBK");

			List<ExportDefine> exportDefines = getExportDefineList(beanName,params);
			Workbook wb = generateWorkbook.generateWorkbook(getPagingService(beanName), exportDefines,
					params);

			out = response.getOutputStream();
			wb.write(out);
			out.flush();
		} catch (Exception e) {
			logger.error(e.getMessage());
		} finally {
			IOUtils.closeQuietly(out);
		}
	}

	protected List<ExportDefine> getExportDefineList(String beanName,Map<String, Object> params) {
		List<ExportDefine> exportDefines = this.getPagingService(beanName).getExportDefines(params);
		if (exportDefines != null)
			return exportDefines;

		String jsonExportDefines = getRequest().getParameter(EXPORT_DEFINES);
		if (StringUtils.isNotEmpty(jsonExportDefines)) {
			return JackJson.fromJsonToObject(jsonExportDefines, new TypeReference<List<ExportDefine>>() {
			});
		}

		throw new ServiceException("获取导出Excel格式信息失败！");
	}
}
