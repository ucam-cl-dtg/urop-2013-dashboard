function groupsIndex() {
  $(".group_delete").click(function() {
  var group_id = $(this).parents("form").attr("id");
  // Ajax delete request
      var deleteData = $.ajax({
            type: 'DELETE',
            url: prepareURL("dashboard/groups/" + group_id),
            success: function(resultData) {
                $("#"+group_id).hide(2000, function() {
                    $(this).remove();
                });
            }
      });
  });
}

function editGroup() {
	$("#editGroupForm").ajaxForm(function(data) {
		applyTemplate($('#editGroupSection'), "dashboard.groups.edit", data);
	});
}