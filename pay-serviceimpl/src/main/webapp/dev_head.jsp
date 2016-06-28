<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<div class="layout top_banner">
	<div class="w_1200">
    	<div class="logo fl">
        	<a href="${pageContext.request.contextPath}/main.html"><img src="${pageContext.request.contextPath}/images/logo.png" /></a>
            <span class="usercent-bt"> | 用户中心</span>
        </div>
        <div class="nav fl">
        	<c:if test="${currentUsername!=null }">
	        	<ul>
	            	<li><a id="main" href="${pageContext.request.contextPath}/main.html">首页</a></li>
	            	<li><a id="userInfo" href="${pageContext.request.contextPath}/dev/user/user_info">基本资料</a></li>
	            	<li><a id="updatePwd" href="${pageContext.request.contextPath}/dev/user/updatepwd">修改密码</a></li>
	            	<li><a id="problem" href="${pageContext.request.contextPath}/dev/user/security_center">安全中心</a></li>
	            	<li><a id="bindUser" href="${pageContext.request.contextPath}/dev/user/binduser">用户绑定/解除</a></li>
	            </ul>
            </c:if>
        </div>
        <div class="loginbar fr">
        	<c:if test="${currentUsername!=null }">
	        	<span><a href="${pageContext.request.contextPath}/dev/user/user_info.html"><i class="iconbg useravatar"></i>${currentUsername}</a></span> 
	            <span class="mlr10">|</span> 
	        	<span><a href="${pageContext.request.contextPath}/logout">退出</a></span>
        	</c:if>
        	<c:if test="${currentUsername==null }">
        		<span><a href="http://www.open.com.cn" target="_self"><i class="iconbg useravatar"></i>返回官网</a></span> 
        	</c:if>
        </div>
	</div>
</div>
<script src="${pageContext.request.contextPath}/js/jquery-1.10.1.min.js" type="text/javascript"></script>
<script type="text/javascript">
jQuery(document).ready(function() {
   	var pageCode = $('#pageCode').val();
   	$('#'+pageCode+'').addClass('active'); 
});
</script>