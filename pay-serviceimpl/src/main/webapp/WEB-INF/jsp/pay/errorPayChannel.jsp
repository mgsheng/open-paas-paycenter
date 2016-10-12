<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE>
<html>
<head>
	<meta charset="utf-8">
	<title>open 收银台</title>
	<link rel="stylesheet" href="${pageContext.request.contextPath}/css/pay_style.css">
</head>

<body>
<form id="inputForm" action="${pageContext.request.contextPath}/alipay/selectChannelPay" method="post" target="_blank">
<input type="hidden"  name="outTradeNo" value="${outTradeNo}" id="outTradeNo"/>
<div class="wrap">
	<header class="header">
		<div class="header-box">
			<span class="logo">
				<img src="${pageContext.request.contextPath}/images/open-logo.png" />
				<h3 style="display: initial;">奥鹏教育</h3>
			</span>
		</div>
		
	</header>

		<div class="pay-tips" style="height: 430px">
			<dl>
				<dt><img src="${pageContext.request.contextPath}/images/gtanhao.jpg" /></dt>
				<dd class="form-info1"  style="float:left; margin-left: 50px; text-align: left">
					<h3>订单提交错误！</h3>
					<p>错误信息：${errorMsg}</p>
					<p>订  单  号：${outTradeNo}</p>
				</dd>
			</dl>
		</div>

	<footer class="footer" style="padding-top: 50px;margin-top: 30px;height: 10px">Copyright &copy; 2016</footer>
</div>
</form>

<div id="load-print" class="load-print">
			<img style="width:150px" src="${pageContext.request.contextPath}/images/dongtu.gif" />
</div>




<script src="${pageContext.request.contextPath}/js/jquery-1.7.1.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.qrcode.min.js"></script><!--生成二维码-->
<script>

	function onBridgeReady() {
	    var appId=
		WeixinJSBridge.invoke('getBrandWCPayRequest', {
			"appId" : "wx2421b1c4370ec43b", //公众号名称，由商户传入     
			"timeStamp" : " 1395712654", //时间戳，自1970年以来的秒数     
			"nonceStr" : "e61463f8efa94090b1f366cccfbbb444", //随机串     
			"package" : "prepay_id=u802345jgfjsdfgsdg888",
			"signType" : "MD5", //微信签名方式：     
			"paySign" : "70EA570631E4BB79628FBCA90534C63FF7FADD89" //微信签名 
		}, function(res) {
			if (res.err_msg == "get_brand_wcpay_request：ok") {
			} // 使用以上方式判断前端返回,微信团队郑重提示：res.err_msg将在用户支付成功后返回    ok，但并不保证它绝对可靠。 
		});
	}
	if (typeof WeixinJSBridge == "undefined") {
		if (document.addEventListener) {
			document.addEventListener('WeixinJSBridgeReady', onBridgeReady,
					false);
		} else if (document.attachEvent) {
			document.attachEvent('WeixinJSBridgeReady', onBridgeReady);
			document.attachEvent('onWeixinJSBridgeReady', onBridgeReady);
		}
	} else {
		onBridgeReady();
	}
</script>
</body>
</html>