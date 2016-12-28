$(function(){
	$('input').bind('click',function(){
		$(this).css("color","black");
	});
	
	/*
	$('#username').keypress(function(event) {
		var key = event.which;
		if (key >= 97 && key <= 122) {
		event.preventDefault();
		$(this).val($(this).val() + String.fromCharCode(key - 32));
		}
	});*/
		
		
	
});

function changeCode(){
	var location = '/ccms1/checkcode?'+new Date().toLocaleString();
	$('#checkcodeImage').attr('src',location);
}
function num_check(){
	var num=$("#num_reg").val();
	jQuery.ajax({ url:"/ccms1/check?num="+num,   //数据请求页面url 
	     type:"post",  //数据传递方式(get或post) 
	     success:function(data)//当请求成功时触发函数 
	     {  
	      if(data=='fail'){
	    	  $("#errorInfo").text("您输入的验证码有误");
	      }else{
	    	  $("#errorInfo").text("");
	      }
	     }
	});
}
function checkInfo(){
	var username=$("#username").val();
	if(username==null||username==''){
		alert("您输入的用户名不能为空");
		return false;
	}
	return true;
}