<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE HTML>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>登录成功显示页</title>
<link rel="stylesheet" type="text/css" href="css/style.css"/>
<style type="text/css">
	table{width:60%;border:1px solid black;}
</style>
</head>

<body>

<div class="open_main">
	<div class="open_content clearfix">
    	<p>Json形式：</p>
    	<p>${json}</p>   
    	<p>对象形式：</p>
    	<table>
    		<thead><tr><td>Name</td><td>CallbackUrl</td><td>SourceId</td></tr></thead>
    		<tbody>
    			<c:forEach var="app" items="${infoList}" varStatus="status">
    			<tr><td>${app.name}</td><td>${app.callbackUrl }</td><td>${app.sourceId }</td></tr>
    			</c:forEach>
    		</tbody>
    	</table> 	
    </div>
</div>
</body>
</html>