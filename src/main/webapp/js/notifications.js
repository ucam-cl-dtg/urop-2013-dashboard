function markNotificationAsReadUnread() {
	$('.mark-notification-as-read').on('click', function() {
		var target = $(this).attr('data-notification-target');
		var clickedSection = $(this).parent().attr('data-section');
		// Implicitly checking whether markAsUnread is true or false
		var markAsRead = $(this).children('a').hasClass('mark-as-read');
	    $.ajax({
	    	type: 'PUT',
	    	url: prepareURL("notifications/" + target + "?read=" + markAsRead),
	    	success: function(data) {
	    		if (data.errors) {
	    			errorNotification("Could not update notification");
	    			console.log(data.errors);
	    		} else {
	    			successNotification("Succesfully updated notification");
	    			$("#" + target).parent().slideUp(500, function() {
	    				$(this).remove();
	    			});
	    			refreshNotificationCount([clickedSection]);
	    		}
	    	},
	    	error: function() {
	    		errorNotification("Could not update notification");
	    	}
	    });
	});
}