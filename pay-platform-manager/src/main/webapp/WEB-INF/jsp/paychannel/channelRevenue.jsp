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
						<select class="easyui-combobox" data-options="editable:false,prompt:'请选择支付渠道'" id="queryChannelId" 
								name="queryChannelId"  style="width:180px;height:25px;padding:5px;">
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
						<input id="startDate" name="startDate" class="easyui-datebox" data-options="sharedCalendar:'#cc'" editable="false">~
						<input id="endDate" name="endDate" class="easyui-datebox" data-options="sharedCalendar:'#cc'" editable="false">
					</td>
				</tr>
				<tr>
					<td style="text-align: center;" colspan="6">
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
			    <th data-options="field:'channelId',width:100,align:'center'">支付渠道</th>
				<th data-options="field:'orderAmount',width:80,align:'center'">营收金额</th>
				<th data-options="field:'payCharge',width:80">手续费</th>
				<th data-options="field:'dismension',width:80,align:'center'">统计维度</th>
				<th data-options="field:'date',width:200">日期</th>
			</tr>
		</thead>
	</table>
	
	<div id="cc" class="easyui-calendar"></div>
</body>
<script>
		//页面预加载
		$(function(){
			getDayType("day");
			$("#queryDimension") .combobox({
            	onChange: function (n, o) {
            		alert(n);
            		getDayType(n);
	            }
        	});
			loadSelect();
			/* var operator=$("#realName",window.parent.document).text();
			$("#addOperator").val(operator);
			findOrderOffline();
			 */
		});
		
		function getDayType(date) {
			var input14=getnowtime();
			var input15=getnowtime();
			if(date=="day"){
				var d=new Date();
			    var n=new Date(d.getTime()-86400000*1);
			    input14=n.getFullYear()+"-"+padleft0(n.getMonth()+1)+"-"+padleft0(n.getDate());
			}else if(date=="week"){
				var d=new Date();
			    var n=new Date(d.getTime()-86400000*7);
			    input14=n.getFullYear()+"-"+padleft0(n.getMonth()+1)+"-"+padleft0(n.getDate());
			}else if(date=="month"){
				var d=new Date();
			    var n=new Date(d.getTime()-86400000*30);
			    input14=n.getFullYear()+"-"+padleft0(n.getMonth()+1)+"-"+padleft0(n.getDate());
			}else if(date=="year"){
				var d=new Date();
			    var n=new Date(d.getTime()-86400000*365);
			    input14=n.getFullYear()+"-"+padleft0(n.getMonth()+1)+"-"+padleft0(n.getDate());
			}else{//custom自定义
				input14="";
				input15="";
			}		
			$("#_easyui_textbox_input3").val(input14);
			$("#_easyui_textbox_input4").val(input15);
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
		
        //加载select
        function loadSelect(){
        	//加载所有支付方式名称，并且选中支付名称后触发根据该名称查询对应渠道编码的事件
            $('#queryChannelId').combobox({
				/* url:'${pageContext.request.contextPath}/paychannel/findPayNames', */
				url:'${pageContext.request.contextPath}/manage/findSourceType?flag=offline',
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

		function findOrderOffline(){
		 	var url="${pageContext.request.contextPath}/manage/getMerchantOrderOffline";
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
		}
		
		function submitForm(){
			var queryOrderId = $('#queryOrderId').textbox('getValue');
			var queryMerchantOrderId = $('#queryMerchantOrderId').textbox('getValue');
			var querySourceUserName = $('#querySourceUserName').textbox('getValue');
			var queryMerchantName = $('#queryMerchantName').combobox('getValue');
			var queryAppId = $('#queryAppId').combobox('getValue');
			var queryChannelId = $('#queryChannelId').combobox('getValue');
			var queryOperator = $('#queryOperator').val();
			var startDate = $('#startDate').datebox('getValue');
			var endDate = $('#endDate').datebox('getValue');
			
			if(queryOrderId==""&&queryMerchantOrderId==""&&querySourceUserName==""&&queryMerchantName==""&&(queryAppId=="" || queryAppId=='0')&&queryChannelId==""&&queryOperator==""&&startDate==""&&endDate==""){
				//$.messager.alert("提示","请输入查询条件!");
				findOrderOffline();
			}else if(!startDate==""&&!endDate==""){
				if(startDate>endDate){
					 $.messager.alert("提示","开始时间大于结束时间!");
				}else{
					var url="${pageContext.request.contextPath}/manage/getMerchantOrderOffline?orderId="+queryOrderId+"&merchantOrderId="+queryMerchantOrderId+"&sourceUserName="+querySourceUserName+"&merchantName="+queryMerchantName+"&appId="+queryAppId+"&channelId="+queryChannelId+"&operator="+queryOperator+"&startDate="+startDate+"&endDate="+endDate;
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
				}
			}else{
				var url="${pageContext.request.contextPath}/manage/getMerchantOrderOffline?orderId="+queryOrderId+"&merchantOrderId="+queryMerchantOrderId+"&sourceUserName="+querySourceUserName+"&merchantName="+queryMerchantName+"&appId="+queryAppId+"&channelId="+queryChannelId+"&operator="+queryOperator+"&startDate="+startDate+"&endDate="+endDate;
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
			}
		}
		
		function downloadSubmit(){			
			var queryOrderId = $('#queryOrderId').textbox('getValue');
			var queryMerchantOrderId = $('#queryMerchantOrderId').textbox('getValue');
			var querySourceUserName = $('#querySourceUserName').textbox('getValue');
			var queryMerchantName = $('#queryMerchantName').combobox('getValue');
			var queryAppId = $('#queryAppId').combobox('getValue');
			var queryChannelId = $('#queryChannelId').combobox('getValue');
			var queryOperator = $('#queryOperator').val();
			var startDate = $('#startDate').datebox('getValue');
			var endDate = $('#endDate').datebox('getValue');
		
		 	var url="${pageContext.request.contextPath}/manage/offlineDownloadSubmit?orderId="+queryOrderId+"&merchantOrderId="+queryMerchantOrderId+"&sourceUserName="+querySourceUserName+"&merchantName="+queryMerchantName+"&appId="+queryAppId+"&channelId="+queryChannelId+"&operator="+queryOperator+"&startDate="+startDate+"&endDate="+endDate;
			
			document.getElementById("ff").action=url;
		    document.getElementById("ff").submit();
		}
	</script>
</html>