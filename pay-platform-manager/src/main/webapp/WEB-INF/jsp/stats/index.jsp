<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<!DOCTYPE HTML>
<html>
<head>
<meta charset="UTF-8">
<title>经营分析</title>
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/css/themes/default/easyui.css">
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/css/themes/icon.css">
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/css/dataList.css">
<script type="text/javascript"
	src="${pageContext.request.contextPath}/js/jquery.min.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/js/jquery.easyui.min.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/js/highcharts/highcharts.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/js/highcharts/modules/exporting.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/js/locale/easyui-lang-zh_CN.js"></script>

<style type="text/css">
table {
	border: 1px solid;
	border-color: grey;
}
</style>
</head>
<body>
<div class="easyui-panel" title="查询条件" style="width:100%;max-width:100%;padding:20px 30px;">
<form id="ff" method="post">
	<div
		style="border:0px solid;border-radius:8px;margin-bottom:10px;height: 150px; width: 1000px">
		<div
			style="border:0px solid;border-color:green;height: 30px;display:inline-block; width: 100%px;">
			<div>
				<span> <select class="easyui-combobox" name="channelId"
					id="channelId" label="支付方式" style="width:200px"
					data-options="editable:false,prompt:'全部'">
						
				</select> </span> <span style="margin-left: 60px"> <select
					class="easyui-combobox" id="payClient" name="payClient"
					label="缴费来源" style="width:230px;" data-options="editable:false">
						<option value="" selected="selected">全部</option>
						<option value="pc">PC端</option>
						<option value="mobile">移动端</option>
				</select> </span> <span style="margin-left: 190px"> <select
					class="easyui-combobox" id="paymentId" name="paymentId"
					label="缴费银行" style="width:200px" data-options="editable:false">
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
				</select> </span>
			</div>
			<div style="margin-top: 5px">
				<span style=""> <select class="easyui-combobox" name="appId"
					id="appId" label="业务类型" style="width:200px"
					data-options="editable:false,valueField:'id',textField:'text',">
						
				</select> </span>
				
				<div style="margin-left: 60px; display: inline-block;">
					<a href="#" class="easyui-linkbutton" data-options="plain:true"	onclick="getDayType('seven')">7天</a> 
					<a href="#"	class="easyui-linkbutton" data-options="plain:true"	onclick="getDayType('thirty')">30天</a>

					<div style="display:inline-block;margin-left: 10px">
						&nbsp;累计 &nbsp; <input class="easyui-datebox"	data-options="onSelect:onStartSelect" style="width:150px; "	id="aa">
					</div>
					&nbsp;至&nbsp;
					<div style="display:inline-block;">
						<input class="easyui-datebox" data-options="onSelect:onEndSelect"  style="width:150px; " id="bb">
					</div>
				</div>
			</div>
			<div style="margin-top: 5px">
				<span style=""> <select class="easyui-combobox" name="sourceType"
					id="sourceType" label="支付渠道" style="width:200px"
					data-options="editable:false,prompt:'全部'">
						
				</select> </span>
				<div style="margin-left: 60px; display: inline-block;">
					
					<div style="display:inline-block; margin-left: 65px">
						<a href="#" class="easyui-linkbutton" onclick="queryData()"
							style="width: 60px">查询</a>
						<a href="#" class="easyui-linkbutton" onclick="exportSubmit()"
							style="width: 60px">导出</a>
					</div>
				</div>
			</div>
		</div>
		<div
			style="border:0px solid;height: 100px;display:inherit; margin-top: 10px;">
			<table style="width: 100%; height: 100%; text-align: center; ">
				<tr
					style="border: 1px solid;border-color:grey;background-color:#DDDDDD;">
					<td>成交金额（元）</td>
					<td>成交笔数</td>
					<td>缴费人数</td>
					<td>手续费</td>
				</tr>
				<tr
					style="border: 1px solid;border-color:grey;background-color:#F5F5F5;">
					<td><div id="totalPayAmount" style="">0.0</div>
					</td>
					<td><div id="totalPayCount" style="">0</div>
					</td>
					<td><div id="totalUserCount" style="">0</div>
					</td>
					<td><div id="totalPayCharge" style="">0.0</div>
					</td>
				</tr>
			</table>
		</div>
	</div>
	</form>
</div>
	<div class="easyui-datagrid" title="趋势" >
	</div>
		<div class="easyui-tabs" style="height:445px">
			<div title="成交金额" style="padding:10px">
				<div id="orderAmount" style=""></div>
			</div>
			<div title="成交笔数" style="width:1000px;">
				<div id="payCount" style=""></div>
			</div>
			<div title="缴费人数" style="width:1000px;">
				<div id="userCount" style=""></div>
			</div>
			<div title="手续费" style="">
				<div id="payCharge" style=""></div>
			</div>
		</div>
		
		
		<div class="botton" style="width: 100%;height: 300px">
			<table  id="dg"  class="easyui-datagrid" title="查询结果"    style="width:100%;max-width:100%;padding:20px 30px;"
				data-options="singleSelect:true,method:'get'">
			<thead>
				<tr>
					<th data-options="field:'orderAmount',width:150">成交金额</th>
					<th data-options="field:'merchantOrderId',width:150">成交笔数</th>
					<th data-options="field:'sourceUid',width:150">缴费人数</th>
					<th data-options="field:'payCharge',width:150">手续费</th>
					<th data-options="field:'foundDate',width:150">日期</th>
				</tr>
			</thead>
			
			</table>
		</div>
	

</body>
<script>
	var startTime = "";
	var endTime = "";
	var timeType = "";
	//获取开始时间
	function onStartSelect(date) {
		//alert(date);
		//var d = new Date(date);
		//console.log(d.toLocaleString());
		//alert(d.toLocaleString().replace("日","").replace(/[年月]/g,"-"));
		startTime = format(date, 'yyyy-MM-dd');
		timeType = "";
	}
	//获取结束时间
	function onEndSelect(date) {
		//alert(date);
		//var d = new Date(date);
		//console.log(d.toLocaleString());
		//alert(d.toLocaleString().replace("日","").replace(/[年月]/g,"-"));
		endTime = format(date, 'yyyy-MM-dd');
		timeType = "";
	}
	function getDayType(date) {
		var input6 = getnowtime();
		if (date == "thirty") {
			//判断点击三十天
			timeType = "1";
			var d = new Date();
			var n = new Date(d.getTime() - 86400000 * 29);
			var input5 = n.getFullYear() + "-" + padleft0(n.getMonth() + 1)
					+ "-" + padleft0(n.getDate());
		} else {
			//判断点击七天
			timeType = "2";
			var d = new Date();
			var n = new Date(d.getTime() - 86400000 * 6);
			var input5 = n.getFullYear() + "-" + padleft0(n.getMonth() + 1)
					+ "-" + padleft0(n.getDate());
		}
		startTime = input5;
		endTime = input6;
		$("#_easyui_textbox_input6").val(input5);
		$("#_easyui_textbox_input7").val(input6);
	}
	function getnowtime() {
		var nowtime = new Date();
		var year = nowtime.getFullYear();
		var month = padleft0(nowtime.getMonth() + 1);
		var day = padleft0(nowtime.getDate());
		var hour = padleft0(nowtime.getHours());
		var minute = padleft0(nowtime.getMinutes());
		var second = padleft0(nowtime.getSeconds());
		var millisecond = nowtime.getMilliseconds();
		millisecond = millisecond.toString().length == 1 ? "00" + millisecond
				: millisecond.toString().length == 2 ? "0" + millisecond
						: millisecond;
		return year + "-" + month + "-" + day;
	}
	//补齐两位数
	function padleft0(obj) {
		return obj.toString().replace(/^[0-9]{1}$/, "0" + obj);
	}
	function queryData() {
		submitForm();
		//去除Y轴的单位k、m
		Highcharts.setOptions({
			lang : {
				numericSymbols : [],
				loading : '数据载入中...'
			}
		});
	    startTime = $("#_easyui_textbox_input6").val();
		endTime = $("#_easyui_textbox_input7").val();
		$.post("${pageContext.request.contextPath}/user/statistics/chart",
						{
							paymentId : $("input[name='paymentId']").val(),
							appId : $("input[name='appId']").val(),
							payClient : $("input[name='payClient']").val(),
							channelId : $("input[name='channelId']").val(),
							sourceType : $("input[name='sourceType']").val(),
							startTime : startTime,
							endTime : endTime,
							timeType : timeType,
							resourceCode:"query"
						},
						function(data) {
							if (data.timeData != null) {
								var dataString = data.timeData;
								var payAmountchart;
								var options = {
									chart : {
										renderTo : 'orderAmount',
										width : 900,
										colors : [ '#3E8940', '#F9536E', ],
									},
									title : {
										text : '成交金额',
										x : -20
									//center
									},
									xAxis : {
										categories : dataString,
										labels : {
											rotation : -45,
											align : 'right',
											style : {
												fontSize : '13px',
												fontFamily : 'Verdana, sans-serif'
											}
										}
									},
									yAxis : {
										title : {
											text : '金额（元）'
										},
										plotLines : [ {
											value : 0,
											width : 1,
											color : '#808080'
										} ]
									},
									exporting : {
										buttons : {
											contextButton : {
												enabled : false
											}
										}
									},
									tooltip : {
										valueSuffix : '元'
									},
									legend : {
										layout : 'vertical',
										align : 'right',
										verticalAlign : 'middle',
										borderWidth : 0
									},
									series : data.payAmountListMap
								};

								$('#orderAmount').highcharts(options); //创建图表动画
								payAmountchart = $('#orderAmount').highcharts(); //获取图表对象

								$('#payCount')
										.highcharts(
												{

													chart : {
														renderTo : 'payCount',
														width : 900,
														colors : [ '#3E8940',
																'#F9536E', ],
													},
													title : {
														text : '成交笔数',
														x : -20
													//center
													},
													xAxis : {
														categories : dataString,
														labels : {
															rotation : -45,
															align : 'right',
															style : {
																fontSize : '13px',
																fontFamily : 'Verdana, sans-serif'
															}
														}
													},
													yAxis : {
														title : {
															text : '成交数(次)'
														},
														plotLines : [ {
															value : 0,
															width : 1,
															color : '#808080'
														} ]
													},
													exporting : {
														buttons : {
															contextButton : {
																enabled : false
															}
														}
													},
													tooltip : {
														valueSuffix : '次'
													},
													legend : {
														layout : 'vertical',
														align : 'right',
														verticalAlign : 'middle',
														borderWidth : 0
													},
													series : data.payCountListMap
												});

								$('#userCount')
										.highcharts(
												{
													chart : {
														renderTo : 'userCount',
														width : 900,
														colors : [ '#3E8940',
																'#F9536E', ],
													},
													title : {
														text : '缴费人数',
														x : -20
													//center
													},

													xAxis : {
														categories : dataString,
														labels : {
															rotation : -45,
															align : 'right',
															style : {
																fontSize : '13px',
																fontFamily : 'Verdana, sans-serif'
															}
														}
													},
													yAxis : {
														title : {
															text : '人数(人)'
														},
														plotLines : [ {
															value : 0,
															width : 1,
															color : '#808080'
														} ]
													},
													exporting : {
														buttons : {
															contextButton : {
																enabled : false
															}
														}
													},
													tooltip : {
														valueSuffix : '人'
													},
													legend : {
														layout : 'vertical',
														align : 'right',
														verticalAlign : 'middle',
														borderWidth : 0
													},
													series : data.userCountListMap
												});

								$('#payCharge')
										.highcharts(
												{
													chart : {
														renderTo : 'payCharge',
														width : 900,
														colors : [ '#3E8940',
																'#F9536E', ],
													},
													title : {
														text : '手续费',
														x : -20
													//center
													},
													xAxis : {
														categories : dataString,
														labels : {
															rotation : -45,
															align : 'right',
															style : {
																fontSize : '13px',
																fontFamily : 'Verdana, sans-serif'
															}
														}
													},
													yAxis : {
														title : {
															text : '金额(元)'
														},
														plotLines : [ {
															value : 0,
															width : 1,
															color : '#808080'
														} ]
													},
													exporting : {
														buttons : {
															contextButton : {
																enabled : false
															}
														}
													},
													tooltip : {
														valueSuffix : '元'
													},
													legend : {
														layout : 'vertical',
														align : 'right',
														verticalAlign : 'middle',
														borderWidth : 0
													},
													series : data.payChargeListMap

												});

							}
							$("#totalPayAmount").html(data.totalPayAmount);
							$("#totalPayCount").html(data.totalPayCount);
							$("#totalUserCount").html(data.totalUserCount);
							$("#totalPayCharge").html(data.totalPayCharge);
						});
	}
	var format = function(time, format) {
		var t = new Date(time);
		var tf = function(i) {
			return (i < 10 ? '0' : '') + i;
		};
		return format.replace(/yyyy|MM|dd|HH|mm|ss/g, function(a) {
			switch (a) {
			case 'yyyy':
				return tf(t.getFullYear());
				break;
			case 'MM':
				return tf(t.getMonth() + 1);
				break;
			case 'mm':
				return tf(t.getMinutes());
				break;
			case 'dd':
				return tf(t.getDate());
				break;
			case 'HH':
				return tf(t.getHours());
				break;
			case 'ss':
				return tf(t.getSeconds());
				break;
			}
		});
	};
	
	//页面加载  
	$(document).ready(function(){ 
				var hy="seven";
				getDayType(hy);
				initialise();
				paySourceType();
				payIrrigation();
	            
	});  
	
	
	function  initialise(){
		$.post("${pageContext.request.contextPath}/manage/findAllDepts",
	            function(data){
					$('#appId').combobox('loadData',data);
					if (data.length == 1) {
						$('#appId').combobox('select',data[0].id);
					}
				
            	}
        );
		
	}
	
	function  payIrrigation(){
		$('#channelId').combobox({
			url:'${pageContext.request.contextPath}/manage/findAllPayChannel',
			valueField:'id',
			textField:'text'
		});
	}
	function paySourceType(){
		$('#sourceType').combobox({
			url:'${pageContext.request.contextPath}/manage/findSourceType',
			valueField:'id',
			textField:'text'
		});
	}
	
	
	function submitForm(){
		var channelId = $("input[name='channelId']").val();//支付方式
		var payClient = $("input[name='payClient']").val();//缴费来源
		var paymentId = $("input[name='paymentId']").val();//缴费银行
		var appId = $("input[name='appId']").val();//业务类型
		var sourceType = $("input[name='sourceType']").val();//业务类型
		var startDate = $("#_easyui_textbox_input6").val();
		var endDate = $("#_easyui_textbox_input7").val();
		$('#dg').datagrid({
			collapsible:true,
			rownumbers:true,
			pagination:true,
	        url: "${pageContext.request.contextPath}/user/queryMerchant?channelId="+channelId+"&payClient="+payClient+"&paymentId="+paymentId+"&appId="+appId+"&startDate="+startDate+"&endDate="+endDate+"&sourceType="+sourceType,  
            onLoadSuccess:function(data){
                if (data.total<1){
                   $.messager.alert("提示","没有符合查询条件的数据!");
              }
               
            }
	     
	    }); 
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
	
	
	function exportSubmit(){
		
		var channelId = $("input[name='channelId']").val();//支付方式
		var payClient = $("input[name='payClient']").val();//缴费来源
		var paymentId = $("input[name='paymentId']").val();//缴费银行
		var appId = $("input[name='appId']").val();//业务类型
		var sourceType = $("input[name='sourceType']").val();
		var startDate = $("#_easyui_textbox_input6").val();
		var endDate = $("#_easyui_textbox_input7").val();
		
		document.getElementById("ff").action="${pageContext.request.contextPath}/user/exportSubmit?channelId="+channelId+"&payClient="+payClient+"&paymentId="+paymentId+"&appId="+appId+"&startDate="+startDate+"&endDate="+endDate+"&sourceType="+sourceType,
	    document.getElementById("ff").submit();
	}
</script>
</html>