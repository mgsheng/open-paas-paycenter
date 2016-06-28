<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE HTML>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>重置密码页</title>
<link rel="stylesheet" type="text/css" href="../css/style.css"/>
<style type="text/css">
	.tit_w1{
		color: #000;
	    float: left;
	    font-size: 14px;
	    line-height: 46px;
	    padding-right: 10px;
	    text-align: right;
	    width: 70px;}
    .mess{  color: red;  font-size: 15px;  margin-left: 250px;  margin-bottom: 10px;  width: 100%;}
</style>
<script type="text/javascript" src="../js/jquery-1.7.1.min.js"></script>
<script type="text/javascript" src="../js/user_reset_pwd.js"></script>
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
    	<!--登录表单开始-->
        <div class="login_form">
        	<h2 class="login_form_title">重置密码</h2>
            <form id="inputForm" action="${pageContext.request.contextPath}/user/reset_pwd.do" method="post">
            	<input type="hidden" name="id" value='<c:out value="${id}"/>' />
                <dl class="infolist">
                    <dt class="tit_w1">新密码：</dt>
                    <dd class="inp"><input type="password" id="password" name="password" class="winput" placeholder="请输入密码" value="" required="required"/></dd>
                	<span class="tips">6～15位密码，请注意大小写，（A-Z，a-z，0-9，不要输入空格）</span>
                </dl>
                <dl class="infolist">
                    <dt class="tit_w1">重复密码：</dt>
                    <dd class="inp"><input type="password" name="againpwd" id="againpwd" class="winput" placeholder="请再次输入密码" value="" required="required"/></dd>
               		<span class="tips">请再次输入密码 </span>
                </dl>
                <div class="mess"></div>    
                <div class="btnbox"><input type="submit" class="btn btn_submit w_248" value="重置密码"/><a href="#" class="btn btn_cancel w_248">取消</a></div>     
                <p class="textlinks">提示：为了保障帐号安全，请认准本页URL地址必须api.open.com开头</p>
            </form>
        </div>
        <!--登录表单结束-->
    </div>
</div>
</body>
</html>