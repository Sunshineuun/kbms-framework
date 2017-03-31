package com.winning.kbms.medical.utils;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.WebApplicationContext;

public class Constants {
	public static  ApplicationContext SPRING_CONTAINER = null;
	public static  WebApplicationContext SPRING_CONTAINER_WEB = null;
	public static final String SYS_TMP_DIR_PATH = System.getProperty("java.io.tmpdir");
			//"D:\\KBMS-WorkSpace\\TEMP";
			//System.getProperty("java.io.tmpdir");
//	public static String LUC_NAME = SPRING_CONTAINER_WEB.getServletContext().getRealPath("/")+"luc";
}
