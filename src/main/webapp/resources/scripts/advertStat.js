(function(){
            var app = angular.module('objectSettings', ['chart.js','ngDialog']);
            console.log('startAd');

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
            app.controller("AdvertStatCtrl", function ($scope) {
            var id = getUrlParameter('id');
            var labels = [];
            var data = [];
            $scope.opts = {
                responsive: false,
                maintainAspectRatio: false
            };
            console.log("query start");
            $.ajax({
                url: "/unc-project/StatServlet/getStat",
                async:false,
                data: {"object_id": "", "ad_id":id}
                }).done(function(visits) {
                    console.log(visits);
                    labels = []; data = [];
                $.each(visits, function(index, visit) {
                        labels.push(visit.date);
                        data.push(visit.count);
                    });
                });
                if (labels.length === 0){
                    labels.push("0");
                        data.push("0");
                }
            
            $scope.labels = labels;
            $scope.series = ['Просмотров объявлений за день'];
            $scope.data = [data];
            console.log(labels);
            console.log(data);
        });
        
        
        
        app.controller("ModerCtrl", function ($scope, ngDialog){
               console.log("advert moder ready!");
                $scope.del_model = {};
                $scope.del_model.txt = "";
                $scope.del_model.uid = $('#hidden_user_name').val();
                $scope.deleteAdvert = function (){
                   $.ajax({
                url: "/unc-project/ModerServlet/delAdvert",
                async:false,
                data: {"id": getUrlParameter("id"),
                        "uid":encodeURIComponent($scope.del_model.uid),
                        "msg":encodeURIComponent($scope.del_model.txt),
                        "value":true}
                }).done(function(data) {
                    console.log("del success!");
                    console.log("uid="+$scope.del_model.uid);
                    console.log("umsg="+$scope.del_model.txt);
                    $scope.close_diag.close();
                });
               };
               
                $scope.clickToDel = function (){
                        $scope.close_diag = ngDialog.open({ template: 'directives/delete.html',
                        scope: $scope
                    });
                };
             
            });

        app.controller("BookmarksController", function ($scope, $http){

            $.urlParam = function(name){
                var results = new RegExp('[\?&]' + name + '=([^&#]*)').exec(window.location.href);
                return results[1] || 0;
            };
            $scope.obje = {};
            $scope.obje.id = $.urlParam('id');
            $("#delete-object").on("click",function(){
                $http({
                    url: '/unc-project/delete',
                    method: 'POST',
                    params: $scope.obje
                })
                    .success(function (data) {
                        if (data==0 || data=='0'){
                            var hr = document.location.href;
                            hr = hr.substring(0,hr.indexOf("unc_object.jsp?id="));

                            window.location.replace(hr);
                        }
                    });
                var hr = document.location.href;
                hr = hr.substring(0,hr.indexOf("unc_object.jsp?id="));

                window.location.replace(hr);
            });

            $scope.isInBookmarks = isInBookmarksGlobal;
            $scope.bookmarksButtonText = $scope.isInBookmarks ?
                                            "Удалить из избранного" :
                                            "В избранное";

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
                        $scope.isInBookmarks = false;
                        $scope.bookmarksButtonText = "В избранное";
                    });
            };

            $scope.addBookmark = function(advertId) {
                $http({
                    url: '/unc-project/bookmarks',
                    method: 'GET',
                    params: {
                        'action': 'addBookmark',
                        'bookmarkId': advertId
                    }
                })
                    .success(function(data){
                        $scope.isInBookmarks = true;
                        $scope.bookmarksButtonText = "Удалить из избранного";
                    });
            };

            $scope.addOrDeleteBookmark = function(advertId) {
                if ($scope.isInBookmarks) {
                    $scope.deleteBookmarks(advertId);
                }
                else {
                    $scope.addBookmark(advertId);
                }
            }
        });
})();


