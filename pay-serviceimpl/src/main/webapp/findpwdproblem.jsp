<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="utf-8" />
		<title>找回密码</title>
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/style2.css" />
		<link href="${pageContext.request.contextPath}/images/open_logo.ico" rel="Shortcut Icon" />
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
					<div id="div_username" class="main">
						<table cellpadding="0" cellspacing="0" class="tbl-findpwd">
							<tr>
								<td colspan="2"><span class="fts-1">请输入您的用户名</span></td>
							</tr>
							<tr>
								<td colspan="2">
									<input type="text" id="userName" class="input-len" placeholder="输入用户名" />
									<div class="div_error" id="userName_error"></div>
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
							<tr>
								<td colspan="2">
									<input type="button" class="btn-len" style="width:120px;" value="返回" onclick="window.location.href='${pageContext.request.contextPath}/findpwd'">
									<input type="button" class="btn-len" style="width:200px;" value="下一步" onclick="btnGetProblem()" />
								</td>
							</tr>
						</table>
					</div>
					<div id="div_problem" class="main" style="display: none;">
						<form id="form_problem" action="${pageContext.request.contextPath}/dev/user/activated_reset_password_problem.html" method="post">
						<table cellpadding="0" cellspacing="0" class="tbl-findpwd">
							<tr>
								<td colspan="2"><span class="fts-1">请输入您的密保问题答案</span></td>
							</tr>
							<tr>
								<td colspan="2"><span class="fts-2" id="span_problem"></span></td>
							</tr>
							<tr>
								<td colspan="2">
									<input type="hidden" id="problemId" name="problemId"/>
									<input type="hidden" id="hide_userName" name="userName"/>
									<input type="text" id="answer" name="answer" class="input-len" placeholder="输入答案" />
									<div class="div_error" id="answer_error"></div>
								</td>
							</tr>
							<tr>
								<td colspan="2">
									<input type="button" class="btn-len" style="width:120px;" value="返回" onclick="btnBack();">
									<input type="button" class="btn-len" style="width:200px;" value="下一步" onclick="btnSubmit()" />
								</td>
							</tr>
						</table>
						</form>
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
			
			function btnGetProblem(){
				jQuery('.input-len,.input-short').removeClass('frm_error');
				jQuery('.div_error').html('');
				if(!Checking.checkbool('#idfCode_error')){
					return;
				}
				var userName=$('#userName').val();
				if(jQuery.trim(userName)==''){
					jQuery('#userName').addClass('frm_error');
					jQuery('#userName_error').html('请输入用户名');
					Checking.init();
					return;
				}
				$.post("${pageContext.request.contextPath}/dev/user/user_problem_by_username",
					{userName:$('#userName').val()},
	   				function(data){
	   					if(data.flag){
	   						jQuery('#problemId').val(data.problemData[0].id);
	   						jQuery('#span_problem').html('问题：'+data.problemData[0].name);
	   						jQuery('#div_username').hide();
							jQuery('#div_problem').show();
	   					}
	   					else{
	   						jQuery('#userName').addClass('frm_error');
	   						if(data.errorCode=='invalid_username'){
	   							jQuery('#userName_error').html('用户名不存在');
	   						}
	   						else if(data.errorCode=='no_problem'){
	   							jQuery('#userName_error').html('该用户没有设置密保问题');
	   						}
	   						Checking.init();
	   					}
	   				}
   				);
			}
			
			function btnBack(){
				jQuery('#div_username').show();
				jQuery('#div_problem').hide();
			}
			
			function btnSubmit(){
				jQuery('.input-len,.input-short').removeClass('frm_error');
				jQuery('.div_error').html('');
				var answer=$('#answer').val();
				if(jQuery.trim(answer)==''){
					jQuery('#answer').addClass('frm_error');
					jQuery('#answer_error').html('请输入答案');
					return;
				}
				$.post("${pageContext.request.contextPath}/dev/user/activated_reset_password_problem",
					{
					userName:jQuery('#userName').val(),
					problemId:$('#problemId').val(),
					answer:$('#answer').val()
					},
	   				function(data){
	   					if(data.flag){
	   						jQuery('#hide_userName').val(jQuery('#userName').val());
	   						$('#form_problem').submit();
	   					}
	   					else{
	   						if(data.errorCode=='answer_error'){
	   							jQuery('#answer').addClass('frm_error');
	   							jQuery('#answer_error').html('问题答案不正确');
	   						}
	   						else if(data.errorCode=='invalid_problem' || data.errorCode=='invalid_username'){
	   							jQuery('#answer').addClass('frm_error');
	   							jQuery('#answer_error').html('无效数据，请重新申请');
	   						}
	   					}
	   				}
   				);
			}
			
		</script>
	</body>
</html>