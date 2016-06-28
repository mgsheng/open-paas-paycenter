<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="utf-8" />
		<title>用户中心</title>
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/style2.css" />
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/password.css" />
		<link href="${pageContext.request.contextPath}/images/open_logo.ico" rel="Shortcut Icon" />
		<style type="text/css">
			.basic_info_text li {
			    width: 100%;
			}
			.basic_info_text li span{
			    width: 120px;
			}
			.revise_info {
				float: left;
			}
			.help-inline{
				line-height: 0px;
				color: red; 
				padding-left: 125px;
			}
			.pwdck_table{
				padding-top: 10px;
				padding-left: 125px;
				width: 282px;
			}
		</style>
	</head>
	<body>
		<%@ include file="/dev_head.jsp"%>
		<input type="hidden" id='pageCode' value="updatePwd">
		<div class="layout mt35 clearfix">
			<div class="w_1200">
		    	<div class="basic_info clearfix">
		        	<div class="basic_info_tab">
		                <ul>
		                    <li><a href="${pageContext.request.contextPath}/dev/user/user_info">基本资料</a></li>
		                    <li><a href="${pageContext.request.contextPath}/dev/user/updatepwd" class="cur">修改密码</a></li>
		                    <li><a href="${pageContext.request.contextPath}/dev/user/addproblem">安全中心</a></li>
		                    <li><a href="${pageContext.request.contextPath}/dev/user/binduser">用户绑定/解绑</a></li>
		                </ul>
					</div>
					<div class="basic_info_cont">
		            	<div class="basic_info_title">
		            		修改密码
		            	</div>
		            	<div class="page_msg mini">
							<c:if test="${bool=='null'}">
								<div class="inner">
									<span class="msg_icon_wrp">
										<i class="icon_msg_mini waiting"></i>
									</span>
									<div class="msg_content">
										<p>
											定期更换密码可以让您的账户更加安全；建议密码采用字母和数字混合，并且不短于6位
										</p>
									</div>
								</div>
							</c:if>
							<c:if test="${bool=='true'}">
								<div class="inner">
									<span class="msg_icon_wrp">
										<i class="icon_msg_mini success"></i>
									</span>
									<div class="msg_content">
										<p>
											修改密码成功！
										</p>
									</div>
								</div>
							</c:if>
							<c:if test="${bool=='false'}">
								<div class="inner">
									<span class="msg_icon_wrp">
										<i class="icon_msg_mini warn"></i>
									</span>
									<div class="msg_content">
										<p>
											修改密码失败
											<c:if test="${msg=='pwderror'}">
												<span>，密码错误！</span>
											</c:if>
										</p>
									</div>
								</div>
							</c:if>
						</div>
		                <form id="form" method="post">
			                <ul class="basic_info_text clearfix">
			                	<li>
			                		<span>当前密码：</span>
			                		<input type="password" name="oldpassword" class="frm_input" maxlength="20" />
			                	</li>
			                	<li>
			                		<span>新密码：</span>
			                		<input type="password" name="password" id="password" class="frm_input" maxlength="20" onKeyUp="checkIntensity(this.value)" />
			                		<table class="pwdck_table" border="0" cellpadding="0" cellspacing="0"> 
										<tr align="center"> 
											<td id="pwdck_Weak" class="pwdck pwdck_c">&nbsp;</td> 
											<td id="pwdck_Medium" class="pwdck pwdck_c pwdck_f">无</td> 
											<td id="pwdck_Strong" class="pwdck pwdck_c pwdck_c_r">&nbsp;</td> 
										</tr> 
									</table>
			                	</li>
			                	<li>
			                		<span>再次输入新密码：</span>
			                		<input type="password" name="conform_password" class="frm_input" maxlength="20" />
			                	</li>
			                	<li>
			                		<a id="btn_info" class="revise_info" href="javascript:void(0);" onclick="formSubmit();">修改</a>
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
		<script src="${pageContext.request.contextPath}/js/password.js" type="text/javascript"></script>
		<script src="${pageContext.request.contextPath}/js/dev/updatepwd.js" type="text/javascript"></script>
		<script type="text/javascript">
			jQuery(document).ready(function() {
				validate.init();
			});
			
			function formSubmit(){
				if(validateForm.valid()){
					$("#form").submit();
				}
			}
		</script>
	</body>
</html>