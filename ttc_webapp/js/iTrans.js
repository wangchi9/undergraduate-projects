/* name: Chi Wang  UTORid: wangchi9 student#: 998457931
   name: Yiming(Jeff) Jiao UTORid: jiaoyimi  student#: 998379446  */

var serviceURL = "https://mathlab.utsc.utoronto.ca/courses/cscc09f12/wangchi9/php/";  
var servicePHPURL = "http://www.utsc.utoronto.ca/~rosselet/cscc09f12/asn/services/";
var PHP_URL = "https://www.utsc.utoronto.ca/~rosselet/cscc09f12/asn/services/";

function StopList2StopDict(StopList) {
	/* convert from server dictionary-list format returned in JSON, e.g.:
	   [{_id: "cd1", band: "The Beatles", price: "22.00", title: "Sgt Pepper"},...]
	   to localStorage dictionary format, e.g.:
	   {cd1: {band: "The Beatles", price: "22.00", title: "Sgt Pepper"},...}
	   
	   The reason for this conversion is to confine generation of HTML
	   DOM code to one location, for both the CDList and Playlist pages.
	*/
	StopDict = {};
	$.each(StopList, function(index, stop) {
		console.log(index, stop, StopDict);  // just to demo Web Developer
		//CDDict[cd._id] = {'band':cd.band, 'price':cd.price, 'title':cd.title};
		StopDict[stop.id] = {'display_name':stop.display_name};
	});
	return StopDict;
}

function getRoutes() {
	/* Make a server-side CGI-Saxon-XSLT call to retrieve data from a
       server-side routes represented as XML data.  The
       XSLT script returns a JSON dictionary representation of the XML
       routes data.*/

	$.getJSON(serviceURL + "routes.php", function(data) {
	    var routes = data.items; 
	    $.each(routes, function(index, route) {				
		var routeItem = '<li><span>' + route.display_name + '<br>&nbsp;&nbsp<a href="#RoutePage" route_id="' + route.id  
		    + '" title="' + route.display_name + '" data-role="button"' 
		    + '" data-inline="true" data-mini="true" data-icon="arrow-r"' 
		    + ' data-theme="b" class="btn getroute" data-transition="flip">Show Directions</a><form><input id="route'
			+  route.id + '" type="checkbox" data-inline="true" data-theme="b" value="MapStops">'
			+ '&nbsp;&nbsp &nbsp;&nbsp&nbsp;&nbspMap Stops  &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; '
			+ '<input id="vehicle' + route.id + '" type="checkbox" data-inline="true" data-theme="b" value="MapVehicles">&nbsp;&nbsp &nbsp;&nbsp&nbsp;&nbspMap Vehicles </form></span></li>';
		$('#RoutesList').append(routeItem);}); 
	$('#RoutesList').listview('refresh');});
};

function getRuns(routeid) {
	/* routeid: the id of the route taht we get directions for.
	Make a server-side PHP call to retrieve data from a
       server-side directions represented as XML data.  The
       XSLT script returns a JSON dictionary representation of the XML
       runs data.*/

	$('#RunsList').empty();
	$('#routeheader').html('route'+routeid);
	$.getJSON(serviceURL + "runs.php?id=" + routeid, function(data) {
	var runs = data.items; 
	$.each(runs, function(index, run) {				
	    var runItem = '<li><a href="#RoutedetailPage" route_id="' + run.route_id  + '" run_id="'
		+ run.id + '" data-role="button" data-inline="true" data-mini="true" data-icon="arrow-r"'
		+ ' data-theme="b" class="btn getstop" data-transition="flip">'
		+ run.display_name + '</a></li>';
	    $('#RunsList').append(runItem);}); 
	$('#RunsList').listview('refresh');});
};

function getStops(routeid, runid) {
	/* Make a server-side PHP call to retrieve data from a
       server-side stops represented as XML data.  The
       XSLT script returns a JSON dictionary representation of the XML
       stops data.*/

	$('#StopsList').empty();
	$.getJSON(serviceURL + "stops.php?routeid=" + routeid + '&runid=' + runid, function(data) {
	var stops = data.items; 
	$.each(stops, function(index, s) {				
	    var stopItem = '<li><span>' + s.display_name + '<br /><a href="/a2/servlet/SaveStop" stop_id="' + s.id  
		+ '" display_name="' + s.display_name + '" data-role="button"' 
		+ ' data-inline="true" data-mini="true" data-icon="star"'
		+ ' data-theme="b" class="save_s" data-transition="flip">Save Stop</a>'
		+ '&nbsp;&nbsp;<a href="#ArrivalsPage" stop_id="' + s.id + '" data-role="button"'
		+ ' data-inline="true" data-mini="true" data-icon="arrow-r"'
		+ ' data-theme="b" class="btn predict" data-transition="flip">Predict Arrivals</a></span></li>';
	    $('#StopsList').append(stopItem);}); 
	$('#StopsList').listview('refresh');});
};

function getPredictions(stop_id) {
	/* Make a server-side PHP call to retrieve data from a
       server-side Predictions represented as XML data.  The
       XSLT script returns a JSON dictionary representation of the XML
       Predictions data.*/

	$('#ArrivalList').empty(); //clear the Prediction list everytime when calling the function
	$.getJSON(servicePHPURL + "getpredictions.php?id=" + stop_id, function(data) {
	var arrivals = data.items; 
	$.each(arrivals, function(index, arr) {				
	    var arrivalItem = '<li>' + arr.minutes + ' minutes</li>';
	    $('#ArrivalList').append(arrivalItem);}); 
	$('#ArrivalList').listview('refresh');});
};

// functions for save stops:

function retrieveLocalStorageDict(key) {
    var dict = localStorage[key];
    if (dict == null) {
        dict = {}; /* always return a valid dictionary object */
    }
    else {   /* localStorage values are strings - parse to JSON object */
        dict = JSON.parse(dict);
    };
    return dict;
}

function displayStopDict(StopDict,htmlList,options) {
    /* display, as a sequence of <li> list items, the contents of stops
       list mystopsList, appending the result to HTML element htmlList.  If
       options dictionary value delete is "true" then add a delete button
       to the generated listview items. */

    $(htmlList).empty();  // clear the HTML list before rebuilding it
    $.each(StopDict, function(key,stop) {
        var del = "";
        if (options["delete"] == true) {
	    del = '&nbsp;&nbsp;<a data-role="button" href="#" data-inline="true" data-mini="true" '
		+ 'data-icon="delete" data-theme="b" class="btn delStop" style="line-height:40%;" target="'
		+ key + '">delete</a>'
        };

	/* iterate through the stops list, building an HTML list
	   with attributes drawn from the dictionary fields. */
	$(htmlList).append('<li><span>' + stop.display_name + '<br /><a href="#ArrivalsPage" stop_id="' 
		+ stop.stop_id + '" display_name="' + stop.display_name +  '" data-role="button"'
		+ ' data-inline="true" data-mini="true" data-icon="arrow-r"'
		+ ' data-theme="b" class="btn predict" data-transition="flip">Prediction</a>' 
		+ del +'</span></li>');
    });
    /* refresh the browser view to show newly-inserted DOM elements */
    $(htmlList).listview('refresh');
}

function displayMystopsList(mystopsList,htmlList,options) {
    /* display, as a sequence of <li> list items, the contents of CD dictionary
       list CDDictList, appending the result to HTML element htmlList.  If
       options dictionary value delete is "true" then add a delete button
       to the generated listview items. */
    $(htmlList).empty();  // clear the HTML list before rebuilding it
    $.each(mystopsList, function(index,stop) {
        var del = "";
        if (options["delete"] == true) {
	    del = '&nbsp;&nbsp;<a data-role="button" href="" data-inline="true" data-mini="true" data-icon="delete" data-theme="b" class="btn delStop" style="line-height:40%;" target="' + stop.id + '">delete</a>'
        };
	/* iterate through the CD dictionary list, building an HTML list
	   with attributes drawn from the dictionary fields. */
		$(htmlList).append('<li><span>' + stop.display_name + '<br /><a href="#ArrivalsPage" stop_id="' 
		+ stop.id + '" display_name="' + stop.display_name + '" data-role="button"'
		+ ' data-inline="true" data-mini="true" data-icon="arrow-r"'
		+ ' data-theme="b" class="btn predict" data-transition="flip">Prediction</a>' 
		+ del +'</span></li>');
    });
    /* refresh the browser view to show newly-inserted DOM elements */
    $(htmlList).listview('refresh');
}


// Asst 3 map-page functionality 
var lat, lon = null;
/* Use HTML5 geolocation capability to provide location-based service */
function geoLocation() {
    if (navigator.geolocation) {  // try to get user's geoLocation
        navigator.geolocation.getCurrentPosition (function(position) {
          lat = position.coords.latitude;
          lon = position.coords.longitude;
        });
    }
    else {  // centre on UTSC
          lat = 43.78646;
          lon = -79.1884399;
    }
    /*  need user's geoLocation before drawing map, so block here
	until geoLocation is determined */
    if (lat==null || lon==null) {  // keep trying until geoLocation determined
	setTimeout(geoLocation, 500);
    }
    else {  // have geoLocation, now draw the map
	drawMap([],[]);
    }
}

var map = null;  // google map object
var stopsNearMe = [];  // stops within 1km of user's geolocation
var route_icons = ["img/blue.gif", "img/red.gif", "img/green.gif",
		"img/yellow.gif", "img/purple.gif", "img/cyan.gif",
		"img/black.gif", "img/orange.gif", "img/white.gif"];
// use dictionary for bus icons, so can look up by run direction 
var bus_icons = {"North": "img/bus_north.jpg", "East": "img/bus_east.jpg",
		"South": "img/bus_south.jpg", "West": "img/bus_west.jpg"}; 
    

/* draw the map, and add overlay markers for user-selected information */
function drawMap(stopRoutes,vehicleRoutes) {
 console.log(lat,lon);
    map = new google.maps.Map(document.getElementById('map'), {
      zoom: 15,
      center: new google.maps.LatLng(lat, lon),
      mapTypeId: google.maps.MapTypeId.ROADMAP
    });

    mapStops(stopsNearMe);  // add markers for stops near user
   // var stopRoutes = [510, 509];
    mapRouteStops(stopRoutes);  // add markers for user-selected route stops
    //var vehicleRoutes = [7, 95, 38];
    mapVehicles(vehicleRoutes);  // add markers for user-selected route vehicles
    // refresh vehicle markers every 30 seconds to show updated vehicle positions
    setInterval(function() { mapVehicles(vehicleRoutes); }, 30000); 
};

/* add map markers for parameter list of stops with geolocation near the user */
function mapStops(stopList) {
    var infowindow = new google.maps.InfoWindow();
	$.each(stopList, function(index, stop) {
            var i;
	    /* place map markers for parameter stops - make them clickable to
	       obtain predicted arrival times for clicked stop */
	    text = stop.display_name + ', route: ' 
			+ stop.routeid + ', direction: ' + stop.run_display_name
			+ ', distance: ' + (stop.distance*1.61*1000).toFixed(0) + 'm';
            var marker = new google.maps.Marker({
                position: new google.maps.LatLng(stop.latitude, stop.longitude),
            	map: map,
                title: text,
		icon: "img/me.gif"  // use a distinct icon for nearby stops
            });
    
            google.maps.event.addListener(marker, 'click', (function(marker) {
                return function() {
		  // Obtain real-time prediction data for user-clicked stop
		  $.getJSON(PHP_URL + 'getpredruns.php?id=' + stop.id, function(data) {
		      // load content of map info-window that opens on click
                      var predList = '<span>' + stop.display_name
                            + '<br/>Vehicles arriving in: ';
                      $.each(data.items, function(index, prediction) {
	    	          var dirName;  // name to display for vehicle direction
	    	          // some vehicles have no run display_name, or their run # has no defined name
	    	          if (prediction.run_name == null || prediction.run_name == undefined) {
	    		      dirName = "#"+prediction.run_id;
	    	          }
	    	          else {  // have a run name, so display it
	    		      dirName = prediction.run_name; 
	    	          };
                          predList += prediction.minutes + ' (' + dirName + '), ';
                      });
                      predList += ' minutes</span>';
                      infowindow.setContent(predList);
                      infowindow.open(map, marker);
                  });       // getJSON prediction data
               }
           })(marker));
       });
};

/* add markers for stops on the routes in parameter routeList */
function mapRouteStops(routeList) {
    var infowindow = new google.maps.InfoWindow();
    // iterate through each route in routeList
    $.each(routeList, function(index, route) {
      // for each route, retrieve its run-list
      $.getJSON(PHP_URL + "getruns.php?id=" + route, function(data) {
        $.each(data.items, function(runidx, run) {
	  // for each run, retrieve its stop-list
	  $.getJSON(PHP_URL + "getstops.php?routeid=" + route + "&runid=" 
						+ run.id, function(data) {
	    // for each stop, create a marker with a run-coded icon
            $.each(data.items, function(index, stop) {
              var i;
	      /* place markers on map for nearby stops - make them clickable to
	         obtain predicted arrival times for clicked stop */
	      var icon = route_icons[runidx];
	      text = "Stop: " + stop.display_name + ', Route: ' 
			+ route + ', Direction: ' + run.display_name;
              var marker = new google.maps.Marker({
                position: new google.maps.LatLng(stop.latitude, stop.longitude),
            	map: map,
                title: text,
		icon: icon
              });
    
              google.maps.event.addListener(marker, 'click', (function(marker) {
                return function() {
		  // Obtain real-time prediction data for user-clicked stop
		  $.getJSON(PHP_URL + 'getpredruns.php?id=' + stop.id, function(data) {
		    if (data.items != null) {
		      // load content of map info-window that opens on click
                      var predList = '<span>' + stop.display_name
                            + '<br/>Vehicles arriving in: ';
                      $.each(data.items, function(index, prediction) {
                          predList += 
                            prediction.minutes + ' (' + prediction.run_name + '), ';
                      });
                      predList += ' minutes</span>';
                      infowindow.setContent(predList);
		    }
		    else {
			infowindow.setContent("Sorry, no prediction data available at this time; please try again later");
		    }
                    infowindow.open(map, marker);
                  });       // getJSON prediction data
                }
             })(marker));
         });  // each stop
       });  // getJSON stops
     });  // each run
   });  // getJSON runs
 });  // each route
};

var markerArray = [];  // remember vehicle markers, so we can remove/refresh them

/* add markers for vehicles on the routes in parameter routeList */
function mapVehicles(routeList) {
    /* clear current vehicle markers before updating with new ones, else end
	up with vehicle markers in old and new locations. */
    while (markerArray.length > 0) {
          var m =  markerArray.pop();
          m.setMap(null);
    }
    /* add vehicle markers for user-selected routeList */
    $.each(routeList, function(index, route) {
      /* retrieve location data for vehicles on this route */
      $.getJSON(PHP_URL + "getvehruns.php?id=" + route, function(data) {
        var vehList = data.items;  // extract the list of vehicles
	/* create a marker for each vehicle */
	$.each(vehList, function(index, vehicle) {
            var marker;
	    var dirName;  // name to display for vehicle direction
	    // some vehicles have no run names, or their run # has no defined name
	    if (vehicle.run_name == null || vehicle.run_name == undefined) {
		dirName = "#"+vehicle.run_id;
	    }
	    else {  // have a run name, so display it
		dirName = vehicle.run_name; 
	    };
	    text = 'direction: ' + dirName + ', vehicle#: ' + vehicle.id;
    	    marker = new google.maps.Marker({
                position: new google.maps.LatLng(vehicle.latitude, vehicle.longitude),
                map: map,
                title: text,
		icon: bus_icons[vehicle.dir_name] 
            });
	    marker.setMap(map);
	    markerArray.push(marker);
	});
      });
    });
};




////////////////////////////////////////////

$('a.register').live("click", function(event) {
    fromPage = $('.ui-page-active').attr('id');
});


$('a.login').live("click", function(event) {
    // remember the page from which the user clicked the Login button
    fromPage = $('.ui-page-active').attr('id');
    });

$('a.logout').live("click", function(event) {
    // remember the page from which the user clicked the Login button
    fromPage = $('.ui-page-active').attr('id');
    console.log(fromPage);
    $('.login').toggleClass('logout');
    $('.login .ui-btn-text').text("Login");
    $.ajax({
 	url: '/a2/servlet/Controller',
  	type: 'POST',
    	dataType: 'json',
    	data: {"action": "logout", "page": fromPage},
		success: function(data, status) {
       	    // displayPage = data.page;
	    // never executes, since call does not return
	    $.mobile.changePage(fromPage);
console.log(fromPage, "logged out");
	}
    });
});

$('#RoutesPage').live("pageinit", function(event) {
	/* When the page with id Routes is initialized,
	   invoke the getRoutes() function 
	*/
	geoLocation(); 
	getRoutes();
	$('#login').on('submit', function(e) {
        e.preventDefault();
        var loginData = $("#login").serializeArray();
        loginData.push({ name: "page", value: "#"+fromPage });
        loginData.push({ name: "action", value: "login" });
        var $form = $('#login');
        $.ajax({
	    url: '/a2/servlet/Controller?action=login',//$form.attr('action'),
	    type: $form.attr('method'),
	    dataType: 'json',
	    data: loginData,
	    success: function(data, status) {
	        loggedIn = data.authenticated;
	        if (loggedIn == 'true') {
		    // Logout currently not implemented, also Logout does not change state
		    // when autologout occurs on server - need to test loggedIn state after
		    // each server request.
		    $('.login .ui-btn-text').html("Logout");
		    $('.login').toggleClass('logout');
		    userId = data.userId;
		}else {
		    $('#loginStatus').html('<h3 style="color:red">Login Failed, Please Try Again</h3>');
	        };
	        displayPage = data.page;
	        $.mobile.changePage(displayPage);
    	        return false;  // prevent default page-oriented submit action
        }, 
	    error: function(x,y,z) { console.log(y); }
        });
    });

	$('#register').on('submit', function(e) {
        e.preventDefault();
        var registerData = $("#register").serializeArray();
        registerData.push({ name: "page", value: "#"+fromPage });
        registerData.push({ name: "action", value: "register" });
        var $form = $('#register');
        $.ajax({
	    url: '/a2/servlet/Controller?action=register',//$form.attr('action'),
	    type: $form.attr('method'),
	    dataType: 'json',
	    data: registerData,
	    success: function(data, status) {
	        signUp = data.authenticated;
	        if (signUp == 'true') {
		    // Logout currently not implemented, also Logout does not change state
		    // when autologout occurs on server - need to test loggedIn state after
		    // each server request.
		    $('.login .ui-btn-text').html("Logout");
		    $('.login').toggleClass('logout');
		    userId = data.userId;
		}else {
		    $('#registerStatus').html('<h3 style="color:red">register Failed, Please Try Again</h3>');
	        };
	        displayPage = data.page;
	        $.mobile.changePage(displayPage);
    	        return false;  // prevent default page-oriented submit action
        }, 
	    error: function(x,y,z) { console.log(y); }
        });
    });
});

$('a.getroute').live("click", function(event) {
	var routeid = $(this).attr('route_id');  // get the routeid from the event link
	getRuns(routeid);
});

$('a.getstop').live("click", function(event) {
	var routeid = $(this).attr('route_id');  // get the routeid from the event link
	var runid = $(this).attr('run_id');  // get the runid from the event link
	getStops(routeid, runid);
});

$('a.save_s').live("click", function(event) {
	event.preventDefault();
    fromPage = $('.ui-page-active').attr('id');

	var stopid = $(this).attr('stop_id');  // get the stopid from the event link
	var stoptitle = $(this).attr('display_name');  // gets the stoptitle from the event link
	/* replace localStorage impl with Servlet impl ...
	var stoplist = retrieveLocalStorageDict('stoplist');
	if (stopid != null) {  // don't update if no stop is currently selected
		stoplist[stopid] = {'stop_id': stopid, 'display_name': stoptitle};  
		localStorage['stoplist'] = JSON.stringify(stoplist); // convert back to string
	}
	$.mobile.changePage('#MystopsPage');
	*/
	
	var StopData = new Array();
	var loginData = new Array();
	loginData.push({ name: "page", value: "#"+fromPage });
	loginData.push({ name: "action", value: "login" });
	console.log(loginData);
    if (stopid != null) {  // don't update if no CD is currently selected
        StopData.push({ name: "stopid", value: stopid });
        StopData.push({ name: "stop_title", value: stoptitle });
        StopData.push({ name: "action", value: "save" });
	console.log(StopData);
	$.ajax({
	    		url: '/a2/servlet/Controller',
	    		type: 'POST',
	    		dataType: 'json',
	    		data: StopData,
	    		success: function(data, status) {
	    			if(data!=null){
	        	    displayPage = data.page;
			console.log(displayPage);
			}
		}
	});	
	}
	$.mobile.changePage('#MystopsPage');
});

$('a.predict').live("click", function(event) {
	var stopid = $(this).attr('stop_id');  // get the stopid from the event link
	getPredictions(stopid);
});

// $('#MystopsPage').live("pageinit", function(event) {
//     /* When Mystops page is initialized, restore stoplist from localStorage. */
//     $('#mystopsList').listview();
//     //displayStopDict(retrieveLocalStorageDict('stoplist'),'#mystopsList',{delete:true});
//     $.getJSON('/a2/servlet/Controller?action=mystopsList', function(data) {
//         alert("aaabb");
//         console.log(data);
//         displayMystopsList(data,'#mystopsList',{delete:true});
//     });alert("aaabbbb");
// });

$('#MystopsPage').live("pageshow", function(event) {
    /* Whenever mystopsList page is viewed, restore stoplist from localStorage. */
    //displayStopDict(retrieveLocalStorageDict('stoplist'),'#mystopsList',{delete:true});
    //$('#mystopsList').listview();
    
    $.getJSON('/a2/servlet/Controller?action=mystopsList', function(data) {
        if(data!=null){
        console.log(data);
        displayMystopsList(data,'#mystopsList',{delete:true});
    	}
    });
});

$('a.delStop').live("click", function(event) {
    /* localStorage implementation from A1 ...
    var stopid = $(this).attr('target');  // gets the stop id from the link
    var stoplist = retrieveLocalStorageDict('stoplist');
    delete stoplist[stopid];
    localStorage['stoplist'] = JSON.stringify(stoplist);
    // update the UI to drop the containing <li> element
    $(this).closest('li.ui-li').remove();
    $('#mystopsList').listview('refresh');
    */

    /* note event.target and this are not always equiv - see this article
        for more details, incl performance implications */
    var stopid = $(this).attr('target');  // gets the CD id from the link
    $.ajax({
	url: '/a2/servlet/Controller?action=delete',
	type: 'POST',
	dataType: 'json',
	data: {id: stopid},
	success: function(data, status) {
    	    // update the UI to drop the containing <li> element
	console.log(data,status);
	}
    });
    // update the UI to drop the containing <li> element
    $(this).closest('li.ui-li').remove();
    $('#mystopsList').listview('refresh');
});







$('a.map_me').live("click", function(event) {
    /* stopsnearme.php extracts stops near user geolocation from Stops table
	and Runs table (run_display_name) */
    $.getJSON(PHP_URL + "stopsnearme.php?lat=" + lat + "&lon=" + lon, function(data) {
        stopsNearMe = data.items;  // extract the list of nearby stops
	mapStops(stopsNearMe);
    });
    $.mobile.changePage('#MapPage');
});


$('#MapPage').live("pageshow", function(event) {
   var stopRoutes = [];
    var vehicleRoutes = [];
$.getJSON(serviceURL + "routes.php", function(data) {
	    var routes = data.items; 
	    $.each(routes, function(index, route) {
		if($('#route'+route.id).is(':checked')){
			stopRoutes.push(route.id);
		}
		if($('#vehicle'+route.id).is(':checked')){
			vehicleRoutes.push(route.id);
		}
	});
});
  drawMap(stopRoutes,vehicleRoutes);  // redraw the map when page is shown
});


$('#InfoPage').live("pageshow", function(event) {
    $('#svc_alerts').empty();  // clear the HTML list before rebuilding it
    $.get(PHP_URL + "getalerts.php", function(data) {
	var ttcPage = data;
	var svcAlerts = $(ttcPage).find(".ttc-service-alert p").not('.alert-updated');
	$.each(svcAlerts, function(index, data) {
	    $('#svc_alerts').append(data);
	});
    });
});

