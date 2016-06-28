<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE HTML>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">    
<title>登录页</title>
<link rel="stylesheet" type="text/css" href="css/style.css"/>
<style type="text/css">
	.text-danger{  display: block;  width: 100%; font-size: 16px;  color: red;  text-align: center; margin-top: 15px;}
</style>
</head>

<body>
<!--header start-->
<div class="open_header">
	<div class="open_header_inner">
    	<div class="open_logo"><img src="images/open_logo.png" /></div>
        <%-- <a class="header_regbtn" href="${pageContext.request.contextPath}/user/user_register?client_Id=abf4bdedbea446f7af10dcfce1010e4f">注册</a> --%>
        <a class="header_regbtn" href="http://${pageContext.request.remoteAddr}:8080/spring-oauth-client/register">注册</a>
    </div>
</div>
<!--header end-->

<div class="open_main">
	<div class="open_content clearfix">
    	<!--登录表单开始-->
        <div class="login_form">
            <c:if test="${!empty param.appname}">
        	<h2 class="login_form_title">使用您的奥鹏账号访问${param.appname},并同时自动登录</h2>
            </c:if>

            <form action="${pageContext.request.contextPath}/login.do" method="post">
                <dl class="infolist">
                    <dt class="tit">账号：</dt>
                    <dd class="inp"><input type="text" id="username" name="j_username" class="winput" placeholder="请用微博账号登录" value="" required="required"/></dd>
                </dl>
                <dl class="infolist">
                    <dt class="tit">密码：</dt>
                    <dd class="inp"><input type="password" name="j_password" id="password" class="winput" placeholder="请输入密码" value="" required="required"/></dd>
                </dl>
                <div class="btnbox"><input type="submit" class="btn btn_submit w_248" value="登录"/><a href="#" class="btn btn_cancel w_248">取消</a></div>     
                <span class="text-danger">
			        <c:if test="${param.authorization_error eq 2}">Access denied !!!</c:if>
			        <c:if test="${param.authentication_error eq 1}">Authentication Failure</c:if>
			    </span>        
                <p class="textlinks">提示：为了保障帐号安全，请认准本页URL地址必须api.open.com开头</p>
            </form>
        </div>
        <!--登录表单结束-->
    </div>
</div>
</body>
</html>