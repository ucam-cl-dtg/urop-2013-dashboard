moduleScripts['dashboard']['deadlines'] = {
	'index' : 
		[
	    function() {
    	      
              // Delete deadline
              $(".deadline_delete").click(function() {
              var deadline_id = $(this).parents("form").attr("id");
              // Ajax delete request
                  var deleteData = $.ajax({
                        type: 'DELETE',
                        url: "/dashboard/deadlines/" + deadline_id,
                        success: function(resultData) {
                            $("#"+deadline_id).hide(2000, function() {
                                $(this).remove();
                            });
                            $("#d_"+deadline_id).hide(2000, function() {
                                $(this).remove();
                            });
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