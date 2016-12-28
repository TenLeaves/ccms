var ret={};
function exporeFlag(data){
	if(data==0){
		$("#tr1").css("display","none");
		$("#exploreTime").val("");
	}else{
		$("#tr1").css("display","table-row");
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
          var offset = element.offset();
          window.parent.scrollTo(offset.left, offset.top);
        }
 
        return false;
      });
    }
  });
});
var myselfWay;
function checkOrderInfo(){
    	var telephone=$("#telephone").val();
//    	if(telephone!=""){
//    		var reg=/^1[0-9]{10}$/;
//    		if(!reg.test(telephone)){
//    			alert("您输入的联系电话必须为1开头的11位数字");
//    			return;
//    		}
//    	}
    	retList=[];
    	ret.telephone=telephone;
    	var custName=$("#custName").val();
    	ret.custName=custName;
    	var explorWay;
    	for(var i=0;i<2;i++){
    		if($("#explorWay"+i).attr("class").indexOf("sel-box-on")>=0){
    			explorWay=i+"";
    		}
    	}
    	for(var i=0;i<=3;i++){
    		if($("#myselfWay"+i).attr("class").indexOf("sel-box-on")>=0){
    			myselfWay=i+"";
    		}
    	}
    	
    	
    	ret.explorWay=explorWay;
    	ret.myselfWay=myselfWay;
    	if(ret.explorWay == '1'){
    		var exploreTime=$("#exploreTime").val();
    		ret.exploreTime=exploreTime;
    		
    	}
    	var address=$("#address").val();
    	ret.address=address;
    	jQuery.startLoading();
    	$('#selCusts').ajaxSubmit({
    		type:'POST',
    		data:{"selInfo":$.toJSON(ret)},
    		success: function (result) {
    			jQuery.endLoading();
    			$('#pagesList').empty().html(result);
    			if(ret.explorWay == '1'){//已经导出过
    				$("#btn1").css("display","none");
    				$("#btn0").css("display","none");
    			}else{
    				if(myselfWay==0){
        				$("#btn1").css("display","block");
        				$("#btn0").css("display","none");
        			}else{
        				$("#btn1").css("display","none");
        				$("#btn0").css("display","block");
        			}
    			}
            }
        });
    }
    var retList=new Array();
    function checkOutInfo(obj){
		var tdRet="";
		var tr=obj.parentNode.parentNode;
		var index=0;
		var orderId="";
		var subId="";
		var flag=false;
		for(var i=1;i<tr.childNodes.length;i++){
			 if(!flag && tr.childNodes[0].nodeName=="#text"){
				 flag=true;
				 i+=2;
			 }
			 if(tr.childNodes[i].nodeName!="#text"){
				 tdRet+=tr.childNodes[i].innerHTML+"-&-";
				 index++;
				 if(index==7){
					 orderId=tr.childNodes[i].innerHTML;
				 }else if(index==8){
					 subId=tr.childNodes[i].innerHTML;
				 }
			 }
		}
		tdRet+=";";
    	if(!$(obj).attr("checked")){
    		$("[name='c1']").removeAttr("checked");
    		for(var i=0;i<retList.length;i++){
    			var list=new Array();
    			list=retList[i].split("-&-");
    			if(list[6]==orderId&& list[7]==subId){
    				remove(i,retList);
    			}
    		}
    	}else{
    		retList.push(tdRet);
    	}
    }
    
    function exportList(){
    	if(retList==null || retList.length==0){
            $("#succ_tips_lable").text("请选择所要导出的信息");
            $("#succ_tips_layer").show().centerV();
    		return false;
    	}
    	$("#partExport").val(retList+"");
    	return true;
    }
    function sureList(){
    	if(retList==null || retList.length==0){
            $("#succ_tips_lable").text("请选择要出库的信息");
            $("#succ_tips_layer").show().centerV();
    		return false;
    	}
    	$("#partStock").val(retList+"");
    	return true;
    }
    function remove(dx,list) 
    {
        if(isNaN(dx)||dx>list.length){
        	return false;
        } 
        for(var i=0,n=0;i<list.length;i++) 
        { 	
            if(list[i]!=list[dx]) 
            { 
                list[n++]=list[i]; 
            } 
        } 
        list.length-=1; 
    } 