/* 
 *jQuery openOverlay Plugin
 * version: 1.01 (12-DEC-2009)
 * @requires jQuery 
 *
 * Examples and documentation at: http://www.hardcode.nl/demos/overlay.html
 * Dual licensed under the MIT and GPL licenses:
 *   http://www.opensource.org/licenses/mit-license.php
 *   http://www.gnu.org/licenses/gpl.html
 *
 */

(function($) {
    var startTimer = function(popupMsg) {
        popupMsg.everyTime('1s', function() {
            var span = $(this).find('.alertPopMsgTimer span');
            span.text(parseInt(span.text()) + 1);
        });
    };

    var popupMsg = function(/* String */popClass, /* String */msg) {
        var docbody = $(document.body);
        docbody.append('<div class="' + popClass + '" id="popupMsgId">' + '<div class="popupMsg">'
                + msg + '</div>' + '<span class="alertPopMsgTimer"><span>0</span>秒</span>'
                + '</div>');

        var popupMsg = $('#popupMsgId');
        popupMsg.openOverlay( {
            zIndex : 2000
        });

        startTimer(popupMsg);

        // use setTimeout to fix ie focus problem.
        setTimeout(function() {
            popupMsg.focus();
        }, 10);
    };

    var closePopup = function(popupMsg) {
        var costSeconds = popupMsg.find('.alertPopMsgTimer span').text();
        popupMsg.stopTime();
        popupMsg.closeOverlay().remove();
        return parseInt(costSeconds);
    };

    var popdownMsg = function() {
        var popupMsg = $('#popupMsgId');
        var costSeconds = closePopup(popupMsg)
        return costSeconds;
    };

    var popupFn = function(/* String */popClass, /* String */msg, /* Boolean */confirm, /* Function */
    callback,/* String */btnname) {
        
        var flag = (btnname!=undefined);

        var docbody = $(document.body);
        if ($('#popupMsgId').length) {
            return;
        }
        
        var dochtml = '<div class="' + popClass + '" id="popupMsgId">' + '<div class="popupMsg">'
                    + msg + '</div>' + '<span class="alertPopMsgTimer"><span>0</span>秒</span>'
                    + '<div class="alertPopMsgBtn">确定</div>'
                    + (confirm ? '<div class="alertPopMsgBtn">取消</div>' : '');
        
        if(flag)
            dochtml = dochtml + (flag ? '<div class="alertPopMsgBtn">'+btnname+'</div>' : '');
        
        dochtml = dochtml+'</div>';
        
        docbody.append(dochtml);

        var popupMsg = $('#popupMsgId');
        var waringbtn = popupMsg.find('.alertPopMsgBtn');

        popupMsg.openOverlay( {
            zIndex : 2000
        });

        startTimer(popupMsg);

        var closeFunction = function(action) {
            if(action!="other")
                var costSeconds = closePopup(popupMsg);
            callback && callback(action, costSeconds);
        };

        waringbtn.eq(0).click(function() {
            closeFunction('ok');
        }).keypress(function(e) {
            var code = (e.keyCode ? e.keyCode : e.which);
            if (code == 13 || code == 32) {
                closeFunction('ok');
            }
        });

        waringbtn.eq(1).click(function() {
            closeFunction('cancel');
        });
        
        if(flag){
            waringbtn.eq(2).click(function() {
                closeFunction('other');
            });
        }

        // use setTimeout to fix ie focus problem.
        setTimeout(function() {
            waringbtn.eq(0).focus();
        }, 10);
    };

    $.popup = function(/* String */msg) {
        popupMsg("alertPop", msg);
    };

    $.popdown = function() {
        return popdownMsg();
    };

    $.alert = function(/* String */msg, /* Function? */callback) {
        popupFn("alertPop", msg, false, callback);
    }

    $.smile = function(/* String */msg, /* Function? */callback) {
        popupFn("smilePop", msg, false, callback);
    }

    $.confirm = function(/* String */msg, /* Function? */callback) {
        popupFn("confirmPop", msg, true, callback);
    }

    $.confirm2 = function(/* String */msg, /* Function? */callbackOK, /* Function? */
    callbackCancel) {
        popupFn("confirmPop", msg, true, function(ret, costSeconds) {
            if (ret == 'ok') {
                callbackOK && callbackOK('ok', costSeconds);
            } else if (ret == 'cancel') {
                callbackCancel && callbackCancel('cancel', costSeconds);
            }
        });
    }
    
    $.confirm3 = function(/* String */msg,/* String */btnname,/* Function? */callback) {
        popupFn("confirmPop", msg, true, callback, btnname);
    }
    
    $.confirm4 = function(/* String */msg,/* String */btnname,/* Function? */callbackOK,/* Function? */callbackCancel,/* Function? */callbackOther) {
        popupFn("confirmPop", msg, true, function(ret, costSeconds) {
            if (ret == 'ok') {
                callbackOK && callbackOK('ok', costSeconds);
            } else if (ret == 'cancel') {
                callbackCancel && callbackCancel('cancel', costSeconds);
            } else if (ret == 'other') {
                callbackOther && callbackOther('other', costSeconds);
            }
        }, btnname);
    }

    $.prompt = function(/* String */msg, /* Function? */callback) {
        var docbody = $(document.body);
        docbody
                .append('<div class="promptPop" id="popupMsgId" style="position:absolute;top:20%;left:30%;">'
                        + '<div class="alertPopMsg">'
                        + msg
                        + '</div>'
                        + '<input type="text" style="position:absolute;top:23%;left:33%;"/>'
                        + '<span class="alertPopMsgTimer"><span>0</span>秒</span>'
                        + '<div class="alertPopMsgBtn">确定</div><div class="alertPopMsgBtn">取消</div></div>');

        var popupMsg = $('#popupMsgId');
        var waringbtn = popupMsg.find('.alertPopMsgBtn');
        var inputText = popupMsg.find(':text');

        popupMsg.openOverlay( {
            zIndex : 2000
        });

        startTimer(popupMsg);

        var closeFunction = function(inputText) {
            var costSeconds = closePopup(popupMsg);
            callback && callback(inputText, costSeconds);
        };

        waringbtn.eq(0).click(function() {
            closeFunction(inputText.val());
        });

        inputText.keypress(function(e) {
            var code = (e.keyCode ? e.keyCode : e.which);
            if (code == 13) {
                closeFunction(inputText.val());
            }
        });

        waringbtn.eq(1).click(function() {
            closeFunction(null);
        });

        // use setTimeout to fix ie focus problem.
        setTimeout(function() {
            inputText.focus();
        }, 10);
    };

    $.startLoading = function(/* String ? */waitingTips) {
        var docbody = $(document.body);
        var tips = waitingTips || '请稍候...';
        docbody
                .append('<div class="loadingPop" id="loadingId">' + tips + '(<B>0</B>秒)</div>');
        var popupMsg = $('#loadingId');
        popupMsg.openOverlay( {
            zIndex : 3000
        });
        popupMsg.everyTime('1s', function() {
            var span = $(this).find('B');
            span.text(parseInt(span.text()) + 1);
        });
    };

    $.endLoading = function() {
        var popupMsg = $('#loadingId');
        var costSeconds = popupMsg.find('span').text();
        popupMsg.stopTime();
        popupMsg.closeOverlay().remove();
        return parseInt(costSeconds);
    };
    
    $.startLoading2 = function(/* String ? */waitingTips) {
        var docbody = $(document.body);
        var tips = waitingTips || '请稍候...';
        docbody
                .append('<div class="loadingPop" id="loadingId2">' + tips + '(<span>0</span>秒)</div>');
        var popupMsg = $('#loadingId2');
        popupMsg.openOverlay( {
            zIndex : 3000
        });
        popupMsg.everyTime('1s', function() {
            var span = $(this).find('span');
            span.text(parseInt(span.text()) + 1);
        });
    };
    

    $.endLoading2 = function() {
        var popupMsg = null==$('#loadingId2')?$('#loadingId2'):$('#loadingId2');
        var costSeconds = popupMsg.find('span').text();
        popupMsg.stopTime();
        popupMsg.closeOverlay().remove();
        return parseInt(costSeconds);
    };

    $.fn.enter = function(callback) {
        $(this).keypress(function(e) {
            var code = (e.keyCode ? e.keyCode : e.which);
            if (code == 13) {
                callback && callback();
            }
        });
    };

    $.fn.hasOverlay = function() {
        var modalwindow = $(this);
        var overlayLayerID = $(this).attr('id') + '_overlayLayer';
        if ($('#' + overlayLayerID).length) {
            return true;
        }
        false;
    };

    $.fn.openOverlay = function(options) {
        var modalwindow = $(this);
        var overlayLayerID = $(this).attr('id') + '_overlayLayer';
        if ($('#' + overlayLayerID).length) {
            return modalwindow;
        }

        // build main options before element iteration
        var opts = $.extend( {}, $.fn.openOverlay.defaults, options);

        markLayer(overlayLayerID, opts); 

        overlayLayer = $('#' + overlayLayerID);
        fillrUp(modalwindow, overlayLayer, opts);
        $(window).bind('scroll.overlay.' + overlayLayerID, function() {
            fillrUp(modalwindow, overlayLayer, opts);
        }).bind('resize.overlay.' + overlayLayerID, function() {
            fillrUp(modalwindow, overlayLayer, opts);
        });

        // append each matched element to the modalwindow
        var iLength = this.length;
        return this.each(function(i) {
            // occ.append(this);
                if (i === iLength - 1) positionOverlayContent(modalwindow, opts);

            });
    };


    $.fn.reLoad = function(options) {

        // 当前层对象
        var modalwindow = $(this);
        modalwindow.closeOverlay();
        var opts = $.extend( {}, $.fn.openOverlay.defaults, options);
        var overlayLayerID = $(this).attr('id') + '_overlayLayer';

        var layer = $('#' + overlayLayerID);
        if(layer.length){
            layer.remove();
        }

        markLayer(overlayLayerID, opts);

        var overlayLayer = $('#' + overlayLayerID);

        fillrUp(modalwindow, overlayLayer, opts);
        $(window).bind('scroll.overlay.' + overlayLayerID, function() {
            fillrUp(modalwindow, overlayLayer, opts);
        }).bind('resize.overlay.' + overlayLayerID, function() {
            fillrUp(modalwindow, overlayLayer, opts);
        });
    }

    
    var markLayer = function(overlayLayerID, opts) {
        var h = $(window).height();
        var w = $(window).width();
        var docbody = $(document.body);

        if (!h) h = 10000;

        docbody
                .append('<div id="'
                        + overlayLayerID
                        + '" style="'
                        + 'text-align:center; z-index:'
                        + opts.zIndex
                        + '; position:absolute; top:0px; bottom:0px; left:0px; right:0px;'
                        + 'opacity: 0.'
                        + opts.iOpacity
                        + ';'
                        + '-ms-filter:progid:DXImageTransform.Microsoft.Alpha(Opacity='
                        + opts.iOpacity
                        + ');'
                        + 'filter: alpha(opacity='
                        + opts.iOpacity
                        + ');'
                        + 'background:'
                        + opts.sColor
                        + ';'
                        + 'height:'
                        + h
                        + 'px">'
                        + '<iframe style="position:absolute;left:0;top:0;width:100%;height:100%;z-index:1;filter:progid:DXImageTransform.Microsoft.Alpha(opacity=30);" scrolling="no"></iframe>'
                        + '</div>');
    }

    // private functions

    function positionOverlayContent(o, opts) {
        var h = $(window).height();
        var w = $(window).width();
        // alert(h);
        if (!h) h = 300;
        o.css( {
            marginTop : h / 2 - o.outerHeight() / 2 + $(window).scrollTop() + 'px',
            marginLeft : w / 2 - o.outerWidth() / 2 + $(window).scrollLeft() + 'px',
            left : '0',
            top : '0',
            // width:opts.width,
            position : 'absolute',
            zIndex : opts.zIndex + 1,
            display : 'block'
        });

        if (opts.width) {
            o.css('width', opts.width);
        }
    }
    function fillrUp(o, overlayLayer, opts) {
        var h = $(window).height();
        var w = $(window).width();
        if (h) {
            overlayLayer.height(h + $(window).scrollTop() + 16);
            overlayLayer.width(w + $(window).scrollLeft() + 16);
        } else {
            overlayLayer.css( {
                top : $(window).scrollTop() + 'px',
                left : $(window).scrollLeft() + 'px'
            });
        }
        positionOverlayContent(o, opts)
    }

    function debug($obj) {
        if (window.console && window.console.log)
            window.console.log('openoverlay selection count: ' + $obj.size());
    }
    ;
    // define and expose public functions

    $.fn.closeOverlay = function() {
        var modalwindow = $(this);
        var overlayLayerID = modalwindow.attr('id') + '_overlayLayer';

        $(window).unbind('scroll.overlay.' + overlayLayerID).unbind(
                'resize.overlay.' + overlayLayerID);

        var overlayLayer = $('#' + overlayLayerID);
        overlayLayer.fadeOut('fast', function() {
            $(this).remove();
        });
        modalwindow.hide();
        // $('#modalwindow').fadeOut('slow',function(){
        // $(this).remove();
        // });
        return modalwindow;
    }

    // plugin defaults
    $.fn.openOverlay.defaults = {
        iOpacity : 60,
        sColor : '#444444',
        // width:'500px',
        zIndex : 1000
    };
    // end of closure
})(jQuery);
