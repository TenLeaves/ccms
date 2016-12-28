$(function(){
	// 选择框选择
	$(".sel-box").click(function(){
		$(this).addClass("sel-box-on").siblings().removeClass("sel-box-on");
	});
	// 弹出层关闭
	$(".layer-close,.layer-sure,.layer-cancel").click(function(){
		$(this).closest(".layer").hide();
	});
	// 提示层关闭
    $(".tips-layer-sure").click(function(){
        $(this).closest(".tips-layer").hide();
    });
});

//弹出层垂直居中
;(function($){
    $.fn.centerV=function(){
        var scrollTop=window.parent?$(window.parent.document).scrollTop():$(document).scrollTop(),
            windowH=window.parent?$(window.parent).height():$(window).height();
        var top=scrollTop+(windowH- this.outerHeight())/2;
        if(top<20) {top=20;}
        return this.css({"top":top+"px"});
    };
})(jQuery);

//弹出div层和遮罩层
function showDiv(show_div, bg_div){
    $("#" + bg_div).height(1152);
    $("#" + bg_div).width(pageWidth());
    $("#" + show_div).show();
    $("#" + bg_div).show();
    
    adjust(show_div);
    
}

function showNewDiv(show_div, bg_div){
	$("#" + bg_div).height(1152);
    $("#" + bg_div).width(pageWidth());
    $("#" + bg_div).show();
    
	$("#" + show_div).show().centerV();
}

// 关闭弹出层
function closeDiv(show_div, bg_div){
    $("#" + show_div).hide();
    $("#" + bg_div).hide();
}

/* 当前页面高度 */
function pageHeight() {
    if(document.body.scrollHeight <= document.documentElement.clientHeight){
        return document.documentElement.clientHeight;
    }else{
        return document.body.scrollHeight;
    }
}

/* 当前页面宽度 */
function pageWidth() {
    return document.body.scrollWidth;
}

/* 定位到页面中心 */
function adjust(id) {
    var w = $("#" + id).width();
    var h = $("#" + id).height();
    var t = scrollY() + (windowHeight()/2) - (h/2);
    if(t < 0) t = 0;
    
    var l = scrollX() + (windowWidth()/2) - (w/2);
    if(l < 0) l = 0;
    $("#" + id).css({left: l+'px', top: t+'px'});
}

//浏览器视口的高度
function windowHeight() {
    var de = document.documentElement;

    return self.innerHeight || (de && de.clientHeight) || document.body.clientHeight;
}

//浏览器视口的宽度
function windowWidth() {
    var de = document.documentElement;

    return self.innerWidth || (de && de.clientWidth) || document.body.clientWidth
}

/* 浏览器垂直滚动位置 */
function scrollY() {
    var de = document.documentElement;

    return self.pageYOffset || (de && de.scrollTop) || document.body.scrollTop;
}

/* 浏览器水平滚动位置 */
function scrollX() {
    var de = document.documentElement;

    return self.pageXOffset || (de && de.scrollLeft) || document.body.scrollLeft;
}


