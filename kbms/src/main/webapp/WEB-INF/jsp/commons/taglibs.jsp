<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<script type="text/javascript">
	var ctx = "${ctx}";
	document.ctx = ctx;
	var dictionary = new Object();
</script>
<link rel="stylesheet" type="text/css"
	href="${ctx}/resources/css/icons.css">
<link rel="stylesheet" type="text/css"
	href="${ctx}/resources/css/warn.css">
<link id="themeCss" rel="stylesheet" type="text/css"
	href="${ctx}/resources/js/extjs4/resources/ext-theme-custom/ext-theme-custom-all.css">
<script type="text/javascript">
	//获取主题
	//var themeCssPath = null;
	//var arr, reg = new RegExp("(^| )" + "themeCss" + "=([^;]*)(;|$)");
	//if (arr = document.cookie.match(reg))
	//	themeCssPath = unescape(arr[2]);
	//if (themeCssPath) {
	//	setActiveStyleSheet(themeCssPath);
	//} else {
	//	setActiveStyleSheet("ext-theme-classic/ext-theme-classic-all.css");
	//}
	//function setActiveStyleSheet(filename) {
	//	document.getElementById("themeCss").href = "${ctx}/resources/js/extjs4/resources/"
	//			+ filename;
	//}
</script>
<script type="text/javascript"
	src="${ctx}/resources/js/extjs4/ext-all.js"></script>
<script type="text/javascript"
	src="${ctx}/resources/js/extjs4/locale/ext-lang-zh_CN.js"></script>
<script type="text/javascript" src="${ctx}/resources/js/app/commons.js"></script>
<script type="text/javascript" src="${ctx}/resources/js/app/warn.js"></script>
<script type="text/javascript">
	history.forward(1);
	Ext.QuickTips.init();
</script>
