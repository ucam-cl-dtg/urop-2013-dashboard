function bindSaveSettingsListener() {
	$(document).on('click', '#save-account-settings', function(e) {
		e.preventDefault();
		
		var signups = $('#signups-opt-in').is(':checked');
		var questions = $('#questions-opt-in').is(':checked');
		var handins = $('#handins-opt-in').is(':checked');
		
	    $.ajax({
	    	type: 'PUT',
	    	url: prepareURL("dashboard/account?signups=" + signups + "&questions=" + questions + "&handins=" + handins),
	    	success: function(data) {
	    		if (data.errors) {
	    			errorNotification(data.errors);
	    		} else {
	    			successNotification("RedirectTo: " + data.redirectTo);
	    		}
	    	},
	    	error: function(jqXHR, textStatus, error) {
	    		errorNotification("Could not perform AJAX request: " + error);
	    	}
	    });
	});
}