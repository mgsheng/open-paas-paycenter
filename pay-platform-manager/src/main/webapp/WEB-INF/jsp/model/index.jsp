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
	<table id="dg" class="easyui-datagrid" title="权限资源管理" style="width:100%;height:540px"
			data-options="rownumbers:true,singleSelect:true,url:'',method:'get',toolbar:'#tb'">
		<thead>
			<tr>
				<th data-options="field:'name',width:400">名称</th>
				<th data-options="field:'code',width:300">code</th>
				<th data-options="field:'status',width:240,align:'right'">状态</th>
				<th data-options="field:'creatTime',width:250,align:'right'">创建时间</th>
			</tr>
		</thead>
	</table>
	<div id="tb" style="padding:2px 5px;">
	   <span >
		创建时间: <input class="easyui-datebox" style="width:110px">
		To: <input class="easyui-datebox" style="width:110px">
		&nbsp;&nbsp;&nbsp;&nbsp;
		名称: 
		<input class="easyui-textbox" name="name" id="name" style="width:110px;">
		&nbsp;&nbsp;&nbsp;&nbsp;
		<a href="#" class="easyui-linkbutton" iconCls="icon-search">查询</a>
		</span>
		<span style="margin-left: 45%;">
		<a href="#" class="easyui-linkbutton" iconCls="icon-add" plain="true"></a>
		<a href="#" class="easyui-linkbutton" iconCls="icon-edit" plain="true"></a>
		<a href="#" class="easyui-linkbutton" iconCls="icon-cut" plain="true"></a>
		</span>
	</div>

</body>
<script>

        function getSelected(){
			var row = $('#dg').datagrid('getSelected');
			if (row){
				$.messager.alert('Info', row.itemid+":"+row.productid+":"+row.attr1);
			}
		}
		function submitForm(){
			var orderId = $("input[name='orderId']").val();//商户订单号
			var merchantOrderId = $("input[name='merchantOrderId']").val();//商户订单号
			var payOrderId = $("input[name='payOrderId']").val();//第三方订单号
			var channelId = $("input[name='channelId']").val();//支付方式
			var appId = $("input[name='appId']").val();//业务类型
			var userName = $("input[name='userName']").val();//商户订单号
			var createDate =$("input[name='createDate']:checked").val(); 
			var startDate = $("#_easyui_textbox_input7").val();
			var endDate = $("#_easyui_textbox_input8").val();
			
			if(orderId==""&&merchantOrderId==""&&payOrderId==""&&channelId==""&&appId==""&&userName==""){
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
			if(orderId==""&&userName==""){
				alert("请填写订单号或用户名！");
				return;
			}
			
			
			
			
			$('#dg').datagrid({
				collapsible:true,
				rownumbers:true,
				pagination:true,
		        url: "${pageContext.request.contextPath}/manage/userQueryMessage?orderId="+orderId+"&merchantOrderId="+merchantOrderId+"&payOrderId="+payOrderId+"&channelId="+channelId+"&appId="+appId+"&createDate="+createDate+"&startDate="+startDate+"&endDate="+endDate+"&userName="+userName,  
		        //pagination: true,显示分页工具栏
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
			
			$("#_easyui_textbox_input7").val(input5);
			$("#_easyui_textbox_input8").val(input6);
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