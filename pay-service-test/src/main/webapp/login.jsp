<%--
 * 
 * @author Shengzhao Li
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE HTML>
<html>
<head>
    <title>Oauth Client Login</title>
</head>
<body>
<a href="${contextPath}/">Home</a>

<h2>Oauth Client Login</h2>

<form action="${contextPath}/login.do" method="post">

    <label for="username">Username:</label>
    <input type="text" id="username" name="j_username" value="" required="required"/>
    <br/>
    <br/>
    <label for="password">Password:</label>
    <input type="password" name="j_password" id="password" value="" required="required"/>
    <br/>
    <input type="submit" value="Login" class="btn btn-primary"/>
    <span style="color:red;">
        <c:if test="${param.authorization_error eq 2}">Access denied !!!</c:if>
        <c:if test="${param.authentication_error eq 1}">Authentication Failure</c:if>
    </span>
</form>
<div>
    <p>You can use the users to login as follow:</p>
    <table class="table table-bordered">
        <thead>
        <tr>
            <th>Username</th>
            <th>Password</th>
            <th>Privileges</th>
        </tr>
        </thead>
        <tbody>
        <tr>
            <td>unity_user</td>
            <td>unity_pass</td>
            <td>ROLE_UNITY</td>
        </tr>
        <tr>
            <td>mobile_user</td>
            <td>mobile_pass</td>
            <td>ROLE_MOBILE</td>
        </tr>
        </tbody>
    </table>
</div>
</body>
</html>