moduleScripts['dashboard'] = {
	'home' : {
		'index': [
            bindNotificationShowMoreListener,
            markNotificationAsReadUnread
        ]
	},
	'account' : {
		'index': [
            bindSaveSettingsListener
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
		],
		'manage' : [
			editGroup,
//			userTokenInput,		
		],
		'edit' : [
			editGroup,
//			userTokenInput
		]
	},
	'deadlines' : {
		'index' : [
			deadlinesIndex
		],
		'manage' : [
			editDeadline,
			applyDatepicker,
			dUserTokenInput,
			dGroupTokenInput
		],
		'edit' : [
			editDeadline,
			applyDatepicker,
			dUserTokenInput,
			dGroupTokenInput
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
	        dUserTokenInput,
			dGroupTokenInput,
			gUserTokenInput,
//			gUserSurnameTokenInput,
			gGroupTokenInput,
			tokenInputType
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
			dUserTokenInput,
			dGroupTokenInput
		],
		'newgroup' : [
			createGroup,	
	        applyDatepicker,
			gUserTokenInput,
//			gUserSurnameTokenInput,
		], 
		'importgroup' : [
		    importGroup,
		    gGroupTokenInput
		],
	}
};
