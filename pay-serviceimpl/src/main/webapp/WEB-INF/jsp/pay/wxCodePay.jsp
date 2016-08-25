<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE HTML>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>微信支付渠道页</title>
<script type="text/javascript" src="../js/jquery-1.7.1.min.js"></script>
<script type="text/javascript" src="../js/jquery.qrcode.min.js"></script>
<script>
</script>
</head>

<body>
	<input type="hidden" value="${urlCode}" id="urlCode">
	<div style="width: 1000px;height: 50px;margin-top: 100px" align="center">
		<h3 class="login_form_title">微信二维码支付</h3>
	</div>
	<div id="qrcode"
		style="width: 1000px"align="center"></div>
	<script>
  var content=$("#urlCode").val();
  //console.log(content);
  $("#qrcode").qrcode({
           render: "table",
           text: content
  });
</script>

</body>

</html>