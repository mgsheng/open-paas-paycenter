<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
	<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta http-equiv="X-UA-Compatible" content="IE=9; IE=8; IE=7; IE=EDGE">
		<meta charset="utf-8" />
		<title>找回密码</title>
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/style3.css" />
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
					<div class="main">
						<table cellpadding="0" cellspacing="0" class="tbl-findpwd">
							<tr>
								<td colspan="2"><span class="fts-1">请使用您已绑定的手机号</span></td>
							</tr>
							<tr>
								<td colspan="2">
									<input type="text" id="phone" class="input-len" value="${showMobileNo}" placeholder="输入手机号" onchange="changeValue()"/>
									<input type="hidden" id="mobileNo" class="input-len" value="${mobileNo}" />
									<div class="div_error" id="phone_error"></div>
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
								<td>
									<input type="text" id="phonecode" class="input-short" placeholder="输手机收到的验证码" />
									<div class="div_error" id="phonecode_error"></div>
								</td>
								<td>
									<input type="button" id="btnCode" class="btn-short" onclick="btnGetCode()" value="获取验证码"/>
									<div class="" id="phonecode_msg"></div>
								</td>
							</tr>
							<%-- <tr>
								<td colspan="2"><span class="fts-2">如果没有绑定手机，请选择 <a href="${pageContext.request.contextPath}/findpwdemail">邮箱验证</a> 修改密码</span></td>
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
			function changeValue(){
			 $('#mobileNo').val('');
			}
			function btnGetCode(){
				jQuery('.input-len,.input-short').removeClass('frm_error');
				jQuery('.div_error').html('');
				if(!Checking.checkbool('#idfCode_error')){
					return;
				}
				var phoneReg = /^1\d{10}$/;
				var mobileNo =$('#mobileNo').val();
				var phone=$('#phone').val();
				if(mobileNo!=""){
				 phone=mobileNo;
				}
				
				if(!phoneReg.test(phone)){
					jQuery('#phone').addClass('frm_error');
					jQuery('#phone_error').html('手机号格式不正确');
					Checking.init();
					return;
				}
				$.post("${pageContext.request.contextPath}/dev/user/send_reset_password_phone",
					{phone:phone},
	   				function(data){
	   					if(data.flag){
	   						alert('验证码已发送到您的手机');
	   						codetime = 30;
	   						//屏蔽发送按钮
	   						jQuery('#btnCode').attr('disabled','disabled');
	   						unlockBtnCode();
	   					}
	   					else{
	   						jQuery('#phone').addClass('frm_error');
	   						if(data.errorCode=='invalid_phone'){
	   							jQuery('#phone_error').html('无效手机号');
	   						}
	   						else if(data.errorCode=='no_phone'){
	   							jQuery('#phone_error').html('该手机号不存在');
	   						}
	   						else if(data.errorCode=='send_error'){
	   							jQuery('#phone_error').html('操作失败，请重新获取');
	   						}
	   						else if(data.errorCode=='system_error'){
	   							jQuery('#phone_error').html('系统异常，请重新获取');
	   						}
	   						Checking.init();
	   					}
	   				}
   				);
			}
			
			var codetime;
			function unlockBtnCode(){
				if(codetime==0){
					jQuery('#phonecode_msg').html('');
					jQuery('#btnCode').removeAttr('disabled');
				}
				else{
					jQuery('#phonecode_msg').html(codetime+'秒后才可以再次获取');
					codetime--;
					setTimeout("unlockBtnCode()",1000);
				}
			}
			
			function btnSubmit(){
				jQuery('.input-len,.input-short').removeClass('frm_error');
				jQuery('.div_error').html('');
				var phoneReg = /^1\d{10}$/;
				var phone=$('#phone').val();
				var mobileNo =$('#mobileNo').val();
				var phone=$('#phone').val();
				if(mobileNo!=""){
				 phone=mobileNo;
				}
				if(!phoneReg.test(phone)){
					jQuery('#phone').addClass('frm_error');
					jQuery('#phone_error').html('手机号格式不正确');
					Checking.init();
					return;
				}
				var phonecode=$('#phonecode').val();
				if(jQuery.trim(phonecode)==''){
					jQuery('#phonecode').addClass('frm_error');
					jQuery('#phonecode_error').html('请输入验证码');
					return;
				}
				$.post("${pageContext.request.contextPath}/dev/user/activated_reset_password_phone",
					{phone:phone,code:$('#phonecode').val()},
	   				function(data){
	   					if(data.flag){
	   						window.location.href="${pageContext.request.contextPath}/dev/user/activated_reset_password_phone.html?code="+$('#phonecode').val()+"&phone="+phone;
	   					}
	   					else{
	   						if(data.errorCode=='invalid_phone'){
	   							jQuery('#phone').addClass('frm_error');
	   							jQuery('#phone_error').html('无效手机号');
	   						}
	   						else if(data.errorCode=='invalid_data'){
	   							jQuery('#phonecode').addClass('frm_error');
	   							jQuery('#phonecode_error').html('验证码无效');
	   						}
	   						else if(data.errorCode=='send_error'){
	   							jQuery('#phonecode').addClass('frm_error');
	   							jQuery('#phonecode_error').html('操作失败，请重新获取');
	   						}
	   						else if(data.errorCode=='time_out'){
	   							jQuery('#phonecode').addClass('frm_error');
	   							jQuery('#phonecode_error').html('验证码超时，请重新获取');
	   						}
	   					}
	   				}
   				);
			}
			
		</script>
	</body>
</html>