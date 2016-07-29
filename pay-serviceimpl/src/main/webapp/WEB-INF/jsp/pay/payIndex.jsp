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
<input type="hidden" name="goodsName" value="${goodsName}" id="goodsName"/>
<input type="hidden" name="totalFee" value="${totalFeeValue}" id="totalFee"/>
<input type="hidden" name="goodsDesc" value="${goodsDesc}" id="goodsDesc"/>
<input type="hidden" name="goodsId" value="${goodsId}" id="goodsId"/>
<input type="hidden" name="merchantId" value="${merchantId}" id="merchantId"/>

<div class="wrap">
	<header class="header">
		<div class="header-box">
			<span class="logo">
				<img src="${pageContext.request.contextPath}/images/open-logo.png" />
				<h3>收银台</h3>
			</span>
		</div>
		
	</header>
	<div class="paymentfi" style="position:relative;">
		<div class="paymentfi" style="position:relative;">
				<div id="tipInfo1" class="paymentTop" >
						<div class="cloutOut" >
							<h4 style="text-align:right;" ><span onclick="clouOut()"><img src="${pageContext.request.contextPath}/images/close.png" /></span></h4>
						</div>
						<div ><h4>没有开通网上银行如何购买？</h4>
							<p>各银行已有支持无需网银的小额支付产品，可直接选择付款。或者，可选择银联在线支付或快捷支付（只支持信用卡）付款。</p>
						</div>
						<div ><h4>没找到我常用的网上银行？</h4>
							<p>建议选择银联在线支付付款，支持超过180家银行，包括主流银行和地方银行，例如华夏银行，江苏银行，南京银行等。</p>
						</div>
						<div><h4>什么是地方银行？</h4>
							<p>地方银行主要指注册和经营在当地的城市商业银行、农村合作银行、村镇银行等中小金融机构，例如江苏银行，南京银行，华夏银行等。</p>
						</div>
						<div ><h4>无法跳转到对应的支付页面支付？</h4>
							<p>建议刷新当前页面，如果没恢复正常建议重启或更换到IE浏览器。</p>
						</div>
						<div><h4>网上银行扣款后，订单仍显示"未付款"怎么办？</h4>
							<p>可能是由于银行的数据没有即时传输，请您不要担心，稍后刷新页面查看。如较长时间仍显示未付款，可先向银行或支付平台获取支付凭证（扣款订单号/第三方交易号），联系奥鹏客服为您解决。</p>
						</div>
				</div>
		</div>
	</div>
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
				</dd>
			</dl>
			<dl>
				<div>
						<div id="tipInfo" class="payment"  >
							<div><h4><span ></span>没有开通网上银行如何购买？</h4>
								<p>各银行已有支持无需网银的小额支付产品，可直接选择付款。或者，可选择银联在线支付或快捷支付（只支持信用卡）付款。</p>
							</div>
							<div><h4><span ></span>没找到我常用的网上银行？</h4>
								<p>建议选择银联在线支付付款，支持超过180家银行，包括主流银行和地方银行，例如华夏银行，江苏银行，南京银行等。</p>
							</div>
							<div><h4><span></span>什么是地方银行？</h4>
								<p>地方银行主要指注册和经营在当地的城市商业银行、农村合作银行、村镇银行等中小金融机构，例如江苏银行，南京银行，华夏银行等。</p>
							</div>
							<div><h4><span ></span>无法跳转到对应的支付页面支付？</h4>
								<p>建议刷新当前页面，如果没恢复正常建议重启或更换到IE浏览器。</p>
							</div>
							<div><h4><span ></span>网上银行扣款后，订单仍显示"未付款"怎么办？</h4>
								<p>可能是由于银行的数据没有即时传输，请您不要担心，稍后刷新页面查看。如较长时间仍显示未付款，可先向银行或支付平台获取支付凭证（扣款订单号/第三方交易号），联系奥鹏客服为您解决。</p>
							</div>
						</div>
				  </div>
			</dl>
		</div>
		<span class="assist" onMouseOver="showTip();" onMouseOut="hideTip();">支付帮助</span>
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
				<li value="10001"><img src="${pageContext.request.contextPath}/images/zhaohang.jpg" /></li>
				<li value="10002"><img src="${pageContext.request.contextPath}/images/gonghang.jpg" /></li>
				<li value="10003"><img src="${pageContext.request.contextPath}/images/jianhang.jpg" /></li>
				<li value="10004"><img src="${pageContext.request.contextPath}/images/nonghang.jpg" /></li>
				<li value="10005"><img src="${pageContext.request.contextPath}/images/zhonghang.jpg" /></li>
				<li value="10006"><img src="${pageContext.request.contextPath}/images/jiaohang.jpg" /></li>
				<li value="10007"><img src="${pageContext.request.contextPath}/images/youzheng.jpg" /></li>
				<li value="10008"><img src="${pageContext.request.contextPath}/images/guangfa.jpg" /></li>
				<li value="10009"><img src="${pageContext.request.contextPath}/images/pufa.jpg" /></li>
				<li value="10010"><img src="${pageContext.request.contextPath}/images/guangda.jpg" /></li>
				<li value="10011"><img src="${pageContext.request.contextPath}/images/pingan.jpg" /></li>
				<li>查看更多</li>
			</ul>
			<div class="box" >
				<p>
					<span class="confirm" onclick="submitCheck('confirm-modal');">确认</span>
					<span class="cancel">取消</span>
				</p>
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
		<img class="close" onclick="closeAll()" src="${pageContext.request.contextPath}/images/close.png" />
	</div>
	<div>
		<div class="phone"><img src="${pageContext.request.contextPath}/images/phone.jpg" /></div>
		<div class="erweimaBox">
			<div class="erweima" id="erweima">
			</div>
			<p>请使用微信扫一扫<br>扫描二维码支付</p>
		</div>
	</div>
</div>


<div id="confirm-modal" style="display:none;">
 		<div class="modal-mask"></div>
	 <div class="modal-box">
		<div class="modal-head"><i class="icon icon-close"></i>
		<img class="icon icon-close" onclick="closeBtu()" src="${pageContext.request.contextPath}/images/close.png" />
		</div>
		<div class="modal-body1"><i class="icon icon-ecom"></i>
			<img class="icon icon-ecom" src="${pageContext.request.contextPath}/images/gtanhao.jpg" />
			<div class="modal-contentw"><h3>请您在新打开的页面上完成付款。</h3><p>付款完成前请不要关闭此窗口。<br>完成付款后请根据您的情况点击下面的按钮：</p>
				<div class="modal-btn-group">
					<button class="btn1 modal-btn-ok" onclick="accompLish('${outTradeNo}')">已完成付款</button>
					<button class="btn1 modal-btn-cancel" onclick="outBtu()">付款遇到问题</button>
				</div>
				<span class="modal-back" onclick="closeBtu()">返回选择其他支付方式</span>
			</div>
		</div> 
	</div>
 
</div>

<div id="load-print" class="load-print">
			<img style="width:150px" src="${pageContext.request.contextPath}/images/dongtu.gif" />
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
		if(areaCode=="1"){
			$("#areaCode").val("1");
		}else if(areaCode=="2"){
			$("#areaCode").val("2");
		}else if(areaCode=="10001"){
			$("#areaCode").val("CMB");
		}else if(areaCode=="10002"){
			$("#areaCode").val("ICBC");
		}else if(areaCode=="10003"){
			$("#areaCode").val("CCB");
		}else if(areaCode=="10004"){
			$("#areaCode").val("ABC");
		}else if(areaCode=="10005"){
			$("#areaCode").val("BOC");
		}else if(areaCode=="10006"){
			$("#areaCode").val("BCOM");
		}else if(areaCode=="10007"){
			$("#areaCode").val("PSBC");
		}else if(areaCode=="10008"){
			$("#areaCode").val("CGB");
		}else if(areaCode=="10009"){
			$("#areaCode").val("SPDB");
		}else if(areaCode=="10010"){
			$("#areaCode").val("CEB");
		}else if(areaCode=="10011"){
			$("#areaCode").val("PAB");
		}
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
	function submitCheck(obj){
         if(areaCode=="3"){
        	 var a=document.getElementById("erweima").innerHTML.length;
        	 if ( a == 4) {
		         var payWx="${payWx}";
		         var totalFee="${totalFeeValue}";
		         var goodsDesc="${goodsDesc}";
		         var goodsId="${goodsId}";
		         $(".mask").show();
			     $(".payLayer").show();
		         $.ajax({
					 type: 'post',
					 beforeSend:function () {
						 $("#load-print").show();
					    },
					 dataType:'text',
					 url: "${pageContext.request.contextPath}/alipay/selectChannelPay?areaCode="+areaCode+"&outTradeNo="+outTradeNo+"&appId="+appId+"&payWx="+payWx+"&totalFee="+totalFee+"&goodsDesc="+goodsDesc+"&goodsId="+goodsId,
					 success: function(date){
						   urlCode=date;
						   console.log(urlCode+"==urlCode");
					       $("#payCode").empty();
						   var urlDu = date.split(",")[1];
						   var url=urlDu.slice(10,urlDu.length-1);
						   document.getElementById("erweima").innerHTML="<img src="+url+" width='260' height='260'>";
					       $("#load-print").hide();
					   }
				 });
	        	 }else{
	        		 $(".mask").show();
	        		 $(".payLayer").show();
	        	 }
         }else{
         	$("form").submit();
         }
         if(areaCode!=3){
        	 document.getElementById(obj).style.display = 'block';
        	 $('.mask').show();
         }
	}
	function aa(){
	$("form").submit();
	}
	
	function accompLish(outTradeNo){
		 $.ajax({
			 type: 'post',
			 dataType:'text',
			 url: "${pageContext.request.contextPath}/alipay/selectAccomplish?outTradeNo="+outTradeNo,
			 success: function(date){
				  if(date=="success"){
					  $("#confirm-modal").hide();
					  alert("交易成功");
					  $('.mask').hide();
				  }else{
					  $("#confirm-modal").hide();
					  alert("交易失败");
					  $('.mask').hide();
				  }
			   }
			   });
	}
	
	function clouOut(){
		$("#tipInfo1").hide();
	}
	function outBtu(){
		$("#confirm-modal").hide();
		$("#tipInfo1").show();
		$('.mask').hide();
	}
	
	function closeBtu(){
		$("#confirm-modal").hide();
		 $('.mask').hide();
		 $("#load-print").hide();
	}
	
	function showTip() {
		$("#tipInfo").show();
	}
	
	function hideTip() {
		$("#tipInfo").hide();
	}
	
	function closeAll() {
		$("#load-print").hide();
	}
	
</script>
</body>
</html>