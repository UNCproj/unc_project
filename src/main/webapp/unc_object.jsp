<%-- 
    Document   : unc_object
    Created on : 04.03.2016, 11:03:18
    Author     : Andrey
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="unc.helpers.UncObject" %>
<%@ page import="java.beans.PropertyVetoException" %>
<%@ page import="java.sql.SQLException" %>
<%@ page import="java.net.URL" %>
<%
    String objectId = request.getParameter("id");
    UncObject currentObject = new UncObject(objectId, null, true);
    
    if (currentObject.getType()==null){
        response.sendRedirect("jsp404.jsp");   
    }
    
    try {
        currentObject.selectFromDB();
    } catch (SQLException|PropertyVetoException e) {
        e.printStackTrace();
    }
    
    
%>
<!DOCTYPE html>
<html ng-app="objectSettings">
<head>
    <c:catch var="e">
        <%@ include file="/includes/object/scripts/1.jspf" %>
    </c:catch>
    <c:if test="${!empty e}">
        <%@ include file="/includes/object/scripts/1.jspf" %>
    </c:if>

    <c:catch var="e">
        <c:import url="/includes/object/css/<%= currentObject.getType() %>.jspf" />
    </c:catch>
    <c:if test="${!empty e}">
        <c:import url="/includes/object/css/default.jspf" />
    </c:if>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title><%= currentObject.getType() %></title>
    <link rel="stylesheet" type="text/css" href="resources/css/bootstrap.min.css">
    <link rel="stylesheet" type="text/css" href="resources/css/angular-chart.css">
    <link rel="stylesheet" type="text/css" href="resources/css/template.css">
    <link rel="stylesheet" type="text/css" href="resources/css/params.css"> 
</head>
    <body>
        <div class="main">
                 <div id="header">
                     <ul class="menu">
                         <li><a class="a-outline button-style" href="#">Главная</a></li>
                         <li><a class="a-outline button-style" href="#">Контакты</a></li>
                         <li><a class="a-outline button-style" href="#">О нас</a></li>
                     </ul>
                     <div class="enter">
                         <a class="button-style button-style-enter a-outline" href="#">Войти/Регистрация</a>
                     </div>
                 </div>
                 <div class="content">
                     <ul class="custom-tabs nav nav-tabs tabs" id="tab_name">
                         <li class="active"><a href="#tab<%= currentObject.getAttributeGroups().get(0) %>" data-toggle="tab"><%= currentObject.getAttributeGroups().get(0) %></a></li>
                         <% for (int i = 1; i < currentObject.getAttributeGroups().size(); i++) { %>
                                 <li><a href="#tab<%= currentObject.getAttributeGroups().get(i) %>" data-toggle="tab"><%= currentObject.getAttributeGroups().get(i) %></a></li>
                         <% } %>
                         <%if ("1".equals(currentObject.getType())) {%>
                                 <li><a href="#statid" data-toggle="tab">Статистика</a></li>
                         <% } %>
                     </ul>
                     <div class="settings tab-content" id="tab_content">
                         <% String activeSwicth1 = "active"; %>
                         <% for (int i = 0; i < currentObject.getAttributeGroups().size(); i++) { %>
                         <div id="tab<%= currentObject.getAttributeGroups().get(i)  %>" class="tab-pane fade in <%= activeSwicth1 %>">
                             <div class="table-pos">
                                 <table class="table table-params">
                                         <%  for (int j = 0; j < currentObject.getParams().size(); j++) { %>
                                                 <% if (currentObject.getParams().get(j).getGroup().equals(currentObject.getAttributeGroups().get(i))) { %>
                                                 <tr>
                                                      <td><%= currentObject.getParams().get(j).getName() %></td>
                                                      <td><%= currentObject.getParams().get(j).getValue() %></td>
                                                 </tr>     
                                                 <% } %>
                                           <% } %>
                                 </table>
                             </div>
                         </div>
                         <% activeSwicth1 = ""; %>
                         <% } %>
                         <%if ("1".equals(currentObject.getType())) {%>
                            <div id="statid" class="tab-pane fade in">
                               <h3>Статистика просмотров объявлений:</h3>
                               <div id = "dropdown1" action="" class="tab-pane fade in" ng-controller="LineCtrl" ng-show="isExistData()">
                                   <div class="dropdown">
                                <button class="btn btn-default dropdown-toggle" type="button" id="dropdownMenu1" data-toggle="dropdown" aria-haspopup="true" aria-expanded="true">
                                    {{selectedItem.name}}
                                    <span class="caret"></span>
                                </button>
                                <ul class="dropdown-menu" aria-labelledby="dropdownMenu1">
                                    <li ng-repeat="a in subjects"><a ng-click="dropboxitemselected(a)">{{a.name}}</a></li>
                                </ul>
                                </div>
                               <canvas id="line" class="chart chart-line" chart-data="data"
                                 chart-labels="labels" chart-legend="true" chart-series="series"
                                 chart-click="onClick" height="40%" width="40%">
                               </canvas>
                               </div>
                               <div id="ifempty"></div>
                            </div>
                        <% } %>
                 </div> 
                 <div id="footer">
                     <div class="block-footer">Copyright &copy; 2015 &minus; UNC3-NC <br>
                         <img src="resources/img/netcracker-logo.png" alt="netcracker-logo">
                     </div>
                 </div>
             </div>     
    </body>
</html>
