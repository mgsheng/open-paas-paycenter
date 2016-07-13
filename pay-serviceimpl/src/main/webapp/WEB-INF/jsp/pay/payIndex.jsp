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
<input type="hidden" name="areaCode" value="" id="areaCode"/>
<input type="hidden" name="appId" value="${appId}" id="appId"/>
<div class="wrap">
	<header class="header">
		<div class="header-box">
			<span class="logo">
				<img src="${pageContext.request.contextPath}/images/open-logo.png" />
				<h3>收银台</h3>
			</span>
			<ul class="info">
				<li>小黄人</li>
				<li style="margin:0 7px 0 47px">|</li>
				<li>我的订单</li>
			</ul>
		</div>
	</header>

		<div class="pay-tips">
			<dl>
				<dt><img src="${pageContext.request.contextPath}/images/success.png" /></dt>
				<dd class="form-info1">
					<h3>订单提交成功！去付款～</h3>
					<p>下单时间：${orderCreateTime}</p>
					<p>订  单  号：${outTradeNo}</p>
				</dd>
				<dd class="form-info2">
					<h3>应付总额：<span class="color">${totalFee}</span>元</h3>
					<p>${goodsName}</p>
					<p>数量1</p>
				</dd>
			</dl>
		</div>
		<div class="pay-method content" >
			<h3 class="title">选择以下支付方式付款</h3>
			<h3 class="tit">支付平台<span> （大额支付推荐使用支付宝）</span></h3>
			<ul>
				<li value="1"><img src="${pageContext.request.contextPath}/images/zhifubao.jpg" /></li>
				<li value="2"><img src="${pageContext.request.contextPath}/images/zaixian.jpg" /></li>
				<li value="3" id="weixinPay"><img src="${pageContext.request.contextPath}/images/weixin.jpg" /></li>
			</ul>
			<h3 class="tit">银行借记卡及信用卡</h3>
			<ul>
				<li value="4"><img src="${pageContext.request.contextPath}/images/zhaohang.jpg" /></li>
				<li value="5"><img src="${pageContext.request.contextPath}/images/gonghang.jpg" /></li>
				<li value="6"><img src="${pageContext.request.contextPath}/images/jianhang.jpg" /></li>
				<li value="7"><img src="${pageContext.request.contextPath}/images/nonghang.jpg" /></li>
				<li value="8"><img src="${pageContext.request.contextPath}/images/zhonghang.jpg" /></li>
				<li value="9"><img src="${pageContext.request.contextPath}/images/jiaohang.jpg" /></li>
				<li value="10"><img src="${pageContext.request.contextPath}/images/youzheng.jpg" /></li>
				<li value="11"><img src="${pageContext.request.contextPath}/images/guangfa.jpg" /></li>
				<li value="12"><img src="${pageContext.request.contextPath}/images/pufa.jpg" /></li>
				<li value="13"><img src="${pageContext.request.contextPath}/images/guangda.jpg" /></li>
				<li value="14"><img src="${pageContext.request.contextPath}/images/pingan.jpg" /></li>
				<li>查看更多</li>
			</ul>
			<div class="btn" >
				<span class="confirm" onclick="submitCheck();">确认</span>
				<span class="cancel">取消</span>
			</div>
		</div>

	<footer class="footer">Copyright &copy; 2016</footer>
</div>
</form>
<!--弹出窗扣-->
<div class="mask"></div>
<div class="payLayer">
	<div class="hd">
		<img src="${pageContext.request.contextPath}/images/weixinPay.jpg" />
		<img class="close" src="${pageContext.request.contextPath}/images/close.png" />
	</div>
	<div>
		<div class="phone"><img src="${pageContext.request.contextPath}/images/phone.jpg" /></div>
		<div class="erweimaBox">
			<div class="erweima"><div id="payCode"></div></div>
			<p>请使用微信扫一扫<br>扫描二维码支付</p>
		</div>
	</div>
</div>

<script src="${pageContext.request.contextPath}/js/jquery-1.7.1.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.qrcode.min.js"></script><!--生成二维码-->
<script>
		
     var areaCode ;
     var outTradeNo="${outTradeNo}";
     var urlCode="";
	 var appId=$("#appId").val();

	$(".pay-method li").click(function(){
	    
		$(".pay-method li").removeClass('active');
		$(this).addClass('active');
		areaCode=$(this).attr("value");
		$("#areaCode").val(areaCode);
		console.log(areaCode+"==areaCode");
	});
	$("#weixinPay").bind("click",function(){
	       var appId=$("#appId").val();
	     /*   $.ajax({
			 type: 'post',
			 url: "${pageContext.request.contextPath}/alipay/selectChannelPay?areaCode="+areaCode+"&outTradeNo="+outTradeNo+"&appId="+appId,
			 success: function(date){
			 
			   }
			}); */
		
	});

	
	$(".close").bind("click",function(){
		$(".mask").hide();
		$(".payLayer").hide();
	});
	function submitCheck(){
	         if(areaCode=="3"){
	          $.ajax({
			 type: 'post',
			 dataType:'text',
			 url: "${pageContext.request.contextPath}/alipay/selectChannelPay?areaCode="+areaCode+"&outTradeNo="+outTradeNo+"&appId="+appId,
			 success: function(date){
			   urlCode=date;
			   console.log(urlCode+"==urlCode");
			   $(".mask").show();
		       $(".payLayer").show();
		       $("#payCode").empty();
		       $('#payCode').qrcode(urlCode);// 生成二维码
			   }
			   });
	         }else{
	         	$("form").submit();
	         }
			
			
	}
	function aa(){
	$("form").submit();
	}
</script>
</body>
</html>