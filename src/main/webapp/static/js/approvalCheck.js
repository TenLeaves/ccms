	var approvalState;
	var approvalType;
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
	function selectBtn(){
		for(var i=0;i<5;i++){
			if($("#appType"+i).attr("class").indexOf("sel-box-on")>=0){
				approvalType=i;
			}
		}
		for(var i=0;i<4;i++){
			if($("#appWay"+i).attr("class").indexOf("sel-box-on")>=0){
				approvalState=i;
			}
		}
		jQuery.startLoading();
		$('#selCusts').ajaxSubmit({
			data:{"approvalState":approvalState,"approvalType":approvalType},
	        success: function (result) {
	        	jQuery.endLoading();
	        	$('#pagesList').empty().html(result);
	        }
	    });
	}
	function showDetail(obj){
		var tr=$(obj).closest("tr").find("td");
		var list=$(tr[0]).html();
		if(approvalType==1){
			window.showModalDialog("/ccms1/toPrintDetail?subscribeId="+list,window,"dialogWidth=1000px;dialogHeight=500px");
		}else{
			window.showModalDialog("/ccms1/toOrderEdit?subscribeId="+list+"&key=1",window,"dialogWidth=1000px;dialogHeight=500px");
		}
	}
    
