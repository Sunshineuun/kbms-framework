package com.winning.test;

import java.io.IOException;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpClientParams;
import org.apache.commons.httpclient.params.HttpMethodParams;

public class HttpContentGeter {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
//		getHtmlContent("http://yp.120ask.com/manual/14715.html");
		getHtmlContent("http://yp.120ask.com/manual/15719.html");
	}

	public static String getHtmlContent(String url) {
		String htmlString = null;
		// 构造HttpClient的实例
		HttpClient httpClient = new HttpClient();
		// 创建GET方法的实例
		GetMethod getMethod = new GetMethod(url);
//		PostMethod psMethod = new PostMethod(url);
		//设置头文件UTF-8
		getMethod.addRequestHeader("Content-Type", "text/html;charset=utf-8");
		
		//设置请求参数
		HttpClientParams params = httpClient.getParams();
		// 使用系统提供的默认的恢复策略
		params.setParameter(HttpMethodParams.RETRY_HANDLER,
				new DefaultHttpMethodRetryHandler());
		//设置html内容的编码
		params.setContentCharset("utf-8");
//		params.setConnectionManagerTimeout(2000);
//		params.setSoTimeout(4000);
		
		try {
			// 执行getMethod
			int statusCode = httpClient.executeMethod(getMethod);
			if (statusCode != HttpStatus.SC_OK) {
				System.err.println("请求失败: "
						+ getMethod.getStatusLine());
				return htmlString;
			}else{
				System.out.println("请求成功返回");
			}
			// 读取内容
//			byte[] responseBody = getMethod.getResponseBody();
			
			// 处理内容
//			htmlString = new String(responseBody,"utf-8");
			htmlString = getMethod.getResponseBodyAsString();
			if(htmlString.indexOf("药品不存在")>0){
				htmlString = null;
				return htmlString;
			}
			System.out.println(htmlString);
		} catch (HttpException e) {
			// 发生致命的异常，可能是协议不对或者返回的内容有问题
			System.out.println("请检查您的地址是否正确!");
			e.printStackTrace();
		} catch (IOException e) {
			// 发生网络异常
			e.printStackTrace();
		} finally {
			// 释放连接
			getMethod.releaseConnection();
		}
		return htmlString;
		
		
	}

}
