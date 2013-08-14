function createDeadline() {
	$("#newDeadlineForm").ajaxForm(function(data) {
		applyTemplate($('#newDeadlineSection'), "dashboard.supervisor.newdeadline", data);
	});
}
function createGroup() {
	$("#newGroupForm").ajaxForm(function(data) {
		applyTemplate($('#newGroupSection'), "dashboard.supervisor.newgroup", data);
	});
}
function importGroup() {
	$("#importGroupForm").ajaxForm(function(data) {
		applyTemplate($('#importGroupSection'), "dashboard.supervisor.importgroup", data);
	});
}

function deleteDeadline() {
    $(".deadline_delete").click(function() {
        var str_id = $(this).parents('form').attr("id");
        var deadline_id = str_id.substring(2);
        $.ajax({
            type: 'DELETE',
            url: prepareURL("deadlines/") + deadline_id,
            success: function(resultData) {
                if(resultData.success){
                    $("#d_"+resultData.id).hide(2000, function() {
                        $(this).remove();
                    });
                }
            }
        });
    });
}

function deleteGroup() {
    $(".group_delete").click(function() {
        var str_id = $(this).parents('form').attr("id");
        var group_id = str_id.substring(2);
        $.ajax({
            type: 'DELETE',
            url: prepareURL("groups/") + group_id,
            success: function(resultData) {
                if(resultData.success){
                    $("#g_"+resultData.id).hide(2000, function() {
                        $(this).remove();
                    });
                }
            }
        });
    });
}

