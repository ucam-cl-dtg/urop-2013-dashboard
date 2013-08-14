function markNotificationAsReadUnread() {
	$(document).on('click', '.mark-notification-as-read', function() {
		var target = $(this).attr('data-notification-target');
		// Implicitly checking whether markAsUnread is true or false
		var markAsRead = $(this).children('a').hasClass('mark-as-read');
	    $.ajax({
	    	type: 'PUT',
	    	url: prepareURL("notifications/" + target + "?read=" + markAsRead),
	    	success: function(data) {
	    		if (data.errors) {
	    			alert(data.errors);
	    		} else {
	    			$("#" + target).parent().slideUp(500, function() {
	    				$(this).remove();
	    			});
	    		}
	    	},
	    	error: function() {
	    		alert('Error: could not perform AJAX request');
	    	}
	    });
	});
}