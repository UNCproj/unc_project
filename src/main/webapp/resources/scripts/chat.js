var chat = angular.module('chat',[]);

chat.controller('chatController', ['$scope', '$http',
    function($scope, $http){

    //$http.defaults.headers.post['Content-Type'] = 'application/x-www-form-urlencoded;charset=UTF-8';

    $scope.lastmesid;
    $scope.recipientId;

    $scope.publish = function(){
        //var mesText = $("#textMessage").val();

        //console.log(mesText);
        $scope.object = {};
        $scope.object.message = $("#textMessage").val();
        if ($scope.object.message!=""){
            $("#textMessage").val("");
            $scope.start();
            $scope.object.recipientId = $scope.recipientId;
            console.log("Message - " + $scope.object.message + ", id = " + $scope.object.recipientId);
            $http({
                url: 'chatPublish',
                method: 'POST',
                params: $scope.object
            }).success(function (data) {});
        }
    };

    $scope.subscribe = function (id){
        //$scope.lastmesid=id;
        $scope.object = {};
        $scope.object.lastMessageId = id;
        $scope.object.recipientId = $scope.recipientId;

        $http({
            url: 'chatSubscribe',
            method: 'POST',
            params: $scope.object
        }).success(function(data) {
                for (var i = 0; i < data.length; i++) {
                    $scope.lastmesid = data[i].id;
                    $("table#message tbody").append("<tr></tr>");
                    $("table#message tbody tr:last").append('<td style="width: 50px">' + data[i].sender  +"</td>");
                    $("table#message tbody tr:last").append('<td style="width: 300px">' + data[i].text  +"</td>");
                    $("table#message tbody tr:last").append('<td style="width: 80px">' + data[i].date.substring(0,19) +"</td>");
                }
                $scope.subscribe($scope.lastmesid);
            $("div.message").animate({ scrollTop: 999 }, 1100);
        });
    };

    $scope.getUrlVars = function (){
        var vars = {};
        var parts = window.location.href.replace(/[?&]+([^=&]+)=([^&]*)/gi, function(m,key,value) {
                vars[key] = value;
            }
        );
        return vars;
    };

    $scope.start = function(){
        $scope.recipientId=getUrlVars()["id"];
    };

    $("document").ready(function(){
        $scope.start();
        $scope.subscribe($scope.lastmesid);
    });
}]);
function getUrlVars() {
    var vars = {};
    var parts = window.location.href.replace(/[?&]+([^=&]+)=([^&]*)/gi, function(m,key,value) {
        vars[key] = value;
    });
    return vars;
}


