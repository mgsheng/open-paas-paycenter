<%--
 * 
 * @author Shengzhao Li
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE HTML>
<html>
<head>
    <title>User Info</title>
</head>
<body>
<a href="${contextPath}/">Home</a>

<h2>User Info
    <small>数据来源于 'spring-oauth-server' 中提供的API接口</small>
</h2>

<dl class="dl-horizontal">
    <dt>username</dt>
    <dd><code>${userDto.username}</code></dd>
    <dt>guid</dt>
    <dd><code>${userDto.guid}</code></dd>
    <dt>phone</dt>
    <dd><code>${userDto.phone}</code></dd>
    <dt>email</dt>
    <dd><code>${userDto.email}</code></dd>
    <dt>nickName</dt>
    <dd><code>${userDto.nickName}</code></dd>
    <dt>realName</dt>
    <dd><code>${userDto.realName}</code></dd>
    <dt>headPicture</dt>
    <dd><code>${userDto.headPicture}</code></dd>
    <dt>privileges</dt>
    <dd><code>${userDto.privileges}</code></dd>

</dl>

<a href="javascript:history.back();" class="btn btn-default">Back</a>
</body>
</html>