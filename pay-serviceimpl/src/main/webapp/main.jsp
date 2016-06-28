<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE HTML>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <title>用户中心</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/style3.css" />
    <link href="${pageContext.request.contextPath}/images/open_logo.ico" rel="Shortcut Icon" />
</head>
<body>
	<%@ include file="dev_head.jsp"%>
	<input type="hidden" id='pageCode' value="main">
	<!--个人信息、焦点图、安全级别-->
	<div class="layout mt35 clearfix">
		<div class="w_1200">
	    	<div class="userinfo_safety fl">
	        	<div class="userface">
	        		<c:if test="${headPicture != '' && headPicture != null}">
	        			<img style="width:150px;height:150px" src="${headPicture}" alt="" class="avatar" onerror="javascript:this.src='${pageContext.request.contextPath}/images/face_default.jpg';"/>
					</c:if>
					<c:if test="${headPicture == '' || headPicture == null}">
						<img style="width:150px;height:150px" src="${pageContext.request.contextPath}/images/face_default.jpg" alt="" class="avatar"/>
					</c:if>
	            	<p class="name">${currentUsername}</p>
	            </div>
	            <ul class="usertext">
	            	<li>姓名：<font id="realName" title="${realName}"></font></li>
	                <li>昵称：<font id="nickName" title="${nickName}"></font></li>
	            </ul>
	        </div>
	    	<div class="focus fl" id="index_image"></div>
	    	<div class="userinfo_safety fr">
	        	<h3 class="safety_title"><i class="iconbg safety"></i>安全级别</h3>
	            <ul class="usertext">
	            	<li>手机：
	            		<c:if test="${phone != ''}">
						   	<font color="green">[已设置]</font>
						</c:if>
						<c:if test="${phone == '' || phone == null}">
						    <font color="red">[</font><a href="${pageContext.request.contextPath}/dev/user/setup_phone" style="color: red;">未设置</a><font color="red">]</font>
						</c:if>
	            	</li>
	                <li>邮箱：
	                	<c:if test="${email == '' || email == null}">
						    <font color="red">[</font><a href="${pageContext.request.contextPath}/dev/user/setup_email" style="color: red;">未设置</a><font color="red">]</font>
						</c:if>
						<c:if test="${email != '' && email != null}">
							<c:if test="${emailActivation == 1}">
							   	<font color="green">[已验证]</font>
							</c:if>
							<c:if test="${emailActivation == 0 || emailActivation == null}">
							    <font color="red">[</font><a href="${pageContext.request.contextPath}/dev/user/security_center" style="color: red;">未验证</a><font color="red">]</font>
							</c:if>
						</c:if>
	                </li>
	            	<li>问题：
	            		<c:if test="${problemStatus == 1}">
						   	<font color="green">[已设置]</font>
						</c:if>
						<c:if test="${problemStatus == 0}">
						    <font color="red">[</font><a href="${pageContext.request.contextPath}/dev/user/addproblem" style="color: red;">未设置</a><font color="red">]</font>
						</c:if>
	            	</li>
	            	<li>&nbsp;</li>
	            	<li>
	            		最近登录时间：<br/>${lastLoginTime }
	            	</li>
	            </ul>
	        </div>
	        <h2 class="page-title fl mt20 clearfix">我的应用</h2>
	    </div>
	</div>
	<!--个人信息、焦点图、安全级别-->
	<!--应用列表-->
	<div class="layout bg_f6">
		<div id="app_list" class="w_1200">
	    </div>
	</div>
	<!--应用列表-->
	<%@ include file="dev_foot.jsp"%>
	<script src="${pageContext.request.contextPath}/js/jquery-1.10.1.min.js" type="text/javascript"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/js/layer/layer.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/js/cookie.js"></script>
	
	<script type="text/javascript">
        jQuery(document).ready(function() {
			//加载提示信息
			loadConfirm();

			//加载首页图片公告
			loadIndexImages();
			
        	//加载当前登陆用户所有授权过的APP
        	loadAppList();
        	
        	jQuery('#realName').html(loadText('${realName}',13));
        	jQuery('#nickName').html(loadText('${nickName}',13));
        });

        function loadAppList(){
        	$.post("${pageContext.request.contextPath}/dev/find_app_list",null,
   				function(data){
   					var htmlStr = "";
   					$.each(data.appList,function(){
   						htmlStr += '<div class="applist">';
   			            htmlStr += '<div class="app_img fl"><img src="'+this.icon+'" alt="应用图片"/></div>';
   			            htmlStr += '<div class="app_info fl">';
   		                htmlStr += '<h3 class="app_info_title">'+loadText(this.name,15)+'</h3>';
   		                htmlStr += '<div class="app_info_text">';
   	                    htmlStr += '<p>'+this.description+'</p>';
   		                htmlStr += '</div>';
   			            htmlStr += '</div>';
   			            htmlStr += '<div class="app_btn fl"><a href="javascript:void(0)" onclick="loadUrl(\''+this.callbackUrl+'\')" class="goplatforms">进入平台</a></div>';
   			        	htmlStr += '</div>';
   					});
   					$('#app_list').html(htmlStr);
   				}
   			);
        }

        function loadIndexImages(){
        	$.post("${pageContext.request.contextPath}/dev/find_index_images",null,
   				function(data){
   					var htmlStr = "";
   					$.each(data.indexImageList,function(){
   						htmlStr = "";
   						if(this.type==data.type_url){
   							htmlStr += '<a href="javascript:void(0);" onclick="loadUrl(\''+this.url+'\')">';
   						}else if(this.type==data.type_contents){
   							htmlStr += '<a target="_blank" href="${pageContext.request.contextPath}/index_images_detail?idkey='+this.idkey+'">';
   						}
   						htmlStr +='<img style="background-image:url("'+this.imgUrl+'");height:300px;width:176px;" src="'+this.imgUrl+'"/>';
   						htmlStr +='</a>';
   					});
   					if(htmlStr == ''){
   						htmlStr = "<img src='${pageContext.request.contextPath}/images/focus_01.jpg'/>";
   					}
   					$('#index_image').html(htmlStr);
   				}
   			);
        }
        
        function loadConfirm(){
        	var doNotPrompt = $.cookie("doNotPrompt");
			if($.trim(doNotPrompt) == ''|| doNotPrompt == 'null'){
	        	var confirmStr = "";
				if('${problemStatus}' == 0){
					if($.trim(confirmStr) == ''){
						confirmStr += '系统检测到您账号安全级别低！<br/>';
					}
					confirmStr += '密保问题未设置！请及时去设置！<a href="${pageContext.request.contextPath}/dev/user/addproblem">设置密保问题</a><br/>';
				}
				if($.trim('${email}') != '' && ('${emailActivation}' == 0 || $.trim('${emailActivation}') == '')){
					if($.trim(confirmStr) == ''){
						confirmStr += '系统检测到您账号安全级别低！<br/>';
					}
					confirmStr += '邮箱未验证！请及时去验证！<a href="${pageContext.request.contextPath}/dev/user/user_info">验证邮箱</a><br/>';
				}
				if($.trim('${email}') == '' ){
					if($.trim(confirmStr) == ''){
						confirmStr += '系统检测到您账号安全级别低！<br/>';
					}
					confirmStr += '邮箱未设置！请及时去设置！<a href="${pageContext.request.contextPath}/dev/user/user_info">设置邮箱</a><br/>';
				}
				if($.trim(confirmStr) != ''){
					layer.confirm(confirmStr, {
					    btn: ['不再提示','知道了'] //按钮
					}, function(index){
						var expiresDate= new Date();
						expiresDate.setTime(expiresDate.getTime() + (3*30*24*60*60*1000));
						$.cookie("doNotPrompt",new Date().getTime(), {
				            path : '/',           //cookie的作用域
				            expires : expiresDate
				        });
						layer.close(index);
						loadUpdatePwdPrompt();
					}, function(index){
						loadUpdatePwdPrompt();
					});
				}
			}else{
				loadUpdatePwdPrompt();
			}
        }

        function loadUpdatePwdPrompt(){
        	var updatePwdPrompt = $.cookie("updatePwdPrompt");
			if($.trim(updatePwdPrompt) == ''|| updatePwdPrompt == 'null'){
				if('${detectPwd}' == 1){
					layer.confirm('系统检测到您账号密码很久没有修改！<br/>请及时去修改您的账号密码<a href="${pageContext.request.contextPath}/dev/user/updatepwd">去修改</a>', {
					    btn: ['不再提示','知道了'] //按钮
					}, function(index){
						var expiresDate= new Date();
						expiresDate.setTime(expiresDate.getTime() + (3*30*24*60*60*1000));
						$.cookie("updatePwdPrompt",new Date().getTime(), {
				            path : '/',           //cookie的作用域
				            expires : expiresDate
				        });
						layer.close(index);
					});
				}
			}
        }
        function loadText(text,sub){
			if($.trim(text) != '' && text.length > sub){
				return text.substring(0,sub)+"...";
			}
			return text;
        }

        function loadUrl(url){
        	window.open(url);
        }
    </script>
</body>
</html>