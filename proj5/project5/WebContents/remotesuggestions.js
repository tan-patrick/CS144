
/**
 * Provides suggestions for state names (USA).
 * @class
 * @scope public
 */
function RemoteStateSuggestions() {

    if (typeof XMLHttpRequest != "undefined") {
        this.http = new XMLHttpRequest();
    } else if (typeof ActiveXObject != "undefined") {
        this.http = new ActiveXObject("MSXML2.XmlHttp");
    } else {
        alert("No XMLHttpRequest object available. This functionality will not work.");
    }

}

/**
 * Request suggestions for the given autosuggest control. 
 * @scope protected
 * @param oAutoSuggestControl The autosuggest control to provide suggestions for.
 */
RemoteStateSuggestions.prototype.requestSuggestions = function (oAutoSuggestControl /*:AutoSuggestControl*/,
                                                          bTypeAhead /*:boolean*/) {

    var oHttp = this.http;
                                                             
    //if there is already a live request, cancel it
    if (oHttp.readyState != 0) {
        oHttp.abort();
    }                 
    
    //build the URL
    var sURL = "suggest?q=" + encodeURIComponent(oAutoSuggestControl.textbox.value);
    
    //open connection to states.txt file
    oHttp.open("get", encodeURI(sURL) , true);
    oHttp.onreadystatechange = function () {
        if (oHttp.readyState == 4) {
            //evaluate the returned text JavaScript (an array)
            var aSuggestions = oHttp.responseXML;
			var top = aSuggestions.documentElement;
			var completeSuggArr = top.childNodes;
			var retString = "[";
			for(var i = 0; i < top.childElementCount; i++){
				if(i === top.childElementCount - 1)
					retString += "\"" + completeSuggArr[i].childNodes[0].getAttribute("data") + "\"";
				else
					retString += "\"" + completeSuggArr[i].childNodes[0].getAttribute("data") + "\",";
			}
			retString += "]";
            //provide suggestions to the control
            oAutoSuggestControl.autosuggest(eval(retString), bTypeAhead);        
        }    
    };
    oHttp.send(null);
    

};