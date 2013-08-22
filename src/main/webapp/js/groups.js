function deleteGroup() {
	$(".delete-group").click(function() {
		var groupID = $(this).attr("id");
		$.ajax({
            type: 'DELETE',
            url: prepareURL("groups/" + groupID),
            success: function(data) {
            	successNotification("Succesfully deleted group");
            	router.navigate(data.redirectTo, {trigger: true});
            },
        	error: function() {
        		errorNotification("Could not delete group");
        	}
		});	
	});
}

function editGroup() {
	submitAjaxForm("editGroupForm", ".main", "dashboard.groups.manage", "Successfully edited group");
}