<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<html>
<head>
	<meta charset="UTF-8">
	<title>添加部门</title>
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/themes/default/easyui.css">
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/themes/icon.css">
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/msgList.css">
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.min.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.easyui.min.js"></script>
</head>
<body style="margin-left: 350px">
	<div style="margin:40px auto;padding:40px 0;"></div>
	<div class="easyui-panel" title="添加部门" style="width:100%;max-width:600px;padding:65px 60px;">
		<form id="ff" class="easyui-form" method="post" data-options="novalidate:true">
			<div style="margin-bottom:20px">
				<input id="deptname"  class="easyui-textbox" name="deptname" style="width:100%;height:35px;padding:5px;" data-options="required:true" 
					prompt="由2-20位汉字、字母、数字、下划线组成" label="部&nbsp;门&nbsp;名：" missingMessage="部门名不能为空！">
			</div>
		</form>
		<div style="text-align:center;padding:5px 0">
			<a href="javascript:void(0)" class="easyui-linkbutton" onclick="submitForm()" style="width:80px;margin:10px 15px">提交</a>
			<a href="javascript:void(0)" class="easyui-linkbutton" onclick="clearForm()" style="width:80px;margin:10px 15px">清空</a>
			<a href="${pageContext.request.contextPath}/department/departmentList" class="easyui-linkbutton" 
				style="width:80px;margin:10px 15px">取消</a>
		</div>
	</div>
	<script>
		// 清空添加用户表单
		function clearForm(){
			$('#ff').form('clear');
		}
		
		//前端校验
		function checked(){
			var regex_deptname=/^[\u4E00-\u9FA5A-Za-z0-9_]{2,20}$/;
			var deptname = $.trim($('#deptname').val()) ;
			if(deptname == "" || deptname == null || deptname == undefined || regex_deptname.test(deptname) != true){
					$.messager.alert("系统提示","部门名格式不正确，请重新填写！","error");	
					return false;
			}
			return true;
		}
		
		//列表重新加载
		function reload(){
			$('#dg').datagrid('reload',{
	            url: "${pageContext.request.contextPath}/department/findDepts?deptname=''",
	            method: "post"
	          }); 
		}
		
		// 提交（部门信息）
		function submitForm(){
			//非空提醒
			$('#ff').form('submit',{
				onSubmit:function(){
					return $(this).form('enableValidation').form('validate');
				}
			});
			var deptname = $.trim($('#deptname').val()) ;
			// 提交信息前完成前端校验
			var check_result = checked();
			if(!check_result){
				return;
			}
			$.ajax({
				type:"post",
				url:"/pay-platform-manager/department/addDept",
				data:{"deptname":deptname},
				dataType:"json",
				success:function (data){
					if(data.result == true){
						clearForm();
						$.messager.alert("系统提示","恭喜，添加部门成功!","info");
					}else if(data.result == false){
						clearForm();
						$.messager.alert("系统提示","该部门名已被注册，请重新填写部门名!","error");	
					}else{
						clearForm();
						$.messager.alert("系统提示","添加部门失败，请重新添加!","error");
					}
				},
				error:function(){
					$.messager.alert("系统提示","部门添加异常，请刷新页面!","error");
				}
			});
		}
	</script>
</body>
</html>