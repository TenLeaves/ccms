// 滚动条滚动，让div固定位置
window.onresize=window.onscroll=window.onload=function()
{
    $("#modify").css("top", (document.documentElement.clientHeight-$("#modify").height())/2+document.documentElement.scrollTop+'px');
}

//点击查询之后 保存查询参数，分页时使用
var paymentId = "";
var paymentName = "";
var payBindFlag = "";
var updateTime = "";
var paymentValue = "";
var paymentPhone = "";
var paymentAddress = "";
var buyCount = "";
var postCode = "";
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
});
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
// 校验金额
function checkMoney(moneyId){
    var money = $("#" + moneyId).val();
    var reg=/^(([1-9]\d{0,9})|0)(\.\d{1,2})?$/;
    if(money == "" || !reg.test(money)){
        $("#succ_tips_lable").text("汇款金额输入不正确！");
        $("#succ_tips_layer").show().centerV();
        $("#" + moneyId).focus();
    }
    
}
//确认导入
function sureImport(){
	$("#result_info1").html(0);
	$("#result_info2").html(0);
	$("#result_info3").html(0);
	var tb = document.getElementById('tal_error');
	var rowNum=tb.rows.length;
	for (var i=1;i<rowNum;i++)
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
	var postCompanyInfo;
    for(var i=1;i<3;i++){
    	if($("#postCompanyInfo"+i).attr("class").indexOf("sel-box-on")>=0){
    		postCompanyInfo = i+"";
    	}
    } 
    $('#postCompanyInfo').val(postCompanyInfo);
	jQuery.startLoading();
	$('#import_form').ajaxSubmit({
		dataType:"json",
		success: function (data) {
			jQuery.endLoading();
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
            $("#succ_tips_lable").text("导入数据失败");
            $("#succ_tips_layer").show().centerV();
			return;
		}
		}
    });
}

// 参数校验
function validate(){
    
    if($("#paymentId").val() == ""){
        $("#succ_tips_lable").text("电子回单号不能为空！");
        $("#succ_tips_layer").show().centerV();
        return false;
    }
    
    if($("#paymentName").val() == ""){
        $("#succ_tips_lable").text("付款人姓名不能为空！");
        $("#succ_tips_layer").show().centerV();
        return false;
    }
    
    if($("#paymentTime").val() == ""){
        $("#succ_tips_lable").text("汇款时间不能为空！");
        $("#succ_tips_layer").show().centerV();
        return false;
    }
    
    var money = $("#paymentValue").val();
    var reg=/^(([1-9]\d{0,9})|0)(\.\d{1,2})?$/;
    if(money == "" || !reg.test(money)){
        $("#succ_tips_lable").text("汇款金额输入不正确！");
        $("#succ_tips_layer").show().centerV();
        return false;
    }
    
    if($("#paymentPhone").val() != ""){
    	var reg=/^1[0-9]+$/;
		if(($("#paymentPhone").val().length!=11 || !reg.test($("#paymentPhone").val()))){
			$("#succ_tips_lable").text("汇款人电话必须为1开头的11位数字！");
	        $("#succ_tips_layer").show().centerV();
			return false;
		}
    }
    if($("#postCode").val() != ""){
    	var reg=/^[0-9]{6}$/;
		if(!reg.test($("#postCode").val())){
			$("#succ_tips_lable").text("邮编输入不正确！");
	        $("#succ_tips_layer").show().centerV();
			return false;
		}
    }
    
    return true;
}

// 增加客户提交前 参数准备
function addPayReady(){
	var paymentFlag;
	var postCompany;
    for(var i=1;i<3;i++){
    	if($("#postCompany"+i).attr("class").indexOf("sel-box-on")>=0){
    		postCompany = i+"";
    	}
    } 
    $('#postCompany').val(postCompany);
    for(var i=1;i<3;i++){
    	if($("#paymentFlag"+i).attr("class").indexOf("sel-box-on")>=0){
    		paymentFlag = i+"";
    	}
    } 
    $('#paymentFlag').val(paymentFlag);
    //参数校验
    if(!validate()){
        return false;
    }
    jQuery.startLoading();
    $('#submitInfo').ajaxSubmit({
        success: function () {
        	jQuery.endLoading();
            $("#succ_tips_lable").text("添加汇款单成功");
            $("#succ_tips_layer").show().centerV();
        	resetInfo();
        	return;
        }
    });
}

// 分页查询回款单信息
function payListAjax(){
    
   /* var paydata = {};
    
    paydata["paymentId"] = paymentId;
    paydata["paymentName"] = paymentName;
    paydata["bindFlag"] = payBindFlag;
    paydata["updateTime"] = updateTime;
    paydata["paymentValue"] = paymentValue;*/
	jQuery.startLoading();
    $('#payList').ajaxSubmit({
        success: function (result) {
        	jQuery.endLoading();
            $('#payList').empty().html(result);
        }
    });
    
}

// “查询”按钮查询所有回款单
function selAllPay(){

    for(var i=1;i<3;i++){
    	if($("#bindFlag"+i).attr("class").indexOf("sel-box-on")>=0){
    		bindFlag = i+"";
    	}
    } 
    $('#bindFlag').val(bindFlag);
    
    jQuery.startLoading();
    
    $('#selPays').ajaxSubmit({
        success: function (result) {
            
            //点击查询之后 保存查询参数，分页时使用
            /*paymentId = $('#sel_paymentId').val();
            paymentName = $('#sel_paymentName').val();
            payBindFlag = bindFlag;
            updateTime = $('#sel_updateTime').val();
            paymentValue = $('#sel_paymentValue').val();*/
            jQuery.endLoading();
            
            $('#payList').empty().html(result);
        }
    });
    
}

// 弹出修改配送单
function showModify(showid, bgid, curObject){
    
    var tdObjects = $(curObject).closest("tr").find("td");
    $("#mod_paymentId").val($(tdObjects[2]).text());
    $("#mod_paymentName").val($(tdObjects[3]).text());
    $("#mod_paymentValue").val($(tdObjects[4]).text());
    $("#mod_remark").val($(tdObjects[1]).text());
    $("#mod_paymentTime").val($(tdObjects[6]).text());
    $("#mod_paymentPhone").val($(tdObjects[8]).text());
    $("#mod_paymentAddress").val($(tdObjects[9]).text());
    $("#mod_buyCount").val($(tdObjects[10]).text());
    $("#mod_postCode").val($(tdObjects[13]).text());
    $("#modify").show().centerV();

}
//删除配送单
function deletePayment(obj){
	var choice = window.confirm("您确定删除这条汇款单？");
    if(!choice){
    	return;
    }
	var tdObjects = $(obj).closest("tr").find("td");
	var paymentId=$(tdObjects[2]).text();
	
	jQuery.ajax({
	      type : 'POST',
	      url : '/ccms1/deletePayment',
	      data : {
	    	  PAYMENT_ID:paymentId
	      },
	      success : function(data)// 当请求成功时触发函数
	      {
	    	  $("#succ_tips_lable").text("该汇款单已删除！");
	          $("#succ_tips_layer").show().centerV();
	    	  selAllPay();//删除陈功再查一遍
	      }
	    });
}

// 修改回款单信息 ajax提交
function updateCust(){
    
    if($("#mod_paymentId").val() == ""){
        $("#succ_tips_lable").text("电子回单号不能为空！");
        $("#succ_tips_layer").show().centerV();
        return false;
    }
    
    if($("#mod_paymentName").val() == ""){
        $("#succ_tips_lable").text("付款人姓名不能为空！");
        $("#succ_tips_layer").show().centerV();
        return false;
    }
    
    var money = $("#mod_paymentValue").val();
    var reg=/^(([1-9]\d{0,9})|0)(\.\d{1,2})?$/;
    if(money == "" || !reg.test(money)){
        $("#succ_tips_lable").text("汇款金额输入不正确！");
        $("#succ_tips_layer").show().centerV();
        return false;
    }
    if($("#mod_paymentPhone").val() != ""){
    	var reg=/^1[0-9]+$/;
		if(($("#mod_paymentPhone").val().length!=11 || !reg.test($("#mod_paymentPhone").val()))){
			$("#succ_tips_lable").text("汇款人电话必须为1开头的11位数字！");
	        $("#succ_tips_layer").show().centerV();
			return false;
		}
    }
    if($("#mod_postCode").val() != ""){
    	var reg=/^[0-9]{6}$/;
		if(!reg.test($("#mod_postCode").val())){
			$("#succ_tips_lable").text("邮编输入不正确！");
	        $("#succ_tips_layer").show().centerV();
			return false;
		}
    }
   
    var payInfo={};
    payInfo["paymentId"]= $("#mod_paymentId").val();
    payInfo["paymentName"]=$("#mod_paymentName").val();
    payInfo["paymentValue"]=$("#mod_paymentValue").val();
    payInfo["remark"]=$("#mod_remark").val();
    payInfo["paymentTime"]=$("#mod_paymentTime").val();
    payInfo["paymentPhone"]=$("#mod_paymentPhone").val();
    payInfo["paymentAddress"]=$("#mod_paymentAddress").val();
    payInfo["buyCount"]=$("#mod_buyCount").val();
    payInfo["postCode"]=$("#mod_postCode").val();
    jQuery.startLoading();
    
    $.ajax({
        url: "/ccms1/updatePay",
        data: payInfo,
        type: "POST",
        dataType: "json",
        cache: false,
        success: function (data) {
            
            jQuery.endLoading();
            
            if (data.status == "success") {
               $("#succ_tips_lable").text("修改成功");
               $("#succ_tips_layer").show().centerV();
               
               // 关闭修改窗口
               closeDiv();
               
               // 修改成功后，重新查询页面
               payListAjax();
               
            }else{
               $("#succ_tips_lable").text("修改失败，请重新修改");
               $("#succ_tips_layer").show().centerV();
            }
            
        }
    });
} 
function resetInfo(){
	$("#paymentId").val("");
	$("#paymentName").val("");
	$("#paymentValue").val("");
	$("#remark").val("");
	$("#paymentPhone").val("");
	$("#paymentAddress").val("");
	$("#buyCount").val("");
	$("#postCode").val("");
	$("#paymentTime").val("");
}
function closeDiv(){
	$("#modify").hide();
	$("#mod_paymentId").val("");
	$("#mod_paymentName").val("");
	$("#mod_paymentValue").val("");
	$("#mod_remark").val("");
	$("#mod_paymentTime").val("");
	$("#mod_paymentPhone").val("");
	$("#mod_paymentAddress").val("");
	$("#mod_buyCount").val("");
	$("#mod_postCode").val("");
}   
    