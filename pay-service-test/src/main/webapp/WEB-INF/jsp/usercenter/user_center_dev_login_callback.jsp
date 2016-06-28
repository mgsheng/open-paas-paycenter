<%--
 * 
 * @author Shengzhao Li
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE HTML>
<html>
<head>
    <title>UserCenterLogin</title>
</head>
<body>
<a href="${contextPath}/">Home</a>
<div class="panel panel-default">
    <div class="panel-body">
        <div ng-controller="AuthorizationCodeCtrl" class="col-md-10">
           	<div ng-show="visible">
                   <div class="well well-sm">
                       <span class="text-muted">最终发给 spring-oauth-server的 URL:</span>
                       <br/>
                       <div class="text-primary">
                       <a href="${url}">${url}</a>
                       </div>
                       <p class="help-block">若登录不成功，请检查userId\appkey\appsecret等数据是否填写正确！</p>
                   </div>
               </div>
        </div>
    </div>
</div>
</body>
</html>