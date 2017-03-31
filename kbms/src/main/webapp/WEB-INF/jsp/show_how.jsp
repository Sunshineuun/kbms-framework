<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@include file="/WEB-INF/jsp/commons/taglibs.jsp"%>
<%@page import="com.winning.kbms.core.utils.ContextUtils"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>知识管理系统</title>
<style type="text/css">
p {
	margin: 5px;
}

#loading-mask {
	background-color: white;
	height: 100%;
	position: absolute;
	left: 0;
	top: 0;
	width: 100%;
	z-index: 20000;
}

#loading {
	height: auto;
	position: absolute;
	left: 45%;
	top: 40%;
	width: 200px;
	padding: 2px;
	z-index: 20001;
}

#loading a {
	color: #225588;
}

#loading .loading-indicator {
	background: white;
	color: #444;
	font: bold 13px Helvetica, Arial, sans-serif;
	height: auto;
	margin: 0;
	padding: 10px;
}

#loading-msg {
	font-size: 10px;
	width: 250px;
	font-weight: normal;
}
</style>
<script type="text/javascript"
	src="${ctx}/resources/js/extjs4/ux/IFrame.js"></script>
<script type="text/javascript"
	src="${ctx}/resources/js/extjs4/ux/TabCloseMenu.js"></script>
<script type="text/javascript"
	src="${ctx}/resources/js/ux/SearchField.js"></script>
<script type="text/javascript"
	src="${ctx}/resources/js/app/index/menu.js"></script>
<script type="text/javascript"
	src="${ctx}/resources/js/app/index/main.js"></script>
<script type="text/javascript"
	src="${ctx}/resources/js/app/index/showHow.js"></script>
</head>
<body>
	<div id="loading-mask"></div>
	<div id="loading">
		<div class="loading-indicator">
			<img src="${ctx}/resources/images/index/loading.gif" width="120"
				height="120"
				style="margin-right: 8px; float: left; vertical-align: top;" />
		</div>
	</div>
</body>
</html>