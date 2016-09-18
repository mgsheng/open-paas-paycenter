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
	<div
		style="border:0px solid;border-radius:8px;margin-bottom:10px;height: 150px; width: 1000px">
		<div
			style="border:0px solid;border-color:green;height: 30px;display:inline-block; width: 100%px;">
			<div>
				<span> <select class="easyui-combobox" name="channelId"
					id="channelId" label="支付方式" style="width:200px"
					data-options="editable:false">
						<option value="" selected="selected">全部</option>
						<option value="10001">支付宝</option>
						<option value="10002">微信</option>
						<option value="10005">直连银行</option>
						<option value="10003">银联</option>
				</select> </span> <span style="margin-left: 180px"> <select
					class="easyui-combobox" id="payClient" name="payClient"
					label="缴费来源" style="width:230px;" data-options="editable:false">
						<option value="" selected="selected">全部</option>
						<option value="pc">PC端</option>
						<option value="mobile">移动端</option>
				</select> </span> <span style="margin-left: 170px"> <select
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
					data-options="editable:false">
						<option value="" selected="selected">全部</option>
						<option value="1">OES学历</option>
						<option value="10026">mooc2u</option>
				</select> </span>
				<div style="margin-left: 180px; display: inline-block;">
					<a href="#" class="easyui-linkbutton" data-options="plain:true"
						onclick="getDayType('seven')">7天</a> <a href="#"
						class="easyui-linkbutton" data-options="plain:true"
						onclick="getDayType('thirty')">30天</a>

					<div style="display:inline-block;margin-left: 10px">
						&nbsp;累计 &nbsp; <input class="easyui-datebox"
							data-options="onSelect:onStartSelect" style="width:150px; "
							id="aa">
					</div>
					&nbsp;至&nbsp;
					<div style="display:inline-block;">
						<input class="easyui-datebox" data-options="onSelect:onEndSelect"
							style="width:150px; " id="bb">
					</div>
					<div style="display:inline-block; margin-left: 65px">
						<a href="#" class="easyui-linkbutton" onclick="queryData()"
							style="width: 60px">查询</a>
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

	<div
		style="border:1px solid;margin-top: 25px;border-radius:8px;margin-bottom: 10px; border-color:grey;background-color:#DDDDDD;height: 30px; width: 988px;padding-top: 10px;padding-left: 10px ;">趋势</div>
	<div class="easyui-tabs" style="width:1000px;height:445px">
		<div title="成交金额" style="padding:10px">
			<div id="payAmount" style=""></div>
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
			var n = new Date(d.getTime() - 86400000 * 30);
			var input5 = n.getFullYear() + "-" + padleft0(n.getMonth() + 1)
					+ "-" + padleft0(n.getDate());
		} else {
			//判断点击七天
			timeType = "2";
			var d = new Date();
			var n = new Date(d.getTime() - 86400000 * 7);
			var input5 = n.getFullYear() + "-" + padleft0(n.getMonth() + 1)
					+ "-" + padleft0(n.getDate());
		}
		startTime = input5;
		endTime = input6;
		$("#_easyui_textbox_input5").val(input5);
		$("#_easyui_textbox_input6").val(input6);
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
		//去除Y轴的单位k、m
		Highcharts.setOptions({
			lang : {
				numericSymbols : [],
				loading : '数据载入中...'
			}
		});
		$
				.post(
						"${pageContext.request.contextPath}/user/statistics/chart",
						{
							paymentId : $("input[name='paymentId']").val(),
							appId : $("input[name='appId']").val(),
							payClient : $("input[name='payClient']").val(),
							channelId : $("input[name='channelId']").val(),
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
										renderTo : 'payAmount',
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

								$('#payAmount').highcharts(options); //创建图表动画
								payAmountchart = $('#payAmount').highcharts(); //获取图表对象

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
</script>
</html>