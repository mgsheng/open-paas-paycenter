<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/themes/default/easyui.css">
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/themes/icon.css">
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/dataList.css">
	<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.min.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/js/locale/easyui-lang-zh_CN.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/js/common.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/js/highcharts/highcharts.js"></script>
	<style type="text/css">
		.txt01{
			 background-color: #eee;
		}
	</style>
</head>
<body style="height:100%">
	<div class="easyui-panel" title="查询条件" style="width:100%; border-bottom:none;padding:1%;">
		<form id="ff" method="post">
			<table style="width:100%;">
				<tr>
					<td style="text-align: right;">支付银行：</td>
					<td>
						<select class="easyui-combobox" data-options="editable:false,prompt:'请选择支付银行',multiple:false" id="queryPaymentId" 
								name="queryPaymentId"  style="width:180px;height:25px;padding:5px;">
						</select>
					</td>
					<td style="text-align: right;">所属应用：</td>
					<td>
						<select class="easyui-combobox" data-options="editable:false,valueField:'id',textField:'text',multiple:false" id="queryMerchantId" 
								name="queryMerchantId"  style="width:180px;height:25px;padding:5px;">
						</select>
					</td>
					<td style="text-align: right;">统计维度：</td>
					<td>
						<select class="easyui-combobox" data-options="editable:false" id="queryDimension" 
								name="queryDimension"  style="width:80px;height:25px;padding:5px;">
								<option value="day" selected="selected">天</option>
								<option value="week">自然周</option>
								<option value="month">月</option>
								<option value="year">年</option>
								<option value="custom">自定义</option>
						</select>
					</td>
					<td style="text-align: right;">统计日期：</td>
					<td>
						<input id="startDate" name="startDate" class="easyui-datebox" data-options="sharedCalendar:'#cc'" editable="false">
						<div id="end" style="display:inline;">~<input id="endDate" name="endDate" class="easyui-datebox" data-options="sharedCalendar:'#cc'" editable="false"></div>
					</td>
				</tr>
				<tr>
					<td style="text-align: center;" colspan="8">
						<a href="javascript:void(0)" class="easyui-linkbutton" onclick="submitForm()" style="width:80px">查询</a>
						&nbsp;&nbsp;
						<a href="javascript:void(0)" class="easyui-linkbutton" onclick="clearForm()" style="width:80px">清空</a>
						&nbsp;&nbsp;
						<a href="javascript:void(0)" class="easyui-linkbutton"  onclick="downloadSubmit()" style="width:80px">下载</a>
					</td>
				</tr>
			</table>
		</form>
	</div>
	
	<table id="dg" class="easyui-datagrid" title="银行缴费报表" style="height:400px;width:100%;max-width:100%;" 
			data-options="rownumbers:true,singleSelect:true,striped:true,fitColumns:false,method:'get'">
		<thead>
			<tr>
			    <th data-options="field:'paymentName',width:100,align:'center'">支付银行</th>
			    <th data-options="field:'merchantName',width:100,align:'center'">所属应用</th>
				<th data-options="field:'countOrderAmount',width:80,align:'center'">交易金额</th>
				<th data-options="field:'countPayAmount',width:80,align:'center'">营收金额</th>
				<th data-options="field:'countPayCharge',width:80">手续费</th>
				<th data-options="field:'dismension',width:80,align:'center'">统计维度</th>
				<th data-options="field:'foundDate',width:200">日期</th>
			</tr>
		</thead>
	</table>
	
	<div id="cc" class="easyui-calendar"></div>
</body>
<script>
		//页面预加载
		$(function(){
        	//加载查询条件中的支付渠道
			loadSelect();
			//加载所属商户应用名称
			$.post("${pageContext.request.contextPath}/paychannel/findMerchantNames",
			            function(data){
							$('#queryMerchantId').combobox('loadData',data);
							if (data.length == 1) {
								$('#queryMerchantId').combobox('select',data[0].id);
							}
							//预加载,默认维度天,日期昨天
							findPayment();
		            	}
	            );
			
		});
		
        //加载select
        function loadSelect(){
        	//加载所有支付银行名称，并且选中支付名称后触发根据该名称查询对应渠道编码的事件
            $('#queryPaymentId').combobox({
				url:'${pageContext.request.contextPath}/payment/findPayment?type=1',
				valueField:'id',
				textField:'text'
			});
			/* //加载所有商户应用名称
            $('#queryMerchantId').combobox({
				url:'${pageContext.request.contextPath}/paychannel/findMerchantNames?flag=all',
				valueField:'id',
				textField:'text'
			}); */
        }

		//弹出信息窗口 title:标题 msgString:提示信息 msgType:信息类型 [error,info,question,warning]
		function msgShow(title, msgString, msgType) {
			$.messager.alert(title, msgString, msgType);
		}

		//重新加載
		function reload(url){
		$('#dg').datagrid('reload',{
            url: url, method: "post"
          }); 
		}

		function findPayment(){
			var dimension="day";
			var paymentType=1;
			var merchantId = $('#queryMerchantId').combobox('getValue');
		 	var url="${pageContext.request.contextPath}/payment/getPayment?dimension="+dimension+"&paymentType="+paymentType+"&merchantId="+merchantId;
        	$('#dg').datagrid({
				collapsible:true,
				rownumbers:true,
				pagination:true,
		        url: url,  
		        onLoadSuccess:function(data){
                    if (data.total<1){
                       $.messager.alert("提示","没有符合查询条件的数据!");
                  	}
                }
		    }); 
		    setPage();
		}
		
		function setPage(){
			//设置分页控件 
		    var p = $('#dg').datagrid('getPager'); 
		    $(p).pagination({ 
		        pageSize: 15,//每页显示的记录条数，默认为10 
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
			$('select#queryDimension').combobox('setValue','day');
			$.post("${pageContext.request.contextPath}/paychannel/findMerchantNames",
		            function(data){
						$('#queryMerchantId').combobox('loadData',data);
						if (data.length==1) {
							$('#queryMerchantId').combobox('select',data[0].id);
						}
	            	}
            );
		}
		
		function submitForm(){
			var queryPaymentId = $('#queryPaymentId').combobox('getValue');
			var queryMerchantId = $('#queryMerchantId').combobox('getValue');
			var queryDimension = $('#queryDimension').combobox('getValue');
			var startDate = $('#startDate').datebox('getValue');
			var endDate = $('#endDate').datebox('getValue');
			if(queryPaymentId==""&&queryDimension=="day"&&startDate==""&&endDate==""){
				findPayment();
			}else if(!startDate==""&&!endDate==""){
				if(startDate>endDate){
					 $.messager.alert("提示","开始时间大于结束时间!");
				}else{
					var url="${pageContext.request.contextPath}/payment/getPayment?&paymentId="+queryPaymentId+"&merchantId="+queryMerchantId+"&dimension="+queryDimension+"&startDate="+startDate+"&endDate="+endDate+"&paymentType=1";
		        	$('#dg').datagrid({
						collapsible:true,
						rownumbers:true,
						pagination:true,
				        url: url,  
				        onLoadSuccess:function(data){
		                    if (data.total<1){
		                       $.messager.alert("提示","没有符合查询条件的数据!");
		                  	}
		                  	if(queryDimension=="week" || queryDimension=="month" || queryDimension=="year"){
		                  		$(".datagrid-pager").hide();
		                  	}else{
		                  		$(".datagrid-pager").show();
		                  	}
		                }
				    }); 
 				}
			}else{
				var url="${pageContext.request.contextPath}/payment/getPayment?&paymentId="+queryPaymentId+"&dimension="+queryDimension+"&startDate="+startDate+"&endDate="+endDate+"&paymentType=1"+"&merchantId="+queryMerchantId;
	        	$('#dg').datagrid({
					collapsible:true,
					rownumbers:true,
					pagination:true,
			        url: url,  
			        onLoadSuccess:function(data){
	                    if (data.total<1){
	                       $.messager.alert("提示","没有符合查询条件的数据!");
	                  	}
	                  	if(queryDimension=="week" || queryDimension=="month" || queryDimension=="year"){
	                  		$(".datagrid-pager").hide();
	                  	}else{
	                  		$(".datagrid-pager").show();
	                  	}
	                }
			    }); 
			}
		}
		
		function downloadSubmit(){			
			var queryPaymentId = $('#queryPaymentId').combobox('getValue');
			var queryMerchantId = $('#queryMerchantId').combobox('getValue');
			var queryDimension = $('#queryDimension').combobox('getValue');
			var startDate = $('#startDate').datebox('getValue');
			var endDate = $('#endDate').datebox('getValue');
			
		 	var url="${pageContext.request.contextPath}/payment/paymentDownloadSubmit?&paymentId="+queryPaymentId+"&merchantId="+queryMerchantId+"&dimension="+queryDimension+"&startDate="+startDate+"&endDate="+endDate+"&paymentType=1";
			
			document.getElementById("ff").action=url;
		    document.getElementById("ff").submit();
		}
	</script>
</html>