$(function(){
	$('.relateCss').bind('click',function(){
		var value = $(this).attr('relateVal');
		$(".relateCss").removeClass('on');
		$(this).addClass('on');
		if(value=='1'){//领导审批
			shoWaitLdCheck();
		}else if(value=='2'){//财务收款
			shoWCwConfirmPay();
		}

	});
	showOrderToBind();
});
//显示待绑定的菜单
function showOrderToBind(){
	jQuery.ajax({
		type : 'POST',
		url : '/ccms1/showFxtoDoInfo',
		
		success : function(data) {
			$('#faxingTodoTable').empty().append(data);
			shoWaitLdCheck();
		}
	});
}

//显示需要领导审核 相关
function shoWaitLdCheck(){
	jQuery.ajax({
		type : 'POST',
		url : '/ccms1/showFxLdVerify',
		
		success : function(data) {
			$('#fxlingdaoshenpi').empty().append(data);
			$('#fxrelateld').show();
			
			$('#fxcaiwushoukuan').empty()
			$('#fxrelatecw').hide();//隐藏财务收款区域
			
		}
	});
}

//需要财务收款
function shoWCwConfirmPay(){
	jQuery.ajax({
		type : 'POST',
		url : '/ccms1/showFxcwVerify',
		
		success : function(data) {
			$('#fxcaiwushoukuan').empty().append(data);
			$('#fxrelatecw').show();
			
			$('#fxlingdaoshenpi').empty()
			$('#fxrelateld').hide();//隐藏领导审批区域
			
		}
	});
}


