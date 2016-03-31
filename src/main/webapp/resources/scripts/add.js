$(function(){
    $("ul.nav li a:first").tab("show");
    $("ul.nav li").addClass("the-tab");

    $("div.footlog p.small").hide();
});

(function(){
    var app = angular.module('add', []);

    $.urlParam = function(name){
        var results = new RegExp('[\?&]' + name + '=([^&#]*)').exec(window.location.href);
        return results[1] || 0;
    };

    app.controller('addController', ['$scope', '$http', '$timeout', '$document',
        function($scope, $http, $timeout) {
            $scope.object = {};
            $scope.object.type = $.urlParam('type');
            $scope.arrAttr = [];
            $scope.arrAttrIndex = 0;

            $("div#category-div-1 a").on("click",function(){
                $("div#category-div-1 a").removeClass("a-category-active");
                $(this).addClass("a-category-active");
                $scope.object.type1=$(this).text();
                $scope.loadTypes("type1");
            });

            $scope.loadTypes =  function(types){
                var type = $scope.object;
                if(types==='type1'){
                    $("#category-div-2").empty();
                    type.type2=null;
                    $("#category-div-3").empty();
                    type.type3=null;
                    $("#category-div-4").empty();
                    type.type4=null;
                }
                if(types==='type2'){
                    $("#category-div-3").empty();
                    type.type3=null;
                    $("#category-div-4").empty();
                    type.type4=null;
                }
                if(types==='type3'){
                    $("#category-div-4").empty();
                    type.type4=null;
                }
                $http({
                    url: '/unc-project/loadTypes',
                    method: 'POST',
                    params: type
                })
                    .success(function (data) {
                        console.log(types + ':' + data);
                        if(types=='type1' && data[0]!=null){
                            for(var i=0;i<data.length;i++){
                                $("div#category-div-2").append('<a class="list-group-item a-category">' + data[i]  +"</a>");
                            }
                            $("div#category-div-2 a").on("click",function(){
                                $("div#category-div-2 a").removeClass("a-category-active");
                                $(this).addClass("a-category-active");
                                $scope.object.type2=$(this).html();
                                $scope.loadTypes("type2");
                            });
                        }
                        if(types=='type2' && data[0]!=null){
                            for(var i=0;i<data.length;i++){
                                $("div#category-div-3").append('<a class="list-group-item a-category">'
                                    + data[i]  +"</a>");
                            }
                            $("div#category-div-3 a").on("click",function(){
                                $("div#category-div-3 a").removeClass("a-category-active");
                                $(this).addClass("a-category-active");
                                $scope.object.type3=$(this).html();
                                $scope.loadTypes("type3");
                            });
                        }
                        if(types=='type3' && data[0]!=null){
                            for(var i=0;i<data.length;i++){
                                $("div#category-div-4").append('<a class="list-group-item a-category">'
                                    + data[i]  +"</a>");
                                $("div#category-div-4").show();
                            }
                            $("div#category-div-4 a").on("click",function(){
                                $("div#category-div-4 a").removeClass("a-category-active");
                                $(this).addClass("a-category-active");
                                $scope.object.type4=$(this).html();

                                $scope.hideCategory();
                            });
                        }
                        if(data[0]==null){
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
                if($scope.object.type3==null) {
                    $("div#category label").append('<div class="col-md-12"><p class="select-category">' + $scope.object.type1 +
                        ' / ' + $scope.object.type2 + '</p></div>');
                } else if($scope.object.type4==null){
                    $("div#category label").append('<div class="col-md-12"><p class="select-category">' +
                        $scope.object.type1 + ' / ' + $scope.object.type2 + ' / ' + $scope.object.type3 + '</p></div>');
                } else {
                    $("div#category label").append('<div class="col-md-12"><p class="select-category">' +
                        $scope.object.type1 + ' / ' + $scope.object.type2 + ' / ' + $scope.object.type3 + ' / '
                        + $scope.object.type4 + '</p></div>');
                }
                $("div#category p.select-category").on('click',function(){
                    $("div#category p.select-category").empty();
                    $("div.attributes").empty();
                    $("div#category-div-1").show();
                    $("div#category-div-2").show();
                    $("div#category-div-3").show();
                    $("div#category-div-4").show();
                });
                $scope.loadAttrs();
            };
            $scope.loadAttrs = function(){
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

                        $scope.arrAttr=[];

                        for(var i=0; i<data.length;i++){
                            $scope.arrAttr[$scope.arrAttrIndex] = data[i].attrName;
                            $scope.arrAttrIndex++;
                            $scope.createAttr(data[i].attrName,data[i].attrNameRu,data[i].attrType);
                        }

                        $("div.table-pos div.attributes").append(
                            '<input type="button" class="button-style a-outline button-update" value="Добавить" id="btn-load"' +
                            'onclick="location.href=\'index.jsp\'">');


                        $("#btn-load").on('click',function(){
                            var params = {};
                            params.type = $scope.object.type;
                            params.type1 = $scope.object.type1;
                            params.type2 = $scope.object.type2;
                            params.type3 = $scope.object.type3;
                            params.type4 = $scope.object.type4;
                            params.name = $("#name").val();
                            for(var i=0;i<$scope.arrAttr.length;i++) {
                                if ($scope.arrAttr[i]!=null && $("#"+$scope.arrAttr[i]).val()!="") {
                                    params[$scope.arrAttr[i]] = $("#"+$scope.arrAttr[i]).val();
                                }
                            }
                            console.log(params);
                            $scope.postAdd(params);
                        });
                    });
            };

            $scope.postAdd = function(p){
                $http({
                    url: '/unc-project/uncadd',
                    method: 'POST',
                    params: p
                })
                    .success(function (data) {
                        console.log(data);
                    });
            };

            $scope.createAttr = function(a_name, a_ru_name, a_type){
                if(a_type==1){
                    $("div.attributes table.table-params tbody").append(
                        '<tr>' +
                            '<td>' +
                                a_ru_name+
                            '</td>'+
                            '<td>' +
                                '<input type="text" ng-model="object.'+a_name+'" id="'+a_name+'"/>' +
                            '</td>'+
                        '</tr>'
                    );
                }else if(a_type==3){
                    $("div.attributes table.table-params tbody").append(
                        '<tr>' +
                            '<td>' +
                                a_ru_name+
                            '</td>'+
                            '<td>' +
                                '<input type="file" ng-model="object.'+a_name+'" multiple="multiple" id="'+a_name+'"/>' +
                            '</td>'+
                        '</tr>'
                    );
                }else if(a_type==4){
                    $("div.attributes table.table-params tbody").append(
                        '<tr>' +
                            '<td>' +
                                a_ru_name+
                            '</td>'+
                            '<td>' +
                                '<textarea ng-model="object.'+a_name+'" rows="5" id="'+a_name+'"></textarea>' +
                            '</td>'+
                        '</tr>'
                    );
                }else if(a_type==5){
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
                    $("div.attributes table.table-params tbody").append(
                        '<tr>' +
                            '<td>' +
                                a_ru_name +
                            '</td>'+
                            '<td>' +
                                '<select id="'+a_name+'" ng-model="object.'+a_name+'" required ng-options="n for n in '+a_name+'">' +
                                '</select>' +
                            '</td>'+
                        '</tr>'
                    );
                    if(a_name=='city') {
                        $("div.attributes select#city").append(
                            '<option disabled value="">Выберите ' + a_ru_name.toLowerCase() +'</option>');
                        for (var i = 0; i < $scope.city.length; i++) {
                            $("div.attributes select#city").append('<option value="' + $scope.city[i] + '">'+$scope.city[i]+'</option>');
                        }
                    };
                    if(a_name=='vip_status'){
                        $("div.attributes select#vip_status").append('<option value="">Выбрать VIP-статус</option>'+
                            '<option value="Gold">Золотой</option>' +
                            '<option value="Platinum">Платиновый</option>');
                        $("div.attributes tbody tr:last td:last").append("<p>Если хотите реализовать свое объявление " +
                            "быстрее, воспользуйтесь услугой VIP-статус.</p>");

                    };
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


        }
    ]);
})();
