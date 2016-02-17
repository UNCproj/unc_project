var chat = angular.module('chat',[]);

chat.controller('chatController', function($scope, $http){

    $http.defaults.headers.post['Content-Type'] = 'application/x-www-form-urlencoded;charset=UTF-8';

    $scope.lastmesid;

    $scope.publish = function(){
        var mesText = $("#textMessage").val();
        if (mesText!=""){
            $("#textMessage").val("");
            $http.post("chatPublish", "message=" + mesText);
        }
    };

    $scope.subscribe = function (id){
        $scope.lastmesid=id;
        console.log("subscribe with "+$scope.lastmesid);
        $http.post("chatSubscribe","lastMessageId=" + $scope.lastmesid).
        success(function(data, status) {
            if (status == "200"){
                console.log(data);
                for (var i = 0; i < data.length; i++) {
                    console.log("Message " + i + " " + data[i].text);
                    $scope.lastmesid = data[i].id;
                    $("div#messages").children("div#text").children().append('<p>' + data[i].text  +"<p>");
                    $("div#messages").children("div#date").children().append('<p>' + data[i].date.substring(0,19) +"<p>");
                }
                console.log("last id " + $scope.lastmesid);
                $scope.subscribe($scope.lastmesid);
            }
        }).
        error(function(data, status) {
            console.log("error : data - "+data+", status - "+status);
        });
    };

    $("document").ready(function(){
        $scope.subscribe($scope.lastmesid);
    });
});
