;(function($){
    $.fn.Tab=function(op){
        var op = $.extend({},$.fn.Tab.def,op);
        return this.each(function(i,dom){
            var $this=$(this);
            var tabLi=$this.find(op.li),tabContext=$this.find(op.context);
            tabLi[op.type](function(){
                var $li=$(this),index=tabLi.index($li);
                tabLi.removeClass(op.current);
                $li.addClass(op.current);
                tabContext.hide().eq(index).show();
            });
        });
    }
    $.fn.Tab.def={"current":"current","type":"click","li":".tab-li","context":".tab-context"};

    //可选options = {"position":"fixed"}绝对剧中。
    $.fn.center = function(options){
      var _this=$(this),options=$.extend({},$.fn.center.defaults,options);
      if(_this.length<=0)return _this;
      var top = ($(window).height() - _this.height())/2,left = ($(window).width() - _this.width())/2;
      var scrollTop = $(document).scrollTop(),scrollLeft = $(document).scrollLeft();
      top=top<0?20:top;
      if($.browser.msie&&($.browser.version == "6.0")&&!$.support.style ){
           options.position="absolute";top=top+scrollTop;
      }else if(options.position=="fixed"){
           top = ((top/$(window).height())*100)+"%";
      }else{
          top = top+scrollTop;
      }
      return this.css( { position : options.position, 'top' : top , left : left + scrollLeft } );
    }
    $.fn.center.defaults ={"position":"absolute"}
})(jQuery);