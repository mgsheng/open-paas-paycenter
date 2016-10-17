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
					<td style="text-align: right;">订&nbsp;&nbsp;&nbsp;单&nbsp;&nbsp;&nbsp;号：</td>
					<td>
						<input class="easyui-textbox" name="queryOrderId" id="queryOrderId" style="width:180px;">
					</td>
					<td style="text-align: right;">线下订单号：</td>
					<td>
						<input class="easyui-textbox" name="queryMerchantOrderId" id="queryMerchantOrderId" style="width:180px;">
					</td>
					<td style="text-align: right;">收&nbsp;费&nbsp;商&nbsp;户：</td>
					<td>
						<select class="easyui-combobox" data-options="editable:false,prompt:'请选择商户名'" id="queryMerchantName" 
								name="queryMerchantName"  style="width:180px;height:25px;padding:5px;">
						</select>
					</td>
					
				</tr>
				<tr>
					<td style="text-align: right;">用&nbsp;&nbsp;&nbsp;户&nbsp;&nbsp;&nbsp;名：</td>
					<td>
						<input id="querySourceUserName" name="querySourceUserName" class="easyui-textbox" style="width:180px;">
					</td>
					<td style="text-align: right;">操&nbsp;&nbsp;&nbsp;作&nbsp;&nbsp;&nbsp;人：</td>
					<td>
						<input id="queryOperator" name="queryOperator" class="easyui-textbox" style="width:180px;">
					</td>
					<td style="text-align: right;">业&nbsp;务&nbsp;来&nbsp;源：</td>
					<td>
						<select class="easyui-combobox" data-options="editable:false,prompt:'请选择业务来源'" id="queryAppId" 
								name="queryAppId"  style="width:180px;height:25px;padding:5px;">
							<option value="0">请选择业务来源</option>
							<option value="1">OES学历</option>
							<option value="10026" >mooc2u</option>
						</select>
					</td>
				</tr>
				<tr>
					<td style="text-align: right;">支&nbsp;付&nbsp;方&nbsp;式：</td>
					<td>
						<select class="easyui-combobox" data-options="editable:false,prompt:'请选择支付方式'" id="queryChannelId" 
								name="queryChannelId"  style="width:180px;height:25px;padding:5px;">
						</select>
					</td>
					<td style="text-align: left;padding-left:80px;" colspan="4">
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
	
	<table id="dg" class="easyui-datagrid" title="线下收费记录" style="height:400px;width:100%" 
			data-options="rownumbers:true,singleSelect:true,striped:true,fitColumns:true,method:'get',toolbar:'#tb'">
		<thead>
			<tr>
			    <th data-options="field:'id',width:170">订单号</th>
				<th data-options="field:'merchantOrderId',width:150">线下订单号</th>
				<th data-options="field:'merchantId',width:90">收费商户</th>
				<th data-options="field:'money',width:60,align:'center'">收费金额</th>
				<th data-options="field:'appId',width:60,align:'center',formatter:formatAppId">业务来源</th>
				<th data-options="field:'sourceUID',align:'center',width:50">用户ID</th>
				<th data-options="field:'sourceUserName',align:'center',width:60">用户名</th>
				<th data-options="field:'realName',align:'center',width:60">真实姓名</th>
				<th data-options="field:'phone',align:'center',width:95">手机号</th>
				<th data-options="field:'channelId',width:60,align:'center'">支付方式</th>
				<th data-options="field:'bankCode',width:110,align:'center'">发卡行</th>
				<th data-options="field:'operator',align:'center',width:55">操作人</th>
				<th data-options="field:'remark',align:'center',width:70">备注</th>
			</tr>
		</thead>
	</table>
	<div id="tb" style="padding:0.4% 2%; text-align: right;">
		<a href="#" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="openAddWin();"></a>
	</div>

	<!-- 添加线下收费单据窗口 -->
	<div id="addWin" class="easyui-window" title="线下收费-添加" collapsible="false" minimizable="false" maximizable="false" 
		style=" background: #fafafa;">
		<div border="false" style="padding: 20px; background: #fff; border: 1px solid #ccc;">
			<form id="addForm" class="easyui-form" method="post"  data-options="novalidate:true">
				<table cellpadding=3>
					<tr>
						<td style="margin-bottom:20px">
							线下订单号:
						</td>
						<td style="width:35%;">	
							<input id="addMerchantOrderId" class="easyui-textbox" style="width:98%;"
			                 	name="addMerchantOrderId"  required="true"  missingMessage="输入有效订单号" />
						</td>
					
						<td style="margin-bottom:20px">
							收&nbsp;费&nbsp;商&nbsp;户:
						</td>
						<td style="width:35%;">	
							<select class="easyui-combobox" data-options="editable:false,prompt:'请选择商户名'" id="addMerchantName" 
								name="addMerchantName"  style="width:100%;height:30px;padding:5px;">
							</select>
						</td>
					</tr>
					<tr>
						<td style="margin-bottom:20px">
							收&nbsp;费&nbsp;金&nbsp;额:
						</td>
						<td>
			                 <input id="addMoney" class="easyui-numberbox" style="width:98%;" data-options="precision:2"
			                 	name="addMoney"  required="true"  missingMessage="金额可以精确到分" />
						</td>
					
						<td style="margin-bottom:20px">
							业&nbsp;务&nbsp;来&nbsp;源:
						</td>
						<td style="width:35%;">	
							<select class="easyui-combobox" data-options="editable:false,prompt:'请选择业务来源'" id="addAppId" 
								name="addAppId"  style="width:100%;height:30px;padding:5px;">
								<option value="0">请选择业务来源</option>
								<option value="1">OES学历</option>
								<option value="10026" >mooc2u</option>
							</select>
						</td>
					</tr>
					<tr>
						<td style="margin-bottom:20px">
							用&nbsp;&nbsp;&nbsp;户&nbsp;&nbsp;&nbsp;名:
						</td>
						<td>
			                 <input id="addSourceUserName" class="easyui-textbox" style="width:98%;"
			                 	name="addSourceUserName"  />
						</td>
					
						<td style="margin-bottom:20px">
							支&nbsp;付&nbsp;方&nbsp;式:
						</td>
						<td style="width:35%;">	
							<select class="easyui-combobox" data-options="editable:false,prompt:'请选择支付方式'" id="addChannelId" 
								name="addChannelId"  style="width:100%;height:30px;padding:5px;">
							</select>
						</td>											
					</tr>
					<tr>
						<td style="margin-bottom:20px">
							真&nbsp;实&nbsp;姓&nbsp;名:
						</td>
						<td>
			                 <input id="addRealName" class="easyui-textbox" style="width:98%;"
			                 	name="addRealName"  />
						</td>
					
						<td style="margin-bottom:20px">
							发&nbsp;&nbsp;&nbsp;卡&nbsp;&nbsp;&nbsp;行:
						</td>
						<td style="width:35%;">	
							<select class="easyui-combobox" data-options="editable:false,prompt:'请选择发卡行'" id="addBankCode" 
								name="addBankCode"  style="width:100%;height:30px;padding:5px;">
							</select>
						</td>	
					</tr>
					<tr>
						<td style="margin-bottom:20px">
							用&nbsp;&nbsp;&nbsp;户&nbsp;&nbsp;&nbsp;ID:
						</td>
						<td>
			                 <input id="addSourceUID" class="easyui-textbox" style="width:98%;"
			                 	name="addSourceUID" value=''/>
						</td>
					
						<td style="margin-bottom:20px">
							手&nbsp;&nbsp;&nbsp;机&nbsp;&nbsp;&nbsp;号:
						</td>
						<td>
			                 <input id="addPhone" class="easyui-textbox" style="width:98%;"
			                 	name="addPhone"/>
						</td>
					</tr>
					<tr>
						<td style="margin-bottom:20px">
							备&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;注:
						</td>
						<td colspan="3">
			                 <input id="addRemark" class="easyui-textbox" data-options="multiline:true" style="width:98%;"
			                 	name="addRemark" />
						</td>
					</tr>
					<tr hidden="true">
						<td >
							操&nbsp;&nbsp;&nbsp;作&nbsp;&nbsp;&nbsp;人:
						</td>
						<td>
			                 <input id="addOperator"  style="width:98%;border:none;"
			                 	name="addOperator" value=''/>
						</td>
					</tr>
				</table>
			</form>
		</div>
		<div border="false" style="text-align:center; height: 3%;margin-top:4%;">
			<a class="easyui-linkbutton" icon="icon-ok" href="javascript:void(0)" onclick="submitAddOffline();"> 确定</a>
			<a class="easyui-linkbutton" icon="icon-clear" href="javascript:void(0)" onclick="clearAddForm()" style="margin-left:30px;">清空</a> 
			<a class="easyui-linkbutton" icon="icon-cancel" href="javascript:void(0)"  onclick="closeAddWin();" style="margin-left:30px;">取消</a>
		</div>
	</div>
</body>
<script>
		//页面预加载
		$(function(){
			var operator=$("#realName",window.parent.document).text();
			$("#addOperator").val(operator);
			findOrderOffline();
			loadSelect();
		});
		
		//设置添加费率窗口
        function addWin() {
            $('#addWin').window({
                title: '线下收费单据-添加',
                width: '50%',
                modal: true,
                shadow: true,
                closed: true,
                height: '70%',
                resizable:false
            });
        }
	
         //关闭添加费率窗口
        function closeAddWin() {
            $('#addWin').window('close');
	      	window.location.reload();
        }
        
        //清空添加表单
        function clearAddForm(){
        	$('#addForm').form('clear');
        	var operator=$("#realName",window.parent.document).text();
			$("#addOperator").val(operator);
        }	
		
		//打开线下收费录入窗口
        addWin();
        function openAddWin() {
            $('#addWin').window('open');
            loadSelect();			
        }
        
        //加载select
        function loadSelect(){
        	//加载所有支付方式名称，并且选中支付名称后触发根据该名称查询对应渠道编码的事件
            $('#addChannelId,#queryChannelId').combobox({
				url:'${pageContext.request.contextPath}/paychannel/findPayNames',
				valueField:'id',
				textField:'text'
			});
			
			//加载所有商户名
			 $('#addMerchantName,#queryMerchantName').combobox({
				url:'${pageContext.request.contextPath}/paychannel/findMerchantNames',
				valueField:'id',
				textField:'text'
			});
			
			//加载所有BankCode
			$('#addBankCode').combobox({
				url:'${pageContext.request.contextPath}/manage/findBankCode',
				valueField:'id',
				textField:'text'
			});
        }
        
		//提交添加
		function submitAddOffline(){
			$('#addForm').form('submit',{
				onSubmit:function(){
					return $(this).form('enableValidation').form('validate');
				}
			});
			var addMerchantOrderId = $('#addMerchantOrderId').textbox('getValue');
			var addMoney = $('#addMoney').textbox('getValue');
			var addSourceUserName = $('#addSourceUserName').textbox('getValue');
			var addRealName = $('#addRealName').textbox('getValue');
			var addPhone = $('#addPhone').textbox('getValue');
			var addMerchantName = $('#addMerchantName').combobox('getValue');
			var addAppId = $('#addAppId').combobox('getValue');
			var addChannelId = $('#addChannelId').combobox('getValue');
			var addBankCode = $('#addBankCode').combobox('getValue');
			var addRemark = $('#addRemark').textbox('getValue');
			var addOperator = $('#addOperator').val();
			var addSourceUID = $('#addSourceUID').textbox('getValue');
			//若校验为true 提交
			if(checkAddOrderOffline(addMerchantOrderId,addMoney,addMerchantName,addAppId,addChannelId,addBankCode,addPhone)){
				$.ajax({
					type:"post",
					url:"/pay-platform-manager/manage/submitAddOrderOffline",
					data:{"addMerchantOrderId":addMerchantOrderId,"addMoney":addMoney,"addSourceUserName":addSourceUserName,"addRealName":addRealName,"addPhone":addPhone,"addMerchantName":addMerchantName,"addAppId":addAppId,"addChannelId":addChannelId,"addBankCode":addBankCode,"addRemark":addRemark,"addOperator":addOperator,"addSourceUID":addSourceUID},
					dataType:"json",
					success:function (data){
						if(data.result == 1){
							$.messager.alert("系统提示","恭喜，添加成功!","info");
							clearAddForm();
							closeAddWin();
							var url = "${pageContext.request.contextPath}/manage/getMerchantOrderOffline";
							//window.location.reload();
							reload(url);
						}else if(data.result == 2){
							clearAddForm();
							$.messager.alert("系统提示","该订单号已存在，请核实后再填写!","error");	
						}else{
							clearAddForm();
							$.messager.alert("系统提示","添加失败，请重新添加!","error");
						}
					},
					error:function(){
						$.messager.alert("系统提示","添加异常，请刷新页面!","error");
					}
				});
			}
		}
		
		//提交添加前的校验
		function checkAddOrderOffline(addMerchantOrderId,addMoney,addMerchantName,addAppId,addChannelId,addBankCode,addPhone){
			if(addMerchantOrderId == "" || addMerchantOrderId == null || addMerchantOrderId == undefined){
					$.messager.alert("系统提示","请填写线下订单号！","error");	
					return false;
			}
			if(addMoney == "" || addMoney == null || addMoney == undefined){
					$.messager.alert("系统提示","请填写收费金额！","error");		
					return false;
			}
			if(addMerchantName == "" || addMerchantName == null || addMerchantName == undefined){
					$.messager.alert("系统提示","请选择商户名称！","error");		
					return false;
			}
			if(addAppId == "" || addAppId == null || addAppId == undefined || addAppId == 0){
					$.messager.alert("系统提示","请选择业务来源！","error");		
					return false;
			}
			if(addChannelId == "" || addChannelId == null || addChannelId == undefined){
					$.messager.alert("系统提示","请选择支付方式！","error");		
					return false;
			}
			if(addBankCode == "" || addBankCode == null || addBankCode == undefined){
					$.messager.alert("系统提示","请选择发卡行！","error");		
					return false;
			}
			if(addPhone != "" && addPhone != null){
				if(!checkPhone(addPhone)){	
					return false;
				}
			}
			return true;
		}
		
      	//手机号格式校验
      	 function checkPhone(addPhone){
      	 	var phone1=/^([1])([1-9]{1}[0-9]{9})?$/;
			if(addPhone.length!=11 || !addPhone.match(phone1)){
				$.messager.alert("系统提示","请输入正确手机号！","error");	
				return false;
			}
      		return true;
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
		
		function formatAppId(val,row){
			if (val == 1){
				return 'OES学历';
			} else if (val == 10026){
				return 'mooc2u';
			}
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
			
			if(queryOrderId==""&&queryMerchantOrderId==""&&querySourceUserName==""&&queryMerchantName==""&&(queryAppId=="" || queryAppId=='0')&&queryChannelId==""&&queryOperator==""){
				$.messager.alert("提示","请输入查询条件!");
			}else{
			 	var url="${pageContext.request.contextPath}/manage/getMerchantOrderOffline?orderId="+queryOrderId+"&merchantOrderId="+queryMerchantOrderId+"&sourceUserName="+querySourceUserName+"&merchantName="+queryMerchantName+"&appId="+queryAppId+"&channelId="+queryChannelId+"&operator="+queryOperator;
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
			
		 	var url="${pageContext.request.contextPath}/manage/offlineDownloadSubmit?orderId="+queryOrderId+"&merchantOrderId="+queryMerchantOrderId+"&sourceUserName="+querySourceUserName+"&merchantName="+queryMerchantName+"&appId="+queryAppId+"&channelId="+queryChannelId+"&operator="+queryOperator;
			
			document.getElementById("ff").action=url;
		    document.getElementById("ff").submit();
		}
	</script>
</html>