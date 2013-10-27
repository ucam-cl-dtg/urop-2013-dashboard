function bindSaveSettingsListener() {
	$('#save-account-settings').on('click', function(e) {
		e.preventDefault();
		
		var signups = $('#signups-opt-in').is(':checked');
		var questions = $('#questions-opt-in').is(':checked');
		var handins = $('#handins-opt-in').is(':checked');
		
		var dashboardEmail = $('#dashboard-get-email').is(':checked');
		var signupsEmail = $('#signups-get-email').is(':checked');
		var questionsEmail = $('#questions-get-email').is(':checked');
		var handinsEmail = $('#handins-get-email').is(':checked');
		
	    $.ajax({
	    	type: 'PUT',
	    	url: prepareURL("account?signups=" + signups + "&questions=" + questions + "&handins=" + handins
	    					+ "&dashboardEmail=" + dashboardEmail + "&signupsEmail=" + signupsEmail + "&questionsEmail=" + questionsEmail 
	    					+ "&handinsEmail=" + handinsEmail),
	    	success: function(data) {
	    		if (data.errors) {
	    			errorNotification(data.errors);
	    		} else {
	    			successNotification("Successfully updated account settings");
	    			location.reload();
	    		}
	    	},
	    	error: function(jqXHR, textStatus, error) {
	    		errorNotification("Could not perform AJAX request: " + error);
	    	}
	    });
	});
}

function bindNewApiKeyListener() {
	$('#generate-new-api-key').on('click', function() {
		$.ajax({
	    	type: 'GET',
	    	url: prepareURL("keys/new"),
	    	success: function(data) {
	    		if (data.errors) {
	    			errorNotification(data.errors);
	    		} else {
	    			successNotification("Successfully created a new API key");
	    			$('.api-keys').append('<li><div class="fixed-width-icon"><i class="icon icon-lock"></i></div>' + data.key + '<div class="api-delete-icon"><a class="delete-api-key" data-key="' + data.key + '"><i class="icon icon-cancel_circle"></i></a></div></li>');
	    			bindDeleteApiKeyListener();
	    			if ($('.api-keys li').length == 1) {
	    				location.reload();
	    			}
	    		}
	    	},
	    	error: function(jqXHR, textStatus, error) {
	    		errorNotification("Could not perform AJAX request: " + error);
	    	}
	    });
	});
}

function bindDeleteApiKeyListener() {
	$('.delete-api-key').on('click', function() {
		var key = $(this).data("key");
		var keyEntry = $(this).parents("li");
		var keyList = $(this).parents(".api-keys li");
		$.ajax({
	    	type: 'DELETE',
	    	data: key,
	    	url: prepareURL("keys/delete"),
	    	success: function(data) {
	    		if (data.success) {
	    			successNotification("API key deleted successfully");
	    			keyEntry.hide();
	    			keyEntry.remove();
	    			if ($('.api-keys li').length == 0) {
	    				location.reload();
	    			}
	    		} else {
	    			errorNotification("Could not remove api key");
	    		}
	    	},
	    	error: function(jqXHR, textStatus, error) {
	    		errorNotification("Could not perform AJAX request: " + error);
	    	}
	    });
	});
}

function bindResetApiKeyListener() {
	$('#reset-api-key').on('click', function() {
		$.ajax({
	    	type: 'POST',
	    	url: prepareURL("keys/reset"),
	    	success: function(data) {
	    		if (data.errors) {
	    			errorNotification(data.errors);
	    		} else {
	    			successNotification("Successfully created a new API key");
	    			$('.api-keys').html('<li><div class="fixed-width-icon"><i class="icon icon-lock"></i></div>' + data.key + '</li>');
	    			$('#deadlines-cal-url').val(prepareURL("deadlines/calendar?key="+data.key+"&userId="+data.userID));
	    			var eventsURL = prepareURL("api/events/calendar?key="+data.key+"&userId="+data.userID).replace("dashboard", "signups");
	    			$('#events-cal-url').val(eventsURL);
	    		}
	    	},
	    	error: function(jqXHR, textStatus, error) {
	    		errorNotification("Could not perform AJAX request: " + error);
	    	}
	    });
	});
}