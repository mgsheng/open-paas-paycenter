<%@ page import="org.springframework.security.web.savedrequest.DefaultSavedRequest" %>
<%@ page import="org.springframework.context.support.ClassPathXmlApplicationContext" %>
<%@ page import="org.springframework.context.ApplicationContext" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE HTML>
<html>
<head>
    <meta charset="utf-8">
    <title>登录页</title>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="Charisma, a fully featured, responsive, HTML5, Bootstrap admin template.">
    <meta name="author" content="Muhammad Usman">

    <!-- The styles -->
    <link rel="stylesheet" type="text/css" href="css/style.css"/>
    <link rel="stylesheet" type="text/css" href="css/charisma-app.css"/>
    <link id="bs-css" href="css/bootstrap.css" rel="stylesheet">
    <link href="css/bootstrap-cerulean.min.css" rel="stylesheet">
    <link href="css/login-mobile.css" rel="stylesheet">

    <!-- jQuery -->
    <script src="js/jquery-1.7.1.min.js"></script>
    <script src="js/charisma.js"></script>

    <!-- The HTML5 shim, for IE6-8 support of HTML5 elements -->
    <!--[if lt IE 9]>
    <!--script src="http://html5shim.googlecode.com/svn/trunk/html5.js"></script>
    <![endif]--   .navbar-default{background-color:#000000;border-color:#E4F8F8}>

    <!-- The fav icon -->
    <link rel="shortcut icon" href="img/favicon.ico">
</head>

<body style="font-size: 70%">

<div class="nbar nbar-default" >
    <div>
        <a href="index.html">
            <!--width: 28.6rem;height: 8.5rem-->
            <img alt="Open Logo" src="images/open_logo.png" style="width: 15rem;height: 4.5rem"/></a>
        <div class="btn-group pull-right">
            <button type="button" class="btn btn-default right">
                <a href="http://${pageContext.request.remoteAddr}:8080/spring-oauth-client/register"
                   class="center-text" >注册
                </a>
            </button>
        </div>
    </div>
</div>

<div class="ch-container">

    <div class="row">
        <c:if test="${!empty param.appname}">
            <div class="row">
                <!-- style="font-size:14px;line-height:102px;padding-left:52px;" -->
                <div class="col-md-12 left login-header" style="font-size: .8rem">
                <h4>用您的账号访问${param.appname},并自动登录</h4>
                </div>
                <!--/span-->
            </div><!--/row-->
        </c:if>

    <div class="row" style="padding: 3.8rem 0rem 0rem 0rem">
        <div class="well col-md-12 left login-box" style="padding: 0em 0em 0em 0em" >
            <form class="form-horizontal" action="${pageContext.request.contextPath}/login.do" method="post">
                <fieldset>
                    <div class="input-group input-group-lg">
                        <span class="input-group-addon" style="font-size: 1.4rem">账号</span>
                        <input type="text"  id="username" name="j_username" class="form-control"
                               placeholder="请用微博账号登录" required="required">
                    </div>
                    <div class="clearfix"></div><br>

                    <div class="input-group input-group-lg">
                        <span class="input-group-addon" style="font-size: 1.4rem">密码</span>
                        <input type="password" class="form-control" name="j_password" id="password"
                               required="required" placeholder="请输入密码">
                    </div>
                    <div class="clearfix"></div>

                    <!--div class="input-prepend">
                        <label class="remember" for="remember"><input type="checkbox" id="remember"> Remember me</label>
                    </div -->
                    <div class="clearfix"></div>
                    <div style="padding: 0em 0em 0rem 0rem">
                        <div class="left col-md-6" style="padding: 0em 0em 0em 0em">
                            <input type="submit" class="btn btn_submit w_248" style="background-color:#3594cb;color: white"
                                   value="登录"/>
                        </div>
                        <div class="right col-md-6" style="padding: 0em 0em 0em 0em">
                            <input type="button" href="#" class="btn btn_cancel w_248"  style="background-color:#3594cb;color: white" value="取消"/>
                        </div>
                    </div>
                </fieldset>
            </form>
            <span class="text-danger">
			        <c:if test="${param.authorization_error eq 2}">Access denied !!!</c:if>
			        <c:if test="${param.authentication_error eq 1}">Authentication Failure</c:if>
			        </span>
            <p class="textlinks">提示：为了保障帐号安全，请认准本页URL地址必须api.open.com开头</p>
        </div>
        <!--/span-->
    </div><!--/row-->
</div><!--/fluid-row-->

</div><!--/.fluid-container-->

</body>
</html>
