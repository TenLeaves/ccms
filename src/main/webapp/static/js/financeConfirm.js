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
var pay = "1"; // 是否付款 1 未付款

// 切换是否付款
function changePay(type){
    if(type == 1){
        pay = "1";
        
        $("#unPay").addClass("current").siblings(".current").removeClass("current");
        
        selFinance(pay);
    }else if(type == 2){
        pay = "2";
        
        $("#hasPay").addClass("current").siblings(".current").removeClass("current");
        
        selFinance(pay);
    }
}

var financePram = {}; // 查询参数

// 查询
function selFinance(pay){
	if(pay==undefined){
		pay='1';
	}
    
//    var phone = $("#phone").val();
//    var reg=/^[0-9]+$/;
//    if( !("" == phone ||reg.test(phone))){
//        alert("您输入的联系电话必须为数字");
//        $("#phone").focus();
//        return false;
//    }
    
    // 判断日期格式是否正确
    var beginTime = $("#beginTime").val();
    var endTime = $("#endTime").val();
    
    var reg=/^\d{4}-\d{2}-\d{2}$/;
    if(!(beginTime == "" || reg.test(beginTime))){
        alert("出库时间输入格式不正确，格式为：2014-12-12");
        return false;
    }
    
    if(!(endTime == "" || reg.test(endTime))){
        alert("出库时间输入格式不正确，格式为：2014-12-12");
        return false;
    }
    
    if(!(beginTime == "" || endTime == "" || beginTime <= endTime )){
        alert("下单初始时间不能大于截止时间");
        return false;
    }
    financePram.SUBSCRIBE_ID = $("#subscribeId").val();
    financePram.CUSTOMER_NAME = $("#name").val() ;
    financePram.CUSTOMER_PHONE = $("#phone").val();
    financePram.COMPANY = $("#company").val();
    financePram.OrderStartTime = $("#beginTime").val();
    financePram.OrderEndTime = $("#endTime").val();  
    financePram.PAY_FLAG = pay;
    financePram.orderState = pay;
    jQuery.startLoading();
    
    $.ajax({
        url: "/ccms1/selAllFinance",
        data: {"financePram": $.toJSON(financePram)},
        type: "POST",
        cache: false,
        success: function (result) {
            
            jQuery.endLoading();
            
            $('#financeList').empty().html(result);
            $('#resultArea').show();
        }
    });
}

// 确认收款
var orderId='';
function financeConfirm(curObject){
    
    var subscribe={};
    var tdObjects = $(curObject).closest("tr").find("td");
    subscribe["SUBSCRIBE_STATE"]= $(tdObjects[0]).text();
    subscribe["SUBSCRIBE_ID"]= $(tdObjects[1]).text();
    orderId = $(tdObjects[1]).text();
    $('#no-pass-layer').show();
    
    $.ajax({
        url: "/ccms1/financeConfirm",
        data:  {"subscribe": $.toJSON(subscribe)},
        type: "POST",
        dataType: "json",
        cache: false,
        success: function (data) {
            
        }
    });
} 

// 弹出详情
function showDetail(curObject){
    
    var tdObjects = $(curObject).closest("tr").find("td");
    var subscribeId = $(tdObjects[1]).text();
    window.showModalDialog("/ccms1/toOrderEdit?subscribeId="+subscribeId+"&key=1",window,"dialogWidth=1000px;dialogHeight=500px");
}


//分页查询
function clickAjax(){
    
    $('#selFinance').ajaxSubmit({
        success: function (result) {
            
            $('#selFinance').empty().html(result);
        }
    });
    
}

function selectOrderSta(obj){
	$this = $(obj);
	var index = $this.get(0).selectedIndex;
	changePay(index);
}


function addRemarkToOrder(){
	var remark = $('#orderConfirmRemarkInput').val();
	var subOrderId = orderId;
	$.ajax({
        url: "/ccms1/addReMarkToOrderWhenConfirm",
        data:  {"SUBSCRIBE_ID": subOrderId,
        	"REMARK":remark},
        type: "POST",
        dataType: "json",
        cache: false,
        success: function (data) {
        	$('#no-pass-layer').hide();
        	$('#orderConfirmRemarkInput').val('');
        	selFinance('1');
        	$("#succ_tips_lable").text("确认收款成功");
            $("#succ_tips_layer").show().centerV();
        }
    });
}