<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
	<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1">
		<meta charset="utf-8" />
		<title>找回密码</title>
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/style3.css" />
		<link href="${pageContext.request.contextPath}/images/open_logo.ico" rel="Shortcut Icon" />
	</head>
	<body class="page_setting pwd page_introduction">
		<%@ include file="/dev_head.jsp"%>
		<!--找回密码 star-->
		<div class="layout">
			<div class="w_1200">
				<div class="getbackpwd">
					<div class="stepbar">
						<div class="first curr"><span class="num">1</span>选择密码找回方式</div>
						<div class="second"><span class="num">2</span>验证身份</div>
						<div class="third"><span class="num">3</span>修改密码</div>
					</div>
					<div class="getbackchoice">
						<div class="phone"><a href="${pageContext.request.contextPath}/findpwdphone"><span class="txtchoice">用绑定手机找回</span></a></div>
						<div class="email"><a href="${pageContext.request.contextPath}/findpwdemail"><span class="txtchoice">用绑定邮箱找回</span></a></div>
						<%-- <div class="problem"><a href="${pageContext.request.contextPath}/findpwdproblem"><span class="txtchoice">用密保问题找回</span></a></div> --%>
					</div>
				</div>
				<div style="text-align:center"><font color="red" >OES用户暂不支持邮箱找回</font></div>
			</div>
		</div>
		<!--找回密码 end-->
		<%@ include file="/dev_foot.jsp"%>
		<script type="text/javascript">
			jQuery(document).ready(function() {
				if('${flag}' == 'false'){
					var errorCode = '${errorCode}'
					if(errorCode == 'time_out'){
						alert('该激活链接已失效，请重新申请找回');
					}
					else if(errorCode == 'invalid_email' || errorCode == 'invalid_code'){
						alert('无效的请求，请重新申请找回')
					}
				}
			});
		</script>
	</body>
</html>