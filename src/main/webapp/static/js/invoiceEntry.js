$(function(){
	// 切换tab签
	$("#tablist li").click(function(){
		var $this=$(this),index=$this.index();
		$this.addClass("on").siblings().removeClass("on");
		$(".tabs-cont",".sec-box").eq(index).show().siblings().hide();
	});
	// 修改订购信息
	$(".add-btn,.detail-btn").live("click",function(){
		$("#list-edit").show();
	});
	
	//选择发票状态
	$('.invoiceState').bind('click',function(){
		$('.invoiceState').removeClass('sel-box-on');
		$(this).addClass('sel-box-on');
		$('#usrSelectOrderInvoiceSta').val($(this).attr('invoicestate'));
	});
	
	$('.tab-lis li').bind('click',function(){
		$('.tab-lis li').removeClass('current');
		$(this).addClass('current');
		$('#selectOrderState').val($(this).attr('orderStsate'));
		
		var condition={};
		condition.orderId= $('#orderIdInput').val();
		condition.orderStart=$('#orderTimeStartInput').val();
		condition.orderEnd=$('#orderTimeEndInput').val();
		condition.contactName=$('#orderContactInput').val();
		condition.contactAddr=$('#orderAddrInput').val();
		condition.orderState=$('#selectOrderState').val();
		
		jQuery.ajax({
		    type : 'POST',
		    url : '/ccms1/searchOdersInvoice',
		    data : {
		      'condition':$.toJSON(condition)
		    },
		    success : function(data) {
		    	$('#invoiceInfoTable').empty().append(data);
		    }
		  });
	});
	
	$('#searchInvoiceBtn').bind('click',function(){
		var condition={};
		condition.orderId= $('#orderIdInput').val();
		condition.orderStart=$('#orderTimeStartInput').val();
		condition.orderEnd=$('#orderTimeEndInput').val();
		condition.contactName=$('#orderContactInput').val();
		condition.contactAddr=$('#orderAddrInput').val();
		condition.orderState=$('#selectOrderState').val();
		
		jQuery.startLoading();
		jQuery.ajax({
		    type : 'POST',
		    url : '/ccms1/searchOdersInvoice',
		    data : {
		      'condition':$.toJSON(condition)
		    },
		    success : function(data) {
		    	jQuery.endLoading();
		    	$('#invoiceInfoTable').empty().append(data);
		    	$('#resultArea').show();
		    }
		  });
	});
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

function showDetailFormInvoice(obj,tag){
	var dataRow=$(obj).closest('tr');
	var colArr=dataRow.find('td');
	var subscribleId=$(colArr[0]).text();
	window.showModalDialog("/ccms1/toOrderEdit?subscribeId="+subscribleId+"&key="+tag,window,"dialogWidth=1000px;dialogHeight=500px");

}
function selectOrderState(obj){
	$this = $(obj);
	var orderState =  $this.val();
	$('#selectOrderState').val(orderState);
}
//查询发票
function invoiceSearch(){
	var condition = {};
	
	condition.state=$('#usrSelectOrderInvoiceSta').val();
	condition.startTime=$('#invoiceStartTime').val();
	condition.endTime=$('#invoiceEndTime').val();
	
	jQuery.startLoading();
	jQuery.ajax({
	    type : 'POST',
	    url : '/ccms1/searchInvoiceToType',
	    data : {
	      'condition':$.toJSON(condition)
	    },
	    success : function(data) {
	    	jQuery.endLoading();
	    	$('#typedInvoiceInfoTable').empty().append(data);
	    }
	  });
	
}
function addInvoice(obj){
    _this = $(obj);
    var tds = _this.parent().find('td');
    var subscribeId = $(tds[0]).text();
    window.showModalDialog("/ccms1/toOrderEdit?subscribeId="+subscribeId+"&key=2",window,"dialogWidth=1000px;dialogHeight=500px");
}
