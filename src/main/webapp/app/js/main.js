$(document).ready(function() {

	//
	// Sidebar dropdowns
	//

	$('.sidebar-navigation-item-header').on('click', function() {
		$(this).next('.sidebar-sub-navigation').slideToggle();
	});

    // 
    // Support for .module-loader
    //

    $(document).on("click", ".module-loader", function() {
        var location = $(this).attr('data-target');
        router.navigate(location, {trigger: true});
    });
    
	//
	// Mobile navigation bar
	//

	$('.mobile-nav-options-button').on('click', function() {
		$('.mobile-nav-wrapper').slideToggle();
	});

	$('.mobile-nav-wrapper .module-loader').on('click', function() {
		$('.mobile-nav-wrapper').slideUp();
	});

	//
	// Essential event listeners
	//

	$(document).on('click', '.expand-sub-panel', function() {
		$(this).closest('.list-panel').siblings('.sub-panel')
			.slideToggle().toggleClass('hidden');
	});

	$(document).on('click', '.upload-marked-work', function() {
		$('.upload-marked-work-form').slideToggle();
	});

	// AJAX loader
	/*
	$(document).ajaxStart(function() {
		$(".ajax-loader").clearQueue().fadeIn(200);
	}).ajaxComplete(function() {
		$(".ajax-loader").clearQueue().fadeOut(200);
	});
	*/
});
