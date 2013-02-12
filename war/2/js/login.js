$.ajaxSetup ({
	cache: false
});

var BASE_URL = "http://127.0.0.1:8888/";
var HOUSE_URL = BASE_URL+"house";

$(document).ready(function() {
	$("#signup").click(function(){
		var houseName = $("#houseName").val();
		var housePassword = $("#housePassword").val();
		if (houseName.length == 0) {  
			$("#houseName").focus();
			log("Manque un nom");
		} else if (housePassword.length == 0){
			$("#housePassword").focus();
			log("Manque un password");
		} else {
			var data = '{"name": "'+houseName+'", "password": "'+housePassword+'"}';
			$.ajax({
				url: HOUSE_URL,
				type: "post",
				dataType: "json",
				contentType: "application/json; charset=utf-8",
				data: data,
				success: function(data, textStatus, jqXHR) {
					log(data);
				},
				error: function(jqXHR, textStatus, errorThrown) {
					log(textStatus+":"+errorThrown);
				},
			});
		}
		return false;
	});
});

log = function(message){
	$("#log").empty().append(message);
}