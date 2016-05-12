var chat = angular.module('chat',[]);

chat.controller('chatController', ['$scope', '$http',
    function($scope, $http){

    //$http.defaults.headers.post['Content-Type'] = 'application/x-www-form-urlencoded;charset=UTF-8';

    $scope.lastmesid;
    $scope.recipientId;

    $scope.publish = function(){
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
                    $('.message-div').append('<div class="message-box"></div>');
                    console.log(data[i].recipientId+'='+$scope.recipientId);
                    if(data[i].recipientId == $scope.recipientId){
                        console.log('111');
                        $('.message-div .message-box:last').append('<div class="message-right"><div class="mess-text-box">' +
                            '<div class="mess-name">' + data[i].sender + '</div>' +
                            '<div class="mess-text">'+ data[i].text + '</div>' +
                            '<div class="mess-date">'+ data[i].date + '</div>' +
                            '</div></div>');
                        var width = $('.message-box:last').width()-$('.message-box:last .message-right:last .mess-text-box:last').width()-20;
                        //width = '"'+width+'px"';
                        console.log(width);
                        $('div.message-right:last').css("margin-left", width );
                        //$('div.message-right:last').css("margin-left", '"' + width + 'px"');
                    }else{
                        $('.message-div .message-box:last').append('<div class="message-left"><div class="mess-text-box">' +
                            '<div class="mess-name">' + data[i].sender + '</div>' +
                            '<div class="mess-text">'+ data[i].text + '</div>' +
                            '<div class="mess-date">'+ data[i].date + '</div>' +
                            '</div></div>');
                    }
                    $("div.message").animate({ scrollTop: 5000 }, 5);
                }
                $scope.subscribe($scope.lastmesid);
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


