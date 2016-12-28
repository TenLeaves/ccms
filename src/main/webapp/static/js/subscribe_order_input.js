$(function(){
	// 从保存客户中查找
	$("#append").click(function(){
		$("#customer-layer").show();
	})
	// 下一步
	$(".next-step").click(function(){
		var $this=$(this),index=$this.attr("id").slice(-1);
		$(".processing").hide();
		$("#process"+index).show();
		$(".process li").removeClass().eq(index).addClass("on").prevAll().addClass("visited");

	})
	// 上一步
	$(".pre-step").click(function(){
		var $this=$(this),index=$this.attr("id").slice(-1);
		$(".processing").hide();
		$("#process"+index).show();
		$(".process li").removeClass().eq(index).addClass("on").prevAll().addClass("visited");
	})
	

	// 订阅周期非连续月切换
	$(".paper-years .sel-box").click(function(){
		var $this=$(this);
		var parentL=$this.closest(".paper-years").offset().left,
			thisL=$this.offset().left;
		$this.nextAll(".paper-monthes").css({"left":parentL-thisL+"px"});
		$this.closest(".paper-years").find(".sel-box").removeClass("sel-box-on")
		$this.addClass("sel-box-on");
		$this.parent(".all-year").addClass("all-year-on").siblings(".all-year").removeClass("all-year-on")

	});
})