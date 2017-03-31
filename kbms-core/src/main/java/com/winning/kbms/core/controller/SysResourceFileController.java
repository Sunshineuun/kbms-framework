package com.winning.kbms.core.controller;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.winning.domain.kbms.core.SysResourceFile;
import com.winning.kbms.core.commons.PropertyConfigurer;
import com.winning.kbms.core.domain.JResult;
import com.winning.kbms.core.service.ManagementService;
import com.winning.kbms.core.service.SysResourceFileService;
import com.winning.utils.RandomUIDUtils;

@Controller
@RequestMapping("/sysResourceFile")
public class SysResourceFileController extends QueryController {
	@Resource(name = "sysResourceFileService")
	private ManagementService<SysResourceFile> sysResourceFileService;

	private SysResourceFileService getSysResourceFileService() {
		return (SysResourceFileService) sysResourceFileService;
	}

	@RequestMapping("/uploadResourcesFile")
	@ResponseBody
	public JResult uploadResourcesFile(MultipartHttpServletRequest request) {
		try {
			SysResourceFile resourceFile = new SysResourceFile();
			resourceFile.setId(RandomUIDUtils.getUUID());

			CommonsMultipartFile file = (CommonsMultipartFile) request.getFile("file");
			String fileName = file.getFileItem().getName();
			String folderPath = PropertyConfigurer.getString("sys.uploadFilePath");

			File folder = new File(folderPath);
			if (!folder.exists())
				folder.mkdirs();

			if (StringUtils.isNotEmpty(fileName)) {
				resourceFile.setFileName(fileName.substring(0, fileName.lastIndexOf(".")));
				if (fileName.contains("."))
					resourceFile.setExtension(fileName.substring(fileName.lastIndexOf(".") + 1));
			}
			resourceFile.setUploadBy(this.getCurrUser().getUserName());
			resourceFile.setUploadTime(new Date());
			resourceFile.setUpdateBy(this.getCurrUser().getUserName());
			resourceFile.setUpdateTime(new Date());
			resourceFile.setType(request.getParameter("type"));

			String filePathName = folderPath + "/" + resourceFile.getId() + "." + resourceFile.getExtension();
			FileUtils.copyInputStreamToFile(file.getInputStream(), new File(filePathName));

			sysResourceFileService.add(resourceFile);
		} catch (IOException e) {
			e.printStackTrace();
			return new JResult(false, e.getMessage());
		}
		return new JResult(true, "");
	}

	@RequestMapping("/removeResourcesFile")
	@ResponseBody
	public JResult removeResourcesFile(@RequestParam("id") String id) {
		sysResourceFileService.deleteById(id);
		return new JResult(true, "");
	}

	@RequestMapping("/downloadResourcesFile")
	public void downloadResourcesFile(@RequestParam("id") String id, HttpServletResponse response) throws Exception {
		ServletOutputStream sos = null;
		try {
			SysResourceFile resourceFile = getSysResourceFileService().getById(id);

			if (resourceFile == null)
				throw new Exception("ID=“" + id + "”的文件已删除！");

			String fileName = new String(resourceFile.getFileName().getBytes("GBK"), "ISO8859-1");
			response.setContentType("application/unknown;charset=gbk");
			response.setHeader("Content-Disposition",
					"attachment;filename=" + fileName + "." + resourceFile.getExtension());
			response.setCharacterEncoding("GBK");

			sos = response.getOutputStream();

			String folderPath = PropertyConfigurer.getString("sys.uploadFilePath");
			String filePathName = folderPath + "/" + resourceFile.getId() + "." + resourceFile.getExtension();
			File downloadFile = new File(filePathName);

			if (!downloadFile.exists())
				throw new Exception("下载的文件不存在！");

			FileUtils.copyFile(downloadFile, sos);
		} finally {
			IOUtils.closeQuietly(sos);
		}
	}
}
