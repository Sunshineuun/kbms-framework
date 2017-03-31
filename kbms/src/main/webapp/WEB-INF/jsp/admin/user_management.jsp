<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@page import="com.winning.kbms.core.utils.ContextUtils"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@include file="/WEB-INF/jsp/commons/taglibs.jsp"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>用户管理界面</title>
<link rel="stylesheet" type="text/css" href="${ctx}/resources/js/extjs4/example/ux/css/ItemSelector.css" />
<script type="text/javascript">
	var auth = <%=ContextUtils.hasPermission ("user:add","user:edit","user:del","user:updatePassword","user:authorize","user:grantRoleToUser","user:changeWsKey")%>;
</script>
<script type="text/javascript" src="${ctx}/resources/js/ux/Combobox.js"></script>
<script type="text/javascript" src="${ctx}/resources/js/ux/GridView.js"></script>
<script type="text/javascript" src="${ctx}/resources/js/app/admin/userManagement.js"></script>
</head>
<body>

</body>
</html>