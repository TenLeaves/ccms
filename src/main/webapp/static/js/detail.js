//点击编辑按钮
function edit(){
	$('#detailPageId').css('display','none');
	initEditPage();
 }
//
function back(){
	$('#editPageId').css('display','none');
	$('#detailPageId').css('display','block');
}

//展示相信信息
function initDetailpage(){
	var parentId = $('#parentOrderIdVal').text();
	jQuery.ajax({
	      type : 'POST',
	      url : '/ccms1/queryFirstOrderAndFirstSend',
	      dataType:'json',
	      data:{
	    	  'SUBSCRIBE_ID':parentId,
	    	  'INVOICE_STATE':'0'
	      },
	      success : function(data)// 当请求成功时触发函数
	      {
	        var orderInfo = data.orderInfo;
	        var sendInfo = data.sendInfo;
	        var invoiceInfo=data.invoiceInfo;
	        
	        initDetailOrderArea(orderInfo);
	        initDetailSendArea(sendInfo);
	        if(invoiceInfo!=null&&invoiceInfo!=undefined){
	        	initDetailInvoiceArea(invoiceInfo);
	        }else{//隐藏发票区域
	        	$('#fapiaoArearDivDetail').css('display','none');
	        }
	      }
	    });
}

function initDetailOrderArea(orderInfo){
	//amount prodesc period orderWay fapiao sendfirst orderId
	if(orderInfo.SELECT_SEQ==null||orderInfo.SELECT_SEQ==undefined){
		orderInfo.SELECT_SEQ='订阅信息<1>';
	}
	$('#selectOrderDetailSeq').text(orderInfo.SELECT_SEQ);
	
	var infoArr = $('#orderInfoTablDetail').find('td');
	$(infoArr[0]).text(orderInfo.PRODUCT_PER_COUNT);
	$(infoArr[1]).text(orderInfo.PRODUCT_TYPE=='1'?'中国书法专业版':'中国书法普及版');
	
	if(orderInfo.CONTIN_MONTH=='1'){
		$(infoArr[2]).text(orderInfo.PRODUCT_BEGIN+'至'+orderInfo.PRODUCT_END);
	}else{
		var showInfo =orderInfo.PRODUCT_BEGIN;
		if(orderInfo.PRODUCT_BEGIN.length>16){
			showInfo=orderInfo.PRODUCT_BEGIN.substring(0,13)+'...';
		}
		$(infoArr[2]).text(orderInfo.PRODUCT_BEGIN);
	}
	var orderWaydesc='';
	if(orderInfo.ORDER_TYPE=='1'){
		orderWaydesc='电话订阅';
	}else if(orderInfo.ORDER_TYPE=='2'){
		orderWaydesc='邮局订阅';
	}else{
		orderWaydesc='上门订阅';
	}
	$(infoArr[3]).text(orderWaydesc);
	//是否需要发票
	if(orderInfo.needInvoice=='1'){
		$(infoArr[4]).text('是');
	}else{
		$(infoArr[4]).text('否');
	}
	
	//是否先发货
	if(orderInfo.sendFirst=='1'){
		$(infoArr[5]).text('是');
	}else{
		$(infoArr[5]).text('否');
	}
	
	
	$(infoArr[6]).text(orderInfo.ORDER_ID);
}
function initDetailSendArea(sendInfo){
	//stockAddr amount sendWay sendAddr  postcode name phone ismark 
	var infoArr = $('#sendInfoTblDetail').find('span');
	if (sendInfo != null && sendInfo.length != 0) {
		if (sendInfo.SELECT_SEQ == null || sendInfo.SELECT_SEQ == undefined) {
			sendInfo.SELECT_SEQ = '配送信息<1>';
		}
		// 初始化title
		$('#selectSendDetailSeq').text(sendInfo.SELECT_SEQ);

		var stockDesc = '';
		if (sendInfo.STOCK_ADDRESS == '1') {
			stockDesc = '杂志社';
		} else if (sendInfo.STOCK_ADDRESS == '2') {
			stockDesc = '邮局';
		} else {
			stockDesc = '印刷厂';
		}
		$(infoArr[0]).text(stockDesc);
		$(infoArr[1]).text(sendInfo.DISTRIBUT_COUNT);
		var sendWaydesc = '';
		if (sendInfo.DISTRIBUT_TYPE == '1') {
			sendWaydesc = '自提';
		} else if (sendInfo.DISTRIBUT_TYPE == '2') {
			sendWaydesc = '邮局';
		} else if (sendInfo.DISTRIBUT_TYPE == '3') {
			sendWaydesc = '中铁';
		}else{
			sendWaydesc = '其他';
		}
		$(infoArr[2]).text(sendWaydesc);
		$(infoArr[3]).text(sendInfo.DISTRIBUT_ADDRESS);
		$(infoArr[4]).text(sendInfo.ZIP_CODE);
		$(infoArr[5]).text(sendInfo.ADDRESSEE_NAME);
		$(infoArr[6]).text(sendInfo.ADDRESSEE_PHONE);
		$(infoArr[7]).text(sendInfo.NEED_LABEL_FLAG == '1' ? '不需要' : '需要');
		$('#nosendInfoId').hide();
		$('#change-dispatch-btnDetail').show();
	}else{
		$(infoArr[0]).text('--');
		$(infoArr[1]).text('--');
		$(infoArr[2]).text('--');
		$(infoArr[3]).text('--');
		$(infoArr[4]).text('--');
		$(infoArr[5]).text('--');
		$(infoArr[6]).text('--');
		$(infoArr[7]).text('--');
		$('#nosendInfoId').show();
		$('#change-dispatch-btnDetail').hide();
	}
}
function initDetailInvoiceArea(invoiceInfo){
	if(invoiceInfo!=null&&invoiceInfo!=undefined){
		//type title jin'e  amount 
		var infoArr = $('#invoiceInfoTblDetail').find('td');
		var typeDesc = '';
		if(invoiceInfo.INVOICE_TYPE=='1'){
			typeDesc='普通发票';
		}else{
			typeDesc='增值发票';
		}
		$(infoArr[0]).text(typeDesc);
		$(infoArr[1]).text(invoiceInfo.INVOICE_TITLE);
		$(infoArr[2]).text(invoiceInfo.INVOICE_AMOUNT+" 元");
		$(infoArr[3]).text(invoiceInfo.INVOICE_COUNT+" 张");
	}else{
		$('#fapiaoArearDivDetail').css('display','none');
	}
}


function cancleHanler(id){
	$('#'+id).css('display','none');
	$('#shadow').css('display','none');
}

function closeDialog(){
	window.close();
}

function updateSendFirst(){
	var parentId = $('#parentOrderIdVal').text(),
	   sendfirst=$('#userSelectSendFirst').val();
	jQuery.ajax({
	      type : 'POST',
	      url : '/ccms1/updateOrderSendFirst',
	      dataType:'json',
	      data:{
	    	  'SUBSCRIBE_ID':parentId,
	    	  'SEND_FIRST':sendfirst
	      },
	      success : function(data)// 当请求成功时触发函数
	      {
	    	  $("#succ_tips_lable").text(data);
			  $("#succ_tips_layer").show().centerV();
	        closeDialog();
	      }
	    });
}







