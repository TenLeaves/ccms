$(function(){
	var bjfxj = $("#paperQry").text(),
	    tsgs = $("#bookQry").text(),
	    pfs = $("#machentQry").text(),
	    dkh = $("#custQry").text(),
	    ls = $("#saleNewQry").text(),
	    zkyk = $("#giveNewQry").text(),
	    yl = $("#saveQry").text(),
	    month = $("#month").text(),
	    year = $("#year").text(),
	    type = $("#prodType").text();
	
	$('#paper').val(bjfxj);
	$('#book').val(tsgs);
	$('#machent').val(pfs);
	$('#cust').val(dkh);
	$('#saleNew').val(ls);
	$('#giveNew').val(zkyk);
	$('#save').val(yl);
	
	var prodTypeDesc = type==1?"专业版":"普及版";
	var prodDesc=year+"年"+prodTypeDesc+"第"+month+"期";
	$('#type1').text(prodDesc);
});

function totalInfo(){
	$('input').attr('style','');
	var regtex=/^[0-9]\d*$/;
	
	var bjfxj = $("#paper").val(),
    tsgs = $("#book").val(),
    pfs = $("#machent").val(),
    dkh = $("#cust").val(),
    ls = $("#saleNew").val(),
    zkyk = $("#giveNew").val(),
    yl = $("#save").val(),
    month = $("#month").text(),
    year = $("#year").text(),
    type = $("#prodType").text();
	
	if(!regtex.test(bjfxj)){
		$("#paper").attr("style",'border-color:red;');
		$("#succ_tips_lable").text("请输入合法的数量");
        $("#succ_tips_layer").show().centerV();
		return;
	}
	if(!regtex.test(tsgs)){
		$("#book").attr("style",'border-color:red;');
		$("#succ_tips_lable").text("请输入合法的数量");
        $("#succ_tips_layer").show().centerV();
		return;
	}
	if(!regtex.test(pfs)){
		$("#machent").attr("style",'border-color:red;');
		$("#succ_tips_lable").text("请输入合法的数量");
		$("#succ_tips_layer").show().centerV();
		return;
	}
	if(!regtex.test(dkh)){
		$("#cust").attr("style",'border-color:red;');
		$("#succ_tips_lable").text("请输入合法的数量");
		$("#succ_tips_layer").show().centerV();
		return;
	}
	if(!regtex.test(ls)){
		$("#saleNew").attr("style",'border-color:red;');
		$("#succ_tips_lable").text("请输入合法的数量");
		$("#succ_tips_layer").show().centerV();
		return;
	}
	if(!regtex.test(zkyk)){
		$("#giveNew").attr("style",'border-color:red;');
		$("#succ_tips_lable").text("请输入合法的数量");
		$("#succ_tips_layer").show().centerV();
		return;
	}
	if(!regtex.test(yl)){
		$("#save").attr("style",'border-color:red;');
		$("#succ_tips_lable").text("请输入合法的数量");
		$("#succ_tips_layer").show().centerV();
		return;
	}
	
	var totalAmt = parseInt(bjfxj)+parseInt(tsgs)+parseInt(pfs)+
	                parseInt(dkh)+parseInt(ls)+parseInt(zkyk)+
	                parseInt(yl);
	$('#total0').text(totalAmt);
}

function edit(){
	var printTbl={};
	printTbl.PRINT_CODE=$('#printCode').text();
	printTbl.CUSTOMER_COUNT=$("#cust").val();
	printTbl.AGENT_COUNT=$("#machent").val();
	printTbl.RETAIL_COUNT=$("#saleNew").val();
	printTbl.GIVE_COUNT=$("#giveNew").val();
	printTbl.BJPAPER_COUNT=$("#paper").val();
	printTbl.RESERVE_COUNT=$("#save").val();
	printTbl.INTERBOOK_COUNT=$("#book").val();
	printTbl.TOTAL_COUNT=$("#total0").text();
	
	jQuery.ajax({
	      type : 'POST',
	      url : '/ccms1/updatePrintTbl',
	      data : {
	        'printTbl' : $.toJSON(printTbl),
	      },
	      success : function(data)// 当请求成功时触发函数
	      {
	        $("#succ_tips_lable").text("更新成功");
	        $("#succ_tips_layer").show().centerV();
	      }
	    });
	
}