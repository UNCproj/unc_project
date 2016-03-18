<%-- 
    Document   : unc_object
    Created on : 04.03.2016, 11:03:18
    Author     : Andrey
--%>
<%@page import="unc.helpers.Param"%>
<%@page import="java.util.ArrayList"%>
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
    
    ArrayList<String> listCategories = currentObject.selectCategory(currentObject.getType());
    
%>
<!DOCTYPE html>
<html ng-app="objectSettings">
<head>
    <c:catch var="e">
        <%@ include file="/includes/object/scripts/1.jspf" %>
    </c:catch>
    <c:if test="${!empty e}">
        <%@ include file="/includes/object/scripts/default.jspf" %>
    </c:if>

        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title><%= currentObject.getName() %></title>
    <c:catch var="e">
        <c:import url="/includes/object/css/<%= currentObject.getType() %>.jspf" />
    </c:catch>
    <c:if test="${!empty e}">
        <c:import url="/includes/object/css/default.jspf" />
    </c:if>
</head>
    <body>
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
                 <%if (!"1".equals(currentObject.getType())) {%>
                     <div class="list-categories clearfix">
                         <ul>
                             <% for (int i = 0; i < listCategories.size(); i++ ) { %>
                                <li class="list-categories-li">
                                    <a href="#"><%= listCategories.get(i) %></a>
                                </li>
                             <% } %>
                             <li class="list-categories-li"><%= currentObject.getName() %></li>
                         </ul>
                     </div>
                  <% } %>
                    <ul class="custom-tabs nav nav-tabs tabs" id="tab_name">
                    <% if (currentObject.getAttributeGroups() != null && currentObject.getAttributeGroups().size() != 0) {%>
                         <li class="active"><a href="#tab<%= currentObject.getAttributeGroups().get(0) %>" data-toggle="tab"><%= currentObject.getAttributeGroups().get(0) %></a></li>
                         <% for (int i = 1; i < currentObject.getAttributeGroups().size(); i++) { %>
                                 <li><a href="#tab<%= currentObject.getAttributeGroups().get(i) %>" data-toggle="tab"><%= currentObject.getAttributeGroups().get(i) %></a></li>
                         <% } %>
                         <%if ("1".equals(currentObject.getType())) {%>
                                 <li><a href="#statid" data-toggle="tab">Статистика</a></li>
                         <% } %>
                     <% } %>
                     </ul>
                     <div class="settings tab-content" id="tab_content">
                        <% String activeSwicth1 = "active", s; %>
                        <% for (int i = 0; i < currentObject.getAttributeGroups().size(); i++) {
                           String attrGroupName = currentObject.getAttributeGroups().get(i);
                           ArrayList<Param> currentGroupParams = currentObject.getParams(attrGroupName); %>
                        <div id="tab<%= currentObject.getAttributeGroups().get(i)  %>" class="tab-pane fade in <%= activeSwicth1 %>">
                            <div class="table-pos">
                                <table class="table table-params">
                                        <%  for (int j = 0; j < currentGroupParams.size(); j++) { %>
                                                <c:catch var="e">
                                                    <c:import url="/includes/object/attr_views/<%= currentObject.getType() %>.jsp" />
                                                </c:catch>
                                                <c:if test="${!empty e}">
                                                    <c:import url="/includes/object/attr_views/default.jsp">
                                                        <c:param name="attr_name" value="<%= currentGroupParams.get(j).getName()%>" />
                                                        <c:param name="attr_name_ru" value="<%= currentGroupParams.get(j).getRuName()%>" />
                                                        <c:param name="attr_value" value="<%= currentGroupParams.get(j).getValue()%>" />
                                                        <c:param name="attr_type" value="<%= currentGroupParams.get(j).getType()%>" />
                                                    </c:import>
                                                </c:if>       
                                          <% } %>
                                </table>
                            </div>
                        </div>
                        <% activeSwicth1 = ""; %>
                        <% } %>
                
                         <%if ("1".equals(currentObject.getType())) {%>
                            <div id="statid" class="tab-pane fade in">
                               <h3>Статистика просмотров объявлений:</h3>
                               <div ng-controller="LineCtrl">
                                    <div id = "dropdown1" action="" class="tab-pane fade in" >
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
                                      chart-click="onClick" height="20%" width="40%">
                                    </canvas>
                                    </div>
                               </div>
                               <div id="ifempty"></div>
                            </div>
                        <% } %>
                 </div>
                 <div class="references">
                    <h3>Список ссылок</h3>
                    <ul class="references-ul">
                        <% ArrayList<String> listReferences = currentObject.lisrReferences(); %>
                        <% for (int i = 0; i < listReferences.size(); i++) {
                                UncObject object = new UncObject(listReferences.get(i), null, true); 
                                try {
                                    object.selectFromDB();
                                } catch (SQLException|PropertyVetoException e) {
                                    e.printStackTrace();
                                }
                                //String attrGroupName = currentObject.getAttributeGroups().get(i);
                                //ArrayList<Param> currentGroupParams = currentObject.getParams(attrGroupName); %>
                        <li class="references-ul-li">
                            <a a href="unc_object.jsp?id=<%= listReferences.get(i) %>"><%= object.getName() %></a>
                        </li>

                        <% } %>
                    </ul>
                 </div>
             </div>
            <c:catch var="e">
                <c:import url="/includes/object/footers/default.jspf" />
            </c:catch>
        </div>
    </body>
</html>
