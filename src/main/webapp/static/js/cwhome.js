$(function(){
	$('.todoCss').bind('click',function(){
		var value = $(this).attr('todoVal');
		$(".todoCss").removeClass('on');
		$(this).addClass('on');
		if(value=='1'){//收款
			cwToGetPay();
		}else if(value=='2'){//打发票
			cwToPrintInvoice();
		}

	});
	//默认展示收款
	cwToGetPay();
});

//财务收款
function cwToGetPay(){
	jQuery.ajax({
		type : 'POST',
		url : '/ccms1/cwToGetPay',
		
		success : function(data) {
			$('#cwGetPayTable').empty().append(data);
			
			$('#getPayDiv').show();
			$('#invoiceTypeDiv').hide();//隐藏带打印发票区域
			
			//财务没有关联的项目
		}
	});
}

//财务打印发票
function cwToPrintInvoice(){
	jQuery.ajax({
		type : 'POST',
		url : '/ccms1/cwPrintInvoice',
		
		success : function(data) {
			$('#invoiceTypeTable').empty().append(data);
			
			$('#invoiceTypeDiv').show();
			$('#getPayDiv').hide();//隐藏代收款区域
			
			//财务没有关联的项目
		}
	});
}