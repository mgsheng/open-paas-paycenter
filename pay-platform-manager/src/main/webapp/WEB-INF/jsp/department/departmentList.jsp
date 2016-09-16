<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<!DOCTYPE HTML>
<html>
	<head>
		<meta charset="UTF-8">
		<title>部门信息列表</title>
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
			<div>
				<div style="border:0px solid;border-radius:8px;margin-bottom:0px;width: 100%;max-width:100%;">
					<div class="top" style="width: 100%">
						<div class="easyui-panel" title="查询条件" style="width:100%;max-width:100%;padding:20px 25px;">
							<form id="fm" method="post" action="/department/findDepts">
								<table cellpadding="5px">
									<tr>
										<td>
												<input class="easyui-textbox" name="deptname" id="deptname" label="部&nbsp;门&nbsp;名：" 
													prompt="选填" style="width:200px"></input> 
										</td>
										<td>	
											<a href="javascript:void(0)" class="easyui-linkbutton" onclick="findDepts();" style="width: 120px;margin-left:30px;">
												<span style="font-weight:bold;margin-right:15px;margin-left:15px;">查&nbsp;&nbsp;&nbsp;&nbsp;询</span>
												<span class="icon-search">&nbsp;&nbsp;&nbsp;&nbsp;</span>
											</a>
										</td>
										<td>	
											<a href="${pageContext.request.contextPath}/department/showAddDept" class="easyui-linkbutton"  
												style="width: 120px;margin-left:30px;">
												<span style="font-weight:bold;margin-right:15px;margin-left:15px;">添加部门</span>
												<span class="icon-add">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span>
											</a>
										</td>
										<td>	
											<a href="javascript:void(0)" class="easyui-linkbutton" onclick="clearForm();" style="width:120px;margin-left:30px;">
												<span style="font-weight:bold;margin-right:15px;margin-left:15px;">清&nbsp;&nbsp;
													&nbsp;&nbsp;除</span>
												<span class="icon-clear">&nbsp;&nbsp;&nbsp;&nbsp;</span>
											</a>
										</td>	
									</tr>
								</table>
							</form>
						</div>
					</div>
				</div>
				<div class="botton" style="margin-top:0px;width:100%;height:300px">
					<table  id="dg"  class="easyui-datagrid" title="查询结果"  style="width:100%;max-width:100%;padding:20px 30px;"
						data-options="singleSelect:true,method:'get'">
						<thead>
							<tr>
								<th data-options="field:'id',align:'center'" hidden="true" style="width:15%;max-width:100%;">ID</th>
								<th data-options="field:'deptName',align:'center'" style="width:15%;max-width:100%;">部&nbsp;&nbsp;门&nbsp;&nbsp;名</th>
								<th data-options="field:'create_Time',align:'center'" style="width:18%;max-width:100%;">注册时间</th>
								<th data-options="field:'foundDate',align:'center',formatter:formatOper"  style="width:18%;max-width:100%;">
									<span style="font-weight:bold;margin-left:2%;margin-right:2%;">操&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;作</span>
									<span class="icon-edit">&nbsp;&nbsp;&nbsp;&nbsp;</span>
									</th>
							</tr>
					</thead>
				</table>
			</div>
		</div>
	</div>	
	<!--修改用户窗口--> 
	<div id="upda" class="easyui-window" title="部门信息" collapsible="false"
		minimizable="false" maximizable="false" icon="icon-save"
		style="width: 300px; height: 150px;
        background: #fafafa;">
		<div class="easyui-layout" fit="true">
			<div region="center" border="false"
				style="background: #fff; border: 1px solid #ccc;">
				<table cellpadding="10px" id="tb"  style="border: 0px;margin:10px 10px" >
					<tr>
						<td>部门名：</td>
						<td><input id="dept_name" type="text" class="txt01" name="dept_name" class="easyui-textbox" />
						</td>
					</tr>
					<tr>
						<td>注册时间：</td>
						<td><input id="create_time" type="text" class="txt01" name="create_time" class="easyui-textbox" readonly/>
						</td>
					</tr>
				</table>
			</div>
			<div region="south" border="false" style="text-align:center; height: 50px; line-height: 50px;">
				<a id="btnEp" class="easyui-linkbutton" icon="icon-ok" href="javascript:void(0)" onclick="updateDept()" style="margin:8px"> 确定</a>
				<a id="btnCancel" class="easyui-linkbutton" onclick="closeWin()" icon="icon-cancel" href="javascript:void(0)" style="margin:8px">取消</a>
				<a id="btnClear" href="javascript:void(0)" onclick="clearTable()" class="easyui-linkbutton" icon="icon-clear" style="margin:8px">清空</a>
			</div>
		</div>
	</div>
	</body>
	<script>
	
		 //设置修改部门窗口
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
		
		// 清空修改部门窗口
		function clearTable(){
			$('#tb').form('clear');
		};
		
		win();
		//打开修改部门窗口
		function openWin(){
			//打开修改部门窗口之前先清空
			clearTable();
			var row = $('#dg').datagrid('getSelected');
			if (row){
				var deptName = row.deptName;
				var create_Time = row.create_Time;
				$('#dept_name').val(deptName);
				$('#create_time').val(create_Time);
				$('#upda').window('open');
			}else{
            	msgShow('系统提示', '请选择要修改的部门！', 'info');
            }
		};
		//关闭修改部门窗口
		function closeWin(){
			$('#upda').window('close');
		};
		
		//弹出信息窗口 title:标题 msgString:提示信息 msgType:信息类型 [error,info,question,warning]
		function msgShow(title, msgString, msgType) {
			$.messager.alert(title, msgString, msgType);
		}
	
		//追加操作列
		function formatOper(val,row,index){  
		    return  '<a href="#" onclick="removeDeptByID('+index+')">删除</a>&nbsp;<a style="margin-left:20px" href="#" onclick="openWin()">修改</a>';  
		}  
		
		//根据部门ID删除部门
		function removeDeptByID(){
			var row = $('#dg').datagrid('getSelected');
			if (row){
			$.messager.confirm('系统提示', '是否确定删除?', function(r){
				if (r){
					   var id=row.id;
					   var url='${pageContext.request.contextPath}/department/removeDeptByID?id='+id;
			            $.post(url, function(data) {
			                if(data.result==true){
			                 	msgShow('系统提示', '恭喜，删除成功！', 'info');
			                }else{
			                  	msgShow('系统提示', '删除失败！', 'error');
			                }
			            });
			              //刷新
			              var url='${pageContext.request.contextPath}/department/removeDeptByID';
			              reload(url,name);
				}
			   });
			}else{
            	msgShow('系统提示', '请选择要删除的用户！', 'info');
            }
		}
		
		//列表重新加载
		function reload(url,name){
			$('#dg').datagrid('reload',{
	            url: url, queryParams:{ name:name}, method: "post"
	          }); 
		}
		
		// 查询部门方法
		function findDepts(){
			//部门名
			var dept_name = $('#deptname').val().trim();
			//创建时间
			//var createtime = $('#createtime').val().trim();
			
			$('#dg').datagrid({
				collapsible:true,
				rownumbers:true,
				pagination:true,
		        url: "${pageContext.request.contextPath}/department/findDepts?dept_name="+dept_name,  
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
        
        // 提交修改后的部门信息
         function updateDept() {
		 	var row = $('#dg').datagrid('getSelected');
			var id=row.id;
            var dept_name = $('#dept_name').val().trim();
            if (dept_name == '') {
                msgShow('系统提示', '请输入部门名！', 'warning');
                return false;
            }
            alert(id);
            var url=encodeURI('${pageContext.request.contextPath}/department/updateDept?dept_name='+dept_name+'&id='+id);
            $.post(url, function(data) {
                if(data.result==true){
	                 msgShow('系统提示', '修改成功！', 'info');
	                 closeWin();
	                 var url='${pageContext.request.contextPath}/department/updateDept';
		             reload(url,name);
                }else if(data.result==false){
                	msgShow('系统提示', '该部门名已被注册！', 'error');
	                 closeWin();
	                 var url='${pageContext.request.contextPath}/department/updateDept';
		             reload(url,name);
                }else{
	                 msgShow('系统提示', '修改失败！', 'error');
	                 closeWin();
                }
            });
        }
	</script>
</html>