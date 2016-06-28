(function(e) {
	e.fn.zzmSlider = function(t) {
		var t = e.extend( {}, e.fn.zzmSlider.defaults, t);
		this.each(function() {
			function a() {
				return setInterval(function() {
					u %= o;
					var e = (u + 1) % o;
					r.eq(u).fadeOut(t.fadeTime), r.eq(e).fadeIn(t.fadeTime), i
							.eq(u).removeClass("active"), i.eq(e).addClass(
							"active"), u = e
				}, t.interval)
			}
			var n = e(this), r = e(this).find("li"), i = e(this).find(
					".toggle-btn a"), s, o = r.length, u = 0;
			s = a(), n.hover(function() {
				clearInterval(s), s = null
			}, function() {
				s = a()
			}), i
					&& i.on("click", function() {
						clearInterval(s), s = null;
						var n = e(this).index();
						if (n == u)
							return;
						r.eq(u).fadeOut(t.fadeTime),
								r.eq(n).fadeIn(t.fadeTime), i.eq(n).addClass(
										"active").siblings().removeClass(
										"active"), u = n
					})
		})
	}, e.fn.zzmSlider.defaults = {
		interval : 5e3,
		fadeTime : 500
	}
})(jQuery);