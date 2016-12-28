$(function(){
	// 切换tab签
	$("#tablist li").click(function(){
		var $this=$(this),index=$this.index();
		$this.addClass("on").siblings().removeClass("on");
		$(".tabs-cont",".sec-box").eq(index).show().siblings().hide();
	});
	// 修改订购信息
	$(".edit-btn").live("click",function(){
		$("#list-edit").show();
	});
	//产品类型选择 普通 专业
	$('.prodtype').bind('click',function(){
		$('.prodtype').removeClass('sel-box-on');
		$(this).addClass('sel-box-on');
		$('#userselectProdType').val($(this).attr('prodtype'));
	});
	//选择从那个仓库调拨
	$('.stockAddrCss').bind('click',function(){
		$('.stockAddrCss').removeClass('sel-box-on');
		$(this).addClass('sel-box-on');
		var val=$(this).attr('stockAddr');
		$('#userselectStockFrom').val(val);
		if(val=='1'){
			$('#zzswantMntlabel').hide();
			$('#yjwantMntlabel').show();
			$('#yscwantMntlabel').show();
		}else if(val=="2"){
			$('#zzswantMntlabel').show();
			$('#yjwantMntlabel').hide();
			$('#yscwantMntlabel').show();
		}else{
			$('#zzswantMntlabel').show();
			$('#yjwantMntlabel').show();
			$('#yscwantMntlabel').hide();
		}
	});
})

function selectCertainPro(obj){
	var _this = $(obj);
	var spans =_this.find('span');
	
	var selectProd = $(spans[1]).text()
	var selectProId = $(spans[2]).text();
	$('#selectedProdId').val(selectProd);
	$('#selectedprintId').val(selectProId);
	$('.productList').removeClass('sel-box-on');
	_this.addClass('sel-box-on');
	
	jQuery.ajax({
	    type : 'POST',
	    url : '/ccms1/qryCertainProInfo',
	    data : {
	      'prodCode':selectProd,
	      'printId':selectProId
	    },
	    success : function(data) {
	    	var tblRows = $('#productInfoTbl').find('tr');
	    	$(tblRows[1]).remove();
	    	$('#productInfoTbl').append(data);
	    	var applyAmt = $('#applyTotalMnt').text();
	    	$('#totalAmoutToPrint').val(applyAmt.trim());
	    }
	  });
	
}

function confirmPrintIn(){
	var applyAmount=$('#applyTotalMnt').text();
	var confirmInfo={};
	confirmInfo.needTotalAmt=$('#totalAmoutToPrint').val();
	confirmInfo.zzsneedAmt=$('#zzsAmountToPrint').val();
	confirmInfo.officeneedAmt=$('#ofisAmountToPrint').val();
	confirmInfo.factryneedAmt=$('#printFactToPrint').val();
	confirmInfo.prodId=$('#selectedProdId').val();
	confirmInfo.printId=$('#selectedprintId').val();
	
	var regtex=/^\d+$/;
	
	if(!regtex.test(confirmInfo.needTotalAmt)){
		$("#fail_tips_lable").text('请输入合法的印刷总数');
        $("#fail_tips_layer").show().centerV();
		return;
	}
	
	if(confirmInfo.needTotalAmt<applyAmount){
		$("#fail_tips_lable").text('印刷数量不能小于申请数量');
        $("#fail_tips_layer").show().centerV();
		return;
	}
	if(!regtex.test(confirmInfo.zzsneedAmt)){
		$("#fail_tips_lable").text('请输入合法的杂志社分配总数');
        $("#fail_tips_layer").show().centerV();
		return;
	}
	if(!regtex.test(confirmInfo.officeneedAmt)){
		$("#fail_tips_lable").text('请输入合法的邮局分配总数');
        $("#fail_tips_layer").show().centerV();
		return;
	}
	if(!regtex.test(confirmInfo.factryneedAmt)){
		$("#fail_tips_lable").text('请输入合法的印刷厂分配总数');
        $("#fail_tips_layer").show().centerV();
		return;
	}
	var total = parseInt(confirmInfo.needTotalAmt),
	    num1 =parseInt(confirmInfo.zzsneedAmt),
	    num2 =parseInt(confirmInfo.officeneedAmt),
	    num3=parseInt(confirmInfo.factryneedAmt);
	if(total!=(num1+num2+num3)){
		$("#fail_tips_lable").text('印刷总数量和分配数不相等');
        $("#fail_tips_layer").show().centerV();
		return;
	}
	jQuery.ajax({
	    type : 'POST',
	    url : '/ccms1/confirmPrintIn',
	    data : {
	      'amount':$.toJSON(confirmInfo)
	    },
	    success : function(data) {
	    	
	    	$("#succ_tips_lable").text(data);
	          $("#succ_tips_layer").show().centerV();
	    	window.location.href = "/ccms1/toConfirmPrint";
	    }
	  });
	
}

function searchStock(){
	var period = $('#prodPirod').val(),prodType=$('#userselectProdType').val();
	if(period==''){
		$("#fail_tips_lable").text('请选择期刊');
        $("#fail_tips_layer").show().centerV();
		return;
	}
	if(prodType==''){
		$("#fail_tips_lable").text('请选择产品类型');
        $("#fail_tips_layer").show().centerV();
		return;
	}
	
	jQuery.ajax({
	    type : 'POST',
	    url : '/ccms1/queryEachStockByProd',
	    dataType : 'json',
	    data : {
	      'period':period,
	      'prodType':prodType
	    },
	    success : function(data) {
	    	var stockInfo = data.stockInfo;
	    	$('#zzsAmtId').text(stockInfo.zzsAmt);
	    	$('#yjAmtId').text(stockInfo.yjAmt);
	    	$('#yscAmtId').text(stockInfo.yscAmt);
	    	$('#printCode').val(stockInfo.printCode);
	    	
	    	//让仓库标题变成可选择的 sel-box
	        $('.stockAddrCss').addClass('sel-box');
	    }
	  });
}

function distribute(){
	var stockFrom = $('#userselectStockFrom').val(),
	remainAmt =0,
	printCode=$('#printCode').val(),
	zzsdistributeAmt=$('#zzswantMnt').val(),
	yjdistributeAmt=$('#yjwantMnt').val(),
	yscdistributeAmt=$('#yscwantMnt').val();
	
	var period = $('#prodPirod').val(),prodType=$('#userselectProdType').val();
	if(period==''){
		$("#fail_tips_lable").text('请选择期刊');
        $("#fail_tips_layer").show().centerV();
		return;
	}
	if(prodType==''){
		$("#fail_tips_lable").text('请选择产品类型');
        $("#fail_tips_layer").show().centerV();
		return;
	}
	var regetex=/^[1-9]\d+$/;
	if(stockFrom==''){
		$("#fail_tips_lable").text('请先选择被调拨仓库');
        $("#fail_tips_layer").show().centerV();
		return;
	}
	if(zzsdistributeAmt!='0'){
		if(!regetex.test(zzsdistributeAmt)){
			$("#fail_tips_lable").text('分配给杂志社的数量只能是数字');
	        $("#fail_tips_layer").show().centerV();
			return;
		}
	}
	if(yjdistributeAmt!='0'){
		if(!regetex.test(yjdistributeAmt)){
			$("#fail_tips_lable").text('分配给邮局的数量只能是数字');
	        $("#fail_tips_layer").show().centerV();
			return;
		}
	}
	if(yscdistributeAmt!='0'){
		if(!regetex.test(yscdistributeAmt)){
			$("#fail_tips_lable").text('分配给印刷厂的数量只能是数字');
	        $("#fail_tips_layer").show().centerV();
			return;
		}
	}
	
	
	if(stockFrom=='1'){
		remainAmt = $('#zzsAmtId').text();
	}else if(stockFrom=='2'){
		remainAmt = $('#yjAmtId').text();
	}else{
		remainAmt = $('#yscAmtId').text();
	}
	
	var wantTotalAmt = parseInt(zzsdistributeAmt)+parseInt(yjdistributeAmt)+parseInt(yscdistributeAmt);
	if(wantTotalAmt==0){
		$("#fail_tips_lable").text('请输入分配数量');
        $("#fail_tips_layer").show().centerV();
		return ;
	}
	if(wantTotalAmt>remainAmt){
		$("#fail_tips_lable").text('剩余库存不足，请重新分配');
        $("#fail_tips_layer").show().centerV();
		return ;
	}
	
	jQuery.ajax({
	    type : 'POST',
	    url : '/ccms1/distributeStock',
	    data : {
	      'zzsAmt':zzsdistributeAmt,
	      'yjAmt':yjdistributeAmt,
	      'yscAmt':yscdistributeAmt,
	      'stockFrom':stockFrom,
	      'printCode':printCode
	    },
	    success : function(data) {
	    	$("#succ_tips_lable").text(data);
	          $("#succ_tips_layer").show().centerV();
	    	searchStock();
	    	$('.stockAddrCss').removeClass('sel-box');
	    	$('#zzswantMnt').val('0');
	    	$('#yjwantMnt').val('0');
	    	$('#yscwantMnt').val('0');
	    }
	  });
	
}