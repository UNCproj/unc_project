<%@ page import="beans.BeansHelper" %>
<%@ page import="beans.UserAccountBean" %>
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
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
    String objectType = request.getParameter("type");
    UncObject currentObject = new UncObject(null, null, objectType);

    try {
        currentObject.loadAttributesListFromDB();
    } catch (SQLException |PropertyVetoException e) {
        e.printStackTrace();
    }
    ArrayList<Param> params = currentObject.getParams();
//    for(Param p: params){
//        out.println(p.getName() + ":" + p.getNameRU() + ":" + p.getOrder() + ":" + p.getType());
//    }
%>

<html ng-app="default">
<head>
    <title>Страница добавления</title>
    <script type="text/javascript" src="resources/scripts/ng-flow-standalone.min.js"></script>
    <c:catch var="e">
        <c:import url="/includes/add/scripts/<%= currentObject.getTypeName() %>.jspf" />
    </c:catch>
    <c:if test="${!empty e}">
        <c:import url="/includes/add/scripts/default.jspf" />
    </c:if>

    <c:catch var="e">
        <c:import url="/includes/add/css/<%= currentObject.getTypeName() %>.jspf" />
    </c:catch>
    <c:if test="${!empty e}">
        <c:import url="/includes/add/css/default.jspf" />
    </c:if>
</head>

<body>

<%--<%if (currentObject.getTypeName().equals("advert")){%>--%>
    <%--<div class="page-header">--%>
        <%--<h1>Новое объявление</h1>--%>
    <%--</div>--%>
<%--<%}else{%>--%>
    <%--<div class="page-header">--%>
        <%--<h1>Страница добавления</h1>--%>
    <%--</div>--%>
<%--<%}%>--%>

<ul class="nav nav-tabs">
    <% for (int i = 0; i < currentObject.getAttributeGroups().size(); i++) { %>
    <%--<li><a href="#<%= currentObject.getAttributeGroups().get(i) %>" data-toggle="tab"><%=currentObject.getAttributeGroups().get(i)%></a></li>--%>
    <li><a href="#<%= currentObject.getAttributeGroups().get(i) %>" data-toggle="tab">
        <%if(currentObject.getTypeName().equals("advert")){%>
            Новое объявление
        <%}else{%>
            <%=currentObject.getAttributeGroups().get(i)%>
        <%}%>
    </a></li>
    <% } %>

    <c:catch var="e">
        <c:import url="/includes/add/headers/<%= currentObject.getType() %>.jspf" />
    </c:catch>
</ul>

<div class="settings tab-content .col-md-3 .col-md-offset-3" ng-controller="addController">

    <% for (int i = 0; i < currentObject.getAttributeGroups().size(); i++) {
        String attrGroupName = currentObject.getAttributeGroups().get(i);
        ArrayList<Param> currentGroupParams = currentObject.getParams(attrGroupName);
    %>

    <div id="<%= attrGroupName %>" class="tab-pane fade in">

        <form enctype="multipart/form-data">


            <% for (int j = 0; j < currentGroupParams.size(); j++) {
                if(j==2 && currentObject.getTypeName().equals("advert")){%>
                    <label>Название</label>
                    <input type="text" ng-model="object.name">
                <%}%>
            <%if(j==0 && currentObject.getTypeName().equals("advert")){%>

            <div id="category">
            <label>Категория</label>
            <select ng-model="object.type1" ng-change="loadTypes('type1')" id="type1">
                <option disabled value="">--Выберите--</option>
            <%Connection connection = null;
            try{
                connection = DataSource.getInstance().getConnection();
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(SQLQueriesHelper.selectTypes("advert"));
                while (resultSet.next()){
                    %><option value="<%=resultSet.getString("ot_name")%>"><%=resultSet.getString("ot_name")%></option>
                <%
                }
            }finally {
                connection.close();
            }%>
            </select>
                    <select ng-model="object.type2" ng-change="loadTypes('type2')" id="type2"></select>
                    <select ng-model="object.type3" ng-change="loadTypes('type3')" id="type3"></select>
                    <select ng-model="object.type4" ng-change="loadTypes('type4')" id="type4"></select>
                </div>
                <%--<label>Категория</label>--%>
            <%--<div id="category">--%>
                <%--<select ng-model="object.category" onclick="loadSel()">--%>
                    <%--<option disabled value="">--...--</option>--%>
                    <%--<option value="1">1</option>--%>
                    <%--<option value="2">2</option>--%>
                <%--</select>--%>
            <%--</div>--%>
            <%}%>
            <c:catch var="e">
                <c:import url="/includes/add/attr_views/<%= currentObject.getTypeName() %>.jsp" />
            </c:catch>

            <c:if test="${!empty e}">

                <c:import url="/includes/add/attr_views/default.jsp">

                    <c:param name="attr_name" value="<%= currentGroupParams.get(j).getName()%>" />
                    <c:param name="attr_name_ru" value="<%= currentGroupParams.get(j).getNameRU()%>" />
                    <c:param name="attr_type" value="<%= currentGroupParams.get(j).getType()%>" />

                </c:import>

            </c:if>

            <% } %>
            <div flow-init="{target: '/unc-project/upload', testChunks:false}"
                flow-file-added="fileAdded($file, $event, $flow)"
                flow-complete="complete()"
                flow-name="uploader.flow">
            <%--<input type="button" value="Создать" ng-click="add()" />--%>
            <input type="button" value="Создать" ng-click="add()"/>
            </div>
        </form>
    <%--</div>--%>
    <% } %>
</div>

<c:catch var="e">
    <c:import url="/includes/add/tabs/<%= currentObject.getType() %>.jspf" />
</c:catch>

<c:catch var="e">
    <c:import url="/includes/update/footers/<%= currentObject.getType() %>.jspf" />
</c:catch>
<c:if test="${!empty e}">
    <c:import url="/includes/update/footers/default.jspf" />
</c:if>
</body>
</html>
