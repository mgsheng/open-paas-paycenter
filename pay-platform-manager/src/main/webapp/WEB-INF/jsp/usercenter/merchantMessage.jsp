<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/themes/default/easyui.css">
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/themes/icon.css">
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/dataList.css">
	<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.min.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.easyui.min.js"></script>
</head>
<body>
	<div class="top" style="width: 100%;height: 300px">
	<div style="margin:20px 0;"></div>
	<div class="easyui-panel" title="查询条件" style="width:100%;max-width:1000px;padding:30px 60px;">
		<form id="ff" method="post">
		<table>
		<tr>
			<td style="text-align: right;">商户订单号：</td>
			<td>
				<input class="easyui-textbox" name="merchantOrderId" id="merchantOrderId" style="width:100%;height:32px">
			</td>
			<td style="text-align: right;">交易状态：</td>
			<td>
				<select class="easyui-combobox" name="payStatus" style="width:100%">
					<option value="">全部</option>
					<option value="0">处理中</option>
					<option value="1">成功</option>
					<option value="2">失败</option>
				</select>
			</td>
		</tr>
		
		<tr>
			<td style="text-align: right;">第三方订单号：</td>
			<td><input id="payOrderId" name="payOrderId" class="easyui-textbox" style="width:100%;height:32px"></td>
			<td style="text-align: right;">支付方式：</td>
			<td>
				<select class="easyui-combobox" id="channelId" name="channelId" style="width:100%">
					<option value="">全部</option>
					<option value="10001">支付宝</option>
					<option value="10002">微信</option>
					<option value="10003">银联</option>
					<option value="10005">易宝</option>
					<option value="10006">易宝pos</option>
					<option value="10004">TCL汇银通</option>
				</select>
			</td>
		</tr>
		<tr>
			<td style="text-align: right;">业务类型：</td>
			<td>
				<select class="easyui-combobox" name="appId" style="width:100%">
						<option value="" selected="selected">全部</option>
						<option value="1">OES学历</option>
						<option value="10026" >mooc2u</option>
				</select>
			</td>
			<td style="text-align: right;">发卡行：</td>
			<!-- <td><input id="openingBank" name="openingBank" class="easyui-textbox" style="width:100%;height:32px"></td> -->
			<td>
				<select class="easyui-combobox" name="paymentId" style="width:100%">
					<option value="" selected="selected">全部</option>
						<option value="10012">支付宝-即时到账</option>
						<option value="10013">微信-扫码支付</option>
						<option value="10014">银联</option>
						<option value="10001">招商银行</option>
						<option value="10002">工商银行</option>
						<option value="10003">建设银行</option>
						<option value="10004">农业银行</option>
						<option value="10005">中国银行</option>
						<option value="10006">交通银行</option>
						<option value="10007">中国邮政银行</option>
						<option value="10008">广发银行</option>
						<option value="10009">浦发银行</option>
						<option value="10010">中国光大银行</option>
						<option value="10011">中国平安银行</option>
				</select>
			
			</td>
		</tr>
		<tr>
			<td style="text-align: right;">缴费来源：</td>
			<td>
				<select class="easyui-combobox" name="source" style="width:100%">
					<option value="">全部</option>
					<option value="1">pc端</option>
					<option value="2">移动端</option>
				</select>
			</td>
			
		</tr>
		<tr>
			<td style="text-align: right;">交易时间：</td>
			<td style="text-align: center;">
				<!--  --><label><input type="radio" name="dealDate" checked="checked" value="-2"  />全部</label>
				<label><input name="dealDate" type="radio" value="0" />今天</label>
				<label><input name="dealDate" type="radio" value="1" />昨天</label>
				<label><input name="dealDate" type="radio" value="7" />7天</label>
				<label><input name="dealDate" type="radio" value="30" />30天</label>
				<label><input name="dealDate" type="radio" value="-1" />自定义</label>
			</td>
				<td style="text-align: right;"><input id="startDate" name="startDate" class="easyui-datebox" data-options="sharedCalendar:'#cc'">—到—</td>
				<td><input id="endDate" name="endDate" class="easyui-datebox" data-options="sharedCalendar:'#cc'"></td>
		</tr>
		</table>
		</form>
		<div style="text-align:center;padding:5px 0">
			<a href="javascript:void(0)" class="easyui-linkbutton" onclick="submitForm()" style="width:80px">提交</a>
			<a href="javascript:void(0)" class="easyui-linkbutton" onclick="clearForm()" style="width:80px">清空</a>
			<a href="javascript:void(0)" class="easyui-linkbutton" onclick="downloadSubmit()" style="width:80px">下载</a>
		</div>
	</div>
	
	</div>
	  <div class="botton" style="width: 100%;height: 300px">
		<table  id="dg"  class="easyui-datagrid" title="查询结果"   style="width:1000px;height:250px"
			data-options="singleSelect:true,method:'get'">
		<thead>
			<tr>
				<th data-options="field:'foundDate',width:150">下单时间</th>
				<th data-options="field:'businessDate',width:150">交易时间</th>
				<th data-options="field:'merchantOrderId',width:150">商户订单号</th>
				<th data-options="field:'payOrderId',width:150">第三方订单号</th>
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
			var merchantOrderId = $("input[name='merchantOrderId']").val();//商户订单号
			var payOrderId = $("input[name='payOrderId']").val();//第三方订单号
			var channelId = $("input[name='channelId']").val();//支付方式
			var appId = $("input[name='appId']").val();//业务类型
			var paymentId = $("input[name='paymentId']").val();//发卡行
			var source = $("input[name='source']").val();//缴费来源
			var payStatus = $("input[name='payStatus']").val();//缴费状态
			var dealDate = $("input[name='dealDate']").val();//交易时间
			var startDate = $("input[name='startDate']").val();//开始时间
			var endDate = $("input[name='endDate']").val();//结束时间
			
			$('#dg').datagrid({
				collapsible:true,
				rownumbers:true,
				pagination:true,
		        url: "${pageContext.request.contextPath}/manage/queryMerchant?merchantOrderId="+merchantOrderId+"&payOrderId="+payOrderId+"&channelId="+channelId+"&appId="+appId+"&paymentId="+paymentId+"&source="+source+"&payStatus="+payStatus+"&dealDate="+dealDate+"&startDate="+startDate+"&endDate="+endDate,  
		        //pagination: true,显示分页工具栏
		        
		     
		    }); 
			 //设置分页控件 
		    var p = $('#dg').datagrid('getPager'); 
		    $(p).pagination({ 
		        pageSize: 10,//每页显示的记录条数，默认为10 
		        pageList: [5,10,15],//可以设置每页记录条数的列表 
		        beforePageText: '第',//页数文本框前显示的汉字 
		        afterPageText: '页    共 {pages} 页', 
		        displayMsg: '当前显示 {from} - {to} 条记录   共 {total} 条记录', 
		        /*onBeforeRefresh:function(){
		            $(this).pagination('loading');
		            alert('before refresh');
		            $(this).pagination('loaded');
		        }*/ 
		    }); 
//			$.ajax({
//		       	url:"${pageContext.request.contextPath}/manage/queryMerchant",
//		       	data:$('#ff').serialize(),
//		       	type:'post',
//		       	success: function(data) {
		       	 
//		       	}
//		    });
			$('#ff').form('submit');
			
		}
		function clearForm(){
			$('#ff').form('clear');
		}
		function downloadSubmit(){
			
		//	var action ="${pageContext.request.contextPath}/manage/skipPages1";

		//	document.all.form.action = action;

		//	document.all.form.submit();
			
			document.getElementById("ff").action="${pageContext.request.contextPath}/manage/downloadSubmit";
		    document.getElementById("ff").submit();
			
		}
		
		
		//页面加载  
		$(document).ready(function(){  
		            loadGrid();  
		});  
		  
		
		  
		
	</script>
</html>