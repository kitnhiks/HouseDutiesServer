/*****
Summary
******/
// Global conf /variable [CONF]
// Initialisation [INIT]
// Utils [TOOL]
// Controllers [CTRL]
// Views [VIEW]


/*****
[CONF]
******/

var BASE_URL = "http://localhost:8888/";
var HOUSE_URL = BASE_URL+"house/";
var LOGIN_URL = HOUSE_URL+"login/";
var jsonContentType = "application/json; charset=utf-8";

/*****
[INIT]
******/

var token = "";
var houseId = "";

$.ajaxSetup ({
	cache: false
});

$(document).ready(function() {
	$("#signup").click(signup);
	$("#signin").click(signin);
});

/*****
[TOOL]
******/

log = function(message){
	$("#log").empty().append(message);
}

startWait = function(){
	log("");
	$("#throbber").empty().append("Wait...");
	//TODO : Throbber ?
}

stopWait = function(){
	$("#throbber").empty();
	//TODO : Throbber ?
}

checkMandatoryFields = function(houseName, housePassword){
	if (houseName.length == 0) {  
		$("#houseName").focus();
		log("Manque un nom");
		return false;
	} else if (housePassword.length == 0){
		$("#housePassword").focus();
		log("Manque un password");
		return false;
	}
	return true;
}

postJson = function(url, data, callback, statusCode, headers){
	startWait();
	$.ajax({
		url: url,
		type: "POST",
		dataType: "json",
		contentType: jsonContentType,
		data: data,
		success: callback,
		error: function(request, textStatus, errorThrown) {
			log(textStatus+":"+errorThrown);
		},
		complete: function(){
			stopWait();
        },
		statusCode : statusCode,
		headers : headers
	});
}

postJsonWithToken = function(url, data, callback, statusCode){
	postJson(url, data, callback, statusCode, {"X-AuthKey":token});
}

getJsonWithToken = function(url, callback, statusCode){
	startWait();
	$.ajax({
		url: url,
		type: "GET",
		dataType: "json",
		success: callback,
		error: function(request, textStatus, errorThrown) {
			log(textStatus+":"+errorThrown);
		},
		complete: function(){
			stopWait();
        },
		statusCode : statusCode,
		headers : {"X-AuthKey":token}
	});
}

setToken = function(value){
	token = value;
}

getToken = function(){
	return token;
}

/*****
[CTRL]
******/

signin = function(){
	var houseName = $("#houseName").val();
	var housePassword = $("#housePassword").val();
	if (checkMandatoryFields(houseName, housePassword)){
		postJson(
			LOGIN_URL, 
			'{"name": "'+houseName+'", "password": "'+housePassword+'"}', 
			function(data, textStatus, request) {
				setToken(request.getResponseHeader("X-AuthKey"));
				loadHouse(data.id);
			},
			{
				404: function() {
					log("unknown credentials");
				}
			}
		)
	}
	return false;
}

signup = function(){
	var houseName = $("#houseName").val();
	var housePassword = $("#housePassword").val();
	if (checkMandatoryFields(houseName, housePassword)){
		postJson(
			HOUSE_URL, 
			'{"name": "'+houseName+'", "password": "'+housePassword+'"}', 
			function(data, textStatus, request) {
				setToken(request.getResponseHeader("X-AuthKey"));
				loadHouse(data.id);
			},
			{
				404: function() {
					log("unknown house");
				}
			}
		)
	}
	return false;
}

loadHouse = function(id){
	getJsonWithToken(
		HOUSE_URL+id, 
		function(data, textStatus, request) {
			houseId = id;
			setToken(request.getResponseHeader("X-AuthKey"));
			showHouse(data);
		},
		{
			404: function() {
				log("unknown house");
			}
		}
	)
}

loadOccupants = function(id){
	getJsonWithToken(
		HOUSE_URL+id, // TODO 
		function(data, textStatus, request) {
			setToken(request.getResponseHeader("X-AuthKey"));
			showOccupants(data);
		},
		{
			404: function() {
				log("unknown house");
			}
		}
	)
}

createOccupant = function(){
	var occupantName = $("#occupantName").val();
	var occupantPassword = $("#occupantPassword").val();
	if (checkMandatoryFields(occupantName)){
		postJson(
			HOUSE_URL, // TODO  
			'{"name": "'+occupantName+'", "password": "'+occupantPassword+'"}', 
			function(data, textStatus, request) {
				setToken(request.getResponseHeader("X-AuthKey"));
				loadOccupants(houseId);
			},
			{
				404: function() {
					log("unknown house");
				}
			}
		)
	}
	return false;
}

/*****
[VIEW]
******/

var partials = 
			{
				houseName : '<h1>Bienvenue dans la maison {{name}}</h1>'
			};

var houseHomeHeader = '<div class="panel">{{>houseName}}</div>';
var houseHomeContent = 
						'<div class="row">'+
							'<div class="row">'+
								'<div class="eight columns push-two">'+
									'<input id="occupantName" type="text" value="Enter Occupant Name"/>'+
								'</div>'+
							'</div>'+
							'<div class="row">'+
								'<div class="eight columns push-two">'+
								  '<input id="occupantPassword" type="text" value="Enter Password (optional)"/>'+
								'</div>'+
							'</div>'+
							'<div class="row">'+
								'<div class="two columns">'+
									'<p><a id="createOccupant" href="#" class="button">Create</a></p>'+
								'</div>'+
							'</div>'+
						'</div>';
			
			
showHouse = function(house){
	$('#header').html(Mustache.to_html(houseHomeHeader, house, partials));
	$('#content').html(Mustache.to_html(houseHomeContent, house, partials));
	$("#createOccupant").click(createOccupant);
}

showOccupants = function(occupants){
	
}
