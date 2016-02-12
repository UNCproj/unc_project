<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html ng-app="accountSettings">
<head>
    <title>unc_project | Личный кабинет</title>
    <link rel="stylesheet" type="text/css" href="resources/css/bootstrap.min.css">
    <link rel="stylesheet" type="text/css" href="resources/css/angular-chart.css" >
    <script type="text/javascript" src="resources/scripts/jquery-1.12.0.min.js"></script>
    <script type="text/javascript" src="resources/scripts/angular.min.js"></script>
    <script type="text/javascript" src="resources/scripts/bootstrap.min.js"></script>
    <script type="text/javascript" src="resources/scripts/ng-flow-standalone.min.js"></script>
    <script type="text/javascript" src="resources/scripts/accountSettings.js"></script>
    <script type="text/javascript" src="resources/scripts/Chart.js"></script>
    <script type="text/javascript" src="resources/scripts/angular-chart.js"></script>
    <script type="text/javascript" src="resources/scripts/d3.min.js"></script>
</head>
<body flow-prevent-drop>
<h2>Личный кабинет:</h2>

<ul class="nav nav-tabs">
    <li class="active"><a href="#main" data-toggle="tab">Основная информация</a></li>
    <li><a href="#about" data-toggle="tab">О себе</a></li>
    <li><a href="#statid" data-toggle="tab">Статистика</a></li>
</ul>

<div class="tab-content">
    <div id="main" class="tab-pane fade in active">
        <h3>Основная информация:</h3>
        <form action="" class="form-horizontal col-md-3 col-md-offset-3" flow-init ng-controller="mainSettingsController as mainSetCtrl">
            <div class="form-group">
                <img flow-img="$flow.files[0]">
                <br/>
                <label for="load-avatar">Загрузить аватар:</label>
                <div id="load-avatar" class="alert bg-primary" flow-drop>
                    Перетащите изображение сюда
                </div>
                <button type="button" flow-btn>Загрузить аватар</button>
            </div>
            <div class="form-group">
                <label for="login" class="control-label">Логин:</label>
                <div><input id="login" type="text" class="form-control" placeholder="${userAccount.getLogin()}" ng-model="mainSetCtrl.login"></div>
            </div>
            <div class="form-group">
                <label for="changePass" class="control-label">Изменить пароль:</label>
                <div><input id="changePass" type="password" class="form-control" ng-model="mainSetCtrl.changePass"></div>
            </div>
            <div class="form-group">
                <label for="email" class="control-label">Email:</label>
                <div><input id="email" type="text" class="form-control" placeholder="${userAccount.getEmail()}" ng-model="mainSetCtrl.email"></div>
            </div>
            <div class="form-group">
                <label for="pass" class="control-label">Введите пароль:</label>
                <div><input id="pass" type="text" class="form-control" ng-model="mainSetCtrl.pass"></div>
            </div>
            <input type="submit" ng-click="mainSetCtrl.submit()">
        </form>
    </div>
    <div id="about" class="tab-pane fade in">
        <h3>О себe:</h3>
    </div>
    <div id="statid" class="tab-pane fade in">
        <h3>Статистика просмотров объявлений:</h3>
        <div action="" class="tab-pane fade in" ng-controller="LineCtrl">
        <canvas id="line" class="chart chart-line" chart-data="data"
  chart-labels="labels" chart-legend="true" chart-series="series"
  chart-click="onClick" >
</canvas>
    </div>
    </div>
</div>
<script type="text/javascript">

</script>          
</body>
</html>
