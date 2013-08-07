function applyDatepicker() {
	$(".datepicker").datepicker({dateFormat: "dd/mm/yy"});
}

function userTokenInput() {
	$(".user-token-input").tokenInput("api/dashboard/groups/queryCRSID", {
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
				console.log(v.crsid);
				console.log(v.name);
				target.tokenInput("add",{crsid: v.crsid, name: v.name});
			});
		}                    
}