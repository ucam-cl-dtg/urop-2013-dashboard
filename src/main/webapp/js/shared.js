function tabMemory() {
	$(document).on('click', 'section .title', function() {
		if ( $(this).parent().parent().attr('data-base-path') ) {
			var location = $(this).parent().parent().attr('data-base-path') + "/" + $(this).children('a').attr('data-target');
			router.navigate(location, {trigger: false});
		}
	});
}

function submitAjaxForm(form, section, template){
	var options = {
			beforeSubmit: function(){
				$('input[type=submit]', '#'+form).attr('disabled', 'disabled');
				$('input[type=submit]', '#'+form).addClass('secondary');
			},
			success: function(data) {
				applyTemplate($('#'+section), template, data);
			}		
	};
	$('#'+form).ajaxForm(options);	
}

function bindPaginationShowMoreListener() {
	
	$(document).on('click', '.show-more-pagination', function(e) {
		e.preventDefault();
		
		if (!$(this).hasClass('disabled')) {
			$(this).addClass('disabled');
			
			var $elem = $('.pagination-feed');
			var offset = Number($elem.attr('data-offset'));
			var limit = Number($elem.attr('data-limit'));
			var total = Number($elem.attr('data-total'));
			var newOffset = offset + limit;
			
			$elem.attr('data-offset', newOffset);	
			
			var queryString = 'offset=' + newOffset + '&limit=' + limit;
			var $targetElem = $('#new-pagination-wrapper');
			var location = $elem.attr('data-location') + '?' + queryString;
			var template = $elem.attr('data-template');	
			
			loadModule($targetElem, location, template, function() {
				var $newItems = $targetElem.clone();
				$targetElem.empty();
				$('.pagination-feed').append($newItems.html());
				
				// Check if the number of items returned is the limit, otherwise
				// the number returned must be lower than the limit, and therefore
				// the list is exhausted.
				if ($newItems.children().length == limit) {
					// If the number returned is the limit, check if the total has
					// been reached.
					if (newOffset + limit == total) {
						noMoreNotifications();
					} else {
						$('.show-more-pagination').removeClass('disabled');
					}
				} else {
					noMorePagination();
				}
			});
			
		}
		
	});
	
}

function noMorePagination() {
	$('.show-more-pagination').text('No more results');
}

function applyDatepicker() {
	$(".datepicker").datepicker({dateFormat: "dd/mm/yy"});
}

function applyTokenInput(element, type, path, tokenValue, propertyToSearch, resultFormat, tokenFormat){
	
	if(type=="user"){
		var hintText = "Search for a user";
	} else if(type=="group"){
		var hintText = "Serach for a group";
	} else {
		var intText = "Start typing to search";
	}
	
	$("."+ element).tokenInput(prepareURL(path), {
		theme: "facebook",
		method: "post",
        tokenValue: tokenValue,
        propertyToSearch: propertyToSearch,
        theme: "facebook",
        minChars: 1,
        hintText: hintText,
        resultsLimit: 10,
        preventDuplicates: true,
        
        resultsFormatter: resultFormat,
        tokenFormatter: tokenFormat,   
		});   
		
	if(type=="user"){
		if (data = (target = $("."+element)).data("prepopulate")) {
			$.each(data.users, function(i,v) {
				target.tokenInput("add",{crsid: v.crsid, name: v.name});
			});
		}
	} else if(type=="group") {
		if (data = (target = $("."+element)).data("prepopulate")) {
			$.each(data.users, function(i,v) {
				target.tokenInput("add",{id: v.id, name: v.name});
			});
		}		
	}
}

function gUserTokenInput() {
	var resultFormat = function(item){ return "<li>" + "<div style='display: inline-block; padding-left: 10px;'><div class='full_name'>" + item.name + " (" + item.crsid + ")</div><div class='email'>" + item.crsid + "@cam.ac.uk</div></div></li>"; };
	var tokenFormat = function(item) { return "<li><p>" + item.name + " (" + item.crsid + ")</p></li>"; };
	
	applyTokenInput("group-user-token-input", "user", "groups/queryCRSID", "crsid", "crsid", resultFormat, tokenFormat);
}

function dUserTokenInput() {
	var resultFormat = function(item){ return "<li>" + "<div style='display: inline-block; padding-left: 10px;'><div class='full_name'>" + item.name + " (" + item.crsid + ")</div><div class='email'>" + item.crsid + "@cam.ac.uk</div></div></li>"; };
	var tokenFormat = function(item) { return "<li><p>" + item.name + " (" + item.crsid + ")</p></li>"; };
	
	applyTokenInput("deadline-user-token-input", "user", "groups/queryCRSID", "crsid", "crsid", resultFormat, tokenFormat);
}

function dGroupTokenInput() {
	var resultFormat = function(item){ return "<li>" + "<div style='display: inline-block; padding-left: 10px;'><div class='full_name'>" + item.group_name + "</div></div></li>";};
	var tokenFormat = function(item) { return "<li><p>" + item.group_name + "</p></li>"; };   
	
	applyTokenInput("deadline-group-token-input", "group", "deadlines/queryGroup", "group_id", "group_name", resultFormat, tokenFormat);
}

function gGroupTokenInput() {
	var resultFormat = function(item){ return "<li>" + "<div style='display: inline-block; padding-left: 10px;'><div class='full_name'>" + item.name + "</div></div></li>";};
	var tokenFormat = function(item) { return "<li><p>" + item.name + " (" + item.crsid + ")</p></li>"; };   
	
	applyTokenInput("import-group-token-input", "group", "groups/queryGroup", "id", "name", resultFormat, tokenFormat);
}

function tokenInputType() {
	var path = "groups/queryCRSID"; //default path
	var property = "crsid";
	$(".token-type-radio").change(function() {
		var type = $(this).val();	
		if(type=="crsid"){
			path = "groups/queryCRSID";
			property = "crsid";
		} else if(type=="surname"){
			path = "groups/querySurname";
			property = "surname";
		}
		
		$(this).parents("form").find("input[name='users']").tokenInput("setOptions", {url: prepareURL(path), propertyToSearch: property});
	});
}
