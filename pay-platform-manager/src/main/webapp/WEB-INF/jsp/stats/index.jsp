<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<!DOCTYPE HTML>
<html>
<head>
	<meta charset="UTF-8">
	<title>Basic DataGrid - jQuery EasyUI Demo</title>
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/themes/default/easyui.css">
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/themes/icon.css">
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/dataList.css">
	<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.min.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.easyui.min.js"></script>
</head>
<body>
	<div style="border:1px solid;border-radius:8px;margin-bottom: 10px; border-color: red ;height: 150px; width: 80%">
	<div style="border:1px solid; border-color: green ;height: 30px;display:inline-block; width: 420px; margin-left:55%">
	<a href="#" class="easyui-linkbutton" data-options="plain:true">7天</a>
	<a href="#" class="easyui-linkbutton" data-options="plain:true">30天</a>
	<a href="#" class="easyui-linkbutton" data-options="plain:true">累计</a>
	<div style="margin-bottom:20px;display:inline-block;">
			<input class="easyui-datebox" data-options="onSelect:onSelect" style="width:100%;">
		</div>
	<div style="margin-bottom:20px;display:inline-block;">
			<input class="easyui-datebox" data-options="onSelect:onSelect" style="width:100%;">
		</div>
	</div>
	<div style="border:1px solid;margin-bottom: 10px; border-color: blue ;height: 10px;display:inherit; margin-top: 10px">
	<table>
	</table>
	</div>
	</div>
	<div style="margin:20px 0;"></div>
	
	<div style="margin:20px 0 10px 0;"></div>
	<div class="easyui-tabs" style="width:700px;height:250px">
		<div title="About" style="padding:10px">
			<p style="font-size:14px">jQuery EasyUI framework helps you build your web pages easily.</p>
			<ul>
				<li>easyui is a collection of user-interface plugin based on jQuery.</li>
				<li>easyui provides essential functionality for building modem, interactive, javascript applications.</li>
				<li>using easyui you don't need to write many javascript code, you usually defines user-interface by writing some HTML markup.</li>
				<li>complete framework for HTML5 web page.</li>
				<li>easyui save your time and scales while developing your products.</li>
				<li>easyui is very easy but powerful.</li>
			</ul>
		</div>
		<div title="My Documents" style="padding:10px">
			<ul class="easyui-tree" data-options="url:'tree_data1.json',method:'get',animate:true"></ul>
		</div>
		<div title="Help" data-options="iconCls:'icon-help',closable:true" style="padding:10px">
			This is the help content.
		</div>
	</div>

</body>
<script>
		function onSelect(date){
			$('#result').text(date);
		}
	</script>
</html>