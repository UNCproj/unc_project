(function() {
    var app = angular.module('jsp404', []);

    app.controller('loadAdvertsController', ['$scope', '$http',
        function($scope, $http) {
            $scope.loadedAdverts = [];

            var shuffle = function(array) {
                var currentIndex = array.length, temporaryValue, randomIndex;

                while (0 !== currentIndex) {
                    randomIndex = Math.floor(Math.random() * currentIndex);
                    currentIndex -= 1;
                    temporaryValue = array[currentIndex];
                    array[currentIndex] = array[randomIndex];
                    array[randomIndex] = temporaryValue;
                }

                return array;
            };

            $http({
                url: 'load-vip-adverts',
                method: 'POST',
                params: {
                    "type": 'advert'
                }
            })
                .success(function(data) {
                    $scope.loadedAdverts = shuffle(data).slice(0, 2);
                });
        }
    ]);
})();