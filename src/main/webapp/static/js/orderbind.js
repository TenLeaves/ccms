

function searchPay(){
	var qryType=$('#paymentQryTypeId').val();//1 单号  2 人  3 金额  4 日期
	var val ='';
	if(qryType!='4'){
		val =$('#inputPayKeyWords').val();
	}else{
		val=$('#inputPayKeyWordsTime').val();
	}
	 
	jQuery.ajax({
	      type : 'POST',
	      url : '/ccms1/searchPayment',
	      data : {
	        'qryType' : qryType,
	        'condition' : val
	      },
	      success : function(data)// 当请求成功时触发函数
	      {
	    	 
	        $('#payMentTable').empty().append(data);
	      }
	    });
}


function qryOrderInfo(){
	var qryType=$('#qryOrderTypeValId').val();//1 单号  2 人  3 金额  4 日期
	var val ='';
	if(qryType!='4'){
		val =$('#inputOrderQryKey').val();
	}else{
		val=$('#inputOrderQryKeyTime').val();
	}
	jQuery.ajax({
	      type : 'POST',
	      url : '/ccms1/searchOrderTobind',
	      data : {
	        'qryType' : qryType,
	        'condition' : val
	      },
	      success : function(data)// 当请求成功时触发函数
	      {
	        $('#orderInfoTable').empty().append(data);
	      }
	    });
}

function bindPayment(){
	var checkBoxs = $("input[type='checkbox']");
	var radios = $("input[type='radio']");
    var paynum=false;
    var paymentArr= new Array();
    var n=0;
    for(i=0;i<checkBoxs.length;i++){
    	if($(checkBoxs[i]).attr('checked')){
    		paynum=true;
    		var eachPaymentRow = $(checkBoxs[i]).parent().parent();
    		var colArr = eachPaymentRow.find('td');
    		var eachPaymentInfo = {};
    		eachPaymentInfo.paymentId=$(colArr[1]).text();
    		eachPaymentInfo.paymentName=$(colArr[2]).text();
    		eachPaymentInfo.paymentValue=$(colArr[3]).text();
    		eachPaymentInfo.paymentDate=$(colArr[4]).text();
    		paymentArr[n]=eachPaymentInfo;
    		n++;
    	}
    }
    var orderNum=false;
    var orderInfo = {}
    for(j=0;j<radios.length;j++){
    	var ischecked = $(radios[j]).attr('checked');
    	if(ischecked){
    		orderNum=true;
    		var selectedOrdeRow = $(radios[j]).parent().parent();
    		var orderColArr=$(selectedOrdeRow).find('td');
    		orderInfo.orderId = $(orderColArr[1]).text();
    		break;
    	}
    }
    if(!paynum){
    	$("#fail_tips_lable").text('请先选择需要操作的汇款单');
        $("#fail_tips_layer").show().centerV();
    	return;
    }
    if(!orderNum){
    	$("#fail_tips_lable").text('请先选择需要操作的订单');
        $("#fail_tips_layer").show().centerV();
    	return;
    }
    jQuery.ajax({
	      type : 'POST',
	      url : '/ccms1/bindPaymentOperation',
	      data : {
	        'payments' : $.toJSON(paymentArr),
	        'order' :$.toJSON(orderInfo) 
	      },
	      success : function(data)// 当请求成功时触发函数
	      {
	        searchPay();                                                                                                                                                                                                                                                                                                                                                       
	        qryOrderInfo();
	        $("#succ_tips_lable").text(data);
	        $("#succ_tips_layer").show().centerV();
	      }
	    });
}

function changeAryTypePay(obj){
	$('.paymentQryType').removeClass('on');
	var _this = $(obj);
	var choice= _this.attr('qryType');
	$('#paymentQryTypeId').val(choice);
	if(choice=='4'){
		$('#inputNotimeDiv').hide();
		$('#inputtimeDiv').show();
		
	}else{
		$('#inputNotimeDiv').show();
		$('#inputtimeDiv').hide();
	}
	$('#inputPayKeyWords').val('');
	_this.addClass('on');
}

function changeqryTypeOrder(obj){
	
		$('.orderQryType').removeClass('on');
		var _this = $(obj);
		var choice=_this.attr('orderType')
		$('#qryOrderTypeValId').val(choice);
		if(choice=='5'){//按日期查询
			$('#qryorderNotimeDiv').hide();
			$('#qryordertimeDiv').show();
		}else{
			$('#qryorderNotimeDiv').show();
			$('#qryordertimeDiv').hide();
		}
		$('#inputOrderQryKey').val('');
		_this.addClass('on');
}