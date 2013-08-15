function deleteGroup() {
	$(".delete-group").click(function() {
		var groupID = $(this).attr("id");
		$.ajax({
            type: 'DELETE',
            url: prepareURL("groups/" + groupID),
            success: function(data) {
            	router.navigate(data.redirectTo, {trigger: true});
            }
		});	
	});
}

function editGroup() {
	submitAjaxForm("editGroupForm", "editGroupSection", "dashboard.groups.edit");
}