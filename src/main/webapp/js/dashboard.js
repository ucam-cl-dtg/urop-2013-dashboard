moduleScripts['dashboard'] = {
	'home' : {
		'index': [
            bindNotificationShowMoreListener,
            markNotificationAsReadUnread
        ]
	},
	'notifications' : {
		'index': [
			bindNotificationShowMoreListener,
            markNotificationAsReadUnread
		]
	},
	'shared' : {
		'getNotifications': [
            markNotificationAsReadUnread
        ]
	},
	'groups' : {
		'index': [
			groupsIndex
		]
	},
	'deadlines' : {
		'index' : [
			deadlinesIndex
		],
		'edit' : [
			applyDatepicker
		]
	},
	'supervisor' : {
		'index' : [
	        editDeadline,
	        deleteDeadline,
	        autocomplete,
	        editGroup,
	        deleteGroup,
	        applyDatepicker
		],
		'deadline' : [
		  	editDeadline,
			deleteDeadline,
			autocomplete,
			applyDatepicker
		]
	}
};