(function() {
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
                    "action": "get_first_lvl_categories"
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

            $scope.setAdvanced = function() {
                $scope.isAdvanced = !$scope.isAdvanced;

                if ($scope.isAdvanced && $scope.subCategories.length == 0) {
                    $scope.subCategories[0] = {
                        id: 4,
                        name: 'Все категории',
                        subCategoriesIds: [],
                        subCategoriesNames: $scope.categories,
                        attributes: []
                    };

                    if ($scope.selectedCategoryId != 4) {
                        $scope.subCategories[1] = {
                            id: $scope.selectedCategoryId,
                            name: $scope.selectedCategoryName,
                            subCategoriesIds: [],
                            subCategoriesNames: [],
                            attributes: []
                        };
                    }

                    $http({
                        url: 'advertsList',
                        method: 'POST',
                        params: {
                            "action": "get_first_lvl_categories",
                            "adCategoryId": $scope.selectedCategoryId
                        }
                    })
                        .success(function(data) {
                            $scope.subCategories[1].subCategoriesIds = data;
                        });

                    $http({
                        url: 'advertsList',
                        method: 'POST',
                        params: {
                            "action": "get_first_lvl_categories",
                            "adCategoryId": $scope.selectedCategoryId
                        }
                    })
                        .success(function(data) {
                            $scope.subCategories[1].subCategoriesIds = data;
                        });
                }
            }
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