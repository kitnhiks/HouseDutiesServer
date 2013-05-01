/*****
Summary
******/
// Global conf /variable [CONF]
// Initialisation [INIT]
// Utils [TOOL]
// Controllers [CTRL]
// Network [NETW]
// Views [VIEW]


/*****
[CONF]
******/

var BASE_URL = "http://localhost:8888/";
var HOUSE_URL = BASE_URL+"house/";
var LOGIN_URL = HOUSE_URL+"login/";
var TASKS_URL = BASE_URL+"tasks/";
var jsonContentType = "application/json; charset=utf-8";

/*****
[INIT]
******/

var context = {
	token : "",
	houseId : "",
	occupantId : ""
}

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
	postJson(url, data, callback, statusCode, {"X-AuthKey":getToken()});
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
		headers : {"X-AuthKey":getToken()}
	});
}

setToken = function(value){
	context.token = value;
}

getToken = function(){
	return context.token;
}

/*****
[CTRL]
******/

clickSignin = function(){
	var houseName = $("#houseName").val();
	var housePassword = $("#housePassword").val();
	if (checkHouseMandatoryFields(houseName, housePassword)){
		signin(houseName, housePassword);
	}
	return false;
}

clickSignup = function(){
	var houseName = $("#houseName").val();
	var housePassword = $("#housePassword").val();
	if (checkHouseMandatoryFields(houseName, housePassword)){
		signup(houseName, housePassword);
	}
	return false;
}

clickCreateOccupant = function(){
	var occupantName = $("#occupantName").val();
	var occupantPassword = $("#occupantPassword").val();
	if (checkOccupantMandatoryFields(occupantName)){
		createOccupant(occupantName, occupantPassword);
	}
	return false;
}

clickOccupant = function (){
	context.occupantId = this.id;
	loadOccupant();
}

clickShowTasks = function (){
	loadTasks();
}

clickOccupantTask = function (){
	alert ("TBI");
}

clickAddOccupantTask = function (){
	alert ("TBI");
}


/*****
[NETW]
******/

signin = function (houseName, housePassword){
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

signup = function (houseName, housePassword){
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

createOccupant = function(occupantName, occupantPassword){
	postJsonWithToken(
		HOUSE_URL+context.houseId+"/occupant", 
		'{"name": "'+occupantName+'", "password": "'+occupantPassword+'"}', 
		function(data, textStatus, request) {
			setToken(request.getResponseHeader("X-AuthKey"));
			loadOccupants();
		},
		{
			404: function() {
				log("unknown house");
			},
			403: function() {
				log("unknown credentials");
			}
		}
	)
}

loadHouse = function(id){
	getJsonWithToken(
		HOUSE_URL+id, 
		function(data, textStatus, request) {
			context.houseId = id;
			setToken(request.getResponseHeader("X-AuthKey"));
			showHouse(data);
			loadOccupants();
		},
		{
			404: function() {
				log("unknown house");
			},
			403: function() {
				log("unknown credentials");
			}
		}
	)
}

loadOccupants = function(){
	getJsonWithToken(
		HOUSE_URL+context.houseId+"/occupants",
		function(data, textStatus, request) {
			setToken(request.getResponseHeader("X-AuthKey"));
			showOccupants(data);
		},
		{
			404: function() {
				log("unknown house");
			},
			403: function() {
				log("unknown credentials");
			}
		}
	)
}

loadOccupant = function(){
	getJsonWithToken(
		HOUSE_URL+context.houseId+"/occupant/"+context.occupantId,
		function(data, textStatus, request) {
			setToken(request.getResponseHeader("X-AuthKey"));
			showOccupant(data);
			loadOccupantTasks(data.key.id);
		},
		{
			404: function() {
				log("unknown occupant");
			},
			403: function() {
				log("unknown credentials");
			}
		}
	)
}

loadOccupantTasks = function(){
	getJsonWithToken(
		HOUSE_URL+context.houseId+"/occupant/"+context.occupantId+"/tasks",
		function(data, textStatus, request) {
			setToken(request.getResponseHeader("X-AuthKey"));
			showOccupantTasks(data);
		},
		{
			404: function() {
				log("unknown occupant");
			},
			403: function() {
				log("unknown credentials");
			}
		}
	)
}

loadTasks = function(){
	getJsonWithToken(
		TASKS_URL,
		function(data, textStatus, request) {
			setToken(request.getResponseHeader("X-AuthKey"));
			showTasks(data);
		},
		{
			403: function() {
				log("unknown credentials");
			}
		}
	)
}

/*****
[VIEW]
******/

var partials = 
			{
				houseName : '<h1>Welcome into the houde {{name}}</h1>',
				occupantName : '<h1>The tasks {{name}} is working on</h1>'
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
						'<div id="occupantsPanel" class="row">'+
						'</div>'+
						'<hr>'+
						'<div id="newHouseOccupantPanel" class="row">'+
						'</div>';
						

var newHouseOccupantForm = 
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
						'</div>';
						
var houseOccupantsContent = 
							'{{#occupants}}'+
								'<div class="row">'+
									'<div class="eight columns push-two">'+
										'<p><a id="{{key.id}}" href="#" class="button">{{name}} ({{points}})</a></p>'+
									'</div>'+
								'</div>'+
							'{{/occupants}}';
							
var occupantHomeHeader = '<div class="panel">{{>occupantName}}</div>';
var occupantHomeContent = 
						'<div id="houseOccupantTasksPanel" class="row">'+
						'</div>'+
						'<hr>'+
						'<div id="newHouseOccupantTaskPanel" class="row">'+
						'</div>';
						
var newHouseOccupantTaskForm = 
				'<div class="row">'+
					'<div class="row">'+
						'<div id="tasksPanel" class="row">'+
						'</div>'+
						'<div class="two columns">'+
							'<p><a id="addTask" href="#" class="button">Add Task</a></p>'+
						'</div>'+
					'</div>'+
				'</div>';
						
var houseOccupantTasksContent = 
						'{{#tasks}}'+
							'<div class="row">'+
								'<div class="eight columns push-two">'+
									'<p><a id="{{key.id}}" href="#" class="button">{{name}} ({{points}})</a></p>'+
								'</div>'+
							'</div>'+
						'{{/tasks}}';

showLogin = function (){
	$('#header').html(Mustache.to_html(loginHeader, null, partials));
	$('#content').html(Mustache.to_html(loginContent, null, partials));
	$("#signup").click(clickSignup);
	$("#signin").click(clickSignin);
	InputWithTextHandler();
}

showHouse = function(house){
	$('#header').html(Mustache.to_html(houseHomeHeader, house, partials));
	$('#content').html(Mustache.to_html(houseHomeContent, null, partials));
	$('#newHouseOccupantPanel').html(Mustache.to_html(newHouseOccupantForm, null, partials));
	$("#createOccupant").click(clickCreateOccupant);
	InputWithTextHandler();
}

showOccupants = function(occupants){
	$('#occupantsPanel').html(Mustache.to_html(houseOccupantsContent, {"occupants" : occupants}, partials));
	var nbOccupants = occupants.length;
	for (i=0; i<nbOccupants; i++){
		var occupantId = occupants[i].key.id;
		$("#"+occupantId).click(clickOccupant);
	}
}

showOccupant = function(occupant){
	$('#header').html(Mustache.to_html(occupantHomeHeader, occupant, partials));
	$('#content').html(Mustache.to_html(occupantHomeContent, null, partials));
	$('#newHouseOccupantTaskPanel').html(Mustache.to_html(newHouseOccupantTaskForm, null, partials));
	$("#addTask").click(clickShowTasks);
}

showOccupantTasks = function(tasks){
	$('#houseOccupantTasksPanel').html(Mustache.to_html(houseOccupantTasksContent, {"tasks" : tasks}, partials));
	var nbTasks = tasks.length;
	for (i=0; i<nbTasks; i++){
		var taskId = tasks[i].key.id;
		$("#"+taskId).click(clickOccupantTask);
	}
}

InputWithTextHandler = function (){
	$(".text-within").click(clearInputField);
	$(".text-within").blur(leaveInputField);
}

showTasks = function(tasks){
	$('#tasksPanel').html(Mustache.to_html(tasksContent, {"tasks" : tasks}, partials));
	var nbTasks = tasks.length;
	for (i=0; i<nbTasks; i++){
		var taskId = tasks[i].key.id;
		$("#"+taskId).click(clickAddOccupantTask);
	}
}
