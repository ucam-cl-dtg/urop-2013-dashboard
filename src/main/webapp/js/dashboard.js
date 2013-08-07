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
		'manage' : [
			editDeadline,
			applyDatepicker,
			userTokenInput
		],
		'edit' : [
			editDeadline,
			applyDatepicker,
			userTokenInput
		]
	},
	'supervisor' : {
		'index' : [
			createDeadline,	
	        deleteDeadline,
	      	createGroup,
	      	importGroup,
	        deleteGroup,
	        applyDatepicker,
			userTokenInput
		],
		'deadline' : [
			deleteDeadline
		],
		'group' : [
			deleteGroup
		],
		'newdeadline' : [
			createDeadline,	
	        applyDatepicker,
			userTokenInput	
		],
		'newgroup' : [
			createGroup,	
	      	importGroup,
	        applyDatepicker,
			userTokenInput	
		]
	}
};
