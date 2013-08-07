function deadlinesIndex() {
      
        // Toggle deadline archived
        $(".archive_deadline").click(function() {
        var deadline_id = $(this).parents("form").attr("id");
        // Ajax update request
            var archiveData = $.ajax({
                  type: 'PUT',
                  url: prepareURL("dashboard/deadlines/" + deadline_id + "/archive"),
                  success: function(resultData) {
                      alert("archived");
                  }
            });
        });
        
        // Toggle deadline complete
        $(".complete_deadline").click(function() {
        var deadline_id = $(this).parents("form").attr("id");
        // Ajax update request
            var archiveData = $.ajax({
                  type: 'PUT',
                  url: prepareURL("dashboard/deadlines/" + deadline_id + "/complete"),
                  success: function(resultData) {
                      alert("complete");
                  }
            });
        });
  
}

function editDeadline() {
	$("#editDeadlineForm").ajaxForm(function(data) {
		applyTemplate($('#editDeadlineSection'), "dashboard.deadlines.edit", data);
	});
}