//选择零售
function changeChanges(obj) {
	$this = $(obj);//银行  邮局   现金 2 1 3
	var val = $this.val();
	$('#userSelectChanges').val(val);
	if (val == '2'||val == '1') {// 非现金
		$('.noChangesCss').show();
		$('.yjChangesCss').show();
		$('.cashChangesCss').hide();
	} else {
		$('.noChangesCss').hide();
		$('.yjChangesCss').hide();//录入时间
		$('.cashChangesCss').show();//订单下单时间
	}
}

// 选择绑定状态
function changeBindState(obj) {
	$this = $(obj);
	var val = $this.val();
	$('#userSelectBindSta').val(val);
}

function qryFananceSatuation() {
	var condition = {};
	condition.isSale = $('#userSelectChanges').val();
	if (condition.isSale == '1'||condition.isSale == '2') {
		condition.payNo = $('#paymentNo').val();
		condition.payName = $('#paymentName').val();
		condition.bindflag = $('#userSelectBindSta').val();
		condition.start = $('#startTime').val();
		condition.end = $('#endTime').val();
		condition.start1 = $('#startTime1').val();
		condition.end1 = $('#endTime1').val();
	}else{
		condition.orderStart=$('#orderTimeStart').val();
		condition.orderEnd=$('#orderTimeEnd').val();
	}

	jQuery.startLoading('请稍候...');
	jQuery.ajax({
		url : "/ccms1/fananceMoneySum",
		type : 'POST',
		dataType : "json",
		data : {
			"condition" : $.toJSON(condition)
		},
		success : function(result) {
			jQuery.endLoading();
			if (result.MONEY_NOT_PAY == undefined|| result.MONEY_NOT_PAY == "0") {
				result.MONEY_NOT_PAY = 0;
			}
			if (result.MONEY_HAS_PAY == undefined|| result.MONEY_HAS_PAY == "0") {
				result.MONEY_HAS_PAY = 0;
			}
			if (condition.isSale == '1'||condition.isSale == '2') {// 非现金
				var total = parseInt(result.MONEY_NOT_PAY)+ parseInt(result.MONEY_HAS_PAY);
				$("#count1").text("汇款单总金额：" + total + "（元）");
				$("#count2").text("已绑定总金额：" + result.MONEY_HAS_PAY + "（元）");
				$("#count1").show();
			} else {
				$("#count1").hide();
				$("#count2").text("已收款总金额：" + result.MONEY_HAS_PAY + "（元）");
			}
			jQuery.startLoading('请稍候...');
			selectPaymentInfo(condition);
			
		}
	});
}

function selectPaymentInfo(condition) {
	
	if (condition.isSale == '1'||condition.isSale == '2') {//非现金
		var url = "/ccms1/qryOrderPaySituationNoneCash";
		jQuery.ajax({
			url : url,
			type : 'POST',
			data : {
				"condition" : $.toJSON(condition)
			},
			success : function(result) {
				jQuery.endLoading();
				$('#pagesList1').empty().html(result);
				$('#pagesList1').show();
				$('#pagesList2').empty().hide();
				$("#btn0").show();
				$("#btn1").hide();
			}
		});
	} else {
		var url = "/ccms1/qryOrderPaySituationCash";
		jQuery.ajax({
			url : url,
			type : 'POST',
			data : {
				"condition" : $.toJSON(condition)
			},
			success : function(result) {
				jQuery.endLoading();
				$('#pagesList2').empty().html(result);
				$('#pagesList2').show();
				$('#pagesList1').empty().hide();
				$("#btn1").show();
				$("#btn0").hide();
			}
		});
	}

}
