<%--
 * 
 * @author Shengzhao Li
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE HTML>
<html>
<head>
    <title>Home</title>
</head>
<body>
<h2>Spring Security&Oauth2 Client is work!</h2>


<div>
    操作说明:
    <ol>
        <li>
            <p>
                spring-oauth-client 的实现没有使用开源项目 <a
                    href="https://github.com/spring-projects/spring-security-oauth/tree/master/spring-security-oauth2"
                    target="_blank">spring-security-oauth2</a> 中提供的代码与配置, 如:<code>&lt;oauth:client
                id="oauth2ClientFilter" /&gt;</code>
            </p>
        </li>
        <li>
            <p>
                按照Oauth2支持的grant_type依次去实现. 共5类.
                <br/>
            <ul>
                <li>authorization_code</li>
                <li>password</li>
                <li>client_credentials</li>
                <li>implicit</li>
                <li>refresh_token</li>
            </ul>
        </li>
        <li>
            <p>
                <em>
                    在开始使用之前, 请确保 <a href="http://git.oschina.net/shengzhao/spring-oauth-server" target="_blank">spring-oauth-server</a>
                    项目已正确运行, 且 spring-oauth-client.properties (位于项目的src/main/resources目录) 配置正确
                </em>
            </p>
        </li>
    </ol>
</div>
<br/>

<p>
    &Delta; 注意: 项目前端使用了 Angular-JS 来处理动态数据展现.
</p>
<hr/>

<div>
    <strong>菜单</strong>
    <ul>
       <!--  <li>
            <p><a href="dirctPay">dirctPay</a> <br/>用户信息绑定接口</p>
        </li> -->
        <li>
            <p><a href="unifyPay">统一支付请求</a> <br/>统一支付请求接口</p>
        </li>
        <li>
            <p><a href="orderManualSend">订单手动补发</a> <br/>订单手动补发接口</p>
        </li>
         <li>
            <p><a href="orderQuery">订单查询</a> <br/>订单查询接口</p>
        </li>
        <li>
            <p><a href="orderRefund">订单退款</a> <br/>订单退款接口</p>
        </li>
        <li>
            <p><a href="getOrderQuery">汇银通订单查询</a> <br/>汇银通订单查询</p>
        </li>
        <li>
            <p><a href="unifyCosts">统一扣费</a> <br/>统一扣费接口</p>
        </li>
        <li>
            <p><a href="fileDownlond">对账文件下载申请</a> <br/>对账文件下载申请</p>
        </li>
        <li>
            <p><a href="payDitch">支付渠道接口</a> <br/>支付渠道接口</p>
        </li>
        <!-- <li>
            <p><a href="alipayCallBack">alipayCallBack</a> <br/>支付回调接口</p>
        </li> -->
    </ul>
</div>
</body>
</html>