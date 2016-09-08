<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<!DOCTYPE HTML>
<html>
	<head>
		<meta charset="UTF-8">
		<title>用户信息列表</title>
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/themes/default/easyui.css">
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/themes/icon.css">
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/dataList.css">
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.min.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.easyui.min.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/highcharts/highcharts.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/highcharts/modules/exporting.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/locale/easyui-lang-zh_CN.js"></script>
		<style type="text/css">
			table {
				border: 1px solid;
				border-color: grey;
			}
		</style>
	</head>
	<body >
			<div style="border:0px solid;border-radius:8px;margin-bottom:25px;height: 80%; width: 100%;">
				<div class="top" style="width: 100%">
					<div class="easyui-panel" title="查询条件" style="width:100%;max-width:100%;padding:20px 25px;">
						<form id="fm" method="post" action="/managerUser/findUsers">
							<div>
								<span style="margin-left: 20px"> 
										<input class="easyui-textbox" name="username" id="un" label="用&nbsp;户&nbsp;名：" 
											prompt="选填" style="width:200px"></input> 
								</span> 
								<span style="margin-left: 20px"> 
										<input class="easyui-textbox" name="realname" id="rn" label="真实姓名：" prompt="选填"  
											style="width:200px"></input> 
								</span> 
								<span style="margin-left: 20px"> 
										<input class="easyui-textbox" name="nickname" id="nn" label="昵&nbsp;&nbsp;&nbsp;
											&nbsp;称：" prompt="选填" style="width:200px"></input> 
								</span> 
								<span style="margin-left:20px;">
									<a href="javascript:void(0)" class="easyui-linkbutton" onclick="findUsers();" style="width: 100px;">
										<span style="font-weight:bold;margin-right:10px;margin-left:10px;">查&nbsp;&nbsp;&nbsp;&nbsp;询</span>
										<span class="icon-search">&nbsp;&nbsp;&nbsp;&nbsp;</span>
									</a>
								</span>
								<span style="margin-left:20px">
									<a href="${pageContext.request.contextPath}/managerUser/showAdd" class="easyui-linkbutton"  
										style="width: 100px;">
										<span style="font-weight:bold;margin-right:5px;margin-left:5px;">添加用户</span>
										<span class="icon-add">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span>
									</a>
								</span>
								<span style="margin-left:20px;">
									<a href="javascript:void(0)" class="easyui-linkbutton" onclick="clearForm();" style="width: 100px;">
										<span style="font-weight:bold;margin-right:8px;margin-left:10px;">清&nbsp;&nbsp;
											&nbsp;&nbsp;除</span>
										<span class="icon-clear">&nbsp;&nbsp;&nbsp;&nbsp;</span>
									</a>
								</span>
							</div>
						</form>
					</div>
				</div>
			</div>
			<div class="botton" style="width: 100%;height: 300px;">
				<table  id="dg"  class="easyui-datagrid" title="查询结果"    style="width:100%;max-width:100%;border:false;"
					data-options="singleSelect:true,method:'get'">
					<thead>
						<tr>
							<th data-options="field:'id',width:200,align:'center'" hidden="true">ID</th>
							<th data-options="field:'password',width:200,align:'center'" hidden="true">密码</th>
							<th data-options="field:'username',width:200,align:'center'">用&nbsp;&nbsp;户&nbsp;&nbsp;名</th>
							<th data-options="field:'realName',width:200,align:'center'">真实姓名</th>
							<th data-options="field:'nickName',width:200,align:'center'">昵&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;称</th>
							<th data-options="field:'createTime',width:200,align:'center'">注册时间</th>
							<th data-options="field:'lastLoginTime',width:200,align:'center'">上次登陆时间</th>
							<th data-options="field:'foundDate',width:200,align:'center',formatter:formatOper">
								<span style="font-weight:bold;margin-left:40px;margin-right:20px;">操&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;作</span>
								<span class="icon-edit">&nbsp;&nbsp;&nbsp;&nbsp;</span>
								</th>
						</tr>
				</thead>
			</table>
		</div>
		
	<!--修改用户窗口--> 
	<div id="upda" class="easyui-window" title="用户信息" collapsible="false"
		minimizable="false" maximizable="false" icon="icon-save"
		style="width: 300px; height: 150px; padding: 5px;
        background: #fafafa;">
		<div class="easyui-layout" fit="true">
			<div region="center" border="false"
				style="background: #fff; border: 1px solid #ccc;">
				<table cellpadding="5px" id="tb"  style="border: 0px;margin:30px 10px" >
					<tr>
						<td>真实姓名：</td>
						<td><input id="realname" type="text" class="txt01" name="realname" class="easyui-textbox" />
						</td>
					</tr>
					<tr>
						<td>昵&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;称：</td>
						<td><input id="nickname" type="text" class="txt01" name="nickname" class="easyui-textbox" />
						</td>
					</tr>
				</table>
			</div>
			<div region="south" border="false" style="text-align:center; height: 50px; line-height: 50px;">
				<a id="btnEp" class="easyui-linkbutton" icon="icon-ok" href="javascript:void(0)" onclick="updateUser()"> 确定</a>
				<a id="btnCancel" class="easyui-linkbutton" onclick="closeWin()" icon="icon-cancel" href="javascript:void(0)">取消</a>
				<a id="btnClear" href="javascript:void(0)" onclick="clearTable()" class="easyui-linkbutton" icon="icon-clear" style="width:80px;margin:10px 15px">清空</a>
			</div>
		</div>
	</div>
	</body>
	<script>
	
		 //设置修改用户窗口
        function win() {
            $('#upda').window({
                title: '用户信息',
                width: 300,
                modal: true,
                shadow: true,
                closed: true,
                height: 260,
                resizable:false
            });
        }
        
          // 清除查询表单
		function clearForm(){
			$('#fm').form('clear');
		}
		
		// 清空修改用户窗口
		function clearTable(){
			$('#tb').form('clear');
		};
		
		win();
		//打开修改用户窗口
		function openWin(){
			//打开修改用户窗口之前先清空
			clearTable();
			$('#upda').window('open');
		};
		//关闭修改用户窗口
		function closeWin(){
			$('#upda').window('close');
		};
		
		//弹出信息窗口 title:标题 msgString:提示信息 msgType:信息类型 [error,info,question,warning]
		function msgShow(title, msgString, msgType) {
			$.messager.alert(title, msgString, msgType);
		}
	
		//追加操作列
		function formatOper(val,row,index){  
		    return  '<a href="#" onclick="removeUserByID('+index+')">删除</a>&nbsp;<a style="margin-left:20px" href="#" onclick="openUp('+index+')">修改</a>';  
		}  
		
		
		//修改用户信息
		function openUp(index){
			var row = $('#dg').datagrid('getSelected');
			var id=row.id;  
			openWin();
		}  
		
		//根据用户ID删除用户
		function removeUserByID(){
			var row = $('#dg').datagrid('getSelected');
			if (row){
			$.messager.confirm('系统提示', '是否确定删除?', function(r){
				if (r){
					   var id=row.id;
					   var url='${pageContext.request.contextPath}/managerUser/removeUserByID?id='+id;
			            $.post(url, function(data) {
			                if(data.result==true){
			                 	msgShow('系统提示', '恭喜，删除成功！', 'info');
			                }else{
			                  	msgShow('系统提示', '删除失败！', 'error');
			                }
			            });
			              //刷新
			              var url='${pageContext.request.contextPath}/managerUser/removeUserByID';
			              reload(url,name);
				}
			   });
			}
		}
		
		//列表重新加载
		function reload(url,name){
			$('#dg').datagrid('reload',{
	            url: url, queryParams:{ name:name}, method: "post"
	          }); 
		}
		
		// 查询用户方法
		function findUsers(){
			//用户名
			var username = $('#un').val().trim();
			//真实姓名
			var realname = $('#rn').val().trim();
			//昵称
			var nickname = $('#nn').val().trim();
			
			$('#dg').datagrid({
				collapsible:true,
				rownumbers:true,
				pagination:true,
		        url: "${pageContext.request.contextPath}/managerUser/findUsers?username="+username+"&realname="
		        	+realname+"&nickname="+nickname,  
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
        
        // 提交用户信息
         function updateUser() {
		 	var row = $('#dg').datagrid('getSelected');
			var id=row.id;
            var $realname = $('#realname').val().trim();
            var $nickname = $('#nickname').val().trim();
            if ($realname == '') {
                msgShow('系统提示', '请输入真实姓名！', 'warning');
                return false;
            }
            if ($nickname == '') {
                msgShow('系统提示', '请输入昵称！', 'warning');
                return false;
            }
            var url=encodeURI('${pageContext.request.contextPath}/managerUser/updateUser?realname='+$realname+'&nickname='+$nickname+'&id='+id);
           	if(row){
	            $.post(url, function(data) {
	                if(data.result==true){
		                 msgShow('系统提示', '修改成功！', 'info');
		                 closeWin();
		                 var url='${pageContext.request.contextPath}/managerUser/updateUser';
			             reload(url,name);
	                }else{
		                 msgShow('系统提示', '修改失败！', 'info');
		                 closeWin();
	                }
	            });
            }else{
            	msgShow('系统提示', '请选择修改用户！', 'info');
            }
        }
	</script>
</html>