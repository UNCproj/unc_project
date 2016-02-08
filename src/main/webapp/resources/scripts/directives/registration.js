(function() {
    var app = angular.module('registration', []);

    app.controller('registrationController', ['$http', function($http) {
        this.login;
        this.pass;
        this.retypePass;
        this.email;
        this.isRegistrationFailed = false;
        var context = this;

        this.submit = function () {
            $http({
                url: '/unc-project/registration',
                method: 'GET',
                params: {
                    'login': this.login,
                    'pass': this.pass,
                    'retypePass': this.retypePass,
                    'email': this.email
                }
            })
                .success(function (data) {
                    if (data["registred"] == "true") {
                        login($http, context, function (data) {
                            window.location = "/unc-project/account";
                        });
                    }
                    else {
                        context.isRegistrationFailed = true;
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

    app.directive('compareTo', function(){
        return {
            restrict: 'A',
            scope: {
                otherModelValue: "=compareTo"
            },
            require: 'ngModel',
            link: function($scope, iElm, iAttrs, ngModel) {
                ngModel.$validators.compareTo = function (modelValue) {
                    return modelValue == $scope.otherModelValue;
                };

                $scope.$watch("otherModelValue", function () {
                    ngModel.$validate();
                });
            }
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