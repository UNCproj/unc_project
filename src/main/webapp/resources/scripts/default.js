(function(){
    var app = angular.module('default', ['flow']);

    $.urlParam = function(name){
        var results = new RegExp('[\?&]' + name + '=([^&#]*)').exec(window.location.href);
        return results[1] || 0;
    }

    app.controller('updateController', ['$scope', '$http', '$timeout',
        function($scope, $http, $timeout) {
            $scope.object = {};
            $scope.uploader = {};

            $scope.update = function() {
                var params = $scope.object;
                params.id = $.urlParam('id');

                $http({
                    url: '/unc-project/uncupdate',
                    method: 'POST',
                    params: params
                })
                    .success(function (data) {

                    });
            };

            $scope.fileAdded = function ($file, $event, $flow) {
                $event.preventDefault();
                $scope.uploader.flow.files[0] = $file;
                var path = $($event.target).parent().attr('path');

                if (path == undefined || path == null || path.length <= 0) {
                    path = '/var/' + $.urlParam('id')+ '.png';

                    $http({
                        url: '/unc-project/uncupdate',
                        method: 'POST',
                        params: {
                            "id": $.urlParam('id'),
                            "user_pic_file": path
                        }
                    })
                        .success(function (data) {
                            var avatarImgElem = $('#' + paramAttrName);
                            avatarImgElem.attr('src', path + "?" + Date.now());
                        });
                }

                $scope.uploader.flow.files[0].name = path;
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
                        var avatarImgElem = $('#' + paramAttrName);
                        avatarImgElem.attr('src', avatarImgElem.attr('src') + "?" + Date.now());
                        $scope.isAvatarChanged = true;
                        $timeout(function(){
                            $scope.isAvatarChanged = false;
                        }, 5000);
                    });
            };
        }]);
})();