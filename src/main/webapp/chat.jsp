<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!DOCTYPE html>

<html ng-app="chat" lang="ru">
<head>
    <title>Title</title>
    <script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.8/jquery.min.js"></script>
    <script type="text/javascript" src="resources/scripts/angular.min.js"></script>
    <link rel="stylesheet" type="text/css" href="resources/css/bootstrap.min.css">
    <script src="resources/scripts/chat.js"></script>
    <link rel="stylesheet" href="resources/css/chat.css">
</head>
<body>
    <div ng-controller="chatController">

            <div class="col-sm-offset-2 col-sm-8 messages" id="messages">
                <div class="col-sm-3" id="name"><p></p></div>
                <div class="col-sm-6" id="text"><p></p></div>
                <div class="col-sm-3" id="date"><p></p></div>
                <p></p>
            </div>

            <div class=" col-sm-offset-2 col-sm-8 col-sm-offset-2">
                <textarea id="textMessage" class="form-control" name="textMessage" cols="30" rows="10"></textarea>
            </div>

        <div class="form-group">
            <div class="col-sm-offset-2 col-sm-10">
                <button type="submit" class="btn btn-default" ng-click="publish()">Отправить сообщение</button>
            </div>
        </div>

    </div>

</body>
</html>
