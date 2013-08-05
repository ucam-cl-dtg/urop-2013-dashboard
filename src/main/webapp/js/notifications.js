function bindNotificationShowMoreListener() {
	
	$('#show-more-notifications').on('click', function(e) {
		e.preventDefault();
		
		if (!$(this).hasClass('disabled')) {
			$(this).addClass('disabled');
			
			var offset = Number($('.notification-feed').attr('data-offset'));
			var limit = Number($('.notification-feed').attr('data-limit'));
			var newOffset = offset + limit;
			$('.notification-feed').attr('data-offset', newOffset)
		
			var queryString = 'offset=' + newOffset + '&limit=' + limit;
		
			getNotifications(queryString, limit);
		}
		
	});
	
}

function getNotifications(queryString, limit) {
	var elem = $('#new-notifications-wrapper');
	var location = 'dashboard/notifications?' + queryString;
	var template = 'dashboard.notifications.getNotifications';	
	
	loadModule(elem, location, template, function() {
		var $newItems = $('#new-notifications-wrapper').clone();
		$('#new-notifications-wrapper').empty();
		$('.notification-feed').append($newItems.html());
		
		if ($newItems.children().length == limit) {
			$('#show-more-notifications').removeClass('disabled');
		} else {
			$('#show-more-notifications').text('No more notifications');
		}
	});
}