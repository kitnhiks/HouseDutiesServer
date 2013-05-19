var page = "p";
var pageLogin = "L";
var pageHouse = "H";
var pageOccupant = "O";

var bookmarkedState = YAHOO.util.History.getBookmarkedState(page);
var initialState = bookmarkedState || "";

changePage = function(state){
	try {
		YAHOO.util.History.navigate(page, state);
	}catch(e){
		// history not supported
	}
}

YAHOO.util.History.register(page, initialState, 
	function (state) {
		refreshPage(state);
});

YAHOO.util.History.onReady(
	function () {
		refreshPage(YAHOO.util.History.getCurrentState(page));
});

try{
	YAHOO.util.History.initialize("yui-history-field", "yui-history-iframe");
}catch(e){
	// history not supported
}