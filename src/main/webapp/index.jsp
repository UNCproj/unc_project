<%@ page import="beans.BeansHelper" %>
<%@ page import="beans.UserAccountBean" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html ng-app="mainPage">
<head>
    <title>unc_project | Главная</title>
    <link rel="stylesheet" href="resources/css/bootstrap.min.css" />
    <link rel="stylesheet" href="resources/css/bootstrap-theme.min.css" />
    <link rel="stylesheet" href="resources/css/index.css" />
    <link rel="stylesheet" href="resources/css/template.css" />
    <script src="resources/scripts/jquery-1.12.0.min.js"></script>
    <script src="resources/scripts/bootstrap.min.js"></script>
    <script src="resources/scripts/angular.min.js"></script>
    <script src="resources/scripts/index.js"></script>
</head>
<body>
    <div class="main">
        <div id="header">
            <ul class="menu">
                <li><a class="a-outline button-style" href="index.jsp">Главная</a></li>
                <li><a class="a-outline button-style" href="unc_add.jsp?type=advert" style="width: 110%">Новое объявление</a></li>
            </ul>
            <%
                UserAccountBean accountBean = (UserAccountBean)session.getAttribute(BeansHelper.USER_ACCOUNT_SESSION_KEY);
                if (accountBean != null && accountBean.isLoggedIn()) {
            %>
                <div class="enter-login clearfix">
                    <ul class="enter-login-ul">
                        <li class="private-office">
                            <a class="private-office button-style a-outline" href="${pageContext.request.contextPath}/unc_object.jsp?id=${userAccount.getId()}">Личный кабинет</a>
                        </li>
                        <li>
                            <a class="button-style a-outline" href="${pageContext.request.contextPath}/logout">Выйти</a>
                        </li>
                    </ul>
                </div>
            <%} else {%>

                <div class="enter">
                    <a class="button-style button-style-enter a-outline" href="reg-and-login.jsp">Войти</a>
                </div>
            <%}%>
        </div>
        <div class="list-categories clearfix" ng-controller="categoriesController as catCtrl">
            <ul>
                <li class="list-categories-li">
                    <a href="#">Главная</a>
                </li>
                <li class="list-categories-li" ng-repeat="enteredCat in enteredCategories">
                    <a href="#">{{enteredCat}}</a>
                </li>
            </ul>
        </div>
        <div class="content">

            <%--Поиск--%>
            <div class="search-wrapper col-md-6 col-md-offset-3" ng-controller="searchController as searchCtrl">
                <%--Форма поиска--%>
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

                    <%--Расширенный поиск--%>
                    <div class="input-group">
                        <label ng-click="setAdvanced()">
                            <span ng-class="isAdvanced ? 'caret' : 'caret-right'"></span>
                            Расширенный поиск
                        </label>
                    </div>
                    <div class="input-group" ng-show="isAdvanced">
                        <div class="sub-category" ng-repeat="subCat in subCategories">
                            <%--<h3 class="sub-category-title">{{subCat.name}}</h3>--%>
                            <div class="subcategory-attributes" ng-repeat="attr in subCat.attributes">
                                <attribute-view name="attr[1]" type="attr[2]"></attribute-view>
                            </div>

                            <button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown"
                                    aria-haspopup="true" aria-expanded="false">SomeCat <span class="caret"></span>
                            </button>
                            <ul class="dropdown-menu">
                                <li ng-repeat="subSubCat in subCat.subCategories">
                                    <a ng-click="selectSubCategoryInAdvancedSearch(cat[0], cat[1])" href="#">{{subSubCat[1]}}</a>
                                </li>
                            </ul>
                        </div>
                    </div>
                </form>

                <%--Результаты поиска--%>
                <img src="resources/img/ajax-loader.gif" class="loading-bar col-md-2 col-md-offset-5" ng-hide="resultsLoaded" />
                <br />
                <div class="search-results-wrapper">
                    <ul class="search-results list-group" ng-if="foundedAds.length">
                        <li class="list-group-item" ng-repeat="adv in foundedAds">
                            <a href="#" ng-click="redirToAdvertPage(adv.id)">
                                <div class="name">
                                    <h3>{{adv.name}}</h3>
                                </div>
                                <div class="description">
                                    {{adv.description}}
                                </div>
                            </a>
                        </li>
                    </ul>
                    <br />
                    <div ng-if="pagesCount > 0"></div>
                        <ul class="pagination">
                            <li ng-repeat="advPage in getPages() track by $index" ng-class="{active: $index == activePageNum}">
                                <a href="" ng-click="makePageActive($index)">{{$index + 1}}</a>
                            </li>
                        </ul>
                </div>
            </div>

            <%--VIP объявления--%>
            <div ng-controller="vipAdvertsController">
                <div class="col-md-3" id="vip-adverts">
                    <p>Лучшие объявления</p>

                </div>
            </div>

            <%--Последние объявления--%>
            <div class="lastAdded col-md-4 col-md-offset-4" ng-controller="lastAddedController as laCtrl">
                <h2>Последние объявления</h2>
                <img src="resources/img/ajax-loader.gif" class="loading-bar col-md-2 col-md-offset-5" ng-hide="loaded" />
                <br />
                <div ng-repeat="adv in lastAdverts">
                    <div>{{adv.name}}</div>
                </div>
            </div>

            <%--Категории объявлений--%>
            <div class="categories col-md-4 col-md-offset-4" ng-controller="categoriesController as catCtrl">
                <h2>Объявления по категориям</h2>
                <span class="glyphicon glyphicon-chevron-left"
                      ng-click="loadCategory(enteredCategories[enteredCategories.length - 2] == 'Все объявления' ? 4 :-1,
                                             enteredCategories[enteredCategories.length - 2])"></span>
                <h3>{{categoryToShow.name}}</h3>
                <div class="sub-category" ng-repeat="subCat in categoryToShow.subCategories"
                     ng-click="loadCategory(subCat[0], subCat[1])">
                    <a href="">{{subCat[1]}}</a>
                </div>
                <div class="advert-in-category" ng-repeat="adv in categoryToShow.adverts">
                    {{adv.name}}
                </div>
            </div>
        </div>
        <div id="footer"></div>
    </div>
</body>
</html>
