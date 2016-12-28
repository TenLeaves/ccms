$(function(){
	var flag = $('#isIndexLeaderFormTag').text();
	if(flag!=''){//是首页
		$('#timeConditionArea').hide();
		$('#resultAreaInfo').hide();
		var year = '' +new Date().getFullYear();
		$("#year").val('2014');
		checkYear();
	}else{
		$('#timeConditionArea').show();
		$('#resultAreaInfo').show();
	}
});

function checkYear(){
	var year=$("#year").val();
	if(year==""){
        $("#succ_tips_lable").text("请填写需要查询的年份");
        $("#succ_tips_layer").show().centerV();
        return;
	}
	jQuery.startLoading();
	jQuery.ajax({
		url: "/ccms1/leaderFormsAction?year="+year,
		success: function (result) {
			jQuery.endLoading();
			$('#pagesList').empty().html(result);
			$('#userSelectYearId').text(year+"年");
        }
    });	

}
