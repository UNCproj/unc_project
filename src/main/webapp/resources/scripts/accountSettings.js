/**
 * Created by Денис on 05.02.2016.
 */
(function(){
<<<<<<< HEAD
    var app = angular.module('accountSettings', ['flow']);

    app.controller('mainSettingsController', ['$scope', '$http', function($scope, $http) {
        $scope.user = {};
=======
    var app = angular.module('accountSettings', ['flow','chart.js']);
        this.login;
        this.pass;
        this.changePass;
        this.email;
>>>>>>> 1e2fbbd889a9aa82dad3ce4fdeafdd004373202c

        $scope.newAvatarAdded = false;
        $scope.isSettingsChanged = false;
        $scope.uploader = {};

        var uploadChanges = function(settingsGroup) {
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

                    if ($scope.uploader.flow.files.length > 0 && $scope.uploader.flow.files[0].name === undefined) {
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
                .success(function(data) {
                    if (data["changed"]) {
                        $scope.isSettingsChanged = true;
                    }
                    //TODO: show error
                });
        }

        $scope.submit = function(settingsGroup) {
            if ($scope.newAvatarAdded) {
                $scope.uploader.flow.files[0].name = '/img/ava_' + initialUserLogin + "_" + Date.now() + ".png";
                $scope.uploader.flow.upload();
            }
            else {
                uploadChanges(settingsGroup);
            }
        }

        $scope.fileAdded = function ($file, $event, $flow) {
            $event.preventDefault();
            $scope.uploader.flow.files[0] = $file;
            $scope.newAvatarAdded = true;
        };

        $scope.complete = function () {
            uploadChanges();
        };
    }]);

<<<<<<< HEAD
    //app.config(['flowFactoryProvider', function (flowFactoryProvider) {
    //    flowFactoryProvider.defaults = {
    //        target: '/unc-project/upload',
    //        permanentErrors:[404, 500, 501],
    //        singleFile: true,
    //        uploadMethod: 'POST',
    //        method: 'multipart'
    //    };
    //}])
})();
=======
    app.config(['flowFactoryProvider', function (flowFactoryProvider) {
        flowFactoryProvider.defaults = {
            target: '/uploadImg',
            permanentErrors:[404, 500, 501],
            singleFile: true
        };
    }])

    
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
>>>>>>> 1e2fbbd889a9aa82dad3ce4fdeafdd004373202c
