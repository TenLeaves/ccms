function showDetail(){
	var subscribeId=$("#subscribeId").text();
	window.showModalDialog("/ccms1/toOrderEdit?subscribeId="+subscribeId+"&key=1",window,"dialogWidth=1000px;dialogHeight=500px");
}
function insertOrder(){
	var data=$("#hidden").val();
	window.location.href = "/ccms1/"+data;
}