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
            //console.log("id="+id);
            var labels = [];
            var data = [];
            var adverts = [];
            
            
                
            function isExistData(){
                    $.ajax({
                url: "/unc-project/StatServlet/getList",
                async: false,
                data: {"object_id": id}
                }).done(function(ads) {
                    console.log("len="+ads.length);
                $.each(ads, function(index, ad) {
                        adverts.push({id:ad.id, name:ad.name})
                    });
                });
                console.log("check!");
                if ((data.length === 0)&&(adverts.length === 0)){
                    $('#ifempty').html('У вас пока что нет объявлений.');
                    return false;
                }
                $('#ifempty').html('');
                return true;
            };
            
            $scope.isExistData = isExistData();
            
            
            
            
            
            $scope.dropboxitemselected = function (item) {
                if (!$scope.isExistData) { return; }
                console.log('query1!'+$scope.isExistData);
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
                console.log('query1!');
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
            };
            $scope.subjects = adverts;
            $scope.selectedItem = adverts[0];
            $scope.dropboxitemselected($scope.selectedItem);
            $scope.onClick = function (points, evt) {
                        console.log(points, evt);
            };
            }); 
            
            app.controller("ModerCtrl", function ($scope){
               console.log("user moder ready!"); 
            });
})();