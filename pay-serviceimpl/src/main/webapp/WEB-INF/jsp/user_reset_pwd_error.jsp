<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE HTML>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>出错</title>
<link rel="stylesheet" type="text/css" href="../css/style.css"/>
<style type="text/css">
    .mess{  color: red;  font-size: 15px;  margin-left: 250px;  margin-bottom: 10px;  width: 100%;}
    .login_error_form{width:600px;padding:0 0 25px 42px;}
</style>
<script type="text/javascript" src="../js/jquery-1.7.1.min.js"></script>
<script>
	var errorStr = "";
	var time = 5;
	$(document).ready(function(){
		var mess=$(".mess").text();
		if(mess=="error"){
			errorStr = "重置密码失败！";
		}
		if(mess == "time_out"){
			errorStr = "链接已过期，请重新申请！";
		}
		if(mess == "invalid_code"){
			errorStr = "该链接已过期，请重新申请！";
		}
		if(mess == "not_found"){
			errorStr = "没有此用户！";
		}
		$('.mess').html(errorStr);
	});
</script>
</head>

<body>
<!--header start-->
<div class="open_header">
	<div class="open_header_inner">
    	<div class="open_logo"><img src="../images/open_logo.png" /></div>
        <%-- <a class="header_regbtn" href="${pageContext.request.contextPath}/user/user_register?client_Id=abf4bdedbea446f7af10dcfce1010e4f">注册</a> --%>
    </div>
</div>
<!--header end-->

<div class="open_main">
	<div class="open_content clearfix">
        <div class="login_error_form">
        	<div class="login_form_title"> 错误提示</div>
            <div class="mess">${mess}</div> 
        </div>
    </div>
</div>
</body>
</html>