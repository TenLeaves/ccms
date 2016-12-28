$(function(){
	// 切换tab签
	$("#tablist li").click(function(){
		var $this=$(this),index=$this.index();
		$this.addClass("on").siblings().removeClass("on");
		$(".tabs-cont",".sec-box").eq(index).show().siblings().hide();
	});
	// 选择文件
	$("#file-btn").click(function(){
		$(this).prev("input[type='file']").trigger("click");
	});
	// 查看订购详情
	$(".detail-btn").live("click",function(){
		$("#customer-layer").show().centerV();
	});
	// 修改订购信息
	$(".edit-btn").live("click",function(){
		$("#customer-edit").show().centerV();
	});
	// 搜索条件切换
	$(".classify li").click(function(){
		$(this).addClass("on").siblings().removeClass("on");
	});
});

function importPost(){
	jQuery.startLoading();
	$('#importPostInfo').ajaxSubmit({
		dataType : "json",
        success: function (data) {
            jQuery.endLoading();
            var dataMap = data.returnMap;
            if(dataMap.tag=='0'){
            	$('#totalNumId').text(dataMap.totalNum);
            	$('#successNumId').text(dataMap.succNum);
            	$('#failNumId').text(dataMap.errorNum);
            	var errorDetail=dataMap.errorList;
            	for(i=0;i<errorDetail.length;i++){
            		var tempError = errorDetail[i];
            		var tb = $('#importFailRecordTb');
            		tb.append("<tr>" +
            				"<td width='20%'>"+tempError.line+"</td>" +
            				"<td width='40%'>"+tempError.error+"</td>" +
            				"<td width='40%'>"+tempError.right+"</td>" +
            				"</tr>");
            	}
            	$("#succ_tips_lable").text('导入成功');
  	          $("#succ_tips_layer").show().centerV();
            }else{
            	alert(data.errorMsg);
            }
        }
    });
}

function searchSendList(){
	var condition={};
	condition.sendId=$('#sendId').val();
	condition.receivePeople=$('#receivePeople').val();
	condition.receivePhone=$('#receivePhone').val();
	condition.receiveAddr=$('#receiveAddr').val();
	condition.start=$('#sendTimeStartInput').val();
	condition.end=$('#sendTimeEndInput').val();
	if(!validatecondition(condition)){
		return;
	}
	jQuery.startLoading();
	jQuery.ajax({
	      type : 'POST',
	      url : '/ccms1/qrySendWithPostInfo',
		data:{
			"condition":$.toJSON(condition)
		},
        success: function (data) {
        	jQuery.endLoading();
            $('#sendListInfoTbl').empty().append(data);//此处ID为from的ID
        }
    });
	
}

function validatecondition(condition){
	$('.red_border').removeClass('red_border');
	
	if(condition.receivePhone!=''){
		regetex = /^1\d{10}$/;
		if(!regetex.test(condition.receivePhone)){
			$("#fail_tips_lable").text('收件人手机号必须是11位数字');
	        $("#fail_tips_layer").show().centerV();
			$('#receivePhone').addClass('red_border');
			return false;
		}
	}
	
	if(condition.start!=''&&condition.end==''){
		$("#fail_tips_lable").text('请选择结束日期');
        $("#fail_tips_layer").show().centerV();
		$('#sendTimeEndInput').addClass('red_border');
		return false;
	}
	if(condition.start==''&&condition.end!=''){
		$("#fail_tips_lable").text('请选择开始日期');
        $("#fail_tips_layer").show().centerV();
		$('#sendTimeStartInput').addClass('red_border');
		return false;
	}
	return true;
}
