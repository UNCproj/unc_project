<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="unc.helpers.UncObject" %>
<%@ page import="java.beans.PropertyVetoException" %>
<%@ page import="java.sql.SQLException" %>
<%@ page import="java.net.URL" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="unc.helpers.Param" %><%--
  Created by IntelliJ IDEA.
  User: Денис
  Date: 24.02.2016
  Time: 12:17
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String objectId = request.getParameter("id");
    UncObject currentObject = new UncObject(objectId, null, true);

    try {
        currentObject.selectFromDB();
    } catch (SQLException|PropertyVetoException e) {
        e.printStackTrace();
    }
%>
<html ng-app="default">
<head>
    <title>update | <%= currentObject.getName() %></title>
    <c:catch var="e">
        <c:import url="/includes/update/scripts/<%= currentObject.getType() %>.jspf" />
    </c:catch>
    <c:if test="${!empty e}">
        <c:import url="/includes/update/scripts/default.jspf" />
    </c:if>

    <c:catch var="e">
        <c:import url="/includes/update/css/<%= currentObject.getType() %>.jspf" />
    </c:catch>
    <c:if test="${!empty e}">
        <c:import url="/includes/update/css/default.jspf" />
    </c:if>
</head>
<body>
<c:catch var="e">
    <c:import url="/includes/update/headers/<%= currentObject.getType() %>.jspf" />
</c:catch>
<c:if test="${!empty e}">
    <c:import url="/includes/update/headers/default.jspf" />
</c:if>
    <div class="main">
                <div id="header">
                    <ul class="menu">
                        <li><a class="a-outline button-style" href="index.jsp">Главная</a></li>
                    </ul>
                    <div class="enter">
                        <a class="button-style button-style-enter a-outline" href="reg-and-login.jsp">Войти</a>
                    </div>
                </div>
                <div class="content">
                    <ul class="custom-tabs nav nav-tabs tabs">
                    <% String activeSwicth1 = "active"; %>
                    <% if (currentObject.getAttributeGroups() != null && currentObject.getAttributeGroups().size() != 0) {%>
                        <li class="active"><a href="#<%= currentObject.getAttributeGroups().get(0) %>" data-toggle="tab"><%=currentObject.getAttributeGroups().get(0)%></a></li>
                        <% for (int i = 1; i < currentObject.getAttributeGroups().size(); i++) { %>
                            <li><a href="#<%= currentObject.getAttributeGroups().get(i) %>" data-toggle="tab"><%=currentObject.getAttributeGroups().get(i)%></a></li>
                        <% } %>
                    <% } %>
                    </ul>
                    <div class="settings tab-content .col-md-3 .col-md-offset-3" ng-controller="updateController">
                        <% for (int i = 0; i < currentObject.getAttributeGroups().size(); i++) {
                            String attrGroupName = currentObject.getAttributeGroups().get(i);
                            ArrayList<Param> currentGroupParams = currentObject.getParams(attrGroupName);
                        %>
                        <div id="<%= attrGroupName %>" class="tab-pane fade in <%= activeSwicth1 %>">
                            <div class="table-pos">
                                <table class="table table-params">
                                    <form>
                                        <% for (int j = 0; j < currentGroupParams.size(); j++) { %>
                                        <c:catch var="e">
                                            <c:import url="/includes/update/attr_views/<%= currentObject.getType() %>.jsp" />
                                        </c:catch>
                                        <c:if test="${!empty e}">
                                            <c:import url="/includes/update/attr_views/default.jsp">
                                                <c:param name="attr_name" value="<%= currentGroupParams.get(j).getName()%>" />
                                                <c:param name="attr_value" value="<%= currentGroupParams.get(j).getValue()%>" />
                                                <c:param name="attr_type" value="<%= currentGroupParams.get(j).getType()%>" />
                                            </c:import>
                                        </c:if>
                                        <% } %>
                                    </form>
                                </table>
                                <input type="button" onclick="location.href='unc_object.jsp?id=<%= request.getParameter("id") %>'" class="button-style a-outline button-update" value="Обновить" ng-click="update()" />
                            </div>
                        </div>
                        <% activeSwicth1 = ""; %>
                        <% } %>
                    </div>
                    <c:catch var="e">
                        <c:import url="/includes/update/tabs/<%= currentObject.getType() %>.jspf" />
                    </c:catch>
                </div>
                <c:catch var="e">
                    <c:import url="/includes/update/footers/default.jspf" />
                </c:catch>
    </div>
</body>
</html>