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
var inputValues = {
	"houseName" : "Enter Name",
	"housePassword" : "Enter Password",
	"occupantName" : "Enter Name" ,
	"occupantPassword" : "Enter Password (optional)",
};

$.ajaxSetup ({
	cache: false
});

$(document).ready(function() {
	showLogin();
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

checkHouseMandatoryFields = function(houseName, housePassword){
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

checkOccupantMandatoryFields = function(occupantName){
	if (occupantName.length == 0 || occupantName == inputValues.occupantName) {  
		$("#occupantName").focus();
		log("Manque un nom");
		return false;
	}
	return true;
}

clearInputField = function(){
	if ($(this).val() == inputValues[$(this).attr('id')]){
		$(this).val("");
	}
}

leaveInputField = function(){
	if ($(this).val() == ""){
		$(this).val(inputValues[$(this).attr('id')]);
	}
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
	if (checkHouseMandatoryFields(houseName, housePassword)){
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
	if (checkHouseMandatoryFields(houseName, housePassword)){
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
			loadOccupants(id);
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
		HOUSE_URL+id+"/occupants", // TODO 
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
	if (checkOccupantMandatoryFields(occupantName)){
		postJson(
			HOUSE_URL+houseId+"/occupant", 
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

var loginHeader = 
	'<div class="panel">'+
		'<h1>House Duties</h1>'+
	'</div>';
		
var loginContent =
	'<div class="row">'+
		'<div class="three columns mobile-one"><img src="img/houduty.png" /></div>'+
		'<div class="nine columns mobile-three">'+
			'<p>House Duties helps you deal with all these "little pleasures" of your daily life as a familly, a couple or a roommate.</p>'+
		'</div>'+
	'</div>'+

	'<div class="row">'+
		'<div class="row">'+
			'<div class="eight columns push-two">'+
				'<input id="houseName" class="text-within" type="text" value="'+inputValues.houseName+'"/>'+
			'</div>'+
		'</div>'+
		'<div class="row">'+
			'<div class="eight columns push-two">'+
				'<input id="housePassword" class="text-within" type="text" value="'+inputValues.housePassword+'"/>'+
			'</div>'+
		'</div>'+
		'<div class="row">'+
			'<div class="two columns">'+
				'<p><a id="signup" href="#" class="button">Create</a></p>'+
			'</div>'+
			'<div class="two columns">'+
				'<p><a id="signin" href="#" class="button">Enter</a></p>'+
			'</div>'+
		'</div>'+
	'</div>';
			
var houseHomeHeader = '<div class="panel">{{>houseName}}</div>';
var houseHomeContent = 
						'<div class="row">'+
							'<div class="row">'+
								'<div class="eight columns push-two">'+
									'<input id="occupantName" class="text-within" type="text" value="'+inputValues.occupantName+'"/>'+
								'</div>'+
							'</div>'+
							'<div class="row">'+
								'<div class="eight columns push-two">'+
								  '<input id="occupantPassword" class="text-within" type="text" value="'+inputValues.occupantPassword+'"/>'+
								'</div>'+
							'</div>'+
							'<div class="row">'+
								'<div class="two columns">'+
									'<p><a id="createOccupant" href="#" class="button">Create</a></p>'+
								'</div>'+
							'</div>'+
						'</div>'+
						'<div id="occupantsPanel" class="row">'+
						'</div>';

var houseOccupantsContent = 
							'{{#occupants}}'+
								'<div class="row">'+
									'<div class="eight columns push-two">'+
										'<p><a id="showOccupant[{{key.id}}]" href="#" class="button">{{name}} ({{points}})</a></p>'+
									'</div>'+
								'</div>'+
							'{{/occupants}}';

showLogin = function (){
	$('#header').html(Mustache.to_html(loginHeader, null, partials));
	$('#content').html(Mustache.to_html(loginContent, null, partials));
	$("#signup").click(signup);
	$("#signin").click(signin);
	showCommon();
}
							
showHouse = function(house){
	$('#header').html(Mustache.to_html(houseHomeHeader, house, partials));
	$('#content').html(Mustache.to_html(houseHomeContent, null, partials));
	$("#createOccupant").click(createOccupant);
	showCommon();
}

showOccupants = function(occupants){
	$('#occupantsPanel').html(Mustache.to_html(houseOccupantsContent, {"occupants" : occupants}, partials));
	showCommon();
}

showCommon = function (){
	$(".text-within").click(clearInputField);
	$(".text-within").blur(leaveInputField);
}
