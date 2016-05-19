(function() {
    var app = angular.module('login', []);

    $.urlParam = function(name){
        var results = new RegExp('[\?&]' + name + '=([^&#]*)').exec(window.location.href);

        if (!results) {
            return 0;
        }

        return results[1] || 0;
    };

    app.controller('loginController', ['$http', '$timeout', function($http, $timeout) {
        this.login;
        this.pass;
        this.isLogInFailed = false;
        this.isLogging = false;
        var context = this;

        this.submit = function() {
            login($http, this, function (data) {
                if (data["logged"]) {
                    var from = $.urlParam('from');

                    if (from) {
                        window.location = from;
                    }
                    else {
                        window.location = "/unc-project/index";
                    }
                }
                else {
                    context.isLogInFailed = true;
                    context.cause = data["cause"];
                }
            });
        };
        
        this.vk = function() {
           vk_log($http, this, function (data) {
                if (data["logged"] == "true") {
                    var from = $.urlParam('from');

                    if (data["migrated"]) {
                        window.location = "/unc-project/unc_update.jsp?id=" + data["id"];
                    }
                    else if (from) {
                        window.location = "/" + from;
                    }
                    else {
                        window.location = "/unc-project/index";
                    }
                }
                else {
                    context.isLogInFailed = true;
                }
            }) 
        };
    }])
        .directive('loginForm', function(){
            return {
                restrict: 'E',
                templateUrl: '../../../unc-project/directives/login.html'
            };
        });

    function login ($http, context, onSuccess) {
        context.isLogging = true;

        $http({
            url: '/unc-project/login',
            method: 'GET',
            params: {
                'login': context.login,
                'pass': context.pass
            }
        })
            .success(function(data) {
                context.isLogging = false;
                onSuccess(data);
            });
    }
    
    function vk_log($http, context, onSuccess){
        window.location.replace("/unc-project/vklogin")
    }
})();