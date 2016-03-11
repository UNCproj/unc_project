(function(){
    var app = angular.module('default', []);

    $.urlParam = function(name){
        var results = new RegExp('[\?&]' + name + '=([^&#]*)').exec(window.location.href);
        return results[1] || 0;
    }

    app.controller('updateController', ['$scope', '$http', '$timeout',
        function($scope, $http, $timeout) {
            $scope.object = {};

            $scope.update = function() {
                var params = $scope.object;
                params.id = $.urlParam('id');

                $http({
                    url: '/unc-project/uncupdate',
                    method: 'POST',
                    params: params
                })
                    .success(function (data) {
                        alert("updated successfully");
                    });
            }
        }]);
})();