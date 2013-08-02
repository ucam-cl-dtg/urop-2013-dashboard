moduleScripts['dashboard']['supervisor'] = {
    'index' : 
        [
            function() {
            // Delete group
              $(".group_delete").click(function() {
              var group_id = $(this).parents("form").attr("id");
              // Ajax delete request
                  var deleteData = $.ajax({
                        type: 'DELETE',
                        url: "/dashboard/groups/" + group_id,
                        success: function(resultData) {
                            $("#"+group_id).hide(2000, function() {
                                $(this).remove();
                            });
                        }
                  });
              });
              
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
              
              // Create deadline datepicker
              $(".datepicker").datepicker({dateFormat: "dd/mm/yy"});
              
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
              
              
              // Delete deadline
              $(".deadline_delete").click(function() {
              var deadline_id = $(this).parents("form").attr("id");
              // Ajax delete request
                  var deleteData = $.ajax({
                        type: 'DELETE',
                        url: "/dashboard/deadlines/" + deadline_id,
                        success: function(resultData) {
                            if(resultData.success==true){
                                $("#"+resultData.id).hide(2000, function() {
                                    $(this).remove();
                                });
                                $("#d_"+resultData.id).hide(2000, function() {
                                    $(this).remove();
                                });
                            }
                        }
                  });
              });
              
               // Edit deadline
              $(".deadline_edit").click(function() {
                var deadline_id = $(this).parents('form').attr("id");
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
        ]
}