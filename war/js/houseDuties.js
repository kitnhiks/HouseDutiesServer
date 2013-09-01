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
};
showHelp('<p>HouseDuties <strong>helps you deal</strong> with all these <strong>"little pleasures" of your daily life </strong>as a familly, a couple or a roommate.</p>');

removeMsg = function(){
    $("#msg").html('');
};

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

deleteWithToken = function(url, callback, statusCode){
    startWait();
    $.ajax({
        url: url,
        type: "DELETE",
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

optsModal = {
        backdropFade: true,
        dialogFade:true
};

/*****
 [CTRL]
 ******/

loginCtrl = function ($scope, $route, $routeParams, $location){
    removeMsg();
    $scope.clickSignin = function(){
        signin($scope.houseName, $scope.housePassword, $scope, $location);
    };
};

houseCtrl = function ($scope, $route, $routeParams, $location){
    removeMsg();
    if (getToken() == ""){
        unknownCredential($scope, $location);
    }else{
        context.houseId = $routeParams.houseId;
    }
};

occupantListCtrl= function ($scope, $route, $routeParams, $location){
    // TODO : Handle caching
    $scope.occupantTasks = {};

    $scope.clickAddOccupant = function(){
        addOccupant($scope.occupantName, $scope.occupantPassword, $scope, $location);
        $scope.occupantName = "";
        $scope.occupantPassword = "";
        $scope.addOccupantModal = false;
    };
    $scope.clickRemoveOccupant = function(occupant){
        var saisie = confirm("Do you really want to remove this occupant ?");
        if (saisie) {
            removeOccupant(occupant.key.id, $scope, $location);
        }
    };
    $scope.showOccupantLadder = function(){
        openHouseLadder(context.houseId, $scope, $location);
    }
    $scope.showOccupant = function(occupantId){
        openHouseOccupant(context.houseId, occupantId, $scope, $location);
    };
    $scope.showOccupantTasks = function(occupantId, isCollapse){
        if (isCollapse){
            loadOccupantTasks(occupantId, $scope, $location);
        }
    };
    $scope.openAddTasksModal = function (occupant) {
        $scope.occupant = occupant;
        $scope.addTasksModal = true;
    };
    $scope.closeAddTasksModal = function (task, occupant) {
        if (typeof(task) != 'undefined' || typeof(occupant) != 'undefined'){
            addTaskToOccupant(task, occupant.key.id, $scope, $location);
        }
        $scope.addTasksModal = false;
    };
    $scope.openAddOccupantModal = function () {
        $scope.addOccupantModal = true;
    };
    $scope.closeAddOccupantModal = function () {
        $scope.addOccupantModal = false;
    };
    $scope.optsModal = optsModal;

    if (getToken() == ""){
        unknownCredential($scope, $location);
    }else{
        loadOccupants($scope,$location);
    }
};

occupantCtrl = function ($scope, $route, $routeParams, $location){
    removeMsg();
    if (getToken() == ""){
        unknownCredential($scope, $location);
    }else{
        context.occupantId = $routeParams.occupantId;
        logInfo("welcome "+$routeParams.occupantId);
    }
};

allTasksCtrl = function ($scope, $route, $routeParams, $location){
    if (getToken() == ""){
        unknownCredential($scope, $location);
    }else{
        loadAllTasks($scope,$location);
    }
};


msgCtrl = function ($scope){
    $scope.$on('msgShow', function(event, args) {
        $scope.msgType = args.type;
        $scope.msgText = args.message;
        $scope.msgTitle = args.title;
    });
};

ladderCtrl = function ($scope, $route, $routeParams, $location){
    if (getToken() == ""){
        unknownCredential($scope, $location);
    }else{
        loadOccupantsLadder($scope,$location);
    }
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
            openHouse(context.houseId, $scope, $location);
            $scope.$apply();
        },
        {
            404: function() {
                var saisie = confirm("This house does not exist, do you want to create it ?");
                if (saisie) {
                    signup(houseName, housePassword, $scope, $location);
                }
            },
            403: function(){
                unknownCredential($scope, $location)
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
            openHouse(context.houseId, $scope, $location);
            $scope.$apply();
        },
        {
            403: function(){
                unknownCredential($scope, $location)
            }
        }
    )
};

addOccupant = function (occupantName, occupantPassword, $scope, $location){
    postJsonWithToken(
        HOUSE_URL+context.houseId+"/occupant",
        '{"name": "'+occupantName+'", "password": "'+occupantPassword+'"}',
        function(data, textStatus, request) {
            setToken(request.getResponseHeader("X-AuthKey"));
            loadOccupants($scope, $location);
        },
        {
            404: function() {
                logError("Unknown Occupant");
            },
            403: function(){
                unknownCredential($scope, $location)
            }
        }
    )
};

removeOccupant = function (occupantId, $scope, $location){
    deleteWithToken(
        HOUSE_URL+context.houseId+"/occupant/"+occupantId,
        function(data, textStatus, request) {
            setToken(request.getResponseHeader("X-AuthKey"));
            loadOccupants($scope, $location);
        },
        {
            404: function() {
                logError("Unknown Occupant");
            },
            403: function(){
                unknownCredential($scope, $location)
            }
        }
    )
};

loadOccupants = function ($scope, $location){
    getJsonWithToken(
        HOUSE_URL+context.houseId+"/occupants",
        function(data, textStatus, request) {
            setToken(request.getResponseHeader("X-AuthKey"));
            $scope.occupants = data;
            $scope.$apply();
        },
        {
            404: function() {
                logError("Unknown House");
            },
            403: function(){
                unknownCredential($scope, $location)
            }
        }
    )
};

loadAllTasks = function ($scope, $location){
    getJsonWithToken(
        TASKS_URL,
        function(data, textStatus, request) {
            setToken(request.getResponseHeader("X-AuthKey"));
            $scope.allTasks = data;
            $scope.$apply();
        },
        {
            403: function(){
                unknownCredential($scope, $location)
            }
        }
    )
};

addTaskToOccupant = function (task, occupantId, $scope, $location){
    postJsonWithToken(
        HOUSE_URL+context.houseId+"/occupant/"+occupantId+"/task/",
        '{"categoryKey":"'+task.categoryKey+'", "name":"'+task.name+'", "points":"'+task.points+'"}',
        function(data, textStatus, request) {
            setToken(request.getResponseHeader("X-AuthKey"));
            loadOccupants ($scope, $location);
        },
        {
            404: function() {
                logError("Unknown Occupant");
            },
            403: function(){
                unknownCredential($scope, $location)
            }
        }
    )
};

loadOccupantTasks = function (occupantId, $scope, $location){
    getJsonWithToken(
        HOUSE_URL+context.houseId+"/occupant/"+occupantId+"/tasks",
        function(data, textStatus, request) {
            setToken(request.getResponseHeader("X-AuthKey"));
            $scope.occupantTasks[occupantId] = data;
            $scope.$apply();
        },
        {
            404: function() {
                logError("Unknown Occupant");
            },
            403: function(){
                unknownCredential($scope, $location)
            }
        }
    )
};

loadOccupantsLadder = function ($scope, $location){
    getJsonWithToken(
        HOUSE_URL+context.houseId+"/occupants/ladder",
        function(data, textStatus, request) {
            setToken(request.getResponseHeader("X-AuthKey"));
            $scope.occupants = data;
            $scope.$apply();
        },
        {
            404: function() {
                logError("Unknown House");
            },
            403: function(){
                unknownCredential($scope, $location)
            }
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
var pageLogin = "/";
var pageHouse = "/H";
var pageOccupant = "/O";
var pageLadder = "/L";

unknownCredential = function($scope, $location) {
    changePage(pageLogin, $scope, $location);
    logError("Unknown Credentials");
}

changePage = function(page, $scope, $location){
    $location.path(page);
    //$scope.$apply();
};

openHouse = function(houseId, $scope, $location){
    changePage(pageHouse+houseId, $scope, $location);
};

openHouseLadder = function(houseId, $scope, $location){
    changePage(pageHouse+houseId+pageLadder, $scope, $location);
};

openHouseOccupant = function(houseId, occupantId, $scope, $location){
    changePage(pageHouse+houseId+pageOccupant+occupantId, $scope, $location);
};

angular.module('houseDuties', ['ui.bootstrap'], function($routeProvider, $locationProvider) {
    $routeProvider.when(pageLogin, {
        templateUrl: 'login.html',
        controller: loginCtrl
    });
    $routeProvider.when(pageHouse+':houseId', {
        templateUrl: 'house.html',
        controller: houseCtrl
    });
    $routeProvider.when(pageHouse+':houseId'+pageOccupant+':occupantId', {
        templateUrl: 'occupant.html',
        controller: occupantCtrl
    });
    $routeProvider.when(pageHouse+':houseId'+pageLadder, {
        templateUrl: 'ladder.html',
        controller: ladderCtrl
    });
    $routeProvider.otherwise({redirectTo:pageLogin});
});