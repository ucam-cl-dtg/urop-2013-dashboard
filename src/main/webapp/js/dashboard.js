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
			gUserTokenInput,
			tokenInputType
		],
		'edit' : [
			editGroup,
			gUserTokenInput,
			tokenInputType
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
			dGroupTokenInput,
			tokenInputType
		],
		'edit' : [
			editDeadline,
			applyDatepicker,
			dUserTokenInput,
			dGroupTokenInput,
			tokenInputType
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
			dGroupTokenInput,
			tokenInputType
		],
		'newgroup' : [
			createGroup,	
	        applyDatepicker,
			gUserTokenInput,
			tokenInputType
		], 
		'importgroup' : [
		    importGroup,
		    gGroupTokenInput
		],
	}
};
