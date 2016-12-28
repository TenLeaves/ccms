$(function() {
	/*-----------------给选择项添加点击事件-----------------------*/
	// 选择库存
	$('.stocktypecss').bind('click', function() {
		$('.stocktypecss').removeClass('sel-box-on');
		$(this).addClass('sel-box-on');
		$('#userSelectStockAddr').val($(this).attr('stocktype'));
	});
	// 选择寄送方式
	$('.sendwaycss').bind('click', function() {
		$('.sendwaycss').removeClass('sel-box-on');
		$(this).addClass('sel-box-on');
		$('#userSelectSendWay').val($(this).attr('sendway'));
	});

	// 选择是否需要标签
	$('.bookmarkcss').bind('click', function() {
		$('.bookmarkcss').removeClass('sel-box-on');
		$(this).addClass('sel-box-on');
		$('#userSelectNeedBookMrk').val($(this).attr('bookmark'));
	});
	// 选择发票类型
	$('.invoiceTypeCss').bind('click', function() {
		$('.invoiceTypeCss').removeClass('sel-box-on');
		$(this).addClass('sel-box-on');
		$('#userSelectInvoiceType').val($(this).attr('invoiceType'));
	});
	// 选择发票项目
	$('.invoiceItem').bind('click', function() {
		$('.invoiceItem').removeClass('sel-box-on');
		$(this).addClass('sel-box-on');
		$('#userSelectInvoiceItem').val($(this).attr('invoiceItem'));
	});
	//是否未付款先发货
	$('.sendFirstCss').bind('click',function(){
		$('.sendFirstCss').removeClass('input-xsbtn01ed');
		$(this).addClass('input-xsbtn01ed');
		$('#userSelectSendFirst').val($(this).attr('sendfirst'));
	});
	// 切换订单
	$('#changeOrderId').bind('click', function() {
		showAllOrders();
	});
	// 切换配送
	$('#changeSendInfoId').bind('click', function() {
		showAllSends();
	});
	
	//编辑页面修改是否要发票和是否先发货
	$('.needInvoiceEdit').bind('click',function(){
		$('.needInvoiceEdit').removeClass('sel-box-on');
		$(this).addClass('sel-box-on');
		var val = $(this).attr('needinvoice');
		$('#parentOrderInvoiceTag').val(val);
	});
	$('.sendFirstEdit').bind('click',function(){
		$('.sendFirstEdit').removeClass('sel-box-on');
		$(this).addClass('sel-box-on');
		$('#parentOrderSendfirstTag').val($(this).attr('sendfirst'));
	});
	
	
	/*-----------------给选择项添加点击事件-----------------------*/
	// 初始化页面信息，默认展示第一条信息
	var option = $('#optionKeyId').text();
	if (option == '1') {
		initDetailpage();
		$('#editOptionBtn').css('display', 'none');
	} else if (option == '2') {
		edit();
	} else {
		initDetailpage();
	}

});

// 查询第一条子订单信息，查询该子订单的配送信息 用户页面展示
function initEditPage() {
	var parentId = $('#parentOrderIdVal').text();
	jQuery.ajax({
		type : 'POST',
		url : '/ccms1/queryFirstOrderAndFirstSend',
		dataType : 'json',
		data : {
			"SUBSCRIBE_ID" : parentId,
			'INVOICE_STATE':'1'
		},
		success : function(data)// 当请求成功时触发函数
		{
			var orderInfo = data.orderInfo;
			var sendInfo = data.sendInfo;
			var roleInfo = data.roleInfo;
			var invoiceInfo = data.invoiceInfo;

			// 根据登录角色信息禁用页面部分功能
			initAbility(roleInfo);

			initOrderArea(orderInfo);
			initSendArea(sendInfo);

			initInvoiceArea(invoiceInfo);

			$('#editPageId').css('display', 'block');
			$('#shadow').css("display", 'none');// 遮罩
		}
	});
}

// 初始化订阅区域
function initOrderArea(orderInfo) {
	var orderInfoRow = $('#orderInfoTrId');
	// amt prodType period orderway invoice sendfirst orderid prodtype start end islianxu
	var colArr = orderInfoRow.find('td');

	// 初始化title
	if (orderInfo.SELECT_SEQ == null || orderInfo.SELECT_SEQ == undefined) {
		orderInfo.SELECT_SEQ = '订阅信息<1>';
	}
	$('#selectOrderSeq').text(orderInfo.SELECT_SEQ);

	$(colArr[0]).text(orderInfo.PRODUCT_PER_COUNT);

	if (orderInfo.PRODUCT_TYPE == '1') {
		$(colArr[1]).text('中国书法专业版');
	} else {
		$(colArr[1]).text('中国书法普及版');
	}
	// 显示订阅周期
	if (orderInfo.CONTIN_MONTH == '1') {
		$(colArr[2])
				.text(orderInfo.PRODUCT_BEGIN + '至' + orderInfo.PRODUCT_END);
	} else {
		var showInfo = orderInfo.PRODUCT_BEGIN;
		if (orderInfo.PRODUCT_BEGIN.length > 16) {
			showInfo = orderInfo.PRODUCT_BEGIN.substring(0, 13) + '...';
		}
		$(colArr[2]).text(orderInfo.PRODUCT_BEGIN);
	}
	//显示订阅方式
	if(orderInfo.ORDER_TYPE=='1'){
		$(colArr[3]).text("电话订阅");
	}else if(orderInfo.ORDER_TYPE=='2'){
		$(colArr[3]).text("邮局订阅");
	}else{
		$(colArr[3]).text("上门订阅");
	}
	//是否需要发票
	var invoiceSpanArr = $(colArr[4]).find('span');
	for(i=0;i<invoiceSpanArr.length;i++){
		if(orderInfo.needInvoice==$(invoiceSpanArr[i]).attr('needinvoice')){
			$(invoiceSpanArr[i]).click();
			break;
		}
	}
	//是否未付款先发货
	var sendSpanArr = $(colArr[5]).find('span'); 
	for(i=0;i<sendSpanArr.length;i++){
		if(orderInfo.sendFirst==$(sendSpanArr[i]).attr('sendfirst')){
			$(sendSpanArr[i]).click();
			break;
		}
	}
	
	// 订单号
	$(colArr[6]).text(orderInfo.ORDER_ID);
	$(colArr[7]).text(orderInfo.PRODUCT_TYPE);
	$(colArr[8]).text(orderInfo.PRODUCT_BEGIN);
	$(colArr[9]).text(orderInfo.PRODUCT_END);
	$(colArr[10]).text(orderInfo.CONTIN_MONTH);
}

// 初始化配送区域
function initSendArea(sendInfo) {
	if(sendInfo!=undefined&&sendInfo!=null){
		// 取到的都是td
		var stockAddr = $('#stockAddrId');
		var mountPerMon = $('#sendAmontPerMonthId');
		var sendWay = $('#sendWayId');
		var sendAddr = $('#sendAddrId');
		var sendPost = $('#sendPostId');
		var receiveName = $('#receiveName');
		var receivePhone = $('#receivePhone');
		var bookMark = $('#needMarkId');
		if (sendInfo.SELECT_SEQ == undefined||sendInfo.SELECT_SEQ == null  ) {
			sendInfo.SELECT_SEQ = '配送信息<1>';
		}
		// 初始化title
		$('#selectSendSeq').text(sendInfo.SELECT_SEQ);
		// 初始化仓库
		var stockAddrArr = stockAddr.find('span');
		for (i = 0; i < stockAddrArr.length; i++) {
			if ($(stockAddrArr[i]).attr('stocktype') == sendInfo.STOCK_ADDRESS) {
				$(stockAddrArr[i]).click();
				break;
			}
		}

		$(mountPerMon.find('input')).val(sendInfo.DISTRIBUT_COUNT);
		$(receiveName.find('input')).val(sendInfo.ADDRESSEE_NAME);
		$(receivePhone.find('input')).val(sendInfo.ADDRESSEE_PHONE);

		var sendWayArr = sendWay.find('span');
		for (j = 0; j < sendWayArr.length; j++) {
			if ($(sendWayArr[j]).attr('sendway') == sendInfo.DISTRIBUT_TYPE) {
				$(sendWayArr[j]).click();
				break;
			}
		}

		$(sendAddr.find('input')).val(sendInfo.DISTRIBUT_ADDRESS);
		$(sendPost.find('input')).val(sendInfo.ZIP_CODE);

		var bookMarkArr = bookMark.find('input');
		for (k = 0; k < bookMarkArr.length; k++) {
			if ($(bookMarkArr[k]).attr('bookmark') == sendInfo.NEED_LABEL_FLAG) {
				$(bookMarkArr[k]).click();
				break;
			}
		}
		// 保存选中配送单id到页面
		$('#selectSendInfoId').val(sendInfo.DISTRIBUT_ID);
		$('#nosendInfoEdit').hide();//没有配送单 提示区域隐藏
		$('#optionBtnAreaId').show();
		$('#deleteSendBtn').show();
		$('#deleteSendBtn').show();
		$('#changeSendInfoId').show();
	}else{
		$('#nosendInfoEdit').show();//提示信息显示
		$('#optionBtnAreaId').hide();
		$('#deleteSendBtn').hide();
		$('#sendInfoEditTblId').hide();
		$('#changeSendInfoId').hide();//切换按钮隐藏
	}
	
}

// 初始化发票区域
function initInvoiceArea(invoiceInfo) {
	if (invoiceInfo == undefined || invoiceInfo == null) {//没有发票
		
		$('#cancelAddInvoice').hide();//取消新建
		$('#addnewInvoice').show();//新建按钮
		$('#saveNewInvoice').hide();//保存新建按钮
		$('#PreInvoicesaveBtn').hide();//修改发票
		$('#deleteInvoiceBtn').hide();//删除按钮
		
		$('#change-receipt-btn').hide();//qiehuan btn
	} else {
		$('#cancelAddInvoice').hide();//取消新建
		$('#addnewInvoice').show();//新建按钮
		$('#saveNewInvoice').hide();//保存新建按钮
		$('#PreInvoicesaveBtn').show();//修改发票
		$('#deleteInvoiceBtn').show();//删除按钮
		$('#change-receipt-btn').show();
		var invoiceTbl = $('#invoiceInfoTblId');
		// type title jin'e amount NO. invoiceId
		var tdArr = invoiceTbl.find('td');
		// 发票类型
		var typeArr = $(tdArr[0]).find('span');
		for (i = 0; i < typeArr.length; i++) {
			if (invoiceInfo.INVOICE_TYPE == $(typeArr[i]).attr('invoiceType')) {
				$(typeArr[i]).click();
				break;
			}
		}
		//发票项目
		var invoiceItem = invoiceInfo.INVOICE_ITEM;
		var itemArr = $(tdArr[1]).find('span');
		for (i = 0; i < typeArr.length; i++) {
			if (invoiceItem == $(itemArr[i]).attr('invoiceitem')) {
				$(itemArr[i]).click();
				break;
			}
		}

		$(tdArr[2]).find('input').val(invoiceInfo.INVOICE_TITLE);
		$(tdArr[3]).find('input').val(invoiceInfo.INVOICE_AMOUNT);
		$(tdArr[4]).find('input').val(invoiceInfo.INVOICE_COUNT);

		if (invoiceInfo.INVOICE_NO == undefined
				|| invoiceInfo.INVOICE_NO == 'undefined') {
			invoiceInfo.INVOICE_NO = '';
		}
		$(tdArr[5]).find('input').val(invoiceInfo.INVOICE_NO);
		$(tdArr[6]).find('input').val(invoiceInfo.INVOICE_ID);
		$(tdArr[7]).find('textarea').val(invoiceInfo.REMARK);
		
	}
}

// 查询所有的子订单信息并显示
function showAllOrders(page) {
	var orderId = $('#parentOrderIdVal').text();
	jQuery
			.ajax({
				type : 'POST',
				url : '/ccms1/queryAllChildOrders',
				dataType : 'json',
				data : {
					'parentOrderId' : orderId
				},
				success : function(data)// 当请求成功时触发函数
				{
					var orderList = data.orderList;
					var orderTbl = $('#orderToShowTblId');
					if (page == 'detail') {
						orderTbl = $('#orderToShowTblIdDetail');
					}
					orderTbl.empty();

					for (i = 0; i < orderList.length; i++) {
						var eachOrder = orderList[i];
						var prodTypeDesc = eachOrder.PRODUCT_TYPE == 1 ? '中国书法专业版'
								: '中国书法普及版';
						var showStr = eachOrder.CONTIN_MONTH == 1 ? (eachOrder.PRODUCT_BEGIN
								+ '至' + eachOrder.PRODUCT_END)
								: eachOrder.PRODUCT_BEGIN;
						var dataRow = "<tr> "
								+ "<td width='30'><input type='radio' name='radio-one' class='input-radio' /></td>"
								+ " <td width='100'>订阅信息&lt;"
								+ (i + 1)
								+ "&gt;</td>"
								+ " <td width='140'><a href='javascript:;' title='' class='cred'>"
								+ eachOrder.ORDER_ID
								+ "</a></td> "
								+ "<td width='150'>"
								+ prodTypeDesc
								+ "</td> "
								+ "<td width='110'><span class='c03f'>"
								+ showStr
								+ "</span></td>"
								+ " <td>"
								+ eachOrder.PRODUCT_PER_COUNT
								+ "</td> "
								+ " <td style='display:none;'>"
								+ eachOrder.ORDER_TYPE
								+ "</td> "
								+ " <td style='display:none;'>"
								+ eachOrder.PRODUCT_TYPE
								+ "</td> "
								+ " <td style='display:none;'>"
								+ eachOrder.CONTIN_MONTH
								+ "</td> "
								+ " <td style='display:none;'>"
								+ eachOrder.PRODUCT_BEGIN
								+ "</td> "
								+ " <td style='display:none;'>"
								+ eachOrder.PRODUCT_END + "</td> " + "</tr>";
						orderTbl.append(dataRow);
					}
					if (page == 'detail') {
						//showDiv('orderListDivDetail', 'shadow');
						 $('#orderListDivDetail').css('display','block');
					} else {
						//showDiv('orderListDiv', 'shadow')
						 $('#orderListDiv').css('display','block');
					}
					// $('#shadow').css("display", 'block');//遮罩
				}
			});
}

// 选中某一条子订单切换
function changeOrder(page) {
	// radio index orderId prodTypedesc showstr amount orderType (0 6)
	// prodtype contimonth start end (7-10)

	var orderRowArr = $('#orderToShowTblId tr');// 默认是编辑页面的子订单选择区域
	if (page == 'detail') {
		orderRowArr = $('#orderToShowTblIdDetail tr');
	}

	var selectOrderInfo = {};
	for (i = 0; i < orderRowArr.length; i++) {
		var colArr = $(orderRowArr[i]).find('td');
		if ($(colArr[0]).find('input').attr('checked')) {
			selectOrderInfo.ORDER_ID = $(colArr[2]).text();
			selectOrderInfo.PRODUCT_PER_COUNT = $(colArr[5]).text();
			selectOrderInfo.PRODUCT_TYPE = $(colArr[7]).text();
			selectOrderInfo.CONTIN_MONTH = $(colArr[8]).text();
			selectOrderInfo.PRODUCT_BEGIN = $(colArr[9]).text();
			selectOrderInfo.PRODUCT_END = $(colArr[10]).text();

			selectOrderInfo.SELECT_SEQ = '订阅信息<' + (i+1) + '>';
			break;
		}
	}
	if (selectOrderInfo.PRODUCT_PER_COUNT == ''
			|| selectOrderInfo.PRODUCT_PER_COUNT == undefined) {
		$("#fail_tips_lable").text("请选择订单");
        $("#fail_tips_layer").show().centerV();
		return;
	}
	// 把选中的值填写到页面
	if (page == 'detail') {// 详情展示页
		$('#orderListDivDetail').css('display', 'none');
		initDetailOrderArea(selectOrderInfo)

	} else {// 详情编辑页
		$('#orderListDiv').css('display', 'none');
		initOrderArea(selectOrderInfo);
	}
	$('#shadow').css("display", 'none');// 遮罩

	// 根据选中的订单号初始化配送信息
	jQuery.ajax({
		type : 'POST',
		url : '/ccms1/queryFirstSendInfoByOrderId',
		dataType : 'json',
		data : {
			'ORDER_ID' : selectOrderInfo.ORDER_ID
		},
		success : function(data)// 当请求成功时触发函数
		{
			var sendInfo = data.sendMap;
			if (page == 'detail') {
				initDetailSendArea(sendInfo);
			} else {
				initSendArea(sendInfo);
			}

		}
	});

}

// 显示自订单下所有的配送列表
function showAllSends(page) {

	var orderInfoRow = $('#orderInfoTrId');
	var colArr = orderInfoRow.find('td');
	var orderId = $(colArr[6]).text();

	if (page == 'detail') {
		orderInfoRow = $('#orderInfoTablDetail')
		colArr = orderInfoRow.find('td');
		orderId = $(colArr[6]).text();
	}

	jQuery
			.ajax({
				type : 'POST',
				url : '/ccms1/querySendInfoByOrderId',
				dataType : 'json',
				data : {
					'ORDER_ID' : orderId
				},
				success : function(data)// 当请求成功时触发函数
				{
					
					var sendList = data.sendList;
					if(sendList!=undefined&&sendList!=null){
						var sendTbl = $('#sendToShowTblId');
						if (page == 'detail') {
							sendTbl = $('#sendToShowTblIdDetail');
						}
						sendTbl.empty();

						for (i = 0; i < sendList.length; i++) {
							var eachSend = sendList[i];
							var dataRow = "<tr> "
									+ "<td width='30'><input type='radio' name='radio-one' class='input-radio' /></td>"
									+ " <td width='100'>配送信息&lt;"
									+ (i + 1)
									+ "&gt;</td>"
									+ " <td style='display:none;'><a href='javascript:;' title='' class='cred'>"
									+ eachSend.DISTRIBUT_ID
									+ "</a></td> "
									+ "<td width='160'>"
									+ eachSend.PROD_DESC
									+ "</td> "
									+ "<td width='200'>"
									+ eachSend.DISTRIBUT_ADDRESS
									+ "</td> "
									+ " <td width='90'>"
									+ eachSend.ADDRESSEE_NAME
									+ "</td> "
									+ " <td>"
									+ eachSend.ADDRESSEE_PHONE
									+ "</td> "
									+ " <td style='display:none;'>"
									+ eachSend.STOCK_ADDRESS
									+ "</td> "
									+ " <td style='display:none;'>"
									+ eachSend.DISTRIBUT_COUNT
									+ "</td> "
									+ " <td style='display:none;'>"
									+ eachSend.DISTRIBUT_TYPE
									+ "</td> "
									+ " <td style='display:none;'>"
									+ eachSend.ZIP_CODE
									+ "</td> "
									+ " <td style='display:none;'>"
									+ eachSend.NEED_LABEL_FLAG + "</td> " + "</tr>";
							sendTbl.append(dataRow);
						}
						if (page == 'detail') {
							//showDiv('sendListDivDetail', 'shadow');
							$('#sendListDivDetail').show();
						
						} else {
							//showDiv('sendListDiv', 'shadow');
							$('#sendListDiv').show();
						}
					}else{
						$("#fail_tips_lable").text("该订单本月没有配送单");
						$("#fail_tips_layer").show().centerV();
					}
					
				}
			});

}

// 选中某条配送单
function changeSend(page) {
	// radio seq sendid addr name phone stockaddr count sendtype zip mark
	var sendRowArr = $('#sendToShowTblId tr');
	if (page == 'detail') {
		sendRowArr = $('#sendToShowTblIdDetail tr');

	}
	var selectSendInfo = {};
	for (i = 0; i < sendRowArr.length; i++) {
		var colArr = $(sendRowArr[i]).find('td');
		if ($(colArr[0]).find('input').attr('checked')) {
			selectSendInfo.ADDRESSEE_NAME = $(colArr[5]).text();
			selectSendInfo.ADDRESSEE_PHONE = $(colArr[6]).text();
			selectSendInfo.STOCK_ADDRESS = $(colArr[7]).text();
			selectSendInfo.DISTRIBUT_COUNT = $(colArr[9]).text();
			selectSendInfo.DISTRIBUT_TYPE = $(colArr[9]).text();
			selectSendInfo.DISTRIBUT_ADDRESS = $(colArr[4]).text();
			selectSendInfo.ZIP_CODE = $(colArr[10]).text();
			selectSendInfo.NEED_LABEL_FLAG = $(colArr[11]).text();

			selectSendInfo.SELECT_SEQ = '配送信息<' + (i+1) + '>';
			break;
		}
	}
	if (selectSendInfo.STOCK_ADDRESS == ''
			|| selectSendInfo.STOCK_ADDRESS == undefined) {
		$("#fail_tips_lable").text("请选择一条配送信息");
        $("#fail_tips_layer").show().centerV();
		return;
	}
	if (page == 'detail') {
		initDetailSendArea(selectSendInfo);
		$('#sendListDivDetail').css('display', 'none');
	} else {
		initSendArea(selectSendInfo);
		$('#sendListDiv').css('display', 'none');
	}

	$('#shadow').css("display", 'none');// 遮罩
}

// 显示所有发票信息
function showAllInvoice(page) {
	var parentId = $('#parentOrderIdVal').text();

	jQuery
			.ajax({
				type : 'POST',
				url : '/ccms1/queryInvoice',
				dataType : 'json',
				data : {
					'SUBSCRIBE_ID' : parentId
				},
				success : function(data)// 当请求成功时触发函数
				{
					var invoiceArr = data.invoiceList;
					var invoiceTbl = $('#invoiceToShowTblId');
					if (page == 'detail') {
						invoiceTbl = $('#invoiceToShowTblIdDetail');
					}
					invoiceTbl.empty();
					
						for (i = 0; i < invoiceArr.length; i++) {
							var typeDesc = '';
							if (invoiceArr[i].INVOICE_TYPE == '1') {
								typeDesc = '办公用品';
							} else if (invoiceArr[i].INVOICE_TYPE == '2') {
								typeDesc = '图书';
							} else {
								typeDesc = '其他';
							}
							var dataRow = "<tr class='tr-select'> "
									+ "<td width='30'><input type='radio' name='radio-one' class='input-radio' /></td>"
									+ " <td width='110'>发票信息&lt;"
									+ parseInt(i)+1
									+ "&gt;</td>"
									+ " <td style='display:none;'><a href='javascript:;' title='' class='cred'>"
									+ invoiceArr[i].INVOICE_ID
									+ "</a></td> "
									+ "<td width='100'>"
									+ typeDesc
									+ "</td> "
									+ "<td width='160'>"
									+ invoiceArr[i].INVOICE_TITLE
									+ "</td>"
									+ "<td width='90'>"
									+ invoiceArr[i].INVOICE_AMOUNT
									+ "</td>"
									+ "<td>"
									+ invoiceArr[i].INVOICE_COUNT
									+ "</td> "
									+ "<td style='display:none;' class='invoiceNo'>"
									+ invoiceArr[i].INVOICE_NO
									+ "</td> "
									+ "<td style='display:none;' class='invoiceType'>"
									+ invoiceArr[i].INVOICE_TYPE
									+ "</td> "
									+ "<td style='display:none;' class='invoiceType'>"
									+ invoiceArr[i].INVOICE_ITEM
									+ "</td> "
									+ "</tr> ";
							invoiceTbl.append(dataRow);
						}
					
					
					if (page == 'detail') {
						//showDiv('invoiceListDivDetail', 'shadow');
						 $('#invoiceListDivDetail').css('display','block');
						 if(invoiceArr.length<=1){
								$('#change-receipt-btn1').hide();
							 }else{
								 $('#change-receipt-btn1').show();
							 }
					} else {
						//showDiv('invoiceListDiv', 'shadow');
						 $('#invoiceListDiv').css('display','block');
						 if(invoiceArr.length<=1){
							$('#change-receipt-btn').hide();
						 }else{
							 $('#change-receipt-btn').show();
						 }
						 
					}

					// $('#shadow').css("display", 'block');//遮罩
				}
			});
}

// 选中某条发票信息
function changeInvoice(page) {
	// radio req invoiceId typeDesc title jin'e mount no type
	var sendRowArr = $('#invoiceToShowTblId tr');
	if (page == 'detail') {
		sendRowArr = $('#invoiceToShowTblIdDetail tr')
	}
	var invoiceInfo = {};
	for (i = 0; i < sendRowArr.length; i++) {
		var colArr = $(sendRowArr[i]).find('td');
		if ($(colArr[0]).find('input').attr('checked')) {
			invoiceInfo.INVOICE_SEQ = $(colArr[1]).text();
			invoiceInfo.INVOICE_ID = $(colArr[2]).find('a').text();
			invoiceInfo.INVOICE_NO = $(colArr[7]).text();
			invoiceInfo.INVOICE_TITLE = $(colArr[4]).text();
			invoiceInfo.INVOICE_AMOUNT = $(colArr[5]).text();
			invoiceInfo.INVOICE_COUNT = $(colArr[6]).text();
			invoiceInfo.INVOICE_TYPE = $(colArr[8]).text();
			invoiceInfo.INVOICE_ITEM = $(colArr[9]).text();
			break;
		}
	}
	if (page == 'detail') {
		initDetailInvoiceArea(invoiceInfo);
		$('#invoiceListDivDetail').css('display', 'none');
	} else {
		initInvoiceArea(invoiceInfo);
		$('#invoiceListDiv').css('display', 'none');
	}

	$('#shadow').css("display", 'none');// 遮罩
}

// 删除配送单
function deleteSend() {
	var sendId = $('#selectSendInfoId').val();
	var flag = window.confirm('您确定删除这条配送信息么');
	if (flag) {
		jQuery.ajax({
			type : 'POST',
			url : '/ccms1/deleteSendInfo',
			dataType : 'json',
			data : {
				'DISTRIBUT_ID' : sendId
			},
			success : function(data)// 当请求成功时触发函数
			{
				// 删除一条后显示后面一条
				initSendAredAfterHandle();
				$("#succ_tips_lable").text(data);
		        $("#succ_tips_layer").show().centerV();
			}
		});
	}
}

// 点击新增配送单
function addSend() {
	$('#addNewSendBtnId').css('display', 'none');
	$('#modifyBtnId').css('display', 'none');
	$('#saveAddBtnId').css('display', 'inline');
	$('#cancelAddBtnId').css('display', 'inlie');
	$('#sendInfoEditTblId').show();
	resetSendArea();
}

// 取消新建
function cancelAdd() {
	$('#addNewSendBtnId').css('display', 'block');
	$('#saveAddBtnId').css('display', 'none');
	$('#cancelAddBtnId').css('display', 'none');
	$('#modifyBtnId').css('display', 'block');
	// 初始化配送信息
	initSendAredAfterHandle();
}

// 点击保存新建的配送信息
function saveAdd() {
	var sendInfo = {};

	var orderColArr = $('#orderInfoTrId').find('td');

	sendInfo.orderId = $(orderColArr[6]).text();
	sendInfo.prodType = $(orderColArr[7]).text();
	sendInfo.prodBegin = $(orderColArr[8]).text();
	sendInfo.prodEnd = $(orderColArr[9]).text();
	sendInfo.isLianxu = $(orderColArr[10]).text();

	sendInfo.sendId = new Date().getTime();
	sendInfo.selectStockType = $('#userSelectStockAddr').val();
	sendInfo.amount = $('#sendAmontPerMonthId').find('input').val();
	sendInfo.sendWay = $('#userSelectSendWay').val();
	sendInfo.sendAddr = $('#sendAddrId').find('input').val();
	sendInfo.postCode = $('#sendPostId').find('input').val();
	sendInfo.name = $('#receiveName').find('input').val();
	sendInfo.phone = $('#receivePhone').find('input').val();
	sendInfo.needMark = $('#userSelectNeedBookMrk').val();

	if (validateInputInfo()) {
		jQuery.ajax({
			type : 'POST',
			url : '/ccms1/addOneSendInfo',

			data : {
				'sendInfo' : $.toJSON(sendInfo)
			},
			success : function(data)// 当请求成功时触发函数
			{
				if(data=='添加成功'){
					$('#selectSendInfoId').val(sendInfo.sendId);// 反填选中的配送单号
					$('#addNewSendBtnId').css('display', 'inline');
					$('#modifyBtnId').css('display', 'inline');
					$('#saveAddBtnId').css('display', 'none');
					$('#cancelAddBtnId').css('display', 'none');
				}else{
					$("#fail_tips_lable").text(data);
			        $("#fail_tips_layer").show().centerV();
				}
			}
		});
	}

}

// 修改配送单
function editSend() {
	var flag = validateInputInfo();
	if (!flag) {
		return;
	}

	var sendInfo = {};

	var orderColArr = $('#orderInfoTrId').find('td');
	sendInfo.orderId = $(orderColArr[6]).text();
	sendInfo.sendId = $('#selectSendInfoId').val()
	sendInfo.selectStockType = $('#userSelectStockAddr').val();
	sendInfo.amount = $('#sendAmontPerMonthId').find('input').val();
	sendInfo.sendWay = $('#userSelectSendWay').val();
	sendInfo.sendAddr = $('#sendAddrId').find('input').val();
	sendInfo.postCode = $('#sendPostId').find('input').val();
	sendInfo.name = $('#receiveName').find('input').val();
	sendInfo.phone = $('#receivePhone').find('input').val();
	sendInfo.needMark = $('#userSelectNeedBookMrk').val();

	jQuery.ajax({
		type : 'POST',
		url : '/ccms1/modifyOneSendInfo',

		data : {
			'sendInfo' : $.toJSON(sendInfo)
		},
		success : function(data)// 当请求成功时触发函数
		{
			$("#succ_tips_lable").text(data);
	        $("#succ_tips_layer").show().centerV();
		}
	});
}

// 校验输入值是否合法
function validateInputInfo() {
	var sendWay = $('#userSelectSendWay').val(), sendAddr = $('#sendAddrId')
			.find('input').val(), postCode = $('#sendPostId').find('input')
			.val(), name = $('#receiveName').find('input').val(), phone = $(
			'#receivePhone').find('input').val(), amountPermont = $(
			'#sendAmontPerMonthId').find('input').val();
	
	var regetex = /\d+/, phoneRegex = /1\d{10}/;
	if (!regetex.test(amountPermont)) {
		$("#fail_tips_lable").text("配送数量只能是数字");
        $("#fail_tips_layer").show().centerV();
		return false;
	}
	if(sendWay!='1'){
		if (sendAddr == '') {
			$("#fail_tips_lable").text("配送地址不能为空");
	        $("#fail_tips_layer").show().centerV();
			return false;
		}
	}
	if (!regetex.test(postCode)) {
		$("#fail_tips_lable").text("邮编只能是数字");
        $("#fail_tips_layer").show().centerV();
		return false;
	}

	if (name == '') {
		$("#fail_tips_lable").text("收件人不能为空");
        $("#fail_tips_layer").show().centerV();
		return false;
	}
	if (!regetex.test(phone)) {
		$("#fail_tips_lable").text("请输入合法的手机号");
        $("#fail_tips_layer").show().centerV();
		return false;
	} else if (!phoneRegex.test(phone)) {
		$("#fail_tips_lable").text("手机号必须为11个数字");
        $("#fail_tips_layer").show().centerV();
		return false;
	}
	return true;
}
// 清空配送区域
function resetSendArea() {
	// 取到的都是td
	var stockAddr = $('#stockAddrId');
	var mountPerMon = $('#sendAmontPerMonthId');
	var sendWay = $('#sendWayId');
	var sendAddr = $('#sendAddrId');
	var sendPost = $('#sendPostId');
	var receiveName = $('#receiveName');
	var receivePhone = $('#receivePhone');
	var bookMark = $('#needMarkId');
	// 初始化title
	$('#selectSendSeq').text('配送信息<->');
	// 初始化仓库
	var stockAddrArr = stockAddr.find('span');
	$(stockAddrArr[0]).click();

	$(mountPerMon.find('input')).val('');
	$(receiveName.find('input')).val('');
	$(receivePhone.find('input')).val('');

	var sendWayArr = sendWay.find('span');
	$(sendWayArr[0]).click();

	$(sendAddr.find('input')).val('');
	$(sendPost.find('input')).val('');

	var bookMarkArr = bookMark.find('span');
	$(bookMarkArr[0]).click();

	// 保存选中配送单id到页面
	$('#selectSendInfoId').val('');
}

// 删除一条配送信息后重新初始化显示界面
function initSendAredAfterHandle() {

	jQuery.ajax({
		type : 'POST',
		url : '/ccms1/queryFirstSendInfoByOrderId',
		dataType : 'json',
		success : function(data)// 当请求成功时触发函数
		{
			var sendInfo = data.sendMap;
			initSendArea(sendInfo);
		}
	});
}

function initAbility(loginUser) {
	var faxing = '1', kuguan = '2', caiwu = '3', lingdao = '4';

	var roleval = loginUser.roleVal;
	// 领导和财务只能查看详情页面 默认屏蔽操作功能
	$('#optionBtnAreaId').css('display', 'none');// 订阅信息操作按钮
	$('#invoiceOptionBtnAreaId').css('display', 'none');// 发票信息操作按钮

	if (roleval.indexOf(faxing) != -1) {
		$('#orderEditAreaId').show();
	}else{
		$('#orderEditAreaId').hide();
	}
	if (roleval.indexOf(kuguan) != -1) {
		$('#fapiaoArearDivEdit').show();
		$('#invoiceOptionBtnAreaId').show();
		//滚动到发票添加区域
		var position = $('#BOTTOM').offset();
		window.parent.scrollTo(position.left, position.top);
	}else{
		$('#fapiaoArearDivEdit').hide();
	}
}

// 点击添加一张发票按钮
function addOneInvoice() {
	$('#cancelAddInvoice').show();//取消新建
	$('#addnewInvoice').hide();//新建按钮
	$('#saveNewInvoice').show();//保存新建按钮
	$('#PreInvoicesaveBtn').hide();//修改发票
	$('#deleteInvoiceBtn').hide();//删除按钮
	$('#deleteInvoiceBtn').css('display', 'none');
	resetInvoiceArea();
}
//导入发票
function importInvoice(){
	
	showNewDiv("import","shadow");
	
	$("#fileInfo").val("");
	$("#result_info1").html(0);
	$("#result_info2").html(0);
	$("#result_info3").html(0);
	var tb = document.getElementById('tal_error');
	var rowNum=tb.rows.length;
	for (var i=0;i<rowNum;i++)
	{
		tb.deleteRow(i);
		rowNum=rowNum-1;
		i=i-1;
	}
}
//导入发票的确认导入按钮
function importSubmit(){
	$("#result_info1").html(0);
	$("#result_info2").html(0);
	$("#result_info3").html(0);
	var tb = document.getElementById('tal_error');
	var rowNum=tb.rows.length;
	for (var i=0;i<rowNum;i++)
	{
		tb.deleteRow(i);
		rowNum=rowNum-1;
		i=i-1;
	}
	var import_form=$("#fileInfo").val();
	if(""==import_form){
        $("#succ_tips_lable").text("您需要选择导入文件的地址");
        $("#succ_tips_layer").show().centerV();
		return;
	}
	$("#subscribeId").val($('#parentOrderIdVal').text());
	$('#import_form').ajaxSubmit({
		dataType:"json",
		success: function (data) {
			if (data.status == "success") {
            $("#succ_tips_lable").text("导入数据成功");
            $("#succ_tips_layer").show().centerV();
			$("#result_info1").html(data.sum);
			$("#result_info2").html(data.infoLength);
			$("#result_info3").html(data.errorLength);
            var errorInfos=data.errorInfos;
            for(var i=0;i<errorInfos.length;i++){
        		$col = $("<tr><td width='120'>"+errorInfos[i].line+"</td><td  width='200'>"+errorInfos[i].error+"</td>" +
    					"<td>"+errorInfos[i].right+"</td></tr>");  
        		$("#tal_error").append($col);
            }
		}else{
            $("#succ_tips_lable").text(data.status);
            $("#succ_tips_layer").show().centerV();
			return;
		}
		}
    });

}
// 点击保存新建发票
function saveNewInvoice() {
	var invoiceInfo = {};

	var invoiceTbl = $('#invoiceInfoTblId');
	// type item title jin'e amount NO. invoiceId 
	var tdArr = invoiceTbl.find('td');
	invoiceInfo.type =$(tdArr[0]).find('input').val();
	invoiceInfo.item =$(tdArr[1]).find('input').val();
	invoiceInfo.title = $(tdArr[2]).find('input').val();
	invoiceInfo.money = $(tdArr[3]).find('input').val();
	invoiceInfo.amount = $(tdArr[4]).find('input').val();
	invoiceInfo.id = new Date().getTime();
	invoiceInfo.subscribeId = $('#parentOrderIdVal').text();
	invoiceInfo.remark=$('#invoiceRemark').val();
	var flag= validateInvoiceInfo(invoiceInfo);
	if(!flag){
		return;
	}
	jQuery.ajax({
		type : 'POST',
		url : '/ccms1/addOneInvoice',
		data : {
			'invoiceInfo' : $.toJSON(invoiceInfo)
		},
		success : function(data)// 当请求成功时触发函数
		{	
			if(data=='添加成功'){
				$(tdArr[6]).find('input').val(invoiceInfo.id);
				var aArr = $('#invoiceOptionBtnAreaId').find('input');
				$('#cancelAddInvoice').hide();//取消新建
				$('#addnewInvoice').show();//新建按钮
				$('#saveNewInvoice').hide();//保存新建按钮
				$('#PreInvoicesaveBtn').show();//修改发票
				$('#deleteInvoiceBtn').css('display', 'inline');
				$("#fail_tips_lable").text(data);
		        $("#fail_tips_layer").show().centerV();
			}else{
				$("#fail_tips_lable").text(data);
		        $("#fail_tips_layer").show().centerV();
			}
			
		}
	});

}

// 取消新建
function cancelAddNewInvoide() {
	var aArr = $('#invoiceOptionBtnAreaId').find('input');
	$('#cancelAddInvoice').hide();//取消新建
	$('#addnewInvoice').show();//新建按钮
	$('#saveNewInvoice').hide();//保存新建按钮
	$('#PreInvoicesaveBtn').hide();//修改发票
	$('#deleteInvoiceBtn').css('display', 'inline');

	var parentId = $('#parentOrderIdVal').text();
	jQuery.ajax({
		type : 'POST',
		url : '/ccms1/queryFirstInvoice',
		dataType : 'json',
		data:{
			'SUBSCRIBE_ID' : parentId
		},
		success : function(data)// 当请求成功时触发函数
		{
			var invoice = data.invoiceInfo;
			initInvoiceArea(invoice);
		}
	});
}

// 删除发票
function deleteInvoice() {
	var invoiceTbl = $('#invoiceInfoTblId');
	// type title jin'e amount NO. invoiceId
	var tdArr = invoiceTbl.find('td');
	var invoiceId = $(tdArr[5]).find('input').val();

	var flag = window.confirm('您确定删除这条发票信息么');
	if (flag) {
		jQuery.ajax({
			type : 'POST',
			url : '/ccms1/deleteInvoice',
			dataType : 'json',
			data : {
				'INVOICE_ID' : invoiceId
			},
			success : function(data)// 当请求成功时触发函数
			{
				// 删除一条后显示后面一条
				cancelAddNewInvoide();
				$("#succ_tips_lable").text(data);
		        $("#succ_tips_layer").show().centerV();
			}
		});
	}

}

function updateTheInvoice(){
	var invoiceInfo = {};

	var invoiceTbl = $('#invoiceInfoTblId');
	// type title jin'e amount NO. invoiceId
	var tdArr = invoiceTbl.find('td');
	invoiceInfo.type =$(tdArr[0]).find('input').val();
	invoiceInfo.title = $(tdArr[1]).find('input').val();
	invoiceInfo.money = $(tdArr[2]).find('input').val();
	invoiceInfo.amount = $(tdArr[3]).find('input').val();
	invoiceInfo.id = $(tdArr[5]).find('input').val();
	invoiceInfo.subscribeId = $('#parentOrderIdVal').text();
	
	jQuery.ajax({
		type : 'POST',
		url : '/ccms1/updateInvoice',
		data : {
			'invoiceInfo' : $.toJSON(invoiceInfo)
		},
		success : function(data)// 当请求成功时触发函数
		{
			$("#succ_tips_lable").text(data);
	        $("#succ_tips_layer").show().centerV();
		}
	});
}

function resetInvoiceArea() {
	var invoiceTbl = $('#invoiceInfoTblId');
	// type item title jin'e amount NO. invoiceId remark
	var tdArr = invoiceTbl.find('td');
	var typeArr = $(tdArr[0]).find('span');
	$(typeArr[0]).click();
	
	var itemArr = $(tdArr[1]).find('span');
	$(itemArr[0]).click();
	
	$(tdArr[2]).find('input').val('');
	$(tdArr[3]).find('input').val('');
	$(tdArr[4]).find('input').val('1');
	$(tdArr[5]).find('input').val('');
	$('#invoiceRemark').val();
}
function validateInvoiceInfo(invoiceInfo){
	if(''==invoiceInfo.title){
		$("#fail_tips_lable").text("发票抬头不能为空");
        $("#fail_tips_layer").show().centerV();
        return false;
	}
	if(!/^[1-9]\d*$/.test(invoiceInfo.money)){
		$("#fail_tips_lable").text("发票金额输入不合法");
        $("#fail_tips_layer").show().centerV();
        return false;
	}
	
	return true;
	
}
