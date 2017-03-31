package com.winning.kbms.core.controller;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.multipart.support.ByteArrayMultipartFileEditor;

import com.winning.kbms.core.domain.JResult;
import com.winning.kbms.core.service.ImportService;
import com.winning.kbms.core.utils.ApplicationContextUtils;

/**
 * 
 * @author gang.liu
 * @date 2013-4-2
 */
@Controller
@RequestMapping("/importData")
public class ImportDataController extends BaseController {
	private static final Logger logger = LoggerFactory.getLogger(ImportDataController.class);

	/**
	 * 上传导入文件
	 * 
	 * @return
	 * @throws IOException
	 */

	@RequestMapping("/{beanName}/importFile")
	@ResponseBody
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public JResult importFile(@PathVariable("beanName") String beanName, MultipartHttpServletRequest request,
			HttpServletResponse response) throws IOException {
		Map<String, Object> params = this.getRequestParameters(request);

		ImportService importService = ApplicationContextUtils.getBean(beanName);
		Iterator<String> iterator = request.getFileNames();
		while (iterator.hasNext()) {
			CommonsMultipartFile multipartFile = (CommonsMultipartFile) request.getFile((String) iterator.next());
			String fileName = multipartFile.getFileItem().getName();

			if (StringUtils.endsWith(fileName, ".xls") || StringUtils.endsWith(fileName, ".xlsx"))
				importService.importExcel(multipartFile.getInputStream(), params);
			else if (StringUtils.endsWith(fileName, ".csv"))
				importService.importCsv(multipartFile.getInputStream(), params);
			else
				continue;
		}

		return new JResult(true, "");
	}

	/**
	 * 上传导入文件
	 * 
	 * @return
	 * @throws IOException
	 */
	@RequestMapping("/downloadTemplete")
	@ResponseBody
	public void downloadTemplete(@RequestParam("fileName") String fileName, HttpServletResponse response) {
		ServletOutputStream out = null;
		try {
			fileName = URLDecoder.decode(fileName, "utf-8");
			response.setContentType("application/unknown;charset=GBK");
			response.setHeader("Content-Disposition", "attachment;filename="
					+ new String(fileName.getBytes("GBK"), "ISO8859-1"));
			response.setCharacterEncoding("GBK");

			Resource resource = ApplicationContextUtils.getResource("classpath:/templete/" + fileName);
			out = response.getOutputStream();

			IOUtils.copy(resource.getInputStream(), out);
			out.flush();
		} catch (Exception e) {
			logger.error(e.getMessage());
		} finally {
			IOUtils.closeQuietly(out);
		}
	}

	protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) throws ServletException {
		binder.registerCustomEditor(byte[].class, new ByteArrayMultipartFileEditor());
	}
}
