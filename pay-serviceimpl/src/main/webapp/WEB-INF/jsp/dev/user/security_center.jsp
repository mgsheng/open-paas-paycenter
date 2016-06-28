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
			.basic_info_text li span{
			    width: 80px;
			}
			.revise_info {
				float: left;
			}
			input,select {
				width: 322px;
			}
			.help-inline{
				line-height: 0px;
				color: red; 
				padding-left: 85px;
			}
		</style>
	</head>
	<body>
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
										<c:if test="${email == null || email==''}">
											设置并激活邮箱，保障账号安全方便及时找回！<a href="${pageContext.request.contextPath}/dev/user/setup_email">设置</a>
										</c:if>
										<c:if test="${email != null && email!='' && emailBool=='false'}">
											邮箱未激活！<a href="${pageContext.request.contextPath}/dev/user/setup_email">修改</a>&nbsp;|&nbsp;<a id="emailCheck">激活</a><span id="countdown" style="width:200px;text-align: left;"></span>
										</c:if>
										<c:if test="${email != null && email!='' && emailBool=='true'}">
											邮箱验证通过！<a href="${pageContext.request.contextPath}/dev/user/setup_email">修改</a>
										</c:if>
									</p>
								</div>
							</div>
						</div>
						<div class="basic_info_title" style="margin-top:50px">
		            		手机号
		            	</div>
		            	<div class="page_msg mini">
							<div class="inner">
								<span class="msg_icon_wrp">
									<i class="icon_msg_mini waiting"></i>
								</span>
								<div class="msg_content">
									<p>
										<c:if test="${phoneBool=='false'}">
											设置手机号，保障账号安全方便及时找回！<a href="${pageContext.request.contextPath}/dev/user/setup_phone">设置</a>
										</c:if>
										<c:if test="${phoneBool=='true'}">
											手机号已设置完成！<a href="${pageContext.request.contextPath}/dev/user/setup_phone">修改</a>
										</c:if>
									</p>
								</div>
							</div>
						</div>
		            	<div class="basic_info_title"  style="margin-top:50px">
		            		密保问题
		            	</div>
		            	<div class="page_msg mini">
							<div class="inner">
								<span class="msg_icon_wrp">
									<i class="icon_msg_mini waiting"></i>
								</span>
								<div class="msg_content">
									<p>
										<c:if test="${problemBool=='false'}">
											设置密保问题，保障账号安全方便及时找回！<a href="${pageContext.request.contextPath}/dev/user/addproblem">设置</a>
										</c:if>
										<c:if test="${problemBool=='true'}">
											密保问题已设置完成！<a href="${pageContext.request.contextPath}/dev/user/checkproblem">修改</a>
										</c:if>
									</p>
								</div>
							</div>
						</div>
		            </div>
		        </div>
		    </div>
		</div>
		<%@ include file="/dev_foot.jsp"%>
		<script src="${pageContext.request.contextPath}/js/jquery-1.10.1.min.js" type="text/javascript"></script>
		<script src="${pageContext.request.contextPath}/js/jquery.validate.min.js" type="text/javascript"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/cookie.js"></script>
		<script type="text/javascript">
			var countTime = 180000; //3分钟
			jQuery(document).ready(function() {
				var beforeTime = $.cookie("beforeTime");
				if(beforeTime == null){
					$("#emailCheck").attr("href","javascript:emailActivation(${id},'${activatedEmail}')");
				}else{
					var diff = new Date().getTime() - beforeTime;
					if(diff >= countTime){
						$("#emailCheck").attr("href","javascript:emailActivation(${id},'${activatedEmail}')");
					}else{
						var countDown = countTime-diff;
						countDownTime();
						$("#emailCheck").attr("href","javascript:void(0);");
						emailTimeOut(countDown);
					}
				}
			});


			/**
			 * 倒计时显示
			 */
			function countDownTime(){
				var beforeTime = $.cookie("beforeTime");
				var diff = new Date().getTime() - beforeTime;
				var countDown = countTime-diff;
				var seconds = countDown/1000;  
	            var minutes = Math.floor(seconds/60);  
	            var CMinute= minutes % 60;  
	           	var CSecond= Math.floor(seconds%60);
	           	if(180000<=diff){
	           		$("#countdown").html(""); 
	           	}else{
	           		$("#countdown").html("<i>剩余时间："+CMinute+"</span><em>分</em><span style='width:20px;'>"+CSecond+"</span><em>秒</em>"); //输出有天数的数据  
	           	}
				setTimeout("countDownTime()",1000);  
			}
			function emailActivation(userId,email){
				if(confirm("立即给邮箱："+email+"发送邮件激活？","系统提示")){
					$("#emailCheck").attr("href","javascript:void(0);");
					$.cookie("beforeTime",new Date().getTime());
					$.post("${pageContext.request.contextPath}/dev/user/emailCheck.json",
						{userId:userId,
						email: email},
						function(data){
							if(data){
								alert("邮件已发送");
								$("#countdown");
								$("#emailCheck").attr("href","javascript:void(0);");
								$.cookie("beforeTime",new Date().getTime());
								emailTimeOut(countTime);
								countDownTime();
							}else{
								alert("邮件发送失败");
							}
					});
				}
			}
			function emailTimeOut(time){
				setTimeout(function(){
					$("#emailCheck").attr("href","javascript:emailActivation(${id},'${activatedEmail}')");
				},time)
			}
		</script>
	</body>
</html>