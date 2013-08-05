function editDeadline() {

    $(".deadline_edit").click(function() {
        var str_id = $(this).parents('form').attr("id");
        var deadline_id = str_id.substring(2);
        alert("deadline id" + deadline_id);
        var panel = $(this).parents('form');
        var subpanel = $(this).parents('form').find('.deadline_subpanel');
        loadModule(subpanel, "dashboard/deadlines/"+deadline_id+"/edit", "dashboard.deadlines.edit", function(){
            //expand subpanel
            var updateForm = "#d_edit_"+deadline_id;         
            $(updateForm).ajaxForm({
                type: 'POST',
                url: "/dashboard/deadlines/" + deadline_id + "/edit",
                success: function() {
                    loadModule(panel, "dashboard/deadlines/"+deadline_id+"/edit", "dashboard.supervisor.deadline", function(){
                    })    
                }
            });
        });
    });

}

function deleteDeadline() {

    $(".deadline_delete").click(function() {
        var str_id = $(this).parents('form').attr("id");
        var deadline_id = str_id.substring(2);
        alert("deadline id" + deadline_id);
        var deleteData = $.ajax({
            type: 'DELETE',
            url: "/dashboard/deadlines/" + deadline_id,
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

function autocomplete() {

    $(".deadline_user_token_input").tokenInput("/dashboard/groups/queryCRSID", {
        method: "post",
        tokenValue: "crsid",
        propertyToSearch: "crsid",
        theme: "facebook",
        minChars: 3,
        hintText: "Search for a user",
        resultsLimit: 10,
        preventDuplicates: true,
                               
        resultsFormatter: function(item){ return "<li>" + "<div style='display: inline-block; padding-left: 10px;'><div class='full_name'>" + item.name + " (" + item.crsid + ")</div><div class='email'>" + item.crsid + "@cam.ac.uk</div></div></li>" },
        tokenFormatter: function(item) { return "<li><p>" + item.name + " (" + item.crsid + ")</p></li>" },                           
    });
    $(".deadline_group_token_input").tokenInput("/dashboard/deadlines/queryGroup", {
        method: "post",
        tokenValue: "group_id",
        propertyToSearch: "group_name",
        theme: "facebook",
        hintText: "Search your groups",
        resultsLimit: 10,
        preventDuplicates: true           
    });

    // Autocomplete
    $(".member_token_input").tokenInput("/dashboard/groups/queryCRSID", {
        method: "post",
        tokenValue: "crsid",
        propertyToSearch: "crsid",
        theme: "facebook",
        minChars: 3,
        hintText: "Search for a user",
        resultsLimit: 10,
        preventDuplicates: true,
            
        resultsFormatter: function(item){ return "<li>" + "<div style='display: inline-block; padding-left: 10px;'><div class='full_name'>" + item.name + " (" + item.crsid + ")</div><div class='email'>" + item.crsid + "@cam.ac.uk</div></div></li>" },
        tokenFormatter: function(item) { return "<li><p>" + item.name + " (" + item.crsid + ")</p></li>" },                           
    });
    $(".exgroup_token_input").tokenInput("/dashboard/groups/queryGroup", {
        method: "post",
        tokenValue: "id",
        propertyToSearch: "name",
        theme: "facebook",
        tokenLimit : 1,
        
        resultsFormatter: function(item){ return "<li>" + "<div style='display: inline-block; padding-left: 10px;'><div class='full_name'>" + item.name + "</div><div class='email'>" + item.description + "</div></div></li>" },
        tokenFormatter: function(item) { return "<li><p>" + item.name + "</p></li>" },
    });
      
}

function editGroup() {

    $(".group_edit").click(function() {
        var str_id = $(this).parents('form').attr("id");
        var group_id = str_id.substring(2);
        alert("group id" + group_id);
        var panel = $(this).parents('form');
        var subpanel = $(this).parents('form').find('.group_subpanel');
        loadModule(subpanel, "dashboard/groups/"+group_id+"/edit", "dashboard.groups.edit", function(){
            //expand subpanel
            var updateForm = "#g_edit_"+group_id;   
            alert("update "+group_id);      
            $(updateForm).ajaxForm({
                type: 'POST',
                url: "/dashboard/groups/" + group_id + "/edit",
                success: function() {
                    alert("group updated");
                    loadModule(panel, "dashboard/groups/"+group_id+"/edit", "dashboard.supervisor.group", function(){
                    })    
                }
            });
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
            url: "/dashboard/groups/" + group_id,
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