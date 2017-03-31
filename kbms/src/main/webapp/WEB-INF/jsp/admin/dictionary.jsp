<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@page import="com.winning.kbms.core.utils.ContextUtils"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@include file="/WEB-INF/jsp/commons/taglibs.jsp"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>新数据字典</title>
<script type="text/javascript">
	var authc = <%=ContextUtils.hasPermission ("dictionary:add","dictionary:edit","dictionary:del")%>;
</script>
<script type="text/javascript" src="${ctx}/resources/js/ux/Combobox.js"></script>
<script type="text/javascript" src="${ctx}/resources/js/ux/GridView.js"></script>
<script type="text/javascript" src="${ctx}/resources/js/app/admin/dictionary.js"></script>
</head>
<body>

</body>
</html>