(function() {
    var app = angular.module('registration', []);

    app.controller('registrationController', ['$http', '$scope', function($http, $scope) {
        $scope.registrationObj = {};
        $scope.failedInfo = {};


        this.submit = function () {
            $http({
                url: '/unc-project/registration',
                method: 'GET',
                params: $scope.registrationObj
            })
                .success(function (data) {
                    if (data["registred"]) {
                        login($http, $scope.registrationObj, function (data) {
                            window.location = "/unc-project/unc_update.jsp?id=" + data["userId"];
                        });
                    }
                    else {
                        $scope.failedInfo = data;
                    }
                });
        };
    }])
        .directive('registrationForm', function(){
            return {
                restrict: 'E',
                templateUrl: '../../../unc-project/directives/registration.html'
            };
        });

    function login ($http, context, onSuccess) {
        $http({
            url: '/unc-project/login',
            method: 'GET',
            params: {
                'login': context.login,
                'pass': context.password
            }
        })
            .success(onSuccess);
    }
})();