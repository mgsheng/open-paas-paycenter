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
				<h3 style="display: initial;">收银台</h3>
			</span>
		</div>
		
	</header>

		<div class="pay-tips" style="height: 430px">
			<dl>
				<dt><img src="${pageContext.request.contextPath}/images/gtanhao.jpg" /></dt>
				<dd class="form-info1"  style="float:left; margin-left: 50px; text-align: left">
					<h3>订单支付信息！</h3>
					<p>订  单  号：${outTradeNo}</p>
					<p id="backMsg"></p>
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
 var backMsg="${backMsg}";
 $(function(){
 if(backMsg=="success"){
 $("#backMsg").text("支付信息:支付成功");
 }else{
 $("#backMsg").text("支付信息:支付失败");
 }
 
  }); 
</script>
</body>
</html>