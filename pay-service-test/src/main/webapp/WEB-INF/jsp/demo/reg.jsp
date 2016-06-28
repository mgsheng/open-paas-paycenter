<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE HTML>
<html>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>注册页</title>
<style type="text/css">
	.mess{  color: red;  font-size: 15px;  margin-top: -30px;  margin-bottom: 10px;  width: 100%;  text-align: center;}
</style>
<script src="js/jquery-1.7.1.min.js"></script>
<script src="js/user_register.js"></script>
</head>

<body>
<!--header start-->
<div class="open_header">
	<div class="open_header_inner">
    	<div class="open_logo"><!-- <img src="../images/open_logo.png" /> --></div>
        <%-- <span class="open_login_text">您已经有帐号了？<a href="${pageContext.request.contextPath}/login.do" class="open_login_btn">登录</a></span> --%>
    </div>
</div>
<!--header end-->

<div class="open_main">
	<div class="open_content clearfix">
    	<!--注册表单开始-->
        <div class="reg_form">
        	<h2 class="reg_form_title">个人注册</h2>
            <form action="reg" method="post">
    			<input type="hidden" name="userCenterRegUri" value='<c:out value="${userCenterRegUri}"/>' />
    			<input type="hidden" name="clientId" value='<c:out value="${clientId}"/>' />
    			<input type="hidden" name="access_token" value='<c:out value="${accessToken}"/>' />
            	<input type="hidden" name="grantType" value='<c:out value="${grantType}"/>' />
            	<input type="hidden" name="scope" value='<c:out value="${scope}"/>' />
                <dl class="infolist">
                    <dt class="tit">用户名：</dt>
                    <dd class="inp"><input type="text" name="username" class="winput"/></dd>
                    <span class="tips">字母或数字，不要超过15个字符 </span>
                </dl>
                <dl class="infolist">
                    <dt class="tit">设置密码：</dt>
                    <dd class="inp"><input type="password" name="password" class="winput"/></dd>
                    <span class="tips">6～15位密码，请注意大小写，（A-Z，a-z，0-9，不要输入空格）</span>
                </dl>
                <dl class="infolist">
                    <dt class="tit">重复密码：</dt>
                    <dd class="inp"><input type="password" name="againpwd" class="winput"/></dd>
                    <span class="tips">请再次输入密码 </span>
                </dl>
                <dl class="infolist">
                    <dt class="tit"> 邮 箱：</dt>
                    <dd class="inp"><input type="text" name="email" class="winput"/></dd>
                    <span class="tips">请使用您常用的邮箱 </span>
                </dl>
                <dl class="infolist">
                    <dt class="tit"> 手机号：</dt>
                    <dd class="inp"><input type="text" name="phone" class="winput"/></dd>
                    <span class="tips">请输入真实手机号码，以便于联系  </span>
                </dl>
                <dl class="infolist">
                    <dd class="inp"><label><input id="agree" name="agreement" type="checkbox" value="" class="agreement_checkb"/>我已经看过并同意<a href="#">《用户协议及服务条款》</a></label>
                    </dd>
                </dl>
                <div class="mess">${mess}</div>       
                <div class="btnbox"><input type="submit" class="btn btn_submit w_382" value="立即注册" /></div>
            </form>
        </div>
    </div>
</div>
</body>
</html>
