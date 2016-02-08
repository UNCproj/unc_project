/**
 * Created by Денис on 05.02.2016.
 */
(function(){
    var app = angular.module('accountSettings', ['flow']);

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
})();