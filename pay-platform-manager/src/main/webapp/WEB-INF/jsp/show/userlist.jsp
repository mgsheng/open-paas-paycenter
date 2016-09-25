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
	</head>
	<body >
			<div id="tt" class="easyui-tabs" data-options="tools:'AuthorizeRole'," fit="true" >
				<div title="用户信息">
					<div style="border:0px solid;border-radius:8px;margin-bottom:0px;width: 100%;max-width:100%;" fit="true" >
						<div class="top" style="width: 100%">
							<div class="easyui-panel" title="查询条件" style="width:100%;max-width:100%;padding:20px 25px;">
								<form id="fm" method="post" action="/managerUser/findUsers">
									<table cellpadding="5px">
										<tr>
											<td>用&nbsp;户&nbsp;名:</td>
											<td>
													<input class="easyui-textbox" name="username" id="un" prompt="选填" style="width:90%"></input> 
											</td>
											<td>真实姓名：</td>
											<td>	
													<input class="easyui-textbox" name="realname" id="rn" prompt="选填"  style="width:90%"></input> 
											</td>
											<td>昵&nbsp;&nbsp;&nbsp;&nbsp;称:</td>
											<td> 
													<input class="easyui-textbox" name="nickname" id="nn" prompt="选填" style="width:90%"></input> 
											</td>
											<td>部&nbsp;&nbsp;&nbsp;&nbsp;门:</td>
											<td> 
													<input class="easyui-textbox" name="deptname" id="dn" prompt="选填" style="width:90%"></input> 
											</td>
											<td>	
												<a href="javascript:void(0)" class="easyui-linkbutton" onclick="findUsers();" style="width: 100px;">
													<span style="font-weight:bold;margin-right:5px;margin-left:5px;">查&nbsp;&nbsp;&nbsp;&nbsp;询</span>
													<span class="icon-search">&nbsp;&nbsp;&nbsp;&nbsp;</span>
												</a>
											</td>
											<td>	
												<a href="javascript:void(0)" class="easyui-linkbutton" onclick="clearForm();" style="width:100px;">
													<span style="font-weight:bold;margin-right:5px;margin-left:5px;">清&nbsp;&nbsp;
														&nbsp;&nbsp;除</span>
													<span class="icon-clear">&nbsp;&nbsp;&nbsp;&nbsp;</span>
												</a>
											</td>	
											<td>	
												<a href="javascript:void(0)" class="easyui-linkbutton" onclick="openAddWin();" style="width: 100px;">
													<span style="font-weight:bold;margin-right:5px;margin-left:5px;">添加用户</span>
													<span class="icon-add">&nbsp;&nbsp;&nbsp;&nbsp;</span>
												</a>
											</td>
											<td>	
												<a href="javascript:void(0)" class="easyui-linkbutton" onclick="removeUserByID();" style="width: 100px;">
													<span style="font-weight:bold;margin-right:5px;margin-left:5px;">删除用户</span>
													<span class="icon-cut">&nbsp;&nbsp;&nbsp;&nbsp;</span>
												</a>
											</td>
											<td>	
												<a href="javascript:void(0)" class="easyui-linkbutton" onclick="updateWin();" style="width: 100px;">
													<span style="font-weight:bold;margin-right:5px;margin-left:5px;">修改用户</span>
													<span class="icon-edit">&nbsp;&nbsp;&nbsp;&nbsp;</span>
												</a>
											</td>
											<td>	
												<a href="javascript:void(0)" class="easyui-linkbutton" onclick="openWinRole();" style="width:100px;">
													<span style="font-weight:bold;margin-right:5px;margin-left:5px;">授权角色</span>
													<span class="icon-edit">&nbsp;&nbsp;&nbsp;&nbsp;</span>
												</a>
											</td>	
										</tr>
									</table>
								</form>
							</div>
						</div>
					</div>
					<div class="botton" style="margin-top:0px;width:100%;height:400px" fit="true">
						<table  id="dg"  class="easyui-datagrid" title="查询结果"  style="width:100%;max-width:100%;padding:20px 30px;"
							data-options="singleSelect:true,method:'get'" fit="true">
							<thead>
								<tr>
									<th data-options="field:'id',align:'center'" hidden="true" style="width:15%;max-width:100%;">ID</th>
									<th data-options="field:'password',align:'center'" hidden="true" style="width:15%;max-width:100%;">密码</th>
									<th data-options="field:'username',align:'center'" style="width:15%;max-width:100%;">用&nbsp;&nbsp;户&nbsp;&nbsp;名</th>
									<th data-options="field:'realName',align:'center'" style="width:15%;max-width:100%;">真实姓名</th>
									<th data-options="field:'nickName',align:'center'" style="width:15%;max-width:100%;">昵&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;称</th>
									<th data-options="field:'deptName',align:'center'" style="width:15%;max-width:100%;">所属部门</th>
									<th data-options="field:'deptID',align:'center'" hidden="true" style="width:15%;max-width:100%;">所属部门ID</th>
									<th data-options="field:'create_Time',align:'center'" style="width:18%;max-width:100%;">注册时间</th>
									<th data-options="field:'lastLoginTime',align:'center'" style="width:18%;max-width:100%;">上次登陆时间</th>
							 </tr>
						</thead>
					</table>
				</div>
			</div>
		</div>	
	<!--修改用户窗口--> 
	<div id="upda" class="easyui-window" title="用户信息" collapsible="false" minimizable="false" maximizable="false" 
		icon="icon-save" style="background: #fafafa;">
		<div region="center" border="false" style="background: #fff; border: 1px solid #ccc;">
			<table cellpadding="10px" id="tb"  style="border: 0px;margin:10px 10px" >
				<tr>
					<td>用&nbsp;户&nbsp;名：</td>
					<td><input id="userName" type="text" class="txt01" name="username" class="easyui-textbox" readonly/>
					</td>
				</tr>
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
				<tr>
					<td>部门名称：</td>
					<td>
						<select class="easyui-combobox" data-options="editable:false"  id="deptName" name="deptName" 
							style="width:100%;height:35px;padding:5px;">
						</select>
					</td>
				</tr>
			</table>
		</div>
		<div region="south" border="false" style="text-align:center; height: 50px; line-height: 50px;">
			<a id="btnEp" class="easyui-linkbutton" icon="icon-ok" href="javascript:void(0)" onclick="updateUser()" style="margin:8px"> 确定</a>
			<a id="btnCancel" class="easyui-linkbutton" onclick="closeWin()" icon="icon-cancel" href="javascript:void(0)" style="margin:8px">取消</a>
			<a id="btnClear" href="javascript:void(0)" onclick="clearTable()" class="easyui-linkbutton" icon="icon-clear" style="margin:8px">清空</a>
		</div>
	</div>
	<!-- 添加用户窗口 -->
	<div id="addWin" class="easyui-window" title="添加用户" style="width:37%;padding:30px 45px;background: #fafafa;height:450px;"
		minimizable="false" maximizable="false" icon="icon-add">
		<form id="ff" class="easyui-form" method="post" style="margin:15px 30px;width:90%" data-options="novalidate:true">
			<table>
				<tr>
					<td > 
						用&nbsp;&nbsp;户&nbsp;&nbsp;名:
					</td>
					<td style="width:80%;">	
						<input id="uname" class="easyui-textbox" missingMessage="由2-20位字母、数字、下划线组成" name="username" 
							type="text" style="width:100%;height:35px;padding:5px;" required=true>
					</td>
				</tr>
				<tr>
					<td>
						真实姓名：
					</td>
					<td style="width:80%;">	
						<input id="rname" class="easyui-textbox" missingMessage="由2-20位汉字、字母、数字、下划线组成" name="realname" 
							type="text"  style="width:100%;height:35px;padding:5px;" required=true>
					</td>
				</tr>
				<tr>
					<td>
						昵&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;称:
					</td>
					<td style="width:80%;">	
						<input id="nname" class="easyui-textbox" missingMessage="由2-20位字母、数字、下划线、汉字组成" name="nickname" 
							type="text"  style="width:100%;height:35px;padding:5px;" required=true>
					</td>
				</tr>
				<tr>
					<td>
						密&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;码:
					</td>
					<td style="width:80%;">	
						<input id="pwd" class="easyui-passwordbox" missingMessage="由6-20位字母、数字、下划线组成"
							 type="text"  name="password" style="width:100%;height:35px;padding:5px;" required=true>
					</td>
				</tr>
				<tr>
					<td>
						确认密码：
					</td>
					<td style="width:80%;">	
						<input id="confirm_pwd" class="easyui-passwordbox" missingMessage="由6-20位字母、数字、下划线组成"
							name="confirmPwd" type="text"  style="width:100%;height:35px;padding:5px;" required=true>
					</td>
				</tr>
				<tr>
					<td style="margin-bottom:20px">
						部门名称：
					</td>
					<td style="width:80%;">	
						<select class="easyui-combobox" data-options="editable:false,prompt:'请选择部门'" id="addDeptName" name="addDeptName" 
							style="width:100%;height:35px;padding:5px;">
						</select>
					</td>
				</tr>
			</table>
		</form>
		<div style="text-align:center;padding:5px 0">
			<a href="javascript:void(0)" class="easyui-linkbutton" onclick="submitAddForm()" style="width:80px;margin:10px 15px">提交</a>
			<a href="javascript:void(0)" class="easyui-linkbutton" onclick="clearAddForm()" style="width:80px;margin:10px 15px">清空</a>
			<a href="${pageContext.request.contextPath}/managerUser/userList" class="easyui-linkbutton" style="width:80px;margin:10px 15px">取消</a>
		</div>
	</div>
	</body>
	<script>
		
		//添加tab页面
		function addPanel(userName,userId){
			if ($('#tt').tabs('exists', userName)){
			 	$('#tt').tabs('select', userName);
			} else {
				 var url = '${pageContext.request.contextPath}/managerUser/toRole?id='+userId+'&userName='+userName;
			 	 var content = '<iframe scrolling="auto" frameborder="0" src="'+url+'" style="width:100%;height:100%;"></iframe>';
				 $('#tt').tabs('add',{
					 title:userName,
					 content:content,
					 closable:true,
					 cache:true
				 });
			}
		}	
		//移除tab页面
		function removePanel(){
			var tab = $('#tt').tabs('getSelected');
			if (tab){
				var index = $('#tt').tabs('getTabIndex', tab);
				$('#tt').tabs('close', index);
			}
		}
		
		//打开授权角色窗口
		function openWinRole(){
			var row = $('#dg').datagrid('getSelected');
			if (row){
				var userId = row.id;
				var userName=row.username;
				addPanel(userName,userId);
			}else{
            	msgShow('系统提示', '请选择用户！', 'warning');
            }
		};
		
		//添加用户窗口
		function addWin(){
			 $('#addWin').window({
                title: '添加用户',
                modal: true,
                shadow: true,
                closed: true,
                resizable:false
            });
		}
		
		//打开添加用户窗口
		addWin();
		function openAddWin(){
			clearAddForm();
			$('#addWin').window('open');
			$('#addDeptName').combobox({
				url:'${pageContext.request.contextPath}/managerUser/findAllDepts',
				valueField:'id',
				textField:'text'
			});
		}
		
		//关闭添加用户窗口
		function closeAddWin(){
			$('#addWin').window('close');
		}
		
		// 清除添加用户表单
		function clearAddForm(){
			$('#ff').form('clear');
		}
		
		//修改用户窗口
        function win() {
            $('#upda').window({
                title: '用户信息',
                width: 350,
                modal: true,
                shadow: true,
                closed: true,
                height: 300,
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
		
		//打开修改用户窗口
		win();
		function openWin(){
			$('#upda').window('open');
		}
		
		//修改用户
		function updateWin(){
			//打开修改用户窗口之前先清空
			clearTable();
			var row = $('#dg').datagrid('getSelected');
			if (row){
				var user_name = row.username;
				var real_name = row.realName;
				var nick_name = row.nickName;
				var dept_Name = row.deptName;
				var dept_ID = row.deptID;
				$('#userName').val(user_name);
				$('#realname').val(real_name);
				$('#nickname').val(nick_name);
				
				openWin();
				$('#deptName').combobox({
					url:'${pageContext.request.contextPath}/managerUser/findAllDepts',
					valueField:'id',
					textField:'text',
					onLoadSuccess:function(){
						$('#deptName').combobox('setValue',dept_ID);
						$('#deptName').combobox('setText',dept_Name);
				}
			});
			}else{
            	msgShow('系统提示', '请选择修改用户！', 'info');
            }
		};
		//关闭修改用户窗口
		function closeWin(){
			$('#upda').window('close');
		};
		
		//弹出信息窗口 title:标题 msgString:提示信息 msgType:信息类型 [error,info,question,warning]
		function msgShow(title, msgString, msgType) {
			$.messager.alert(title, msgString, msgType);
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
				            findUsers();
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
		
		// 查询用户方法
		function findUsers(){
			//用户名
			var username = $('#un').val().trim();
			//真实姓名
			var realname = $('#rn').val().trim();
			//昵称
			var nickname = $('#nn').val().trim();
			//部门
			var deptName = $('#dn').val().trim();
			$('#dg').datagrid({
				collapsible:true,
				rownumbers:true,
				pagination:true,
		        url: "${pageContext.request.contextPath}/managerUser/findUsers?username="+username+"&realname="
		        	+realname+"&nickname="+nickname+"&deptName="+deptName,  
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
        
        // 提交修改后的用户信息
         function updateUser() {
		 	var row = $('#dg').datagrid('getSelected');
			var id=row.id;
            var $realname = $('#realname').val().trim();
            var $nickname = $('#nickname').val().trim();
            var $deptID = $('#deptName').combobox('getValue');
            var $deptName = $('#deptName').combobox('getText');
           	// alert($deptName);
            if ($realname == '') {
                msgShow('系统提示', '请输入真实姓名！', 'warning');
                return false;
            }
            if ($nickname == '') {
                msgShow('系统提示', '请输入昵称！', 'warning');
                return false;
            }
             if ($deptName == '') {
                msgShow('系统提示', '请选择部门！', 'warning');
                return false;
            }
            var url=encodeURI('${pageContext.request.contextPath}/managerUser/updateUser?realname='+$realname+
            		'&nickname='+$nickname+'&updateDeptName='+$deptName+'&updateDeptID='+$deptID+'&id='+id);
            $.post(url, function(data) {
                if(data.result==true){
	                 msgShow('系统提示', '修改成功！', 'info');
	                 closeWin();
	                 findUsers();
                }else{
	                 msgShow('系统提示', '修改失败！', 'info');
	                 closeWin();
                }
            });
        }
        
        //前端校验
		function check(){
			var regex_username = /^[a-zA-Z0-9_]{2,20}$/;
			var regex_realname=/^[\u4E00-\u9FA5A-Za-z0-9_]{2,20}$/;
			var regex_nickname= /^[\u4E00-\u9FA5A-Za-z0-9_]{2,20}$/;
			var regex_password= /^(\w){6,20}$/;
			var username = $.trim($('#uname').val()) ;
			var realname = $.trim($('#rname').val());
			var nickname = $.trim($('#nname').val()) ;
			var password = $.trim($('#pwd').val()) ;
			var confirm_pass = $.trim($('#confirm_pwd').val()) ;
			var addDeptName = $('#addDeptName').combobox('getText');
			if(username == "" || username == null || username == undefined || regex_username.test(username) != true){
					$.messager.alert("系统提示","用户名不能为空或格式不正确，请重新填写！","error");	
					return false;
			}
			if(realname == "" || realname == null || realname == undefined || regex_realname.test(realname) != true){
					$.messager.alert("系统提示","真实姓名不能为空或格式不正确，请重新填写！","error");		
					return false;
			}
			if(nickname == "" || nickname == null || nickname == undefined || regex_nickname.test(nickname) != true){
					$.messager.alert("系统提示","昵称不能为空或格式不正确，请重新填写！","error");		
					return false;
			}
			if(password == "" || password == null || password == undefined || regex_password.test(password) != true){
					$.messager.alert("系统提示","密码不能为空或格式不正确，请重新填写！","error");			
					return false;
			}
			if(addDeptName == "" || addDeptName == null || addDeptName == undefined){
					$.messager.alert("系统提示","请选择部门！","error");			
					return false;
			}
			if(confirm_pass =="" || confirm_pass == null || confirm_pass == undefined || regex_password.test(confirm_pass) != true){
					$.messager.alert("系统提示","确认密码不能为空或格式不正确，请重新填写！","error");		
					return false;
			}
			if(password != confirm_pass){
				$.messager.alert("系统提示","密码输入不一致，请重新输入！","error");
				return false;
			}
			return true;
		}
		
		// 提交（用户信息）
		function submitAddForm(){
			var username = $.trim($('#uname').val()) ;
			var realname = $.trim($('#rname').val());
			var nickname = $.trim($('#nname').val()) ;
			var password = $.trim($('#pwd').val()) ;
			var addDeptName = $('#addDeptName').combobox('getText');
			var deptID = $('#addDeptName').combobox('getValue');
			$('#ff').form('submit',{
				onSubmit:function(){
					return $(this).form('enableValidation').form('validate');
				}
			});
			
			// 提交信息前完成前端校验
			var check_result = check();
			if(!check_result){
				return;
			}
			$.ajax({
				type:"post",
				url:"/pay-platform-manager/managerUser/addUser",
				data:{"user_name":username,"real_name":realname,"nickname":nickname,"sha_password":password,"addDeptName":addDeptName,"deptID":deptID},
				dataType:"json",
				success:function (data){
					if(data.result == true){
						$.messager.alert("系统提示","恭喜，添加用户成功!","info");
						closeAddWin();
						var url = "${pageContext.request.contextPath}/managerUser/userList";
						window.location.reload();
					}else if(data.result == false){
						clearAddForm();
						$.messager.alert("系统提示","该用户名已被注册，请重新填写用户名!","error");	
					}else{
						clearAddForm();
						$.messager.alert("系统提示","添加用户失败，请重新添加!","error");
					}
				},
				error:function(){
					$.messager.alert("系统提示","用户添加异常，请刷新页面!","error");
				}
			});
		}
		
		//页面预加载
		$(function(){
			findUsers();
		});
		
		//
		
		
	</script>
</html>