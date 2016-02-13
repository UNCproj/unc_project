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
<script>
    var initialUserLogin = '${userAccount.getLogin()}';
</script>

<h2>Личный кабинет:</h2>

<ul class="nav nav-tabs">
    <li class="active"><a href="#main" data-toggle="tab">Основная информация</a></li>
    <li><a href="#about" data-toggle="tab">О себе</a></li>
    <li><a href="#statid" data-toggle="tab">Статистика</a></li>
</ul>

<div class="settings tab-content" ng-controller="mainSettingsController">
    <div id="main" class="tab-pane fade in active"
         flow-init="{target: '/unc-project/upload', testChunks:false}"
         flow-file-added="fileAdded($file, $event, $flow)"
         flow-name="uploader.flow">
        <h3>Основная информация:</h3>
        <form class="form-horizontal col-md-3 col-md-offset-3" novalidate>

            <!--Обновление аватара-->
            <div class="form-group">
                <img src="${userAccount.getUserPicFile()}" flow-img="uploader.flow.files[0]">
                <br/>
                <label for="load-avatar">Загрузить аватар:</label>
                <div id="load-avatar" class="alert bg-primary" flow-drop>
                    Перетащите изображение сюда
                </div>
                <button type="button" flow-btn>Загрузить аватар</button>
            </div>

            <!--Обновление логина-->
            <div class="form-group">
                <label for="login_elem" class="control-label">Логин:</label>
                <div><input id="login_elem" type="text" class="form-control" placeholder="${userAccount.getLogin()}" ng-model="user.login"></div>
            </div>

            <!--Обновление пароля-->
            <div class="form-group">
                <label for="changePass_elem" class="control-label">Изменить пароль:</label>
                <div><input id="changePass_elem" type="password" class="form-control" ng-model="user.changePass"></div>
            </div>

            <!--Обновление email-->
            <div class="form-group">
                <label for="email_elem" class="control-label">Email:</label>
                <div><input id="email_elem" type="email" class="form-control" placeholder="${userAccount.getEmail()}" ng-model="user.email"></div>
            </div>

            <!--Валидация пароля-->
            <div class="form-group">
                <label for="pass_elem" class="control-label">Введите пароль:</label>
                <div><input id="pass_elem" type="password" class="form-control" ng-model="user.pass" required></div>
            </div>

            <input type="submit" ng-click="submit('main')">
        </form>
    </div>
    <div id="about" class="tab-pane fade in">
        <h3>О себе:</h3>
        <form class="form-horizontal col-md-3 col-md-offset-3" novalidate>

            <!--Обновление имени-->
            <div class="form-group">
                <label for="firstName_elem" class="control-label">Имя:</label>
                <div><input id="firstName_elem" type="text" class="form-control" placeholder="${userAccount.getFirstName()}" ng-model="user.firstName"></div>
            </div>

            <!--Обновление отчества-->
            <div class="form-group">
                <label for="secondName_elem" class="control-label">Отчество:</label>
                <div><input id="secondName_elem" type="text" class="form-control" placeholder="${userAccount.getSecondName()}" ng-model="user.secondName"></div>
            </div>

            <!--Обновление фамилии-->
            <div class="form-group">
                <label for="surname_elem" class="control-label">Фамилия:</label>
                <div><input id="surname_elem" type="text" class="form-control" placeholder="${userAccount.getSurname()}" ng-model="user.surname"></div>
            </div>

            <!--Обновление телефона-->
            <div class="form-group">
                <label for="phone_elem" class="control-label">Телефон:</label>
                <div><input id="phone_elem" type="text" class="form-control" placeholder="${userAccount.getPhone()}" ng-model="user.phone"></div>
            </div>

            <!--Обновление улицы и дома-->
            <div class="form-group">
                <label for="streetAndHouse_elem" class="control-label">Улица и дом::</label>
                <div><input id="streetAndHouse_elem" type="text" class="form-control" placeholder="${userAccount.getStreetAndHouse()}" ng-model="user.streetAndHouse"></div>
            </div>

            <!--Обновление города-->
            <div class="form-group">
                <label for="city_elem" class="control-label">Город:</label>
                <div><input id="city_elem" type="text" class="form-control" placeholder="${userAccount.getCity()}" ng-model="user.city"></div>
            </div>

            <!--Обновление страны-->
            <div class="form-group">
                <label for="country_elem" class="control-label">Страна:</label>
                <div><input id="country_elem" type="text" class="form-control" placeholder="${userAccount.getCountry()}" ng-model="user.country"></div>
            </div>

            <!--Обновление доп. информации-->
            <div class="form-group">
                <label for="additionalInfo_elem" class="control-label">О себе:</label>
                <div><textarea id="additionalInfo_elem" type="text" class="form-control"
                               placeholder="${userAccount.getAdditionalInfo()}" ng-model="user.additionalInfo"></textarea>
                </div>
            </div>

            <!--Валидация пароля-->
            <div class="form-group">
                <label for="pass_elem1" class="control-label">Введите пароль:</label>
                <div><input id="pass_elem1" type="password" class="form-control" ng-model="user.pass" required></div>
            </div>
            <input type="submit" ng-click="submit('about')">
        </form>
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
</body>
</html>
