<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<!DOCTYPE HTML>
<html>
	<head>
		<meta charset="UTF-8">
		<title>部门用户信息列表</title>
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/themes/default/easyui.css">
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/themes/icon.css">
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/dataList.css">
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.min.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.easyui.min.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/highcharts/highcharts.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/highcharts/modules/exporting.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/locale/easyui-lang-zh_CN.js"></script>
	</head>
	<body >
		<div class="easyui-pannel">
			<div id="deptUsersWin" class="botton" style="margin-top:0;width:100%;height:500px;border:1px;" >
				<div id="tb" style="padding:10px 10px;">
					<span style="text-align:left;" hidden="true" >
						<input class="easyui-textbox" id="${deptID}" name="${deptName}" hidden="true">
					</span>
				</div>
				<table  id="deptUsers"  class="easyui-datagrid" title="部门用户信息列表"  data-options="toolbar:'#tb'" fit="true" >
				</table>
			</div>
		</div>
	</body>
	<script>
		//页面加载时触发
		$(function(){
				$('#deptUsers').datagrid({
					url: '${pageContext.request.contextPath}/department/findDeptUsers?deptID='+'${deptID}'+'&deptName'+'${deptName}',
					type:'post',
					collapsible:true,
					rownumbers:true,
					pagination:true,
		        	pagination: true,//显示分页工具栏
					fit:true,
					nowrap: false, //true 就会把数据显示在一行里
            		striped: true, //true奇偶行使用不同背景色
            		fitColumns: true, //true 真正的自动展开/收缩列的大小，以适应网格的宽度，防止水平滚动。
					rownumbers: true,
					singleSelect: false,
					columns:[[
									{ field: 'id', title: 'ID',align:'center',sortable:true,width:'10%',hidden:true},
									{ field: 'username', title: '用户名',align:'center',sortable:true,width:'15%'},
									{ field: 'realName', title: '真实姓名',align:'center',sortable:true,width:'15%'},
									{field: 'nickName', title: '昵称',align:'center',sortable:true,width:'15%'},
									{field: 'deptName', title: '所属部门',align:'center',sortable:true,width:'15%'},
									{field: 'create_Time', title: '注册时间',align:'center',sortable:true,width:'15%'},
									{field: 'lastLoginTime', title: '上次登录时间',align:'center',sortable:true,width:'15%'}
									]],
					onLoadSuccess:function(data){
						if (data.total<1){
                       		$.messager.alert("系统提示","没有符合查询条件的数据!","info");
                  		}                   
					}                
				});
		    
			 //设置分页控件 
		    var p = $('#deptUsers').datagrid('getPager'); 
		    $(p).pagination({ 
		        pageSize: 15,//每页显示的记录条数，默认为10 
		        pageList: [5,10,15,20],//可以设置每页记录条数的列表 
		        beforePageText: '第',//页数文本框前显示的汉字 
		        afterPageText: '页    共 {pages} 页', 
		        displayMsg: '当前显示 {from} - {to} 条记录   共 {total} 条记录', 
		        onBeforeRefresh:function(){
		            $(this).pagination('loading');
		            $(this).pagination('loaded');
		        } 
		    }); 
		});
		
		//弹出信息窗口 title:标题 msgString:提示信息 msgType:信息类型 [error,info,question,warning]
		function msgShow(title, msgString, msgType) {
			$.messager.alert(title, msgString, msgType);
		}
		
		// 查询部门用户方法
		function findDeptUsers(){
			//部门ID
			var deptID = $('#deptID').val().trim();
			
			$('#deptUsers').datagrid({
				collapsible:true,
				rownumbers:true,
				pagination:true,
		        url: "${pageContext.request.contextPath}/managerUser/findDeptUsers?deptID="+deptID,  
		        pagination: true,//显示分页工具栏
		        onLoadSuccess:function(data){
                    if (data.total<1){
                       $.messager.alert("系统提示","没有符合查询条件的数据!","info");
                  };
                }
		    }); 
		    
			 //设置分页控件 
		    var p = $('#deptUsers').datagrid('getPager'); 
		    $(p).pagination({ 
		        pageSize: 15,//每页显示的记录条数，默认为10 
		        pageList: [5,10,15,20],//可以设置每页记录条数的列表 
		        beforePageText: '第',//页数文本框前显示的汉字 
		        afterPageText: '页    共 {pages} 页', 
		        displayMsg: '当前显示 {from} - {to} 条记录   共 {total} 条记录', 
		        onBeforeRefresh:function(){
		            $(this).pagination('loading');
		            $(this).pagination('loaded');
		        } 
		    }); 
		}
        
	</script>
</html>