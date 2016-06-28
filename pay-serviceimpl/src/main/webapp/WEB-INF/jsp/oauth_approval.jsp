<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<!DOCTYPE HTML>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">    
<title>授权页</title>
<link rel="stylesheet" type="text/css" href="../css/style.css"/>
<script type="text/javascript" src="../js/jquery-1.7.1.min.js"></script>
<style type="text/css">
	form{display:inline;}
</style>
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
<!--header start-->
<div class="open_header">
	<div class="open_header_inner">
    	<div class="open_logo"><img src="../images/open_logo.png" /></div>
        <ul class="licensnav">            
        	<li><a href="javascript:void(0);" url="${pageContext.request.contextPath}/user/logout" id="logout">换个帐号</a></li>
        	<li><a href="${appLoginUrl}">我的应用</a></li>
       		<li><a href="#"><sec:authentication property="principal.username" /> </a></li>
        </ul>
    </div>
</div>
<!--header end-->

<div class="open_main">
	<div class="open_content clearfix">
    	<div class="licensbox">
        	<div class="licenspic">
        	<c:choose>
       		<c:when test="${empty icon || icon==''}">
        		<img width="128px" height="128px" src="../images/licenspic.jpg" />
        	</c:when>
       		<c:otherwise>
        		<img width="128px" height="128px" src="${icon}" />
        	</c:otherwise>
        	</c:choose>
        	</div>
            <ul class="licenslist">
            	<li>将允许奥鹏教育进行以下操作：</li>
            	<li><i class="arr01"></i>获得你的个人信息，好友关系</li>
            	<li><i class="arr02"></i>分享内容到你的微博</li>
            	<li><i class="arr03"></i>获得你的评论</li>
            </ul>
            <div class="btnbox">
            	<form id='confirmationForm' name='confirmationForm' action='${pageContext.request.contextPath}/oauth/authorize' method='post'>
				    <input name='user_oauth_approval' value='true' type='hidden'/>
				    <label><input name='authorize' value='连接' type='submit' class="btn btn_submit w_173"/></label>
				</form>
				<form id='denialForm' name='denialForm' action='${pageContext.request.contextPath}/oauth/authorize' method='post'>
				    <input name='user_oauth_approval' value='false' type='hidden'/>
				    <label><input name='deny' value='取消' type='submit' class="btn btn_submit w_173"/></label>
				</form>
            </div>       
            <p class="textlinks">提示：为了保障帐号安全，请认准本页URL地址必须api.open.com开头</p>
        </div>
    </div>
</div>
</body>
</html>
