<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>易宝支付插件调试Demo</title>

<embed id="NPCom" type="application/x-yibao-plugin" width=0 height=0>
<OBJECT id="NLHostcomm" classid="clsid:CC872CC4-13DD-4E17-8A85-67FD420767E4" width=0 height=0
    codebase="yibaoATL.cab#version=1,0,0,0"></OBJECT>

<style type="text/css">
    .btn { width:100px; height:30px; }
</style>
    
<script type="text/javascript">
window.onload = install();
function install() {
    var bInstall = true;
    var name = navigator.appName;
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
            location = "${pageContext.request.contextPath}/download/yeepay/plug";
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
    var orderId='${orderId}';
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
    control.value = tmp;
}
</script>
</head>
  <body onLoad="install()">
    <div id="result" style="color:#F00;">在此处显示每次执行过程中返回的错误信息</div><br>
    <form id="inputForm" action="${pageContext.request.contextPath}/yeepay/pos/request" method="post">
     	<table >
     	<tr><td>
            <input type="button" class="btn" value="签到" onClick=DoTrans("00")>
            <input type="button" class="btn" value="缴费" onClick=DoTrans("01")>
            <input type="button" class="btn" value="补登" onClick=DoTrans("02")>
            </td></tr>
 	   </table> 

 	<input type="hidden" name="RETURNCODE" value="" size=50>
 	<input type="hidden" name="orderId" value="${orderId}" size=50>
	 </form>
  </body>
</html>
