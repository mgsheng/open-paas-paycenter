<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE HTML>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>绑定页</title>
<link rel="stylesheet" type="text/css" href="../css/style.css"/>
<style type="text/css">
	.mess{  color: red;  font-size: 15px;  margin-top: 20px;  margin-bottom: 10px;  width: 100%;  text-align: center;}
</style>
<script type="text/javascript" src="../js/jquery-1.7.1.min.js"></script>
<script type="text/javascript" src="../js/user_bind.js"></script>
<script>
	$(document).ready(function(){
		var mess=$(".mess").text();
		if(mess=="error"){
			$(".mess").text("用户名已存在");
		}else if(mess=="nonexist"){
			$(".mess").text("用户名或密码错误");
		}
		var flag=$(".flag").text();
		if(flag=="1"){
			$("form dl:last").remove();
			$("form dl.tips").remove();
			$(".btn").val("立即登录");
			$("form").attr("action","relevance.do");
		}
		
		$(".open_login_btn").click(function(){
			$("form dl.again").remove();
			$("form dl .tips").remove();
			$(".mess").text("");
			$(".btn").val("立即登录");
			$("form").attr("action","relevance.do");
		});
	});
</script>
</head>

<body>
<!--header start-->
<div class="open_header">
	<div class="open_header_inner">
    	<div class="open_logo"><img src="../images/open_logo.png" /></div>
        <span class="open_login_text">您已经有帐号了？<a href="javascript:void(0);" class="open_login_btn">登录</a></span>
    </div>
</div>
<!--header end-->

<div class="open_main">
	<div class="open_content clearfix">
    	<!--绑定表单开始-->
        <div class="reg_form">
        	<h2 class="reg_form_title">绑定奥鹏通行证</h2>
            <form action="bind.do" method="post">
            	<input type="hidden" name="clientId" value='<c:out value="${clientId}"/>' />
            	<input type="hidden" name="redirect" value='<c:out value="${redirect}"/>' />
            	<input type="hidden" name="error" value='<c:out value="${error}"/>' />
            	<input type="hidden" name="sourceId" value='<c:out value="${sourceId}"/>' />
                <dl class="infolist">
                    <dt class="tit">用户名：</dt>
                    <dd class="inp"><input type="text" name="username" class="winput"/></dd>
                    <span class="tips">字母或数字，不要超过15个字符 </span>
                </dl>
                <dl class="infolist">
                    <dt class="tit">密码：</dt>
                    <dd class="inp"><input type="password" name="password" class="winput"/></dd>
                    <span class="tips">6～15位密码，请注意大小写，（A-Z，a-z，0-9，不要输入空格） </span>
                </dl>
               <dl class="again infolist">
                    <dt class="tit">确认密码：</dt>
                    <dd class="inp"><input type="password" name="againpwd" class="winput"/></dd>
                    <span class="tips">请再次输入密码 </span>
                </dl>             
                <div class="mess">${mess}</div>                
                <div style="display:none;" class="flag">${flag}</div>
                <div class="btnbox"><input type="submit" class="btn btn_submit w_382" value="立即绑定" /></div>
                <p class="textlinks"><a href="#">微博个人信息保护政策</a><a href="legalnotice.jsp">法律声明</a></p>
            </form>
        </div>
        <!--绑定表单结束-->
        <div class="reg_sidebar"><img src="../images/open_pass_pic.jpg" /></div>
    </div>
</div>
</body>
</html>
