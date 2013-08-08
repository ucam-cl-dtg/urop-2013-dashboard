function bindSaveSettingsListener() {
	$(document).on('click', '#save-account-settings', function(e) {
		e.preventDefault();
		
		var signups = $('#signups-opt-in').val();
		alert(signups);
		var questions = $('#questions-opt-in').val();
		alert(questions);
		var handins = $('#handins-opt-in').val();
		alert(handins);
		
	    $.ajax({
	    	type: 'PUT',
	    	url: prepareURL("dashboard/account?signups=" + signups + "&questions=" + questions + "&handins=" + handins),
	    	success: function(data) {
	    		if (data.errors) {
	    			alert(data.errors);
	    		} else {
	    			alert(data.redirectTo);
	    		}
	    	},
	    	error: function() {
	    		alert('Error: could not perform AJAX request');
	    	}
	    });
	});
}