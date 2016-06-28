<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE HTML>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title>用户中心</title>
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/base23a479.css" />
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/style2.css" />
	<link href="${pageContext.request.contextPath}/images/open_logo.ico" rel="Shortcut Icon" />
</head>
<body class="page_setting dev page_introduction">
	<%@ include file="/dev_head.jsp"%>
	<div class="body">
		<div class="inner wrp">
			<div class="container_box">
			<div class="main_bd" style="height: 200px;font-size:25px"></div>
			<font size=""></font>
			</div>
		</div>
	</div>
	<%@ include file="/dev_foot.jsp"%>
	<script src="${pageContext.request.contextPath}/js/jquery-1.10.1.min.js" type="text/javascript"></script>
	<script type="text/javascript">
		var time = 5;
		var flag = 'false';
		var errorStr = '';
		jQuery(document).ready(function() {
			flag = '${flag}';
			if('${errorCode}' == 'time_out'){
				errorStr = '链接已过期！请重新激活。';
			}else if('${errorCode}' == 'invalid_code'){
				errorStr = '无效的参数！';
			}
			backIndex('${pageContext.request.contextPath}');
		});

		function backIndex(path){
			if(time > 0){
				setTimeout("backIndex('"+path+"')", 1000);
				if(flag=='true'){
					$('.main_bd').html('成功激活邮箱！<font size="3"><font color=red id="timeFont">'+time+'</font>秒后为您跳转到首页。<a href="'+path+'/main">立即跳转</a></font>');
				}else{
					$('.main_bd').html('激活失败！'+errorStr+'<font size="3"><font color=red id="timeFont">'+time+'</font>秒后为您跳转到首页。<a href="'+path+'/main">立即跳转</a></font>');
				}
				time=time-1;
			}else{
				window.location.href=path+'/main';
			}
		}
	</script>
</body>
</html>
