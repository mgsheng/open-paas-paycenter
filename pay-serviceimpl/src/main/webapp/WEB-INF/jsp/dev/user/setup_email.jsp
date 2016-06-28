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
		            		邮箱
		            	</div>
		            	<div class="page_msg mini">
							<div class="inner">
								<span class="msg_icon_wrp">
									<i class="icon_msg_mini waiting"></i>
								</span>
								<div class="msg_content">
									<p>
										设置邮箱，保障账号安全方便及时找回！
									</p>
								</div>
							</div>
						</div>
						<form id="form" action="${pageContext.request.contextPath}/dev/user/setup_email" method="post">
			                <ul class="basic_info_text clearfix">
			                	<li>
			                		<span>原邮箱：</span>
			                		${email}
			                	</li>
			                	<li>
			                		<span>新邮箱：</span>
			                		<input type="text" name="email" id="email" class="frm_input" maxlength="50" style="width:150px"/>
			                	</li>
			                	<li>
			                		<a id="btn_info" class="revise_info" href="javascript:void(0);" onclick="saveEmail();">设置</a>
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
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/dev/setup_email.js"></script>
		<script type="text/javascript">
			jQuery(document).ready(function() {
				validate.init('${pageContext.request.contextPath}');
			});
			function saveEmail(){
				if(validateForm.valid()){
					$("#form").submit();
				}
			}
		</script>
	</body>
</html>