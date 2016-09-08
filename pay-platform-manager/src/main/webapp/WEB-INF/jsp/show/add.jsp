<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<html>
<head>
	<meta charset="UTF-8">
	<title>添加用户</title>
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/themes/default/easyui.css">
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/themes/icon.css">
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/msgList.css">
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.min.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.easyui.min.js"></script>
</head>
<body style="margin-left: 350px">
	<div style="margin:40px auto;"></div>
	<div class="easyui-panel" title="添加用户" style="width:60%;max-width:600px;padding:60px 60px;">
		<form id="ff" method="post" style="margin:15px 30px;width:90%">
			<div style="margin-bottom:20px">
				<input id="un" class="easyui-textbox" missingMessage="由2-20位字母、数字、下划线组成" name="username" 
					prompt="用户名" type="text" style="width:100%;height:35px;padding:5px;" required=true>
			</div>
			<div style="margin-bottom:20px">
				<input id="rn" class="easyui-textbox" missingMessage="由2-20位汉字、字母、数字、下划线组成" name="realname" 
					prompt="真实姓名" type="text"  style="width:100%;height:35px;padding:5px;" required=true>
			</div>
			<div style="margin-bottom:20px">
				<input id="nn" class="easyui-textbox" missingMessage="由2-20位字母、数字、下划线、汉字组成" name="nickname" 
					prompt="昵称" type="text"  style="width:100%;height:35px;padding:5px;" required=true>
			</div>
			<div style="margin-bottom:20px">
				<input id="pass" class="easyui-passwordbox" missingMessage="由6-20位字母、数字、下划线组成"
					prompt="密码" type="text"  name="password" style="width:100%;height:35px;padding:5px;" required=true>
			</div>
			<div style="margin-bottom:20px">
				<input id="confirm_pass" class="easyui-passwordbox" missingMessage="由6-20位字母、数字、下划线组成"
					prompt="确认密码" name="confirmPass" type="text"  style="width:100%;height:35px;padding:5px;" required=true>
			</div>
		</form>
		<div style="text-align:center;padding:5px 0">
			<a href="javascript:void(0)" class="easyui-linkbutton" onclick="submitForm()" style="width:80px;margin:10px 15px">提交</a>
			<a href="javascript:void(0)" class="easyui-linkbutton" onclick="clearForm()" style="width:80px;margin:10px 15px">清空</a>
			<a href="${pageContext.request.contextPath}/managerUser/userList" class="easyui-linkbutton" style="width:80px;margin:10px 15px">取消</a>
		</div>
	</div>
	<script type="text/javascript">
		// 清空添加用户表单
		function clearForm(){
			$('#ff').form('clear');
		}
		
		//前端校验
		function check(){
			var regex_username = /^[a-zA-Z0-9_]{2,20}$/;
			var regex_realname=/^[\u4E00-\u9FA5A-Za-z0-9_]{2,20}$/;
			var regex_nickname= /^[\u4E00-\u9FA5A-Za-z0-9_]{2,20}$/;
			var regex_password= /^(\w){6,20}$/;
			var username = $.trim($('#un').val()) ;
			var realname = $.trim($('#rn').val());
			var nickname = $.trim($('#nn').val()) ;
			var password = $.trim($('#pass').val()) ;
			var confirm_pass = $.trim($('#confirm_pass').val()) ;
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
		
		//列表重新加载
		function reload(){
			$('#dg').datagrid('reload',{
	            url: "${pageContext.request.contextPath}/managerUser/findUsers?username=''&realname=''&nickname=''",
	            method: "post"
	          }); 
		}
		
		// 提交（用户信息）
		function submitForm(){
			var username = $.trim($('#un').val()) ;
			var realname = $.trim($('#rn').val());
			var nickname = $.trim($('#nn').val()) ;
			var password = $.trim($('#pass').val()) ;
			// 提交信息前完成前端校验
			var check_result = check();
			if(!check_result){
				return;
			}
			$.ajax({
				type:"post",
				url:"/pay-platform-manager/managerUser/addUser",
				data:{"user_name":username,"real_name":realname,"nickname":nickname,"sha_password":password},
				dataType:"json",
				success:function (data){
					if(data.result == true){
						clearForm();
						//reload();
						$.messager.alert("系统提示","恭喜，添加用户成功!","info");
					}else if(data.result == false){
						clearForm();
						$.messager.alert("系统提示","该用户名已被注册，请重新填写用户名!","error");	
					}else{
						clearForm();
						$.messager.alert("系统提示","添加用户失败，请重新添加!","error");
					}
				},
				error:function(){
					$.messager.alert("系统提示","用户添加异常，请刷新页面!","error");
				}
			});
		}
	</script>
</body>
</html>