(function () {
    var app = angular.module('objectSettings', ['chart.js', 'ngDialog', 'flow','ui.bootstrap']);
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

        function isExistData() {
            console.log("check!");
            $.ajax({
                url: "/unc-project/StatServlet/getList",
                async: false,
                data: {"object_id": id}
            }).done(function (ads) {
                console.log('ids=' + ads);
                $.each(ads, function (index, ad) {
                    console.log('id=' + ad);
                    adverts.push({id: ad.id, name: ad.name})
                });
            });
            if ((data.length === 0) && (adverts.length === 0)) {
                $('#ifempty').html('У вас пока что нет объявлений.');
                return false;
            }
            $('#ifempty').html('');

            return true;
        }
        ;

        $scope.isExistData = isExistData();
        $scope.subjects = adverts;
        $scope.selectedItem = adverts[0];

        $scope.dropboxitemselected = function (item) {
            console.log('query!');
            if (!$scope.isExistData) {
                return;
            }
            console.log('query1!' + $scope.isExistData);
            $scope.selectedItem = item;
            console.log($scope.selectedItem.id);
            $.ajax({
                url: "/unc-project/StatServlet/getStat",
                async: false,
                data: {"object_id": id, "ad_id": $scope.selectedItem.id}
            }).done(function (visits) {
                labels = [];
                data = [];
                $.each(visits, function (index, visit) {
                    labels.push(visit.date);
                    data.push(visit.count);
                });
                console.log('query1!');
                if (labels.length == 0)
                {
                    labels = ['0'];
                    data = ['0'];
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
    app.controller("MigrationUser", function ($scope) {
        $scope.CountRowUser;
        $scope.listerr;
        $scope.err;

        $scope.uploader = {};

        $scope.fileAdded = function ($file, $event, $flow) {
            $event.preventDefault();
            $scope.uploader.flow.files[0] = $file;
            $scope.uploader.flow.files[0].name = '/excel/1.xls';
            $scope.uploader.flow.upload();
        };

        $scope.success = function ($message) {
            var params = JSON.parse($message);
            $scope.CountRowUser = 'Успешно ' + params.countRowUser;
            $scope.err = 'С ошибкой ' + params.listErrUser.length;
            $scope.listerr = params.listErrUser;
        };

    });
    app.controller("MigrationAdvert", function ($scope) {
        $scope.CountRowAdvert;
        $scope.listerr;
        $scope.err;

        $scope.uploader = {};

        $scope.fileAdded = function ($file, $event, $flow) {
            $event.preventDefault();
            $scope.uploader.flow.files[0] = $file;
            $scope.uploader.flow.files[0].name = '/excel/1.xls';
            $scope.uploader.flow.upload();
        };

        $scope.success = function ($message) {
            var params = JSON.parse($message);
            $scope.CountRowAdvert = 'Успешно ' + params.countRowAdvert;
            $scope.err = 'С ошибкой ' + params.listErrAdvert.length;
            $scope.listerr = params.listErrAdvert;
        };

    });

    app.controller("AdminCtrl", function ($scope, ngDialog) {
        console.log("adminmoder ready!");
        $scope.del_id = "";
        $scope.del_value = "";
        $scope.del_model = {};
        $scope.del_model.txt = "";
        $scope.del_model.uid = $('#hidden_user_name').val();
        $scope.deleteAdvert = function (usr_id, value) {
            console.log('del_id=' + $scope.del_id);
            $.ajax({
                url: "/unc-project/ModerServlet/delAdvert",
                async: false,
                data: {"id": usr_id,
                    "uid": encodeURIComponent($scope.del_model.uid),
                    "msg": encodeURIComponent($scope.del_model.txt),
                    "value": value}
            }).done(function (data) {
                console.log("del success!");
                console.log("uid=" + $scope.del_model.uid);
                console.log("umsg=" + $scope.del_model.txt);
                if ($scope.close_diag != null) $scope.close_diag.close();
                $scope.loadAllUsers();
            });
        };
        $scope.set_del_id = function (value){
            $scope.del_id = value;
        };
        $scope.clickToDel = function () {
            $scope.del_value = "true";
            console.log('delv='+$scope.del_value);
            $scope.close_diag = ngDialog.open({template: 'directives/delete.html',
                scope: $scope
            });
        };
        
        $scope.clickToUnblock = function () {
            $scope.del_value = "false";
            $scope.deleteAdvert($scope.del_id, $scope.del_value);
            
        };
        
        $scope.clickToModer = function (id, value, attr) {
            $.ajax({
                url: "/unc-project/ModerServlet/setModerRights",
                async: false,
                data: {"id": id,
                    "value": value,
                    "attr":attr
                }
            }).done(function (data) {
                console.log("moder success!");
                $scope.loadAllUsers();
            });
        };
        
        $scope.users = {};
        $scope.loadAllUsers = function (){
            $.ajax({
                url: "/unc-project/ModerServlet/GetUsersList",
                async: false,
                data: {}
            }).done(function (data) {
                $scope.users = data;
                console.log($scope.users);
            });
        };
        
        
        //pager
        console.log('pager');
        if ($('#rights').val()==='admin'){
            $scope.loadAllUsers();
        };
        $scope.viewby = 10;
        $scope.totalItems = $scope.users.length;
        $scope.currentPage = 1;
        $scope.itemsPerPage = $scope.viewby;
        $scope.maxSize = 5; //Number of pager buttons to show
        
        //$scope.numPages = 1 + ($scope.data.length / $scope.viewby);
        
        $scope.setPage = function (pageNo) {
            $scope.currentPage = pageNo;
        };

        $scope.pageChanged = function () {
            console.log('Page changed to: ' + $scope.currentPage);
        };

        $scope.setItemsPerPage = function (num) {
            $scope.itemsPerPage = num;
            $scope.currentPage = 1; //reset to first paghe
        };
        
        //stat
        $scope.reg_data = {};
        $scope.reg_data.opts = {
                    responsive: false,
                    maintainAspectRatio: false
                };
        $scope.reg_data.series = ['Статистика регистраций'];
        function loadRegData() {
            console.log("check!");
            $.ajax({
                url: "/unc-project/StatServlet/getRegistrations",
                async: false,
                data: {}
            }).done(function (ads) {
                $scope.reg_data.count = []; $scope.reg_data.date = [];
                $.each(ads, function (index, vis) {
                    
                    $scope.reg_data.date.push(vis.date);
                    $scope.reg_data.count.push(vis.count);
                });
                $scope.reg_data.count = [$scope.reg_data.count];
            });
        };
        
        if ($('#rights').val()==='admin'){
            loadRegData();
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
                            console.log(data);
                            for(var i=0; i<data.length;i++){
                                //$("div.message-list").append('<div class="users-message" onclick=\"location.href=\'chat.jsp?id='+data[i].recipientId +'\'\"></div>');
                                //$("div.message-list div.users-message:last").append('<div class="message-login">' + data[i].recipientLogin + '</div>');
                                //$("div.message-list div.users-message:last").append('<div class="message-date">' + data[i].date + '</div>');
                                //$("div.message-list div.users-message:last").append('<div class="message-text">' + data[i].text + '</div>');
                                $("div.message-list").append('<div class="users-message" onclick=\"location.href=\'chat.jsp?id='+data[i].recipientId +'\'\"></div>');
                                if (data[i].recipientName== null && data[i].recipientSurname==null) {
                                    $("div.message-list div.users-message:last").append('<div class="message-login">' + data[i].recipientLogin + '</div>');
                                } else if (data[i].recipientName!= null && data[i].recipientSurname!=null) {
                                    $("div.message-list div.users-message:last").append('<div class="message-login">' + data[i].recipientName + ' ' +
                                        data[i].recipientSurname + ' (' + data[i].recipientLogin + ')</div>');
                                } else if (data[i].recipientName!= null && data[i].recipientSurname==null) {
                                    $("div.message-list div.users-message:last").append('<div class="message-login">' + data[i].recipientName +
                                        ' (' + data[i].recipientLogin + ')</div>');
                                } else if (data[i].recipientName== null && data[i].recipientSurname!=null) {
                                    $("div.message-list div.users-message:last").append('<div class="message-login">' +
                                        data[i].recipientSurname + ' (' + data[i].recipientLogin + ')</div>');
                                }
                                $("div.message-list div.users-message:last").append('<div class="message-date">' + data[i].date + '</div>');
                                $("div.message-list div.users-message:last").append('<div class="message-text">' + data[i].text + '</div>');
                                 if(data[i].readStatus=='no'){
                                     $("div.message-list div.users-message:last").append('<div class="message-status">' + 'Непрочитано' + '</div>');
                                 }
                            }
                        });
                };
            });
})();