(function() {
    var app = angular.module('login', []);

    app.controller('loginController', ['$http', function($http) {
        this.login;
        this.pass;
        this.isLogInFailed = false;
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
    }])
        .directive('loginForm', function(){
            return {
                restrict: 'E',
                templateUrl: '../../../unc-project/directives/login.html'
            };
        });

    function login ($http, context, onSuccess) {
        $http({
            url: '/unc-project/login',
            method: 'GET',
            params: {
                'login': context.login,
                'pass': context.pass
            }
        })
            .success(onSuccess);
    }
})();