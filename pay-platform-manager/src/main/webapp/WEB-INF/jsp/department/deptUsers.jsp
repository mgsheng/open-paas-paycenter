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
		<style type="text/css">
			#father {
			    position: absolute;
			    left:30px;
			    right:30px;
			    top:30px;
			    bottom:30px;
			    overflow:auto;
			}
		</style>
	</head>
	<body >
		<div id="father">
			<div class="botton" style="margin-top:0px;width:100%;height:300px">
				<table  id="dg"  class="easyui-datagrid" title="部门用户信息列表"  style="width:100%;max-width:100%;padding:20px 30px;"
					data-options="singleSelect:true,method:'get'">
					<thead>
						<tr>
							<th data-options="field:'id',align:'center'" hidden="true" style="width:15%;max-width:100%;">ID</th>
							<th data-options="field:'password',align:'center'" hidden="true" style="width:15%;max-width:100%;">密码</th>
							<th data-options="field:'username',align:'center'" style="width:15%;max-width:100%;">用&nbsp;&nbsp;户&nbsp;&nbsp;名</th>
							<th data-options="field:'realName',align:'center'" style="width:15%;max-width:100%;">真实姓名</th>
							<th data-options="field:'nickName',align:'center'" style="width:15%;max-width:100%;">昵&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;称</th>
							<th data-options="field:'create_Time',align:'center'" style="width:18%;max-width:100%;">注册时间</th>
							<th data-options="field:'lastLoginTime',align:'center'" style="width:18%;max-width:100%;">上次登陆时间</th>
							<th data-options="field:'deptName',align:'center'"  style="width:18%;max-width:100%;">所属部门</th>
							<th data-options="field:'deptID',align:'center'" hidden="true"  style="width:18%;max-width:100%;">所属部门ID</th>
						</tr>
					</thead>
				</table>
			</div>
		</div>	
		<div region="south" border="false" style="text-align:center; height: 50px; line-height: 50px;">
			<a id="btnEp" class="easyui-linkbutton" icon="icon-ok" href="javascript:void(0)" onclick="updateUser()" style="margin:8px"> 确定</a>
			<a id="btnCancel" class="easyui-linkbutton" onclick="closeWin()" icon="icon-cancel" href="javascript:void(0)" style="margin:8px">取消</a>
			<a id="btnClear" href="javascript:void(0)" onclick="clearTable()" class="easyui-linkbutton" icon="icon-clear" style="margin:8px">清空</a>
		</div>
	</body>
	<script>
		//页面加载时触发
		$(findDeptUsers());
		
		//弹出信息窗口 title:标题 msgString:提示信息 msgType:信息类型 [error,info,question,warning]
		function msgShow(title, msgString, msgType) {
			$.messager.alert(title, msgString, msgType);
		}
		
		//列表重新加载
		function reload(url,name){
			$('#dg').datagrid('reload',{
	            url: url, queryParams:{ name:name}, method: "post"
	          }); 
		}
		
		// 查询部门用户方法
		function findDeptUsers(){
			//部门ID
			var deptID = $('#deptID').val().trim();
			
			$('#dg').datagrid({
				collapsible:true,
				rownumbers:true,
				pagination:true,
		        url: "${pageContext.request.contextPath}/managerUser/findDeptUsers?deptID="+deptID,  
		        pagination: true,//显示分页工具栏
		        onLoadSuccess:function(data){
                    if (data.total<1){
                       $.messager.alert("系统提示","没有符合查询条件的数据!","info");
                  }
                }
		    }); 
		    
			 //设置分页控件 
		    var p = $('#dg').datagrid('getPager'); 
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