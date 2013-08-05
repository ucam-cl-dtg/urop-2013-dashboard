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
	        autocomplete,
	        editGroup,
	        deleteGroup,
	        applyDatepicker
		]	
	}
}