jQuery.fn.extend({
    everyTime : function(a, b, c, d) {
        return this.each(function() {
            jQuery.timer.add(this, a, b, c, d)
        })
    },
    oneTime : function(b, c, a) {
        return this.each(function() {
            jQuery.timer.add(this, b, c, a, 1)
        })
    },
    stopTime : function(a, b) {
        return this.each(function() {
            jQuery.timer.remove(this, a, b)
        })
    }
});
jQuery.extend({
    timer : {
        global : [],
        guid : 1,
        dataKey : "jQuery.timer",
        regex : /^([0-9]+(?:\.[0-9]*)?)\s*(.*s)?$/,
        powers : {
            ms : 1,
            cs : 10,
            ds : 100,
            s : 1000,
            das : 10000,
            hs : 100000,
            ks : 1000000
        },
        timeParse : function(c) {
            if (c == undefined || c == null) {
                return null
            }
            var a = this.regex.exec(jQuery.trim(c.toString()));
            if (a[2]) {
                var b = parseFloat(a[1]);
                var d = this.powers[a[2]] || 1;
                return b * d
            } else {
                return c
            }
        },
        add : function(b, h, a, d, f) {
            var g = 0;
            if (jQuery.isFunction(a)) {
                if (!f) {
                    f = d
                }
                d = a;
                a = h
            }
            h = jQuery.timer.timeParse(h);
            if (typeof h != "number" || isNaN(h) || h < 0) {
                return
            }
            if (typeof f != "number" || isNaN(f) || f < 0) {
                f = 0
            }
            f = f || 0;
            var e = jQuery.data(b, this.dataKey) || jQuery.data(b, this.dataKey, {});
            if (!e[a]) {
                e[a] = {}
            }
            d.timerID = d.timerID || this.guid++;
            var c = function() {
                if ((++g > f && f !== 0) || d.call(b, g) === false) {
                    jQuery.timer.remove(b, a, d)
                }
            };
            c.timerID = d.timerID;
            if (!e[a][d.timerID]) {
                e[a][d.timerID] = window.setInterval(c, h)
            }
            this.global.push(b)
        },
        remove : function(c, b, d) {
            var e = jQuery.data(c, this.dataKey), a;
            if (e) {
                if (!b) {
                    for (b in e) {
                        this.remove(c, b, d)
                    }
                } else {
                    if (e[b]) {
                        if (d) {
                            if (d.timerID) {
                                window.clearInterval(e[b][d.timerID]);
                                delete e[b][d.timerID]
                            }
                        } else {
                            for ( var d in e[b]) {
                                window.clearInterval(e[b][d]);
                                delete e[b][d]
                            }
                        }
                        for (a in e[b]) {
                            break
                        }
                        if (!a) {
                            a = null;
                            delete e[b]
                        }
                    }
                }
                for (a in e) {
                    break
                }
                if (!a) {
                    jQuery.removeData(c, this.dataKey)
                }
            }
        }
    }
});
jQuery(window).bind("unload", function() {
    jQuery.each(jQuery.timer.global, function(a, b) {
        jQuery.timer.remove(b)
    })
});
