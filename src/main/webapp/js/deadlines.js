moduleScripts['dashboard']['deadlines'] = {
	'index' : 
		[
	    function() {
    	      
              // Toggle deadline archived
              $(".archive_deadline").click(function() {
              var deadline_id = $(this).parents("form").attr("id");
              // Ajax update request
                  var archiveData = $.ajax({
                        type: 'PUT',
                        url: "/dashboard/deadlines/" + deadline_id+"/archive",
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
                        url: "/dashboard/deadlines/" + deadline_id+"/complete",
                        success: function(resultData) {
                            alert("complete");
                        }
                  });
              });
        
	    }        
	  ],
    'edit' : 
      [
        function() {
              $(".datepicker").datepicker({dateFormat: "dd/mm/yy"});
        }        
      ]
}