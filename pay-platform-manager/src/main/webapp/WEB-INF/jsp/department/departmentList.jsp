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
											<a href="#" class="easyui-linkbutton"  style="width: 120px;margin-left:30px;" onclick="openWinAdd();">
												<span style="font-weight:bold;margin-right:15px;margin-left:15px;"">添加部门</span>
												<span class="icon-add">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span>
											</a>
										</td>
										<td>	
											<a href="javascript:void(0)" class="easyui-linkbutton" onclick="clearFormFind();" style="width:120px;margin-left:30px;">
												<span style="font-weight:bold;margin-right:15px;margin-left:15px;">清&nbsp;&nbsp;
													&nbsp;&nbsp;除</span>
												<span class="icon-clear">&nbsp;&nbsp;&nbsp;&nbsp;</span>
											</a>
										</td>	
										<td>	
											<a href="#" class="easyui-linkbutton" onclick="removeDeptByID();" style="width:120px;margin-left:30px;">
												<span style="font-weight:bold;margin-right:15px;margin-left:15px;">删&nbsp;&nbsp;
													&nbsp;&nbsp;除</span>
												<span class="icon-cut">&nbsp;&nbsp;&nbsp;&nbsp;</span>
											</a>
										</td>	
										<td>	
											<a href="javascript:void(0)" class="easyui-linkbutton" onclick="openWinUpdate();" style="width:120px;margin-left:30px;">
												<span style="font-weight:bold;margin-right:15px;margin-left:15px;">修&nbsp;&nbsp;
													&nbsp;&nbsp;改</span>
												<span class="icon-edit">&nbsp;&nbsp;&nbsp;&nbsp;</span>
											</a>
										</td>
										<td>	
											<a href="javascript:void(0)" class="easyui-linkbutton" onclick="openDeptUsers();" style="width:120px;margin-left:30px;">
												<span style="font-weight:bold;margin-right:5px;margin-left:5px;">部门员工信息</span>
												<span class="icon-man">&nbsp;&nbsp;&nbsp;&nbsp;</span>
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
								<th data-options="field:'create_Time',align:'center'" style="width:18%;max-width:100%;">添加时间</th>
							</tr>
					</thead>
				</table>
			</div>
		</div>
	</div>	
	<!--修改部门窗口--> 
	<div id="upda" class="easyui-window" title="部门信息" collapsible="false" minimizable="false" maximizable="false" 
		icon="icon-save" style="width: 300px; height: 150px; background: #fafafa;">
		<div class="easyui-layout" fit="true">
			<div region="center" border="false" style="background: #fff; border: 1px solid #ccc;">
				<table cellpadding="10px" id="tb"  style="border: 0px;margin:10px 10px;font-weight: bold;"" >
					<tr>
						<td>部&nbsp;门&nbsp;名：</td>
						<td><input id="dept_name" type="text" class="txt01" name="dept_name" />
						</td>
					</tr>
					<tr>
						<td><input id="dept_name_check" type="text" class="txt01" name="dept_name_check" hidden="true" />
						</td>
					</tr>
					<tr>
						<td>注册时间：</td>
						<td><input id="create_time" type="text" class="txt01" name="create_time" readonly/>
						</td>
					</tr>
				</table>
			</div>
			<div region="south" border="false" style="text-align:center; height: 50px; line-height: 50px;">
				<a id="btnEp" class="easyui-linkbutton" icon="icon-ok" href="javascript:void(0)" onclick="updateDept()" style="margin:8px"> 确定</a>
				<a id="btnCancel" class="easyui-linkbutton" icon="icon-cancel" href="javascript:void(0)" onclick="closeWinUpdate()" style="margin:8px">取消</a>
				<a id="btnClear" class="easyui-linkbutton" icon="icon-clear" href="javascript:void(0)" onclick="clearFormUpdate()" style="margin:8px">清空</a>
			</div>
		</div>
	</div>
	<!-- 添加部门窗口 -->
	<div id="addDept" class="easyui-window" title="添加部门" collapsible="false" minimizable="false" maximizable="false" 
		icon="icon-save" style="background: #fafafa;">
		<div class="easyui-layout" fit="true">
			<div region="center" border="false" style="background: #fff; border: 1px solid #ccc;">
				<table cellpadding="10px" id="addForm"  style="border: 0px;margin:50px 20px;font-weight: bold;" data-options="novalidate:true">
					<tr>
						<td>部&nbsp;门&nbsp;名：</td>
						<td>
							<input id="dept_Name"  type="text" class="text" name="dept_Name" >
						</td>
					</tr>
				</table>	
			</div>
			<div region="south" border="false" style="text-align:center; height: 50px; line-height: 50px;">
				<a href="javascript:void(0)" class="easyui-linkbutton" icon="icon-ok" onclick="submitForm()" style="margin:8px">提交</a>
				<a href="javascript:void(0)" class="easyui-linkbutton" icon="icon-clear"  onclick="clearFormAdd()" style="margin:8px">清空</a>
				<a href="javascript:void(0)" class="easyui-linkbutton" icon="icon-cancel" onclick="closeWinAdd()" style="margin:8px">取消</a>
			</div>
		</div>
	</div>
	</body>
	<script>
	
		 //设置修改部门窗口
        function winUpdate() {
            $('#upda').window({
                title: '部门信息',
                width: 300,
                modal: true,
                shadow: true,
                closed: true,
                height: 260,
                resizable:false
            });
        }
        
         //设置添加部门窗口
        function winAdd() {
            $('#addDept').window({
                title: '添加部门',
                width: 300,
                modal: true,
                shadow: true,
                closed: true,
                height: 260,
                resizable:false
            });
        }
        
          // 清除查询表单
		function clearFormFind(){
			$('#fm').form('clear');
		}
		
		// 清空修改部门窗口
		function clearFormUpdate(){
			$('#tb').form('clear');
		};
		
		winUpdate();
		//打开修改部门窗口
		function openWinUpdate(){
			//打开修改部门窗口之前先清空
			var row = $('#dg').datagrid('getSelected');
			if (row){
				var deptName = row.deptName;
				var dept_name_check = row.deptName;
				var create_Time = row.create_Time;
				$('#dept_name').val(deptName);
				$('#dept_name_check').val(dept_name_check);
				$('#create_time').val(create_Time);
				$('#upda').window('open');
			}else{
				msgShow('系统提示', '请选择要修改的部门！', 'error');
			}
		};
		
		winAdd();
		//打开添加部门窗口
		function openWinAdd(){
			//打开添加部门窗口之前先清空
			clearFormAdd();
			$('#addDept').window('open');
		};
		
		//关闭修改部门窗口
		function closeWinUpdate(){
			$('#upda').window('close');
		};
		
		//关闭添加部门窗口
		function closeWinAdd(){
			$('#addDept').window('close');
		};
		
		//弹出信息窗口 title:标题 msgString:提示信息 msgType:信息类型 [error,info,question,warning]
		function msgShow(title, msgString, msgType) {
			$.messager.alert(title, msgString, msgType);
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
				              var url='${pageContext.request.contextPath}/department/findDepts';
				              reload(url,name);
					}
			   });
			}else{
            	msgShow('系统提示', '请选择要删除的部门！', 'error');
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
			if(row){	
				var id=row.id;
	            var dept_name = $('#dept_name').val().trim();
	            var dept_name_check = $('#dept_name_check').val().trim();
	            if (dept_name == '') {
	                msgShow('系统提示', '请输入部门名！', 'warning');
	                return false;
	            }else if(dept_name_check==dept_name){
	            	msgShow('系统提示', '修改成功！', 'info');
	            	closeWinUpdate();
		            var url='${pageContext.request.contextPath}/department/findDepts';
			        reload(url,name);
	                return;
	            }
	            dept_name = $('#dept_name').val().trim();
	            var url=encodeURI('${pageContext.request.contextPath}/department/updateDept?dept_name='+dept_name+'&id='+id);
	            $.post(url, function(data) {
	                if(data.result==true){
		                 msgShow('系统提示', '修改成功！', 'info');
		                 closeWinUpdate();
		                 var url='${pageContext.request.contextPath}/department/findDepts';
			             reload(url,name);
	                }else if(data.result==false){
	                	 msgShow('系统提示', '该部门名已被注册！', 'error');
		                 closeWinUpdate();
		                 var url='${pageContext.request.contextPath}/department/findDepts';
			             reload(url,name);
	                }else{
		                 msgShow('系统提示', '修改失败！', 'error');
		                 closeWinUpdate();
	                }
	            });
            }else{
            	msgShow('系统提示', '请选择要修改的部门！', 'error');
            }
        }
        
        // 清空添加部门表单
		function clearFormAdd(){
			$('#addForm').form('clear');
		}
		
		//前端校验
		function checked(){
			var regex_dept_Name=/^[\u4E00-\u9FA5A-Za-z0-9_]{2,20}$/;
			var dept_Name = $.trim($('#dept_Name').val()) ;
			if(dept_Name == "" || dept_Name == null || dept_Name == undefined || regex_dept_Name.test(dept_Name) != true){
					$.messager.alert("系统提示","部门名不能为空或格式不正确，请重新填写！\n用户名由2-20位汉字、字母、数字、下划线组成","error");	
					return false;
			}
			return true;
		}
		
		// 提交（部门信息）
		function submitForm(){
			var dept_Name = $.trim($('#dept_Name').val()) ;
			// 提交信息前完成前端校验
			var check_result = checked();
			if(!check_result){
				return;
			}
			$.ajax({
				type:"post",
				url:"/pay-platform-manager/department/addDept",
				data:{"deptname":dept_Name},
				dataType:"json",
				success:function (data){
					if(data.result == true){
						$.messager.alert("系统提示","恭喜，添加部门成功!","info");
						var URL = '${pageContext.request.contextPath}/department /findDepts?dept_name=';
						var name = $('#deptname').val().trim();
						closeWinAdd();
						reload(URL,name);
					}else if(data.result == false){
						$.messager.alert("系统提示","该部门名已被注册，请重新填写部门名!","error");	
						clearFormAdd();
					}else{
						$.messager.alert("系统提示","添加部门失败，请重新添加!","error");
						clearFormAdd();
					}
				},
				error:function(){
					$.messager.alert("系统提示","部门添加异常，请刷新页面!","error");
				}
			});
		}
		
		//跳转到所选部门的员工信息页面
		 function openDeptUsers(){
		 	var row = $('#dg').datagrid('getSelected');
			if(row){	
				var id=row.id;
	            var url=encodeURI('${pageContext.request.contextPath}/department/findDeptUsers?id='+id);
	            $.post(url, function(data) {
	                if(data.result==true){
		                 msgShow('系统提示', '查询成功！', 'info');
	                }else{
		                 msgShow('系统提示', '查询失败，请刷新页面后再次尝试！', 'error');
	                }
	            });
            }else{
            	msgShow('系统提示', '请选择部门！', 'error');
            }
		 }
	</script>
</html>