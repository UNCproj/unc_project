<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="unc.helpers.UncObject" %>
<%@ page import="java.beans.PropertyVetoException" %>
<%@ page import="java.sql.SQLException" %>
<%@ page import="java.net.URL" %><%--
  Created by IntelliJ IDEA.
  User: Денис
  Date: 24.02.2016
  Time: 12:17
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%!public URL fileURL;%>
<%
    String objectId = request.getParameter("id");
    UncObject currentObject = new UncObject(objectId);

    try {
        currentObject.selectFromDB();
    } catch (SQLException|PropertyVetoException e) {
        e.printStackTrace();
    }
%>
<html>
<head>
    <title>update | <%= currentObject.getName() %></title>
    <c:catch var="e">
        <c:import url="/includes/scripts/<%= currentObject.getType() %>.jspf" />
    </c:catch>
    <c:if test="${!empty e}">
        <c:import url="/includes/scripts/default.jspf" />
    </c:if>

    <%
        if(customPartsHelper.isCustomCSS()) {
    %>
    <jsp:include page = "/includes/css/<%= currentObject.getType() %>" flush="true" />
    <%
    } else {
    %>
    <jsp:include page="/includes/css/default.jspf" flush="true" />
    <%
        }
    %>
</head>
<body>
    <%
        if(customPartsHelper.isCustomHeader()) {
    %>
    <jsp:include page="/includes/headers/<%= currentObject.getType() %>" flush="true" />
    <%
    } else {
    %>
    <jsp:include page="/includes/headers/default.jspf" flush="true" />
    <%
        }
    %>

<ul class="nav nav-tabs">
    <% for (int i = 0; i < currentObject.getAttributeGroups().size(); i++) { %>
    <li><a href="#<%= o.getAttributeGroups().get(i).getName() %>" data-toggle="tab">О себе</a></li>
    <% } %>

    <%
        if(o.isCustomTabs) {
    %>
    <jsp:include page="tab_nav_<%= o.getId() %>.jspf" flush="true" />
    <%
        }
    %>
</ul>

<div class="settings tab-content">
    <% for (int i = 0; i < o.getAttributes().length; i++) { %>
    <div id="<%= o.getgetAttributeGroups().get(i).getName() %>" class="tab-pane fade in active">
        <!--Это тоже можно будет вынисте в отдельный инклюд-->
        <form>
            <% for (int i = 0; i < o.getAttributes().length; i++) { %>
            <input/><!--Какие то поля редактирования атрибута-->
            <% } %>
            <submit onclick="update()"><!--Метод update() содержится в *.js файле, который заинклюдили в начале-->
        </form>
    </div>
    <% } %>
</div>

<%
    if(o.isCustomTabs) {
%>
<jsp:include page="tab_<%= o.getId() %>.jspf" flush="true" />
<%
    }
%>

<%
    if(o.isCustomContent) {
%>
<jsp:include page="content_<%= o.getId() %>.jspf" flush="true" />
<%
    }
%>

<%
    if(o.isCustomFooter) {
%>
<jsp:include page="footer_<%= o.getId() %>.jspf" flush="true" />
<%
} else {
%>
<jsp:include page="footer_default.jspf" flush="true" />
<%
    }
%>
</body>
</html>