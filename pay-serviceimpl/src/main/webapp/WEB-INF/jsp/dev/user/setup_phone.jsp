<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="utf-8" />
		<title>用户中心</title>
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/style2.css" />
		<link href="${pageContext.request.contextPath}/images/open_logo.ico" rel="Shortcut Icon" />
		<style type="text/css">
			.basic_info_text li {
			    width: 100%;
			}
			.revise_info {
				float: left;
			}
			input,select {
				width: 322px;
			}
		</style>
	</head>
	<body class="page_setting pwd page_introduction">
		<%@ include file="/dev_head.jsp"%>
		<input type="hidden" id='pageCode' value="problem">
		<div class="layout mt35 clearfix">
			<div class="w_1200">
		    	<div class="basic_info clearfix">
		        	<div class="basic_info_tab">
		                <ul>
		                    <li><a href="${pageContext.request.contextPath}/dev/user/user_info">基本资料</a></li>
		                    <li><a href="${pageContext.request.contextPath}/dev/user/updatepwd">修改密码</a></li>
		                    <li><a href="${pageContext.request.contextPath}/dev/user/security_center" class="cur">安全中心</a></li>
		                    <li><a href="${pageContext.request.contextPath}/dev/user/binduser">用户绑定/解绑</a></li>
		                </ul>
					</div>
					<div class="basic_info_cont">
		            	<div class="basic_info_title">
		            		手机号
		            	</div>
		            	<div class="page_msg mini">
							<div class="inner">
								<span class="msg_icon_wrp">
									<i class="icon_msg_mini waiting"></i>
								</span>
								<div class="msg_content">
									<p>
										<c:if test="${bool == 'init'}">
											设置手机号，保障账号安全方便及时找回！
										</c:if>
										<c:if test="${bool == 'false'}">
											设置失败！无效的手机号或验证码！
										</c:if>
										<c:if test="${bool == 'true'}">
											设置成功！
										</c:if>
									</p>
								</div>
							</div>
						</div>
						<form id="form" action="${pageContext.request.contextPath}/dev/user/setup_phone" method="post">
			                <ul class="basic_info_text clearfix">
			                	<li>
			                		<span>原手机号：</span>
			                		${phone}
			                	</li>
			                	<li>
			                		<span>新手机号：</span>
			                		<input type="text" name="phone" id="phone" class="frm_input" maxlength="50" style="width:150px"/>
			                	</li>
			                	<li>
			                		<input type="button" id="btnCode" class="btn-short" onclick="btnGetCode()" value="获取验证码" style="width:120px;margin-left:20px;height:26px;font-size:14px;line-height: 26px"/>
			                		<span class="" id="phonecode_msg" style="width:170px;"></span>
			                	</li>
			                	<li id="security_code_li" style="display:none;">
			                		<span>验证码：</span>
			                		<input type="text" name="code" id="code" class="frm_input" maxlength="50" style="width:150px"/>
			                	</li>
			                	<li>
			                		<a id="btn_info" class="revise_info" href="javascript:void(0);" onclick="savePhone();">设置</a>
			                	</li>
			                </ul>
		                </form>
		            </div>
		        </div>
		    </div>
		</div>
		<%@ include file="/dev_foot.jsp"%>
		<script src="${pageContext.request.contextPath}/js/jquery-1.10.1.min.js" type="text/javascript"></script>
		<script src="${pageContext.request.contextPath}/js/jquery.validate.min.js" type="text/javascript"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/dev/setup_phone.js"></script>
		<script type="text/javascript">
			jQuery(document).ready(function() {
				validate.init('${pageContext.request.contextPath}');
			});

			function btnGetCode(){
				jQuery('#phone').removeClass('frm_error');
				var phoneReg = /^1\d{10}$/;
				var phone=$('#phone').val();
				if(!phoneReg.test(phone)){
					jQuery('#phone').addClass('frm_error');
					return;
				}
				if(window.confirm("立即给手机号："+phone+",发送验证码？")){
					$.post("${pageContext.request.contextPath}/dev/user/send_phone_security_code",
						{phone:$('#phone').val()},
		   				function(data){
		   					if(data.flag){
		   						alert('验证码已发送到您的手机');
		   						codetime = 30;
		   						//屏蔽发送按钮
		   						jQuery('#btnCode').attr('disabled','disabled');
		   						unlockBtnCode();
		   						$('#security_code_li').show();
		   					}
		   					else{
		   						jQuery('#phone').addClass('frm_error');
		   						if(data.errorCode=='invalid_phone'){
		   							jQuery('#phone_error').html('无效手机号');
		   						}
		   						else if(data.errorCode=='exist_phone'){
		   							jQuery('#phone_error').html('该手机号已存在');
		   						}
		   						else if(data.errorCode=='send_error'){
		   							jQuery('#phone_error').html('操作失败，请重新获取');
		   						}
		   						else{
		   							jQuery('#phone_error').html('系统异常，请重新获取');
		   						}
		   					}
		   				}
	   				);
				}
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
			
			function savePhone(){
				if(validateForm.valid()){
					$("#form").submit();
				}
			}
		</script>
	</body>
</html>