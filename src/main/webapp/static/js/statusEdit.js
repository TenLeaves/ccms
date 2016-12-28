// 切换配送单状态
function changeStatus(status){
    if(status == 1){
       $("#statusInfo").css("display","table-row");
    }else{
       $("#statusInfo").css("display","none");
    }
}
$(document).ready(function() {
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
          idElement.show();
          var offset = element.offset();
          window.parent.scrollTo(offset.left, offset.top);
        }
 
        return false;
      });
    }
  });
});
var statusPram = {}; // 查询参数
// 查询
function selStatus(){
	statusPram = {};
    var phone = $("#phone").val();
    var reg=/^[0-9]+$/;
    if( !("" == phone ||reg.test(phone))){
        $("#succ_tips_lable").text("您输入的联系电话必须为数字");
        $("#succ_tips_layer").show().centerV();
        $("#phone").focus();
        return false;
    }
    
    statusPram.name = $("#name").val() ;
    statusPram.phone = $("#phone").val();
    statusPram.address = $("#address").val();
    statusPram.startTime=$("#startTime").val();
    statusPram.endTime=$("#endTime").val();
    for(var i=2;i<5;i++){
    	if($("#postType"+i).attr("class").indexOf("sel-box-on")>=0){
    		statusPram.type = i+"";
    	}
    } 
    if($("#export1").attr("class").indexOf("sel-box-on")>=0){
    	statusPram.exportInfo = "1";
    	 for(var i=1;i<3;i++){
    	    	if($("#status"+i).attr("class").indexOf("sel-box-on")>=0){
    	    		statusPram.status = i+"";
    	    	}
    	 } 
    }else{
    	statusPram.exportInfo = "2";
    }

    jQuery.startLoading();
    
    $.ajax({
        url: "/ccms1/selAllStatus",
        data: {"statusPram": $.toJSON(statusPram)},
        type: "POST",
        cache: false,
        success: function (result) {
            
            jQuery.endLoading();
            
            $('#statusList').empty().html(result);
        }
    });
}

// 暂停，恢复,退货
function statusConfirm(curObject){
	if($("#export1").attr("class").indexOf("sel-box-on")>=0){
		var distribut={};
		var tdObjects = $(curObject).closest("tr").find("td");
		distribut["distributId"]= $(tdObjects[0]).text();
		distribut["orderId"] = $(tdObjects[1]).text();
		var status = $(tdObjects[2]).text();
		distribut["status"] = status;
		var date = $(tdObjects[3]).text();
		distribut["date"] = date;
		distribut["exportInfo"] = statusPram.exportInfo;
		$.ajax({
			url: "/ccms1/updateStatus",
			data:  {"distribut": $.toJSON(distribut)},
			type: "POST",
			dataType: "json",
        	cache: false,
        	success: function (data) {
            
        		if (data.status == "success") {
        			if(status == "1"){
                        $("#succ_tips_lable").text("暂停成功!");
                        $("#succ_tips_layer").show().centerV();
        			}else{
                        $("#succ_tips_lable").text("恢复成功!");
                        $("#succ_tips_layer").show().centerV();
        			}
               
        			// 退货成功后，重新查询页面
        			selStatus();
               
        		}else{
                    $("#succ_tips_lable").text("状态变更失败，请重新变更");
                    $("#succ_tips_layer").show().centerV();
        		}
        	}
		});
	}else{
		returnDistri('returnReason','bgOver', curObject);
	}
} 

//弹出退货原因
function returnDistri(showid, bgid, curObject){
    
    var tdObjects = $(curObject).closest("tr").find("td");
    $("#ret_distributId").val($(tdObjects[0]).text());
    $("#ret_orderId").val($(tdObjects[1]).text());
    $("#ret_status").val($(tdObjects[2]).text());
    $("#ret_reason").val('');
    $("#ret_date").val($(tdObjects[3]).text());
    showDiv(showid, bgid);

}

// 退货申请
function updateReturn(){
    
    var distribut={};
    distribut["distributId"]= $("#ret_distributId").val();
    distribut["orderId"]=$("#ret_orderId").val();
    distribut["reason"]=$("#ret_reason").val();
    distribut["status"] = $("#ret_status").val();
    distribut["date"] = $("#ret_date").val();
    distribut["exportInfo"] = statusPram.exportInfo;
    distribut["returnFlag"] = '1'; // 退货标示
    
    $.ajax({
        url: "/ccms1/updateStatus",
        data:  {"distribut": $.toJSON(distribut)},
        type: "POST",
        dataType: "json",
        cache: false,
        success: function (data) {
            
            if (data.status == "success") {
               $("#succ_tips_lable").text("退货成功!");
               $("#succ_tips_layer").show().centerV();
               
               // 关闭退货窗口
               closeDiv('returnReason','bgOver');
               
               // 退货成功后，重新查询页面
               selStatus();
               
            }else{
               $("#succ_tips_lable").text("退货失败，请重新退货");
               $("#succ_tips_layer").show().centerV();
               
               // 关闭退货窗口
               closeDiv('returnReason','bgOver');
            }
            
        }
    });
} 

//分页查询
function clickAjax(){
    
    $('#statusList').ajaxSubmit({
        success: function (result) {
            
            $('#statusList').empty().html(result);
        }
    });
    
}