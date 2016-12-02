<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<script src="http://res.wx.qq.com/open/js/jweixin-1.0.0.js"></script>
    <script type="text/javascript" src="http://eduadminnew.open.com.cn/Public/Weixin/API/JSHandler.ashx"></script>
    <script type="text/javascript">
    var appId="${appId}";
    var orderId="${orderId}";
    var timeStamp="${timeStamp }";
    var nonceStr="${nonceStr }";
    var Weixinpackage="${wxpackage}";
    var signType="${signType }";
    var paySign="${paySign }";
  	function onBridgeReady(){
		   WeixinJSBridge.invoke(
		       'getBrandWCPayRequest', {
		           "appId":appId,     //公众号名称，由商户传入     
		           "timeStamp":timeStamp,         //时间戳，自1970年以来的秒数     
		           "nonceStr":nonceStr, //随机串     
		           "package":Weixinpackage,     
		           "signType":signType,         //微信签名方式：     
		           "paySign":paySign //微信签名 
		       },
		       function(res){     
		           if(res.err_msg == "get_brand_wcpay_request：ok" ) {
		             window.location.href="http://pay-service-openpre.myalauda.cn/pay-service/paymax/wap/callBack?orderId"+orderId; 
		           }     // 使用以上方式判断前端返回,微信团队郑重提示：res.err_msg将在用户支付成功后返回    ok，但并不保证它绝对可靠。 
		       }
		   ); 
		}
		if (typeof WeixinJSBridge == "undefined"){
		   if( document.addEventListener ){
		       document.addEventListener('WeixinJSBridgeReady', onBridgeReady, false);
		   }else if (document.attachEvent){
		       document.attachEvent('WeixinJSBridgeReady', onBridgeReady); 
		       document.attachEvent('onWeixinJSBridgeReady', onBridgeReady);
		   }
		}else{
		   onBridgeReady();
		}
  </script>
  </head>

  <body >
  </body>
 
</html>
