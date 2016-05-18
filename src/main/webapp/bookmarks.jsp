<%@ page import="beans.BeansHelper" %>
<%@ page import="beans.UserAccountBean" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: Денис
  Date: 18.01.2016
  Time: 12:18
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<html ng-app="bookmarks">
<head>
    <title>Bookmarks</title>
    <link rel="stylesheet" type="text/css" href="resources/css/bootstrap.min.css">
    <link rel="stylesheet" href="resources/css/bootstrap-theme.min.css" />
    <link rel="stylesheet" href="resources/css/template.css" />
    <link rel="stylesheet" href="resources/css/bookmarks.css" />
    <script src="resources/scripts/jquery-1.12.0.min.js"></script>
    <script src="resources/scripts/angular.min.js"></script>
    <script type="text/javascript" src="resources/scripts/bookmarks.js"></script>
</head>
<body>
    <div class="main">
        <div id="header">
            <ul class="menu">
                <li><a class="a-outline button-style" href="index.jsp">Главная</a></li>
                <li><a class="a-outline button-style" href="unc_add.jsp?type=advert">Новое объявление</a></li>
                <li><a class="a-outline button-style" href="forum.jsp">Форум</a></li>
                <li><a class="a-outline button-style" href="faq.jsp">FAQ</a></li>
                <li class="private-office">
                    <a class="button-style a-outline" href="bookmarks.jsp">
                        Закладки
                    </a>
                </li>
            </ul>
            <%
                UserAccountBean accountBean = (UserAccountBean)session.getAttribute(BeansHelper.USER_ACCOUNT_SESSION_KEY);
                if (accountBean != null && accountBean.isLoggedIn()) {
            %>
            <div class="enter-login clearfix">
                <ul class="enter-login-ul">
                    <li class="private-office">
                        <a class="private-office button-style a-outline" href="unc_object.jsp?id=${userAccount.getId()}">Личный кабинет</a>
                    </li>
                    <li>
                        <a class="button-style a-outline" href="logout">Выйти</a>
                    </li>
                </ul>
            </div>
            <%} else {%>

            <div class="enter">
                <a class="button-style button-style-enter a-outline" href="reg-and-login.jsp">Войти</a>
            </div>
            <%}%>
        </div>
        <div class="advertsWrapper col-md-8 col-md-offset-2" ng-controller="advertsController as advCtrl">
            <h2>Закладки</h2>
            <ul class="list-group" ng-if="adverts.length">
                <li class="list-group-item" ng-repeat="adv in adverts">
                    <a href="">
                        <div class="remove-button">
                            <span class="glyphicon glyphicon-remove" aria-hidden="true" ng-click="deleteBookmarks(adv.id)"></span>
                        </div>
                        <div ng-click="redirToAdvertPage(adv.id)">
                            <div class="img">
                                <img ng-src="{{adv.user_pic_file != undefined ? adv.user_pic_file : '${initParam.get("default.advert.image")}'}}">
                            </div>
                            <div class="main-content">
                                <div class="name">
                                    <h3>{{adv.name}}</h3>
                                </div>
                                <div class="description">
                                    {{adv.description}}
                                </div>
                                <div class="price">
                                    {{adv.price}} руб.
                                </div>
                            </div>
                        </div>
                    </a>
                </li>
            </ul>
            <div ng-if="!adverts.length">
                <h3>В избранном нет ни одного объявления</h3>
                Вы можете добавить объвление в избранное на странице объявления
            </div>
        </div>
    </div>
</body>
</html>
