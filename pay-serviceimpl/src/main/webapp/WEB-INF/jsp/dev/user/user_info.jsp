<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE HTML>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title>用户中心</title>
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/style2.css" />
	<link href="${pageContext.request.contextPath}/images/open_logo.ico" rel="Shortcut Icon" />
</head>
<body>
	<%@ include file="/dev_head.jsp"%>
	<input type="hidden" id='pageCode' value="userInfo">
	<div class="layout mt35 clearfix">
		<div class="w_1200">
	    	<div class="basic_info clearfix">
	        	<div class="basic_info_tab">
	                <ul>
	                    <li><a href="${pageContext.request.contextPath}/dev/user/user_info" class="cur">基本资料</a></li>
	                    <li><a href="${pageContext.request.contextPath}/dev/user/updatepwd">修改密码</a></li>
	                    <li><a href="${pageContext.request.contextPath}/dev/user/security_center">安全中心</a></li>
	                    <li><a href="${pageContext.request.contextPath}/dev/user/binduser">用户绑定/解绑</a></li>
	                </ul>
				</div>
	            <div class="basic_info_cont">
	            	<div class="basic_info_title">
	            		<a id="btn_info" class="revise_info" href="javascript:void(0);" onclick="showform();">修改</a>
	            		<a id="btn_info_update" class="revise_info" href="javascript:infoSubmit();" style="margin-right: 10px;display: none;">保存</a>
	            		基本资料
	            	</div>
	                <!--默认基本资料-->
	                <ul id="usrInfo" class="basic_info_text clearfix">
	                	<li><span>用&nbsp;&nbsp;户&nbsp;&nbsp;名：</span>${userName}</li>
	                	<li>
	                    	<span>邮&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;箱：</span>${email}
	                		<c:if test="${email != '' && email != null}">
								<c:if test="${emailActivation == 1}">
								   	<font color="green">（已验证）</font>
								</c:if>
								<c:if test="${emailActivation == 0 || emailActivation == null}">
								   	<font color="red">（未验证）</font>
								</c:if>
							</c:if>
	                	</li>
	                	<li><span>手&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;机：</span>${phone}</li>
	                	<li><span>姓&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;名：</span>${realName}</li>
	                	<li><span>昵&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;称：</span>${nickName}</li>
	                	<li></li>
	                	<p class="basic_info_pic">
	                		<span>头&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;像：</span>
	                		<c:if test="${headPicture != '' && headPicture != null}">
	                			<img width="100px" height="100px" src="${headPicture}" onerror="javascript:this.src='${pageContext.request.contextPath}/images/face_default.jpg';"/>
							</c:if>
	                	</p>
	                </ul>
	                <!--默认基本资料-->
	                <!--修改基本资料-->
	                <form id="usrForm" style="display: none;" onsubmit="adminusercheck()" action="${pageContext.request.contextPath}/dev/user/user_info" method="post">
		                <input type="hidden" id="input_id" name="id" value="${id}" />
						<input type="hidden" id="input_appUid" name="appUid" value="${appUid}" />
						<input type="hidden" name="dataType" value="userInfo" />
		                <ul class="basic_info_text clearfix">
		                	<li><span>用&nbsp;&nbsp;户&nbsp;&nbsp;名：</span>${userName}</li>
		                	<li><span>邮&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;箱：</span>${email}</li>
		                	<li><span>手&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;机：</span>${phone}</li>
		                	<li><span>姓&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;名：</span><input type="text" name="realName" value="${realName}"/></li>
		                	<li><span>昵&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;称：</span><input type="text" name="nickName" value="${nickName}"/></li>
		                    <li></li>
		                	<p class="basic_info_pic">
		                		<span>头&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;像：</span>
								<input type="hidden" value="${headPicture}" name="headPicture" id="headPicture" /> 
								<img width="100px" height="100px" onclick="uploadClick('iconUpload','${pageContext.request.contextPath}')" id="iconNewImg" src="${pageContext.request.contextPath}/images/wechat/icon.png"/>
								<input type="file" style="display: none;" id="iconUpload" name="file" accept="image/*" onchange="fileupload('iconNewImg','headPicture')" /> 
							</p>
		                </ul>
	                </form>
	                <!--修改基本资料-->
	            	<%@ include file="user_info_more.jsp"%>
	            </div>
	        </div>
	    </div>
	</div>
	<%@ include file="/dev_foot.jsp"%>
	<script src="${pageContext.request.contextPath}/js/jquery-1.10.1.min.js" type="text/javascript"></script>
	<script src="${pageContext.request.contextPath}/js/jquery.validate.min.js" type="text/javascript"></script>
	<script src="${pageContext.request.contextPath}/js/jquery.validate.method_zh_CN.js" type="text/javascript"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/cookie.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/dev/user_info.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/calendar/WdatePicker.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/js/dev/user_info_more.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/js/dev/ajaxfileupload.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/js/dev/uploadImage.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/js/cityselect/jquery.cityselect.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/js/common/dict_static_data.js"></script>
	<script type="text/javascript">
		jQuery(document).ready(function() {
			validate.init('${pageContext.request.contextPath}');
			//修改保存后__弹出验证消息
			if('${bool}'=='false'){
				alert('${message}');
			}
			$('#input_education').val('${userDetail_education}');
			$('#input_studyLevel').val('${userEducation_studyLevel}');
			$('#input_marriage').val('${userDetail_marriage}');
			$('#input_politics').val('${userDetail_politics}');
			$('#input_nation').val('${userDetail_nation}');
			$("#city_select").citySelect(
			{prov:'${userDetail_province}', city:'${userDetail_city}', dist:'${userDetail_region}',nodata:"none",required:false}); 
		});
		//加载详细信息div
		function loadUserInfoDiv(){
			if(jQuery('#userInfoDiv').css('display')=='block'){
				jQuery('#userInfoDiv').hide();
			}else{
				jQuery('#userInfoDiv').show();
			}
		}
		//显示修改
		function showform(){
			jQuery('#usrInfo').hide();
			jQuery('#usrForm').show();
			jQuery('#btn_info_update').show();
			jQuery('#btn_info').attr('onclick','showinfo();');
			jQuery('#btn_info').html('返回');
		}
		
		//显示详情
		function showinfo(){
			jQuery('#usrInfo').show();
			jQuery('#usrForm').hide();
			jQuery('#btn_info_update').hide();
			jQuery('#btn_info').attr('onclick','showform();');
			jQuery('#btn_info').html('修改');
		}
		
		function infoSubmit(){
			if(validateInfo.valid()){
				$("#usrForm").submit();
			}
		}
	</script>
</body>
</html>
