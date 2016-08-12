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
			<td style="text-align: right;">下单时间：</td>
			<td><input id="merchantOrderDate" name="merchantOrderDate" class="easyui-datebox" data-options="sharedCalendar:'#cc'" value=""></td>
			<td style="text-align: right;">商户订单号：</td>
			
			<td>
				<input class="easyui-textbox" name="merchantOrderId" id="merchantOrderId" style="width:100%;height:32px">
			</td>
		</tr>
		
		<tr>
			<td style="text-align: right;">第三方订单号：</td>
			<td><input id="payOrderId" name="payOrderId" class="easyui-textbox" style="width:100%;height:32px"></td>
			<td style="text-align: right;">支付方式：</td>
			<td>
				<select class="easyui-combobox" id="channelId" name="channelId" style="width:100%">
					<option value=""></option>
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
			<td><input id="businessType" name="businessType" class="easyui-textbox" style="width:100%;height:32px"></td>
			<td style="text-align: right;">发卡行：</td>
			<td><input id="openingBank" name="openingBank" class="easyui-textbox" style="width:100%;height:32px"></td>
		</tr>
		<tr>
			<td style="text-align: right;">缴费来源：</td>
			<td>
				<select class="easyui-combobox" name="source" style="width:100%">
					<option value=""></option>
					<option value="1">pc端</option>
					<option value="2">移动端</option>
				</select>
			</td>
			<td style="text-align: right;">交易状态：</td>
			<td>
				<select class="easyui-combobox" name="payStatus" style="width:100%">
					<option value=""></option>
					<option value="0">处理中</option>
					<option value="1">第三方成功</option>
					<option value="2">第三方失败</option>
				</select>
			
			</td>
		</tr>
		<tr>
			<td style="text-align: right;">交易时间：</td>
			<td style="text-align: center;">
				<label><input name="createDate" type="radio" value="0" />今天</label>
				<label><input name="createDate" type="radio" value="1" />昨天</label>
				<label><input name="createDate" type="radio" value="7" />7天</label>
				<label><input name="createDate" type="radio" value="30" />30天</label>
				<label><input name="createDate" type="radio" value="-1" />自定义</label>
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
	<!--  <div class="botton" style="width: 100%;height: 90px">
		<table class="easyui-datagrid" title="查询结果" style="width:1000px;height:250px"
			data-options="singleSelect:true,collapsible:true,url:'datagrid_data1.json',method:'get'">
		<thead>
			<tr>
				<th data-options="field:'itemid',width:80">下单时间</th>
				<th data-options="field:'productid',width:100">交易时间</th>
				<th data-options="field:'listprice',width:80,align:'right'">商户订单号</th>
				<th data-options="field:'unitcost',width:80,align:'right'">第三方订单号</th>
				<th data-options="field:'attr1',width:250">支付方式</th>
				<th data-options="field:'status',width:60,align:'center'">业务类型</th>
				<th data-options="field:'itemid',width:80">发卡行</th>
				<th data-options="field:'productid',width:100">卡类型</th>
				<th data-options="field:'listprice',width:80,align:'right'">缴费来源</th>
				<th data-options="field:'unitcost',width:80,align:'right'">交易状态</th>
				<th data-options="field:'attr1',width:250">缴费金额</th>
				<th data-options="field:'status',width:60,align:'center'">实收金额</th>
				<th data-options="field:'status',width:60,align:'center'">手续费</th>
			</tr>
		</thead>
	</table>
	</div>
	-->
<div id="cc" class="easyui-calendar"></div>
<div style="width: 100%;height: 500px;">
	<table id="cxdm" style="border: 1px solid; border-color: red">234567</table> 
</div>
</body>
<script>
		function submitForm(){
			
			$.ajax({
		       	url:"${pageContext.request.contextPath}/manage/queryMerchant",
		       	data:$('#ff').serialize(),
		       	type:'post',
		       	success: function(data) {
		       	 
		       	}
		    });
			$('#ff').form('submit');
			
		}
		function clearForm(){
			$('#ff').form('clear');
		}
		function downloadSubmit(){
			$.ajax({
		       	url:"${pageContext.request.contextPath}/manage/downloadSubmit",
		       	data:$('#ff').serialize(),
		       	type:'post',
		       	success: function(data) {
		       	 
		       	}
		    });
			$('#ff').form('submit');
			
		}
		
		
		//页面加载  
		$(document).ready(function(){  
		            loadGrid();  
		});  
		  
		//加载表格datagrid  
		function loadGrid()  
		{  
		    //加载数据  
		    alert("ss");
		    $('#cxdm').datagrid({  
		                width: 'auto',  
		                height:300,               
		                striped: true,  
		                singleSelect : true,  
		                url:'${pageContext.request.contextPath}/manage/queryMerchant', 
		                //queryParams:{},  
		                loadMsg:'数据加载中请稍后……',  
		                pagination: true,  
		                rownumbers: true,     
		                columns:[[  
		                    {field:'adviceid',title: '来文号111111111111',align: 'center',width: getWidth(0.2)},  
		                    {field:'consulter',title: '案由',align: 'center',width: getWidth(0.45),  
		                        //添加超级链，并将来文号作为参数传入  
		                        formatter:function(val,rec){  
		                            //alert(rec.adviceid);  
		                            return "<a href='jsp/proposal/psconsultview.jsp?id="+rec.adviceid+"'>"+val+"</a>";  
		                       }  
		                    },  
		                    {field:'content',title: '状态',align: 'center',width: getWidth(0.2)},  
		                    {field:'replynumber',title: '回复数',align: 'center',width: getWidth(0.05)}                                                          
		                ]]  
		            });  
		}  
		  
		//为loadGrid()添加参数  
		        var queryParams = $('#cxdm').datagrid('options').queryParams;  
		        queryParams.who = who.value;  
		        queryParams.type = type.value;  
		        queryParams.searchtype = searchtype.value;  
		        queryParams.keyword = keyword.value;  
		        //重新加载datagrid的数据  
		        $("#cxdm").datagrid('reload'); 
	</script>
</html>