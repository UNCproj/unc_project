(function() {
    var app = angular.module('login', []);

    app.controller('loginController', ['$http', '$timeout', function($http, $timeout) {
        this.login;
        this.pass;
        this.isLogInFailed = false;
        this.isLogging = false;
        var context = this;

        this.submit = function() {
            login($http, this, function (data) {
                if (data["logged"] == "true") {
                    window.location = "/unc-project/index";
                }
                else {
                    context.isLogInFailed = true;
                }
            });
        };
        
        this.vk = function() {
           vk_log($http, this, function (data) {
                if (data["logged"] == "true") {
                    window.location = "/unc-project/index";
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