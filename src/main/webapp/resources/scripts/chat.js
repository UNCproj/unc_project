var chat = angular.module('chat',[]);

chat.controller('chatController', function($scope, $http){

    $http.defaults.headers.post['Content-Type'] = 'application/x-www-form-urlencoded;charset=UTF-8';

    $scope.lastmesid;
    $scope.recipientId;

    $scope.publish = function(){
        var mesText = $("#textMessage").val();

        console.log(mesText);
        if (mesText!=""){
            $("#textMessage").val("");
            $scope.start();
            $http.post("chatPublish", "message=" + mesText+"&recipientId="+$scope.recipientId);
        }
    };

    $scope.subscribe = function (id){
        $scope.lastmesid=id;
        $http.post("chatSubscribe","lastMessageId=" + $scope.lastmesid+"&recipientId="+$scope.recipientId).
        success(function(data, status) {
            if (status == "200"){
                for (var i = 0; i < data.length; i++) {
                    $scope.lastmesid = data[i].id;
                    $("table#message tbody").append("<tr></tr>");
                    $("table#message tbody tr:last").append('<td style="width: 50px">' + data[i].sender  +"</td>");
                    $("table#message tbody tr:last").append('<td style="width: 300px">' + data[i].text  +"</td>");
                    $("table#message tbody tr:last").append('<td style="width: 80px">' + data[i].date.substring(0,19) +"</td>");
                }
                $scope.subscribe($scope.lastmesid);
            }
            $("div.message").animate({ scrollTop: 999 }, 1100);
        }).
        error(function(data, status) {
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
});
function getUrlVars() {
    var vars = {};
    var parts = window.location.href.replace(/[?&]+([^=&]+)=([^&]*)/gi, function(m,key,value) {
        vars[key] = value;
    });
    return vars;
}


