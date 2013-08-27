moduleScripts['dashboard'] = {
	'home' : {
		'index': [
            bindPaginationShowMoreListener,
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
			bindPaginationShowMoreListener,
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
		],
		'manage' : [
			editGroup,
			deleteGroup,
			gUserTokenInput,
			tokenInputType
		],
		'edit' : [
			editGroup,
			gUserTokenInput,
			tokenInputType
		],
		'delete' : [
			deleteGroup
		]
	},
	'deadlines' : {
		'index' : [
			deadlinesIndex,
			bindPaginationShowMoreListener
		],
		'manage' : [
			editDeadline,
			deleteDeadline,
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
		],
		'delete' : [
		    deleteDeadline
		]
	},
	'supervisor' : {
		'index' : [
		    tabMemory,
			createDeadline,	
	      	createGroup,
	      	importGroup,
	        applyDatepicker,
	        dUserTokenInput,
			dGroupTokenInput,
			gUserTokenInput,
			gGroupTokenInput,
			sUserTokenInput,
			tokenInputType,
			addSupervisor
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
		'addsupervisor' : [
		    sUserTokenInput,
		    addSupervisor
		]
	}
};
