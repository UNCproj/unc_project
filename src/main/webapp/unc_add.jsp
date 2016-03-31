<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ page import="unc.helpers.UncObject" %>
<%@ page import="java.sql.SQLException" %>
<%@ page import="java.beans.PropertyVetoException" %>
<%@ page import="db.SQLQueriesHelper" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="unc.helpers.Param" %>
<%@ page import="java.sql.Statement" %>
<%@ page import="java.sql.Connection" %>
<%@ page import="db.DataSource" %>
<%@ page import="java.sql.ResultSet" %>
<%@ page import="beans.*" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
    String objectType = request.getParameter("type");
    UncObject currentObject = new UncObject(objectType);

    if (!currentObject.getType().equals("4")) {
        response.sendRedirect("jsp404.jsp");
    }

    try {
        currentObject.loadAttributesListFromDB();
    } catch (SQLException | PropertyVetoException e) {
        e.printStackTrace();
    }
    ArrayList<Param> params = currentObject.getParams();
%>

<html ng-app="add">
<head>

    <title>Новое объявление</title>

    <c:catch var="e">
        <c:import url="/includes/add/scripts/<%= currentObject.getType() %>.jspf"/>
    </c:catch>
    <c:if test="${!empty e}">
        <c:import url="/includes/add/scripts/default.jspf"/>
    </c:if>

    <c:catch var="e">
        <c:import url="/includes/add/css/<%= currentObject.getType() %>.jspf"/>
    </c:catch>
    <c:if test="${!empty e}">
        <c:import url="/includes/add/css/default.jspf"/>
    </c:if>
</head>

<body>
<div class="main">

    <c:catch>
        <%@ include file="/includes/add/headers/default.jsp" %>
    </c:catch>
    <div class="list-categories clearfix" >
        <ul>
            <li class="list-categories-li">
                <a href="index.jsp">Главная</a>
            </li>
            <li class="list-categories-li">
                <a href="">Новое объявление</a>
            </li>
        </ul>
    </div>
    <div class="content">
        <ul class="nav nav-tabs">
            <li><a href="#1" data-toggle="tab" style="border: 0px">
                <%if (currentObject.getType().equals("4")) {%>
                Новое объявление
            </a></li>
            <% } %>
        </ul>

        <div class="settings tab-content" ng-controller="addController">


            <%
                String attrGroupName = currentObject.getAttributeGroups().get(0);
//                ArrayList<Param> currentGroupParams = currentObject.getParams(attrGroupName);
            %>

            <div id="<%= attrGroupName %>" class="tab-pane fade in">
                <div class="table-pos">
                    <form enctype="multipart/form-data">

                        <div id="category" class="col-md-12">
                            <label class="col-md-12 category-label">Выберите категорию</label>
                            <div class="col-md-3 list-group" id="category-div-1">
                                <%
                                    Connection connection = null;
                                    try {
                                        connection = DataSource.getInstance().getConnection();
                                        Statement statement = connection.createStatement();
                                        ResultSet resultSet = statement.executeQuery(SQLQueriesHelper.selectTypes("advert"));
                                        while (resultSet.next()) {
                                %>
                                <a class="list-group-item a-category"><%=resultSet.getString("ot_name")%></a>
                                <%
                                        }
                                    } finally {
                                        connection.close();
                                    }%>
                            </div>
                            <div class="col-md-3 list-group" id="category-div-2"></div>
                            <div class="col-md-3 list-group" id="category-div-3"></div>
                            <div class="col-md-3 list-group" id="category-div-4"></div>
                        </div>
                        <div class="col-md-12 attributes"></div>
                    </form>
                </div>
            </div>
        </div>
    </div>
    <c:catch>
        <c:import url="/includes/object/footers/default.jspf"/>
    </c:catch>
</div>

</body>
</html>