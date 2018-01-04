<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>易宝-pos支付</title>
<embed id="NPCom" type="application/x-yibao-plugin" width=0 height=0>
<OBJECT id="NLHostcomm" classid="clsid:CC872CC4-13DD-4E17-8A85-67FD420767E4" width=0 height=0
    codebase="yibaoATL.cab#version=1,0,0,0"></OBJECT>
    <style>
        html,body{
            margin: 0 !important;
            padding: 0 !important;
            width: 100% !important;
            height: 100% !important;
            background: #e5e5e5 !important;
        }
        .full-height{
            height: 100px;
            width: 100%;
        }
        .header{
            background: #ffffff;
            position: absolute;
            top: 0px;
            z-index: 10;
        }
        .header img{
            margin-left: 13%;
        }
        .footer{
            background: #282828;
            position: absolute;
            bottom: 0px;
            display: table;
        }
        .footer p{
            margin: 0;
            padding: 0;
            color: #fff;
            text-align: center;
            display: table-cell;
            vertical-align: middle;
        }
        .content{
            height: 100%;
            width: 100%;
        }
        .top{
            height: 550px;
            background: #eff7f9;
            background:-webkit-gradient(linear, 0 0, 0 bottom, from(#eff7f9), to(#ffffff));
            background:-moz-linear-gradient(top, #eff7f9, #ffffff);
            width: 100%;
        }
        .center{
            position: absolute;
            width: 76%;
            background: #fff;
            left: 12%;
            height: 100%;
            top: 0;
        }
        .center form{
            position: relative;
            top: 200px;
        }
        .center form table{
            margin: auto;
        }
        .bottom-button{
            text-align: center;
            margin-top: 60px;
        }
        .input{
            height: 30px;
            width: 330px;
            padding-left: 10px ;
            font-size: 18px;
        }
        .sure{
            background: #2eb6aa;
        }
        .pay{
            background: #069bf7;
        }
        .input-button{
            border: none;
            padding: 10px 40px;
            color: #fff;
            margin: 0 15px;
        }
    </style>
<script type="text/javascript">
window.onload = install();
function install() {
    var bInstall = true;
    var name = navigator.appName;
    var returnCode="";
    if(name == "Netscape") {
        var plugin = navigator.plugins["npyibao plugin"];
        if(plugin) {
            bInstall = false;
        }
    } else if(name == "Microsoft Internet Explorer") {
        try {
            var ax = new ActiveXObject("FirstCOM.math.1");
            bInstall =  false;
        } catch (e) {
        }
    } else {
        bInstall = false;
        alert("浏览器内核不支持该插件！");
    }
    if(bInstall) {
        if (confirm("您还没有安装插件\n\n现在下载安装吗？")) {
            location = "${pageContext.request.contextPath}/yeepayPos/download/yeepay/plug";
        }
    }
}

function getobj(){
	var obj = document.getElementById("NLHostcomm");
    if( typeof(obj.Req) == "undefined" ) {
        obj = document.getElementById("NPCom");
        if( typeof(obj.Req) == "undefined" ) {
            throw "该浏览器不支持插件，建议使用IE或360浏览器！";
        }
    }
    return obj;
}



function isInvaild(obj, len) {
    if(obj.length != len) {
        document.getElementById("result").innerHTML = obj.name + " 输入长度不对!";
        obj.focus();
        return true;
    }
    return false;
}

function DoTrans(cmd) {
   var totalFee='${totalFee}';
    var commpany='${commpany}';
    var merchantCode='${merchantCode}';
    var terminal='${terminal}';
    var payId='${payId}';
    var comNo='${comNo}';
    var baudrate='${baudrate}';
    var timeout='${timeout}';
    var orderId='${payOrderId}';
    var space = "                                                                                                    ";
	var buf="", bufr="", i, str;
    var errMsg = "正在交易中 请赖心等待... ..."
    var tcom;
    
    try {
        tcom = getobj();
    } catch(e) {
        alert(e);
        return false;
    }
    //数据有效性判断
    if (isInvaild(commpany, 11) 
        || isInvaild(merchantCode, 15)
        || isInvaild(terminal, 8)
        || isInvaild(totalFee, 12) ) {
        return false;
    }

	//通讯参数（100）
	str = comNo + "|"
    str += baudrate + "|172.17.253.197|28000|"
    str += timeout  + "|172.17.253.197|29000";
    buf = str + space.substr(0, 100 - str.length);
    //str = document.all.COM.value + "|"
    //str += document.all.BAUDRATE.value + "|172.17.253.197|28000|"
    //str += document.all.TIMEOUT.value  + "|172.17.253.197|29000";
    buf = str + space.substr(0, 100 - str.length);
	//交易指令（2）
    buf += cmd;
	//交易数据（11+15+8+40+30+12+40+80
	
	buf += commpany;
	buf += merchantCode;
	buf += terminal;
    str = orderId;
	buf += str + space.substr(0, 40 - str.length);
    str = payId;
	buf += str + space.substr(0, 30 - str.length);
	buf += totalFee;
    str = "";
	buf += str + space.substr(0, 40 - str.length);
    str = "";
	buf += str + space.substr(0, 80 - str.length);
    try{
        tcom.Req = buf;	
        errMsg = tcom.POSPChang();
        bufr = tcom.GetResponData();
    } catch(e) {
        document.getElementById("result").innerHTML = "控件运行失败, " + e;
        return false;
    }
    document.getElementById("result").innerHTML = errMsg;
    setValue(document.all.RETURNCODE, bufr, 2);
    setValue(document.all.TRACE, bufr, 6);
    setValue(document.all.TRANSCODER, bufr, 40);
    document.getElementById("inputForm").submit();
    return true;
}

var curBufPos = 0;

function setValue(control, buf, len, reset) {
    if(reset) {
        curBufPos = 0;
    }
    var end = curBufPos + len;
    if(end > buf.length) end = buf.length;
    var tmp = buf.substring(curBufPos, end);
    curBufPos += len;
    returnCode=tmp;
    control.value = tmp;
}
</script>
</head>
<body style="width: 100%;height: 100%">
    <div class="content">
        <div class="full-height header">
            <img src="${pageContext.request.contextPath}/images/pay.jpg" alt="jpg" />
            <!-- <img src="pay.jpg" alt="pay"> -->
        </div>
        <div class="content" style="">
            <div class="top"></div>
            <div class="center">
                <form id="inputForm" action="${pageContext.request.contextPath}/yeepay/pos/request" method="post">
                    <div>
                    
                        <table cellspacing="10">
                        <tr> <td colspan="2">
                        <div id="result" style="color:#F00;"></div><br>
                        
                        </td></tr>
                            <tr height="40px">
                                <td align="right" style="font-size: 16px;">订单号：</td>
                                <td ><input class="input" name="orderId" type="text" value="${orderId}" readonly= "true" >
                                <input type="hidden" name="RETURNCODE" value="" size=50>
                                 <input type="hidden" name="TRANSCODER" value="" size=50>
                                 <input type="hidden" name="TRACE" value="" size=50>
                                </td>
                            </tr>
                            <tr height="40px">
                                <td align="right" style="font-size: 16px;">金额：</td>
                                <td><input class="input" name="totalFee"  type="text" value="${orderAmount}" readonly= "true"></td>
                            </tr>
                        </table>
                        <div class="bottom-button">
                            <input type="button" value="确认交易" class="input-button sure" onClick=DoTrans("02")>
                            <input type="button" value="提交支付" class="input-button pay" onClick=DoTrans("01")>
                        </div>
                        <div class="bottom-button">
                            <p>
                                <span style="color: #2eb6aa;">确认交易：</span>当刷卡成功扣费后，返回信息提示“缴费不成功”时，
                                <br>可以点击此按钮进行缴费状态查询。
                            </p>
                        </div>
                    </div>
                </form>
            </div>
        </div>
        <div class="full-height footer">
            <p>版权所有：奥鹏教育Copyright ©2013-2015 open.com.cn All rights reserved</p>
        </div>
    </div>
</body>
</html>