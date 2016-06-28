<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE HTML>
<html>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>注册页</title>
<link rel="stylesheet" type="text/css" href="../css/style.css"/>
<style type="text/css">
	.mess{  color: red;  font-size: 15px;  margin-top: -30px;  margin-bottom: 10px;  width: 100%;  text-align: center;}
</style>
<script type="text/javascript" src="../js/jquery-1.7.1.min.js"></script>
<script type="text/javascript" src="../js/user_register.js"></script>
<script>
	$(document).ready(function(){
		var mess=$(".mess").text();
		if(mess=="error"){
			$(".mess").text("用户名已存在！");
		}
	});
</script>
</head>

<body>
<!--header start-->
<div class="open_header">
	<div class="open_header_inner">
    	<div class="open_logo"><img src="../images/open_logo.png" /></div>
        <span class="open_login_text">您已经有帐号了？<a href="${pageContext.request.contextPath}/login.do" class="open_login_btn">登录</a></span>
    </div>
</div>
<!--header end-->

<div class="open_main">
	<div class="open_content clearfix">
    	<!--注册表单开始-->
        <div class="reg_form">
        	<h2 class="reg_form_title">个人注册</h2>
            <form action="${pageContext.request.contextPath}/user/regist.do" method="post">
    			<input type="hidden" name="responseType" value='<c:out value="${responseType}"/>' />
    			<input type="hidden" name="scope" value='<c:out value="${scope}"/>' />
            	<input type="hidden" name="clientId" value='<c:out value="${clientId}"/>' />
            	<input type="hidden" name="redirect" value='<c:out value="${redirect}"/>' />
            	<input type="hidden" name="state" value='<c:out value="${state}"/>' />
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
                <p class="applicationlink">已经在学习中心报名，但还没有获得学习平台的用户名和密码。<a href="#" class="linkcolors">点击申请</a></p>
            </form>
        </div>
        <!--注册表单结束-->
        <div class="reg_sidebar">
        	<div class="reg_help">
            	<h2 class="reg_help_title">微博注册帮助</h2>
                <ul class="help_list">
                    <li><i class="arrow"></i><a class="link" href="#" target="_blank">微博注册操作指南</a></li>
                    <li><i class="arrow"></i><a class="link" href="#" target="_blank">手机注册时提示手机号码已被绑定怎么办手机注册时提示手机号码已被绑定怎么办?</a></li>
                    <li><i class="arrow"></i><a class="link" href="#" target="_blank">注册微博时昵称显示"已经被注册"如何注册微博时昵称显示?</a></li>
                    <li><i class="arrow"></i><a class="link" href="#" target="_blank">注册时提示"你所使用的注册微博时昵称显示IP地址异常",该怎么办?</a></li>
                </ul>
                <a class="linkcolors help_more" href="#" target="_blank">更多帮助»</a>
            </div>
        </div>
    </div>
</div>
</body>
</html>
