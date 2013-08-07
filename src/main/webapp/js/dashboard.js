moduleScripts['dashboard'] = {
	'notifications' : {
		'index': [
			bindNotificationShowMoreListener,
			markNotificationAsRead
		]
	},
	'shared' : {
		'getNotifications': [
            markNotificationAsRead
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