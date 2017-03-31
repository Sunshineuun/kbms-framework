<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@include file="/WEB-INF/jsp/commons/taglibs.jsp"%>
<%@page import="com.winning.kbms.medical.vo.Pager"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

<title>药品搜索</title>


<link rel="stylesheet" type="text/css" href="${ctx}/resources/css/medical_style_blue.css"></link>


<script type="text/javascript" src="${ctx}/resources/js/jquery/jquery-1.7.2.js"></script>
<!-- <script type="text/javascript" src="${ctx}/resources/js/jquery.autoSuggest.js"></script> -->


<script type="text/javascript">
	
	
	jQuery(document).ready(function(){
		
		$(window).scroll(
			function(){
			var minHeight = 500;
			var s = jQuery(window).scrollTop();
			if(s>minHeight){
				jQuery("#btop").fadeIn(100);
			}else{
				jQuery("#btop").fadeOut(100);
			}}
		
		);
		
		jQuery("#btop").mouseover(function(){
			jQuery(this).removeClass("backToTop").addClass("backToTop01");
		}).mouseout(function(){
			jQuery(this).removeClass("backToTop01").addClass("backToTop");
		});
		

	});
	
	
	
</script>	

<style type="text/css">
	.container{
		margin-top:100px;
	}
</style>		

</head>
	<body class="bodyDiv">

		<div class="container">
			<div class="txtContainer">
									<div class="detail detail1 ">
								
										<c:forEach items="${results}" var="item" > 
											   <div class="duanluo">
											   		<h3>
												   		<a href="${basePath}DrugSearchMMAP.do?id=${item.id}">
												   			${item.productNameCn}
												   		</a>
												   		
											   		</h3>
											   		
											   		<p>
											   			<b>商品名</b>
											   			${item.tradeName}
											   			
											   			<b>成分</b>
											   			${item.component}
											   			<br>
											   			<b>适应症</b>
											   			${item.indication}
											   		</p>
											   </div>
		                                </c:forEach>
		                                
										<div class="clear"></div>
								
							
					</div>
			</div>
			<div id="btop" class="backToTop" onclick="scroll(0,0)"></div>
		</div>
	</body>
</html>