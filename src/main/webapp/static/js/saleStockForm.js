$(function() {
	// 选择专业版 普及版
	$(".prodtype").bind('click', function() {
		$('.prodtype').removeClass('sel-box-on');
		$(this).addClass('sel-box-on');
		var val = $(this).attr('prodtype');
		$('#selectProdType').val(val);
	});
});

function qryFormInfo() {
	var prodType = $('#selectProdType').val();
	var prodDate = $('#applyDate').val();
	if (prodDate == undefined || prodDate == '') {// 不选择年份默认查询当前年份
		prodDate = new Date().getFullYear();
	}

	jQuery.ajax({
		type : 'POST',
		url : '/ccms1/searchFromInfo',
		data : {
			'prodType' : prodType,
			'prodDate' : prodDate
		},
		success : function(data) {
			$('#pagesList').empty().append(data);
			calculateTotal(prodType);
			$('#pagesList').show();
		}
	});

}

function calculateTotal(prodType) {
	if(prodType=='1'){
		calculateTotalEachMonth();
		calculateTotalEachType();
	}else{
		calculateTotalEachMonth1();
		calculateTotalEachType1();
	}
	
}
// tbl 结构：saleStockInfo_profession.html
function calculateTotalEachMonth() {
	var tbl = $('#saleStockInfoTabl');
	var trs = tbl.find("tr");
	for (i = 3; i < trs.length - 1; i++) {
		var eachRow = $(trs[i]);
		var cols = eachRow.find('td');
		var totalEachMonth = parseInt($(cols[1]).text())
				+ parseInt($(cols[2]).text()) + parseInt($(cols[3]).text())
				+ parseInt($(cols[4]).text()) + parseInt($(cols[5]).text())
				+ parseInt($(cols[6]).text()) + parseInt($(cols[7]).text());
		$(cols[8]).text(totalEachMonth);
	}
}
function calculateTotalEachType(){
	var faxingjuCells=$('.faxingju');
	var custCells=$('.dakehu');
	var pifasCells=$('.pifashang');
	var faxingzCells=$('.faxingzhan');
	var saleCells=$('.lignshou');
	var sendCells=$('.zengkan');
	var showCells=$('.yangkan');
	var stockCells=$('.kucun');
	
	$('#faxingjuTotal').text(sum(faxingjuCells));
	$('#dakehuTotal').text(sum(custCells));
	$('#pifashangTotal').text(sum(pifasCells));
	$('#faxingzhanTotal').text(sum(faxingzCells));
	$('#lignshouTotal').text(sum(saleCells));
	$('#zengkanTotal').text(sum(sendCells));
	
	var allOrderAmt = sum(faxingjuCells)+sum(custCells)+sum(pifasCells)
	                 +sum(faxingzCells)+sum(saleCells)+sum(sendCells)
	$('#allInAll').text(allOrderAmt);
	$('#stockAll').text(sum(stockCells))
}

// tbl 结构：saleStockInfo_ordinary.html
function calculateTotalEachMonth1(){
	var tbl = $('#saleStockInfoTabl');
	var trs = tbl.find("tr");
	for(i = 2; i < trs.length - 1; i++){
		var eachRow = $(trs[i]);
		var cols = eachRow.find('td');
		var totalEachMonth=parseInt($(cols[1]).text())
		+ parseInt($(cols[2]).text()) + parseInt($(cols[3]).text())
		+ parseInt($(cols[4]).text());
		$(cols[5]).text(totalEachMonth);
	}
}

function calculateTotalEachType1(){
	var faxingjuCells=$('.fxj_ordinary');
	var sendCells=$('.send_ordinary');
	var zbfxCells=$('.zbfx_ordinary');
	var stockCells=$('.stock_ordinary');
	
	$('#fxj_ordinary').text(sum(faxingjuCells));
	$('#send_ordinary').text(sum(sendCells));
	$('#zbfx_ordinary').text(sum(zbfxCells));
	
	$('#stock_ordinary_all').text(sum(stockCells));
	var allTypeOrderAmt = sum(faxingjuCells)+sum(sendCells)+sum(zbfxCells);
	$('#allInall_ordinary').text(allTypeOrderAmt);
	
	
}




function sum(objArr){
	var total=0;
	for(i=0;i<objArr.length;i++){
		total=total+parseInt($(objArr[i]).text());
	}
	return total;
}
