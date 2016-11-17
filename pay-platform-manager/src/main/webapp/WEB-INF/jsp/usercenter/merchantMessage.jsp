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
			<td style="text-align: right;width: 120px">交易状态：</td>
			<td>
				<select class="easyui-combobox" data-options="editable:false"  name="payStatus" style="width:100%">
					<option value="">全部</option>
					<option value="0">处理中</option>
					<option value="1">成功</option>
					<option value="2">失败</option>
				</select>
			</td>
		</tr>
		
		<tr>
			<td style="text-align: right;">第三方订单号：</td>
			<td><input id="payOrderId" name="payOrderId" class="easyui-textbox" style="width:100%"></td>
			<td style="text-align: right;">支付方式：</td>
			<td>
				<select class="easyui-combobox" data-options="editable:false,prompt:'全部'" id="channelId" name="channelId" style="width:100%">
					<option value="">全部</option>
					
				</select>
			</td>
			<td style="text-align: right;">业务类型：</td>
			<td>
				<select class="easyui-combobox" data-options="editable:false,prompt:'全部'"  id="appId" name="appId" style="width:100%">
				</select>
			</td>
			
		</tr>
		<tr>
			<td style="text-align: right;">发卡行：</td>
			<!-- <td><input id="openingBank" name="openingBank" class="easyui-textbox" style="width:100%;height:32px"></td> -->
			<td>
				<select class="easyui-combobox" data-options="editable:false" id="paymentId" name="paymentId" style="width:100%">
				</select>
			</td>
			<td style="text-align: right;">缴费来源：</td>
			<td>
				<select class="easyui-combobox" data-options="editable:false" name="source" style="width:100%">
					<option value="">全部</option>
					<option value="1">pc端</option>
					<option value="2">移动端</option>
				</select>
			</td>
			<td style="text-align: right;">支付渠道：</td>
			<td>
				<select class="easyui-combobox" data-options="editable:false" id="sourceType" name="sourceType" style="width:100%">
				</select>
			</td>
		</tr>
		
		<tr>
			<td style="text-align: right;">下单时间：</td>
			<td style="text-align: right;"><input id="startDate" name="startDate" class="easyui-datebox" data-options="sharedCalendar:'#cc'" editable="false">—到—</td>
			<td><input id="endDate" name="endDate" class="easyui-datebox" data-options="sharedCalendar:'#cc'" editable="false"></td>
		</tr>
		<tr>
			<td style="text-align: right;">缴费时间：</td>
			<td style="text-align: right;"><input id="payStartDate" name="payStartDate" class="easyui-datebox" data-options="sharedCalendar:'#cc'" editable="false">—到—</td>
			<td><input id="payEndDate" name="payEndDate" class="easyui-datebox" data-options="sharedCalendar:'#cc'" editable="false"></td>
			
			<td colspan="3" style="text-align: center;">
				<a href="javascript:void(0)" class="easyui-linkbutton" onclick="submitForm()" style="width:80px">提交</a>
				<a href="javascript:void(0)" class="easyui-linkbutton" onclick="clearForm()" style="width:80px">清空</a>
				<a href="javascript:void(0)" class="easyui-linkbutton"  onclick="downloadSubmit()" style="width:80px">下载</a>
			</td>
		</tr>
		</table>
		</form>
		
	</div>
	
	</div>
	  <div class="botton" style="width: 100%;height: 300px">
		<table  id="dg"  class="easyui-datagrid" title="查询结果"    style="width:100%;max-width:100%;padding:20px 30px;"
			data-options="singleSelect:true,method:'get',toolbar:'#tb'">
		<thead>
			<tr>
				<th data-options="field:'id',width:180">订单号</th>
				<th data-options="field:'merchantOrderId',width:150">商户订单号</th>
				<th data-options="field:'payOrderId',width:150">第三方订单号</th>
				<th data-options="field:'foundDate',width:150">下单时间</th>
				<th data-options="field:'businessDate',width:150">缴费时间</th>
				<th data-options="field:'channelName',width:80">支付方式</th>
				<th data-options="field:'appId',width:60,align:'center'">业务类型</th>
				<th data-options="field:'paymentName',width:80">发卡行</th>
				<th data-options="field:'openingBank',width:100">卡类型</th><!-- 不确定字段 -->
				<th data-options="field:'source',width:80,align:'right'">缴费来源</th>
				<th data-options="field:'payStatusName',width:80,align:'right'">交易状态</th>
				<th data-options="field:'orderAmount',width:80">缴费金额</th>
				<th data-options="field:'payAmount',width:60,align:'center'">实收金额</th>
				<th data-options="field:'payCharge',width:60,align:'center'">手续费</th>
				<th data-options="field:'sourceTypeName',width:60,align:'center'">支付渠道</th>
			</tr>
		</thead>
	</table>
	</div>
	
	<div id="tb" style="padding:0.4% 3%; text-align: right;">
		交易笔数: 
		<label id="dealCount">0</label>
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		缴费总额: 
		<label id="orderAmountCount">0.0</label>
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		手续费总额: 
		<label id="payChargeCount">0.0</label>
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		实收总额: 
		<label id="payAmountCount">0.0</label>
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
			var paymentId = $("input[name='paymentId']").val();//发卡行
			var source = $("input[name='source']").val();//缴费来源
			var payStatus = $("input[name='payStatus']").val();//缴费状态
			var createDate =$("input[name='createDate']:checked").val(); 
			var startDate = $("#_easyui_textbox_input10").val();
			var endDate = $("#_easyui_textbox_input11").val();
			var sourceType = $("input[name='sourceType']").val();
			var payStartDate = $("#_easyui_textbox_input12").val();
			var payEndDate = $("#_easyui_textbox_input13").val();
			if(orderId==""&&merchantOrderId==""&&payOrderId==""&&channelId==""&&appId==""&&paymentId==""&&source==""&&payStatus==""&&sourceType==""){
				if(startDate==""&&endDate==""&&payStartDate==""&&payEndDate==""){
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
				 aDate  =  startDate.split("-");
			     oDate1  =  new  Date(aDate[1]+'/'+aDate[2]+'/'+aDate[0]);    //转换为12-18-2002格式 
			     aDate  =  endDate.split("-"); 
			     oDate2  =  new  Date(aDate[1]+'/'+aDate[2]+'/'+aDate[0]); 
			     iDays1  =  parseInt(Math.abs(oDate1-oDate2)/1000/60/60/24);    //把相差的毫秒数转换为天数
				 if(iDays1>90){
		    	   alert("请选择90天以内的日期"); 
		    	   return;
		         }
			}
			
			if(!payStartDate==""){
				if(payEndDate==""){
					alert("请选缴费择结束时间");
					return;
				}
			}
			if(!payEndDate==""){
				if(payStartDate==""){
					alert("请选择缴费开始时间");
					return;
				}
			}
			if(!payStartDate==""&&!payEndDate==""){
				if(payStartDate>payEndDate){
					alert("开始时间大于结束时间！");
					return;
				}
				aDate  =  payStartDate.split("-");
	       		oDate1  =  new  Date(aDate[1]+'/'+aDate[2]+'/'+aDate[0]);    //转换为12-18-2002格式 
	       		aDate  =  payEndDate.split("-"); 
	       		oDate2  =  new  Date(aDate[1]+'/'+aDate[2]+'/'+aDate[0]); 
	       		iDays2  =  parseInt(Math.abs(oDate1-oDate2)/1000/60/60/24);    //把相差的毫秒数转换为天数 
	       		if(iDays2>90){
		    	  alert("请选择90天以内的日期"); 
		    	  return;
		       }
			}
			
			$('#dg').datagrid({
				collapsible:true,
				rownumbers:true,
				pagination:true,
		        url: "${pageContext.request.contextPath}/manage/queryMerchant?orderId="+orderId+"&merchantOrderId="+merchantOrderId+"&payOrderId="+payOrderId+"&channelId="+channelId+"&appId="+appId+"&paymentId="+paymentId+"&source="+source+"&payStatus="+payStatus+"&createDate="+createDate+"&startDate="+startDate+"&endDate="+endDate+"&payStartDate="+payStartDate+"&payEndDate="+payEndDate+"&sourceType="+sourceType,  
                onLoadSuccess:function(data){
                  if (data.total<1){
                    $.messager.alert("提示","没有符合查询条件的数据!");
                  }else{
                  	$("#dealCount").text(data.total);
                  	$("#orderAmountCount").text(data.countOrderAmount);
                  	$("#payChargeCount").text(data.countPayCharge);
                  	$("#payAmountCount").text(data.countPayAmount);
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
		function clearForm(){
			$('#ff').form('clear');
		}
		function downloadSubmit(){
			
			/* var startDate = $("#_easyui_textbox_input10").val();
			var endDate = $("#_easyui_textbox_input11").val();
			if(startDate>endDate){
				alert("开始时间大于结束时间！");
				return;
			} */
			
			var orderId = $("input[name='orderId']").val();//商户订单号
			var merchantOrderId = $("input[name='merchantOrderId']").val();//商户订单号
			var payOrderId = $("input[name='payOrderId']").val();//第三方订单号
			var channelId = $("input[name='channelId']").val();//支付方式
			var appId = $("input[name='appId']").val();//业务类型
			var paymentId = $("input[name='paymentId']").val();//发卡行
			var source = $("input[name='source']").val();//缴费来源
			var payStatus = $("input[name='payStatus']").val();//缴费状态
			var createDate =$("input[name='createDate']:checked").val(); 
			var startDate = $("#_easyui_textbox_input10").val();
			var endDate = $("#_easyui_textbox_input11").val();
			var sourceType = $("input[name='sourceType']").val();
			var payStartDate = $("#_easyui_textbox_input12").val();
			var payEndDate = $("#_easyui_textbox_input13").val();
			if(orderId==""&&merchantOrderId==""&&payOrderId==""&&channelId==""&&appId==""&&paymentId==""&&source==""&&payStatus==""&&sourceType==""){
				if(startDate==""&&endDate==""&&payStartDate==""&&payEndDate==""){
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
				 aDate  =  startDate.split("-");
			     oDate1  =  new  Date(aDate[1]+'/'+aDate[2]+'/'+aDate[0]);    //转换为12-18-2002格式 
			     aDate  =  endDate.split("-"); 
			     oDate2  =  new  Date(aDate[1]+'/'+aDate[2]+'/'+aDate[0]); 
			     iDays1  =  parseInt(Math.abs(oDate1-oDate2)/1000/60/60/24);    //把相差的毫秒数转换为天数
				 if(iDays1>90){
		    	   alert("请选择90天以内的日期"); 
		    	   return;
		         }
			}
			
			if(!payStartDate==""){
				if(payEndDate==""){
					alert("请选缴费择结束时间");
					return;
				}
			}
			if(!payEndDate==""){
				if(payStartDate==""){
					alert("请选择缴费开始时间");
					return;
				}
			}
			if(!payStartDate==""&&!payEndDate==""){
				if(payStartDate>payEndDate){
					alert("开始时间大于结束时间！");
					return;
				}
				aDate  =  payStartDate.split("-");
	       		oDate1  =  new  Date(aDate[1]+'/'+aDate[2]+'/'+aDate[0]);    //转换为12-18-2002格式 
	       		aDate  =  payEndDate.split("-"); 
	       		oDate2  =  new  Date(aDate[1]+'/'+aDate[2]+'/'+aDate[0]); 
	       		iDays2  =  parseInt(Math.abs(oDate1-oDate2)/1000/60/60/24);    //把相差的毫秒数转换为天数 
	       		if(iDays2>90){
		    	  alert("请选择90天以内的日期"); 
		    	  return;
		       }
			}
			
			//document.getElementById("ff").action="${pageContext.request.contextPath}/manage/downloadSubmit?startDate="+startDate+"&endDate="+endDate;
		    document.getElementById("ff").action="${pageContext.request.contextPath}/manage/downloadSubmit?orderId="+orderId+"&merchantOrderId="+merchantOrderId+"&payOrderId="+payOrderId+"&channelId="+channelId+"&appId="+appId+"&paymentId="+paymentId+"&source="+source+"&payStatus="+payStatus+"&createDate="+createDate+"&startDate="+startDate+"&endDate="+endDate+"&payStartDate="+payStartDate+"&payEndDate="+payEndDate+"&sourceType="+sourceType;
		    document.getElementById("ff").submit();
		}
		//页面加载  
		$(document).ready(function(){ 
			
					var hy="ninety";
					getDayType(hy);
		            loadSelect();
		            
		});  
		
		function loadSelect(){
			$('#paymentId').combobox({
				url:'${pageContext.request.contextPath}/manage/findBankCode?flag=all',
				valueField:'id',
				textField:'text'
			});
		
			$('#appId').combobox({
				url:'${pageContext.request.contextPath}/manage/findAllDepts',
				valueField:'id',
				textField:'text'
			});
			
			$('#channelId').combobox({
				url:'${pageContext.request.contextPath}/manage/findAllPayChannel',
				valueField:'id',
				textField:'text'
			});
			
			$('#sourceType').combobox({
				url:'${pageContext.request.contextPath}/manage/findSourceType',
				valueField:'id',
				textField:'text'
			});
		}
		
		function getDayType(date) {
			var input6=getnowtime();
			if(date=="ninety"){
				//判断点击九十天
				timeType="1";
				var d=new Date();
			    var n=new Date(d.getTime()-86400000*90);
			    var input5=n.getFullYear()+"-"+padleft0(n.getMonth()+1)+"-"+padleft0(n.getDate());
			}
			$("#_easyui_textbox_input10").val(input5);
			$("#_easyui_textbox_input11").val(input6);
			$("#_easyui_textbox_input12").val(input5);
			$("#_easyui_textbox_input13").val(input6);
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