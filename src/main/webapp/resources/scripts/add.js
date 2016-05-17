$(function () {
    $("ul.nav li a:first").tab("show");
    $("ul.nav li").addClass("the-tab");

    $("div.footlog p.small").hide();
});

(function () {
    var app = angular.module('add', ['flow']);

    $.urlParam = function (name) {
        var results = new RegExp('[\?&]' + name + '=([^&#]*)').exec(window.location.href);
        return results[1] || 0;
    };
    
    app.controller('addController', ['$scope', '$http', '$timeout', '$document',
        function ($scope, $http, $timeout) {
            $scope.object = {};
            $scope.object.type = $.urlParam('type');
            $scope.arrAttr = [];
            $scope.arrAttrIndex = 0;
            $scope.advertId;
            $scope.uploader = {};
            $scope.path;
            $scope.file;

            $("div.file-photo").hide();

            if ($.urlParam('type') == 'forum_topic') {
                $scope.object.type = $.urlParam('type');
                $scope.object.type1 = decodeURI($.urlParam('name'));
            }
            if ($.urlParam('type') == 'forum_comment') {
                $scope.object.type = $.urlParam('type');
                $scope.object.topic_id = $.urlParam('id');
            }

            $("div#category-div-1 a").on("click", function () {
                $("div#category-div-1 a").removeClass("a-category-active");
                $(this).addClass("a-category-active");
                $scope.object.type1 = $(this).text();
                $scope.loadTypes("type1");
            });

            $scope.loadTypes = function (types) {
                var type = $scope.object;
                if (types === 'type1') {
                    $("#category-div-2").empty();
                    type.type2 = null;
                    $("#category-div-3").empty();
                    type.type3 = null;
                    $("#category-div-4").empty();
                    type.type4 = null;
                }
                if (types === 'type2') {
                    $("#category-div-3").empty();
                    type.type3 = null;
                    $("#category-div-4").empty();
                    type.type4 = null;
                }
                if (types === 'type3') {
                    $("#category-div-4").empty();
                    type.type4 = null;
                }
                $http({
                    url: '/unc-project/loadTypes',
                    method: 'POST',
                    params: type
                })
                    .success(function (data) {
                        console.log(types + ':' + data);
                        if (types == 'type1' && data[0] != null) {
                            for (var i = 0; i < data.length; i++) {
                                $("div#category-div-2").append('<a class="list-group-item a-category">' + data[i] + "</a>");
                            }
                            $("div#category-div-2 a").on("click", function () {
                                $("div#category-div-2 a").removeClass("a-category-active");
                                $(this).addClass("a-category-active");
                                $scope.object.type2 = $(this).html();
                                $scope.loadTypes("type2");
                            });
                        }
                        if (types == 'type2' && data[0] != null) {
                            for (var i = 0; i < data.length; i++) {
                                $("div#category-div-3").append('<a class="list-group-item a-category">'
                                    + data[i] + "</a>");
                            }
                            $("div#category-div-3 a").on("click", function () {
                                $("div#category-div-3 a").removeClass("a-category-active");
                                $(this).addClass("a-category-active");
                                $scope.object.type3 = $(this).html();
                                $scope.loadTypes("type3");
                            });
                        }
                        if (types == 'type3' && data[0] != null) {
                            for (var i = 0; i < data.length; i++) {
                                $("div#category-div-4").append('<a class="list-group-item a-category">'
                                    + data[i] + "</a>");
                                $("div#category-div-4").show();
                            }
                            $("div#category-div-4 a").on("click", function () {
                                $("div#category-div-4 a").removeClass("a-category-active");
                                $(this).addClass("a-category-active");
                                $scope.object.type4 = $(this).html();

                                $scope.hideCategory();
                            });
                        }
                        if (data[0] == null) {
                            $scope.hideCategory();
                        }
                    });
            };

            $scope.hideCategory = function () {
                $("div#category-div-1").hide();
                $("div#category-div-2").hide();
                $("div#category-div-3").hide();
                $("div#category-div-4").hide();
                $("div.attributes").empty();
                $("div.file-photo").toggle();
                if ($scope.object.type3 == null) {
                    $("div#category label").append('<div class="col-md-12"><p class="select-category">' + $scope.object.type1 +
                        ' / ' + $scope.object.type2 + '</p></div>');
                } else if ($scope.object.type4 == null) {
                    $("div#category label").append('<div class="col-md-12"><p class="select-category">' +
                        $scope.object.type1 + ' / ' + $scope.object.type2 + ' / ' + $scope.object.type3 + '</p></div>');
                } else {
                    $("div#category label").append('<div class="col-md-12"><p class="select-category">' +
                        $scope.object.type1 + ' / ' + $scope.object.type2 + ' / ' + $scope.object.type3 + ' / '
                        + $scope.object.type4 + '</p></div>');
                }
                $("div#category p.select-category").on('click', function () {
                    $("div#category p.select-category").empty();
                    $("div.attributes").empty();
                    $("div.content input#btn-load").remove();
                    $("div#category-div-1").show();
                    $("div#category-div-2").show();
                    $("div#category-div-3").show();
                    $("div#category-div-4").show();
                    $("div.file-photo").toggle();
                });
                $scope.loadAttrs();
            };
            $scope.loadAttrs = function () {
                var attrs = $scope.object;
                $http({
                    url: '/unc-project/loadAttrs',
                    method: 'POST',
                    params: attrs
                })
                    .success(function (data) {

                        $("div.attributes").append('<table class="table table-params"><tbody></tbody></table>');

                        $("div.attributes table.table-params tbody").append(
                            '<tr>' +
                            '<td>' +
                            'Название' +
                            '</td>' +
                            '<td>' +
                            '<input type="text" ng-model="object.name" id="name">' +
                            '</td>' +
                            '</tr>');

                        $scope.arrAttr = [];

                        for (var i = 0; i < data.length; i++) {
                            $scope.arrAttr[$scope.arrAttrIndex] = data[i].attrName;
                            $scope.arrAttrIndex++;
                            $scope.createAttr(data[i].attrName, data[i].attrNameRu, data[i].attrType);
                        }

                        //$("div.table-pos div.attributes").append(
                        $("div.content").append(
                            '<input type="button" style="margin-left: 200px" class="button-style a-outline button-update" value="Добавить" id="btn-load"' +
                                //'onclick="location.href=\'index.jsp\'"' +
                            '>');


                        $("#btn-load").on('click', function () {
                            console.log('Нажали на кнопку');
                            var params = {};
                            params.type = $scope.object.type;
                            params.type1 = $scope.object.type1;
                            params.type2 = $scope.object.type2;
                            params.type3 = $scope.object.type3;
                            params.type4 = $scope.object.type4;
                            params.name = $("#name").val();
                            for (var i = 0; i < $scope.arrAttr.length; i++) {
                                if ($scope.arrAttr[i] != null && $("#" + $scope.arrAttr[i]).val() != "") {
                                    params[$scope.arrAttr[i]] = $("#" + $scope.arrAttr[i]).val();
                                }
                            }
                            console.log(params);
                            $scope.postAdd(params);
                        });
                    });
            };

            $scope.postAdd = function (p) {
                console.log("Начали грузить");
                p.map_coordinates = $scope.map_coordinates;
                var citySplitted = p.city.split(',');

                if (citySplitted.length > 0) {
                    p.city = citySplitted[0];
                }

                if ($scope.validationBefore() == true) {
                    $http({
                        url: '/unc-project/uncadd',
                        method: 'POST',
                        params: p
                    })
                        .success(function (data) {
                            if (data == 1) {
                                var h = window.location.href;
                                var n = h.indexOf('unc_add.jsp?');
                                var hr = h.substring(0, n);
                                if ($scope.object.type == 'forum_topic') {
                                    window.location.href = hr + "forum.jsp?type=" + $scope.object.type1;
                                }
                                if ($scope.object.type == 'forum_comment') {
                                    window.location.href = hr + "forum.jsp?id=" + $scope.object.topic_id;
                                }
                            } else if (data == 2) {
                                var h = window.location.href;
                                var n = h.indexOf('unc_add.jsp?');
                                var hr = h.substring(0, n);
                                if ($scope.object.type == 'advert') {
                                    window.location.href = hr;
                                }
                                if ($scope.object.type == 'forum_topic') {
                                    window.location.href = hr + "forum.jsp?type=" + $scope.object.type1;
                                }
                                if ($scope.object.type == 'forum_comment') {
                                    window.location.href = hr + "forum.jsp?id=" + $scope.object.topic_id;
                                }
                            } else if (data == 3) {

                            } else {

                                //Добавление картинок

                                $scope.advertId = data;
                                for (var i = 0; i < $scope.advertId.length; i++) {
                                    if ($scope.advertId[i] == '\n' || $scope.advertId[i] == '\r') {
                                        $scope.advertId = $scope.advertId.substring(0, i) + $scope.advertId.substring(i + 1);
                                    }
                                }
                                $scope.fileAdded = function () {
                                    $scope.uploader.flow.files[0] = $scope.file;
                                    var path = $scope.path;

                                    if (path == undefined || path == null || path.length <= 0) {
                                        path = '/var/' + $scope.advertId + '.png';
                                    }

                                    console.log(typeof path);
                                    for (var i = 0; i < path.length; i++) {
                                        if (path[i] == '\n' || path[i] == '\r') {
                                            path = path.substring(0, i) + path.substring(i + 1);
                                        }
                                    }
                                    console.log(path);
                                    $scope.uploader.flow.files[0].name = path;
                                    console.log($scope.uploader.flow.files[0].name);
                                    console.log($scope.uploader.flow.files[0]);
                                    console.log($scope.uploader.flow);
                                    $scope.uploader.flow.upload();
                                    $http({
                                        url: '/unc-project/saveAttrPicServlet',
                                        method: 'POST',
                                        params: {
                                            "id": $scope.advertId,
                                            "user_pic_file": path
                                        }
                                    })
                                        .success(function (data) {
                                        });
                                };
                                $scope.fileAdded();

                                //window.location.replace(location.href + 'unc_object?id=' + data);
                            }
                        })
                }
                $scope.validationAfter();
            };
            $scope.validationBefore = function () {
                var validStat = true;
                $("#information").empty();
                if ($("#name").val() == "") {
                    $("#name").addClass("invalid");
                    $("#information").append("<p>Пустое поле ввода названия</p>");
                    validStat = false;
                } else {
                    $("#name").removeClass("invalid");
                }
                if ($("#description").val() == "") {
                    $("#description").addClass("invalid");
                    $("#information").append("<p>Добавьте описание</p>");
                    validStat = false;
                } else {
                    $("#description").removeClass("invalid");
                }
                if ($("#city").val() == "" || $("#city").val() == "Выберите город") {
                    $("#city").addClass("invalid");
                    $("#information").append("<p>Город не выбран</p>");
                    validStat = false;
                } else {
                    $("#city").removeClass("invalid");
                }
                if ($("#price").val() < 0) {
                    $("#price").addClass("invalid");
                    $("#information").append("<p>Цена не может быть меньше нуля</p>");
                    validStat = false;
                } else {
                    $("#price").removeClass("invalid");
                }
                if ($("#price").val() == "") {
                    $("#price").addClass("invalid");
                    $("#information").append("<p>Введите цену</p>");
                    validStat = false;
                } else {
                    $("#price").removeClass("invalid");
                }
                return validStat;
            };
            $scope.validationAfter = function () {

            };
            $scope.createAttr = function (a_name, a_ru_name, a_type) {
                if (a_type == 1) {
                    $("div.attributes table.table-params tbody").append(
                        '<tr>' +
                        '<td>' +
                        a_ru_name +
                        '</td>' +
                        '<td>' +
                        '<input type="text" ng-model="object.' + a_name + '" id="' + a_name + '"/>' +
                        '</td>' +
                        '</tr>'
                    );
                } else if (a_type == 3) {
                    //$("div.attributes table.table-params tbody").append(
                    //    '<tr>' +
                    //        '<td>' +
                    //            a_ru_name+
                    //        '</td>'+
                    //        '<td>' +
                    //'<input type="file" ng-model="object.'+a_name+'" multiple="multiple" id="'+a_name+'"/>' +
                    //'</td>'+
                    //'</tr>'
                    //);
                    //$('div.attributes table.table-params tbody tr:last td:last').append($compile('<input type="text" ng-model="dasty">{{dasty}}')(scope));
                    //scope.$apply();
                } else if (a_type == 4) {
                    $("div.attributes table.table-params tbody").append(
                        '<tr>' +
                        '<td>' +
                        a_ru_name +
                        '</td>' +
                        '<td>' +
                        '<textarea ng-model="object.' + a_name + '" rows="5" id="' + a_name + '"></textarea>' +
                        '</td>' +
                        '</tr>'
                    );
                } else if (a_type == 5) {
                    $("div.attributes table.table-params tbody").append(
                        '<tr>' +
                        '<td>' +
                        a_ru_name +
                        '</td>' +
                        '<td>' +
                        '<input type="number" ng-model="object.' + a_name + '" id="' + a_name + '"/>' +
                        '</td>' +
                        '</tr>'
                    );
                } else if (a_type == 6) {
                    $("div.attributes table.table-params tbody").append(
                        '<tr>' +
                            '<td>' +
                                a_ru_name+
                            '</td>'+
                            '<td>' +
                                '<input type="number" ng-model="object.'+a_name+'" id="'+a_name+'"/>' +
                            '</td>'+
                        '</tr>'
                    );
                }else if(a_type==6){
                    var cityInput =
                        $('<tr>' +
                            '<td>' +
                                a_ru_name+
                            '</td>'+
                            '<td>' +
                                '<input type="text" id="' + a_name + '">' +
                            '</td>'+
                          '</tr>');

                    $("div.attributes table.table-params tbody").append(cityInput);

                    var options = {
                        types: ['(cities)']
                    };

                    var autocomplete = new google.maps.places.Autocomplete($('#' + a_name)[0], options);
                }else if (a_type==8) {
                    $("div.attributes table.table-params tbody").append(
                        '<tr>' +
                            '<td>' +
                                a_ru_name +
                            '</td>'+
                            '<td>' +
                                '<input id="addr-input" class="controls" type="text" placeholder="Введите адрес">' +
                                '<div id="map" style="width: 500px; height:300px"></div>' +
                                '<input id="clear-markers" type="button" value="Удалить маркер" width="100px">' +
                            '</td>'+
                        '<td>' +
                        a_ru_name +
                        '</td>' +
                        '<td>' +
                        '<select id="' + a_name + '" ng-model="object.' + a_name + '" required ng-options="n for n in ' + a_name + '">' +
                        '</select>' +
                        '</td>' +
                        '</tr>'
                    );
                    if (a_name == 'city') {
                        $("div.attributes select#city").append(
                            '<option disabled value="">Выберите ' + a_ru_name.toLowerCase() + '</option>');
                        for (var i = 0; i < $scope.city.length; i++) {
                            $("div.attributes select#city").append('<option value="' + $scope.city[i] + '">' + $scope.city[i] + '</option>');
                        }
                    }
                    ;
                    if (a_name == 'vip_status') {
                        $("div.attributes select#vip_status").append('<option value="">Выбрать VIP-статус</option>' +
                            '<option value="Gold">Золотой</option>' +
                            '<option value="Platinum">Платиновый</option>');
                        $("div.attributes tbody tr:last td:last").append("<p>Если хотите реализовать свое объявление " +
                            "быстрее, воспользуйтесь услугой VIP-статус.</p>");

                    }
                    ;
                } else if (a_type == 8) {
                    $("div.attributes table.table-params tbody").append(
                        '<tr>' +
                        '<td>' +
                        a_ru_name +
                        '</td>' +
                        '<td>' +
                        '<input id="addr-input" class="controls" type="text" placeholder="Введите адрес">' +
                        '<div id="map" style="width: 500px; height:300px"></div>' +
                        '</td>' +
                        '</tr>'
                    );
                    initMap();
                }
            };

            $scope.city = [
                "Абакан",
                "Альметьевск",
                "Анадырь",
                "Анапа",
                "Архангельск",
                "Астрахань",
                "Барнаул",
                "Белгород",
                "Беслан",
                "Бийск",
                "Биробиджан",
                "Благовещенск",
                "Бологое",
                "Брянск",
                "Великий Новгород",
                "Великий Устюг",
                "Владивосток",
                "Владикавказ",
                "Владимир",
                "Волгоград",
                "Вологда",
                "Воркута",
                "Воронеж",
                "Гатчина",
                "Гдов",
                "Геленджик",
                "Горно-Алтайск",
                "Грозный",
                "Гудермес",
                "Гусь-Хрустальный",
                "Дзержинск",
                "Дмитров",
                "Дубна",
                "Ейск",
                "Екатеринбург",
                "Елабуга",
                "Елец",
                "Ессентуки",
                "Златоуст",
                "Иваново",
                "Ижевск",
                "Иркутск",
                "Йошкар-Ола",
                "Казань",
                "Калининград",
                "Калуга",
                "Кемерово",
                "Кисловодск",
                "Комсомольск-на-Амуре",
                "Котлас",
                "Краснодар",
                "Красноярск",
                "Курган",
                "Курск",
                "Кызыл",
                "Лениногорск",
                "Ленск",
                "Липецк",
                "Луга",
                "Любань",
                "Люберцы",
                "Магадан",
                "Майкоп",
                "Махачкала",
                "Миасс",
                "Минеральные Воды",
                "Мирный",
                "Москва",
                "Мурманск",
                "Муром",
                "Мытищи",
                "Набережные Челны",
                "Надым",
                "Нальчик",
                "Назрань",
                "Нарьян-Мар",
                "Находка",
                "Нижневартовск",
                "Нижнекамск",
                "Нижний Новгород",
                "Нижний Тагил",
                "Новокузнецк",
                "Новосибирск",
                "Новый Уренгой",
                "Норильск",
                "Обнинск",
                "Октябрьский",
                "Омск",
                "Оренбург",
                "Орехово-Зуево",
                "Орёл",
                "Пенза",
                "Пермь",
                "Петрозаводск",
                "Петропавловск-Камчатский",
                "Подольск",
                "Псков",
                "Пятигорск",
                "Ростов-на-Дону",
                "Рыбинск",
                "Рязань",
                "Салехард",
                "Самара",
                "Санкт-Петербург",
                "Саранск",
                "Саратов",
                "Северодвинск",
                "Смоленск",
                "Соль-Илецк",
                "Сочи",
                "Ставрополь",
                "Сургут",
                "Сыктывкар",
                "Тамбов",
                "Тверь",
                "Тобольск",
                "Тольятти",
                "Томск",
                "Туапсе",
                "Тула",
                "Тында",
                "Тюмень",
                "Улан-Уде",
                "Ульяновск",
                "Уфа",
                "Хабаровск",
                "Ханты-Мансийск",
                "Чебаркуль",
                "Чебоксары",
                "Челябинск",
                "Череповец",
                "Черкесск",
                "Чистополь",
                "Чита",
                "Шадринск",
                "Шатура",
                "Шуя",
                "Элиста",
                "Энгельс",
                "Южно-Сахалинск",
                "Якутск",
                "Ярославль"
            ];

            function initMap() {
                var mapElem = $('#map');
                var clearButton = $('#clear-markers');
                var marker = null;
                var centerCoords;
                var scope = $scope;
                var geocoder = new google.maps.Geocoder;

                clearButton.on('click', function() {
                    if (marker != null) {
                        marker.setMap(null);
                        marker = null;
                    }

                    $('#city').prop('disabled', false);
                });

                var map = new google.maps.Map(mapElem.get(0), {
                    zoom: 16
                });

                if (navigator.geolocation) {
                    navigator.geolocation.getCurrentPosition(function (position) {
                        centerCoords = {
                            lat: position.coords.latitude,
                            lng: position.coords.longitude
                        };

                        map.setCenter(centerCoords);

                        google.maps.event.addListener(map, 'click', function (event) {
                            placeMarker(event.latLng, map);
                            setCityToParam(event.latLng);
                        });
                    });
                }

                var input = $('#addr-input')[0];
                var searchBox = new google.maps.places.SearchBox(input);
                map.controls[google.maps.ControlPosition.TOP_LEFT].push(input);

                map.addListener('bounds_changed', function () {
                    searchBox.setBounds(map.getBounds());
                });

                searchBox.addListener('places_changed', function () {
                    var places = searchBox.getPlaces();

                    if (places.length == 0) {
                        return;
                    }

                    // For each place, get the icon, name and location.
                    var bounds = new google.maps.LatLngBounds();
                    places.forEach(function (place) {
                        if (place.geometry.viewport) {
                            // Only geocodes have viewport.
                            bounds.union(place.geometry.viewport);
                        } else {
                            bounds.extend(place.geometry.location);
                        }
                    });
                    map.fitBounds(bounds);
                });

                function placeMarker(location, map) {
                    if (marker == null) {
                        marker = new google.maps.Marker({
                            position: location,
                            map: map
                        });
                    }

                    marker.setPosition(location);
                    $scope.map_coordinates = {lat: location.lat(), lon: location.lng()};
                    $('#city').prop('disabled', true);
                }

                function setCityToParam(location) {
                    geocoder.geocode({'location': location}, function(results, status) {
                        if (status === google.maps.GeocoderStatus.OK) {
                            $.each(results[0].address_components, function(i, component) {
                                if (component.types[0] == "locality") {
                                    $('#city').attr('value', component.long_name);
                                }
                            });
                        }
                    });
                }
            }

            $scope.fileCopy = function ($file, $event, $flow) {
                $scope.file = $file;
                var path = $($event.target).parent().attr('path');
                $scope.path = path;
                console.log("$scope.file" + $scope.file);
                console.log("$scope.path" + $scope.path);

                //$scope.uploader.flow.files[0].name = path; !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
                //$scope.uploader.flow.upload();
            };
        }
    ]);
})();
