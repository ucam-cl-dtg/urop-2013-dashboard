function deadlinesIndex() {
        // Toggle deadline complete
        $(".complete_deadline").click(function() {
        var deadline_id = $(this).parents("form").attr("id");
        // Ajax update request
            $.ajax({
                  type: 'PUT',
                  url: prepareURL("deadlines/" + deadline_id + "/complete"),
                  success: function(resultData) {
            		  successNotification("Successfully updated deadline");
                	  if(resultData.complete){
                		  $("#"+deadline_id).find(".complete_deadline").addClass("button success");
                	  } else {
                		  $("#"+deadline_id).find(".complete_deadline").removeClass("success");                		  
                	  }
                  },
                  error: function() {
                  	errorNotification("Could not update deadline");
                  }
            });
        });
}

function deleteDeadline() {
	$(".delete-deadline").click(function() {
		var deadlineID = $(this).attr("id");
		$.ajax({
            type: 'DELETE',
            url: prepareURL("deadlines/" + deadlineID),
            success: function(data) {
            	successNotification("Successfully deleted deadline");
            	router.navigate(data.redirectTo, {trigger: true});
            },
            error: function() {
            	errorNotification("Could not delete deadline");
            }
		});	
	});
}

function editDeadline() {
	submitAjaxForm("editDeadlineForm", ".main", "dashboard.deadlines.manage", "Successfully edited deadline");
}