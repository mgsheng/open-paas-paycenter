<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="utf-8" />
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<meta name="viewport" content="width=device-width, initial-scale=1">
		<title>找回密码</title>
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/style3.css" />
		<link href="${pageContext.request.contextPath}/images/open_logo.ico" rel="Shortcut Icon" />
		<style type="text/css">
			.div_error{
				color: red;
			}
		</style>
	</head>
	<body class="page_setting pwd page_introduction">
		<%@ include file="/dev_head.jsp"%>
		<!--找回密码 star-->
		<div class="layout">
			<div class="w_1200">
				<div class="getbackpwd">
					<div class="stepbar">
						<div class="first"><span class="num">1</span>选择密码找回方式</div>
						<div class="second curr"><span class="num">2</span>验证身份</div>
						<div class="third"><span class="num">3</span>修改密码</div>
					</div>
					<div class="main">
						<table id="main_table"  cellpadding="0" cellspacing="0" class="tbl-findpwd">
							<tr>
								<td colspan="2"><span class="fts-1">请使用您已绑定的邮箱</span></td>
							</tr>
							<tr>
								<td colspan="2">
									<input type="text" class="input-len" id="email" placeholder="输入邮箱" />
									<div class="div_error" id="email_error"></div>
								</td>
							</tr>
							<tr>
								<td>
									<input type="text" id="chk" class="input-short" placeholder="输入验证码" />
									<div class="div_error" id="idfCode_error"></div>
								</td>
								<td>
									<div class="frm_controls" align="left"  >
										<div id="ieContainer" onclick="Checking.init()" style="display: inline-block;">	
											<canvas id="myCanvas" width="130" height="35" ></canvas>
										</div>
									</div>
								</td>
							</tr>
							<%-- <tr>
								<td colspan="2"><span class="fts-2">如果没有绑定邮箱，请选择 <a href="${pageContext.request.contextPath}/findpwdphone">手机验证</a> 修改密码</span></td>
							</tr> --%>
							<tr>
								<td colspan="2">
									<input type="button" class="btn-len" style="width:120px;" value="返回" onclick="window.location.href='${pageContext.request.contextPath}/findpwd'">
									<input type="button" class="btn-len" style="width:200px;" value="下一步" onclick="btnSubmit()" />
								</td>
							</tr>
						</table>
					</div>
				</div>
			</div>
		</div>
		<!--找回密码 end-->
		<%@ include file="/dev_foot.jsp"%>
		<script src="${pageContext.request.contextPath}/js/checking.js" type="text/javascript"></script>
		<script type="text/javascript">
			jQuery(document).ready(function() {
				Checking.init();
			});
			
			function btnSubmit(){
				jQuery('.input-len').removeClass('frm_error');
				jQuery('.div_error').html('');
				if(!Checking.checkbool('#idfCode_error')){
					return;
				}
				var emailReg = /^[\w-]+(\.[\w-]+)*@[\w-]+(\.[\w-]+)+$/;
				var email=$('#email').val();
				if(!emailReg.test(email)){
					jQuery('#email').addClass('frm_error');
					jQuery('#email_error').html('邮箱格式不正确');
					Checking.init();
					return;
				}
				$.post("${pageContext.request.contextPath}/dev/user/send_reset_password_email",
					{email:$('#email').val()},
	   				function(data){
	   					if(data.flag){
	   						var html = '';
	   						html += '<tr>';
							html += '<td colspan="2"><span class="fts-1">邮件已发送<br/>请登录邮箱进行后续操作</span></td>';
							html += '</tr>';
	   						jQuery('#main_table').html(html);
	   					}
	   					else{
	   						jQuery('#email').addClass('frm_error');
	   						if(data.errorCode=='email_activation_no'){
	   							jQuery('#email_error').html('该邮箱没有通过验证');
	   						}
	   						else if(data.errorCode=='no_email'){
	   							jQuery('#email_error').html('不存在该邮箱');
	   						}
	   						else if(data.errorCode=='email_activation_no'){
	   							jQuery('#email_error').html('该邮箱没有通过验证');
	   						}
	   						else if(data.errorCode=='invalid_email'){
	   							jQuery('#email_error').html('该邮箱无效');
	   						}
	   						else if(data.errorCode=='system_error'){
	   							jQuery('#email_error').html('系统异常');
	   						}
	   						Checking.init();
	   					}
	   				}
   				);
			}
		</script>
	</body>
</html>