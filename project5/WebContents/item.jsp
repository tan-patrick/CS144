<!DOCTYPE html>
<html>
	<head>
		<title>Item Details</title>
		<style type="text/css"> 
		  html { height: 100% } 
		  body { height: 100%; margin: 0px; padding: 0px } 
		  #map_canvas { height: 100% } 
		</style> 
		
		
		<script type="text/javascript" 
				src="http://maps.google.com/maps/api/js?sensor=false"> 
		</script> 
		
		<script type="text/javascript"> 
		  function initialize() { 
			var latlng = new google.maps.LatLng(34.063509,-118.44541); 
			var myOptions = { 
			  zoom: 3, // default is 8  
			  center: latlng, 
			  mapTypeId: google.maps.MapTypeId.ROADMAP 
			}; 
			var map = new google.maps.Map(document.getElementById("map_canvas"), myOptions);
			var geocoder = new google.maps.Geocoder();
		  
			if (<%= request.getAttribute("lat") %> === "empty")
			{
			  var address = <%= request.getAttribute("loc") %>;
			  geocoder.geocode( { 'address': address}, function(results, status) {
				if (status == google.maps.GeocoderStatus.OK) {
				  map.setCenter(results[0].geometry.location);
				  map.setZoom(14);
				  var marker = new google.maps.Marker({
					  map: map,
					  position: results[0].geometry.location
				  });
				} else {
				  // alert('Geocode was not successful for the following reason: ' + status);
				}
			  });
			}
			else
			{
				var latlng = new google.maps.LatLng(<%= request.getAttribute("lat") %>, <%= request.getAttribute("lng") %>); 
				var myOptions = { 
				  zoom: 14, // default is 8  
				  center: latlng, 
				  mapTypeId: google.maps.MapTypeId.ROADMAP 
				}; 
				map = new google.maps.Map(document.getElementById("map_canvas"), 
					myOptions); 
			}
		  } 

		</script> 
		
	</head>
	<body onload="initialize()">
		<form action="/eBay/item" method="GET">
			Search another Item via ItemID:
			<input type="text" name="itemId" value="">
			<input type="submit" value="Submit">
		</form>
	
		<h1>Item Details</h1>
		
		<%= request.getAttribute("item") %>
		
		<h1>Map Location</h1>
		<div id="map_canvas" style="width:100%; height:100%"></div> 
		
	</body>
</html>