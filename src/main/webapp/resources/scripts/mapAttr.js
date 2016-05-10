$(function() {
    var isOpenedOnce = false;
    var mapExtended;
    var centerCoords;

    function initMap() {
        var mapElem = $('#map');
        var coordsAttr = mapElem.attr('coords');
        centerCoords = JSON.parse(coordsAttr);
        centerCoords = {lat: centerCoords["lat"], lng: centerCoords["lon"]};

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

        mapExtended = new google.maps.Map(mapExtendedElem.get(0), {
            center: centerCoords,
            zoom: 8,
            disableDefaultUI: true
        });

        var mainMarker = new google.maps.Marker({
            position: centerCoords,
            map: mapExtended
        });


        getMarkers();

        $('#mapModal').on('shown.bs.modal', function (e) {
            google.maps.event.trigger(mapExtended, "resize");
            mapExtended.setCenter(centerCoords);
            mapExtended.setZoom(16);
        });
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
                data = JSON.parse(data);

                $.each(data, function(index, value) {
                    var coords = value.map_coordinates;

                    if (coords.lat != centerCoords.lat || coords.lng != centerCoords.lon) {
                        var pinColor = "0000AA";
                        var pinImage = new google.maps.MarkerImage("http://chart.apis.google.com/chart?chst=d_map_pin_letter&chld=%E2%80%A2|" + pinColor,
                            new google.maps.Size(21, 34),
                            new google.maps.Point(0, 0),
                            new google.maps.Point(10, 34));

                        markers[index] = new google.maps.Marker({
                            position: {lat: coords["lat"], lng: coords["lon"]},
                            icon: pinImage,
                            map: mapExtended
                        });

                        var contentString = "<h3>"  + value.name + "</h3><span>Цена: " + value.price + "</span>";

                        var infowindow = new google.maps.InfoWindow({
                            content: contentString
                        });

                        markers[index].addListener('mouseover', function() {
                            infowindow.open(mapExtended, markers[index]);
                        });

                        markers[index].addListener('mouseout', function() {
                            infowindow.close(mapExtended, markers[index]);
                        });

                        markers[index].addListener('click', function() {
                            window.location.href = "unc_object.jsp?id=" + value.id;
                        });
                    }
                });
            });
    }

    initMap();
});
