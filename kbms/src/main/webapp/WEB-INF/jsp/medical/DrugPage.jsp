<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@include file="/WEB-INF/jsp/commons/taglibs.jsp"%>
<%@page import="com.winning.kbms.medical.vo.Pager"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

<title>药品说明书</title>

<link rel="stylesheet" type="text/css" href="${ctx}/resources/css/medical_style_blue.css"></link>
<script type="text/javascript" src="${ctx}/resources/js/jquery/jquery-1.7.2.js"></script>
<script type="text/javascript" src="${ctx}/resources/js/app/medical/daohang.js"></script>

<script type="text/javascript">
	window.onload = function(){
		initDaoHang();
	}
	
	window.onresize = function(){
		initDaoHang();
	}
</script>



</head>
<body class="bodyDiv">

	<ul class="daohang" id="daohang">
		<c:forEach items="${drugMap}" var="item">
				<c:if test="${item.value!=null&&item.value!=''&&item.key!='药品代码:'&&item.key!='药品英文名称:'&&item.key!='药品通用名称:'&&item.key!='商品名称:'}">
						<li>
							<a href="#${item.key}">${item.key}</a>
						</li>
				</c:if>
		</c:forEach>
	</ul>

	<div class="container" id="container">

		<div class="txtContainer">
			<div class="detail detail1 ">


				<div class='titleFont'>${drugMap['药品通用名称:']}</div>

				<div class='drugName'>

					<span class="drugTitle">药品名称:</span><br />
					<dd>
						<span class="drugNames">通用名称:${drugMap['药品通用名称:']}</span><br /> <span
							class="drugNames">英文名称:${drugMap['药品英文名称:']}</span><br /> <span
							class="drugNames">商品名:${drugMap['商品名称:']}</span><br />
					</dd>
				</div>

				<dl>
					<dt></dt>
					<c:forEach items="${drugMap}" var="item">
						<c:if
							test="${item.value!=null&&item.value!=''&&item.key!='药品代码:'&&item.key!='药品英文名称:'&&item.key!='药品通用名称:'&&item.key!='商品名称:'}">
							<dt id="${item.key}" name="${item.key}">
								<span class="fl">${item.key}</span>
							</dt>

							<dd>${item.value}</dd>
						</c:if>
					</c:forEach>

				</dl>

			</div>
		</div>

	</div>
</body>
</html>