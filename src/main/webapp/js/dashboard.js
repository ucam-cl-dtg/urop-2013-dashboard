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
			createDeadline,	
	        editDeadline,
	        deleteDeadline,
	        editGroup,
	        deleteGroup,
	        applyDatepicker,
	        autocomplete	
		],
		'deadline' : [
		  	editDeadline,
			deleteDeadline,
			autocomplete,
			applyDatepicker
		],
		'newdeadline' : [
			createDeadline,	
	        applyDatepicker,
	        autocomplete	
		]
	}
};
