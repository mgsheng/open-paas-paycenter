<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<!DOCTYPE HTML>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <meta name="viewport" content="width=device-width,initial-scale=1">
    <!--meta name="screen-orientation" content="portrait">
    <meta name="x5-orientation" content="portrait" -->
    <meta name="description" content="Charisma, a fully featured, responsive, HTML5, Bootstrap admin template.">
    <title>授权页</title>
    <!-- The styles -->
    <link rel="stylesheet" type="text/css" href="../css/style.css"/>
    <link rel="stylesheet" type="text/css" href="../css/style1.css"/>
    <link rel="stylesheet" type="text/css" href="../css/charisma-app.css"/>
    <link id="bs-css" href="../css/bootstrap.css" rel="stylesheet">
    <link href="../css/oauth_approval-mobile.css" rel="stylesheet">
    <!-- jQuery -->
    <script src="../js/jquery-1.7.1.min.js"></script>
    <script src="../js/charisma.js"></script>
    <script>
        $(document).ready(function(){
            var url=window.location.href;
            $("#logout").click(function(){
                var u=$(this).attr("url");
                url=u+"?"+url;
                url=url.replace(/\%2F/g,"/");
                url=url.replace(/\%3A/g,":");
                location.href=url;
            });
        });
    </script>

</head>

<body>
<div class="nbar nbar-default">
    <div>
        <div class="logo">
            <a href="index.html"><img alt="Open Logo" src="../images/open_logo.png" class="img-responsive" style="width: 15rem;height: 4.5rem"/></a></div>

        <span class="menu">
            <img src="../images/iconMenu.png" alt="click me"/></span>
        <div class="navg">
            <nav class="cl-effect-1">
                <ul class="res">
                    <li><a href="javascript:void(0);" url="${pageContext.request.contextPath}/user/logout" id="logout"
                           class="active scroll">换个帐号</a></li>
                    <li><a class="scroll" href="#">我的应用</a></li>
                    <li><a class="scroll" href="#"><sec:authentication property="principal.username" /></a></li>
                </ul>
            </nav>
            <script>
                $( "span.menu").click(function() {
                    $(  "ul.res" ).slideToggle("slow", function() {
                        // Animation complete.
                    });
                });
            </script>
        </div>
    </div>
</div>

<div class="open_main ch-container" >
    <div class="clearfix row">
        <div class="col-md-12">
            <div class="center">
                <c:choose>
                    <c:when test="${empty icon || icon==''}">
                        <img src="../images/licenspic.jpg" />
                    </c:when>
                    <c:otherwise>
                        <img src="${icon}" />
                    </c:otherwise>
                </c:choose>
            </div>
            <div class="col-md-12 center" style="padding: .1rem;">
                <ul class="licenslist1">
                    <li>将允许奥鹏教育进行以下操作：</li>
                    <li><i class="arr01"></i>获得你的个人信息，好友关系</li>
                    <li><i class="arr02"></i>分享内容到你的微博</li>
                    <li><i class="arr03"></i>获得你的评论</li>
                </ul>
            </div>
            <div class="col-md-12 center">
                <form id='confirmationForm' name='confirmationForm'
                      action='${pageContext.request.contextPath}/oauth/authorize' method='post' class="form-horizontal">
                    <input name='user_oauth_approval' value='true' type='hidden'/>
                    <label><input name='authorize' value='连接' type='submit' class="btn btn_submit w_173" style="background-color:#3594cb;color: white"/></label>
                </form>
                <form id='denialForm' name='denialForm' action='${pageContext.request.contextPath}/oauth/authorize'
                      method='post' class="form-horizontal">
                    <input name='user_oauth_approval' value='false' type='hidden'/>
                    <label><input name='deny' value='取消' type='submit' class="btn btn_submit w_173" style="background-color:#3594cb;color: white"/></label>
                </form>
            </div>
            <div class="center">
                <p>提示：为了保障帐号安全，请认准本页URL地址必须api.open.com开头</p>
            </div>
        </div>
    </div>
</div>
</body>
</html>
