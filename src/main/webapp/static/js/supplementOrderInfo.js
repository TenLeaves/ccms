function removePlace(){
	$("#fixTel1").val("");
	$("#fixTel1").removeClass("c-place");
}
function loadNumInfo(){
	var date=new Date();
	$("#yearWay0").text((date.getFullYear()-2)+"（0/13）");
	$("#yearWay1").text((date.getFullYear()-1)+"（0/13）");
	$("#yearWay2").text(date.getFullYear()+"（0/13）");
	$("#yearWay3").text((date.getFullYear()+1)+"（0/13）");
}
function labelWay(data){
	if(data == '0'){
		$("#labelTr").css("display","none");
		$("#labelContent").val("");
	}else{
		$("#labelTr").css("display","table-row");
	}
}
function clearMonthSub(data){
	$("#startTime").val("");
	$("#endTime").val("");
	year=[];
	var date=new Date();
	for(var i=0;i<4;i++){
		$('input[name="month'+i+'"]:checked').removeAttr("checked");
		$("#yearWay"+i).text((date.getFullYear()-2+parseInt(i,10))+"（0/13）");
		$("#yearWay"+i).removeClass("sel-box-selected");
		$("#yearWay"+i).removeClass("sel-box-on");
	}
	$("#strongInfo").css("display","none");
	if(data==1){
		overYear=2;
		$("#monthFlag1").show();
		$("#monthFlag0").hide();
		$("#endTime").attr("disabled","disabled");
		$("#startTime").attr("disabled","disabled");
		year_info(2);
	}else{
		$("#monthFlag0").show();
		$("#monthFlag1").hide();
		$("#startTime").removeAttr("disabled");
		$("#endTime").removeAttr("disabled");
	}
}
function clear_info1(){
	$("#fixTel1").val("");
	$("#fixTel2").val("");
	$("#fixTel3").val("");
	$("#username").val("");
	$("#company").val("");
	$("#contactName").val("");
	$("#contactTel").val("");
	$("#telephone").val("");
	$("#textInfo").val("");
	buttonDeal(0,"suppWay",2);
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
function changePage(){
    var error="";
    $("#error1").text("");
	$("#error1").css("display","none");
	var username=document.getElementById("username").value;
	var company=document.getElementById('company').value;
	var contactTel=document.getElementById('contactTel').value;
	var telephone=document.getElementById("telephone").value;
	if(company==null||company==""){
		error="订购单位信息不能为空";
		$("#error1").text(error);
		$("#error1").css("display","block");
		return;
	}
	if(username==null||username==""){
		error="联系人信息不能为空";
		$("#error1").text(error);
		$("#error1").css("display","block");
		return;
	}
//	if(telephone==null||telephone==""){
//		error="联系人电话不能为空";
//		$("#error1").text(error);
//		$("#error1").css("display","block");
//		return;
//	}
	var reg=/^1[0-9]{10}$/;
	if(telephone != "" && !reg.test(telephone)){
		error="您输入的联系人手机必须为1开头的11位数字";
		$("#error1").text(error);
		$("#error1").css("display","block");
		return;
	}
	if(contactTel != "" && (contactTel.length!=11 || !reg.test(contactTel))){
		error="您输入的经联人手机必须为1开头的11位数字";
		$("#error1").text(error);
		$("#error1").css("display","block");
		return;
	}
	var fixTel1=$("#fixTel1").val();
	var fixTel2=$("#fixTel2").val();
	var fixTel3=$("#fixTel3").val();
	if(fixTel1!="" || fixTel2!="" || fixTel3!=""){
		var reg=/^[0-9]+$/;
		if(fixTel1!=''&& fixTel1!="区号" &&!reg.test(fixTel1)){
			error="您输入的固话信息必须为数字";
			$("#error1").text(error);
			$("#error1").css("display","block");
			return;
		}
		if(fixTel2!=''&&!reg.test(fixTel2)){
			error="您输入的固话信息必须为数字";
			$("#error1").text(error);
			$("#error1").css("display","block");
			return;
		}
		if(fixTel3!=''&&!reg.test(fixTel3)){
			error="您输入的固话信息必须为数字";
			$("#error1").text(error);
			$("#error1").css("display","block");
			return;
		}
	}
	hiddenForm(2);
}
var indexFlag=1;
function hiddenForm(data){
	$("#form"+data).css("display","block");
	$("#img"+indexFlag).addClass("visited");
	$("#img"+data).removeClass("visited");
	$("#img"+data).addClass("on");
	for(var i=1;i<=3;i++){
		if(i!=data){
			$("#form"+i).css("display","none");
		}
	}
	indexFlag=data;
	var offset = $("#a4").offset();
    window.parent.scrollTo(offset.left, offset.top);
}
var ret={};
var retSend={};
//按钮数据的页面显示
function buttonDeal(data,btnId,length){
	for(var i=0;i<length;i++){
		if(i==data){
			$("#"+btnId+""+i).addClass("sel-box-on");
		}else{
			$("#"+btnId+""+i).removeClass("sel-box-on");
		}
	}
}
//寄送方式处理
function getPro(data){
	buttonDeal(data,"getProWay",4);
	if(data==0){
		$("#sendAddress").attr("disabled","disabled");
		$("#sendAddress").val("");
		$("#hidden1").hide();
		$("#hidden2").hide();
		$("#getCustomer").val($("#username").val());
		$("#getTel").val($("#telephone").val());
	}else{
		$("#sendAddress").removeAttr("disabled");
		$("#hidden1").show();
		$("#hidden2").show();
	}
}
function clearOrderAll(){
	var tr=$("#sendListTable1");
	tr.empty();
	orderList=[];
}
function changePage2(){
	$("#error3").text("");
	$("#error3").css("display","none");
	var  trInfo=document.getElementById("sendListTable1").getElementsByTagName("tr");
	var trLength=trInfo.length;
	if(trLength==0){
		$("#error3").text("请至少确认一条订单");
		$("#error3").css("display","block");
		return;
	}
	var tb = document.getElementById('tal4');
	if(tb.rows.length==1){
		tb.deleteRow(0);
	}
	$col = $("<tr><td width='20%'>"+orderList[0].OrderSeq+"</td>" +
			"<td width='20%'>"+(orderList[0].productType=='1'?"中国书法普及版":"中国书法专业版")+"</td>" +
			"<td width='16%' onmousemove='showDivHidden(1,\""+orderList[0].OrderSeq+"\")' onmouseout='liveDivHidden(1)'>"+orderList[0].time+"</td>" +
					"<td width='16%'>"+orderList[0].orderCount+"</td>" +
					"<td><a href='javascript:void(0);' class='c36c change-order' onclick='change_order()'>切换订单</a>" +
					"<a href='javascript:void(0);' class='c36c copy-dispatch' onclick='copy_order()'>复制配送</a>" +
					"<a href='javascript:void(0);' class='c36c lead-dispatch' onclick='insert_send()'>导入配送</a></td></tr>");  
			 $("#tal4").append($col);
	sendNumIndex=orderList[0].OrderSeq;
	var tr=$("#sendListTable2");
	tr.empty();
	for(var i=0;i<orderList.length;i++){
		orderList[i].sendList=null;
		orderList[i].number=0;
	}
	sendList=[];
	$("#getCustomer").val($("#username").val());
	$("#getTel").val($("#telephone").val());
	hiddenForm(3);
}
//保存产品(订单)
function saveInfo(){
	var error="";
	$("#error2").text("");
	$("#error2").css("display","none");
	var subscribeCount=$("#subscribeCount").val();
	if(subscribeCount==""){
		error="您所填写的订阅数量不能为空";
		$("#error2").text(error);
		$("#error2").css("display","block");
		return;
	}
	var reg=/^[0-9]+$/;
	if(!reg.test(subscribeCount)){
		error="您输入的订阅数量必须为数字";
		$("#error2").text(error);
		$("#error2").css("display","block");
		return;
	}
	for(var i=0;i<2;i++){
	    if($("#monthSub"+i).attr("checked")){
	    	ret.monthChoice=i+"";
	    }
	} 
	if(ret.monthChoice=='0'){
		var startTime=$("#startTime").val();
		var endTime=$("#endTime").val();
		if(startTime==""||endTime==""){
			$("#strongInfo").text("");
			error="您所填写的订阅周期不能为空";
			$("#error2").text(error);
			$("#error2").css("display","block");
			return;
		}
		var startT=new Array();
		var endT=new Array();
		startT=startTime.split("-");
		endT=endTime.split("-");
		if(parseInt(endT[0],10)<parseInt(startT[0],10)||(parseInt(endT[0],10)==parseInt(startT[0],10)&&parseInt(endT[1],10)<parseInt(startT[1],10))){
			$("#strongInfo").text("");
			error="您所填写的订阅起始周期不能大于结束周期";
			$("#error2").text(error);
			$("#error2").css("display","block");
			return;
		}
		var sum=0;
		if(parseInt(endT[0],10)==parseInt(startT[0],10)){
			sum=parseInt(endT[1],10)-parseInt(startT[1],10)+1;
		}else{
			sum+=parseInt(endT[1],10)+(12-parseInt(startT[1],10))+1;
			sum+=(parseInt(endT[0],10)-parseInt(startT[0],10)-1)*12;
		}
		$("#strongInfo").css("display","inline");
		$("#strongInfo").text("共"+sum+"期");
		var time=startT[0]+"年"+startT[1]+"月至"+endT[0]+"年"+endT[1]+"月";
		ret.sum=sum;
		ret.startTime=startTime;
		ret.endTime=endTime;
		ret.time=time;
	}else{
		var monthFlag=false;
		var time="";
		var time1="";
		var time2="";
		var sum=0;
		var date=new Date();
		for(var i=0;i<4;i++){
			if(year[i]!=null && year[i].length!=0){
				time1+=((date.getFullYear()-2)+i)+"年<br/>";
				if(!monthFlag){
					time+=((date.getFullYear()-2)+i)+"年&nbsp;";
				}
				for(var j=0;j<year[i].length;j++){
					if(year[i][j]=='13'){
						time1+="合刊&nbsp;";
					}else{
						time1+=year[i][j]+"月&nbsp;";
					}
					time2+=((date.getFullYear()-2)+i)+"-"+(parseInt(year[i][j],10)<10?"0"+year[i][j]:year[i][j])+",";
					if(!monthFlag){
						if(year[i][j]=='13'){
							time+="合刊&nbsp;";
						}else{
							time+=year[i][j]+"月&nbsp;";
						}
					}
					sum++;
				}
				time1+="<br/>";
				monthFlag=true;
			}
		}
		time2=time2.substring(0,time2.length-1);
		if(!monthFlag){
			error="请选择您要订购产品的月份";
			$("#error2").text(error);
			$("#error2").css("display","block");
			return;
		}
		ret.time=time;
		ret.time1=time1;
		ret.time2=time2;
		ret.sum=sum;
	}
	if(updateOrderInfoFlag){
		ret.OrderSeq=orderList[updateOrderInfo].OrderSeq;
	    for(var i=0;i<3;i++){
	    	if($("#subsWay"+i).attr("class").indexOf("sel-box-on")>=0){
	    		ret.orderWay=i+"";
	    	}
	    } 
	    for(var i=0;i<2;i++){
	    	if($("#verNum"+i).attr("class").indexOf("sel-box-on")>=0){
	    		ret.productType=i+"";
	    	}
	    } 
	     ret.orderCount=subscribeCount;
		orderList[updateOrderInfo]=ret;
		$("#time"+ret.OrderSeq).html(ret.time);
		$("#time1"+ret.OrderSeq).html(ret.time1);
		$("#orderCount"+ret.OrderSeq).html(ret.orderCount);
		$("#productType"+ret.OrderSeq).html(ret.productType=='1'?"中国书法普及版":"中国书法专业版");
		updateOrderInfoFlag=false;
		$("#saveOrderBtn").val("保存订单");
		resetOrder();
	}else{
		jQuery.startLoading();
		jQuery.ajax({
		   url: "/ccms1/selectSeqNew?date="+new Date().getTime(),
		   success: function(data){
			 jQuery.endLoading();
		     ret.OrderSeq=data;
		     for(var i=0;i<3;i++){
		    	if($("#subsWay"+i).attr("class").indexOf("sel-box-on")>=0){
		    		ret.orderWay=i+"";
		    	}
		     } 
		     for(var i=0;i<2;i++){
		    	if($("#verNum"+i).attr("class").indexOf("sel-box-on")>=0){
		    		ret.productType=i+"";
		    	}
		    } 
		     ret.orderCount=subscribeCount;
		 	addCol(ret);
		   }
		});	
	}
	$("#a1").hide();
//	resetOrder();
	 var offset = $("#a3").offset();
     window.parent.scrollTo(offset.left, offset.top);
}
var updateFlag1=false;
//保存产品（配送）
function saveSendInfo(){
	$("#error4").text("");
	$("#error4").css("display","none");
	var sendNumber=$("#sendNumber").val();
	if(sendNumber==""){
		$("#error4").text("您所填写的配送数量不能为空");
		$("#error4").css("display","block");
		return;
	}
	var reg=/^[0-9]+$/;
	if(!reg.test(sendNumber)){
		$("#error4").text("您输入的配送数量必须为数字");
		$("#error4").css("display","block");
		return;
	}
	var proWay='0';
	for(var i=0;i<3;i++){
		 if($("#getProWay"+i).attr("class").indexOf("sel-box-on")>=0){
			 proWay=i+"";
		 }
	 }
	var getCustomer=$("#getCustomer").val();
	var labelContent=$("#labelContent").val();
	var sendAddress=$("#sendAddress").val();
	var getTel=$("#getTel").val();
	if(proWay!='0'){
		if(getCustomer==""){
			$("#error4").text("您所填写的收件人姓名不能为空");
			$("#error4").css("display","block");
			return;
		}
		if(sendAddress==""){
			$("#error4").text("您所填写的寄送地址不能为空");
			$("#error4").css("display","block");
			return;
		}
//		var reg=/^1[0-9]+$/;
//		if(getTel != "" && (getTel.length!=11 || !reg.test(getTel))){
//			$("#error4").text("您输入的收件人电话必须为1开头的11位数字");
//			$("#error4").css("display","block");
//			return;
//		}
	}
	var postcode=$("#postcode").val();
	if(postcode!=null && postcode!=""){
		var reg=/^[0-9]{6}$/;
		if(!reg.test(postcode)){
			$("#error4").text("您输入的邮编必须为6位数字");
			$("#error4").css("display","block");
			return;
		}
	}
	var listNumberInfo=0;
	for(var i=0;i<orderList.length;i++){
		if(orderList[i].OrderSeq == sendNumIndex){
			listNumberInfo=orderList[i].orderCount;
		}
	}
	var sum=0;
	if(sendList!=null){
		for(var i=0;i<sendList.length;i++){
			if(!(updateFlag && i== updateSeq)){
				sum+=parseInt(sendList[i].sendNumber,10);
			}
		}
	}
	sum+=parseInt($("#sendNumber").val(),10);
	$("#ListNumInfo").css("display","inline");
	listNumberInfo=listNumberInfo-sum;
	$("#ListNumInfo").html("&nbsp;&nbsp;&nbsp;&nbsp;本订单剩余数："+"<strong class='c69c'>"+listNumberInfo+"</strong>本");
	if(listNumberInfo<0){
		$("#error4").text("您订单剩余量小于配送数量，请修改");
		$("#error4").css("display","block");
		return;
	}
	if(updateFlag){
		retSend.sendSeq=sendList[updateSeq].sendSeq;
	     for(var i=0;i<3;i++){
	    	if($("#sendAdd"+i).attr("class").indexOf("sel-box-on")>=0){
	    		retSend.sendAdd=i+"";
	    	}
	     } 
	     for(var i=0;i<4;i++){
	    	if($("#getProWay"+i).attr("class").indexOf("sel-box-on")>=0){
	    		retSend.getProWay=i+"";
	    	}
	     } 
	     for(var i=0;i<2;i++){
	    	if($("#labelInfo"+i).attr("class").indexOf("sel-box-on")>=0){
	    		retSend.labelInfo=i+"";
	    	}
	     } 
	     retSend.sendNumber=sendNumber;
	     retSend.getCustomer=getCustomer;
	     retSend.sendAddress=sendAddress;
	     retSend.labelContent=labelContent;
	     retSend.getTel=getTel;
	     retSend.postcode=$("#postcode").val();
	     retSend.textInfoSend=$("#textInfoSend").val();
	 	 $("#sendAddress"+retSend.sendSeq).html(retSend.sendAddress);
	 	 $("#getCustomer"+retSend.sendSeq).html(retSend.getCustomer);
	 	 $("#getTel"+retSend.sendSeq).html(retSend.getTel);
 		 sendList[updateSeq]=retSend;
 		 retSend={};
 		 $("#saveSendBtn").val("保存配送单");
 	}else{
		     retSend.sendSeq=new Date().getTime();
		     for(var i=0;i<3;i++){
		    	if($("#sendAdd"+i).attr("class").indexOf("sel-box-on")>=0){
		    		retSend.sendAdd=i+"";
		    	}
		     } 
		     for(var i=0;i<4;i++){
		    	if($("#getProWay"+i).attr("class").indexOf("sel-box-on")>=0){
		    		retSend.getProWay=i+"";
		    	}
		     } 
		     for(var i=0;i<2;i++){
		    	if($("#labelInfo"+i).attr("class").indexOf("sel-box-on")>=0){
		    		retSend.labelInfo=i+"";
		    	}
		     } 
		     retSend.sendNumber=sendNumber;
		     retSend.getCustomer=getCustomer;
		     retSend.sendAddress=sendAddress;
		     retSend.labelContent=labelContent;
		     retSend.getTel=getTel;
		     retSend.postcode=$("#postcode").val();
		     retSend.textInfoSend=$("#textInfoSend").val();
		 	 addColSend(retSend);
 	}
	updateFlag=false;
	resetSend();
	$("#a2").hide();
	 var offset = $("#a4").offset();
    window.parent.scrollTo(offset.left, offset.top);
}
var orderList=new Array();
var sendNudmIndex='0';
var sendList=new Array();
//添加一行（订单）
function addCol(order) {  
    $col = $("<tr><td width='20%'>"+order.OrderSeq+"</td>" +
    		"<td id='productType"+order.OrderSeq+"' width='20%'>"+(order.productType=='1'?"中国书法普及版":"中国书法专业版")+"</td>" +
    		"<td onmousemove='showDivHidden(0,\""+order.OrderSeq+"\")' onmouseout='liveDivHidden(0)' id='time"+order.OrderSeq+"' width='20%'>"+order.time+"</td>" +
    		"<td id='orderCount"+order.OrderSeq+"' width='20%'>"+order.orderCount+"</td>" +
    		"<td><a href='javascript:void(0);' class='edit' onclick='orderInfoDetail(this,\""+order.OrderSeq+"\")'><a href='#a1'></a></a>" +
    		"<a href='javascript:void(0);' class='dusbin' onclick='delCol(this)'></a></td>" +
    		"<td class='hidden' id='time1"+order.OrderSeq+"'>"+order.time1+"</td></tr>");  
    $("#sendListTable1").append($col);
    orderList.push(order);
    ret={};
    resetOrder();
}
//添加一行（配送）
function addColSend(order) {  
    $col = $("<tr><td width='20%'>"+order.sendSeq+"</td>" +
    		"<td id='sendAddress"+order.sendSeq+"' width='20%'>"+order.sendAddress+"</td>" +
    	    "<td id='getCustomer"+order.sendSeq+"' width='20%'>"+order.getCustomer+"</td>" +
    	    "<td id='getTel"+order.sendSeq+"' width='20%'>"+order.getTel+"</td>" +
    	    "<td><a href='javascript:void(0);' class='edit'  onclick='sendInfoDetail(this,\""+order.sendSeq+"\")'></a>" +
    	    "<a href='javascript:void(0);' class='dusbin' onclick='delColSend(this)'></a></td></tr>");  
    $("#sendListTable2").append($col);
    sendList.push(order);
    retSend={};
}
//删除一行（订单页面）
function delCol(obj) {
	var tr=obj.parentNode.parentNode; 
	trLength=obj.parentNode.parentNode.rowIndex;
	var tbody=tr.parentNode; 
	tbody.removeChild(tr);
	remove(trLength,orderList);
	updateOrderInfoFlag=false;
	$("#saveOrderBtn").val("保存订单");
	resetOrder();
}
//删除一行（配送）
function delColSend(obj) {
	var tr=obj.parentNode.parentNode; 
	trLength=obj.parentNode.parentNode.rowIndex;
	var tbody=tr.parentNode; 
	tbody.removeChild(tr);
	remove(trLength,sendList);
	$("#saveSendBtn").val("保存配送单");
	updateFlag=false;
	resetSend();
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
//清空按钮调用函数(订单信息)
function resetOrder(){
//	ret={};
	buttonDeal(0,"subsWay",3);
	buttonDeal(0,"verNum",2);
	$("#subscribeCount").val("");
	clearMonthSub(0);
	$("#monthSub0").attr("checked",'true'); 
	$("#monthSub1").removeAttr("checked");
}
//清空按钮调用函数(配送信息)
function resetSend(){
//	retSend={};
	buttonDeal(0,"sendAdd",3);
	$("#sendNumber").val("");
    getPro(0);
	buttonDeal(0,"labelInfo",2);
	$("#getCustomer").val("");
	$("#labelContent").val("");
	$("#sendAddress").val("");
	$("#getTel").val("");
	$("#postcode").val("");
	$("#textInfoSend").val("");
	$("#ListNumInfo").css("display","none");
	$("#sendAddress").attr("disabled","disabled");
	$("#error4").text("");
	$("#getCustomer").val($("#username").val());
	$("#getTel").val($("#telephone").val());
}
//按切换按钮时进行的操作
function change_order(){
	 $("#saveSendBtn").val("保存配送单");
	 updateFlag=false;
	 var tb = document.getElementById('tal3');
     var rowNum=tb.rows.length;
     for (var i=0;i<rowNum;i++)
     {
         tb.deleteRow(i);
         rowNum=rowNum-1;
         i=i-1;
     }
    showNewDiv("OrderListResult","bottomArea");
	for(var i=0;i<orderList.length;i++){
		var number=0;
		if(orderList[i].OrderSeq == sendNumIndex){
			if(sendList!=null){
				number=sendList.length;
				orderList[i].number=number;
			}
			orderList[i].sendList=sendList;
			$col = $("<tr><td><input name='radio' checked='checked' type='radio' id='radio"+i+"' value='"+i+"'/>" +
					"</td><td>"+orderList[i].OrderSeq+"</td>" +
							"<td>"+(orderList[i].productType=='1'?"中国书法普及版":"中国书法专业版")+"</td>" +
									"<td>"+orderList[i].time+"</td><td>"+orderList[i].orderCount+"本/期</td><td>"+(orderList[i].number==null?'0':orderList[i].number)+"</td></tr>");  
		}else{
			$col = $("<tr><td><input name='radio' type='radio' id='radio"+i+"' value='"+i+"'/>" +
				"</td><td>"+orderList[i].OrderSeq+"</td>" +
						"<td>"+(orderList[i].productType=='1'?"中国书法普及版":"中国书法专业版")+"</td>" +
								"<td>"+orderList[i].time+"</td><td>"+orderList[i].orderCount+"本/期</td><td>"+(orderList[i].number==null?'0':orderList[i].number)+"</td></tr>");  
		}
		$("#tal3").append($col);
	}
	
}
//切换页面关闭
function closeChange(){
	closeDiv("OrderListResult","bottomArea");
}
//切换页面确认按钮需要进行的逻辑
function sureChangeOrderNum(){
	closeDiv("OrderListResult","bottomArea");
	var value=$("input[name='radio']:checked").val();
	if(value!=null){
		var tb = document.getElementById('tal4');
		tb.deleteRow(0);
		var tb = document.getElementById('sendListTable2');
		var rowNum=tb.rows.length;
		for (var i=0;i<rowNum;i++)
		{
			tb.deleteRow(i);
			rowNum=rowNum-1;
			i=i-1;
		}
		sendList=orderList[value].sendList;
		if(sendList!=null){
			var flag=sendList.length;
			for(var i=0;i<flag;i++){
			    $col = $("<tr><td width='20%'>"+sendList[i].sendSeq+"</td>" +
			    		"<td id='sendAddress"+sendList[i].sendSeq+"' width='20%'>"+sendList[i].sendAddress+"</td>" +
			    	    "<td id='getCustomer"+sendList[i].sendSeq+"' width='20%'>"+sendList[i].getCustomer+"</td>" +
			    	    "<td id='getTel"+sendList[i].sendSeq+"' width='20%'>"+sendList[i].getTel+"</td>" +
			    	    "<td><a href='javascript:void(0);' class='edit'  onclick='sendInfoDetail(this,\""+sendList[i].sendSeq+"\")'></a>" +
			    	    "<a href='javascript:void(0);' class='dusbin' onclick='delColSend(this)'></a></td></tr>");  
			    $("#sendListTable2").append($col);
			}
		}else{
			sendList=[];
		}
		$col = $("<tr><td width='20%'>"+orderList[value].OrderSeq+"</td>" +
				"<td width='20%'>"+(orderList[value].productType=='1'?"中国书法普及版":"中国书法专业版")+"</td>" +
				"<td width='16%'  onmousemove='showDivHidden(1,\""+orderList[value].OrderSeq+"\")' onmouseout='liveDivHidden(1)'>"+orderList[value].time+"</td><td width='16%'>"+orderList[value].orderCount+"</td>" +
						"<td><a href='javascript:void(0);' class='c36c change-order' onclick='change_order()'>切换订单</a>" +
						"<a href='javascript:void(0);' class='c36c copy-dispatch' onclick='copy_order()'>复制配送</a>" +
						"<a href='javascript:void(0);' class='c36c lead-dispatch' onclick='insert_send()'>导入配送</a></td></tr>");  
		$("#tal4").append($col);
		sendNumIndex=orderList[value].OrderSeq;
	}
}
//复制配送信息按钮
function copy_order(){
	$("#saveSendBtn").val("保存配送单");
	 updateFlag=false;
	var tb = document.getElementById('orderList_tal');
    var rowNum=tb.rows.length;
    for (var i=0;i<rowNum;i++)
    {
        tb.deleteRow(i);
        rowNum=rowNum-1;
        i=i-1;
    }
	showNewDiv("copySendInfo","bottomArea");
	index=0;
	for(var i=0;i<orderList.length;i++){
		if(orderList[i].OrderSeq != sendNumIndex && !(orderList[i].sendList!=null && orderList[i].sendList.length!=0)){
			$col = $("<tr><td>&nbsp;</td><td><input type='checkbox' name='checkbox4' id='checkbox"+index+"' onclick='checkAllInfo("+index+")'/></td>"+
					"<td>"+orderList[i].OrderSeq+"</td>" +
					"<td>"+(orderList[i].productType=='1'?"中国书法普及版":"中国书法专业版")+"</td><td>"+orderList[i].time+"</td>" +
							"<td><p>"+orderList[i].orderCount+"本/期</p></td></tr>");  
			$("#orderList_tal").append($col);
			index+=1;
			copyList.push(i);
		}	
	}
}
var index;
var copyList= new Array();
//点击复制配送页面全选按钮时的逻辑
function allCheckBox(){
	if($("#checkbox").attr("checked")){
		for(var i=0;i<index;i++){
			$("[name='checkbox4']").attr("checked",'true');
		}
	}else{
		for(var i=0;i<index;i++){
			$("[name='checkbox4']").removeAttr("checked");//取消全选
		}
	}
}
//复制配送页面关闭
function closeCopy(){
	closeDiv("copySendInfo","bottomArea");
}
//点击任意一个按钮非全选时的逻辑
function checkAllInfo(data){
	if(!$("#checkbox"+data).attr("checked")){
		$("[name='checkbox']").removeAttr("checked");
	}
} 

//复制配送页面点击确认
function sureCopyOrderInfo(){
	closeDiv("copySendInfo","bottomArea");
	if(document.getElementById('sendListTable2').rows.length==0){
        $("#succ_tips_lable").text("请先填写配送地址，然后在进行复制配送");
        $("#succ_tips_layer").show().centerV();
		return;
	}
	var sum=0;
	for(var i=0;i<sendList.length;i++){
		sum+=sendList[i].sendNumber;
	}
	for(var i=0;i<index;i++){
		if($("#checkbox"+i).attr("checked") && sum>orderList[copyList[i]].orderCount){
            $("#succ_tips_lable").text("您的订单id为"+orderList[copyList[i]].OrderSeq+"的订单数量小于需要复制的数量，请更改");
            $("#succ_tips_layer").show().centerV();
			return;
		}
	}
	var checkIndex=0;
	for(var i=0;i<index;i++){
		if($("#checkbox"+i).attr("checked")){
			var sendList1=new Array();
			for(var j=0;j<sendList.length;j++){
				var sendlist=new Object();
				sendlist={};
				sendlist.sendAdd=sendList[j].sendAdd;
				sendlist.getProWay=sendList[j].getProWay;
				sendlist.labelInfo=sendList[j].labelInfo;
				sendlist.sendNumber=sendList[j].sendNumber;
				sendlist.getCustomer=sendList[j].getCustomer;
				sendlist.labelContent=sendList[j].labelContent;
				sendlist.sendAddress=sendList[j].sendAddress;
				sendlist.getTel=sendList[j].getTel;
				sendlist.postcode=sendList[j].postcode;
				sendlist.textInfoSend=sendList[j].textInfoSend;
				sendlist.sendSeq=new Date().getTime();
				sendList1.push(sendlist);
			}
			orderList[copyList[i]].sendList=sendList1;
			orderList[copyList[i]].number=sendList1.length;
			checkIndex++;
		}
	}
}
var updateFlag=false;
var updateSeq;
//更改配送信息的操作
function sendInfoDetail(data,sendDate){
	updateFlag=true;
	$("#saveSendBtn").val("保存修改配送单");
	var tr=data.parentNode; 
	updateOrderObj=tr;
	for(var i=0;i<sendList.length;i++){
		if(sendList[i].sendSeq==sendDate){
			updateSeq=i;
			buttonDeal(sendList[i].sendAdd,"sendAdd",3);
			getPro(sendList[i].getProWay);
			buttonDeal(sendList[i].labelInfo,"labelInfo",2);
			$("#sendNumber").val(sendList[i].sendNumber);
			$("#getCustomer").val(sendList[i].getCustomer);
			if(sendList[i].labelInfo==1){
				$("#labelTr").show();
				$("#labelContent").val(sendList[i].labelContent);
			}else{
				$("#labelTr").hide();
			}
			$("#sendAddress").val(sendList[i].sendAddress);
			$("#getTel").val(sendList[i].getTel);
		}
	}
	$("#a2").show();
	 var offset = $("#a2").offset();
     window.parent.scrollTo(offset.left, offset.top);
}
var updateOrderInfoFlag=false;
var updateOrderInfo;
var updateOrderObj;
//更新订单信息
function orderInfoDetail(data,order){
	updateOrderInfoFlag=true;
	var tr=data.parentNode; 
	updateOrderObj=tr;
	$("#saveOrderBtn").val("保存修改订单");
	for(var i=0;i<orderList.length;i++){
		if(orderList[i].OrderSeq==order){
			updateOrderInfo=i;
			buttonDeal(orderList[i].orderWay,"subsWay",3);
			buttonDeal(orderList[i].productType,"verNum",2);
			buttonDeal(orderList[i].monthChoice,"monthSub",2);
			$("#subscribeCount").val(orderList[i].orderCount);
			if(orderList[i].monthChoice=='0'){
				$("#monthSub0").attr("checked",'true'); 
				$("#monthSub1").removeAttr("checked");
				clearMonthSub(0);
				$("#startTime").val(orderList[i].startTime);
				$("#endTime").val(orderList[i].endTime);
			}else{
				$("#monthSub1").attr("checked",'true'); 
				$("#monthSub0").removeAttr("checked");
				clearMonthSub(1);
				month=[];
				var date_info;
				year=[];
				var date=new Date();
				var timeList=(orderList[i].time2).split(",");
				for(var j=0;j<timeList.length;j++){
					var time_info=timeList[j].split("-");
					time_info[1]=parseInt(time_info[1],10)<10?time_info[1].split("0")[1]:time_info[1];
					if(j==0){
						date_info=parseInt(time_info[0],10)-(date.getFullYear()-2);
					}
					if(year[parseInt(time_info[0],10)-(date.getFullYear()-2)]==null){
						month=[];
					}else{
						month=year[parseInt(time_info[0],10)-(date.getFullYear()-2)];
					}
					month.push(time_info[1]);
					year[parseInt(time_info[0],10)-(date.getFullYear()-2)]=month;
					for(var k=0;k<4;k++){
						month=year[k];
						if(month!=null){
							$("#yearWay"+k).text((parseInt(k,10)+(date.getFullYear()-2))+"（"+month.length+"/13）");
							year_info(k);
						}else{
							$("#yearWay"+k).text((parseInt(k,10)+(date.getFullYear()-2))+"（0/13）");
						}
						for(var l=0;l<13;l++){
							$('input[name="month'+k+'"]:checked').removeAttr("checked");
						}
						if(month!=null){
							for(var i=0;i<month.length;i++){
								$('input[name="month'+k+'"][value="'+month[i]+'"]').attr("checked",'true'); 
							}
						}
					}
				}
			}
		}
	}
	$("#a1").show();
	 var offset = $("#a1").offset();
     window.parent.scrollTo(offset.left, offset.top);
}
var year=new Array(4);
var overYear=0;
//获取不连续月的年
function year_info(data){
	$("#yearWay"+overYear).addClass("sel-box-selected");
	var parentL=$("#yearWay"+data).closest(".paper-years").offset().left,
		thisL=$("#yearWay"+data).offset().left;
	$("#yearWay"+data).nextAll(".paper-monthes").css({"left":parentL-thisL+"px"});
	$("#yearWay"+data).closest(".paper-years").find(".sel-box").removeClass("sel-box-on");
	$("#yearWay"+data).addClass("sel-box-on");
	$("#yearWay"+data).parent(".all-year").addClass("all-year-on").siblings(".all-year").removeClass("all-year-on");
	overYear=data;
}
//获取不连续月的月
var month=new Array();
function month_info(data){
	month=[];
	var date=new Date();
	$('input[name="month'+parseInt(data,10)+'"]:checked').each(function(){ 
	    month.push($(this).val());
	}); 
	year[parseInt(data,10)]=month;
	if(month!=null){
		$("#yearWay"+parseInt(data,10)).text((date.getFullYear()-2+data)+"（"+month.length+"/13）");
	}else{
		$("#yearWay"+parseInt(data,10)).text((date.getFullYear()-2+data)+"（0/13）");
	}
}
//至于不连续月上显示月份详细信息
function showDivHidden(data,seq){
	var index=$("#time1"+seq).html();
	if(index != null && index != "" && index != 'undefined'){
		$("#divHiddenArea"+data).html(index);
		$("#divHiddenArea"+data).css("display","block");
	}
}
//从不连续月上移除焦点
function liveDivHidden(data){
	$("#divHiddenArea"+data).css("display","none");
	$("#divHiddenArea"+data).html("");
}
function pluceSum(){
	for(var i=0;i<2;i++){
		if($("#monthSub"+i).attr("checked")){
			ret.monthChoice=i+"";
		}
	}
    $("#error2").text("");
	$("#error2").css("display","none");
	if(ret.monthChoice=='0'){
		var startTime=$("#startTime").val();
		var endTime=$("#endTime").val();
		if(startTime==""||endTime==""){
			$("#error2").text("您所填写的订阅周期不能为空");
			$("#error2").css("display","block");
			$("#strongInfo").text("");
			return;
		}
		var startT=new Array();
		var endT=new Array();
		startT=startTime.split("-");
		endT=endTime.split("-");
		var sum=0;
		if(parseInt(endT[0],10)<parseInt(startT[0],10)||(parseInt(endT[0],10)==parseInt(startT[0],10)&&parseInt(endT[1],10)<parseInt(startT[1],10))){
			$("#error2").text("您所填写的订阅起始周期不能大于结束周期");
			$("#error2").css("display","block");
			$("#strongInfo").text("");
			return;
		}
		if(parseInt(endT[0],10)==parseInt(startT[0],10)){
			sum=parseInt(endT[1],10)-parseInt(startT[1],10)+1;
		}else{
			sum+=parseInt(endT[1],10)+(12-parseInt(startT[1],10))+1;
			sum+=(parseInt(endT[0],10)-parseInt(startT[0],10)-1)*12;
		}
		$("#strongInfo").css("display","inline");
		$("#strongInfo").text("共"+sum+"期");
	}
}
function listNumber(){
	var listNumberInfo=0;
	for(var i=0;i<orderList.length;i++){
		if(orderList[i].OrderSeq == sendNumIndex){
			listNumberInfo=orderList[i].orderCount;
		}
	}
	var sum=0;
	if(sendList!=null){
		for(var i=0;i<sendList.length;i++){
			if(!(updateFlag && i == updateSeq)){
				sum+=parseInt(sendList[i].sendNumber,10);
			}
		}
	}
	var num=$("#sendNumber").val();
	var reg=/^[0-9]+$/;
	if(!reg.test(num)){
		$("#error4").text("您输入的月配送量必须为数字");
		$("#error4").css("display","block");
		return;
	}
	var index=parseInt(num,10);
	sum+=index;
	$("#ListNumInfo").css("display","inline");
	listNumberInfo=listNumberInfo-sum;
	$("#ListNumInfo").html("&nbsp;&nbsp;&nbsp;&nbsp;本订单剩余数：<strong class='c69c'>"+listNumberInfo+"</strong>本");
}
function finishSubmit(){
	if(sendList!=null){
		for(var i=0;i<orderList.length;i++){
			if(orderList[i].OrderSeq == sendNumIndex){
				orderList[i].sendList=sendList;
			}
		}
	}
	for(var i=0;i<orderList.length;i++){
		if(orderList[i].sendList==null || orderList[i].sendList.length==0){
            $("#succ_tips_lable").text("订单号为"+orderList[i].OrderSeq+"的配送信息为空，请填写配送信息");
            $("#succ_tips_layer").show().centerV();
			return;
		}
	}
	var user={};
	user.username=$("#username").val();
	user.telephone=$("#telephone").val();
	user.contactName=$("#contactName").val();
	user.contactTel=$("#contactTel").val();
	user.company=$("#company").val();
	var fixTel="";
	if($("#fixTel1").val() != "区号"){
		fixTel=$("#fixTel1").val();
	}
    var fixedTel=fixTel+"-"+$("#fixTel2").val()+"-"+$("#fixTel3").val();
	user.fixedTel=fixedTel;
	var textInfo=$("#textInfo").val();
	user.textInfo=textInfo;
	for(var i=0;i<2;i++){
		if($("#suppWay"+i).attr("class").indexOf("sel-box-on")>=0){
	    	user.suppWay=i+"";
	    }
	} 
	 jQuery.startLoading();
	 jQuery.ajax({
		   type: "POST",
		   url: "/ccms1/saveOrderInfoNew",
		   data: {"user":$.toJSON(user),"orderList":$.toJSON(orderList)},
		   dataType: 'json',
		   success: function(data){
			   jQuery.endLoading();
			   if(data!=null && data.errorMsg!=null){
	                $("#succ_tips_lable").text(data.errorMsg);
	                $("#succ_tips_layer").show().centerV();
	                return;
			   }else{
				   window.location.href="/ccms1/comPage?data=supplementOrderInfo";
			   }
		   },
		   error: function(data) {
			   jQuery.endLoading();
			   if(data.errorMsg!=null){
	                $("#succ_tips_lable").text("订单信息入库失败，请重新进行操作");
	                $("#succ_tips_layer").show().centerV();
			   }else{
	                $("#succ_tips_lable").text(data.errorMsg);
	                $("#succ_tips_layer").show().centerV();
			   }
			   return;
		   } 
		   });
}
//导入配送
function insert_send(){
	$("#saveSendBtn").val("保存配送单");
	updateFlag=false;
	infosList=null;
	showNewDiv("import","bottomArea");
	$("#fileInfo").val("");
	$("#result_info1").html(0);
	$("#result_info2").html(0);
	$("#result_info3").html(0);
	var tb = document.getElementById('tal_error');
	var rowNum=tb.rows.length;
	for (var i=0;i<rowNum;i++)
	{
		tb.deleteRow(i);
		rowNum=rowNum-1;
		i=i-1;
	}
}
//导入配送的取消按钮
function calcelButton(){
	closeDiv("import","bottomArea");
}
//导入配送的确认按钮
function importSubmit(){
	$("#result_info1").html(0);
	$("#result_info2").html(0);
	$("#result_info3").html(0);
	var tb = document.getElementById('tal_error');
	var rowNum=tb.rows.length;
	for (var i=0;i<rowNum;i++)
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
			infosList=data.infos;
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
var infosList;
function sureImportButton(){
	if(infosList==null){
		$("#succ_tips_lable").text("您需要对文件进行确认导入操作");
        $("#succ_tips_layer").show().centerV();
		return;
	}
	 for(var i=0;i<infosList.length;i++){
     	sendList.push(infosList[i]);
     }
     orderList[sendNudmIndex].sendList=sendList;
     $("#import").hide();
     $("#bottomArea").hide();
     var tb = document.getElementById('sendListTable2');
 	 var rowNum=tb.rows.length;
 	 for (var i=0;i<rowNum;i++)
 	 {
 		tb.deleteRow(i);
 		rowNum=rowNum-1;
 		i=i-1;
 	 }
 	 if(sendList!=null){
 		var flag=sendList.length;
 		for(var i=0;i<flag;i++){
 		    $col = $("<tr><td width='20%'>"+sendList[i].sendSeq+"</td>" +
 		    		"<td id='sendAddress"+sendList[i].sendSeq+"' width='20%'>"+sendList[i].sendAddress+"</td>" +
 		    	    "<td id='getCustomer"+sendList[i].sendSeq+"' width='20%'>"+sendList[i].getCustomer+"</td>" +
 		    	    "<td id='getTel"+sendList[i].sendSeq+"' width='20%'>"+sendList[i].getTel+"</td>" +
 		    	    "<td><a href='javascript:void(0);' class='edit'  onclick='sendInfoDetail(this,\""+sendList[i].sendSeq+"\")'></a>" +
 		    	    "<a href='javascript:void(0);' class='dusbin' onclick='delColSend(this)'></a></td></tr>");  
 		    $("#sendListTable2").append($col);
 		}
 	 }else{
 		sendList=[];
 	 }
}