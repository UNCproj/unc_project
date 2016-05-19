var chat = angular.module('chat', []);

chat.controller('chatController', ['$scope', '$http',
    function ($scope, $http) {

        //$http.defaults.headers.post['Content-Type'] = 'application/x-www-form-urlencoded;charset=UTF-8';

        $scope.lastmesid;
        $scope.recipientId;
        $scope.firstMessageId = '';

        $scope.publish = function () {
            $scope.object = {};
            $scope.object.message = $("#textMessage").val();
            if ($scope.object.message != "") {
                $("#textMessage").val("");
                $scope.start();
                $scope.object.recipientId = $scope.recipientId;
                console.log("Message - " + $scope.object.message + ", id = " + $scope.object.recipientId);
                $http({
                    url: 'chatPublish',
                    method: 'POST',
                    params: $scope.object
                }).success(function (data) {
                });
            }
        };

        var loadPreviousMessages = function(firstId){
            $('.load-previous-messages').remove();
            $scope.o = {};
            $scope.o.messId = firstId;
            $scope.o.recId = $scope.recipientId;
            console.log($scope.o.messId + ", " + $scope.o.recId);
            $http({
                url: 'loadPreMess',
                method: 'POST',
                params: $scope.o
            }).success(function (data) {
                console.log(data);

                $scope.firstMessageId=data[0].id;
                for (var i = 0; i < data.length - 1; i++) {
                    var name;
                    if( data[i].recipientName!=undefined && data[i].recipientSurname==undefined){
                        name = data[i].recipientName + '(' + data[i].recipientLogin + ')';
                    } else if(data[i].recipientName==undefined && data[i].recipientSurname!=undefined){
                        name = data[i].recipientSurname + '(' + data[i].recipientLogin + ')';
                    }else if (data[i].recipientName!=undefined && data[i].recipientSurname!=undefined){
                        name = data[i].recipientName + ' ' + data[i].recipientSurname + '(' + data[i].recipientLogin + ')';
                    }else{
                        name =data[i].recipientLogin ;
                    }
                    $('.message-div').prepend('<div class="message-box"></div>');
                    if (data[i].recipientId == $scope.recipientId) {
                        $('.message-div .message-box:first').append('<div class="message-right"><div class="mess-text-box">' +
                            '<div class="mess-name">'+ name + '</div>' +
                            '<div class="mess-text">' + data[i].text + '</div>' +
                            '<div class="mess-date">' + data[i].date + '</div>' +
                            '</div></div>');
                        var width = $('.message-box:first').width() - $('.message-box:last .message-right:last .mess-text-box:last').width() - 20;
                        console.log(width);
                        $('div.message-right:first').css("margin-left", width);
                    } else {
                        $('.message-div .message-box:first').append('<div class="message-left"><div class="mess-text-box">' +
                            '<div class="mess-name">'+ name + '</div>' +
                            '<div class="mess-text">' + data[i].text + '</div>' +
                            '<div class="mess-date">' + data[i].date + '</div>' +
                            '</div></div>');
                    }

                }
                if (data.length==10){
                    $('div.message-div').prepend('<div class="load-previous-messages">Загрузить предыдущие сообщения</div>');
                    $('div.message-div div.load-previous-messages').on('click',function(){
                        loadPreviousMessages(data[data.length - 1].messId);
                    });
                }
            });
        };

        $scope.subscribe = function (id) {
            $scope.object = {};
            $scope.object.lastMessageId = id;
            $scope.object.recipientId = $scope.recipientId;

            $http({
                url: 'chatSubscribe',
                method: 'POST',
                params: $scope.object
            }).success(function (data) {
                console.log(data);
                for (var i = data.length - 1; i >= 0; i--) {
                    var name;
                    if( data[i].name!=undefined && data[i].surname==undefined){
                        name = data[i].name + '(' + data[i].sender + ')';
                    } else if(data[i].name==undefined && data[i].surname!=undefined){
                        name = data[i].surname + '(' + data[i].sender + ')';
                    }else if (data[i].name!=undefined && data[i].surname!=undefined){
                        name = data[i].name + ' ' + data[i].surname + '(' + data[i].sender + ')';
                    }else{
                        name =data[i].sender ;
                    }
                    if ($scope.firstMessageId==""){
                        $scope.firstMessageId=data[data.length-1].id;
                    }
                    $scope.lastmesid=data[0].id;
                    $('.message-div').append('<div class="message-box"></div>');
                    if (data[i].recipientId == $scope.recipientId) {
                        $('.message-div .message-box:last').append('<div class="message-right"><div class="mess-text-box">' +
                            '<div class="mess-name">' + name + '</div>' +
                            '<div class="mess-text">' + data[i].text + '</div>' +
                            '<div class="mess-date">' + data[i].date + '</div>' +
                            '</div></div>');
                        var width = $('.message-box:last').width() - $('.message-box:last .message-right:last .mess-text-box:last').width() - 20;
                        console.log(width);
                        $('div.message-right:last').css("margin-left", width);
                    } else {
                        $('.message-div .message-box:last').append('<div class="message-left"><div class="mess-text-box">' +
                            '<div class="mess-name">' + name + '</div>' +
                            '<div class="mess-text">' + data[i].text + '</div>' +
                            '<div class="mess-date">' + data[i].date + '</div>' +
                            '</div></div>');
                    }
                    $("div.message").animate({scrollTop: 5000}, 5);

                }
                if (data.length==10){
                    $('div.message-div').prepend('<div class="load-previous-messages">Загрузить предыдущие сообщения</div>');
                    $('div.message-div div.load-previous-messages').on('click',function(){
                        loadPreviousMessages($scope.firstMessageId);
                    });
                }
                $scope.subscribe($scope.lastmesid);

            });
        };

        $scope.getUrlVars = function () {
            var vars = {};
            var parts = window.location.href.replace(/[?&]+([^=&]+)=([^&]*)/gi, function (m, key, value) {
                    vars[key] = value;
                }
            );
            return vars;
        };

        $scope.start = function () {
            $scope.recipientId = getUrlVars()["id"];
        };

        $("document").ready(function () {
            $scope.start();
            $scope.subscribe($scope.lastmesid);
        });
    }]);
function getUrlVars() {
    var vars = {};
    var parts = window.location.href.replace(/[?&]+([^=&]+)=([^&]*)/gi, function (m, key, value) {
        vars[key] = value;
    });
    return vars;
}


