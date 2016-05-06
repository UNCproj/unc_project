$(function() {
    var isOpenedOnce = false;
    var centerCoords;

    function initMap() {
        var mapElem = $('#map');
        var coordsAttr = mapElem.attr('coords');
        centerCoords = JSON.parse(coordsAttr);

        var map = new google.maps.Map(mapElem.get(0), {
            center: centerCoords,
            zoom: 16,
            disableDefaultUI: true,
            scrollwheel: false,
            draggable: false
        });

        var marker = new google.maps.Marker({
            position: centerCoords,
            map: map
        });

        mapElem.on('click', function(e) {
            if (!isOpenedOnce) {
                initExtendedMap();
            }
        });
    }

    function initExtendedMap() {
        isOpenedOnce = true;

        var mapExtendedElem = $('#mapExtended');

        var mapExtended = new google.maps.Map(mapExtendedElem.get(0), {
            center: centerCoords,
            zoom: 8,
            disableDefaultUI: true
        });

        var mainMarker = new google.maps.Marker({
            position: centerCoords,
            map: mapExtended
        });

        var closestMarkers = findClosestMarkers(getMarkers());

        $('#mapModal').on('shown.bs.modal', function (e) {
            google.maps.event.trigger(mapExtended, "resize");
            mapExtended.setCenter(coords);
            mapExtended.setZoom(16);
        });
    }

    function rad(x) {
        return x * Math.PI / 180;
    }

    function findClosestMarkers(centralMarker, markers) {
        const EARTH_RADIUS = 6371;
        const CLOSEST_MARKERS_COUNT = 10;

        var lat = centralMarker.position.lat;
        var lng = centralMarker.position.lng;

        for(var i = 0; i < markers.length; i++ ) {
            var mlat = markers[i].position.lat;
            var mlng = markers[i].position.lng;
            var dLat  = rad(mlat - lat);
            var dLong = rad(mlng - lng);
            var a = Math.sin(dLat/2) * Math.sin(dLat/2) +
                Math.cos(rad(lat)) * Math.cos(rad(lat)) * Math.sin(dLong/2) * Math.sin(dLong/2);
            var c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));

            markers[i].distance = EARTH_RADIUS * c;
        }

        markers.sort(function(a, b) {
            return a.distance - b.distance;
        });

        return markers.slice(0, CLOSEST_MARKERS_COUNT);
    }

    function getUrlParameter(sParam) {
        var sPageURL = decodeURIComponent(window.location.search.substring(1)),
            sURLVariables = sPageURL.split('&'),
            sParameterName,
            i;
        for (i = 0; i < sURLVariables.length; i++) {
            sParameterName = sURLVariables[i].split('=');
            if (sParameterName[0] === sParam) {
                return sParameterName[1] === undefined ? null : sParameterName[1];
            }
        }
    }

    function getMarkers() {
        var RADIUS_KM = 5;

        $.ajax({
                method: "POST",
                url: "advertsList",
                data: {
                    action: 'get_adverts_geo_query',
                    adCategoryId: uncObjectType,
                    adCenterCoords: centerCoords,
                    radius: RADIUS_KM
                }
            })
            .done(function(data) {
                var markers = [];

                $.each(data, function(index, value) {
                    markers[index] = new google.maps.Marker({
                        position: value
                    });
                });
            })
            .fail(function() {
                return [];
            });
    }

    initMap();
});
