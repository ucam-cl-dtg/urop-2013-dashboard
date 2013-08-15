function deleteGroup() {
	$(".delete-group").click(function() {
		var groupID = $(this).attr("id");
		$.ajax({
            type: 'DELETE',
            url: prepareURL("groups/" + groupID)
		});	
	});
}

function editGroup() {
	submitAjaxForm("editGroupForm", "editGroupSection", "dashboard.groups.edit");
}