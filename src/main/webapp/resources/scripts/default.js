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
            };

            $scope.fileAdded = function ($file, $event, $flow) {
                $event.preventDefault();
                $scope.uploader.flow.files[0] = $file;
                $scope.uploader.flow.files[0].name = '/img/user-pics/ava_' + initialUserId + ".png";
                $scope.uploader.flow.upload();
            };

            $scope.complete = function () {
                $http({
                    url: '/unc-project/imageResize',
                    method: 'POST',
                    params: {
                        "imageName": $scope.uploader.flow.files[0].name
                    }
                })
                    .success(function(data) {
                        var avatarImgElem = $('#avatar_img');
                        avatarImgElem.attr('src', avatarImgElem.attr('src') + "?" + Date.now());
                        $scope.isAvatarChanged = true;
                        $timeout(function(){
                            $scope.isAvatarChanged = false;
                        }, 5000);
                    });
            };
        }]);
})();