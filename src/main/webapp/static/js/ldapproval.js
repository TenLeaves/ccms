$(function(){
	// 不通过
	$(".no-pass-btn").live("click",function(){
		var $this=$(this);
		var top=$this.offset().top,left=$this.offset().left;
		var $layer=$("#no-pass-layer"),w=$layer.outerWidth();
		$layer.css({left:left-w-10+"px",top:top+"px"}).show().centerV();
	});
});
function selectBtn(){
	jQuery.startLoading();
	$('#selStockCusts').ajaxSubmit({
        success: function (result) {
        	$('#talStock').empty().html(result);
        }
    });
	$('#selCusts').ajaxSubmit({
        success: function (result) {
        	jQuery.endLoading();
        	$('#pagesList').empty().html(result);
        }
    });
}
var obj;
function appSucc(obj1){
	var ret={};
	obj=obj1;
	var tr=obj.parentNode.parentNode;
	var flag=0;
	var key;
	var name;
	for(var i=0;i<tr.childNodes.length;i++){
		 if(flag==0 && tr.childNodes[i].nodeName!="#text"){
			 flag++;
			 key=tr.childNodes[i].innerHTML;
			 ret.key=key;
		 }else if(flag==1 && tr.childNodes[i].nodeName!="#text"){
			 flag++;
			 name=tr.childNodes[i].innerHTML;
			 ret.name=name;
		 }
	}
	jQuery.startLoading();
	jQuery.ajax({
		   type: "POST",
		   url: "/ccms1/updateLdAppInfo",
		   data: {"user":$.toJSON(ret)},
		   success: function(result){
			   jQuery.endLoading();
			   $('#pagesList').empty().html(result);
		   }
	});
}
function appSucc1(obj1){
	obj=obj1;
	var tr=obj.parentNode.parentNode;
	var flag=false;
	var key;
	var ret={};
	for(var i=0;i<tr.childNodes.length;i++){
		 if(!flag && tr.childNodes[i].nodeName!="#text"){
			 flag=true;
			 key=tr.childNodes[i].innerHTML;
			 ret.key=key;
		 }
	}
	jQuery.startLoading();
	jQuery.ajax({
		   type: "POST",
		   url: "/ccms1/updateStockLdAppInfo",
		   data: {"user":$.toJSON(ret)},
		   success: function(result){
			   jQuery.endLoading();
			   $('#talStock').empty().html(result);
		   }
	});
}
function appError(obj1){
	obj=obj1;
}
function closeBox(){
	$("#no-pass-layer").hide();
}
var boxInfo;
function sureBtn(){
	boxInfo=$("#boxInfo").val();
	nextInfo();
}
function nextInfo(){
	var tr=obj.parentNode.parentNode;
	var flag=0;
	var key;
	var name;
	var ret={};
	for(var i=0;i<tr.childNodes.length;i++){
		 if(flag==0 && tr.childNodes[i].nodeName!="#text"){
			 flag++;
			 key=tr.childNodes[i].innerHTML;
			 ret.key=key;
		 }else if(flag==1 && tr.childNodes[i].nodeName!="#text"){
			 flag++;
			 name=tr.childNodes[i].innerHTML;
			 ret.name=name;
		 }
	}
	ret.boxInfo=boxInfo;
	if(key.length != 7){
		jQuery.startLoading();
		jQuery.ajax({
		type: "POST",
		   url: "/ccms1/updateErrAppInfo",
		   data: {"user":$.toJSON(ret)},
		   success: function(result){
			   jQuery.endLoading();
			   $('#pagesList').empty().html(result);
		   }
		});
	}else{
		jQuery.startLoading();
		jQuery.ajax({
		type: "POST",
		   url: "/ccms1/updateStockErrAppInfo",
		   data: {"user":$.toJSON(ret)},
		   success: function(result){
			   jQuery.endLoading();
			   $('#talStock').empty().html(result);
		   }
		});
	}
}
function showDetail(obj){
	var tr=$(obj).closest("tr").find("td");
	var list=$(tr[0]).html();
	if(list.length==7){
		window.showModalDialog("/ccms1/toPrintDetail?subscribeId="+list,window,"dialogWidth=1000px;dialogHeight=500px");
	}else{
		window.showModalDialog("/ccms1/toOrderEdit?subscribeId="+list+"&key=1",window,"dialogWidth=1000px;dialogHeight=500px");
	}
}

