var ret={};
function checkOrderInfo(){
	ret.orderSeq=$("#orderSeq").val();
	ret.startTime=$("#startTime").val();
	ret.endTime=$("#endTime").val();
	ret.custName=$("#custName").val();
	ret.address=$("#address").val();
	ret.subType=$("#subType").val();
	jQuery.startLoading();
	$('#selCusts').ajaxSubmit({
		type:'POST',
		data:{"selInfo":$.toJSON(ret)},
		success: function (result) {
			  jQuery.endLoading();
              $('#pagesList').empty().html(result);
        }
    });
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
function subState(data){
	for(var i=0;i<5;i++){
		if(data==i){
			$("#subType"+i).addClass("current");
		}else{
			$("#subType"+i).removeClass("current");
		}
	}
	checkOrderInfo();
}
function showDetail(obj){
	var tr=$(obj).closest("tr").find("td");
	var list=$(tr[0]).html();
	window.showModalDialog("/ccms1/toOrderEdit?subscribeId="+list+"&key=1",window,"dialogWidth=1000px;dialogHeight=500px");
}
function changeDetailInfo(obj){
	var tr=$(obj).closest("tr").find("td");
	var list=$(tr[0]).html();
	window.showModalDialog("/ccms1/toOrderEdit?subscribeId="+list+"&key=2",window,"dialogWidth=1000px;dialogHeight=500px");
}

function deleteOrder(obj){
    var choice = window.confirm("您确定删除这条订单？");
    if(!choice){
    	return;
    }
	var _this = $(obj);
	var tr=_this.parent().parent();
	var tdArr = tr.find('td');
	var subscribleId=$(tdArr[0]).text();
	jQuery.ajax({
		type : 'POST',
		url : '/ccms1/deleteSubOrder',
		dataType : 'json',
		data : {
			"SUBSCRIBE_ID" : subscribleId,
		},
		success : function(data)// 当请求成功时触发函数
		{
			checkOrderInfo();
			
			if(data.tag=='0'){
				$("#succ_tips_lable").text(data.msg);
		        $("#succ_tips_layer").show().centerV();
			}else{
				$("#fail_tips_lable").text(data.msg);
		        $("#fail_tips_layer").show().centerV();
			}
			
		}
	});
}

