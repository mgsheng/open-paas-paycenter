<%@ page import="org.springframework.security.web.savedrequest.DefaultSavedRequest" %>
<%@ page import="org.springframework.context.support.ClassPathXmlApplicationContext" %>
<%@ page import="org.springframework.context.ApplicationContext" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE HTML>
<html>
<head>
    <meta charset="utf-8">
    <title>重置密码页</title>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="Charisma, a fully featured, responsive, HTML5, Bootstrap admin template.">
    <meta name="author" content="Muhammad Usman">

    <!-- The styles -->
    <link rel="stylesheet" type="text/css" href="../css/style.css"/>
    <link rel="stylesheet" type="text/css" href="../css/charisma-app.css"/>
    <link id="bs-css" href="../css/bootstrap.css" rel="stylesheet">
    <link href="../css/bootstrap-cerulean.min.css" rel="stylesheet">
    <link href="../css/login-mobile.css" rel="stylesheet">

    <!-- jQuery -->
    <script src="../js/jquery-1.7.1.min.js"></script>
    <script src="../js/charisma.js"></script>
	<script type="text/javascript" src="../js/user_reset_pwd.js"></script>

    <!-- The HTML5 shim, for IE6-8 support of HTML5 elements -->
    <!--[if lt IE 9]>
    <!--script src="http://html5shim.googlecode.com/svn/trunk/html5.js"></script>
    <![endif]--   .navbar-default{background-color:#000000;border-color:#E4F8F8}>

    <!-- The fav icon -->
    <link rel="shortcut icon" href="../img/favicon.ico">
    <style type="text/css">
	    .mess{  color: red;  font-size: 15px;  margin-bottom: 10px;  width: 100%;}
	</style>
    <script type="text/javascript">
    	$(document).ready(function(){
			$(".resetbtn").click(function(){
				var password=$("input[name='password']").val(); 
				var againpwd=$("input[name='againpwd']").val(); 
				if($.trim(password).length>15 || $.trim(password).length<6){
					$(".mess").text("密码必须由6-15位字母或数字组成！");
					return false;
				}
				if(againpwd!=password){
					$(".mess").text("重复密码与设置密码不同！");
					return false;
				}
				return true;
			});
		});
    </script>
</head>

<body style="font-size: 70%">

<div class="nbar nbar-default" >
    <div>
        <a href="index.html">
            <!--width: 28.6rem;height: 8.5rem-->
            <img alt="Open Logo" src="../images/open_logo.png" style="width: 15rem;height: 4.5rem"/></a>
    </div>
</div>

<div class="ch-container">

    <div class="row">
        <div class="row">
            <!-- style="font-size:14px;line-height:102px;padding-left:52px;" -->
            <div class="col-md-12 left login-header" style="font-size: .8rem">
            <h4>重置密码</h4>
            </div>
            <!--/span-->
        </div><!--/row-->
    <div class="row" style="padding: 3.8rem 0rem 0rem 0rem">
        <div class="well col-md-12 left login-box" style="padding: 0em 0em 0em 0em" >
            <form class="form-horizontal" action="${pageContext.request.contextPath}/user/reset_pwd.do" method="post">
                <input type="hidden" name="id" value='<c:out value="${id}"/>' />
                <fieldset>
                    <div class="input-group input-group-lg">
                        <span class="input-group-addon" style="font-size: 1.4rem">新&nbsp;&nbsp;密&nbsp;&nbsp;码</span>
                        <input type="password"  id="password" name="password" class="form-control"
                               placeholder="请输入密码" required="required">
                    </div>
                    <div class="clearfix"></div><br>

                    <div class="input-group input-group-lg">
                        <span class="input-group-addon" style="font-size: 1.4rem">重复密码</span>
                        <input type="password" class="form-control" name="againpwd" id="againpwd"
                               required="required" placeholder="请输入密码">
                    </div>
                    <div class="mess">${mess}</div>  
                    <div class="clearfix"></div>

                    <!--div class="input-prepend">
                        <label class="remember" for="remember"><input type="checkbox" id="remember"> Remember me</label>
                    </div -->
                    <div class="clearfix"></div>
                    <div style="padding: 0em 0em 0rem 0rem">
                        <div class="left col-md-6 resetbtn" style="padding: 0em 0em 0em 0em">
                            <input type="submit" class="btn btn_submit w_248" style="background-color:#3594cb;color: white"
                                   value="重置密码"/>
                        </div>
                        <div class="right col-md-6" style="padding: 0em 0em 0em 0em">
                            <input type="button" href="#" class="btn btn_cancel w_248"  style="background-color:#3594cb;color: white" value="取消"/>
                        </div>
                    </div>
                </fieldset>
            </form>
            <p class="textlinks">提示：为了保障帐号安全，请认准本页URL地址必须api.open.com开头</p>
        </div>
        <!--/span-->
    </div><!--/row-->
</div><!--/fluid-row-->

</div><!--/.fluid-container-->

</body>
</html>
