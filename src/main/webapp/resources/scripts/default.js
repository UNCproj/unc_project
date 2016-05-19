(function(){
    var app = angular.module('default', ['flow']);

    $.urlParam = function(name){
        var results = new RegExp('[\?&]' + name + '=([^&#]*)').exec(window.location.href);
        return results[1] || 0;
    };

    app.controller('updateController', ['$scope', '$http', '$timeout',
        function($scope, $http, $timeout) {
            $scope.object = {};
            $scope.uploader = {};
            $scope.nullPassError = isOldPassEmpty;

            $scope.update = function() {
                var params = $scope.object;
                params.id = $.urlParam('id');

                if (isOldPassEmpty && ($scope.object.password == null || $scope.object.password.length() == null)) {
                    $scope.nullPassError = true;
                    return;
                }

                $http({
                    url: '/unc-project/uncupdate',
                    method: 'POST',
                    params: params
                })
                    .success(function (data) {

                    });
            };

            $scope.fileAdded = function ($file, $event, $flow) {
                $event.preventDefault();
                $scope.uploader.flow.files[0] = $file;
                var path = $($event.target).parent().attr('path');

                if (path == undefined || path == null || path.length <= 0) {
                    path = '/var/' + $.urlParam('id')+ '.png';

                    $http({
                        url: '/unc-project/uncupdate',
                        method: 'POST',
                        params: {
                            "id": $.urlParam('id'),
                            "user_pic_file": path
                        }
                    })
                        .success(function (data) {
                            var avatarImgElem = $('#' + paramAttrName);
                            avatarImgElem.attr('src', path + "?" + Date.now());
                        });
                }

                $scope.uploader.flow.files[0].name = path;
                $scope.uploader.flow.upload();
            };

            $scope.complete = function () {
                $http({
                    url: '/unc-project/imageResize',
                    method: 'POST',
                    params: {
                        "imageName": $scope.uploader.flow.files[0].name
                    }
                })
                    .success(function(data) {
                        var avatarImgElem = $('#' + paramAttrName);
                        avatarImgElem.attr('src', avatarImgElem.attr('src') + "?" + Date.now());
                        $scope.isAvatarChanged = true;
                        $timeout(function(){
                            $scope.isAvatarChanged = false;
                        }, 5000);
                    });
            };

            var initMap = function() {
                var mapElem = $('#map');
                var clearButton = $('#clear-markers');
                var marker = null;
                var centerCoords;
                var initialCoords = JSON.parse(mapElem.attr('coords'));
                var geocoder = new google.maps.Geocoder;

                clearButton.on('click', function () {
                    if (marker != null) {
                        marker.setMap(null);
                        marker = null;
                    }

                    $('#city').prop('disabled', false);
                });

                var map = new google.maps.Map(mapElem.get(0), {
                    zoom: 16
                });

                if (initialCoords != null) {
                    centerCoords = {
                        lat: initialCoords.lat,
                        lng: initialCoords.lon
                    };

                    marker = new google.maps.Marker({
                        position: centerCoords,
                        map: map
                    });

                    marker.setPosition(centerCoords);
                    map.setCenter(centerCoords);
                    $('#city').prop('disabled', true);
                }
                else {
                    if (navigator.geolocation) {
                        navigator.geolocation.getCurrentPosition(function (position) {
                            centerCoords = {
                                lat: position.coords.latitude,
                                lng: position.coords.longitude
                            };

                            map.setCenter(centerCoords);
                        });
                    }
                }

                google.maps.event.addListener(map, 'click', function (event) {
                    setCityToParam(event.latLng);
                });

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
                    $scope.object.map_coordinates = {lat: location.lat(), lon: location.lng()};
                }

                function setCityToParam(location) {
                    geocoder.geocode({'location': location}, function (results, status) {
                        if (status === google.maps.GeocoderStatus.OK) {
                            $.each(results[0].address_components, function (i, component) {
                                if (component.types[0] == "locality") {
                                    placeMarker(location, map);
                                    $scope.object.city = component.long_name;

                                    $('#city').prop('disabled', true);
                                    return false;
                                }
                            });
                        }
                    });
                }
            };

            initMap();
        }
    ]);
})();