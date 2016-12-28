var stockPram = {}; // 查询参数

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
var month="";
function changeDate(){
    if($("#updateTime").val()!= ""){
    		$("#changeYear").show();
    		$("#year").text($("#updateTime").val());
    		$("input[name='month']").attr('checked', false);
    		month="";
    }else{
    	$("#changeYear").hide();
    }
}
//查询
function sel(){
	for(var i=0;i<3;i++){
	    	if($("#productType"+i).attr("class").indexOf("sel-box-on")>=0){
	    		stockPram.productType = i+"";
	    	}
	} 
	for(var i=0;i<4;i++){
	    	if($("#stockType"+i).attr("class").indexOf("sel-box-on")>=0){
	    		stockPram.stockType = i+"";
	    	}
	}
	var year="0";
	if($("#updateTime").val() != ""){
		year=$("#updateTime").val();
		$("input[name='month']:checked").each(function(){
			month += (parseInt($(this).val(), 10)<10?"0"+$(this).val():""+$(this).val()) + ",";
		});
		if(month != ""){
			month =  month.substring(0, month.length-1);
		}
	}
    stockPram.year = year;
    stockPram.month = month;
    
    jQuery.startLoading();
    
    $.ajax({
        url: "/ccms1/selAllStock",
        data: {"stockPram": $.toJSON(stockPram)},
        type: "POST",
        cache: false,
        success: function (result) {
            jQuery.endLoading();
            $('#stockList').empty().html(result);
        }
    });
}



