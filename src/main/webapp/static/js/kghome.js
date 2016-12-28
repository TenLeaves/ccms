$(function(){
	showPrintToBeIn();
});
function showPrintToBeIn(){
	jQuery.ajax({
		type : 'POST',
		url : '/ccms1/showPrintTobeIn',
		
		success : function(data) {
			$('#kuguanTodoTable').empty().append(data);
			showkgRelate();
		}
	});
}

function showkgRelate(){
	jQuery.ajax({
		type : 'POST',
		url : '/ccms1/showTypedInvoice',
		
		success : function(data) {
			$('#kgrelateTable').empty().append(data);
		}
	});
}


