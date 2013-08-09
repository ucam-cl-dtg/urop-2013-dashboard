function bindNotificationShowMoreListener() {
	
	$('#show-more-notifications').on('click', function(e) {
		e.preventDefault();
		
		if (!$(this).hasClass('disabled')) {
			$(this).addClass('disabled');
			
			var $elem = $('.notification-feed');
			var offset = Number($elem.attr('data-offset'));
			var limit = Number($elem.attr('data-limit'));
			var total = Number($elem.attr('data-total'));
			var newOffset = offset + limit;
			
			$('.notification-feed').attr('data-offset', newOffset);	
			
			var queryString = 'offset=' + newOffset + '&limit=' + limit;
			var elem = $('#new-notifications-wrapper');
			var location = 'notifications?' + queryString;
			var template = 'shared.dashboard.getNotifications';	
			
			loadModule(elem, location, template, function() {
				var $newItems = $('#new-notifications-wrapper').clone();
				$('#new-notifications-wrapper').empty();
				$('.notification-feed').append($newItems.html());
				
				// Check if the number of items returned is the limit, otherwise
				// the number returned must be lower than the limit, and therefore
				// the list is exhausted.
				if ($newItems.children().length == limit) {
					// If the number returned is the limit, check if the total has
					// been reached.
					if (newOffset + limit == total) {
						noMoreNotifications();
					} else {
						$('#show-more-notifications').removeClass('disabled');
					}
				} else {
					noMoreNotifications();
				}
			});
			
		}
		
	});
	
}

function noMoreNotifications() {
	$('#show-more-notifications').text('No more notifications');
}

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