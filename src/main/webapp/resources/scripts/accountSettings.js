/**
 * Created by Денис on 05.02.2016.
 */
(function(){

    var app = angular.module('accountSettings', ['flow','chart.js']);

    app.controller('mainSettingsController', ['$scope', '$http',
        function($scope, $http, ImageResizerService) {
            $scope.user = {};

            $scope.isSettingsChanged = false;
            $scope.settingsGroup = "";
            $scope.uploader = {};

            var uploadChanges = function (settingsGroup) {
                var params;

                switch (settingsGroup) {
                    case 'main':
                        params = {
                            'login': $scope.user.login,
                            'pass': $scope.user.pass,
                            'changePass': $scope.user.changePass,
                            'email': $scope.user.email,
                            'settingsGroup': settingsGroup
                        };

                        if ($scope.uploader.flow.files.length > 0 && $scope.uploader.flow.files[0].name !== undefined) {
                            params.userPicFile = $scope.uploader.flow.files[0].name;
                        }
                        break;

                    case 'about':
                        params = {
                            'firstName': $scope.user.firstName,
                            'secondName': $scope.user.secondName,
                            'surname': $scope.user.surname,
                            'phone': $scope.user.phone,
                            'streetAndHouse': $scope.user.streetAndHouse,
                            'city': $scope.user.city,
                            'country': $scope.user.country,
                            'additionalInfo': $scope.user.additionalInfo,
                            'pass': $scope.user.pass,
                            'settingsGroup': settingsGroup
                        };
                        break;
                }

                $http({
                    url: '/unc-project/accountSettings',
                    method: 'POST',
                    params: params
                })
                    .success(function (data) {
                        if (data["changed"]) {
                            $scope.isSettingsChanged = true;
                        }
                        //TODO: show error
                    });
            };

            $scope.submit = function (settingsGroup) {
                $scope.settingsGroup = settingsGroup;
                uploadChanges(settingsGroup);
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
                        $scope.isSettingsChanged = true;
                    });
            };
        }]);

    //app.config(['flowFactoryProvider', function (flowFactoryProvider) {
    //    flowFactoryProvider.defaults = {
    //        target: '/unc-project/upload',
    //        permanentErrors:[404, 500, 501],
    //        singleFile: true,
    //        uploadMethod: 'POST',
    //        method: 'multipart'
    //    };
    //}])
    
    app.controller("LineCtrl", function ($scope) {
        var id = "194858612108367968"; //здесь должен быть object_id текущего пользователя
        var labels = [];
        var data = [];
        $.ajax({
        url: "StatServlet",
        async: false,
        data: {"object_id": id}
        }).done(function(visits) {
        $.each(visits, function(index, visit) {
                labels.push(visit.date);
                data.push(visit.count);
            });
      });
        $scope.labels = labels;
        $scope.series = ['Просмотров объявлений за день'];
        $scope.data = [data];
        $scope.onClick = function (points, evt) {
                    //console.log(points, evt);
        };
    });
})();
