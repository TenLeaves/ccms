$(function() {

	// 客户信息页面禁止输入
	$('#discount').attr('disabled', 'disabled');
	$('#receiveAddrId').attr('disabled', true);
	

	$('#orderCompany').bind('blur', function() {
		addCustomerOrNot();
	});
	$('#contactPerson').bind('blur', function() {
		addCustomerOrNot();
	});
	// 添加一行
	$('#addOrderInfo').bind("click", function() {
		var orderId = new Date().getTime();
		addOneOrder(orderId);
	});
	
	// 清空列表 清空的是配送填写区域，不是订单区域
	$('#clearOrderInfo').bind('click', function() {
		resetVal();
	});

	// 选中按钮改变样式 并且给隐藏的input赋值
	$(".orderWay").bind('click', function() {
		var value = $(this).attr('orderway');
		$(".orderWay").removeClass('sel-box-on');
		$(this).addClass('sel-box-on');
		$('#orderWay').val(value);
	});

	$(".productMode").bind('click', function() {
		var value = $(this).attr('productmode');
		$(".productMode").removeClass('red').addClass('grey');
		$(this).removeClass('grey').addClass('red');
		$('#productMode').val(value);
	});

	// 点击不连续月 ,不连续月
	$(".consistance").bind(
			'click',
			function() {
				var value = $(this).attr('consisTag');
				if (value == '2') {//不连续月
					$("input[type='checkbox']").attr('checked', false);
					$("#orderStartTime").val('');
					$("#orderEndTime").val('');
					$('.paper-monthes').attr('display', 'none');
					
					$('#noConsistantArea').show();
					$('#consisInputArea').hide();
					$($('.yearselect')[2]).click();//选中当前年

				} else {
					$("input[type='checkbox'][class='check-style']").attr(
							'checked', false);
					$("#yearHaSelectedId").val('');
					$("#monthSelected").val('');
					$('.yearselect').removeClass('sel-box-on');
					$('.paper-monthes').attr('display', 'none');
					var yearArr = $('.yearselect');
					for (i = 0; i < yearArr.length; i++) {
						var year = 2012 + i;
						$(yearArr[i]).text(year + "(0/13)")
					}
					$('#noConsistantArea').hide();
					$('#consisInputArea').show();
				}
				$('#orderStartTime').val('');
				$('#orderEndTime').val('');
				$('#isConstitance').val(value);
			});
	$(".ticket").bind('click', function() {
		var value = $(this).attr('ticket');
		$(".ticket").removeClass('red').addClass('grey');
		$(this).removeClass('grey').addClass('red');
		$('#isNeedTicket').val(value);
		if(value=='1'){
			$("#typeInvoiceFitstId").show();
		}else{
			$("#typeInvoiceFitstId").hide();
		}
	});
	//是否先打印发票
	$(".invoiceFirst").bind('click', function() {
		var value = $(this).attr('invoiceFirst');
		$(".invoiceFirst").removeClass('red').addClass('grey');
		$(this).removeClass('grey').addClass('red');
		$('#isTypeInvoiceFirst').val(value);
	});
	$(".sendAnyway").bind('click', function() {
		var value = $(this).attr('sendanyway');

		$(".sendAnyway").removeClass('red').addClass('grey');
		$(this).removeClass('grey').addClass('red');
		$('#isSendAnyway').val(value);
	});

	$(".custTypeId").bind('click', function() {
		var value = $(this).attr('custtype');
		$(".custTypeId").removeClass('sel-box-on');
		$(this).addClass('sel-box-on');
		$('#custTypeValue').val(value);
		if (value != "2") {
			$('#discount').val("");
			$('#discount').attr('disabled', true);
			$('#discount').attr('placeholder', "----");
		} else {
			$('#discount').attr('disabled', false);
			$('#discount').attr('placeholder', "折扣率(可为空)");
		}
	});

	// 查询客户信息 选择查询方式
	$('.searchCustManner').bind('click', function() {
		var _this = $(this);
		var qryCustManerVal = _this.attr('asManner');
		$('#searchCustMannerVal').val(qryCustManerVal);
		$('.searchCustManner').parent().removeClass('on');
		_this.parent().addClass('on');
	});

	// 客户信息查询按钮
	$('#qryCustInfoButton').bind('click', function() {
		var maner = $('#searchCustMannerVal').val();
		var keywords = $('#searcustCondition').val();

		jQuery.ajax({
			type : 'POST',
			url : '/ccms1/searchCust',
			data : {
				'qryManner' : maner,
				'condition' : keywords
			},
			success : function(data)// 当请求成功时触发函数
			{
				$('#custInfoListDiv').empty();
				$('#custInfoListDiv').append(data);
				$("#customer-layer").show();

			}
		});
	});
	// 选中一条大客户记录
	$('#selectCustFromListBtn')
			.bind(
					'click',
					function() {
						var radios = $('#custInfoListDiv').find(
								"input[type=radio]");
						var flag = false;
						for (i = 0; i < radios.length; i++) {
							var radio = $(radios[i]);
							if (!radio.attr('checked')) {
								continue;
							}
							flag = true;
							var parentTr = radio.closest('tr');// 获取选中的行
							var tds = parentTr.children();
							var custData = new Array();
							for (j = 1; j < tds.length; j++) {
								custData[j] = $(tds[j]).text();// 遍历每一列的值
							}
							var orderCompany = $('#orderCompany').val(
									custData[2]), contactPerson = $(
									'#contactPerson').val(custData[1]), contactPhone = $(
									'#contactPhone').val(custData[3]),
							// fixQuHao= $('#fixQuHao').val(custData[0]),
							// fixTle= $('#fixTle').val(custData[0]),
							// fixTrans= $('#fixTrans').val(custData[0]),
							discount = $('#discount').val(custData[5]), throughPerson = $(
									'#throughPerson').val(custData[6]), throPsonPhone = $(
									'#throPsonPhone').val(custData[7]), remark = $(
									'#remark').val(custData[8]);
							custId = $('#contactPersonId').val(custData[9]);

							$('#custInfoListDiv').empty();
							$('#customer-layer').css("display", 'none');
							$('#shadow').css("display", 'none');
							// 反填固话
							if (custData[10] != '' && custData[10] != null) {
								var fixInfo = custData[10].split('-');
								$('#fixQuHao').val(fixInfo[0]);
								$('#fixTle').val(fixInfo[1]);
								$('#fixTrans').val(fixInfo[2]);
							}
							// 反选客户类别
							var custArr = $('.custTypeId');
							for (i = 0; i < custArr.length; i++) {
								if ($(custArr[i]).attr('custtype') == custData[4]) {
									$(custArr[i]).click();
								}
							}
							// 选择后就不允许修改
							$('.custTypeId').attr('disabled', true);
							// $('#custInfoStep').find("input[type='text']").attr('disabled',
							// 'disabled');
							break;
						}
						if (!flag) {
							$("#fail_tips_lable").text('请选择一条客户信息');
							$("#fail_tips_layer").show().centerV();

						}
					});

	// 选择产品来自哪个库存
	$(".whereProFrom").bind('click', function() {
		$(".whereProFrom").removeClass('red').addClass('grey');
		$(this).removeClass('grey').addClass('red');
		var proFrom = $(this).attr('storeType');
		$('#whereProductFromId').val(proFrom);
	});
	// 选择物流
	$(".whoSendProd").bind('click', function() {
		$(".whoSendProd").removeClass('red2').addClass('grey');
		$(this).removeClass('grey').addClass('red2');
		var sendway = $(this).attr('sendway');
		if (sendway == '0') {// 自提
			$('#receiveAddrId').attr('disabled', true);
			
			$('#receiveNameId').val('');
			$('#receiveAddrId').val('');
			$('#receivePhoneId').val('');
		} else {
			$('#receiveAddrId').attr('disabled', false);
		}
		$('#whoSendProdid').val(sendway);
	});
	// 是否需要标签
	$(".isNeedBookMark").bind('click', function() {
		$(".isNeedBookMark").removeClass('red').addClass('grey');
		$(this).removeClass('grey').addClass('red');
		var needMark = $(this).attr('needVal');
		if(needMark=='2'){
			$('#markContenttr').show();
		}else{
			$('#markContenttr').hide();
		}
		$('#isNeedBookMarkId').val(needMark);
	});
	$('#completeOrder').bind('click', function() {
		subOrder();
	});

	$('.dateCss')
			.bind(
					'blur',
					function() {
						setTimeout(
								function() {
									var start = $('#orderStartTime').val(), end = $(
											'#orderEndTime').val();
									var totalNum = 0;
									if (start != '' && end != '') {

										var startYear = start.substring(0, 2), startMon = start
												.substring(5), endYear = end
												.substring(0, 2), endMonth = end
												.substring(5);
										totalNum = (parseInt(endYear) - parseInt(startYear))
												* 12
												+ parseInt(endMonth)
												- parseInt(startMon);
										if (totalNum > 0) {
											$('#totalSelectEdition').text(
													"共" + totalNum + "期");
										}
									}
								}, 1500);
					});

	// maodian
	$("a").each(function() {
		var link = $(this);
		var href = link.attr("href");
		if (href && href[0] == "#") {
			var name = href.substring(1);
			$(this).click(function() {
				var nameElement = $("[name='" + name + "']");
				var idElement = $("#" + name);
				var element = null;
				if (nameElement.length > 0) {
					element = nameElement;
				} else if (idElement.length > 0) {
					element = idElement;
				}

				if (element) {
					var offset = element.offset();
					window.parent.scrollTo(offset.left, offset.top);
				}

				return false;
			});
		}
	});
	
	//根据当前年初始化非连续年标签
	initNoneConsisArea();
	$($('.yearselect')[0]).click();
});

function addCustomerOrNot() {
	var company = $('#orderCompany').val(), name = $('#contactPerson').val();
	if (company != '' && name != '') {
		jQuery.ajax({
			type : 'POST',
			url : '/ccms1/qryCustomerByCompanyAndName',
			dataType : 'json',
			data : {
				'company' : company,
				'name' : name
			},
			success : function(data) {
				var customer = data.custInfo;
				if (customer != null && customer != undefined) {
					$("#fail_tips_lable").text('该客户信息已经存在，您可以点击查询选择，无需手动输入');
					$("#fail_tips_layer").show().centerV();
				}
			}
		});
	}
}
function addOneOrder(orderId) {
	var orderWay = $('#orderWay').val(), productMode = $('#productMode').val(), productName = (productMode == '1') ? '中国书法专业版'
			: '中国书法普及版', isConsistance = $('#isConstitance').val(), orderStartTime = $(
			'#orderStartTime').val(), orderEndTime = $('#orderEndTime').val(), amountEachDate = $(
			'#amountEachDate').val();
	// isNeedTicket = $('#isNeedTicket').val(),
	// isSendAnyway = $('#isSendAnyway').val();
	var verifyFlag = validateOrderInfo();
	if (!verifyFlag) {
		return;
	}
	
	orderAdd(orderId);
	//滚动到屏幕顶端
	var position = $('#TOPAREA').offset();
	window.parent.scrollTo(position.left, position.top);
}
// 删除一行订单
function deleteOrderRow(obj) {
	var tr = $(obj).parent().parent();// 获取a标签所在的行
	tr.remove();
	// 删除该行订单的配送列表
	var cloArr = tr.find('td');
	var orderId = $(cloArr[0]).text();
	if ($('#' + orderId).length > 0) {// 判断是否添加了配送单
		$('#' + orderId).remove();
	}
	//再次计算金额
	var type=  $(cloArr[5]).text();
	var start=  $(cloArr[6]).text();
	var end=  $(cloArr[7]).text();
	var flag=  $(cloArr[8]).text();
	var amount = $(cloArr[3]).text();
	var orderMoney=calculate(type,start,end,flag,amount);
	
	var curMoney = $('#currentOrderTotalMoney').text();
	var newTotalMoney = parseInt(curMoney)-orderMoney;
	$('#currentOrderTotalMoney').text(newTotalMoney);
	
}

// 删除一行配送信息
function deleteSendRow(obj) {
	var tr = $(obj).parent().parent();
	var colArr = $(tr).find('td');

	var amount = $(colArr[4]).text();
	var nowLeftAmt = $('#currentOrderLeftAmt').text();
	$('#currentOrderLeftAmt').text(parseInt(amount) + parseInt(nowLeftAmt));
	// 订单下的配送条数减一
	var rowArr = $('#orderHasSelectTbl tr');
	var colArr = $(rowArr[0]).find('td');
	var selectOrderId = $(colArr[0]).text();
	var preBindSendListAmt = $('#' + selectOrderId + 'SendListSize').text();
	$('#' + selectOrderId + 'SendListSize').text(
			parseInt(preBindSendListAmt) - 1);
	tr.remove();
	resetVal();
}

// 选择客户类别 大客户..
function selectCustType() {
	_this = this;
	$("input[custtype'custType']").removeClass("tdred").css('tdblack');
	_this.removeClass("tdblack").css("tdred");
}

// 上一部 下一步控制
function nextOrPre(id1, id2) {
	var divToHide = $('#' + id1);
	var divToShow = $('#' + id2);
	var ploca = '/ccms1/static/images/';

	// 客户下一步
	if (id1 == 'custInfoStep' && id2 == 'orderInfoStep') {
		var flag = checkCustInfo();
		if (flag) {
			$('#custInfoStepBack').removeClass('on').addClass('visited');
			$('#orderInfoStepBack').addClass('on');
			
		} else {
			return;
		}

	}
	// 订单下一步
	if (id1 == 'orderInfoStep' && id2 == 'sendListStep') {
		var rowArr = $('#orderListTable tr')
		if (rowArr.length <= 0) {
			$("#fail_tips_lable").text('目前没有任何订单，请先填写订单');
			$("#fail_tips_layer").show().centerV();
			return;
		}
		initOrderTable();// 在第三页初始化一个订单信息
		//把联系人 电话待到第三步
		$('#receiveNameId').val($('#contactPerson').val());
		$('#receivePhoneId').val($('#contactPhone').val());
		$('#orderInfoStepBack').removeClass('on').addClass('visited');
		$('#sendListStepBack').addClass('on');
	}
	// 订单返回
	if (id1 == 'orderInfoStep' && id2 == 'custInfoStep') {
		$('#orderInfoStepBack').removeClass('on');
		$('#custInfoStepBack').removeClass('visited').addClass('on');

	}
	// 送货返回
	if (id1 == 'sendListStep' && id2 == 'orderInfoStep') {
		$('#sendListStepBack').removeClass('on');
		$('#orderInfoStepBack').removeClass('visited').addClass('on');
	}

	divToHide.css('display', 'none');
	divToShow.css('display', 'block');
}

function saveCustomerInfo() {
	var orderCompany = $('#orderCompany').val(), contactPerson = $(
			'#contactPerson').val(), contactPhone = $('#contactPhone').val(), fixQuHao = $(
			'#fixQuHao').val(), fixTle = $('#fixTle').val(), fixTrans = $(
			'#fixTrans').val(), discount = $('#discount').val(), throughPerson = $(
			'#throughPerson').val(), throPsonPhone = $('#throPsonPhone').val(), remark = $(
			'#remark').val();

	var id = ""+new Date().getTime();
	id=id.substring(5);
	var newCustomer = {};
	newCustomer.CUSTOMER_ID = id;
	newCustomer.CUSTOMER_NAME = contactPerson;
	newCustomer.CUSTOMER_PHONE = contactPhone;
	newCustomer.CUSTOMER_TYPE = $('#custTypeValue').val();
	newCustomer.FIXED_TELEPHONE = fixQuHao + '-' + fixTle + '-' + fixTrans;
	newCustomer.COMPANY = orderCompany;
	newCustomer.DISCOUNT_RATE = discount;
	newCustomer.CONTACT_NAME = throughPerson;
	newCustomer.CONTACT_PHONE = throPsonPhone;
	newCustomer.REMARK = remark;
	jQuery.ajax({
		type : 'POST',
		url : '/ccms1/addOneNewCustomer',
		dataType : 'json',
		data : {
			'newCustomer' : $.toJSON(newCustomer)
		},
		success : function(data) {
			
			$('#contactPersonId').val(id);
		}
	});

	return id;
}
// 校验用户信息

function checkCustInfo() {
	var orderCompany = $('#orderCompany').val(), contactPerson = $(
			'#contactPerson').val(), contactPhone = $('#contactPhone').val(), fixQuHao = $(
			'#fixQuHao').val(), fixTle = $('#fixTle').val(), fixTrans = $(
			'#fixTrans').val(), discount = $('#discount').val(), throughPerson = $(
			'#throughPerson').val(), throPsonPhone = $('#throPsonPhone').val(), remark = $(
			'#remark').val(), custtype = $('#custTypeValue').val();

	// 清空所有红色边框
	$('.red_border').removeClass('red_border');

	if (orderCompany == '') {
		$('#orderCompany').addClass("red_border");
		showErrow($('#errorInfoCust'), '订购单位不能为空');
		return false;
	}
	if (contactPerson == '') {
		$('#contactPerson').addClass("red_border");
		showErrow($('#errorInfoCust'), '联系人不能为空');
		return false;
	}

	if (!/^[1-9]\d{10}$/.test(contactPhone)) {
		$('#contactPhone').addClass('red_border');
		showErrow($('#errorInfoCust'), '手机号码不是 11位数字');
		return false;
	}

	if (fixQuHao != '') {
		if (!/^\d{3,4}$/.test(fixQuHao)) {
			$('#fixQuHao').addClass('red_border');
			showErrow($('#errorInfoCust'), '区号只能是3-4位数字');
			return false;
		}
	}
	if (fixTle != '') {
		if (!/^\d{7,8}$/.test(fixTle)) {

			$('#fixTle').addClass('red_border');
			showErrow($('#errorInfoCust'), '固话只能是7-8位数字');
			return false;
		}

	}
	if (fixTrans != '') {
		if (!/^\d+$/.test(fixTrans)) {
			$('#fixTrans').addClass('red_border');
			showErrow($('#errorInfoCust'), '分机号只能是数字');
			return false;
		}

	}
	if (custtype == '2') {// 批发商才需要校验折扣率
		if (discount != '') {
			if (!/^[1-9]\d*$/.test(discount)) {
				$('#discount').addClass('red_border');
				showErrow($('#errorInfoCust'), '请输入正确的折扣');
				return false;
			}

		}
	}

	// if (throughPerson == '') {
	// $('#throughPerson').addClass('red_border');
	// showErrow($('#errorInfoCust'),'经联人不能为空');
	// return false;
	// }
	if (throPsonPhone != '') {
		if (!/\d{11}/.test(throPsonPhone)) {
			$('#throPsonPhone').addClass('red_border');
			showErrow($('#errorInfoCust'), '经联人电话不是11位数字');
			return false;
		}
	}

	return true;
}

function searchFromExist() {
	jQuery.startLoading();
	jQuery.ajax({
		type : 'GET',
		contentType : 'application/json',
		url : '/ccms1/searchCust',

		success : function(data)// 当请求成功时触发函数
		{
			jQuery.endLoading();
			$('#custInfoListDiv').empty();
			$('#custInfoListDiv').append(data);
			$('#customer-layer').css("display", 'block');
			// showDiv("custInfoToShowTb", "shadow");
		}
	});

}
function closethisDiv(id) {
	$('#' + id).css("display", "none");
	$('#shadow').css("display", 'none');
}

function resetVal() {
	$($('.orderWay')[0]).click();
	$($('.productMode')[0]).click();
	$($('.consistance')[0]).click();
	$('#orderStartTime').val('');
	$('#orderEndTime').val('');
	$('#amountEachDate').val('');
	$('#monthSelected').val('');
	$('#yearHaSelectedId').val('');
	$($('.consistance')[0]).click();
	
	
	initNoneConsisArea();
	

	$('#addOrderInfo').css('display', 'inline');
	$('#modifyOrderInfo').css('display', 'none');

}

function initOrderTable() {
	var rowArr = $('#orderListTable tr');
	for (i = 0; i < rowArr.length; i++) {
		var tempRow = rowArr[i]; // 行
		var colArr = $(tempRow).find('td');// 列
		// orderid proName showStr amount orderWay prodmode start
		// end isLianxu sendlistSize

		var eachColVal = {};
		eachColVal.orderId = $(colArr[0]).text();
		eachColVal.proName = $(colArr[1]).text();
		eachColVal.showStr = $(colArr[2]).text();
		eachColVal.amount = $(colArr[3]).text();
		// 给每一个订单添加一个空的配送列表
		if (!$('#' + eachColVal.orderId).length > 0) {// 该条件是防止用户返回到订单页面修改后重新到第三部，防止生成重复配送TB
			$('#sendListDiv')
					.append(
							"<table id='"
									+ eachColVal.orderId
									+ "' class='result-tb' cellspacing='0' cellpadding='0' border='0'> "
									+ "</table> ");
		}

		if (i == 0) {
			// 初始化第一条订单为选中状态
			$('#orderHasSelectTbl')
					.empty()
					.append(
							"<tr>" + "<td width='20%'>"
									+ eachColVal.orderId
									+ "</td> "
									+ "<td width='20%'>"
									+ eachColVal.proName
									+ "</td>"
									+ " <td width='16%'>"
									+ eachColVal.showStr
									+ "</td> "
									+ "<td width='16%'>"
									+ eachColVal.amount
									+ "</td>"
									+ "<td style='display:none;'>"
									+ $(colArr[6]).text()
									+ "</td> "
									+ // 隐藏的开始时间
									"<td style='display:none;'>"
									+ $(colArr[7]).text()
									+ "</td> "
									+ // 结束时间
									"<td>"
									+ "<a href='javascript:void(0);' class='c36c change-order' onclick='changeOrder();'>切换订单</a> "
									+ "<a href='javascript:void(0);' class='c36c copy-dispatch' onclick='copySendList();'>复制配送</a> "
									+ "<a href='javascript:void(0);' class='c36c lead-dispatch' onclick='insert_send()'>导入配送</a> "
									+ "</td>" + "</tr>");

			$('#' + eachColVal.orderId).css('display', '');// 初始化显示第一条订单的配送table
			// 该条订单可以分配的总数量
			$('#currentOrderLeftAmt').text(eachColVal.amount);
		}

	}

}

// 点解切换订单弹出所有订单
function changeOrder() {
	var rowArr = $('#orderListTable tr');
	$('#orderListToChangeTbl').empty();

	for (i = 0; i < rowArr.length; i++) {
		var tempRow = rowArr[i]; // 行
		var colArr = $(tempRow).find('td');// 列
		// orderid proName showStr amount orderWay prodmode start
		// end isLianxu bindsendlistamt

		var eachColVal = {};
		eachColVal.orderId = $(colArr[0]).text();
		eachColVal.proName = $(colArr[1]).text();
		eachColVal.showStr = $(colArr[2]).text();
		eachColVal.amount = $(colArr[3]).text();
		eachColVal.bindSendListAmt = $(colArr[9]).text();
		$('#orderListToChangeTbl')
				.append(
						"<tr>"
								+ "<td width='40'><input type='radio' class='radio-style' name='page-order' /></td>"
								+ "<td width='110'>" + eachColVal.orderId
								+ "</td>" + "<td width='150'>"
								+ eachColVal.proName + "</td>"
								+ "<td width='100'>" + eachColVal.showStr
								+ "</td>" + "<td width='90'>"
								+ eachColVal.amount + "</td>" + "<td>"
								+ eachColVal.bindSendListAmt + "</td>"
								+ "<td style='display:none;'>"
								+ $(colArr[6]).text() + "</td> " + // 隐藏的开始时间
								"<td style='display:none;'>"
								+ $(colArr[7]).text() + "</td> " + // 结束时间
								"</tr>");
	}

	$('#orderListToChangeDiv').css('display', 'block');

}

// 添加一条配送信息
function addSendInfo() {
	var rowArr = $('#orderHasSelectTbl tr');
	var colArr = $(rowArr[0]).find('td');
	var selectOrderId = $(colArr[0]).text();

	var currOrdSendListTable = $('#' + selectOrderId);// 获取当前选中的订单对应的配送列表

	// 获取页面填写的值
	var proFrom = $('#whereProductFromId').val();
	var amountPerMonth = $('#sendAmountPerMonth').val();
	var sendWay = $('#whoSendProdid').val();
	
	var isNeedBookMark = $('#isNeedBookMarkId').val();
	var postCode = $('#postCodeId').val();
	var markVal = $('#markValueId').val();

	// 添加之前判断用户的分配
	var leftAmount = $('#currentOrderLeftAmt').text();
	var inputAmount = parseInt(amountPerMonth);
	leftAmount = parseInt(leftAmount);
	var checkFlag = validateSendInfo();
	
	var receivName = $('#receiveNameId').val();
	var receivAddr = $('#receiveAddrId').val();
	var receivPhone = $('#receivePhoneId').val();
	if (!checkFlag) {
		return;
	}
	if (inputAmount > leftAmount) {
		showErrow($("#errorInfoSend"),'当前订单剩余可分配数为：' + leftAmount);
		return;
	}
	//针对自提的处理
	if(receivAddr=='') {receivAddr='--';}
	if(receivName=='') {receivName='--';}
	if(receivPhone=='') {receivPhone='--';}
	
	currOrdSendListTable
			.append("<tr>" + "<td style='display:none;'>"
					+ new Date().getTime()
					+ "</td>"
					+ "<td width='20%'>"
					+ receivAddr
					+ "</td>"
					+ "<td width='20%'>"
					+ receivName
					+ "</td>"
					+ "<td width='20%'>"
					+ receivPhone
					+ "</td>"
					+ "<td width='20%'>"
					+ amountPerMonth
					+ "</td>"
					+ "<td style='display:none;'>"
					+ sendWay
					+ "</td>"
					+ "<td style='display:none;'>"
					+ isNeedBookMark
					+ "</td>"
					+ "<td style='display:none;'>"
					+ postCode
					+ "</td>"
					+ "<td style='display:none;'>"
					+ proFrom
					+ "</td>"
					+ "<td style='display:none;'>"
					+ markVal
					+ "</td>"
					+ "<td>"
					+ "<a href='javascript:void(0);' class='dusbin'  onclick='deleteSendRow(this);'>&nbsp;</a>"
					+ "</td>" + "</tr>");
	// 当前订单绑定的配送单数量 加一操作
	var preBindSendListAmt = $('#' + selectOrderId + 'SendListSize').text();
	$('#' + selectOrderId + 'SendListSize').text(
			parseInt(preBindSendListAmt) + 1);
	// 更新页面可分配数量
	$('#currentOrderLeftAmt').text(leftAmount - inputAmount);

	resetSendInput();
	
	$('#receiveNameId').val(receivName);
	$('#receivePhoneId').val(receivPhone);
	
	$('#addSendInfoArea').hide();
	var element = $('#TOPAREA');
	var offset = element.offset();
	window.parent.scrollTo(offset.left, offset.top);
	

}

// 选中某条订单点击确定 setp3
function choseOneOrder() {
	var totalNum = 0;
	var usedNum = 0;

	// redio orderid proNmae showStr amount starttime endtime
	var isSelectTag = false;
	// 获取之前选中的订单TB
	var rows = $('#orderHasSelectTbl tr');
	// 获取之前选中订单号
	var preColArr = $(rows[0]).find('td');
	var preOrderId = $(preColArr[0]).text();

	var rowArr = $('#orderListToChangeTbl tr');
	for (i = 0; i < rowArr.length; i++) {
		var tempRow = rowArr[i]; // 行
		var colArr = $(tempRow).find('td');// 列
		var radio = $(colArr[0]).find('input');
		var ischecked = $(radio).attr('checked');
		if (!ischecked) {// 没有选中
			continue;
		}
		isSelectTag = true;
		// 移除之前的已选中订单
		var selectTblRowArr = $('#orderHasSelectTbl tr');
		$(selectTblRowArr[0]).remove();

		// 把选中的订单显示到已选中tb
		$('#orderHasSelectTbl')
				.append(
						"<tr>" + "<td width='20%'>"
								+ $(colArr[1]).text()
								+ "</td> "
								+ "<td width='20%'>"
								+ $(colArr[2]).text()
								+ "</td>"
								+ " <td width='16%'>"
								+ $(colArr[3]).text()
								+ "</td> "
								+ "<td width='16%'>"
								+ $(colArr[4]).text()
								+ "</td>"
								+ "<td style='display:none;'>"
								+ $(colArr[5]).text()
								+ "</td> "
								+ // 隐藏的开始时间
								"<td style='display:none;'>"
								+ $(colArr[6]).text()
								+ "</td> "
								+ // 结束时间
								"<td>"
								+ "<a href='javascript:void(0);' class='c36c change-order' onclick='changeOrder();'>切换订单</a> "
								+ "<a href='javascript:void(0);' class='c36c copy-dispatch' onclick='copySendList();'>复制配送</a> "
								+ "<a href='javascript:void(0);' class='c36c lead-dispatch' onclick='insert_send()'>导入配送</a> "
								+ "</td>" + "</tr>");
		totalNum = parseInt($(colArr[4]).text());// 获取选中订单可分配的总数量

		// 切换配送单Tb的内容
		$('#' + preOrderId).css('display', 'none');// 隐藏之前订单的对应的送货列表
		$('#' + $(colArr[1]).text()).css('display', '');// 显示选中订单对应的送货列表
		// 获取该订单号下配送列表用掉的数量
		usedNum = getUsedAmount($('#' + $(colArr[1]).text()));
		// 隐藏订单选择tb
		$('#orderListToChangeDiv').css('display', 'none');
		// 设置该订单可以绑定的数量
		var avilableAmt = parseInt(totalNum) - parseInt(usedNum);
		$('#currentOrderLeftAmt').text(avilableAmt);
		break;
	}
	if (!isSelectTag) {// 一条都没有选中
		$("#fail_tips_lable").text('请选择订单');
		$("#fail_tips_layer").show().centerV();
	}

}
// 复制配送信息
function copySendList() {
	if ($('#orderWithOutSendInfoDiv').css('display') == 'block') {
		return;
	}

	/** ***Step1:初始化没有配送信息的订单供选择******************* */
	var rowArr = $('#orderListTable tr');
	for (i = 0; i < rowArr.length; i++) {
		var tempRow = rowArr[i]; // 行
		var colArr = $(tempRow).find('td');// 列
		// orderid proName showStr amount orderWay prodmode start end isLianxu
		// sendListAmt
		var orderId = $(colArr[0]).text();
		var currOrdSendSize = $('#' + orderId + 'SendListSize').text();
		if (currOrdSendSize != '0') {// 已经有配送信息的订单不展示给用户
			continue;
		}
		var orderToSel = $('#orderWithoutSendInfoTbl');
		orderToSel
				.empty()
				.append(
						"<tr>"
								+ "<td width='50'><input type='checkbox' class='check-style' name='checkbox4' /></td>"
								+ "<td width='110'>" + $(colArr[0]).text()
								+ "</td>" + "<td width='150'>"
								+ $(colArr[1]).text() + "</td>"
								+ "<td width='150'>" + $(colArr[2]).text()
								+ "</td>" + "<td>" + $(colArr[3]).text()
								+ "</td>" + "</tr>");
	}

	$('#orderWithOutSendInfoDiv').css('display', 'block');
}

// 选择需要复制到的订单
function choseOrderToCopy() {
	// orderid addr name phone amount kuaidi postcode profrom
	/** ***Step1:获取当前显示订单的配送地址信息******************* */
	// 获取当前选中订单号
	var rows = $('#orderHasSelectTbl tr');
	var preColArr = $(rows[0]).find('td');// 第一行是表头
	// 根据订单号获取配送Tbl
	var preOrderId = $(preColArr[0]).text();

	var sendInfoArr = new Array();
	var currSendRowArr = $("#" + preOrderId + " tr");

	var copyTotalAmount = 0;
	//
	var sendListSize = currSendRowArr.length - 1;
	for (i = 0; i < currSendRowArr.length; i++) {// 一个订单可能有多条配送信息
		var dataCol = $(currSendRowArr[i]).find('td');
		var sendInfo = {};
		sendInfo.addr = $(dataCol[1]).text();
		sendInfo.name = $(dataCol[2]).text();
		sendInfo.tel = $(dataCol[3]).text();
		sendInfo.amount = $(dataCol[4]).text();
		sendInfo.sendWay = $(dataCol[5]).text();
		sendInfo.isNeedMark = $(dataCol[6]).text();
		sendInfo.postCode = $(dataCol[7]).text();
		sendInfo.profrom = $(dataCol[8]).text();
		sendInfoArr[i] = sendInfo;

		copyTotalAmount += parseInt(sendInfo.amount);
	}
	// checkbox orderid proname showstr amount
	var successFlag = true;
	var orderToCopyTbRows = $('#orderWithoutSendInfoTbl tr');
	var checkTotalNum = 0;
	for (i = 0; i < orderToCopyTbRows.length; i++) {
		var colData = $(orderToCopyTbRows[i]).find('td');
		var checkBox = $(colData[0]).find('input');
		var ischecked = $(checkBox).attr('checked');
		if (!ischecked) {// 没有选中
			continue;
		}
		checkTotalNum++;
		var orderTotalAmt = $(colData[4]).text();
		orderTotalAmt = parseInt(orderTotalAmt);
		if (copyTotalAmount > orderTotalAmt) {
			$("#fail_tips_lable").text(
					'复制的配送单的产品总数量:' + copyTotalAmount + '  大于所选订单['
							+ $(colData[1]).text() + "]的总数量：" + orderTotalAmt);
			$("#fail_tips_layer").show().centerV();
			successFlag = false;
			break;// 数量出错就退出复制流程
		}
		$('#currentOrderLeftAmt').text(orderTotalAmt-copyTotalAmount);
		/** ****选中就在该订单对应的配送列表添加数据****************** */
		var orderId = $(colData[1]).text();
		var sendListTb = $('#' + orderId);
		for (j = 0; j < sendInfoArr.length; j++) {// 添加多条配送信息
			var sendInfo = sendInfoArr[j];
			var newOrderId = new Date().getTime()// 后期需要从序列读取
			sendListTb
					.append("<tr>" + "<td style='display:none;'>"
							+ newOrderId
							+ "</td>"
							+ "<td width='20%'>"
							+ sendInfo.addr
							+ "</td>"
							+ "<td width='20%'>"
							+ sendInfo.name
							+ "</td>"
							+ "<td width='20%'>"
							+ sendInfo.tel
							+ "</td>"
							+ "<td width='20%'>"
							+ sendInfo.amount
							+ "</td>"
							+ "<td style='display:none;'>"
							+ sendInfo.sendWay
							+ "</td>"
							+ "<td style='display:none;'>"
							+ sendInfo.isNeedMark
							+ "</td>"
							+ "<td style='display:none;'>"
							+ sendInfo.postCode
							+ "</td>"
							+ "<td style='display:none;'>"
							+ sendInfo.profrom
							+ "</td>"
							+ "<td>"
							+ "<a href='javascript:void(0);' class='edit'></a>"
							+ "<a href='javascript:void(0);' class='dusbin'  onclick='deleteSendRow(this);'></a>"
							+ "</td>" + "</tr>");
			var preBindSendListAmt = $('#' + orderId + 'SendListSize').text();
			$('#' + orderId + 'SendListSize').text(
					parseInt(preBindSendListAmt) + 1);
		}
	}
	if (checkTotalNum < 1) {// 一条都没选
		$("#fail_tips_lable").text('请选择订单');
		$("#fail_tips_layer").show().centerV();
		return;
	}
	if (!successFlag) {// 失败了要把之前选中的成功的配送单清空
		// 再次遍历所有供选择的订单注意清空选中的订单 对应的配送单
		for (i = 0; i < orderToCopyTbRows.length; i++) {
			var colData = $(orderToCopyTbRows[i]).find('td');
			var checkBox = $(colData[0]).find('input');
			var ischecked = $(checkBox).attr('checked');
			if (!ischecked) {// 没有选中
				continue;
			}
			var orderId = $(colData[1]).text();
			var sendListTb = $('#' + orderId);
			sendListTb.empty();
			var preBindSendListAmt = $('#' + orderId + 'SendListSize').text();
			$('#' + orderId + 'SendListSize').text(0);
		}
	}
	
	$('#orderWithoutSendInfoTbl').empty();
	$('#orderWithOutSendInfoDiv').css('display', 'none');
}

function getUsedAmount(sendTable) {
	var totalAmountInEachSendList = 0;
	var sendListRows = $(sendTable).find('tr');
	for (i = 0; i < sendListRows.length; i++) {
		var colArr = $(sendListRows[i]).find('td');
		var amount = $(colArr[4]).text();
		totalAmountInEachSendList += parseInt(amount);
	}
	return totalAmountInEachSendList;
}
function validateOrderInfo() {
	var orderStartTime = $('#orderStartTime').val(), orderEndTime = $(
			'#orderEndTime').val(), amountEachDate = $('#amountEachDate').val(), isConsistance = $(
			'#isConstitance').val(), discount = $('#discount').val();
	var regexp = /^[1-9]\d*$/;
	$('.red_border').removeClass('red_border');

	if (isConsistance == '2') {
		orderStartTime = $('#monthSelected').val();
	}
	if (orderStartTime == '' || orderStartTime == undefined) {

		showErrow($('#errorInfoOrder'), '请选择订阅起始时间')
		return false;
	}
	if (isConsistance == '1') {
		if (orderEndTime == '' || orderEndTime == undefined) {
			$('#orderEndTime').addClass('red_border');
			showErrow($('#errorInfoOrder'), '请选择期刊数终止时间')
			return false;
		}
		if (orderStartTime > orderEndTime) {
			$('#orderStartTime').addClass('red_border');
			$('#orderEndTime').addClass('red_border');
			showErrow($('#errorInfoOrder'), '订阅结束时间不能早于订阅起始时间')
			return false;
		}
	}
	if (!regexp.test(amountEachDate)) {
		$('#amountEachDate').addClass('red_border');
		showErrow($('#errorInfoOrder'), '请填写合法的订阅数量')
		return false;
	}
	return true;
}

function validateSendInfo() {
	var amountPerMonth = $('#sendAmountPerMonth').val();
	var sendWay = $('#whoSendProdid').val();
	var receivName = $('#receiveNameId').val();
	var receivAddr = $('#receiveAddrId').val();
	var receivPhone = $('#receivePhoneId').val();
	var postCode = $('#postCodeId').val();

	var regexp = /^[1-9]\d*$/;
	if (!regexp.test(amountPerMonth)) {
		
		showErrow($("#errorInfoSend"),'请填写合法的配送数');
		return false;
	}
	if (sendWay != '0') {
		if (receivAddr == '' || receivAddr == undefined) {
			showErrow($("#errorInfoSend"),'寄送地址不能为空');
			return false;
		}
		if (receivName == '' || receivName == undefined) {
			showErrow($("#errorInfoSend"),'收件人不能为空');
			return false;
		}
		if (!/^1\d{10}$/.test(receivPhone)) {
			showErrow($("#errorInfoSend"),'收件人电话必须是1开头的11位数字');
			return false;
		}
	}else{//把大客户的信息带过来
		if (receivName == '' || receivName == undefined) {
			$('#receiveNameId').val($('#contactPerson').val());
		}
		if (!/^1\d{10}$/.test(receivPhone)) {
			
			$('#receivePhoneId').val($('#contactPhone').val());

		}
	}
	
	if (postCode != '') {
		if (!/^\d+$/.test(postCode)) {
			showErrow($("#errorInfoSend"),'邮编只能是数字');
			return false;
		}

	}

	return true;
}

function subOrder() {
	// orderid proName showStr amount orderWay prodmode fapiao sendAny start
	// end isLianxu bindsendlistamt
	var rowArr = $('#orderListTable tr');
	var orderArr = new Array();
	var totalSendAmount = 0;
	for (i = 0; i < rowArr.length; i++) {
		var colArr = $(rowArr[i]).find('td');
		if ($(colArr[9]).text() == 0) {
			$("#fail_tips_lable").text(
					'订单 [ ' + $(colArr[0]).text() + " ] 没有绑定任何配送信息");
			$("#fail_tips_layer").show().centerV();
			return;
		}
		var eachOrder = {};
		eachOrder.orderId = $(colArr[0]).text();
		eachOrder.prodmode = $(colArr[5]).text();
		eachOrder.amount = $(colArr[3]).text();
		eachOrder.orderWay = $(colArr[4]).text();
		// eachOrder.fapiao=$(colArr[6]).text();
		// eachOrder.sendAny=$(colArr[7]).text();
		eachOrder.start = $(colArr[6]).text();
		eachOrder.end = $(colArr[7]).text();
		eachOrder.consistant = $(colArr[8]).text();
		// 获取该订单下的所有配送列表 配送列表每行的数据格式如下
		// orderid addr name phone amount sendway bookmark postCode proForm
		var eachOrderSendArr = new Array();

		var sendRowArr = $('#' + eachOrder.orderId).find('tr');
		var eachSendTotal = 0;
		for (j = 0; j < sendRowArr.length; j++) {
			var sendColArr = $(sendRowArr[j]).find('td');
			var eachSendInfo = {};
			eachSendInfo.orderId = $(sendColArr[0]).text();
			eachSendInfo.addr = $(sendColArr[1]).text();
			eachSendInfo.name = $(sendColArr[2]).text();
			eachSendInfo.phone = $(sendColArr[3]).text();
			eachSendInfo.amout = $(sendColArr[4]).text();
			eachSendInfo.sendway = $(sendColArr[5]).text();
			eachSendInfo.bookmark = $(sendColArr[6]).text();
			eachSendInfo.postcode = $(sendColArr[7]).text();
			eachSendInfo.profrom = $(sendColArr[8]).text();
			eachSendInfo.markVal = $(sendColArr[9]).text();
			eachSendInfo.remark = $(sendColArr[10]).text();
			eachOrderSendArr[j] = eachSendInfo;
			eachSendTotal=eachSendTotal+parseInt(eachSendInfo.amout);
		}
		// 把得到的配送列表添加到订单 配送列表是个数组
		eachOrder.sendList = eachOrderSendArr;
		orderArr[i] = eachOrder;
		totalSendAmount=totalSendAmount+eachSendTotal//eachSendTotal没条子订单的配送数量
		
	}// 子订单装配完毕
	if(totalSendAmount>parseInt(eachOrder.amount)){
		$("#fail_tips_lable").text(
				"您提交的该笔订单配送数量超过订单的订购总数");
		$("#fail_tips_layer").show().centerV();
		return;
	}
	
	// 装配客户信息
	var customerId = $('#contactPersonId').val();// 只传用户ID后台重新查询信息
	var custInfo = {};
	if(customerId==''){
		 
		 customerId =saveCustomerInfo();
	}
	custInfo.custId = customerId;
	// 装配该总订单的可选信息 需要发票、未付款先发货
	var isNeedTicket = $('#isNeedTicket').val(), isSendAnyway = $(
			'#isSendAnyway').val(), istypeInvioceFirst=$('#isTypeInvoiceFirst').val(),subscrible = {};
	subscrible.ticket = isNeedTicket;
	subscrible.sendFirst = isSendAnyway;
	subscrible.typeFirst=istypeInvioceFirst;
	// 提交订单
	jQuery.startLoading();
	jQuery.ajax({
		type : 'POST',
		url : '/ccms1/subMainCustOrder',
		dataType : 'json',
		data : {
			'orders' : $.toJSON(orderArr),
			'custinfo' : $.toJSON(custInfo),
			'subscrible' : $.toJSON(subscrible)
		},
		success : function(data) {
			jQuery.endLoading();
			
			if (data.tag == '0') {
			   
				window.location.href = "/ccms1/comPage?data=toOrderInfoInput";
			} else {
				$("#fail_tips_lable").text(data.msg);
				$("#fail_tips_layer").show().centerV();
			}

		}
	});
}
// 导入配送的确认按钮
var importSendData={};
function importSubmit() {

	$("#result_info1").html(0);
	$("#result_info2").html(0);
	$("#result_info3").html(0);
	var tb = document.getElementById('tal_error');
	var rowNum = tb.rows.length;
	for ( var i = 1; i < rowNum; i++) {
		tb.deleteRow(i);
		rowNum = rowNum - 1;
		i = i - 1;
	}
	var import_form = $("#fileInfo").val();
	if ("" == import_form) {
		$("#fail_tips_lable").text("您需要选择导入文件的地址");
		$("#fail_tips_layer").show().centerV();
		return;
	}
	$('#import_form')
			.ajaxSubmit(
					{
						dataType : "json",
						success : function(data) {
							if (data.status == "success") {

								$("#result_info1").html(data.sum);
								$("#result_info2").html(data.infoLength);
								$("#result_info3").html(data.errorLength);
								
								importSendData=data;
								
								var errorInfos = data.errorInfos;
								for ( var i = 0; i < errorInfos.length; i++) {
									$("#tal_error").empty().append(
											"<tr>" + "<td width='120'>"
													+ (i + 1) + "</td>"
													+ "<td width='200'>"
													+ errorInfos[i].error
													+ "</td>" + "<td>"
													+ errorInfos[i].right
													+ "</td>" + "</tr>");
								}
								

							} 
							
						}
					});
}

function showImportDiv() {

	// $('#importSendList').removeClass('hidden');
	showDiv("importSendList", "shadow");
}
function showOrderMonthDetail(obj) {
	// orderid proName showStr amount orderWay prodmode start
	// end isLianxu sendlistSize
	var row = $(obj).parent();
	var cols = row.find('td');
	var startTime = $(cols[6]).text();// 2013-05
	var endTime = $(cols[7]).text();
	var prodmode = $(cols[8]).text();
	var content = '';
	if (prodmode == 1) {// 连续月
		content = startTime.substring(0, 4) + "年" + startTime.substring(5)
				+ "月 至" + endTime.substring(0, 4) + "年" + endTime.substring(5)
				+ "月"
	} else {
		var ordertimeArr = startTime.split(',');
		ordertimeArr = ordertimeArr.sort();
		for (i = 0; i < ordertimeArr.length; i++) {
			var year = ordertimeArr[i].substring(0, 4);
			var month = ordertimeArr[i].substring(5);
			if (i == 0) {
				content = content + year + "年</br>&nbsp;&nbsp;" + month + "月/";
			} else {
				var preyear = ordertimeArr[i - 1].substring(0, 4);
				if (preyear == year) {
					content = content + month + "月/"
				} else {
					content = content + "</br>" + year + "年</br>&nbsp;&nbsp;"
							+ month + "月/"
				}
			}
		}
	}
	$('#selectMonthDetailId').empty().append(content).removeClass('hidden')
			.css('cursor', 'pointer');

}
function hiddenMonthDetail() {
	$('#selectMonthDetailId').empty().addClass('hidden');
}

// 选择年份
function year_info(obj) {
	var $this = $(obj);
	var parentL = $this.closest(".paper-years").offset().left, thisL = $this
			.offset().left;
	$this.nextAll(".paper-monthes").css({
		"left" : parentL - thisL + "px"
	});
	$this.closest(".paper-years").find(".sel-box").removeClass("sel-box-on");
	$this.addClass("sel-box-on");
	$this.parent(".all-year").addClass("all-year-on").siblings(".all-year")
			.removeClass("all-year-on");

	var selectYear = $(obj).text().substring(0, 4);
	// 赋值
	$('#yearHaSelectedId').val(selectYear);

	$(".yearselect").removeClass('sel-box-on');
	$(obj).addClass('sel-box-on');
	var choiceArr = $("input[type='checkbox'][name='" + selectYear + "']");
	choiceArr.attr("checked", false);

	var period = $('#monthSelected').val();
	var yearArr = period.split(",");
	yearArr = yearArr.sort();
	for (i = 0; i < yearArr.length; i++) {// 2013-04
		var year = yearArr[i].substring(0, 4);
		var month = yearArr[i].substring(5);
		if (year != selectYear) {
			continue;
		}

		for (j = 0; j < choiceArr.length; j++) {
			if ($(choiceArr[j]).attr('monthvalue') == month) {
				$(choiceArr[j]).attr('checked', true);
			}
		}
	}
}

// 选择月份
function month_info(obj) {

	var selectyear = $('#yearHaSelectedId').val();
	var allmonth = $('#monthSelected').val();
	if (allmonth.substring(0, 1) == ',') {
		allmonth = allmonth.substring(1);
	}
	var month = $(obj).attr('monthvalue');
	var flag = $(obj).attr('checked');// 选中还是取消
	if (flag) {// 添加月份
		if (allmonth == '') {
			$('#monthSelected').val(selectyear + "-" + month);
		} else {
			$('#monthSelected').val(allmonth + "," + selectyear + "-" + month);
		}

		var yearArr = $(".yearselect");
		for (i = 0; i < yearArr.length; i++) {// 2010(0/13)
			var year = $(yearArr[i]).text().substring(0, 4);
			var monthMnt = $(yearArr[i]).text().substring(5, 6);
			if (selectyear != year) {
				continue;
			}
			var value = year + "(" + (parseInt(monthMnt) + 1) + "/13)";
			$(yearArr[i]).text(value);
			break;
		}
	} else {// 取消选中
		var index = allmonth.indexOf(selectyear + "-" + month);
		allmonth = allmonth.substring(0, index - 1)
				+ allmonth.substring(index + 7);
		$('#monthSelected').val(allmonth);
		var yearArr = $(".yearselect");
		for (i = 0; i < yearArr.length; i++) {// 2010(0/13)
			var year = $(yearArr[i]).text().substring(0, 4);
			var monthMnt = $(yearArr[i]).text().substring(5, 6);
			if (selectyear != year) {
				continue;
			}
			var value = year + "(" + (parseInt(monthMnt) - 1) + "/13)";
			$(yearArr[i]).text(value);
		}

	}
}
function showOrderDetail(obj) {
	// orderid proName showStr amount orderWay prodmode start (0-6)
	// end isLianxu sendlistSize(7-9)
	$('#yearHaSelectedId').val('');
	$('#monthSelected').val('');

	$('#modifyOrderInfo').css('display', 'block');// BUTTON
	$('#addOrderInfo').css('display', 'none');// BUTTON
	// 获取原先填写的值
	var clickRow = $(obj).parent().parent();
	// 点击某行高亮
	var orderListTbRowArr = $('#orderListTable').find('tr');
	orderListTbRowArr.removeClass('selectedRow')
	clickRow.addClass('selectedRow');

	var colArr = clickRow.find('td');

	var orderId = $(colArr[0]).text();
	var orderWay = $(colArr[4]).text();
	var prodmode = $(colArr[5]).text();
	var isLianxu = $(colArr[8]).text();
	var startTime = $(colArr[6]).text();
	var endTime = $(colArr[7]).text();
	var amount = $(colArr[3]).text();
	
	//先减去修改本条订单的金额
    var orderMoney=calculate(prodmode,startTime,endTime,isLianxu,amount);
	var curMoney = $('#currentOrderTotalMoney').text();
	var newTotalMoney = parseInt(curMoney)-orderMoney;
	$('#currentOrderTotalMoney').text(newTotalMoney);
	
	// 记住当前修改的订单号
	$('#ordertoModifyid').val(orderId);

	var orderWayOption = $('.orderWay');// 反选订购方式
	for (i = 0; i < orderWayOption.length; i++) {
		if ($(orderWayOption[i]).attr('orderway') == orderWay) {
			$(orderWayOption[i]).click();
			break;
		}
	}
	var prodModeOption = $('.productMode');// 反选产品类型
	for (i = 0; i < prodModeOption.length; i++) {
		if ($(prodModeOption[i]).attr('productmode') == prodmode) {
			$(prodModeOption[i]).click();
			break;
		}
	}
	// 反填订阅数量
	$('#amountEachDate').val(amount);
	// 反填订阅周期
	var isLianxuOption = $('.consistance');

	if (isLianxu == '1') {
		$(isLianxuOption[0]).click();
		$('#orderStartTime').val(startTime);
		$('#orderEndTime').val(endTime);
	} else {
		$(isLianxuOption[1]).click();
		var orderPeriod = startTime.split(',');
		orderPeriod = orderPeriod.sort();
		var yearOption = $('.yearselect');

		for (m = 0; m < orderPeriod.length; m++) {
			var selectyear = orderPeriod[m].substring(0, 4);
			var selectMonth = orderPeriod[m].substring(5);
			var monthOption = $("input[type='checkbox'][name='" + selectyear
					+ "']");
			// 根据数据模拟手动选择一遍日期 选择年
			var showyear = orderPeriod[0].substring(0, 4);
			for (k = 0; k < yearOption.length; k++) {
				var yearval = $(yearOption[k]).text().substring(0, 4);
				if (yearval == selectyear) {
					$(yearOption[k]).click();
					break;
				}
			}

			// 选择月
			for (j = 0; j < monthOption.length; j++) {
				if ($(monthOption[j]).attr('monthvalue') == selectMonth) {
					$(monthOption[j]).attr('checked', true);
					$(monthOption[j]).click();
					break;
				}
			}
		}
		// 展示用户选择的最早的年份
		var firstToshow = orderPeriod[0].substring(0, 4);
		for (n = 0; n < yearOption.length; n++) {
			var yearval = $(yearOption[n]).text().substring(0, 4);
			if (yearval == firstToshow) {
				$(yearOption[n]).click();
				break;
			}
		}

	}
	$('#addOrderInfoArea').show();

}

// 修改当前选中行的值
function modifyRow() {
	var orderToModifyId = $('#ordertoModifyid').val();
	var orderListTbRowArr = $('#orderListTable').find('tr');

	for (i = 0; i < orderListTbRowArr.length; i++) {
		var colArr = $(orderListTbRowArr[i]).find('td');
		if ($(colArr[0]).text() == orderToModifyId) {
			modifyCurrentRow(colArr);
			$(orderListTbRowArr[i]).removeClass('selectedRow');
			break;
		}
	}

}
function modifyCurrentRow(colArr) {
	var orderWay = $('#orderWay').val(), productMode = $('#productMode').val(), productName = (productMode == '1') ? '中国书法专业版'
			: '中国书法普及版', isConsistance = $('#isConstitance').val(), orderStartTime = $(
			'#orderStartTime').val(), orderEndTime = $('#orderEndTime').val(), amountEachDate = $(
			'#amountEachDate').val();
	// isNeedTicket = $('#isNeedTicket').val(),
	// isSendAnyway = $('#isSendAnyway').val();
	var verifyFlag = validateOrderInfo();
	if (!verifyFlag) {
		return;
	}
	var orderArr = new Array();

	var startTime = '';
	var endTime = $('#orderEndTime').val();
	var noCositantTime = $('#monthSelected').val();
	// 不连续月 需要选择
	var showStr = '';
	if (isConsistance == '2') {

		if (noCositantTime.length > 16) {
			showStr = noCositantTime.substring(0, 14) + "...";
		} else {
			showStr = noCositantTime;
		}

		startTime = noCositantTime;

	} else {
		startTime = $('#orderStartTime').val()
		showStr = startTime + "至" + endTime;
	}
	// orderid proName showStr amount orderWay prodmode start (0-6)
	// end isLianxu sendlistSize(7-9)
	$(colArr[1]).text(productName);
	$(colArr[2]).text(showStr);
	$(colArr[3]).text(amountEachDate);
	$(colArr[4]).text(orderWay);
	$(colArr[5]).text(productMode);
	$(colArr[6]).text(startTime);
	$(colArr[7]).text(endTime);
	$(colArr[8]).text(isConsistance);
	
	//根据修改后的值重新计算价格
	var orderMoney=calculate(productMode,startTime,endTime,isConsistance,amountEachDate);
	var curMoney = $('#currentOrderTotalMoney').text();
	var newTotalMoney = parseInt(curMoney)+orderMoney;
	$('#currentOrderTotalMoney').text(newTotalMoney);

	resetVal();
	$('#addOrderInfoArea').hide();
//	$("#succ_tips_lable").text('修改成功');
//	$("#succ_tips_layer").show().centerV();

}

function closeOrderToshowTbl() {
	$('#orderListToChangeDiv').css('display', 'none');
}
function closeOrderWihtoutSend() {
	$('#orderWithoutSendInfoTbl')
			.empty()
			.append(
					"<tr> <td colspan='2' class='trLeft'>订阅列表</td> <td colspan='6' class='close'><img src='/ccms1/static/images/close1.png' onclick='closeOrderWihtoutSend();'/></td></tr>"
							+ " <tr> <td >全选</td> <td><input type='checkbox' name='checkbox' id='checkbox' /></td> <td width='130'>订阅序列</td> <td width='133'>产品</td> <td width='133'>订阅周期</td> <td width='96'>订阅数</td> </tr>");
	$('#orderWithOutSendInfoDiv').css('display', 'none');
}
function cancelImport() {
	$('#importSendList').css('display', 'none');
	$('#shadow').css('display', 'none');
}

function orderAdd(orderId) {
	var orderWay = $('#orderWay').val(), productMode = $('#productMode').val(), productName = (productMode == '1') ? '中国书法专业版'
			: '中国书法普及版', isConsistance = $('#isConstitance').val(), orderStartTime = $(
			'#orderStartTime').val(), orderEndTime = $('#orderEndTime').val(), amountEachDate = $(
			'#amountEachDate').val();

	var orderArr = new Array();
	var orderTable = $('#orderListTable');
	var lastRow = orderTable.find('tr').last();

	var startTime = '';
	var endTime = $('#orderEndTime').val();
	var noCositantTime = $('#monthSelected').val();
	// 不连续月 需要选择
	var showStr = '';
	if (isConsistance == '2') {

		if (noCositantTime.length > 16) {
			showStr = noCositantTime.substring(0, 14) + "...";
		} else {
			showStr = noCositantTime;
		}

		startTime = noCositantTime;

	} else {
		startTime = $('#orderStartTime').val()
		showStr = startTime + "至" + endTime;
	}
	var orderid = orderId;
	var rowToAdd = "" + "<tr > " + "<td width='20%'>"
			+ orderid
			+ "</td>"
			+ "<td width='20%'>"
			+ productName
			+ "</td>"
			+ "<td width='20%' onmouseover='showOrderMonthDetail(this)'; onmouseout='hiddenMonthDetail(this);'>"
			+ showStr
			+ "</td>"
			+ "<td width='20%'>"
			+ amountEachDate
			+ "</td>"
			+ "<td style='display:none;' >"
			+ orderWay
			+ "</td>"
			+ "<td style='display:none;' >"
			+ productMode
			+ "</td>"
			// + "<td style='display:none;' >"
			// + isNeedTicket
			// + "</td>"
			// + "<td style='display:none;' >"
			// + isSendAnyway
			// + "</td>"
			+ "<td style='display:none;' >"
			+ startTime
			+ "</td>"
			+ "<td style='display:none;' >"
			+ endTime
			+ "</td>"
			+ "<td style='display:none;' >"
			+ isConsistance
			+ "</td>"
			+ "<td style='display:none;' id='"
			+ orderid
			+ "SendListSize'>"
			+ '0'
			+ "</td>"
			+ "<td ' ><a class='edit' href='#MIDDLE' onclick='showOrderDetail(this);' >&nbsp;</a><a class='dusbin' href='javascript:void(0);' onclick='deleteOrderRow(this);'>&nbsp;</a></td>"
			+ "</tr> ";
	orderTable.append(rowToAdd);
	//计算价格
	var orderMoney = calculate(productMode,startTime,endTime,isConsistance,amountEachDate);
	var curMoney = $('#currentOrderTotalMoney').text();
	var newTotalMoney = orderMoney+parseInt(curMoney);
	$('#currentOrderTotalMoney').text(newTotalMoney);
	$('#addOrderInfoArea').hide();
	var element = $('#TOPAREA');
	var offset = element.offset();
	window.parent.scrollTo(offset.left, offset.top);
	resetVal();
}

function resetSendInput() {
	$($('#whereProFrom')[0]).click();
	$('#sendAmountPerMonth').val('');
	$($('.whoSendProd')[0]).click();
	$('#receiveNameId').val('');
	$('#receiveAddrId').val('');
	$('#receivePhoneId').val('');
	$($('.isNeedBookMark')[0]).click();
	$('#postCodeId').val('');
}

function showAddArea() {
	$('#addOrderInfoArea').show();
}
function showAddSendArea() {
	$('#addSendInfoArea').show();
}
function insert_send() {
	$('#importSendList').show();
}
function showErrow(obj, msg) {
//	$("#fail_tips_lable").text(msg);
//	$("#fail_tips_layer").show().centerV();
	 obj.text(msg);
	 $('#addOrderInfo').attr('href','#');
	 obj.fadeOut(3500,function(){
	 obj.text('');
	 obj.show();
	 });

}

//初始化非连续月年标签
function initNoneConsisArea(){
	var currYear = new Date().getFullYear();
	currYear=parseInt(currYear);
	$('#noConsistantArea').empty();
	for(i=currYear-2;i<=currYear+1;i++){
		
			$('#noConsistantArea').append("<div class='all-year all-year-on'> " +
					"<input type='hidden' value='' id='yearHaSelectedId'/>" +
					"<span class='sel-box  yearselect' onclick='year_info(this);'>"+i+"(0/13)</span> <i class='arrow'></i> " +
					"<div class='paper-monthes' >" +
					"<input type='hidden' id='monthSelected' value=''/>" +
					"<span><input type='checkbox' class='check-style' name='"+i+"' onclick='month_info(this)' monthvalue='01'/><label>1月刊</label></span> " +
					"<span><input type='checkbox' class='check-style' name='"+i+"' onclick='month_info(this)' monthvalue='02'/><label>2月刊</label></span> " +
					"<span><input type='checkbox' class='check-style' name='"+i+"' onclick='month_info(this)' monthvalue='03'/><label>3月刊</label></span> " +
					"<span><input type='checkbox' class='check-style' name='"+i+"' onclick='month_info(this)' monthvalue='04'/><label>4月刊</label></span> " +
					"<span><input type='checkbox' class='check-style' name='"+i+"' onclick='month_info(this)' monthvalue='05'/><label>5月刊</label></span> " +
					"<span><input type='checkbox' class='check-style' name='"+i+"' onclick='month_info(this)' monthvalue='06'/><label>6月刊</label></span> " +
					"<span><input type='checkbox' class='check-style' name='"+i+"' onclick='month_info(this)' monthvalue='07'/><label>7月刊</label></span> " +
					"<span><input type='checkbox' class='check-style' name='"+i+"' onclick='month_info(this)' monthvalue='08'/><label>8月刊</label></span> " +
					"<span><input type='checkbox' class='check-style' name='"+i+"' onclick='month_info(this)' monthvalue='09'/><label>9月刊</label></span> " +
					"<span><input type='checkbox' class='check-style' name='"+i+"' onclick='month_info(this)' monthvalue='10'/><label>10月刊</label></span> " +
					"<span><input type='checkbox' class='check-style' name='"+i+"' onclick='month_info(this)' monthvalue='11'/><label>11月刊</label></span> " +
					"<span><input type='checkbox' class='check-style' name='"+i+"' onclick='month_info(this)' monthvalue='12'/><label>12月刊</label></span> " +
					"<span><input type='checkbox' class='check-style' name='"+i+"' onclick='month_info(this)' monthvalue='13'/><label>合刊</label></span> " +
					"</div>" +
					"</div> ");
		
		
	}
	$($('.yearselect')[2]).click();//选中当前年
}

function importWhenNoError(){
	var infosList = importSendData.infos;

	var rows = $('#orderHasSelectTbl tr');
	var preColArr = $(rows[0]).find('td');// 第一行是表头
	// 根据订单号获取配送Tbl
	var orderId = $(preColArr[0]).text();
	var sendListTb = $("#" + orderId);
	for ( var i = 0; i < infosList.length; i++) {

		// "sendAdd", "sendNumber", "getProWay",
		// "sendAddress", "getCustomer", "getTel",
		// "postcode", "labelInfo" labelContent remark
		var eachRowData = infosList[i];
		sendListTb
				.append("<tr>"
						+ "<td style='display:none;'>"
						+ eachRowData.sendSeq
						+ "</td>"
						+ "<td width='20%'>"
						+ eachRowData.sendAddress
						+ "</td>"
						+ "<td width='20%'>"
						+ eachRowData.getCustomer
						+ "</td>"
						+ "<td width='20%'>"
						+ eachRowData.getTel
						+ "</td>"
						+ "<td width='20%'>"
						+ eachRowData.sendNumber
						+ "</td>"
						+ "<td style='display:none;'>"
						+ eachRowData.getProWay
						+ "</td>"
						+ "<td style='display:none;'>"
						+ eachRowData.labelInfo
						+ "</td>"
						+ "<td style='display:none;'>"
						+ eachRowData.postcode
						+ "</td>"
						+ "<td style='display:none;'>"
						+ eachRowData.sendAdd
						+ "</td>"
						+ "<td style='display:none;'>"
						+ eachRowData.labelContent
						+ "</td>"
						+ "<td style='display:none;'>"
						+ eachRowData.textInfoSend
						+ "</td>"
						+ "<td>"
						+ "<a href='javascript:void(0);' class='edit'></a>"
						+ "<a href='javascript:void(0);' class='dusbin'  onclick='deleteSendRow(this);'></a>"
						+ "</td>" + "</tr>");
		var preBindSendListAmt = $(
				'#' + orderId + 'SendListSize')
				.text();
		$('#' + orderId + 'SendListSize').text(
				parseInt(preBindSendListAmt) + 1);
	}
	importSendData={};
}

function calculate(type,start,end,flag,amount){
	var totalMoney=0;
	var price=(type=='1'?50:38);
	
	if(flag=='1'){//连续月
		var totalNum = (parseInt(end.substring(0,4))-parseInt(start.substring(0,4)))*12+parseInt(end.substring(5))-parseInt(start.substring(5))+1;
		totalMoney=totalNum*price;
	}else{
		var dataArr = start.split(',');
		totalMoney = price*dataArr.length;
	}
	var discount = $('#discount').val();
	if(discount!=''){
		discount = parseInt(discount)
		if(discount<10){
			discount = discount/10;
		}else{
			discount = discount/100;
		}
		
		return totalMoney*amount*discount;
	}else{
		return totalMoney*amount;
	}
	
}
