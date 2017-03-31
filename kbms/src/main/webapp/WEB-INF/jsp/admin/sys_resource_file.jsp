<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@page import="com.winning.kbms.core.utils.ContextUtils"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@include file="/WEB-INF/jsp/commons/taglibs.jsp"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script type="text/javascript">
	var authc = <%=ContextUtils.hasPermission ("sysResourceFile:add","sysResourceFile:edit","sysResourceFile:del")%>;
</script>
<script type="text/javascript" src="${ctx}/resources/js/ux/Combobox.js"></script>
<script type="text/javascript" src="${ctx}/resources/js/ux/GridView.js"></script>
<script type="text/javascript" src="${ctx}/resources/js/app/admin/sysResourceFile.js"></script>
</head>
<body>

</body>
</html>