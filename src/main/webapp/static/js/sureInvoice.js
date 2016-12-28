$(function(){

	//maodian
	$("a").each(function (){
	    var link = $(this);
	    var href = link.attr("href");
	    if(href && href[0] == "#")
	    {
	      var name = href.substring(1);
	      $(this).click(function() {
	        var nameElement = $("[name='"+name+"']");
	        var idElement = $("#"+name);
	        var element = null;
	        if(nameElement.length > 0) {
	          element = nameElement;
	        } else if(idElement.length > 0) {
	          element = idElement;
	        }
	 
	        if(element) {
	          var offset = element.offset();
	          window.parent.scrollTo(offset.left, offset.top);
	        }
	 
	        return false;
	      });
	    }
	  });
});

//查询发票
function invoiceSearch(){
	var condition = {};
	
	condition.invoiceTitle=$('#invoiceTitle').val();
	condition.invoiceId=$('#invoiceId').val();
	condition.orderId=$('#orderId').val();
	condition.exportInfo=$('#export').val();
	condition.startTime=$('#startTime').val();
	condition.endTime=$('#endTime').val();
	jQuery.startLoading();
	jQuery.ajax({
	    type : 'POST',
	    url : '/ccms1/sureInvoiceInfoPage',
	    data : {
	      'condition':$.toJSON(condition)
	    },
	    success : function(data) {
	    	 jQuery.endLoading();
	    	$('#pagesList').empty().append(data);
	    	$('#btn0').show();
	    }
	  });
}
var obj;
function sureInvoice(objInfo){
	$("#returnReason").show().centerV();
	$("#invoiceNo").val("");
	obj=objInfo;
}
function updateReturn(){
	var invoiceNo=$("#invoiceNo").val();
	if(invoiceNo == ""){
        $("#succ_tips_lable").text("请填写发票号");
        $("#succ_tips_layer").show().centerV();
        return;
	}
	var tr=$(obj).closest("tr").find("td");
	var orderId=$(tr[0]).html();
	var invoiceId=$(tr[1]).html();
	jQuery.ajax({
	    type : 'POST',
	    url : '/ccms1/updateSureInvoice?orderId='+orderId+"&invoiceId="+invoiceId+"&invoiceNo="+invoiceNo,
	    success : function(data) {
	    	$('#pagesList').empty().append(data);
	    }
	  });
}
function changeExport(){
	var flag=$("#export").val();
	if(flag=='0'){
		$("#exportTime").css("display","none");
	}else{
		$("#exportTime").css("display","table-row");
		$("#startTime").val("");
		$("#endTime").val("");
	}
}
