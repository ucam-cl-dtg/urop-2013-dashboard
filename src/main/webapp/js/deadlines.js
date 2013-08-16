function deadlinesIndex() {
        // Toggle deadline complete
        $(".complete_deadline").click(function() {
        var deadline_id = $(this).parents("form").attr("id");
        // Ajax update request
            $.ajax({
                  type: 'PUT',
                  url: prepareURL("deadlines/" + deadline_id + "/complete"),
                  success: function(resultData) {
                	  console.log(resultData);
                	  if(resultData.complete){
                		  $("#"+deadline_id).find(".complete_deadline").addClass("button success");
                	  } else {
                		  $("#"+deadline_id).find(".complete_deadline").removeClass("success");                		  
                	  }
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
            	router.navigate(data.redirectTo, {trigger: true});
            } 
		});	
	});
}

function editDeadline() {
	submitAjaxForm("editDeadlineForm", ".main", "dashboard.deadlines.manage");
}