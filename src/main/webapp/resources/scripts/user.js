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
            $scope.err = "С ошибкой";
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
            $scope.err = "С ошибкой";
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
        
        $scope.clickToModer = function (value) {
            $.ajax({
                url: "/unc-project/ModerServlet/setModerRights",
                async: false,
                data: {"id": getUrlParameter("id"),
                    "value": value
                }
            }).done(function (data) {
                console.log("moder success!");
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
        $scope.loadAllUsers();
        $scope.data = [{"name": "Bell", "id": "K0H 2V5"}, {"name": "Octavius", "id": "X1E 6J0"}, {"name": "Alexis", "id": "N6E 1L6"}, {"name": "Colton", "id": "U4O 1H4"}, {"name": "Abdul", "id": "O9Z 2Q8"}, {"name": "Ian", "id": "Q7W 8M4"}, {"name": "Eden", "id": "H8X 5E0"}, {"name": "Britanney", "id": "I1Q 1O1"}, {"name": "Ulric", "id": "K5J 1T0"}, {"name": "Geraldine", "id": "O9K 2M3"}, {"name": "Hamilton", "id": "S1D 3O0"}, {"name": "Melissa", "id": "H9L 1B7"}, {"name": "Remedios", "id": "Z3C 8P4"}, {"name": "Ignacia", "id": "K3B 1Q4"}, {"name": "Jaime", "id": "V6O 7C9"}, {"name": "Savannah", "id": "L8B 8T1"}, {"name": "Declan", "id": "D5Q 3I9"}, {"name": "Skyler", "id": "I0O 4O8"}, {"name": "Lawrence", "id": "V4K 0L2"}, {"name": "Yael", "id": "R5E 9D9"}, {"name": "Herrod", "id": "V5W 6L3"}, {"name": "Lydia", "id": "G0E 2K3"}, {"name": "Tobias", "id": "N9P 2V5"}, {"name": "Wing", "id": "T5M 0E2"}, {"name": "Callum", "id": "L9P 3W5"}, {"name": "Tiger", "id": "R9A 4E4"}, {"name": "Summer", "id": "R4B 4Q4"}, {"name": "Beverly", "id": "M5E 4V4"}, {"name": "Xena", "id": "I8G 6O1"}, {"name": "Yael", "id": "L1K 5C3"}, {"name": "Stacey", "id": "A4G 1S4"}, {"name": "Marsden", "id": "T1J 5J3"}, {"name": "Uriah", "id": "S9S 8I7"}, {"name": "Kamal", "id": "Y8Z 6X0"}, {"name": "MacKensie", "id": "W2N 7P9"}, {"name": "Amelia", "id": "X7A 0U3"}, {"name": "Xavier", "id": "B8I 6C9"}, {"name": "Whitney", "id": "H4M 9U2"}, {"name": "Linus", "id": "E2W 7U1"}, {"name": "Aileen", "id": "C0C 3N2"}, {"name": "Keegan", "id": "V1O 6X2"}, {"name": "Leonard", "id": "O0L 4M4"}, {"name": "Honorato", "id": "F4M 8M6"}, {"name": "Zephr", "id": "I2E 1T9"}, {"name": "Karen", "id": "H8W 4I7"}, {"name": "Orlando", "id": "L8R 0U4"}, {"name": "India", "id": "N8M 8F4"}, {"name": "Luke", "id": "Q4Y 2Y8"}, {"name": "Sophia", "id": "O7F 3F9"}, {"name": "Faith", "id": "B8P 1U5"}, {"name": "Dara", "id": "J4A 0P3"}, {"name": "Caryn", "id": "D5M 8Y8"}, {"name": "Colton", "id": "A4Q 2U1"}, {"name": "Kelly", "id": "J2E 2L3"}, {"name": "Victor", "id": "H1V 8Y5"}, {"name": "Clementine", "id": "Q9R 4G8"}, {"name": "Dale", "id": "Q1S 3I0"}, {"name": "Xavier", "id": "Z0N 0L5"}, {"name": "Quynn", "id": "D1V 7B8"}, {"name": "Christine", "id": "A2X 0Z8"}, {"name": "Matthew", "id": "L1H 2I4"}, {"name": "Simon", "id": "L2Q 7V7"}, {"name": "Evan", "id": "Z8Y 6G8"}, {"name": "Zachary", "id": "F4K 8V9"}, {"name": "Deborah", "id": "I0D 4J6"}, {"name": "Carl", "id": "X7H 3J3"}, {"name": "Colin", "id": "C8P 0O1"}, {"name": "Xenos", "id": "K3S 1H5"}, {"name": "Sonia", "id": "W9C 0N3"}, {"name": "Arsenio", "id": "B0M 2G6"}, {"name": "Angela", "id": "N9X 5O7"}, {"name": "Cassidy", "id": "T8T 0Q5"}, {"name": "Sebastian", "id": "Y6O 0A5"}, {"name": "Bernard", "id": "P2K 0Z5"}, {"name": "Kerry", "id": "T6S 4T7"}, {"name": "Uriel", "id": "K6G 5V2"}, {"name": "Wanda", "id": "S9G 2E5"}, {"name": "Drake", "id": "G3G 8Y2"}, {"name": "Mia", "id": "E4F 4V8"}, {"name": "George", "id": "K7Y 4L4"}, {"name": "Blair", "id": "Z8E 0F0"}, {"name": "Phelan", "id": "C5Z 0C7"}, {"name": "Margaret", "id": "W6F 6Y5"}, {"name": "Xaviera", "id": "T5O 7N5"}, {"name": "Willow", "id": "W6K 3V0"}, {"name": "Alden", "id": "S2M 8C1"}, {"name": "May", "id": "L5B 2H3"}, {"name": "Amaya", "id": "Q3B 7P8"}, {"name": "Julian", "id": "W6T 7I6"}, {"name": "Colby", "id": "N3Q 9Z2"}, {"name": "Cole", "id": "B5G 0V7"}, {"name": "Lana", "id": "O3I 2W9"}, {"name": "Dieter", "id": "J4A 9Y6"}, {"name": "Rowan", "id": "I7E 9U4"}, {"name": "Abraham", "id": "S7V 0W9"}, {"name": "Eleanor", "id": "K7K 9P4"}, {"name": "Martina", "id": "V0Z 5Q7"}, {"name": "Kelsie", "id": "R7N 7P2"}, {"name": "Hedy", "id": "B7E 7F2"}, {"name": "Hakeem", "id": "S5P 3P6"}];
        $scope.viewby = 10;
        $scope.totalItems = $scope.users.length;
        $scope.currentPage = 4;
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

    });
    app.controller("MessagesController", function ($scope, $http) {
        $.urlParam = function (name) {
            var results = new RegExp('[\?&]' + name + '=([^&#]*)').exec(window.location.href);
            return results[1] || 0;
        };
        $scope.object = {};
        $scope.object.id = $.urlParam('id');
        $("ul#tab_name li a[href$='#messages']").click(function () {
            $("div.message-list").empty();
            $scope.loadMess();
        });
        $scope.loadMess = function () {
            $http({
                url: '/unc-project/loadMesList',
                method: 'POST',
                params: $scope.object
            })
                    .success(function (data) {
                        for (var i = 0; i < data.length; i++) {
                            $("div.message-list").append('<div class="users-message" onclick=\"location.href=\'chat.jsp?id=' + data[i].recipientId + '\'\"></div>');
                            $("div.message-list div.users-message:last").append('<div class="message-login">' + data[i].recipientLogin + '</div>');
                            $("div.message-list div.users-message:last").append('<div class="message-date">' + data[i].date + '</div>');
                            $("div.message-list div.users-message:last").append('<div class="message-text">' + data[i].text + '</div>');
                        }
                    });
        };
    });
})();