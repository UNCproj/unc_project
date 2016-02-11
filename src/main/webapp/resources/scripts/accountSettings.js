/**
 * Created by Денис on 05.02.2016.
 */
(function(){
    var app = angular.module('accountSettings', ['flow','chart.js']);
    app.controller('mainSettingsController', ['$scope', function($scope) {
        this.login;
        this.pass;
        this.changePass;
        this.email;

        var newAvatarAdded = false;
        var uploadChanges = function() {

        }

        this.submit = function() {
            if (newAvatarAdded) {
                $flow.upload();
            }
            else {
                uploadChanges();
            }
        }

        $scope.$on('flow::fileAdded', function (event, $flow, flowFile) {
            event.preventDefault();
            $flow.files[0] = flowFile;
            newAvatarAdded = true;
        });

        $scope.$on('flow::complete', function () {
            uploadChanges();
        });
    }]);

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
