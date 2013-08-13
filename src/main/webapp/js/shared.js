function applyDatepicker() {
	$(".datepicker").datepicker({dateFormat: "dd/mm/yy"});
}

function applyTokenInput(element, type, path, tokenValue, propertyToSearch, resultFormat, tokenFormat){
	
	if(type=="user"){
		var hintText = "Search for a user";
	} else if(type=="group"){
		var hintText = "Serach for a group";
	} else {
		var intText = "Start typing to search";
	}
	
	$("."+ element).tokenInput(prepareURL(path), {
		theme: "facebook",
		method: "post",
        tokenValue: tokenValue,
        propertyToSearch: propertyToSearch,
        theme: "facebook",
        minChars: 1,
        hintText: hintText,
        resultsLimit: 10,
        preventDuplicates: true,
        
        resultsFormatter: resultFormat,
        tokenFormatter: tokenFormat,   
		});   
		
	if(type=="user"){
		if (data = (target = $("."+element)).data("prepopulate")) {
			$.each(data.users, function(i,v) {
				target.tokenInput("add",{crsid: v.crsid, name: v.name});
			});
		}
	} else if(type=="group") {
		if (data = (target = $("."+element)).data("prepopulate")) {
			$.each(data.users, function(i,v) {
				target.tokenInput("add",{id: v.id, name: v.name});
			});
		}		
	}
}

function gUserTokenInput() {
	var resultFormat = function(item){ return "<li>" + "<div style='display: inline-block; padding-left: 10px;'><div class='full_name'>" + item.name + " (" + item.crsid + ")</div><div class='email'>" + item.crsid + "@cam.ac.uk</div></div></li>"; };
	var tokenFormat = function(item) { return "<li><p>" + item.name + " (" + item.crsid + ")</p></li>"; };
	
	applyTokenInput("group-user-token-input", "user", "groups/queryCRSID", "crsid", "crsid", resultFormat, tokenFormat);
}

function dUserTokenInput() {
	var resultFormat = function(item){ return "<li>" + "<div style='display: inline-block; padding-left: 10px;'><div class='full_name'>" + item.name + " (" + item.crsid + ")</div><div class='email'>" + item.crsid + "@cam.ac.uk</div></div></li>"; };
	var tokenFormat = function(item) { return "<li><p>" + item.name + " (" + item.crsid + ")</p></li>"; };
	
	applyTokenInput("deadline-user-token-input", "user", "groups/queryCRSID", "crsid", "crsid", resultFormat, tokenFormat);
}

function gUserSurnameTokenInput() {
	var resultFormat = function(item){ return "<li>" + "<div style='display: inline-block; padding-left: 10px;'><div class='full_name'>" + item.name + " (" + item.crsid + ")</div><div class='email'>" + item.crsid + "@cam.ac.uk</div></div></li>"; };
	var tokenFormat = function(item) { return "<li><p>" + item.name + " (" + item.crsid + ")</p></li>"; };
	
	applyTokenInput("group-user-token-input", "user", "groups/querySurname", "crsid", "surname", resultFormat, tokenFormat);
}

function dGroupTokenInput() {
	var resultFormat = function(item){ return "<li>" + "<div style='display: inline-block; padding-left: 10px;'><div class='full_name'>" + item.group_name + "</div></div></li>";};
	var tokenFormat = function(item) { return "<li><p>" + item.group_name + "</p></li>"; };   
	
	applyTokenInput("deadline-group-token-input", "group", "deadlines/queryGroup", "group_id", "group_name", resultFormat, tokenFormat);
}

function gGroupTokenInput() {
	var resultFormat = function(item){ return "<li>" + "<div style='display: inline-block; padding-left: 10px;'><div class='full_name'>" + item.name + "</div></div></li>";};
	var tokenFormat = function(item) { return "<li><p>" + item.name + " (" + item.crsid + ")</p></li>"; };   
	
	applyTokenInput("import-group-token-input", "group", "groups/queryGroup", "id", "name", resultFormat, tokenFormat);
}

function tokenInputType() {
	$(".token-type-radio").click(function() {
		var type = $(this).val();
		alert(type);
		if(type=="surname"){
			//
		} else {
			//
		}
	});
}