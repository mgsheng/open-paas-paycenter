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
			<td style="text-align: right;">流水号：</td>
			<td>
				<input class="easyui-textbox" name="serialNo" id="serialNo" style="width:100%">
			</td>
			<td style="text-align: right; width: 100px">用户名：</td>
			<td>
				<input class="easyui-textbox" name="userName" id="userName" style="width:100%">
			</td>
			<td style="text-align: right;">交易类型：</td>
			<td>
				<select class="easyui-combobox" data-options="editable:false" name="payType" style="width:100%">
						<option value=""  selected="selected">全部</option>
						<option value="1">充值</option>
						<option value="2" >消费</option>
				</select>
			</td>
		</tr>
		<tr>
			
			<td style="text-align: right;">业务类型：</td>
			<td>
				<select class="easyui-combobox" data-options="editable:false"  name="appId" style="width:100%">
						<option value="" selected="selected">全部</option>
						<option value="1">OES学历</option>
						<option value="10026" >mooc2u</option>
				</select>
			</td>
			<td style="text-align: right;">下单时间：</td>
			<td>
				<a href="#" class="easyui-linkbutton" data-options="plain:true"	onclick="getDayType('today')">今天</a>
				<a href="#" class="easyui-linkbutton" data-options="plain:true"	onclick="getDayType('both')">2天</a>
				<a href="#" class="easyui-linkbutton" data-options="plain:true"	onclick="getDayType('seven')">7天</a>
				<a href="#" class="easyui-linkbutton" data-options="plain:true" onclick="getDayType('thirty')">30天</a>
			</td>
			
			<td style="text-align: right;"><input id="startDate" name="startDate" class="easyui-datebox" data-options="sharedCalendar:'#cc'" editable="false">—到—</td>
			<td><input id="endDate" name="endDate" class="easyui-datebox" data-options="sharedCalendar:'#cc'" editable="false"></td>
			
		</tr>
		<tr>
			<td></td>
			<td></td>
			<td></td>
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
				<th data-options="field:'serialNo',width:180">流水号</th>
				<th data-options="field:'createDate',width:150">交易时间</th>
				<th data-options="field:'payTypeName',width:150">交易类型</th>
				<th data-options="field:'userName',width:60,align:'center'">用户名</th>
				<th data-options="field:'amount',width:150">金额</th>
				<th data-options="field:'appIdName',width:60,align:'center'">业务类型</th>
			</tr>
		</thead>
		
	</table>
	</div>
	
<div id="cc" class="easyui-calendar"></div>

</body>
<script>


		function submitForm(){
			var serialNo = $("input[name='serialNo']").val();//流水号
			var userName = $("input[name='userName']").val();//用户名
			var payType = $("input[name='payType']").val();//交易类型
			var appId = $("input[name='appId']").val();//业务类型
			var startDate = $("#_easyui_textbox_input5").val();//开始时间
			var endDate = $("#_easyui_textbox_input6").val();//结束时间
			if(serialNo==""&&payType==""&&userName==""&&appId==""){
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
		        url: "${pageContext.request.contextPath}/running/accountQueryMerchant?serialNo="+serialNo+"&userName="+userName+"&payType="+payType+"&appId="+appId+"&startDate="+startDate+"&endDate="+endDate,  
		        //pagination: true,显示分页工具栏
		        
		     
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
	</script>
</html>