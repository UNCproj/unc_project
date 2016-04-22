<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@page pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="beans.BeansHelper" %>
<%@ page import="beans.UserAccountBean" %>
<%request.setCharacterEncoding("UTF-8");%>

<!DOCTYPE html>

<html ng-app="chat" lang="ru">
<head>
    <title>Отправка сообщений</title>
    <script src="resources/scripts/jquery-1.12.0.min.js"></script>
    <script src="resources/scripts/angular.min.js"></script>
    <script src="resources/scripts/bootstrap.min.js"></script>
    <script src="resources/scripts/chat.js"></script>
    <link rel="stylesheet" href="resources/css/chat.css">
    <link rel="stylesheet" type="text/css" href="resources/css/bootstrap.min.css">
    <link rel="stylesheet" type="text/css" href="resources/css/angular-chart.css">
    <link rel="stylesheet" type="text/css" href="resources/css/template.css">
    <link rel="stylesheet" type="text/css" href="resources/css/params.css">
</head>
<body ng-controller="chatController">
<div class="main">
    <c:catch>
        <%@ include file="/includes/object/headers/default.jsp" %>
    </c:catch>
    <div class="content">
        <div style="height: 300px; overflow: scroll" class="message">
            <table class="table table-params" id="message">
                <tbody></tbody>
            </table>
        </div>
        <textarea id="textMessage" class="form-control" name="textMessage" cols="30" rows="10"></textarea>

        <div class="form-group">
            <input type="button" class="button-style a-outline button-update" value="Отправить сообщение" ng-click="publish()" />
        </div>
    </div>
    <c:catch var="e">
        <c:import url="/includes/object/footers/default.jspf" />
    </c:catch>
</div>

</body>
</html>
