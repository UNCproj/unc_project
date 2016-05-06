(function(){
            var app = angular.module('objectSettings', ['chart.js','ngDialog']);
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
                console.log("check!");
                    $.ajax({
                        url: "/unc-project/StatServlet/getList",
                        async: false,
                        data: {"object_id": id}
                        }).done(function(ads) {
                            console.log('ids='+ads);
                        $.each(ads, function(index, ad) {
                            console.log('id='+ad);
                        adverts.push({id:ad.id, name:ad.name})
                        });
                });
                if ((data.length === 0)&&(adverts.length === 0)){
                    $('#ifempty').html('У вас пока что нет объявлений.');
                    return false;
                }
                $('#ifempty').html('');

                return true;
            };

            $scope.isExistData = isExistData();
            $scope.subjects = adverts;
            $scope.selectedItem = adverts[0];

            $scope.dropboxitemselected = function (item) {
                console.log('query!');
                if (!$scope.isExistData) { return; }
                console.log('query1!'+$scope.isExistData);
                $scope.selectedItem = item;
                console.log($scope.selectedItem.id);
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

            $scope.dropboxitemselected($scope.selectedItem);
            $scope.onClick = function (points, evt) {
                        console.log(points, evt);
            };
            });

            app.controller("AdminCtrl", function ($scope, ngDialog){
               console.log("adminmoder ready!");
                $scope.del_model = {};
                $scope.del_model.txt = "";
                $scope.del_model.uid = $('#hidden_user_name').val();
                $scope.deleteAdvert = function (){
                   $.ajax({
                url: "/unc-project/ModerServlet/delAdvert",
                async:false,
                data: {"id": getUrlParameter("id"),
                        "uid":encodeURIComponent($scope.del_model.uid),
                        "msg":encodeURIComponent($scope.del_model.txt)}
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
				$scope.clickToModer = function(value){
                    $.ajax({
                    url: "/unc-project/ModerServlet/setModerRights",
                    async:false,
                    data: {"id": getUrlParameter("id"),
                            "value":value
                          }
                    }).done(function(data) {
                        console.log("moder success!");
                    });
                };

            });
            app.controller("MessagesController", function ($scope, $http){
                $.urlParam = function(name){
                    var results = new RegExp('[\?&]' + name + '=([^&#]*)').exec(window.location.href);
                    return results[1] || 0;
                };
                $scope.object = {};
                $scope.object.id = $.urlParam('id');
                $("ul#tab_name li a[href$='#messages']").click(function(){
                    $("div.message-list").empty();
                    $scope.loadMess();
                });
                $scope.loadMess = function(){
                    $http({
                        url: '/unc-project/loadMesList',
                        method: 'POST',
                        params: $scope.object
                    })
                        .success(function (data) {
                            for(var i=0; i<data.length;i++){
                                $("div.message-list").append('<div class="users-message" onclick=\"location.href=\'chat.jsp?id='+data[i].recipientId +'\'\"></div>');
                                $("div.message-list div.users-message:last").append('<div class="message-login">' + data[i].recipientLogin + '</div>');
                                $("div.message-list div.users-message:last").append('<div class="message-date">' + data[i].date + '</div>');
                                $("div.message-list div.users-message:last").append('<div class="message-text">' + data[i].text + '</div>');
                            }
                        });
                };
            });
})();