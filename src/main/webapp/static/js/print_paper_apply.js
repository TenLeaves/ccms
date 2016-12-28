function merchant(data){
	$("#merchant-edit").show().centerV();
	flag=data;
	if(dataInfo.cust != null){
		 for(var i=0;i<dataInfo.cust.length;i++){
			 if(ret1.num != null && data=='0'){
				 $("#"+dataInfo.cust[i].CUSTOMER_ID).val(ret1.num[i]);
			 }else if(ret2.num != null && data== '1'){
				 $("#"+dataInfo.cust[i].CUSTOMER_ID).val(ret2.num[i]);
			 }else{
				 $("#"+dataInfo.cust[i].CUSTOMER_ID).val("");
			 }
		 }
	}
}
function closeLayer(){
	var error=$("#succ_tips_lable").text();
	$(this).closest(".tips-layer").hide();
	if(error=='本月份的印刷单已申请完成，请进行其他操作'){
		window.location.href="/ccms1/initIndexAfterLogIn";
		return;
	}
}
var flag;
var dataInfo;
function checkInfo(){
	jQuery.ajax({
		   url: "/ccms1/selectPrint?date="+new Date().getTime(),
		   dataType:"json",
		   success: function(data){
			 dataInfo=data;
			 var date=new Date();
			 $("#type1").text(date.getFullYear()+"年中国书法"+"专业版"+"第"+((1+date.getMonth())<10?"0"+(1+date.getMonth()):""+(1+date.getMonth()))+"期");
			 $("#type2").text(date.getFullYear()+"年中国书法"+"普及版"+"第"+((1+date.getMonth())<10?"0"+(1+date.getMonth()):""+(1+date.getMonth()))+"期");
			 $(".month2").text(1+date.getMonth());
			 $(".month3").text(date.getMonth());
			 if(data.list1 == null && data.list2 == null){
	             $("#succ_tips_lable").text("本月份的印刷单已申请完成，请进行其他操作");
	             $("#succ_tips_layer").show().centerV();
				 return;
			 }
			 $("#type1").text(data.year+"年中国书法"+"专业版"+"第"+data.month+"期");
			 $("#type2").text(data.year+"年中国书法"+"普及版"+"第"+data.month+"期");
			 if(data.list1 != null){
				 $("#list1").show();
				 if(data.list1.count1 != null){
					 $("#cust0").text(data.list1.count1);
				 }else{
					 $("#cust0").text(0);
				 }
				 if(data.list1.count3 != null){
					 $("#saleNew0").text(data.list1.count3);
				 }else{
					 $("#saleNew0").text(0);
				 }
				 if(data.list1.count4 != null){
					 $("#giveNew0").text(data.list1.count4);
				 }else{
					 $("#giveNew0").text(0);
				 }
				 $("#sale0").text(data.list1.RETAIL_COUNT);
				 $("#give0").text(data.list1.GIVE_COUNT);
			 }else{
				$("#list1").hide(); 
			 }
			 if(data.list2 != null){
				 $("#list2").show();
				 if(data.list2.count1 != null){
					 $("#cust1").text(data.list2.count1);
				 }else{
					 $("#cust1").text(0);
				 }
				 if(data.list2.count3 != null){
					 $("#saleNew1").text(data.list2.count3);
				 }else{
					 $("#saleNew1").text(0);
				 }
				 if(data.list2.count4 != null){
					 $("#giveNew1").text(data.list2.count4);
				 }else{
					 $("#giveNew1").text(0);
				 }
				 $("#sale1").text(data.list2.RETAIL_COUNT);
				 $("#give1").text(data.list2.GIVE_COUNT);
			 }else{
				 $("#list2").hide();
			 }
			 if(data.cust != null){
				 for(var i=0;i<data.cust.length;i++){
						$col = $("<tr><th>"+data.cust[i].CUSTOMER_NAME+"：</th><td><input type='text' class='ipt-style w120'  id='"+data.cust[i].CUSTOMER_ID+"'/></td></tr>"); 
						$("#tal1").append($col);
				 }
			 }
			totalInfo(0);
			totalInfo(1);
		}
	});
}
var ret1={};
var ret2={};
function sumInfo(){
	var sum=0;
	var data=new Array();
	if(dataInfo.cust != null){
		var reg=/^[1-9][0-9]*$/;
		 for(var i=0;i<dataInfo.cust.length;i++){
			 var num=$("#"+dataInfo.cust[i].CUSTOMER_ID).val();
			 if(num != "" && !reg.test(num)){
                $("#succ_tips_lable").text("您输入的批发商订阅数量必须为数字");
                $("#succ_tips_layer").show().centerV();
				return;
			 }
			data.push(num);
			if(num!=""){
				sum+=parseInt(num,10);
			}
		 }
		 $("#wholesale"+flag).text(sum);
		 if(flag==1){
			 ret2.num=data;
		 }else{
			 ret1.num=data;
		 }
	 }
	totalInfo(0);
	totalInfo(1);
}
function totalInfo(data){
	var reg=/^[1-9][0-9]*$/;
	var paper=$("#paper"+data).val();
	var book=$("#book"+data).val();
	var save=$("#save"+data).val();
	var sum=0;
	if(paper != "" && !reg.test(paper)){
        $("#succ_tips_lable").text("您输入的发行报刊局订阅数量必须为数字");
        $("#succ_tips_layer").show().centerV();
		return;
	}
	if(book != "" && !reg.test(book)){
        $("#succ_tips_lable").text("您输入的国际图书公司订阅数量必须为数字");
        $("#succ_tips_layer").show().centerV();
		return;
	}
	if(save != "" && !reg.test(save)){
        $("#succ_tips_lable").text("您输入的预留订阅数量必须为数字");
        $("#succ_tips_layer").show().centerV();
		return;
	}
	if(paper!=""){
		sum+=parseInt(paper,10);
	}
	if(book!=""){
		sum+=parseInt(book,10);
	}
	if(save!=""){
		sum+=parseInt(save,10);
	}
	sum+=parseInt($("#wholesale"+data).text(),10);
	sum+=parseInt($("#cust"+data).text(),10);
	sum+=parseInt($("#saleNew"+data).text(),10);
	sum+=parseInt($("#giveNew"+data).text(),10);
	ret1.year=dataInfo.year;
	ret1.month=dataInfo.month;
	ret2.year=dataInfo.year;
	ret2.month=dataInfo.month;
	if(data=='0'){
		if(paper!=""){
			ret1.paper=paper;
		}else{
			ret1.paper=0;
		}
		if(book!=""){
			ret1.book=book;
		}else{
			ret1.book=0;
		}
		if(save!=""){
			ret1.list=save;
		}else{
			ret1.list=0;
		}
		ret1.wholesale=$("#wholesale"+data).text();
		ret1.custNum=$("#cust"+data).text();
		ret1.sale=$("#saleNew"+data).text();
		ret1.give=$("#giveNew"+data).text();
		ret1.sum=sum;
		ret1.type='1';
	}else{
		if(paper!=""){
			ret2.paper=paper;
		}else{
			ret2.paper=0;
		}
		if(book!=""){
			ret2.book=book;
		}else{
			ret2.book=0;
		}
		if(save!=""){
			ret2.list=save;
		}else{
			ret2.list=0;
		}
		ret2.wholesale=$("#wholesale"+data).text();
		ret2.custNum=$("#cust"+data).text();
		ret2.sale=$("#saleNew"+data).text();
		ret2.give=$("#giveNew"+data).text();
		ret2.sum=sum;
		ret2.type='2';
	}
	$("#total"+data).text(sum);
}
function addPayReady(data){
	if(dataInfo.list1==null && dataInfo.list2==null){
         $("#succ_tips_lable").text("本月份的印刷单已申请完成，请进行其他操作");
         $("#succ_tips_layer").show().centerV();
		 return;
	}
	totalInfo(0);
	totalInfo(1);
	ret1.cust=dataInfo.cust;
	ret2.cust=dataInfo.cust;
	if(data == '1'){
		ret1.type='1';
		jQuery.ajax({
		   type: "POST",
		   url: "/ccms1/updatePrintDetail",
		   data: {"user":$.toJSON(ret1)},
		   success: function(data){
			   if(data=='success'){
	               $("#succ_tips_lable").text("印刷单申请提交成功");
	               $("#succ_tips_layer").show().centerV();
				   window.location.href="/ccms1/printTable";
			   }else{
	               $("#succ_tips_lable").text("印刷单申请提交失败");
	               $("#succ_tips_layer").show().centerV();
			   }
		   }
		});
	}
	if(data == '2'){
		ret2.type='2';
		jQuery.ajax({
		   type: "POST",
		   url: "/ccms1/updatePrintDetail",
		   data: {"user":$.toJSON(ret2)},
		   success: function(data){
			   if(data=='success'){
	               $("#succ_tips_lable").text("印刷单申请提交成功");
	               $("#succ_tips_layer").show().centerV();
				   window.location.href="/ccms1/printTable";
			   }else{
	               $("#succ_tips_lable").text("印刷单申请提交失败");
	               $("#succ_tips_layer").show().centerV();
			   }
		   }
		});
	}
}