moduleScripts['dashboard'] = {
	'home' : {
		'index': [
            bindPaginationShowMoreListener,
            markNotificationAsReadUnread
        ]
	},
	'account' : {
		'index': [
            bindSaveSettingsListener,
            bindNewApiKeyListener,
            bindDeleteApiKeyListener
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
		'manageindex' : [
		    createDeadline,
		    applyDatepicker,
		    dUserTokenInput,
		    dGroupTokenInput,
		    tokenInputType		    
		],
		'newdeadline' : [
		    createDeadline,
		    applyDatepicker,
		    dUserTokenInput,
		    dGroupTokenInput,
		    tokenInputType
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
	      	createGroup,
	      	importGroup,
	        applyDatepicker,
			dGroupTokenInput,
			gUserTokenInput,
			gGroupTokenInput,
			sUserTokenInput,
			tokenInputType,
			addSupervisor
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
