<%-- 
    Document   : jsp404
    Created on : 06.03.2016, 20:46:04
    Author     : artem
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Ошибка</title>
    <script type="text/javascript" src="resources/scripts/angular.min.js"></script>
    <script src="resources/scripts/jquery-1.12.0.min.js"></script>
    <script src="resources/scripts/angular.min.js"></script>
    <script src="resources/scripts/bootstrap.min.js"></script>
    <script src="resources/scripts/chat.js"></script>
    <link rel="stylesheet" href="resources/css/chat.css">
    <link rel="stylesheet" type="text/css" href="resources/css/bootstrap.min.css">
    <link rel="stylesheet" type="text/css" href="resources/css/angular-chart.css">
    <link rel="stylesheet" type="text/css" href="resources/css/template.css">
    <link rel="stylesheet" type="text/css" href="resources/css/params.css">
    <link rel="stylesheet" href="resources/css/jsp404.css">
</head>
<body>
<div class="main">
    <div id="header">
        <ul class="menu">
            <li><a class="a-outline button-style" href="index.jsp">Главная</a></li>
        </ul>
    </div>
    <div class="content">
        <img src="resources/img/desk.png" alt="" class="desk">
        <c:if test="${param.message != null}">
            <div class="sticker">
                <div class="message">${param.message}</div>
                <img src="resources/img/sticker.png" alt="">
            </div>
        </c:if>
    </div>
    <c:catch var="e">
        <c:import url="/includes/object/footers/default.jspf" />
    </c:catch>
</div>
</body>
</html>