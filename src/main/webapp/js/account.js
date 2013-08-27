function bindSaveSettingsListener() {
	$('#save-account-settings').on('click', function(e) {
		e.preventDefault();
		
		var signups = $('#signups-opt-in').is(':checked');
		var questions = $('#questions-opt-in').is(':checked');
		var handins = $('#handins-opt-in').is(':checked');
		
		var email = $('#notification-sends-email').is(':checked');
		
	    $.ajax({
	    	type: 'PUT',
	    	url: prepareURL("account?signups=" + signups + "&questions=" + questions + "&handins=" + handins + "&notificationSendsEmail=" + email),
	    	success: function(data) {
	    		if (data.errors) {
	    			errorNotification(data.errors);
	    		} else {
	    			successNotification("RedirectTo: " + data.redirectTo);
	    			location.reload();
	    		}
	    	},
	    	error: function(jqXHR, textStatus, error) {
	    		errorNotification("Could not perform AJAX request: " + error);
	    	}
	    });
	});
}