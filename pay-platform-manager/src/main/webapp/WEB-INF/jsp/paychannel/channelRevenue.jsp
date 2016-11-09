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
					<td style="text-align: right;">支付渠道：</td>
					<td>
						<select class="easyui-combobox" data-options="editable:false,prompt:'请选择支付渠道',multiple:false" id="queryChannelId" 
								name="queryChannelId"  style="width:180px;height:25px;padding:5px;">
						</select>
					</td>
					<td style="text-align: right;">所属应用：</td>
					<td>
						<select class="easyui-combobox" data-options="editable:false,prompt:'请选择所属应用',multiple:false" id="queryMerchantId" 
								name="queryMerchantId"  style="width:180px;height:25px;padding:5px;">
							<option value="">全部</option>
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
	
	<table id="dg" class="easyui-datagrid" title="渠道营收报表" style="height:400px;width:100%;max-width:100%;" 
			data-options="rownumbers:true,singleSelect:true,striped:true,fitColumns:false,method:'get'">
		<thead>
			<tr>
			    <th data-options="field:'channelName',width:100,align:'center'">支付渠道</th>
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
			//预加载,默认维度天,日期昨天
			findChannelRevenue();
		});
		
        //加载select
        function loadSelect(){
        	//加载所有支付方式名称，并且选中支付名称后触发根据该名称查询对应渠道编码的事件
            $('#queryChannelId').combobox({
				url:'${pageContext.request.contextPath}/manage/findSourceType',
				valueField:'id',
				textField:'text'
			});
			//加载所属商户应用名称
			$('#queryMerchantId').combobox({
				url:'${pageContext.request.contextPath}/paychannel/findMerchantNames?flag=all',
				valueField:'id',
				textField:'text'
			});
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

		function findChannelRevenue(){
			var dimension="day";
		 	var url="${pageContext.request.contextPath}/paychannel/getChannelRevenue?dimension="+dimension;
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
		}
		
		function submitForm(){
			var queryChannelId = $('#queryChannelId').combobox('getValue');
			var queryMerchantId = $('#queryMerchantId').combobox('getValue');
			var queryDimension = $('#queryDimension').combobox('getValue');
			var startDate = $('#startDate').datebox('getValue');
			var endDate = $('#endDate').datebox('getValue');
			if(queryChannelId==""&&queryMerchantId==""&&queryDimension=="day"&&startDate==""&&endDate==""){
				findChannelRevenue();
			}else if(!startDate==""&&!endDate==""){
				if(startDate>endDate){
					 $.messager.alert("提示","开始时间大于结束时间!");
				}else{
					var url="${pageContext.request.contextPath}/paychannel/getChannelRevenue?&channelId="+queryChannelId+"&merchantId="+queryMerchantId+"&dimension="+queryDimension+"&startDate="+startDate+"&endDate="+endDate;
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
				var url="${pageContext.request.contextPath}/paychannel/getChannelRevenue?&channelId="+queryChannelId+"&dimension="+queryDimension+"&startDate="+startDate+"&endDate="+endDate;
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
			var queryChannelId = $('#queryChannelId').combobox('getValue');
			var queryDimension = $('#queryDimension').combobox('getValue');
			var startDate = $('#startDate').datebox('getValue');
			var endDate = $('#endDate').datebox('getValue');
			
		 	var url="${pageContext.request.contextPath}/paychannel/revenueDownloadSubmit?&channelId="+queryChannelId+"&dimension="+queryDimension+"&startDate="+startDate+"&endDate="+endDate;
			
			document.getElementById("ff").action=url;
		    document.getElementById("ff").submit();
		}
	</script>
</html>