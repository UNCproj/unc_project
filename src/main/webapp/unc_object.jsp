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

    try {
        currentObject.selectFromDB();
    } catch (SQLException|PropertyVetoException e) {
        e.printStackTrace();
    }
%>
<!DOCTYPE html>
<html ng-app="accountSettings">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title><%= currentObject.getType() %></title>
    <link rel="stylesheet" type="text/css" href="resources/css/bootstrap.min.css">
    <link rel="stylesheet" type="text/css" href="resources/css/angular-chart.css">
    <link rel="stylesheet" type="text/css" href="resources/css/template.css">
    <link rel="stylesheet" type="text/css" href="resources/css/params.css">
    <script type="text/javascript" src="resources/scripts/jquery-1.12.0.min.js"></script>
    <script type="text/javascript" src="resources/scripts/angular.min.js"></script>
    <script type="text/javascript" src="resources/scripts/bootstrap.min.js"></script>
    <script type="text/javascript" src="resources/scripts/ng-flow-standalone.min.js"></script>
    <script type="text/javascript" src="resources/scripts/accountSettings.js"></script>
    <script type="text/javascript" src="resources/scripts/Chart.js"></script>
    <script type="text/javascript" src="resources/scripts/angular-chart.js"></script>
    <script type="text/javascript" src="resources/scripts/d3.min.js"></script>
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
                <ul class="custom-tabs nav nav-tabs tabs">
                    <li class="active"><a href="#tab<%= currentObject.getAttributeGroups().get(0) %>" data-toggle="tab"><%= currentObject.getAttributeGroups().get(0) %></a></li>
                    <% for (int i = 1; i < currentObject.getAttributeGroups().size(); i++) { %>
                            <li><a href="#tab<%= currentObject.getAttributeGroups().get(i) %>" data-toggle="tab"><%= currentObject.getAttributeGroups().get(i) %></a></li>
                    <% } %>
                </ul>
                <div class="settings tab-content">
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
            </div> 
            <div id="footer">
                <div class="block-footer">Copyright &copy; 2015 &minus; UNC3-NC <br>
                    <img src="resources/img/netcracker-logo.png" alt="netcracker-logo">
                </div>
            </div>
        </div>
    </body>
</html>
