<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ page isErrorPage="true"%>
<%
    response.setHeader("Cache-Control","no-cache"); //HTTP 1.1
    response.setHeader("Pragma","no-cache");        //HTTP 1.0
    response.setDateHeader ("Expires", 0);      //prevents caching at the proxy server  
%>
<c:if test="${errormessage==null}">
<c:set var="errormessage">
<%=exception.toString()%>
</c:set>
</c:if>
<jsp:useBean id="now" class="java.util.Date" />
<bean:message key="error.page" bundle="common"/>
<hr>
<pre>
<bean:message key="error.code" bundle="common"/> : <c:out value="${errorcode}" default="500"/>
<bean:message key="error.desc" bundle="common"/> : <c:out value="${errormessage}"/>
Request URL : <%=request.getRequestURL()%>
Request <bean:message key="client.ip" bundle="common"/> : <%=request.getRemoteAddr()%>
<bean:message key="error.date" bundle="common"/> : <fmt:formatDate value="${now}" pattern="yyyy/MM/dd HH:mm:ss"/>

detail:
<%
for(int i=0;i<exception.getStackTrace().length;i++){
    out.print("\t");
    out.println(exception.getStackTrace()[i].toString());
}
%>
</pre>