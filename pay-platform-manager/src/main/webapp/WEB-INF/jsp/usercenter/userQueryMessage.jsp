<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/themes/default/easyui.css">
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/themes/icon.css">
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/dataList.css">
	<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.min.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.easyui.min.js"></script>
	<script type="text/javascript"
	src="${pageContext.request.contextPath}/js/locale/easyui-lang-zh_CN.js"></script>
</head>
<body>
	<div class="top" style="width: 100%">
	<div style="margin:20px 0;"></div>
	<div class="easyui-panel" title="查询条件" style="width:100%;max-width:100%;padding:20px 30px;">
		<form id="ff" method="post">
		<table >
		<tr>
			<td style="text-align: right;">订单号：</td>
			<td>
				<input class="easyui-textbox" name="orderId" id="orderId" style="width:100%">
			</td>
			<td style="text-align: right;">商户订单号：</td>
			<td>
				<input class="easyui-textbox" name="merchantOrderId" id="merchantOrderId" style="width:100%">
			</td>
			<td style="text-align: right;">第三方订单号：</td>
			<td><input id="payOrderId" name="payOrderId" class="easyui-textbox" style="width:100%"></td>
		</tr>
		<tr>
			<td style="text-align: right;">支付方式：</td>
			<td>
				<select class="easyui-combobox" data-options="editable:false" id="channelId" name="channelId" style="width:100%">
					<option value="">全部</option>
					<option value="10001">支付宝</option>
					<option value="10002">微信</option>
					<option value="10003">银联</option>
					<option value="10005">易宝</option>
					<option value="10006">易宝pos</option>
					<option value="10004">TCL汇银通</option>
				</select>
			</td>
			<td style="text-align: right;">业务类型：</td>
			<td>
				<select class="easyui-combobox" data-options="editable:false" name="appId" style="width:100%">
						<option value=""  selected="selected">全部</option>
						<option value="1">OES学历</option>
						<option value="10026" >mooc2u</option>
				</select>
			</td>
			<td style="text-align: right;">用户名：</td>
			<td>
				<input class="easyui-textbox" name="userName" id="userName" style="width:100%">
			</td>
		</tr>
		<tr>
			<td style="text-align: right;">下单时间：</td>
			<td>
				<a href="#" class="easyui-linkbutton" data-options="plain:true"	onclick="getDayType('today')">今天</a>
				<a href="#" class="easyui-linkbutton" data-options="plain:true"	onclick="getDayType('both')">2天</a>
				<a href="#" class="easyui-linkbutton" data-options="plain:true"	onclick="getDayType('seven')">7天</a>
				<a href="#" class="easyui-linkbutton" data-options="plain:true" onclick="getDayType('thirty')">30天</a>
			</td>
			
			<td style="text-align: right;"><input id="startDate" name="startDate" class="easyui-datebox" data-options="sharedCalendar:'#cc'" editable="false">—到—</td>
			<td><input id="endDate" name="endDate" class="easyui-datebox" data-options="sharedCalendar:'#cc'" editable="false"></td>
			<td style="text-align: right;"><a href="javascript:void(0)" class="easyui-linkbutton" onclick="submitForm()" style="width:80px">提交</a></td>
			<td>
				<a href="javascript:void(0)" class="easyui-linkbutton" onclick="clearForm()" style="width:80px">清空</a>
			</td>
		</tr>
		
		</table>
		</form>
		
	</div>
	
	</div>
	  <div class="botton" style="width: 100%;height: 300px">
		<table  id="dg"  class="easyui-datagrid" title="查询结果"    style="width:100%;max-width:100%;padding:20px 30px;"
			data-options="singleSelect:true,method:'get'">
		<thead>
			<tr>
				<th data-options="field:'id',width:180">订单号</th>
				<th data-options="field:'merchantOrderId',width:150">商户订单号</th>
				<th data-options="field:'payOrderId',width:150">第三方订单号</th>
				<th data-options="field:'userName',width:60,align:'center'">用户名</th>
				<th data-options="field:'foundDate',width:150">下单时间</th>
				<th data-options="field:'businessDate',width:150">交易时间</th>
				<th data-options="field:'channelName',width:80">支付方式</th>
				<th data-options="field:'appId',width:60,align:'center'">业务类型</th>
				<th data-options="field:'paymentName',width:80">发卡行</th>
				<th data-options="field:'openingBank',width:100">卡类型</th><!-- 不确定字段 -->
				<th data-options="field:'source',width:80,align:'right'">缴费来源</th>
				<th data-options="field:'payStatusName',width:80,align:'right'">交易状态</th>
				<th data-options="field:'amount',width:80">缴费金额</th>
				<th data-options="field:'payAmount',width:60,align:'center'">实收金额</th>
				<th data-options="field:'payCharge',width:60,align:'center'">手续费</th>
			</tr>
		</thead>
		
	</table>
	</div>
	
<div id="cc" class="easyui-calendar"></div>

</body>
<script>


		function submitForm(){
			var orderId = $("input[name='orderId']").val();//商户订单号
			var merchantOrderId = $("input[name='merchantOrderId']").val();//商户订单号
			var payOrderId = $("input[name='payOrderId']").val();//第三方订单号
			var channelId = $("input[name='channelId']").val();//支付方式
			var appId = $("input[name='appId']").val();//业务类型
			var userName = $("input[name='userName']").val();//商户订单号
			var createDate =$("input[name='createDate']:checked").val(); 
			var startDate = $("#_easyui_textbox_input7").val();
			var endDate = $("#_easyui_textbox_input8").val();
			
			if(orderId==""&&merchantOrderId==""&&payOrderId==""&&channelId==""&&appId==""&&userName==""){
				if(startDate==""||endDate==""){
					alert("请选择时间段");
					return;
				}
			}
			if(!startDate==""){
				if(endDate==""){
					alert("请选择结束时间");
					return;
				}
			}
			if(!endDate==""){
				if(startDate==""){
					alert("请选择开始时间");
					return;
				}
			}
			if(!startDate==""&&!endDate==""){
				if(startDate>endDate){
					alert("开始时间大于结束时间！");
					return;
				}
			}
			
			
			
			
			
			$('#dg').datagrid({
				collapsible:true,
				rownumbers:true,
				pagination:true,
		        url: "${pageContext.request.contextPath}/manage/userQueryMessage?orderId="+orderId+"&merchantOrderId="+merchantOrderId+"&payOrderId="+payOrderId+"&channelId="+channelId+"&appId="+appId+"&createDate="+createDate+"&startDate="+startDate+"&endDate="+endDate+"&userName="+userName,  
		        //pagination: true,显示分页工具栏
		        onLoadSuccess:function(data){
                    if (data.total<1){
                       $.messager.alert("提示","表单无数据!");
                  }
                   
                }
		     
		    }); 
			 //设置分页控件 
		    var p = $('#dg').datagrid('getPager'); 
		    $(p).pagination({ 
		        pageSize: 10,//每页显示的记录条数，默认为10 
		        pageList: [5,10,15,20],//可以设置每页记录条数的列表 
		        beforePageText: '第',//页数文本框前显示的汉字 
		        afterPageText: '页    共 {pages} 页', 
		        displayMsg: '当前显示 {from} - {to} 条记录   共 {total} 条记录', 
		        onBeforeRefresh:function(){
		            $(this).pagination('loading');
		            $(this).pagination('loaded');
		        } 
		    }); 

			
		}
		function clearForm(){
			$('#ff').form('clear');
		}
	
		//页面加载  
		$(document).ready(function(){ 
					var hy="seven";
					getDayType(hy);
		            loadGrid();  
		});  
		  
		
		function getDayType(date) {
			var input6=getnowtime();
			if(date=="thirty"){
			//判断点击三十天
			timeType="1";
			var d=new Date();
		    var n=new Date(d.getTime()-86400000*30);
		    var input5=n.getFullYear()+"-"+padleft0(n.getMonth()+1)+"-"+padleft0(n.getDate());
			}else if(date=="seven"){
			//判断点击七天
			timeType="2";
			var d=new Date();
		    var n=new Date(d.getTime()-86400000*7);
		    var input5=n.getFullYear()+"-"+padleft0(n.getMonth()+1)+"-"+padleft0(n.getDate());
			}else if(date=="both"){
			//判断点击二天
			timeType="3";
			var d=new Date();
		    var n=new Date(d.getTime()-86400000*2);
		    var input5=n.getFullYear()+"-"+padleft0(n.getMonth()+1)+"-"+padleft0(n.getDate());
			}else{
			//判断点击一天
			timeType="3";
			var d=new Date();
		    var n=new Date(d.getTime()-86400000*0);
		    var input5=n.getFullYear()+"-"+padleft0(n.getMonth()+1)+"-"+padleft0(n.getDate());
			}
			
			$("#_easyui_textbox_input7").val(input5);
			$("#_easyui_textbox_input8").val(input6);
			}
		function getnowtime() {
            var nowtime = new Date();
            var year = nowtime.getFullYear();
            var month = padleft0(nowtime.getMonth() + 1);
            var day = padleft0(nowtime.getDate());
            var hour = padleft0(nowtime.getHours());
            var minute = padleft0(nowtime.getMinutes());
            var second = padleft0(nowtime.getSeconds());
            var millisecond = nowtime.getMilliseconds(); millisecond = millisecond.toString().length == 1 ? "00" + millisecond : millisecond.toString().length == 2 ? "0" + millisecond : millisecond;
            return year+"-"+month + "-" + day;
        }
        //补齐两位数
        function padleft0(obj) {
            return obj.toString().replace(/^[0-9]{1}$/, "0" + obj);
        }
	</script>
</html>