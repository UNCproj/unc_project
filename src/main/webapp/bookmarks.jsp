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
    <script type="text/javascript" src="resources/scripts/angular.min.js"></script>
    <script type="text/javascript" src="resources/scripts/bookmarks.js"></script>
</head>
<body>
    <div class="advertsWrapper" ng-controller="advertsController as advCtrl">
        <div class="advertsList" ng-repeat="adv in adverts">
            <div class="advert">
                <div class="advertName">{{adv.name}}</div>
                <div class="advertDescription">{{adv.description}}</div>
            </div>
        </div>
    </div>
</body>
</html>
