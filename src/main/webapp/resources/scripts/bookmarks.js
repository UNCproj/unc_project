/**
 * Created by Денис on 18.01.2016.
 */
(function(){
    var app = angular.module('bookmarks', []);

    app.controller('advertsController', ['$http', '$scope', function($http, $scope){
        $scope.adverts = [];

        this.loadAdverts = function(){
            $http({
                url: '/unc-project/bookmarks',
                method: 'GET',
            })
            .success(function(data){
                $scope.adverts = data;
            });
        }

        this.loadAdverts();
    }]);
})();