$(function(){
    // 切换tab签
    $("#tablist li").click(function(){
        var $this=$(this),index=$this.index();
        $this.addClass("on").siblings().removeClass("on");
        $(".tabs-cont",".sec-box").eq(index).show().siblings().hide();
    });

    // 选择文件
   /*  $("#file-btn").click(function(){
        $(this).prev("input[type='file']").trigger("click");
    }); */

    // 查看订购详情
    /* $(".detail-btn").live("click",function(){
        alert(1);
        $("#customer-layer").show();
    }); */

    // 修改订购信息
    $(".edit-btn").live("click",function(){
        $("#customer-edit").show();
    });
});

var custType = "1"; // 增加客户时，客户类型 初始化 为 1 大客户 
var sel_custType = "1"; // 查询客户时，客户类型 初始化 为 1 大客户 
var mod_custType = "1"; // 更新客户时的客户类型

// 增加客户时  客户类型进行切换  1 大客户  2 批发商
function custTypeChange(type){
	custType=type;
}

// 查询客户时，客户类型进行切换  1 大客户  2 批发商
function sel_custTypeChange(type){
	sel_custType=type;
}

// 修改客户时，客户类型进行切换  1 大客户  2 批发商
function mod_custTypeChange(type){
    if(type == 1){
        $("#mod_custType").val("1");
    }else{
        $("#mod_custType").val("2");
    }
}

// 参数校验
function validate(type){
    
    if($("#" + type +"complany").val() == ""){
        $("#" + type +"complany_err").text("订购单位不能为空");
        return false;
    }else{
        $("#" + type +"complany_err").text("");
    }
    if($("#" + type + "name").val() == ""){
        $("#" + type +"name_err").text("联系人不能为空");
        return false;
    }else{
        $("#" + type +"name_err").text("");
    }
    
    if($("#" + type +"phone").val() == ""){
        $("#" + type +"phone_err").text("联系人手机不能为空");
        return false;
    }else{
        $("#" + type +"phone_err").text("");
    }
    
    var phone = $("#" + type + "phone").val();
    var reg=/^1[0-9]+$/;
    if(phone.length!=11 || !reg.test(phone)){
        $("#" + type +"phone_err").text("联系人手机必须为1开头的11位数字");
        return false;
    }else{
        $("#" + type +"phone_err").text("");
    }
    
    var reg=/^[0-9]+$/;
    
    var fixTel1=$("#" + type + "fixTel1").val();
    var fixTel2=$("#" + type + "fixTel2").val();
    var fixTel3=$("#" + type + "fixTel3").val();
    if(fixTel1!="" || fixTel2!="" || fixTel3!=""){
        if( !(fixTel1 == '' || reg.test(fixTel1)) ){
            $("#" + type +"fixTel_err").text("固话必须为数字");
            return false;
        }else if( !(fixTel2 == '' || reg.test(fixTel2)) ){
            $("#" + type +"fixTel_err").text("固话必须为数字");
            return false;
        }else if( !(fixTel3 == '' || reg.test(fixTel3)) ){
            $("#" + type +"fixTel_err").text("固话必须为数字");
            return false;
        }else{
            $("#" + type +"fixTel_err").text("");
        }
    }else{
        $("#" + type +"fixTel_err").text("");
    }
    
    var discount = $("#" + type + "discount").val();
    if(discount != ""){
        reg=/^[0-9]{1,2}$/;
        if(!reg.test(discount)){
            $("#" + type +"discount_err").text("折扣率格式不正确，格式为8或者78,80");
            return false;
        }else{
            $("#" + type +"discount_err").text("");
        }
    }
    
    return true;
}

// 增加客户提交前 参数准备
function addCust(){
    
    //参数校验
    if(!validate("")){
        return false;
    }
    
    var fixedTel=$("#fixTel1").val()+"-" + $("#fixTel2").val() + "-" + $("#fixTel3").val();
    $('#fixedPhone').val(fixedTel);
    
    // 客户类型
    $('#custType').val(custType);
    
    jQuery.startLoading();
    
    $('#addCustInfo').ajaxSubmit({
        success: function (data) {
            
            jQuery.endLoading();
            
            if (data.status == "success") {
                
                $("#succ_tips_lable").text("添加客户信息成功");
                $("#succ_tips_layer").show().centerV();
                
                $('#addCustInfo')[0].reset();
                
             }else{
                 
                 $("#fail_tips_lable").text(data.failInfo);
                 $("#fail_tips_layer").show().centerV();
             }
        }
    });
}

// 分页查询客户信息
function clickAjax(){
    
    $('#pagesList').ajaxSubmit({
        success: function (result) {
            
            $('#pagesList').empty().html(result);
        }
    });
    
}

// “查询”按钮查询所有客户信息
function selAllCust(){
    $('#sel_custType').val(sel_custType);
    
    jQuery.startLoading();
    
    $('#selCusts').ajaxSubmit({
        success: function (result) {
            
            jQuery.endLoading();
            
            $('#pagesList').empty().html(result);
        }
    });
    
}

// 展示客户详情
function showDetails(showid, bgid, curObject){
    
    var tdObjects = $(curObject).closest("tr").find("td");
    $("#det_company").text($(tdObjects[7]).text());
    $("#det_name").text($(tdObjects[8]).text());
    $("#det_custTypeDes").text($(tdObjects[9]).text());
    $("#det_phone").text($(tdObjects[10]).text());
    $("#det_discount").text($(tdObjects[2]).text());
    $("#det_contactName").text($(tdObjects[4]).text());
    $("#det_contactPhone").text($(tdObjects[5]).text());
    $("#det_contactRemark").text($(tdObjects[6]).text());  
    
    // 固定电话展现
    var fixedPhone = $(tdObjects[3]).text();
    var strs = fixedPhone.split("-");
    var a = strs[0];
    var b = strs[1];
    var c = strs[2];
    var fixedTel= a==""?"":(a+"-");
    fixedTel+=b;
    fixedTel+=c==""?"": ("-" + c);
    $("#det_fixedPhone").text(fixedTel);
    
    showDiv(showid, bgid);
    
}

// 弹出修改客户
function showModify(showid, bgid, curObject){
    var tdObjects = $(curObject).closest("tr").find("td");
    $("#mod_custId").val($(tdObjects[0]).text());
    $("#mod_complany").val($(tdObjects[7]).text());
    $("#mod_name").val($(tdObjects[8]).text());
    $("#mod_phone").val($(tdObjects[10]).text());
    $("#mod_discount").val($(tdObjects[2]).text());
    $("#mod_contactName").val($(tdObjects[4]).text());
    $("#mod_contactPhone").val($(tdObjects[5]).text());
    $("#mod_contactRemark").val($(tdObjects[6]).text());
    // 客户类型
    var custTypeCode = $(tdObjects[1]).text();
    $("#mod_custType").val(custTypeCode);
    if(custTypeCode == 1){
        mod_custTypeChange(1);
    }else{
        mod_custTypeChange(2);
    }
    // 固定电话号码
    var fixedPhone = $(tdObjects[3]).text();
    var strs = fixedPhone.split("-");
    var a = strs[0];
    var b = strs[1];
    var c = strs[2];
    $("#mod_fixTel1").val(a);
    $("#mod_fixTel2").val(b);
    $("#mod_fixTel3").val(c);
    
    $("#mod_complany_err").text("");
    $("#mod_name_err").text("");
    $("#mod_phone_err").text("");
    $("#mod_fixTel_err").text("");
    $("#mod_discount_err").text("");
    showDiv(showid, bgid);

}
function showRemove(obj){
    var choice = window.confirm("您确定删除这条客户信息？");
    if(!choice){
    	return;
    }
	var tdObjects = $(obj).closest("tr").find("td");
    var custId=$(tdObjects[0]).text();
    jQuery.startLoading();
    
    $.ajax({
        url: "/ccms1/deleteCust?custId="+custId,
        type:"post",
        cache: false,
        success: function (result) {
            
            jQuery.endLoading();
            
            $('#pagesList').empty().html(result);
        }
    });
}
// 修改客户信息 ajax提交
function updateCust(){
    
    //参数校验
    if(!validate("mod_")){
        return false;
    }
    
    var custInfo={};
    custInfo["custId"]= $("#mod_custId").val();
    custInfo["name"]=$("#mod_name").val();
    custInfo["phone"]=$("#mod_phone").val();
    custInfo["complany"]=$("#mod_complany").val();
        
    
    // 大客户类型
    custInfo["custType"]=$("#mod_custType").val();
    
    // 固定电话
    var fixedTel=$("#mod_fixTel1").val()+"-" + $("#mod_fixTel2").val() + "-" + $("#mod_fixTel3").val();
    custInfo["fixedPhone"]=fixedTel;
    
    custInfo["discount"]=$("#mod_discount").val();
    custInfo["contactName"]=  $("#mod_contactName").val();
    custInfo["contactPhone"]=$("#mod_contactPhone").val();
    custInfo["remark"]= $("#mod_contactRemark").val();
    
    jQuery.startLoading();
    
    $.ajax({
        url: "/ccms1/updateCust",
        data: custInfo,
        type: "POST",
        dataType: "json",
        cache: false,
        success: function (data) {
            if (data.status == "success") {
               jQuery.endLoading();
               // 关闭修改窗口
               closeDiv("customer-edit","bgOver");
               
               $("#succ_tips_lable").text("修改成功");
               $("#succ_tips_layer").show().centerV();
               
               // 修改成功后，重新查询页面
               clickAjax();
               
            }else{
                
               $("#fail_tips_lable").text("修改失败，请重新修改");
               $("#fail_tips_layer").show().centerV();
            }
            
        }
    });
}   
    
    var importFlag = 0; // 0 未导入 1 已导入
    
    // 分页查询批量导入客户错误信息
    function errorAjax(){
        
        $('#errorList').ajaxSubmit({
            success: function (result) {
                $('#errorList').empty().html(result);
            }
        });
        
    }
    
    // 导入客户信息
    function importCust(){
        jQuery.startLoading();
        
        $('#importCust').ajaxSubmit({
            type: "POST",
            dataType:'json',
            success: function (data) {
                jQuery.endLoading();
                if (data.status == "success") {
                    $("#totalSize").text(data.totalSize);
                    $("#succSize").text(data.rightSize);
                    $("#errorSize").text(data.errorSize);
                    // 修改成功后，重新错误信息
                    importFlag = 1;
                    errorAjax();
                    
                 }else{
                    importFlag = 0;
                    alert(data.error);
                 }
            }
        });
        
    }
    
    // 导出错误信息
    function exportError(){
        if("0" ==  importFlag){
            $("#fail_tips_lable").text("先导入数据");
            $("#fail_tips_layer").show().centerV();
            return false;
        }else{
            return true;
        }
    }