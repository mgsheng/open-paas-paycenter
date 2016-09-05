<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<!DOCTYPE HTML>
<html>
<head>
<meta charset="UTF-8">
<title>查询用户</title>
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
<script type="text/javascript" src="${pageContext.request.contextPath}/js/highcharts/highcharts.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/highcharts/modules/exporting.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/js/locale/easyui-lang-zh_CN.js"></script>

<style type="text/css">
table {
	border: 1px solid;
	border-color: grey;
}
</style>
</head>
<body >
		<div
			style="border:0px solid;border-radius:8px;margin-bottom:10px;height: 50px; width: 1000px">
			<div
				style="border:0px solid;border-color:green;height: 30px;display:inline-block; width: 90%px;">
					<div>
						<span style="margin-left: 30px"> 
								<input class="easyui-textbox" name="username" id="un" label="用户名：" style="width:200px"></input> 
						</span> 
						<span style="margin-left: 80px"> 
								<input class="easyui-textbox" name="realname" id="rn" label="真实姓名：" style="width:200px"></input> 
						</span> 
						<span style="margin-left: 80px"> 
								<input class="easyui-textbox" name="nickname" id="nn" label="昵称：" style="width:200px"></input> 
						</span> 
						<span style="margin-left:30px">
							<a href="#" class="easyui-linkbutton" onclick="queryData()" style="width: 60px">查询</a>
						</span>
					</div>
				</div>
			</div>
		<div class="easyui-tabs" style="width:1000px;height:445px">
			<div title="查询结果" style="padding:10px">
				<div id="payAmount" style=""></div>
			</div>
		</div>
</body>
<script>
	var startTime="";
    var endTime="";
    var timeType="";
    //获取开始时间
	function onStartSelect(date) {
		//alert(date);
		//var d = new Date(date);
		//console.log(d.toLocaleString());
		//alert(d.toLocaleString().replace("日","").replace(/[年月]/g,"-"));
		startTime=format(date, 'yyyy-MM-dd');
		timeType="";
	}
	//获取结束时间
	function onEndSelect(date) {
		//alert(date);
		//var d = new Date(date);
		//console.log(d.toLocaleString());
		//alert(d.toLocaleString().replace("日","").replace(/[年月]/g,"-"));
	  endTime=format(date, 'yyyy-MM-dd');
	  timeType="";
	}
	function getDayType(date) {
	var input6=getnowtime();
	if(date=="thirty"){
	//判断点击三十天
	timeType="1";
	var d=new Date();
    var n=new Date(d.getTime()-86400000*30);
    var input5=n.getFullYear()+"-"+padleft0(n.getMonth()+1)+"-"+padleft0(n.getDate());
	}else{
	//判断点击七天
	timeType="2";
	var d=new Date();
    var n=new Date(d.getTime()-86400000*7);
    var input5=n.getFullYear()+"-"+padleft0(n.getMonth()+1)+"-"+padleft0(n.getDate());
	}
	startTime=input5;
	endTime=input6;
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
            var millisecond = nowtime.getMilliseconds(); millisecond = millisecond.toString().length == 1 ? "00" + millisecond : millisecond.toString().length == 2 ? "0" + millisecond : millisecond;
            return year+"-"+month + "-" + day;
        }
        //补齐两位数
        function padleft0(obj) {
            return obj.toString().replace(/^[0-9]{1}$/, "0" + obj);
        }
	function queryData() {
		//去除Y轴的单位k、m
		Highcharts.setOptions({
			lang : {
				numericSymbols : []
			}
		});
		$.post("${pageContext.request.contextPath}/user/admin/userstatistics/chart.json", {
			paymentId : $("input[name='paymentId']").val(),
			appId : $("input[name='appId']").val(),
			payClient : $("input[name='payClient']").val(),
			channelId : $("input[name='channelId']").val(),
			startTime : startTime,
			endTime: endTime,
			timeType: timeType
		}, function(data) {
			if (data.timeData != null) {
				var dataString = data.timeData;
				$('#payAmount').highcharts({
				 chart: {
				    	renderTo : 'payAmount',
				    	width:900,
				    	colors:[
				    	        '#3E8940',
				    	        '#F9536E',
				    	        ],
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
				});
				
				$('#payCount').highcharts({
				 chart: {
				    	renderTo : 'payCount',
				    	width:900,
				    	colors:[
				    	        '#3E8940',
				    	        '#F9536E',
				    	        ],
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
				$('#userCount').highcharts({
				 chart: {
				    	renderTo : 'userCount',
				    	width:900,
				    	colors:[
				    	        '#3E8940',
				    	        '#F9536E',
				    	        ],
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
				
				$('#payCharge').highcharts({
				 chart: {
				    	renderTo : 'payCharge',
				    	width:900,
				    	colors:[
				    	        '#3E8940',
				    	        '#F9536E',
				    	        ],
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
			return (i < 10 ? '0' : '') + i
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