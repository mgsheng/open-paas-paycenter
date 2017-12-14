<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312" />
<title>易宝支付插件调试Demo</title>

<embed id="NPCom" type="application/x-yibao-plugin" width=0 height=0>
<OBJECT id="NLHostcomm" classid="clsid:CC872CC4-13DD-4E17-8A85-67FD420767E4" width=0 height=0
    codebase="yibaoATL.cab#version=1,0,0,0"></OBJECT>

<style type="text/css">
    .btn { width:100px; height:30px; }
</style>
    
<SCRIPT>
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
            location = "yibaoSetup.msi";
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

function isInvaild(obj, len) {
    if(obj.value.length != len) {
        document.getElementById("result").innerHTML = obj.name + " 输入长度不对!";
        obj.focus();
        return true;
    }
    return false;
}

function DoTrans(cmd) {
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
    if (isInvaild(document.all.COMMPANYID, 11) 
        || isInvaild(document.all.MERCHANTID, 15)
        || isInvaild(document.all.TERMINALID, 8)
        || isInvaild(document.all.AMOUNT, 12) ) {
        return false;
    }

	//通讯参数（100）
    str = document.all.COM.value + "|"
    str += document.all.BAUDRATE.value + "|172.17.253.197|28000|"
    str += document.all.TIMEOUT.value  + "|172.17.253.197|29000";
    buf = str + space.substr(0, 100 - str.length);
	//交易指令（2）
    buf += cmd;
	//交易数据（11+15+8+40+30+12+40+80
	buf += document.all.COMMPANYID.value;
	buf += document.all.MERCHANTID.value;
	buf += document.all.TERMINALID.value;
    str = document.all.TRANSCODE.value;
	buf += str + space.substr(0, 40 - str.length);
    str = document.all.YIBAOORDER.value;
	buf += str + space.substr(0, 30 - str.length);
	buf += document.all.AMOUNT.value;
    str = document.all.SENDDATA.value;
	buf += str + space.substr(0, 40 - str.length);
    str = document.all.ADDDATA.value;
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

    setValue(document.all.COMMPANYIDR, bufr, 11, true);
    setValue(document.all.MERCHANTIDR, bufr, 15);
    setValue(document.all.TERMINALIDR, bufr, 8);
    setValue(document.all.TRANSCODER, bufr, 40);
    setValue(document.all.YIBAOORDERR, bufr, 30);
    setValue(document.all.AMOUNTR, bufr, 12);
    setValue(document.all.RETURNCODE, bufr, 2);
    setValue(document.all.PAN, bufr, 19);
    setValue(document.all.DATE, bufr, 8);
    setValue(document.all.TIME, bufr, 6);
    setValue(document.all.TRACE, bufr, 6);
    setValue(document.all.REFERENCENO, bufr, 12);
    setValue(document.all.PAYWAY, bufr, 1);
    setValue(document.all.SENDDATAR, bufr, 40);
    setValue(document.all.ADDDATAR, bufr, 80);
    setValue(document.all.REFUNDINF, bufr, 500);

    return true;
}
</SCRIPT>
</head>
  <body onLoad="install()">
    <div id="result" style="color:#F00;">在此处显示每次执行过程中返回的错误信息</div><br>
    <form>
     	<table >
        <tr><td>选择串口:</td>
    		<td><select name="COM">
                <option value="1" selected>COM1</option>
                <option value="2">COM2</option>
                <option value="3">COM3</option>
                <option value="4">COM4</option>
                <option value="5">COM5</option>
                <option value="6">COM6</option>
                <option value="7">COM7</option>
                <option value="8">COM8</option>
                <option value="9">COM9</option>
                <option value="10">COM10</option>
                </select>
                选择波特率:
                <select name="BAUDRATE">
                <option value="9600">9600</option>
                <option value="115200" selected>115200</option>
                </select>
                超时时间:<input type="text" name="TIMEOUT" value="120" size=5>
            </td></tr>
   		<tr><td>公司代码:</td>
    		<td><input type="text" name="COMMPANYID" value="000000     " size=50 maxlength="11"> 定长11位，不足需手动后补空格</td></tr>
   		<tr><td>商户编号:</td>
    		<td><input type="text" name="MERCHANTID" value="000000000000000" size=50 maxlength="15"> 定长15位，不足需手动后补空格</td></tr>
   		<tr><td>终端编号:</td>
    		<td><input type="text" name="TERMINALID" value="00000000" size=50 maxlength="8"> 定长 8位，不足需手动后补空格</td></tr>
   		<tr><td>订单号:</td>
    		<td><input type="text" name="TRANSCODE" value="000000000001" size=50 maxlength="40"> 最长40位</td></tr>
        <tr><td>易宝订单:</td>
    		<td><input type="text" name="YIBAOORDER" value="00000000002" size=50 maxlength="30"> 最长30位</td></tr>
   		<tr><td>金额:</td>
    		<td><input type="text" name="AMOUNT" value="000000000001" size=50 maxlength="12"> 定长12位，不足需手动后补空格</td></tr>
    	<tr><td> 原数据:</td>
    		<td><input type="text" name="SENDDATA" value="" size=50 maxlength="40"> 最长40位，不支持中文数据</td></tr>
   		<tr><td>附加数据:</td>
    		<td><input type="text" name="ADDDATA" value="" size=50  maxlength="80"> 最长80位，支持中文数据</td></tr>
 		<tr><td></td><td>
            <input type="button" class="btn" value="签到" onClick=DoTrans("00")>
            <input type="button" class="btn" value="缴费" onClick=DoTrans("01")>
            <input type="button" class="btn" value="补登" onClick=DoTrans("02")>
            <input type="button" class="btn" value="结算" onClick=DoTrans("03")>
            <input type="button" class="btn" value="余额" onClick=DoTrans("04")></td></tr>
        <tr><td></td><td>
            <input type="button" class="btn" value="TMS" onClick=DoTrans("TS")>
            <input type="button" class="btn" value="订单撤销" onClick=DoTrans("31")>
            <input type="button" class="btn" value="订单查询" onClick=DoTrans("35")>
            <input type="button" class="btn" value="退货" onClick=DoTrans("10")>
            <input type="button" class="btn" value="商家扫码支付" onClick=DoTrans("11")></td></tr>
        <tr><td></td><td>
            <input type="button" class="btn" value="用户扫码支付" onClick=DoTrans("12")>
            <input type="button" class="btn" value="银联扫码" onClick=DoTrans("13")>
            <input type="button" class="btn" value="扫码订单查询" onClick=DoTrans("14")>
            <input type="button" class="btn" value="扫码订单退货" onClick=DoTrans("15")>
            <input type="button" class="btn" value="扫码退货查询" onClick=DoTrans("16")></td></tr>
 	</table> 
 
 	<table><tr><td>--------------------------------------------------------------------------</td></table>

 	<table>
   		<tr><td>公司代码:</td>
    		<td><input type="text" name="COMMPANYIDR" value="" size=50></td></tr>
   		<tr><td>商户编号:</td>
    		<td><input type="text" name="MERCHANTIDR" value="" size=50></td></tr>
   		<tr><td>终端编号:</td>
    		<td><input type="text" name="TERMINALIDR" value="" size=50></td></tr>
   		<tr><td>订单号:</td>
    		<td><input type="text" name="TRANSCODER" value="" size=50></td></tr>
        <tr><td>易宝订单:</td>
    		<td><input type="text" name="YIBAOORDERR" value="" size=50></td></tr>
   		<tr><td>金额:</td>
    		<td><input type="text" name="AMOUNTR" value="" size=50></td></tr>
    		<tr><td>返回码:</td>
    		<td><input type="text" name="RETURNCODE" value="" size=50></td></tr>
   		<tr><td>卡号:</td>
    		<td><input type="text" name="PAN" value="" size=50></td></tr>
   		<tr><td>交易日期:</td>
    		<td><input type="text" name="DATE" value="" size=50></td></tr>
    		<tr><td>交易时间:</td>
    		<td><input type="text" name="TIME" value="" size=50></td></tr>
   		<tr><td>流水号:</td>
    		<td><input type="text" name="TRACE" value="" size=50></td></tr>
   		<tr><td>参考号:</td>
    		<td><input type="text" name="REFERENCENO" value="" size=50></td></tr>
        <tr><td>支付渠道:</td>
    		<td><input type="text" name="PAYWAY" value="" size=50></td>
            <td>“1”微信，“2”支付宝，“3”银联，“4”其他</td></tr>
     	<tr><td>原数据:</td>
    		<td><input type="text" name="SENDDATAR" value="" size=50></td></tr>
   		<tr><td>附加数据:</td>
    		<td><input type="text" name="ADDDATAR" value="" size=50></td></tr>
        <tr><td>退货信息:</td>
    		<td><textarea style="width:100%;" name="REFUNDINF" value="" clos="100" rows=3></textarea></td>
            <td>最长500字节，格式：len(3) + data1 + "|" + ... + "|" + dataN<br>
            data: 进度(1) + 时间(14，yyyyMMddHHmmss) + 金额(12，000000000001)<br>
            进度: 1(提交退款中)、2(退款处理中)、3(退款成功)、4(退款已取消)</td></tr>
    	</table>
	 </form>
  </body>
</html>
