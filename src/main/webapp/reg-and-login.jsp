<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html ng-app="regAndLogin">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>unc_project | Register</title>
    <link rel="stylesheet" type="text/css" href="resources/css/bootstrap.min.css">
    <link rel="stylesheet" type="text/css" href="resources/css/common.css">

    <script type="text/javascript" src="resources/scripts/jquery-1.12.0.min.js"></script>
    <script type="text/javascript" src="resources/scripts/angular.min.js"></script>
    <script type="text/javascript" src="resources/scripts/directives/login.js"></script>
    <script type="text/javascript" src="resources/scripts/directives/registration.js"></script>
    <script type="text/javascript" src="resources/scripts/reg-and-login.js"></script>
    <script type="text/javascript" src="resources/scripts/common.js"></script>
</head>
<body>
<div class="container" ng-controller="ralController as ralCtrl">
    <div class="col-md-3 col-md-offset-3">
        <div class="logon">
            <login-form ng-show="ralCtrl.isRegistred"></login-form>
            <registration-form ng-hide="ralCtrl.isRegistred"></registration-form>
        </div>
    </div>
</div>
<div class=".col-md-3 .col-md-offset-3 footlog">
    <p class="text-center small">Copyright &copy; 2015 &minus; UNC3-NC <br>
        <img src="resources/img/netcracker-logo.png" alt="netcracker-logo">
    </p>
</div>
</body>
</html>