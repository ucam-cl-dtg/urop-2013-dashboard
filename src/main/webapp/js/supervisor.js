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
		applyTemplate($('#newGroupSection'), "dashboard.supervisor.newgroup", data);
	});
}

function deleteDeadline() {
    $(".deadline_delete").click(function() {
        var str_id = $(this).parents('form').attr("id");
        var deadline_id = str_id.substring(2);
        var deleteData = $.ajax({
            type: 'DELETE',
            url: prepareURL("dashboard/deadlines/") + deadline_id,
            success: function(resultData) {
                if(resultData.success==true){
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
        alert("group id" + group_id);
        var deleteData = $.ajax({
            type: 'DELETE',
            url: prepareURL("dashboard/groups/") + group_id,
            success: function(resultData) {
                if(resultData.success==true){
                    $("#g_"+resultData.id).hide(2000, function() {
                        $(this).remove();
                    });
                }
            }
        });
    });
}

function autocomplete() {

    $(".deadline_group_token_input").tokenInput(prepareURL("dashboard/deadlines/queryGroup"), {
        method: "post",
        tokenValue: "group_id",
        propertyToSearch: "group_name",
        theme: "facebook",
        hintText: "Search your groups",
        resultsLimit: 10,
        preventDuplicates: true           
    });
    $(".exgroup_token_input").tokenInput(prepareURL("dashboard/groups/queryGroup"), {
        method: "post",
        tokenValue: "id",
        propertyToSearch: "name",
        theme: "facebook",
        tokenLimit : 1,
        
        resultsFormatter: function(item){ return "<li>" + "<div style='display: inline-block; padding-left: 10px;'><div class='full_name'>" + item.name + "</div><div class='email'>" + item.description + "</div></div></li>" },
        tokenFormatter: function(item) { return "<li><p>" + item.name + "</p></li>" },
    });
      
}

