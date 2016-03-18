(function() {
    $.urlParam = function(name){
        var results = new RegExp('[\?&]' + name + '=([^&#]*)').exec(window.location.href);

        if (results == null) {
            return -1;
        }

        return results[1] || -1;
    };

    var app = angular.module('mainPage', []);

    app.controller('searchController', ['$scope', '$http',
        function($scope, $http) {
            const ADS_PER_PAGE = 2;

            $scope.searchPhrase = "";
            $scope.selectedCategoryId = 4;
            $scope.selectedCategoryName = "Все категории";
            $scope.categories = [];
            $scope.foundedAds = [];
            $scope.resultsLoaded = true;
            $scope.activePageNum = 0;
            $scope.pagesCount = 0;
            $scope.isAdvanced = false;

            //advanced
            $scope.subCategories = [];

            $http({
                url: 'advertsList',
                method: 'POST',
                params: {
                    action: "get_first_lvl_categories",
                    adCategoryId: "4"
                }
            })
                .success(function(data) {
                    $scope.categories = data;
                });

            $scope.selectCategory = function(catId, catName){
                $scope.selectedCategoryId = catId;
                $scope.selectedCategoryName = catName;
            };

            $scope.search = function() {
                $scope.resultsLoaded = false;
                $scope.foundedAds = [];

                $http({
                    url: 'advertsList',
                    method: 'POST',
                    params: {
                        "action": "get_adverts_count",
                        "adCategoryId": $scope.selectedCategoryId,
                        "adsStartingNum": 0,
                        "adNamePattern": $scope.searchPhrase
                    }
                })
                    .success(function(data) {
                        var foundedAdsCount = data;

                        if (foundedAdsCount == 0) {
                            $scope.foundedAds[0] = {};
                            $scope.foundedAds[0].name = "По Вашему запросу ничего не найдено";
                            $scope.pages = new Array(0);
                            $scope.resultsLoaded = true;
                        }
                        else {
                            $scope.pagesCount = Math.ceil(foundedAdsCount / ADS_PER_PAGE.toFixed(2));
                            $scope.makePageActive(0);
                        }
                    });
            };

            $scope.makePageActive = function(pageNum) {
                $scope.resultsLoaded = false;
                $scope.activePageNum = pageNum;

                $http({
                    url: 'advertsList',
                    method: 'POST',
                    params: {
                        "action": "get_adverts",
                        "adCategoryId": $scope.selectedCategoryId,
                        "adsStartingNum": $scope.activePageNum * ADS_PER_PAGE,
                        "adsCount": ADS_PER_PAGE,
                        "adNamePattern": $scope.searchPhrase
                    }
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


            //advanced search methods
            $scope.setAdvanced = function() {
                $scope.isAdvanced = !$scope.isAdvanced;

                if ($scope.isAdvanced && $scope.subCategories.length == 0) {
                    $scope.subCategories[0] = {
                        id: 4,
                        name: 'Все категории',
                        subCategories: $scope.categories,
                        attributes: []
                    };

                    $http({
                        url: 'advertsList',
                        method: 'POST',
                        params: {
                            "action": "get_adverts_attributes",
                            "adCategoryId": 4
                        }
                    })
                        .success(function (data) {
                            $scope.subCategories[0].attributes = data;
                        });


                    if ($scope.selectedCategoryId != 4) {
                        $scope.subCategories[1] = {
                            id: $scope.selectedCategoryId,
                            name: $scope.selectedCategoryName,
                            subCategories: [],
                            attributes: []
                        };


                        $http({
                            url: 'advertsList',
                            method: 'POST',
                            params: {
                                "action": "get_first_lvl_categories",
                                "adCategoryId": $scope.selectedCategoryId
                            }
                        })
                            .success(function (data) {
                                $scope.subCategories[1].subCategories = data;
                            });

                        $http({
                            url: 'advertsList',
                            method: 'POST',
                            params: {
                                "action": "get_adverts_attributes",
                                "adCategoryId": $scope.selectedCategoryId
                            }
                        })
                            .success(function (data) {
                                $scope.subCategories[1].attributes = data;
                            });
                    }
                }
            }

            $scope.selectSubCategoryInAdvancedSearch = function(id, name) {

            }
        }
    ]);

    app.directive('attributeView', function() {
        return {
            restrict: 'E',
            scope: {
                attrName: "=name",
                attrType: "=type"
            },
            templateUrl: 'directives/attribute-view.html'
        };
    });

    app.controller('categoriesController', ['$scope', '$http',
        function($scope, $http) {
            $scope.categoryToShow = {
                id: -1,
                name: "",
                adverts: [],
                subCategories: []
            };

            $scope.enteredCategories = [];

            var catName = $.urlParam('categoryName');

            $scope.loadCategory = function(id, name) {
                var params = {"action": "get_first_lvl_categories"}

                if (id != -1) {
                    params.adCategoryId = id
                }
                else {
                    params.adCategoryName = name
                }

                $http({
                    url: 'advertsList',
                    method: 'POST',
                    params: params
                })
                    .success(function (data) {
                        $scope.categoryToShow.id = id;
                        $scope.categoryToShow.name = name;
                        $scope.categoryToShow.subCategories = data;
                        //window.history.pushState("", "", 'index.jsp?categoryName=' + name);

                        params.action = "get_adverts";

                        $http({
                            url: 'advertsList',
                            method: 'POST',
                            params: params
                        })
                            .success(function (data) {
                                $scope.categoryToShow.adverts = data;
                            });

                        var subcatIndex;
                        if (subcatIndex = $scope.enteredCategories.indexOf(name) > -1) {
                            $scope.enteredCategories = $scope.enteredCategories.slice(0, subcatIndex + 1);
                        }
                        else {
                            $scope.enteredCategories.push(name);
                        }
                    });
            };

            $scope.loadCategory(catName === -1 ? 4 : -1,
                catName === -1 ? "Все объявления" : catName);
        }
    ]);

    app.controller('lastAddedController', ['$scope', '$http',
        function($scope, $http){
            $scope.lastAdverts = [];
            $scope.loaded = false;

            $http({
                url: 'advertsList',
                method: 'POST',
                params: {
                    "action": "get_adverts",
                    "adCategoryId": 4,
                    "adsCount": 10
                }
            })
                .success(function(data) {
                    $scope.lastAdverts = data;
                    $scope.loaded = true;
                });
        }
    ]);
})();