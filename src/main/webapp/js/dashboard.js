moduleScripts['dashboard'] = {
	'notifications' : {
		'getNotificationsController': [
			bindNotificationShowMoreListener
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
	        deadlineAutocomplete,
	        createGroup,
	        deleteGroup,
	        applyDatepicker
		]	
	}
}