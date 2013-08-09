function applyDatepicker() {
	$(".datepicker").datepicker({dateFormat: "dd/mm/yy"});
}

function userTokenInput() {
	$(".user-token-input").tokenInput(prepareURL("dashboard/groups/queryCRSID"), {
		theme: "facebook",
		method: "post",
        tokenValue: "crsid",
        propertyToSearch: "crsid",
        theme: "facebook",
        minChars: 3,
        hintText: "Search for a user",
        resultsLimit: 10,
        preventDuplicates: true,
        
        resultsFormatter: function(item){ return "<li>" + "<div style='display: inline-block; padding-left: 10px;'><div class='full_name'>" + item.name + " (" + item.crsid + ")</div><div class='email'>" + item.crsid + "@cam.ac.uk</div></div></li>" },
        tokenFormatter: function(item) { return "<li><p>" + item.name + " (" + item.crsid + ")</p></li>" },   
		});   
		
		if (data = (target = $("input[name='users']")).data("prepopulate")) {
			$.each(data.users, function(i,v) {
				target.tokenInput("add",{crsid: v.crsid, name: v.name});
			});
		}                    
}

function userSurnameTokenInput() {
	$(".user-surname-token-input").tokenInput(prepareURL("dashboard/groups/querySurname"), {
		theme: "facebook",
		method: "post",
        tokenValue: "crsid",
        propertyToSearch: "surname",
        theme: "facebook",
        minChars: 3,
        hintText: "Search for a user",
        resultsLimit: 10,
        preventDuplicates: true,
        
        resultsFormatter: function(item){ return "<li>" + "<div style='display: inline-block; padding-left: 10px;'><div class='full_name'>" + item.name + " (" + item.crsid + ")</div><div class='email'>" + item.crsid + "@cam.ac.uk</div></div></li>" },
        tokenFormatter: function(item) { return "<li><p>" + item.name + " (" + item.crsid + ")</p></li>" },   
		});   
		
		if (data = (target = $("input[name='users']")).data("prepopulate")) {
			$.each(data.users, function(i,v) {
				target.tokenInput("add",{crsid: v.crsid, name: v.name});
			});
		}                   
}

function groupTokenInput() {
	$(".group-token-input").tokenInput(prepareURL("dashboard/deadlines/queryGroup"), {
		theme: "facebook",
		method: "post",
        tokenValue: "group_id",
        propertyToSearch: "group_name",
        theme: "facebook",
        minChars: 1,
        hintText: "Search for a group",
        resultsLimit: 10,
        preventDuplicates: true,
        
        resultsFormatter: function(item){ return "<li>" + "<div style='display: inline-block; padding-left: 10px;'><div class='full_name'>" + item.group_name + " (" + item.crsid + ")</div><div class='email'>" + item.crsid + "@cam.ac.uk</div></div></li>" },
        tokenFormatter: function(item) { return "<li><p>" + item.name + " (" + item.crsid + ")</p></li>" },   
		});   
                 
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

function groupImportTokenInput() {
	$(".group-import-token-input").tokenInput(prepareURL("dashboard/groups/queryGroup"), {
		theme: "facebook",
		method: "post",
        tokenValue: "id",
        propertyToSearch: "name",
        theme: "facebook",
        minChars: 3,
        hintText: "Search for a group",
        resultsLimit: 10,
        preventDuplicates: true,
        
        resultsFormatter: function(item){ return "<li>" + "<div style='display: inline-block; padding-left: 10px;'><div class='full_name'>" + item.name + " (" + item.id + ")</div><div class='email'>" + item.description + "</div></div></li>" },
        tokenFormatter: function(item) { return "<li><p>" + item.name +"</p></li>" },   
		});   
		                 
}