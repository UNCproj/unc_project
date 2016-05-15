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
    <script src="resources/scripts/angular-animate.min.js"></script>
    <script src="resources/scripts/ui-bootstrap-tpls-1.2.5.min.js"></script>
    <script src="resources/scripts/index.js"></script>
    <script src="resources/scripts/check-new-message.js"></script>
</head>
<body>
    <div class="main">
        <div id="header">
            <ul class="menu">
                <li><a class="a-outline button-style" href="index.jsp">Главная</a></li>
                <li><a class="a-outline button-style" href="unc_add.jsp?type=advert">Новое объявление</a></li>
                <li><a class="a-outline button-style" href="forum.jsp">Форум</a></li>
                <li><a class="a-outline button-style" href="faq.jsp">FQA</a></li>
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
                        <li class="private-office" id="lk">
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
        <div class="content">

            <%--Поиск--%>
            <div class="search-wrapper" ng-controller="searchController as searchCtrl">
                <div class="list-categories clearfix" ng-controller="navBarController as navBarCtrl">
                    <ul>
                        <li class="list-categories-li">
                            <a href="index.jsp">Главная</a>
                        </li>
                        <li class="list-categories-li">
                            <a ng-click="selectCategory(4, 'Все категории')">Все объявления</a>
                        </li>
                        <li class="list-categories-li" ng-repeat="enteredCat in enteredCategories">
                            <a ng-click="selectCategory(-1, enteredCat)">{{enteredCat}}</a>
                        </li>
                    </ul>
                </div>

                <%--Форма поиска--%>
                <form class="search-form" ng-controller="searchFormController as searchFormCtrl" novalidate>
                    <div class="input-group col-md-8 col-md-offset-2" ng-controller="typeaheadController">
                        <span class="input-group-addon">
                            <span class="glyphicon glyphicon-search" aria-hidden="true"></span>
                        </span>
                        <input type="text" id="search-phrase" class="form-control"
                               ng-model="$parent.$parent.searchPhrase"
                               placeholder="Поиск по объявлениям"
                               uib-typeahead="res for res in typeAhead($viewValue)"
                               typeahead-min-length="2"
                               typeahead-wait-ms="300"
                               autocomplete="off"/>
                        <div class="input-group-btn">
                            <button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown"
                                    aria-haspopup="true" aria-expanded="false">{{selectedCategory.name}} <span class="caret"></span>
                            </button>
                            <ul class="dropdown-menu dropdown-menu-right" ng-hide="selectedLevelCategories.length">
                                <li>Loading...</li>
                            </ul>
                            <ul class="dropdown-menu dropdown-menu-right" ng-if="selectedLevelCategories.length">
                                <li><a ng-click="selectCategory(4, 'Все категории')" href="">Все категории</a></li>
                                <li class="divider"></li>
                                <li ng-repeat="cat in selectedLevelCategories"><a ng-click="selectCategory(cat[0], cat[1])" href="">{{cat[1]}}</a></li>
                            </ul>

                            <button type="submit" class="btn btn-primary" ng-click="$parent.$parent.search()">
                                Найти
                            </button>
                        </div>
                    </div>

                    <div class="clearfix"></div>

                    <%--Категории поиска--%>
                    <div class="categories col-md-offset-1" ng-if="searchButtonClicked || defaultCategoryChanged"
                         ng-controller="categoriesController as catCtrl">
                        <table class="table">
                            <tr class="row" ng-if="enteredCategories.length">
                                <td class="col-md-3 back">
                                    <a class="sub-category" ng-click="enteredCategories.length > 1 ?
                                                                      selectCategory(-1, enteredCategories[enteredCategories.length - 2]) :
                                                                      selectCategory(4, 'Все категории')">
                                        <span class="glyphicon glyphicon-menu-left" aria-hidden="true"></span>
                                        Назад
                                    </a>
                                </td>
                            </tr>
                            <tr class="row" ng-repeat="row in getRowsNum() track by $index" ng-init="i = $index">
                                <td class="col-md-3" ng-repeat="col in getColumnsNum() track by $index" ng-init="j = $index">
                                    <a class="sub-category" ng-if="i * ATTR_COLS_NUM + j < selectedCategory.subCategories.length"
                                       ng-click="selectCategory(selectedCategory.subCategories[i * ATTR_COLS_NUM + j][0], selectedCategory.subCategories[i * ATTR_COLS_NUM + j][1])">
                                        {{selectedCategory.subCategories[i * ATTR_COLS_NUM + j][1]}}
                                    </a>
                                </td>
                            </tr>
                        </table>
                    </div>

                    <div class="clearfix"></div>

                    <%--Атрибуты категорий--%>
                    <div class="attributes col-md-8 col-md-offset-2" ng-if="searchButtonClicked || defaultCategoryChanged" ng-controller="attributesController as attrCtrl">
                        <div class="panel-group">
                            <div class="panel panel-default">
                                <div class="panel-heading">
                                    <h4 class="panel-title">
                                        <a data-toggle="collapse" href="#attrCollapse" ng-click="changeCollapsed()">
                                            <span ng-class="isCollapsed ? 'caret-right' : 'caret'"></span>
                                            Атрибуты
                                        </a>
                                    </h4>
                                </div>
                                <div id="attrCollapse" class="panel-collapse collapse">
                                    <div class="panel-body">
                                        <div class="subcategory-attributes" ng-repeat="attr in selectedCategory.attributes">
                                            <div attribute-view name="attr[0]" runame="attr[1]" type="attr[3]" num="attr[5]"
                                                  attrs="searchAttributes"></div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>

                    <button type="submit" class="btn btn-primary col-md-2 col-md-offset-5" ng-if="searchButtonClicked || defaultCategoryChanged" ng-click="$parent.search()">
                        Найти
                    </button>
                </form>

                <div class="clearfix"></div>
                <br/>

                <%--Вывод списка объявлений--%>
                <div class="adverts-list-wrapper col-md-7 col-md-offset-1"
                     ng-controller="searchResultsController as resultsCtrl">

                    <%--Результаты поиска--%>
                    <div class="pagination-wrapper" ng-if="pagesCount > 0">
                        <ul class="pagination">
                            <li ng-repeat="advPage in getPages() track by $index" ng-class="{active: $index == activePageNum}">
                                <a href="" ng-click="makePageActive($index)">{{$index + 1}}</a>
                            </li>
                        </ul>
                    </div>
                    <div class="ordering-wrapper" ng-if="pagesCount > 0">
                        <ul class="pagination sorting-order-wrapper">
                            <li ng-class="{active: searchResultsSortingOrder == 'asc'}" ng-click="changeSortingOrder('asc')">
                                <span class="glyphicon glyphicon-arrow-up"></span>
                            </li>
                            <li ng-class="{active: searchResultsSortingOrder == 'desc'}" ng-click="changeSortingOrder('desc')">
                                <span class="glyphicon glyphicon-arrow-down"></span>
                            </li>
                        </ul>

                        <div class="dropdown">
                            <button class="btn btn-default dropdown-toggle" type="button" data-toggle="dropdown">
                                {{displayedOrderingAttr}}
                                <span class="caret"></span>
                            </button>
                            <ul class="dropdown-menu">
                                <li ng-click="changeSortingParam('Название', 'name')"><a href="">Название</a></li>
                                <li ng-click="changeSortingParam('Цена', 'price')"><a href="">Цена</a></li>
                                <li class="divider" ng-if="selectedCategory.attributes.length"></li>
                                <li class="dropdown-submenu" ng-if="selectedCategory.attributes.length">
                                    <a tabindex="-1" href="">Другое</a>
                                    <ul class="dropdown-menu">
                                        <li ng-repeat="attr in selectedCategory.attributes"
                                            ng-if="attr[3] != null &&
                                                   attr[0] != 'price' &&
                                                   attr[0] != 'description' &&
                                                   attr[0] != 'user_pic_file' &&
                                                   attr[0] != 'vip_status'"
                                            ng-click="changeSortingParam(attr[1], attr[0])">
                                            <a href="">{{attr[1]}}</a>
                                        </li>
                                    </ul>
                                </li>
                            </ul>
                        </div>
                    </div>
                    <div class="search-results-wrapper">
                        <ul class="search-results list-group" ng-if="foundedAds.length">
                            <li class="list-group-item" ng-repeat="adv in foundedAds">
                                <a href="" ng-click="redirToAdvertPage(adv.id)">
                                    <div class="img">
                                        <img ng-src="{{adv.image != undefined ? adv.image : '${initParam.get("default.advert.image")}'}}">
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
                                </a>
                            </li>
                        </ul>
                        <img src="resources/img/ajax-loader.gif" class="loading-bar"
                             ng-hide="resultsLoaded" />
                        <br />
                        <div ng-if="pagesCount > 0">
                            <ul class="pagination">
                                <li ng-repeat="advPage in getPages() track by $index" ng-class="{active: $index == activePageNum}">
                                    <a href="" ng-click="makePageActive($index)">{{$index + 1}}</a>
                                </li>
                            </ul>
                        </div>
                    </div>
                </div>

                <%--VIP объявления--%>
                <div class="vip-adverts col-md-3 img-rounded" ng-controller="vipAdvertsController as vipAdvertsCtrl">
                    <h3 class="vip-adverts-header">VIP-объявления</h3>
                    <ul class="list-group">
                        <li class="list-group-item" ng-repeat="adv in vipAds">
                            <a href="" ng-click="redirToAdvertPage(adv.id)">
                                <div class="img">
                                    <img ng-src="{{adv.image != undefined ? adv.image : '${initParam.get("default.advert.image")}'}}">
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
                            </a>
                        </li>
                    </ul>
                </div>
            </div>

        </div>
        <div id="footer"></div>
    </div>
</body>
</html>
