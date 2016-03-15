<%@ page import="beans.BeansHelper" %>
<%@ page import="beans.UserAccountBean" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html ng-app="mainPage">
<head>
    <title>unc_project | Главная</title>
    <link rel="stylesheet" href="resources/css/bootstrap.min.css" />
    <link rel="stylesheet" href="resources/css/bootstrap-theme.min.css" />
    <link rel="stylesheet" href="resources/css/index.css" />
    <script src="resources/scripts/jquery-1.12.0.min.js"></script>
    <script src="resources/scripts/bootstrap.min.js"></script>
    <script src="resources/scripts/angular.min.js"></script>
    <script src="resources/scripts/index.js"></script>
</head>
<body>
    <header>
        <div class=".col-md-3">
            <img src="resources/img/netcracker-logo.png" alt="netcracker-logo">
        </div>
    </header>
    <aside class="accountSettings .col-md-3 col-md-offset-9">
        <%
            UserAccountBean accountBean = (UserAccountBean)session.getAttribute(BeansHelper.USER_ACCOUNT_SESSION_KEY);
            if (accountBean != null && accountBean.isLoggedIn()) {
        %>
            <a href="${pageContext.request.contextPath}/account?accountLogin=${userAccount.getLogin()}">Личный кабинет</a>
            <a href="${pageContext.request.contextPath}/logout">Выйти из профиля</a>
        <%} else {%>
            <a href="reg-and-login.jsp">Войти</a>
            <a href="reg-and-login.jsp">Зарегистрироваться</a>
        <%}%>
    </aside>
    <div class="search-wrapper col-md-6 col-md-offset-3" ng-controller="searchController as searchCtrl">
        <form class="search-form" novalidate>
            <div class="input-group">
                <span class="input-group-addon">
                    <span class="glyphicon glyphicon-search" aria-hidden="true"></span>
                </span>
                <input type="text" id="search-phrase" class="form-control" placeholder="Поиск по объявлениям" ng-model="searchPhrase"/>
                <div class="input-group-btn">
                    <button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown"
                            aria-haspopup="true" aria-expanded="false">{{selectedCategoryName}} <span class="caret"></span>
                    </button>
                    <ul class="dropdown-menu dropdown-menu-right" ng-hide="categories.length">
                        <li>Loading...</li>
                    </ul>
                    <ul class="dropdown-menu dropdown-menu-right" ng-if="categories.length">
                        <li><a ng-click="selectCategory(4, 'Все категории')" href="#">Все категории</a></li>
                        <li class="divider"></li>
                        <li ng-repeat="cat in categories"><a ng-click="selectCategory(cat[0], cat[1])" href="#">{{cat[1]}}</a></li>
                    </ul>

                    <button type="submit" class="btn btn-primary" ng-click="search()">
                        Найти
                    </button>
                </div>
            </div>
            <div class="input-group">
                <label ng-click="setAdvanced()">
                    <span ng-class="isAdvanced ? 'caret' : 'caret-right'"></span>
                    Расширенный поиск
                </label>
            </div>
            <div class="input-group" ng-show="isAdvanced">
                <div class="sub-category" ng-repeat="">

                </div>
            </div>
        </form>
        <img src="resources/img/ajax-loader.gif" class="loading-bar col-md-2 col-md-offset-5" ng-hide="resultsLoaded" />
        <br />
        <div class="search-results-wrapper">
            <ul class="search-results list-group" ng-if="foundedAds.length">
                <li class="list-group-item" ng-repeat="adv in foundedAds">
                    <a href="#" ng-click="redirToAdvertPage(adv.id)">
                        <div id="name">
                            <h3>{{adv.name}}</h3>
                        </div>
                        <div id="descriprtion">
                            <h4>Описание</h4>
                            {{adv.descriprtion}}
                        </div>
                    </a>
                </li>
            </ul>
            <br />
            <div ng-if="pagesCount > 0"></div>
                <ul class="pagination">
                    <li ng-repeat="advPage in getPages() track by $index" ng-class="{active: $index == activePageNum}">
                        <a href="#" ng-click="makePageActive($index)">{{$index + 1}}</a>
                    </li>
                </ul>
        </div>
    </div>
    <br />
    <div class="lastAdded col-md-4 col-md-offset-4" ng-controller="lastAddedController as laCtrl">
        <h2>Последние объявления</h2>
        <img src="resources/img/ajax-loader.gif" class="loading-bar col-md-2 col-md-offset-5" ng-hide="loaded" />
        <br />
        <div ng-repeat="adv in lastAdverts">
            <div>{{adv.name}}</div>
        </div>
    </div>
</body>
</html>
