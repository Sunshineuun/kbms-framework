package com.winning.kbms.core.controller;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 用于跳转到jsp页面
 * 
 * @author gang.liu
 * @date Jan 16, 2014
 */
@Controller
public class ForwardController extends BaseController {
	private final Logger logger = LoggerFactory.getLogger(getClass());
	private final String FORWARD = "/forward/";

	@RequestMapping("/forward/**")
	public String forward(HttpServletRequest request) {
		String contextPath = request.getContextPath();
		String requestURI = request.getRequestURI();

		logger.info("当前请求的基地址：{}，uri：{}", contextPath, requestURI);

		String ctx = contextPath + FORWARD;
		String path = requestURI.substring(requestURI.indexOf(ctx) + ctx.length());
		if (path.lastIndexOf(".") > 0) {
			path = path.substring(0, path.lastIndexOf("."));
		}

		logger.info("访问路径：{}", path);
		return path;
	}
}
