// 弹出div层和遮罩层
function showDiv(show_div, bg_div){
    //$("#"+ show_div).show();
    //$("#" + bg_div).show();
    $("#" + bg_div).height(pageHeight());
    $("#" + bg_div).width(pageWidth());
    $("#" + show_div).css('display', 'block');
    $("#" + bg_div).css('display', 'block');
}

// 关闭弹出层
function closeDiv(show_div, bg_div){
    //$("#"+ show_div).hide();
    //$("#" + bg_div).hide();
    $("#" + show_div).css('display', 'none');
    $("#" + bg_div).css('display', 'none');
}

/* 当前页面高度 */
function pageHeight() {
    return document.body.scrollHeight;
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


