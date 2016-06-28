<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE HTML>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
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
    	<div class="open_logo"><!-- <img src="images/open_logo.png" /> --></div>
        <a class="header_regbtn" href="http://localhost:8080/spring-oauth-client/register"></a>
    </div>
</div>
<!--header end-->

<div class="open_main">
	<div class="open_content clearfix">
    	<!--登录表单开始-->
        <div class="login_form">
        	<h2 class="login_form_title"></h2>
            <form action="${pageContext.request.contextPath}/login" method="post">
                <dl class="infolist">
                    <dt class="tit">账号：</dt>
                    <dd class="inp"><input type="text" id="username" name="username" class="winput" placeholder="请用微博账号登录" value="" required="required"/></dd>
                </dl>
                <dl class="infolist">
                    <dt class="tit">密码：</dt>
                    <dd class="inp"><input type="password" name="password" id="password" class="winput" placeholder="请输入密码" value="" required="required"/></dd>
                </dl>
                <div class="btnbox"><input type="submit" class="btn btn_submit w_248" value="登录"/><a href="#" class="btn btn_cancel w_248">取消</a></div>     
                <span class="text-danger">
			    </span>        
                <p class="textlinks"></p>
                <input type="hidden" name="userCenterPasswordUri" value="${userCenterPasswordUri}"/>
                <input type="hidden" name="client_id" value="${clientId}"/>                
                <%-- <input type="hidden" name="client_secret" value="${clientSecret}"/> --%>
                <input type="hidden" name="access_token" value="${accessToken}"/>                
                <input type="hidden" name="scope" value="${scope}"/>
                <input type="hidden" name="grant_type" value="${grantType}"/>
            </form>
        </div>
        <div style="color:red">${mess}</div>
        <div></div>
        <!--登录表单结束-->
    </div>
</div>
<div>
	<p>接入用户中心登录接口说明：</p>
	<p>1.访问登录页面,并传入参数
	{ client_id	客户端从 Oauth Server 申请的client_id
	  client_secret   客户端从 Oauth Server 申请的client_secret
	  grant_type     固定值:'client_credentials'
	  scope         (read,write) }
	 </p>
	<p>2.输入用户名密码点击登录，调用用户中心登录接口UserCenterPassword,查看接口点击 <a href="userCenterPassword">userCenterPassword</a></p>
	<p>3.如果返回json中status=0且error_code=3（用户名不存在），执行原系统的登录;否则返回错误信息</p>
	<p>4.如果返回json中status=1,则登录成功且返回json形式及对象形式的app信息列表</p>
</div>
</body>
</html>