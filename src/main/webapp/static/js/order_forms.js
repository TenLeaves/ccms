$(function(){
	// 切换tab签
	$("#tablist li").click(function(){
		var $this=$(this),index=$this.index();
		$this.addClass("on").siblings().removeClass("on");
		$(".tabs-cont",".sec-box").eq(index).show().siblings().hide();
	});
});
ret={};
function checkOrderInfo(){
	ret.subType=$("#subType").val();
	ret.company=$("#company").val();
	ret.startT=$("#startTime").val();
	ret.endT=$("#endTime").val();
	ret.orderId=$("#orderId").val();
	ret.staffType=$("#staffType").val();
	ret.staffId=$("#staffId").val();
	ret.product=$("#product").val();
	var productPaper=$("#productPaper").val();
	if(productPaper != ""){
		var month=productPaper.split("-");
		ret.month=month[1];
		ret.year=month[0];
	}
	jQuery.startLoading();
	jQuery.ajax({
		url: "/ccms1/checkOrderFormsSum",
		type:'POST',
		dataType:"json",
		data:{"selInfo":$.toJSON(ret)},
		success: function (result) {
			  if(result.num==null || result.num==""){
				  $("#count").text("订购总数："+0+"（本）");
			  }else{
				  $("#count").text("订购总数："+result.num+"（本）");
			  }
			  $("#money").hide();
			  if(productPaper == ""){
				  if(result.money==null || result.money==""){
					  $("#money").text("订购总金额："+0+"（元）");
				  }else{
					  $("#money").text("订购总金额："+result.money+"（元）");
				  }
				  $("#money").show();
			  }
			  selectPage();
        }
    });	
}
function checkOrderDetailInfo(){
	ret.subType=$("#subType").val();
	ret.startT=$("#startT").val();
	ret.endT=$("#endT").val();
	ret.staffType=$("#staffType").val();
	ret.staffId=$("#staffId").val();
	ret.product=$("#product").val();
	ret.year=$("#productPaper").val();
	jQuery.startLoading();
	jQuery.ajax({
		url: "/ccms1/checkDetailFormsSum",
		type:'POST',
		dataType:"json",
		data:{"selInfo2":$.toJSON(ret)},
		success: function (result) {
			 $("#count").text("总套数为："+result+"（套）");
			  selectPage2();
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
function selectPage(){
	jQuery.ajax({
		url: "/ccms1/checkOrderFormsPage",
		type:'POST',
		data:{"selInfo":$.toJSON(ret)},
		success: function (result) {
			  jQuery.endLoading();
              $('#pagesList').empty().html(result);
              $("#btn0").show();
        }
    });
}
function checkSendInfo(){
	ret.custName=$("#custName").val();
	ret.exportInfo=$("#export").val();
	ret.startTime=$("#startTime").val();
	ret.orderId=$("#orderId").val();
	ret.endTime=$("#endTime").val();
	ret.productDis=$("#productDis").val();
	var productPaper=$("#productPaperDis").val();
	if(productPaper != ""){
		var month=productPaper.split("-");
		ret.month=month[1];
		ret.year=month[0];
	}
	jQuery.startLoading();
	jQuery.ajax({
		url: "/ccms1/checkSendFormsSum",
		type:'POST',
		data:{"selInfo1":$.toJSON(ret)},
		success: function (result) {
			  $("#count1").text("配送总数："+result+"（本）");
			  selectPage1();
        }
    });
}
function selectPage1(){
	jQuery.ajax({
		url: "/ccms1/checkSendFormsPage",
		type:'POST',
		data:{"selInfo1":$.toJSON(ret)},
		success: function (result) {
			  jQuery.endLoading();
              $('#pagesList1').empty().html(result);
              $("#btn0").show();
        }
    });
}
function selectPage2(){
	jQuery.ajax({
		url: "/ccms1/checkDetailFormsPage",
		type:'POST',
		data:{"selInfo2":$.toJSON(ret)},
		success: function (result) {
			  jQuery.endLoading();
              $('#pagesList2').empty().html(result);
              $("#btn0").show();
        }
    });
}
function changeExport(){
	var flag=$("#export").val();
	if(flag=='1'){
		$("#exportTime").css("display","none");
	}else{
		$("#exportTime").css("display","table-row");
		$("#startTime").val("");
		$("#endTime").val("");
	}
}