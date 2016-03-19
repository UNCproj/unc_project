var chat = angular.module('chat',[]);

chat.controller('chatController', function($scope, $http){

    $http.defaults.headers.post['Content-Type'] = 'application/x-www-form-urlencoded;charset=UTF-8';

    $scope.lastmesid;
    $scope.sellerLogin;

    $scope.publish = function(){
        var mesText = $("#textMessage").val();
        if (mesText!=""){
            $("#textMessage").val("");
            $scope.start();
            $http.post("chatPublish", "message=" + mesText+"&sellerLogin="+$scope.sellerLogin);
        }
    };

    $scope.subscribe = function (id){
        $scope.lastmesid=id;
        $http.post("chatSubscribe","lastMessageId=" + $scope.lastmesid+"&sellerLogin="+$scope.sellerLogin).
        success(function(data, status) {
            if (status == "200"){
                for (var i = 0; i < data.length; i++) {
                    $scope.lastmesid = data[i].id;
                    $("div#messages").children("div#name").children().append('<p>' + data[i].sender  +"</p>");
                    $("div#messages").children("div#text").children().append('<p>' + data[i].text  +"</p>");
                    $("div#messages").children("div#date").children().append('<p>' + data[i].date.substring(0,19) +"</p>");
                }
                $scope.subscribe($scope.lastmesid);
            }
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
        $scope.sellerLogin=getUrlVars()["name"];
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


