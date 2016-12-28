$(function(){
	//选择专业版 普及版
	$(".prodtype").bind('click',function(){
		$('.prodtype').removeClass('sel-box-on');
		$(this).addClass('sel-box-on');
		var val = $(this).attr('prodtype');
		$('#selectProdType').val(val);
	});
	
	
	
});

function selPrintApply(){
	var prodType=$('#selectProdType').val();
	var prodDate=$('#applyDate').val();
	if(prodDate==undefined||prodDate==''){
		$("#succ_tips_lable").text("请先选择期刊");
        $("#succ_tips_layer").show().centerV();
		return;
	}
	jQuery.ajax({
	    type : 'POST',
	    url : '/ccms1/qryPrintApply',
	    data : {
	      'prodType':prodType,
	      'prodDate':prodDate
	    },
	    success : function(data) {
	    	$('#stockList').empty().append(data);
	    }
	  });
}

function editPrintApply(obj){
	_this = $(obj);
	var tds = _this.parent().find('td');
	var printCode=$(tds[0]).text();
	if(_this.text()=='--'){
		$("#succ_tips_lable").text("只能修改当月未审批或者审批不通过的单子");
        $("#succ_tips_layer").show().centerV();
		return;
	}else{
		window.location.href="/ccms1/printTableEdit?printCode="+printCode;
	}
}