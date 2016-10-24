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
					<td style="text-align: right;">商户订单号：</td>
					<td>
						<input class="easyui-textbox" name="queryMerchantOrderId" id="queryMerchantOrderId" style="width:180px;">
					</td>
					<td style="text-align: right;">退&nbsp;费&nbsp;商&nbsp;户：</td>
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
					<td style="text-align: right;">处&nbsp;理&nbsp;时&nbsp;间：</td>
					<td style="text-align: left;" colspan="3">
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
	
	<table id="dg" class="easyui-datagrid" title="退费单据记录" style="height:400px;width:100%;max-width:100%;" 
			data-options="rownumbers:true,singleSelect:true,striped:true,fitColumns:true,method:'get',toolbar:'#tb'">
		<thead>
			<tr>
			    <th data-options="field:'merchantOrderId',width:180">商户订单号</th>
				<th data-options="field:'merchantId',width:80">退费商户</th>
				<th data-options="field:'refundMoney',width:60,align:'center'">退费金额</th>
				<th data-options="field:'appId',width:60,align:'center',formatter:formatAppId">业务来源</th>
				<th data-options="field:'sourceUID',align:'center',width:50">用户ID</th>
				<th data-options="field:'sourceUserName',align:'center',width:60">用户名</th>
				<th data-options="field:'realName',align:'center',width:60">真实姓名</th>
				<th data-options="field:'phone',align:'center',width:95">手机号</th>
				<th data-options="field:'createTime',align:'center',width:150">处理时间</th>
				<th data-options="field:'remark',align:'center',width:80">备注</th>
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
							商户订单号:
						</td>
						<td style="width:40%">	
							<input id="addMerchantOrderId" class="easyui-textbox" style="width:98%;"
			                 	name="addMerchantOrderId"  required="true"  missingMessage="输入有效订单号" />
						</td>
					
						<td style="margin-bottom:20px">
							用&nbsp;&nbsp;&nbsp;户&nbsp;&nbsp;&nbsp;ID:
						</td>
						<td style="width:40%">
			                 <input id="addSourceUID" class="easyui-textbox" style="width:98%;"
			                 	name="addSourceUID" value=''/>
						</td>						
					</tr>
					<tr>
						<td style="margin-bottom:20px">
							退&nbsp;费&nbsp;金&nbsp;额:
						</td>
						<td style="width:40%">
			                 <input id="addRefundMoney" class="easyui-numberbox" style="width:98%;" data-options="precision:2"
			                 	name="addRefundMoney"  required="true"  missingMessage="金额可以精确到分" />
						</td>
					
						<td style="margin-bottom:20px">
							真&nbsp;实&nbsp;姓&nbsp;名:
						</td>
						<td style="width:40%">
			                 <input id="addRealName" class="easyui-textbox" style="width:98%;"
			                 	name="addRealName"  />
						</td>						
					</tr>
					<tr>
						<td style="margin-bottom:20px">
							退&nbsp;费&nbsp;商&nbsp;户:
						</td>
						<td style="width:40%;">	
							<select class="easyui-combobox" data-options="editable:false,prompt:'请选择商户名'" id="addMerchantName" 
								name="addMerchantName"  style="width:100%;height:30px;padding:5px;">
							</select>
						</td>
					
						<td style="margin-bottom:20px">
							用&nbsp;&nbsp;&nbsp;户&nbsp;&nbsp;&nbsp;名:
						</td>
						<td style="width:40%">
			                 <input id="addSourceUserName" class="easyui-textbox" style="width:98%;"
			                 	name="addSourceUserName"  />
						</td>								
					</tr>
					<tr>
						<td style="margin-bottom:20px">
							业&nbsp;务&nbsp;来&nbsp;源:
						</td>
						<td style="width:40%;">	
							<select class="easyui-combobox" data-options="editable:false,prompt:'请选择业务来源'" id="addAppId" 
								name="addAppId"  style="width:100%;height:30px;padding:5px;">
								<option value="0">请选择业务来源</option>
								<option value="1">OES学历</option>
								<option value="10026" >mooc2u</option>
							</select>
						</td>
					
						<td style="margin-bottom:20px">
							手&nbsp;&nbsp;&nbsp;机&nbsp;&nbsp;&nbsp;号:
						</td>
						<td style="width:40%">
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
				</table>
			</form>
		</div>
		<div border="false" style="text-align:center; height: 3%;margin-top:4%;">
			<a class="easyui-linkbutton" icon="icon-ok" href="javascript:void(0)" onclick="submitAddRefund();"> 确定</a>
			<a class="easyui-linkbutton" icon="icon-clear" href="javascript:void(0)" onclick="clearAddForm()" style="margin-left:30px;">清空</a> 
			<a class="easyui-linkbutton" icon="icon-cancel" href="javascript:void(0)"  onclick="closeAddWin();" style="margin-left:30px;">取消</a>
		</div>
	</div>
	
	<div id="cc" class="easyui-calendar"></div>
</body>
<script>
		//页面预加载
		$(function(){
			findOrderRefund();
			loadSelect();
		});
		
		//设置添加退费单据窗口
         function addWin() {
            $('#addWin').window({
                title: '退费单据-添加',
                width: '60%',
                modal: true,
                shadow: true,
                closed: true,
                height: '80%',
                resizable:false
            });
        } 
	
         //关闭添加退费单据窗口
         function closeAddWin() {
            $('#addWin').window('close');
	      	window.location.reload();
        } 
        
        //清空添加表单
        function clearAddForm(){
        	$('#addForm').form('clear');
        } 	
		
		//打开线下收费录入窗口
         addWin();
        function openAddWin() {
            $('#addWin').window('open');
            loadSelect();			
        } 
        
        //加载select
        function loadSelect(){
			//加载所有商户名
			 $('#addMerchantName,#queryMerchantName').combobox({
				url:'${pageContext.request.contextPath}/paychannel/findMerchantNames',
				valueField:'id',
				textField:'text'
			});
        } 
        
		//提交添加
		function submitAddRefund(){
			$('#addForm').form('submit',{
				onSubmit:function(){
					return $(this).form('enableValidation').form('validate');
				}
			});
			var addMerchantOrderId = $('#addMerchantOrderId').textbox('getValue');
			var addRefundMoney = $('#addRefundMoney').textbox('getValue');
			var addSourceUserName = $('#addSourceUserName').textbox('getValue');
			var addRealName = $('#addRealName').textbox('getValue');
			var addPhone = $('#addPhone').textbox('getValue');
			var addMerchantName = $('#addMerchantName').combobox('getValue');
			var addAppId = $('#addAppId').combobox('getValue');
			var addRemark = $('#addRemark').textbox('getValue');
			var addSourceUID = $('#addSourceUID').textbox('getValue');
			//若校验为true 提交
			if(checkAddOrderRefund(addMerchantOrderId,addRefundMoney,addMerchantName,addAppId,addPhone)){
				$.ajax({
					type:"post",
					url:"/pay-platform-manager/manage/submitAddOrderRefund",
					data:{"addMerchantOrderId":addMerchantOrderId,"addRefundMoney":addRefundMoney,"addSourceUserName":addSourceUserName,"addRealName":addRealName,"addPhone":addPhone,"addMerchantName":addMerchantName,"addAppId":addAppId,"addRemark":addRemark,"addSourceUID":addSourceUID},
					dataType:"json",
					success:function (data){
					alert(data.result);
						if(data.result == 1){
							$.messager.alert("系统提示","恭喜，添加成功!","info");
							clearAddForm();
							closeAddWin();
							var url = "${pageContext.request.contextPath}/manage/getMerchantOrderRefund";
							reload(url);
						}else if(data.result == 2){
							clearAddForm();
							$.messager.alert("系统提示","需退费商户订单号不存在，请核实后再填写!","error");	
						}else if(data.result == 3){
							clearAddForm();
							$.messager.alert("系统提示","退费金额超过实收金额，请核实后再填写!","error");	
						}else if(data.result == 4){
							clearAddForm();
							$.messager.alert("系统提示","商户订单号已退费，请核实后再填写!","error");	
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
		function checkAddOrderRefund(addMerchantOrderId,addRefundMoney,addMerchantName,addAppId,addPhone){
			if(addMerchantOrderId == "" || addMerchantOrderId == null || addMerchantOrderId == undefined){
					$.messager.alert("系统提示","请填写商户订单号！","error");	
					return false;
			}
			if(addRefundMoney == "" || addRefundMoney == null || addRefundMoney == undefined){
					$.messager.alert("系统提示","请填写退费费金额！","error");		
					return false;
			}
			if(addMerchantName == "" || addMerchantName == null || addMerchantName == undefined){
					$.messager.alert("系统提示","请选择退费商户名称！","error");		
					return false;
			}
			if(addAppId == "" || addAppId == null || addAppId == undefined || addAppId == 0){
					$.messager.alert("系统提示","请选择业务来源！","error");		
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

		function findOrderRefund(){
		 	var url="${pageContext.request.contextPath}/manage/getMerchantOrderRefund";
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
			var queryMerchantOrderId = $('#queryMerchantOrderId').textbox('getValue');
			var querySourceUserName = $('#querySourceUserName').textbox('getValue');
			var queryMerchantName = $('#queryMerchantName').combobox('getValue');
			var queryAppId = $('#queryAppId').combobox('getValue');
			var startDate = $('#startDate').datebox('getValue');
			var endDate = $('#endDate').datebox('getValue');
			
			if(queryMerchantOrderId==""&&querySourceUserName==""&&queryMerchantName==""&&(queryAppId=="" || queryAppId=='0')&&startDate==""&&endDate==""){
				findOrderRefund();
			}else if(!startDate==""&&!endDate==""){
				if(startDate>endDate){
					 $.messager.alert("提示","开始时间大于结束时间!");
				}else{
					var url="${pageContext.request.contextPath}/manage/getMerchantOrderRefund?merchantOrderId="+queryMerchantOrderId+"&sourceUserName="+querySourceUserName+"&merchantName="+queryMerchantName+"&appId="+queryAppId+"&startDate="+startDate+"&endDate="+endDate;
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
				var url="${pageContext.request.contextPath}/manage/getMerchantOrderRefund?merchantOrderId="+queryMerchantOrderId+"&sourceUserName="+querySourceUserName+"&merchantName="+queryMerchantName+"&appId="+queryAppId+"&startDate="+startDate+"&endDate="+endDate;
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
			var queryMerchantOrderId = $('#queryMerchantOrderId').textbox('getValue');
			var querySourceUserName = $('#querySourceUserName').textbox('getValue');
			var queryMerchantName = $('#queryMerchantName').combobox('getValue');
			var queryAppId = $('#queryAppId').combobox('getValue');
			var startDate = $('#startDate').datebox('getValue');
			var endDate = $('#endDate').datebox('getValue');
		
		 	var url="${pageContext.request.contextPath}/manage/refundDownloadSubmit?merchantOrderId="+queryMerchantOrderId+"&sourceUserName="+querySourceUserName+"&merchantName="+queryMerchantName+"&appId="+queryAppId+"&startDate="+startDate+"&endDate="+endDate;
			
			document.getElementById("ff").action=url;
		    document.getElementById("ff").submit();
		}
	</script>
</html>