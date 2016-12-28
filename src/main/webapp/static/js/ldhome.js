$(function(){
	showVerifyItem();
});

function showVerifyItem(){
	jQuery.ajax({
		type : 'POST',
		url : '/ccms1/ldtoDoInfo',
		
		success : function(data) {
			$('#lingdaoTodoTable').empty().append(data);
		}
	});
}