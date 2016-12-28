function changePass(){
	$('#errorId').text('');
	var input1=$('#prePassId').val();
	var input2=$('#newPassId').val();
	var input3=$('#passconfirm').val();
	
	if(input1==''){
		$('#errorId').text('原密码不能为空');
		$('#errorId').show();
		return;
	}
	if(input2==''){
		$('#errorId').text('新密码不能为空');
		$('#errorId').show();
		return;
	}
	if(input3==''){
		$('#errorId').text('请输入新密码确认');
		$('#errorId').show();
		return;
	}
	if(input3!=input2){
		$('#errorId').text('两次输入密码不一致，请重新输入');
		$('#errorId').show();
		return;
	}
	
	jQuery.ajax({
		type : 'POST',
		url : '/ccms1/changePass',
		dataType : 'json',
		data : {
			"input1" : input1,
			"input2" : input2,
			"input3" : input3
		},
		success : function(data)// 当请求成功时触发函数
		{
			
			if(data.tag=='0'){
				$('#errorId').text('修改成功,请牢记您的密码：'+data.msg+'           关闭后请重新登录');
				$('#errorId').show();
				$('#next-step1').hide();
				$('#exitBtn').show();
			}else{
				$('#errorId').text(data.msg);
				$('#errorId').show();
			}
			
		}
	});
}
function closeThisWin(){
	window.close();
}