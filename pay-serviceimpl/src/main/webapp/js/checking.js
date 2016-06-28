/**
 * @description :js生成验证码，经测试通过IE+FF+Chrom+Opera+Safari
 * @author : WangTao
 */
var Checking = {};

Checking = {
	$ : function(id) {
		return document.getElementById(id);
	},

	// 判断是否为IE内核
	isIE : function() {
		return !-[1,];
	},
	color : ['#FF0000', '#00FF66', '#FFFF00', '#FFCC00', '#6600FF', '#0000FF',
			'#99FFFF', '#66CCFF'],
	cacheValue : null,// 缓存画布上的值,比对时用
	// 初始化
	init : function() {
		if (this.isIE()) {// 如果是ie内核
			console.log("sssss");
			this.drawIE();
		} else {
			console.log("sssss");
			this.drawCanvas();
		
		}

	},
	// 如果为ie内核，操作div
	drawIE : function() {
		var div = this.$('ieContainer'), sty = div.style;
		div.onselectstart = new Function("return false");// 禁止选中
		div.oncopy = new Function("return false");// 禁止复制

		sty.height = "30px";
		sty.width = "125px";
		var bgColor = this.getRandomColor();// div背景色
		sty.backgroundColor = bgColor;
		sty.font = "26pt Calibri";
		var fontColor = this.getRandomColor();// 文字颜色
		sty.color = bgColor == fontColor ? this.getRandomColor() : fontColor;// 保证文字色基本不会跟背景色一样
		sty.textAlign = "center";
		// 设置div颜色渐变效果
		sty.filter = "progid:DXImageTransform.Microsoft.Gradient(GradientType=1, StartColorStr= "
				+ bgColor + ", EndColorStr= " + this.getRandomColor() + ")";
		var newText = this.getRandomDigit_Letters(5);
		this['cacheValue'] = newText;// 临时存贮生成的验证码，校验时使用
		div.innerHTML = newText;
	},

	// 根据html canvas生成验证码
	drawCanvas : function() {
		var canvas = this.$("myCanvas");// 获得画布
		var context = canvas.getContext("2d");
		this.drawBgColor(context);// 画验证码背景
		this.drawText(context);// 像图片上写验证码
	},
	/**
	 * @description : 画背景
	 * @param {}
	 *            context : 画布上下文，相当于graphics
	 */
	drawBgColor : function(context) {
		var grd = context.createLinearGradient(0, 0, 70, 18);
		var startColor = this.getRandomColor();
		var endColor = this.getRandomColor();
		grd.addColorStop(0, startColor);
		grd.addColorStop(1, startColor == endColor
						? this.getRandomColor()
						: endColor);
		context.fillStyle = grd;
		context.fillRect(0, 0, 800, 600);
	},

	/**
	 * @description : 画文字
	 * @param {}
	 *            context
	 */
	drawText : function(context) {
		var x = 60;
		var y = 30;
		context.font = "30pt Calibri";
		context.textAlign = "center";
		context.fillStyle = this.getRandomColor();
		var newText = this.getRandomDigit_Letters(5);
		this['cacheValue'] = newText;
		context.fillText(newText, x, y);
	},

	/**
	 * 获得随机颜色
	 * 
	 * @return {}String : HEX
	 */
	getRandomColor : function() {
		var len = this.color.length, random = this.getBigRandom(len);
		return this['color'][random];
	},

	/**
	 * @description :根据scale以内的随机整数
	 * @param {}
	 *            scale : 随机数边界
	 * @return {} : Number
	 */
	getBigRandom : function(scale) {
		return Math.floor(Math.random() * (scale || 10));
	},

	/**
	 * @description : 获得画布上的字符串,字母与数字的随机组合
	 * 
	 * @param {}
	 *            length :字符串位数
	 * @return {} :String
	 */
	getRandomDigit_Letters : function(length) {
		var result = [];
		var arr = this.getLetters().concat(this.getDigit());
		for (var i = 0; i < length; i++) {
			result.push(arr[this.getBigRandom(35)]);
		}
		return result.join("");

	},

	/**
	 * 获得0～9的数组
	 * 
	 * @return {}Array[Number]
	 */
	getDigit : function() {
		var arr = [];
		for (var i = 0; i < 10; i++) {
			arr.push(i);
		}
		return arr;
	},

	/**
	 * 获得a～z的字母数组
	 * 
	 * @return {}:Array[String]
	 */
	getLetters : function() {
		var arr = [];
		var start = "a".charCodeAt();
		var end = "z".charCodeAt();

		for (var i = start; i <= end; i++) {
			if (i % 2 == 0) {
				arr.push(String.fromCharCode(i));
			} else {
				arr.push(String.fromCharCode(i).toUpperCase());
			}

		}
		return arr;
	},

	// 判断
	check : function(a,b) {
		var chkValue = this['cacheValue'].toUpperCase();
		var inputValue = this.$('chk').value.toUpperCase();
		if(chkValue == inputValue){
			$("#change_button").attr('type','submit');
			$("#baseForm").submit();
		}else{
			$("#idfCode_error").text("输入验证码不对，请重新输入");
			Checking.init();
		}
	},
	
	// 判断返回bool
	checkbool : function(a) {
		var chkValue = this['cacheValue'].toUpperCase();
		var inputValue = this.$('chk').value.toUpperCase();
		if(chkValue == inputValue){
			return true;
		}else{
			$(a).text("输入验证码不对，请重新输入");
			Checking.init();
			return false;
		}
	}
}
