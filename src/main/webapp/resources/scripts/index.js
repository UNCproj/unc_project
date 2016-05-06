(function() {
    $.urlParam = function(name){
        var results = new RegExp('[\?&]' + name + '=([^&#]*)').exec(window.location.href);

        if (results == null) {
            return -1;
        }

        results[1] = decodeURIComponent(results[1]);

        return results[1] || -1;
    };

    var app = angular.module('mainPage', ['ngAnimate', 'ui.bootstrap']);

    app.controller('searchController', ['$scope', '$http',
        function($scope, $http) {
            $scope.DEFAULT_ADS_COUNT = 10000;

            $scope.searchPhrase = "";
            $scope.searchResultsSortingParam = "name";
            $scope.searchResultsSortingOrder = "asc";
            $scope.foundedAds = [];
            $scope.foundedAdsCount = 0;
            $scope.resultsLoaded = false;
            $scope.searchButtonClicked = false;
            $scope.defaultCategoryChanged = false;
            $scope.enteredCategories = [];
            $scope.searchAttributes = [];

            $scope.selectedCategory = {
                id: 4,
                name: "Все категории",
                subCategories: [],
                attributes: [],
                adverts: []
            };

            $scope.search = function(notFromGUI) {
                $scope.resultsLoaded = false;
                $scope.searchButtonClicked = $scope.searchButtonClicked || !notFromGUI;
                $scope.foundedAds = [];
                $scope.foundedAdsCount = -1;

                var params = {
                    "action": "get_adverts_count",
                    "adsStartingNum": 0,
                    "adsCount": $scope.DEFAULT_ADS_COUNT,
                    "adNamePattern": $scope.searchPhrase,
                    "additionalAttributes": $scope.searchAttributes
                };

                if ($scope.selectedCategory.id === -1) {
                    params.adCategoryName = $scope.selectedCategory.name;
                }
                else  {
                    params.adCategoryId = $scope.selectedCategory.id;
                }

                $http({
                    url: 'advertsList',
                    method: 'POST',
                    params: params
                })
                    .success(function(data) {
                        $scope.foundedAdsCount = data;
                    });
            };

            $scope.loadParentCategories = function(categoryId, categoryName) {
                var params = {"action": "get_parent_categories"};

                if (categoryId !== -1) {
                    params.adCategoryId = categoryId;
                }
                else {
                    params.adCategoryName = categoryName;
                }

                $http({
                    url: 'advertsList',
                    method: 'POST',
                    params: params
                })
                    .success(function (data) {
                        $scope.enteredCategories = data;
                    });
            };

            var catName = $.urlParam('categoryName');

            if (catName !== -1) {
                $scope.selectedCategory.id = -1;
                $scope.selectedCategory.name = catName;
                $scope.search();
            }
            else {
                $scope.search(true);
            }
        }
    ]);

    app.controller('navBarController', ['$scope', '$http',
        function($scope, $http) {
            $scope.selectCategory = function(catId, catName){
                $scope.selectedCategory.id = catId;
                $scope.selectedCategory.name = catName;

                $scope.loadParentCategories($scope.selectedCategory.id, $scope.selectedCategory.name);
                $scope.$parent.search();
            };
        }
    ]);

    app.controller('searchFormController', ['$scope', '$http',
        function($scope, $http) {
            $scope.selectedLevelCategories = [];
            $scope.foundedHelperAds = [];

            $scope.$watch('enteredCategories', function() {
                var selectedCategoryParent = $scope.enteredCategories[$scope.enteredCategories.length - 2];
                $scope.loadSelectedLevelCategories(selectedCategoryParent != null ? selectedCategoryParent : 'advert');
            });

            $scope.selectCategory = function(catId, catName){
                $scope.selectedCategory.id = catId;
                $scope.selectedCategory.name = catName;

                $scope.loadParentCategories($scope.selectedCategory.id, $scope.selectedCategory.name);
                $scope.$parent.defaultCategoryChanged = true;
                $scope.search();
            };

            $scope.loadSelectedLevelCategories = function (selectedCategoryName) {
                var params = {"action": "get_first_lvl_categories"};

                params.adCategoryName = selectedCategoryName;

                $http({
                    url: 'advertsList',
                    method: 'POST',
                    params: params
                })
                    .success(function (data) {
                        $scope.selectedLevelCategories = data;
                    });
            };

            $scope.loadParentCategories($scope.selectedCategory.id, $scope.selectedCategory.name);
        }
    ]);

    app.controller('typeaheadController', ['$scope', '$http',
        function($scope, $http) {
            $scope.typeAhead = function(val) {
                return $http({
                            url: 'advertsList',
                            method: 'POST',
                            params: {
                                "action": "get_adverts_autocomplete",
                                "adCategoryId": $scope.selectedCategory.id,
                                "adsStartingNum": 0,
                                "adNamePattern": val,
                                "additionalAttributes": $scope.searchAttributes
                            }
                        })
                            .then(function(response) {
                                return response.data;
                            });
            };
        }
    ]);

    app.controller('categoriesController', ['$scope', '$http',
        function($scope, $http) {
            $scope.ATTR_COLS_NUM = 3;

            $scope.getColumnsNum = function() {
                return new Array($scope.ATTR_COLS_NUM);
            };

            $scope.getRowsNum = function() {
                return Array(Math.ceil($scope.selectedCategory.subCategories.length / $scope.ATTR_COLS_NUM));
            };

            $scope.$watch('selectedCategory.id', function() {
                $scope.loadSubCategories();
            });

            $scope.loadSubCategories = function() {
                var params = {"action": "get_first_lvl_categories"};

                if ($scope.selectedCategory.id != -1) {
                    params.adCategoryId = $scope.selectedCategory.id
                }
                else {
                    params.adCategoryName = $scope.selectedCategory.name
                }

                $http({
                    url: 'advertsList',
                    method: 'POST',
                    params: params
                })
                    .success(function (data) {
                        $scope.selectedCategory.subCategories = data;
                    });
            };
        }
    ]);

    app.controller('attributesController', ['$scope', '$http',
        function($scope, $http) {
            $scope.isCollapsed = true;

            $scope.changeCollapsed = function() {
                $scope.isCollapsed = !$scope.isCollapsed;
            };

            $scope.$watch('selectedCategory.id', function() {
                $http({
                    url: 'advertsList',
                    method: 'POST',
                    params: {
                        "action": "get_adverts_attributes",
                        "adCategoryId": $scope.selectedCategory.id
                    }
                })
                    .success(function (data) {
                        $scope.searchAttributes.length = 0;

                        for (var i = 0; i < data.length; i++) {
                            data[i][5] = i;
                            $scope.searchAttributes.push({
                                name: data[i][0],
                                type: data[i][3],
                                values: []
                            });
                        }

                        $scope.$parent.$parent.selectedCategory.attributes = data;
                    });
            });
        }
    ]);

    app.directive('attributeView', function() {
        return {
            restrict: 'A',
            scope: {
                attrName: "=name",
                attrRuName: "=runame",
                attrType: "=type",
                attrNum: "=num",
                searchAttrs: "=attrs"
            },
            controller: ['$scope', '$http',
                function($scope, $http) {
                    if ($scope.attrType == 'discrete_multi') {

                        $scope.multiInputItems = [{
                            num: 0,
                            placeholder: 'Введите значение'
                        }];

                        $scope.addInputItem = function () {
                            var lastValue = $scope.searchAttrs[$scope.attrNum].values[$scope.multiInputItems.length - 1];

                            if (lastValue != null && lastValue != '') {
                                $scope.multiInputItems.push({
                                    num: $scope.multiInputItems.length,
                                    placeholder: 'Введите значение'
                                });
                            }
                        };

                        $scope.removeInputItem = function (valueNum) {
                            if ($scope.multiInputItems.length > 1) {
                                $scope.multiInputItems.splice(valueNum, 1);
                            }

                            $scope.searchAttrs[$scope.attrNum].values[valueNum] = "";
                        };
                    }
                    else if ($scope.attrType == 'date') {
                        $scope.DATE_FORMAT = 'dd.MM.yyyy';
                        $scope.fromDatePickerOpened = false;
                        $scope.toDatePickerOpened = false;

                        $scope.openFrom = function() {
                            $scope.fromDatePickerOpened = true;
                        }
                    }

                    $scope.typeAhead = function(attrName, attrValue) {
                        return $http({
                            url: 'advertsList',
                            method: 'POST',
                            params: {
                                "action": "get_attributes_autocomplete",
                                "adsStartingNum": 0,
                                "attrName": attrName,
                                "attrValuePattern": attrValue
                            }
                        })
                            .then(function(response) {
                                return response.data;
                            });
                    };
                }
            ],
            templateUrl: 'directives/attribute-view.html'
        };
    });

    app.controller('searchResultsController', ['$scope', '$http',
        function($scope, $http) {
            const ADS_PER_PAGE = 10;

            $scope.resultsLoaded = true;
            $scope.activePageNum = 0;
            $scope.pagesCount = 0;
            $scope.displayedOrderingAttr = "Сортировка";

            $scope.$watch('foundedAdsCount', function() {
                $scope.foundedAds = {};

                if ($scope.foundedAdsCount == 0) {
                    $scope.foundedAds[0] = {};
                    $scope.foundedAds[0].name = "По Вашему запросу ничего не найдено";
                    $scope.resultsLoaded = true;
                    $scope.pagesCount = 0;
                }
                else if ($scope.foundedAdsCount > 0) {
                    $scope.pagesCount = Math.ceil($scope.foundedAdsCount / ADS_PER_PAGE.toFixed(2));
                    $scope.makePageActive(0);
                }
            });

            $scope.makePageActive = function(pageNum) {
                $scope.resultsLoaded = false;
                $scope.activePageNum = pageNum;

                var params = {
                    "action": "get_adverts",
                    "adsStartingNum": $scope.activePageNum * ADS_PER_PAGE,
                    "adsCount": ADS_PER_PAGE,
                    "adNamePattern": $scope.searchPhrase,
                    "sortingParam": $scope.searchResultsSortingParam,
                    "sortingOrder": $scope.searchResultsSortingOrder,
                    "additionalAttributes": $scope.searchAttributes
                };

                $scope.$parent.searchPhraseBuffer = $scope.$parent.searchPhrase;

                if ($scope.selectedCategory.id === -1) {
                    params.adCategoryName = $scope.selectedCategory.name;
                }
                else  {
                    params.adCategoryId = $scope.selectedCategory.id;
                }

                $http({
                    url: 'advertsList',
                    method: 'POST',
                    params: params
                })
                    .success(function(data) {
                        $scope.foundedAds = data;
                        $scope.resultsLoaded = true;
                    });
            };

            $scope.getPages = function() {
                return new Array($scope.pagesCount);
            };

            $scope.redirToAdvertPage = function(advId) {
                window.location = "unc_object.jsp?id=" + advId;
            };

            $scope.changeSortingParam = function(runame, name) {
                $scope.displayedOrderingAttr = runame;
                $scope.$parent.searchResultsSortingParam = name;

                $scope.search(true);
            };

            $scope.changeSortingOrder = function(order) {
                $scope.$parent.searchResultsSortingOrder = order;

                $scope.search(true);
            };
        }
    ]);
    app.controller('vipAdvertsController', ['$scope', '$http',
        function($scope, $http){
            $scope.vipAds = [];

            $scope.loadVipAdverts = function(){
                $http({
                    url: 'load-vip-adverts',
                    method: 'POST',
                    params: {
                        "type": 'advert'
                    }
                })
                    .success(function(data) {
                        $scope.vipAds = data;
                    });
            };

            $scope.$watch('selectedCategory.id', function() {
                $scope.loadVipAdverts();
            });
        }
    ]);
})();

