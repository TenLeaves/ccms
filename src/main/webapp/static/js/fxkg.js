$(function(){
	//待做事项切换
	$('.todoCss').bind('click',function(){
		var value = $(this).attr('todoVal');
		$(".todoCss").removeClass('on');
		$(this).addClass('on');
		if(value=='1'){//绑定
			OrderToBind();
		}else if(value=='2'){//印刷确认入库
			showPrintIn();
		}

	});
	
	//相关事项切换
	$('.relateCss').bind('click',function(){
		var value = $(this).attr('relateVal');
		$(".relateCss").removeClass('on');
		$(this).addClass('on');
		if(value=='1'){//领导审批
			LdToCheck();
		}else if(value=='2'){//财务收款
			CwConfirmPay();
		}else if(value='3'){
			KgPrintIn();
		}

	});
	
	OrderToBind();
});

function OrderToBind(){
	jQuery.ajax({
		type : 'POST',
		url : '/ccms1/showFxtoDoInfo',
		
		success : function(data) {
			$('#faxingTodoTable').empty().append(data);
			$('#bindPayDivId').show();
			$('#printInDivId').hide();
			
			LdToCheck();//默认初始化第一个标签
		}
	});
}
function showPrintIn(){
	jQuery.ajax({
		type : 'POST',
		url : '/ccms1/showPrintTobeIn',
		
		success : function(data) {
			$('#kuguanTodoTable').empty().append(data);
			
			$('#bindPayDivId').hide();
			$('#printInDivId').show();
		}
	});
}

function LdToCheck(){
	jQuery.ajax({
		type : 'POST',
		url : '/ccms1/showFxLdVerify',
		
		success : function(data) {
			$('#fxlingdaoshenpi').empty().append(data);
			$('#fxrelateld').show();
			$('#fxrelatecw').hide();//隐藏财务收款区域
			$('#kgrelate').hide();//隐藏库管区域
			
		}
	});
}

function CwConfirmPay(){
	jQuery.ajax({
		type : 'POST',
		url : '/ccms1/showFxcwVerify',
		
		success : function(data) {
			$('#fxcaiwushoukuan').empty().append(data);
			$('#fxrelatecw').show();
			
			$('#fxrelateld').hide();//隐藏领导审批区域
			$('#kgrelate').hide();//库管相关
			
		}
	});
}

function KgPrintIn(){
	jQuery.ajax({
		type : 'POST',
		url : '/ccms1/showTypedInvoice',
		
		success : function(data) {
			$('#kgrelateTable').empty().append(data);
			$('#kgrelate').show();
			
			$('#fxrelateld').hide();//隐藏领导审批区域
			$('#fxrelatecw').hide();//隐藏财务收款区域
		}
	});
}
