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
			userTokenInput,		
		],
		'edit' : [
			editGroup,
			userTokenInput
		]
	},
	'deadlines' : {
		'index' : [
			deadlinesIndex
		],
		'manage' : [
			editDeadline,
			applyDatepicker,
			userTokenInput,
			
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
			userTokenInput,
			userSurnameTokenInput,
			groupTokenInput,
			groupImportTokenInput,
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
	        groupTokenInput,
			userTokenInput	
		],
		'newgroup' : [
			createGroup,	
	        applyDatepicker,
			userTokenInput,
			userSurnameTokenInput,
			tokenInputType	
		], 
		'importgroup' : [
		    importGroup,
		    groupImportTokenInput
		],
	}
};
