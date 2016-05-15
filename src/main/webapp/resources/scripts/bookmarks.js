/**
 * Created by Денис on 18.01.2016.
 */
(function(){
    var app = angular.module('bookmarks', []);

    app.controller('advertsController', ['$http', '$scope', function($http, $scope){
        $scope.adverts = [];

        $scope.redirToAdvertPage = function(advId) {
            window.location = "unc_object.jsp?id=" + advId;
        };

        $scope.deleteBookmarks = function(advId) {
            $http({
                url: '/unc-project/bookmarks',
                method: 'GET',
                params: {
                    'action': 'deleteBookmarks',
                    'bookmarkId': advId
                }
            })
                .success(function(data){
                    for (var i = 0; i < $scope.adverts.length; i++) {
                        if ($scope.adverts[i].id = advId) {
                            $scope.adverts.splice(i, 1);
                        }
                    }
                });
        };

        this.loadAdverts = function(){
            $http({
                url: '/unc-project/bookmarks',
                method: 'GET',
                params: {'action': 'getBookmarksList'}
            })
            .success(function(data){
                $scope.adverts = data;
            });
        };

        this.loadAdverts();
    }]);
})();