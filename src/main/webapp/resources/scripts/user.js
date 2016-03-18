(function(){
            var app = angular.module('objectSettings', ['chart.js']);
            console.log('start');           
            var getUrlParameter = function getUrlParameter(sParam) {
                var sPageURL = decodeURIComponent(window.location.search.substring(1)),
                    sURLVariables = sPageURL.split('&'),
                    sParameterName,
                    i;
                for (i = 0; i < sURLVariables.length; i++) {                 
                    sParameterName = sURLVariables[i].split('=');
                    if (sParameterName[0] === sParam) {
                        return sParameterName[1] === undefined ? true : sParameterName[1];
                    }
                }
            };
            app.controller("LineCtrl", function ($scope) {
            var id = getUrlParameter('id');
            var labels = [];
            var data = [];
            var adverts = [];
            
            $scope.isExistData = function isExistData(){
                if ((data.length === 0)&&(adverts.length===0)){
                    $('#ifempty').html('У вас пока что нет объявлений.');
                    return false;
                }
                $('#ifempty').html('');
                return true;
            }
            
            $.ajax({
            url: "/unc-project/StatServlet/getList",
            async: false,
            data: {"object_id": id}
            }).done(function(ads) {
            $.each(ads, function(index, ad) {
                    adverts.push({id:ad.id, name:ad.name})
                });
            });
            
            
            
            $scope.dropboxitemselected = function (item) {
                console.log('query!');
                $scope.selectedItem = item;
                $.ajax({
                url: "/unc-project/StatServlet/getStat",
                async: false,
                data: {"object_id": id, "ad_id":$scope.selectedItem.id}
                }).done(function(visits) {
                    labels = []; data = [];
                $.each(visits, function(index, visit) {
                        labels.push(visit.date);
                        data.push(visit.count);
                    });
                
                if (labels.length == 0) 
                {
                    labels = ['0']; data = ['0'];
                }
                $scope.opts = {
                    responsive: false,
                    maintainAspectRatio: false
                };
                $scope.labels = labels;
                $scope.series = ['Просмотров объявлений за день'];
                $scope.data = [data];
                console.log($scope.labels);
                console.log($scope.data);
            });
            }
            $scope.subjects = adverts;
            $scope.selectedItem = adverts[0];
            $scope.dropboxitemselected($scope.selectedItem);
            $scope.onClick = function (points, evt) {
                        console.log(points, evt);
            };
            }); 
})();