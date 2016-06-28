<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE HTML>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />	
    <title>用户中心</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/style2.css" />
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/base23a479.css" />
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/page_news21b0c8.css" />
    <link href="${pageContext.request.contextPath}/images/open_logo.ico" rel="Shortcut Icon" />
</head>
<body>
	<%@ include file="dev_head.jsp"%>
	<div class="body" style="margin-top:10px">
		<div class="inner wrp ">
			<div class="container_box cell_layout">
				<div class="container_bd">
					<div class="col_main">
						<div class="article_box " >
							<div class="inner" align="center">
								<h3>
									${title}
								</h3>
								<div id="content" style="text-align: left;">
									${contents}
								</div>
								<p class="sign">
									奥鹏团队
									<br/>
									<span id="time">${releaseTime}</span>
								</p>
							</div>
						</div>
						<div class="tool_bar border tc">
							<a href="/" class="btn btn_default">返回首页</a>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
	<%@ include file="dev_foot.jsp"%>
	<script src="${pageContext.request.contextPath}/js/jquery-1.10.1.min.js" type="text/javascript"></script>
</body>
</html>