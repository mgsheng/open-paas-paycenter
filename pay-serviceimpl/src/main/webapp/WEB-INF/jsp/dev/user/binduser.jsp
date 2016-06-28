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
			.frm_select {
				width: 322px;
			}
		</style>
	</head>
	<body class="page_setting pwd page_introduction">
		<%@ include file="/dev_head.jsp"%>
		<input type="hidden" id='pageCode' value="bindUser">
		<div class="layout mt35 clearfix">
			<div class="w_1200">
		    	<div class="basic_info clearfix">
		        	<div class="basic_info_tab">
		                <ul>
		                    <li><a href="${pageContext.request.contextPath}/dev/user/user_info">基本资料</a></li>
		                    <li><a href="${pageContext.request.contextPath}/dev/user/updatepwd">修改密码</a></li>
		                    <li><a href="${pageContext.request.contextPath}/dev/user/addproblem">安全中心</a></li>
		                    <li><a href="${pageContext.request.contextPath}/dev/user/binduser" class="cur">用户绑定/解绑</a></li>
		                </ul>
					</div>
					<div class="basic_info_cont" style="margin-bottom: -90px;">
		            	<div class="basic_info_title">
		            		用户绑定/解绑
		            	</div>
		            	<div class="page_msg mini">
							<div class="inner">
								<span class="msg_icon_wrp">
									<i class="icon_msg_mini waiting"></i>
								</span>
								<div class="msg_content">
									<p>
										用户绑定：将目前登录账号为主账号，绑定的账号为副账号
										<br/>
										用户解绑：将目前登录账号的副账号解除绑定
									</p>
								</div>
							</div>
							<br/>
							<c:if test="${bool=='true'}">
								<div class="inner">
									<span class="msg_icon_wrp">
										<i class="icon_msg_mini success"></i>
									</span>
									<div class="msg_content">
										<p>
											<c:if test="${type==1}">
												用户绑定成功！
											</c:if>
											<c:if test="${type==2}">
												用户解绑成功！
											</c:if>
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
											<c:if test="${type==1}">
												用户绑定失败
											</c:if>
											<c:if test="${type==2}">
												用户解绑失败
											</c:if>
											<c:if test="${msg=='pwderror'}">
												<span>，用户名或密码错误！</span>
											</c:if>
											<c:if test="${msg=='selfuser'}">
												<span>，不能绑定/解绑自己！</span>
											</c:if>
											<c:if test="${msg=='hasuser'}">
												<span>，该用户已经被绑定！</span>
											</c:if>
											<c:if test="${msg=='nobind'}">
												<span>，该用户没有被绑定！</span>
											</c:if>
											<c:if test="${msg=='hasparent'}">
												<span>，该用户为主账号不能被绑定！</span>
											</c:if>
											<c:if test="${msg=='hassameapp'}">
												<span>，该用户和目前登录用户存在相同应用信息不能被绑定！</span>
											</c:if>
											<c:if test="${msg=='noself'}">
												<span>，您不是该用户的主账号！</span>
											</c:if>
										</p>
									</div>
								</div>
							</c:if>
						</div>
		                <form id="form" method="post">
		                	<input type="hidden" id="type" name="type" value="1" />
			                <ul class="basic_info_text clearfix">
			                	<li>
			                		<span>用户名：</span>
			                		<input type="text" name="username" class="frm_input" maxlength="20" />
			                	</li>
			                	<li>
			                		<span>密码：</span>
			                		<input type="password" name="password" id="password" class="frm_input" maxlength="20" />
			                	</li>
			                	<li>
			                		<a id="btn_info" class="revise_info" href="javascript:void(0);" onclick="bind();">绑定</a>
			                		<a id="btn_info" class="revise_info" href="javascript:void(0);" onclick="unbind();" style="margin-left:20px;">解绑</a>
			                	</li>
			                </ul>
		                </form>
		            </div>
		            <div class="basic_info_cont">
		            	<div class="basic_info_title">
		            		已绑定用户列表
		            	</div>
		                <table style="width: 100%">
							<thead>
								<tr>
									<th>用户名</th>
									<th>姓名</th>
									<th>手机号</th>
									<th>邮箱</th>
								</tr>
							</thead>
							<tbody id="bindlist_tbody">
							</tbody>
						</table>
		            </div>
		        </div>
		    </div>
		</div>
		<%@ include file="/dev_foot.jsp"%>
		<script src="${pageContext.request.contextPath}/js/jquery-1.10.1.min.js" type="text/javascript"></script>
		<script src="${pageContext.request.contextPath}/js/jquery.validate.min.js" type="text/javascript"></script>
		<script src="${pageContext.request.contextPath}/js/dev/binduser.js" type="text/javascript"></script>
		<script type="text/javascript">
			jQuery(document).ready(function() {
				validate.init();
				loadBindUser();
			});
			
			function loadBindUser(){
				jQuery.post("${pageContext.request.contextPath}/dev/user/bindlist.json",{
					},function(data){
						var htmlStr="";//拼装表格数据
						jQuery.each(data.userlist,function(){
							htmlStr += '<tr align="center">';
							htmlStr += '<td>'+this.username+'</td>';
							htmlStr += '<td>'+this.realname+'</td>';
							htmlStr += '<td>'+this.phone+'</td>';
							htmlStr += '<td>'+this.email+'</td>';
							htmlStr += '</tr>';
						});
						jQuery('#bindlist_tbody').html(htmlStr);
			    	}
					
				);
			}
			
			function bind(){
				jQuery('#type').val(1);
				if(validateForm.valid()){
					$("#form").submit();
				}
			}
			
			function unbind(){
				jQuery('#type').val(2);
				if(validateForm.valid()){
					$("#form").submit();
				}
			}
		</script>
	</body>
</html>