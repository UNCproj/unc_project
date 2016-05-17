(function() {
    var app = angular.module('jsp404', []);

    app.controller('loadAdvertsController', ['$scope', '$http',
        function($scope, $http) {
            $scope.loadedAdverts = [];

            $http({
                url: 'advertsList',
                method: 'POST',
                params: {
                    "action": "get_adverts",
                    "adCategoryId": "4"
                }
            })
                .success(function (data) {
                    $scope.loadedAdverts = data;
                });
        }
    ]);
})();