$(function(){
	// 菜单折叠
	$(".nav").click(function(){
		var $this=$(this);
		$this.find(".sub-navs").slideDown().end().siblings().find(".sub-navs:visible").slideUp();
	})
	// 根据菜单切换右侧页面
	$(".sub-navs a").click(function(){
		var $this=$(this),href=$this.attr("href");
		if(href=="#") return false;
		$(".sub-navs a").removeClass("selected");
		$this.addClass("selected");
	})
})