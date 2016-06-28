<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE HTML>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>选择支付渠道页</title>
<script type="text/javascript" src="../js/jquery-1.7.1.min.js"></script>
</head>

<body>

<div class="open_main">
	<div class="open_content clearfix">
    	<!--表单开始-->
        <div class="login_form">
        	<h4 class="login_form_title">选择支付渠道</h4>
            <form id="inputForm" action="${pageContext.request.contextPath}/alipay/selectPayChannel" method="post">
            	<input type="hidden" name="orderId" value='<c:out value="${orderId}"/>' />
            	<input type="hidden" name="paymentType" value='<c:out value="${paymentType}"/>' />
                <select name="dictTradeChannel">
                	<c:forEach var="channel" items="${dictTradeChannels}">
                		<option value="${channel.id}">${channel.channelName}</option>
                	</c:forEach> 
                </select>     
                <p></p>
                <input type="submit" value="提交"/>
            </form>
        </div>
        <!--表单结束-->
    </div>
</div>
</body>
</html>