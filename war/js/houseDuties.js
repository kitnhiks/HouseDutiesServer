/*****
 Summary
 ******/
// Global conf /variable [CONF]
// Initialisation [INIT]
// Utils [TOOL]
// Controllers [CTRL]
// Network [NETW]
// Views [VIEW]
// Pages [PAGE]

/*****
 [CONF]
 ******/

var BASE_URL = "http://localhost:8888/";
var HOUSE_URL = BASE_URL+"house/";
var LOGIN_URL = HOUSE_URL+"login/";
var TASKS_URL = BASE_URL+"tasks/";
var jsonContentType = "application/json; charset=utf-8";
var paramSeparator = "_";

/*****
 [INIT]
 ******/

var token = "";
var context = {};

/*****
 [TOOL]
 ******/

logDebug = function (message){
    console.log(message);
};

logError = function (message){
    showMsg("Oops !", message, "error");
};

logInfo = function (message){
    showMsg("Hey !", message, "info");
};

showMsg = function (title, message, type){
    $("#msg").html('<div class="alert alert-'+type+'"><strong>'+title+'</strong> '+message+'<button type="button" class="close" data-dismiss="alert">x</button></div>')
};

showHelp = function (htmlText){
    $("#helpText").html(htmlText);
}
showHelp('<p>HouseDuties <strong>helps you deal</strong> with all these <strong>"little pleasures" of your daily life </strong>as a familly, a couple or a roommate.</p>');


startWait = function (){
    //TODO : Throbber ?
};

stopWait = function (){
    //TODO : Throbber ?
};

postJson = function (url, data, callback, statusCode, headers){
    startWait();
    $.ajax({
        url: url,
        type: "POST",
        dataType: "json",
        contentType: jsonContentType,
        data: data,
        success: callback,
        error: function (request, textStatus, errorThrown){
            logDebug(textStatus + ":" + errorThrown);
        },
        complete: function () {
            stopWait();
        },
        statusCode: statusCode,
        headers: headers
    });
};

postJsonWithToken = function(url, data, callback, statusCode){
    postJson(url, data, callback, statusCode, {"X-AuthKey":getToken()});
};

getJsonWithToken = function(url, callback, statusCode){
    startWait();
    $.ajax({
        url: url,
        type: "GET",
        dataType: "json",
        success: callback,
        error: function(request, textStatus, errorThrown){
            logDebug(textStatus+" : "+errorThrown);
        },
        complete: function(){
            stopWait();
        },
        statusCode : statusCode,
        headers : {"X-AuthKey":getToken()}
    });
};

setToken = function(value){
    token = value;
};

getToken = function(){
    return token;
};

/*****
 [CTRL]
 ******/

occupantCtrl = function ($scope){
    $scope.house = {name : "houseTest", id : "1"};
    $scope.occupants = [{name : "o1", id:"1-1"}, {name : "o2", id:"1-2"}, {name : "o3", id:"1-3"}];
};

loginCtrl = function ($scope, $route, $routeParams, $location){
    $scope.clickSignin = function(){
        signin($scope.houseName, $scope.housePassword, $scope, $location);
        return false;
    };
};

houseCtrl = function ($scope, $route, $routeParams, $location){
   alert("welcome ! " + $routeParams.houseId);
}

msgCtrl = function ($scope){
    $scope.$on('msgShow', function(event, args) {
        $scope.msgType = args.type;
        $scope.msgText = args.message;
        $scope.msgTitle = args.title;
    });
}

/*****
 [NETW]
 ******/

signin = function (houseName, housePassword, $scope, $location){
    postJson(
        LOGIN_URL,
        '{"name": "'+houseName+'", "password": "'+housePassword+'"}',
        function(data, textStatus, request) {
            setToken(request.getResponseHeader("X-AuthKey"));
            context.houseId = data.id;
            changePage('/H'+data.id, $scope, $location);
        },
        {
            404: function() {
                var saisie = confirm("This house does not exist, do you want to create it ?");
                if (saisie) {
                    signup(houseName, housePassword, $scope, $location);
                }
            },
            403: function() {
                logError("Unknown Credentials");
            }
        }
    )
};

signup = function (houseName, housePassword, $scope, $location){
    postJson(
        HOUSE_URL,
        '{"name": "'+houseName+'", "password": "'+housePassword+'"}',
        function(data, textStatus, request) {
            setToken(request.getResponseHeader("X-AuthKey"));
            context.houseId = data.id;
            changePage(pageHouse+paramSeparator+data.id, $scope, $location);
        }
    )
};

/*****
 [PAGE]
 ******/

// Login
//  |- House
//   |- HouseTasks
//   |- HouseOccupant
//    |- HouseOccupantTasks
//  |- Tasks

var page = "p";
var pageLogin = "L";
var pageHouse = "H";
var pageOccupant = "O";

changePage = function(page, $scope, $location){
    $location.path(page);
    $scope.$apply();
}

angular.module('houseDuties', [], function($routeProvider, $locationProvider) {
    $routeProvider.when('/', {
        templateUrl: 'login.html',
        controller: loginCtrl
    });
    $routeProvider.when('/H:houseId', {
        templateUrl: 'house.html',
        controller: houseCtrl
    });
    $routeProvider.otherwise({redirectTo:'/'});
});