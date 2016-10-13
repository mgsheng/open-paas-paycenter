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
	<table id="dg" class="easyui-datagrid" title="线下收费记录" style="height:400px;width:100%" 
			data-options="rownumbers:true,singleSelect:true,striped:true,fitColumns:true,method:'get',toolbar:'#tb'">
		<thead>
			<tr>
			    <th data-options="field:'id',width:180">订单号</th>
				<th data-options="field:'merchantOrderId',width:150">线下订单号</th>
				<th data-options="field:'merchantId',width:150">收费商户</th>
				<th data-options="field:'money',width:80,align:'center'">收费金额</th>
				<th data-options="field:'appId',width:60,align:'center'">业务来源</th>
				<th data-options="field:'sourceUserName',align:'center',width:60">用户名</th>
				<th data-options="field:'realName',align:'center',width:60">真实姓名</th>
				<th data-options="field:'phone',align:'center',width:100">手机号</th>
				<th data-options="field:'channelId',width:60,align:'center'">支付方式</th>
				<th data-options="field:'bankCode',width:80,align:'center'">发卡行</th>
				<th data-options="field:'operator',align:'center',width:80">操作人</th>
				<th data-options="field:'remark',align:'center',width:85">备注</th>
			</tr>
		</thead>
	</table>
	<div id="tb" style="padding:0.4% 3%; text-align: right;">
		<!-- 商户号: 
		<input class="easyui-textbox" name="merchant_id" id="merchant_id" style="width:8%;" prompt="选填">
		&nbsp;&nbsp;&nbsp;&nbsp; -->
		<!-- <a href="#" class="easyui-linkbutton" iconCls="icon-search " plain="true" onclick="findChannelRates();"></a> -->
		<a href="#" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="openAddWin();"></a>
		<!-- <a href="#" class="easyui-linkbutton" iconCls="icon-edit" plain="true" onclick="updateChannelRate();"></a> -->
		<!-- <a href="#" class="easyui-linkbutton" iconCls="icon-cut" plain="true" onclick="removeChannelRate();"></a> -->
	</div>
	<!-- 修改线下收费单据窗口 -->
	<!-- <div id="updateWin" class="easyui-window" title="费率管理-修改" collapsible="false" minimizable="false" maximizable="false" 
		icon="icon-save" style="padding: 5px; background: #fafafa;">
		<div class="easyui-layout" >
			<div border="false" style="padding: 10px; background: #fff; border: 1px solid #ccc;">
				<form id="ff" class="easyui-form" method="post"  data-options="novalidate:true">
					<table cellpadding=3>
						<tr hidden="true">
							<td>ID:</td>
							<td>
								<input id="id" type="hidden" readonly/>
							</td>
							<td></td>
						</tr>
						<tr>
							<td>支&nbsp;&nbsp;付&nbsp;&nbsp;名&nbsp;&nbsp;称:</td>
							<td>
								<input id="payName" type="text" class="txt01" readonly/>
							</td>
							<td></td>
						</tr>
						<tr>
							<td>商&nbsp;&nbsp;&nbsp;&nbsp;户&nbsp;&nbsp;&nbsp;&nbsp;号:</td>
							<td>
				                 <input id="merchantID" type="text" class="txt01" readonly/>
							</td>
							<td></td>
						</tr>
						<tr>
							<td>支付渠道代码:</td>
							<td>
								<input id="payChannelCode" type="text" class="txt01" readonly/>
							</td>
							<td></td>
						</tr>
						<tr>
							<td>费&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;率&nbsp;&nbsp;:</td>
							<td>
				                 <input id="payRate" class="easyui-textbox" style="width:96%;"
				                 	required="true"  missingMessage="费率为三位小数，且千分位只能是1位1-9的数字" />
							</td>
							<td><span style="color: red;font-size: 20px;">*</span></td>
						</tr>
					</table>
				</form>
			</div>
			<div border="false" style="text-align:center; height: 4%;margin-top:4%;">
				<a class="easyui-linkbutton" icon="icon-ok" href="javascript:void(0)" onclick="submitRate();"> 确定</a> 
				<a class="easyui-linkbutton" icon="icon-cancel" href="javascript:void(0)"  onclick="closeUpdateWin();" style="margin-left:30px;">取消</a>
			</div>
		</div>
	</div> -->
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
			                 <input id="addMoney" class="easyui-textbox" style="width:98%;"
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
							手&nbsp;&nbsp;&nbsp;机&nbsp;&nbsp;&nbsp;号:
						</td>
						<td>
			                 <input id="addPhone" class="easyui-textbox" style="width:98%;"
			                 	name="addPhone"/>
						</td>
					
						<td style="margin-bottom:20px">
							备&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;注:
						</td>
						<td>
			                 <input id="addRemark" class="easyui-textbox" style="width:98%;"
			                 	name="addRemark" />
						</td>
						<!-- <td style="margin-bottom:20px">
							操作人:
						</td>
						<td>
			                 <input id="addOperator" class="easyui-textbox" style="width:98%;"
			                 	name="addOperator"  required="true"  missingMessage="" />
						</td> -->
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
			findOrderOffline();
		});
		
		//设置添加费率窗口
        function addWin() {
            $('#addWin').window({
                title: '线下收费单据-添加',
                width: '50%',
                modal: true,
                shadow: true,
                closed: true,
                height: '90%',
                resizable:false
            });
        }
	
         //关闭添加费率窗口
        function closeAddWin() {
            $('#addWin').window('close');
            //var url = "${pageContext.request.contextPath}/paychannel/getChannelRate";
	      	window.location.reload();
        }
        
        //清空添加表单
        function clearAddForm(){
        	$('#addForm').form('clear');
        }	
		
		//打开线下收费录入窗口
        addWin();
        function openAddWin() {
        	//clearAddForm();
            $('#addWin').window('open');
            
            //加载所有支付方式名称，并且选中支付名称后触发根据该名称查询对应渠道编码的事件
            $('#addChannelId').combobox({
				url:'${pageContext.request.contextPath}/paychannel/findPayNames',
				valueField:'id',
				textField:'text',
				onSelect: function(rec){
					var payChannelName = rec.text;
					if(payChannelName != null && payChannelName != ""){
						var url = '${pageContext.request.contextPath}/paychannel/findPayChannelCode?payChannelName='+payChannelName;
						$('#addPayChannelCode').combobox('reload', url);
					}
				}	
			});
			
			//加载所有商户名
			 $('#addMerchantName').combobox({
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
			//若校验为true 提交
			if(checkAddOrderOffline(addMerchantOrderId,addMoney,addMerchantName,addAppId,addChannelId,addBankCode,addPhone)){
				$.ajax({
					type:"post",
					url:"/pay-platform-manager/manage/submitAddOrderOffline",
					data:{"addMerchantOrderId":addMerchantOrderId,"addMoney":addMoney,"addSourceUserName":addSourceUserName,"addRealName":addRealName,"addPhone":addPhone,"addMerchantName":addMerchantName,"addAppId":addAppId,"addChannelId":addChannelId,"addBankCode":addBankCode,"addRemark":addRemark},
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
			if(addAppId == "" || addAppId == null || addAppId == undefined){
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
		
        //设置修改费率窗口
        /* function updateWin() {
            $('#updateWin').window({
                title: '费率管理-修改',
                width: '23%',
                modal: true,
                shadow: true,
                closed: true,
                height: '60%',
                resizable:false
            });
        } */
        
        //打开修改费率窗口
        /* updateWin();
        function openUpdateWin() {
            $('#updateWin').window('open');
        } */
        
         //关闭修改费率窗口
        /* function closeUpdateWin() {
            $('#updateWin').window('close');
        } */
        
        //修改费率
        /* function updateChannelRate(){  
	        var row = $('#dg').datagrid('getSelected');
			if (row){
				$.messager.confirm('系统提示', '是否确定修改本条数据?', function(r){
					if (r){
						var id=row.id;
					    var merchantID=row.merchantID;
					    var payChannelCode=row.payChannelCode;
					    var payName=row.payName;
					    var payRate=row.payRate;
					    $("#id").val(id);
						$("#merchantID").val(merchantID);
						$("#payChannelCode").val(payChannelCode);
						$("#payName").val(payName);
						$('#payRate').textbox('setValue',payRate);
						openUpdateWin();
					}
		   		});
			}else{
				 msgShow('系统提示', '请选择所要修改的记录行！', 'warning');
			}
	    }; */
      	
      	//手机号格式校验
      	 function checkPhone(addPhone){
      	 	var phone1=/^([1])([1-9]{1}[0-9]{9})?$/;
			if(phone.length!=11 || !phone.match(phone1)){
				$(".mess").text("请填写正确联系方式！");
				return false;
			}
      		return true;
      	} 
      	
      	//提交修改
      	/* function submitRate(){
      		var row = $('#dg').datagrid('getSelected');
			if(row){	
				var id=row.id;
	            var payRate = $("#payRate").val().trim();
	            $('#ff').form('submit',{
					onSubmit:function(){
						return $(this).form('enableValidation').form('validate');
					}
				});
	            var checked_result = checkRate(payRate);
	            if (checked_result) {
		            var url=encodeURI("${pageContext.request.contextPath}/paychannel/submitRate?id="+id+"&payRate="+payRate);
		            $.post(url, function(data) {
		                if(data.result==true){
			                 msgShow('系统提示', '修改成功！', 'info');
			                 closeUpdateWin();
			                 var url = "${pageContext.request.contextPath}/paychannel/getChannelRate";
	      					 reload(url);
		                }else{
			                 msgShow('系统提示', '修改失败,请稍候再试！', 'error');
			                 closeUpdateWin();
	      					 var url = "${pageContext.request.contextPath}/paychannel/getChannelRate";
	      					 reload(url);
		                }
		            });
	            }
      		}
      	} */
      	
		//弹出信息窗口 title:标题 msgString:提示信息 msgType:信息类型 [error,info,question,warning]
		function msgShow(title, msgString, msgType) {
			$.messager.alert(title, msgString, msgType);
		}

		//刪除
		/* function removeChannelRate(){
			var row = $('#dg').datagrid('getSelected');
			if (row){
				$.messager.confirm('系统提示', '是否确定删除?', function(r){
					if (r){
					   var id=row.id;
					   var url='${pageContext.request.contextPath}/paychannel/removeChannelRate?id='+id;
			            $.post(url, function(data) {
			                if(data.result==true){
			                 	msgShow('系统提示', '恭喜，删除成功！', 'info');
			                 	var url = "${pageContext.request.contextPath}/paychannel/getChannelRate";
      							reload(url);
			                }else{
			                  	msgShow('系统提示', '删除失败，请稍候再试！', 'error');
			                  	//刷新
					            var url = "${pageContext.request.contextPath}/paychannel/getChannelRate";
		      					reload(url);
			                }
			            });
					}
				 });
			}else{
				 msgShow('系统提示', '请选择所要删除的记录行！', 'warning');
			}
		} */
		
		//重新加載
		function reload(url){
		$('#dg').datagrid('reload',{
            url: url, method: "post"
          }); 
		}
		
		//查询
		/* function findChannelRates(){
		 	var merchant_id=$("#merchant_id").val().trim();
		 	
		 	var url="${pageContext.request.contextPath}/paychannel/getChannelRate?merchantID="+merchant_id;
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
		} */
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
		
	</script>
</html>