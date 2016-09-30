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
	<table id="dg" class="easyui-datagrid" title="渠道费率管理" style="height:400px;width:100%" 
			data-options="rownumbers:true,singleSelect:true,striped:true,fitColumns:true,method:'get',toolbar:'#tb'">
		<thead>
			<tr>
			    <th data-options="field:'id',align:'center'"  style="width:25%;" hidden="true">ID</th>
				<th data-options="field:'merchantID',align:'center'" style="width:25%;" >商户号</th>
				<th data-options="field:'payChannelCode',align:'center'" style="width:25%;" >支付渠道代码</th>
				<th data-options="field:'payName',align:'center'" style="width:25%;" >支付名称</th>
				<th data-options="field:'payRate',align:'center'" style="width:25%;" >费率</th>
			</tr>
		</thead>
	</table>
	<div id="tb" style="padding:0.4% 3%; text-align: right;">
		商户号: 
		<input class="easyui-textbox" name="merchant_id" id="merchant_id" style="width:8%;" prompt="选填">
		&nbsp;&nbsp;&nbsp;&nbsp;
		<a href="#" class="easyui-linkbutton" iconCls="icon-search " plain="true" onclick="findChannelRates();"></a>
		<a href="#" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="openAddWin();"></a>
		<a href="#" class="easyui-linkbutton" iconCls="icon-edit" plain="true" onclick="updateChannelRate();"></a>
		<span hidden="true">
			<a href="#" class="easyui-linkbutton" iconCls="icon-cut" plain="true" onclick="removeChannelRate();"></a>
		</span>
	</div>
	<!-- 修改费率窗口 -->
	<div id="updateWin" class="easyui-window" title="费率管理-修改" collapsible="false" minimizable="false" maximizable="false" 
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
	</div>
	<!-- 添加渠道费率窗口 -->
	<div id="addWin" class="easyui-window" title="费率管理-添加" collapsible="false" minimizable="false" maximizable="false" 
		style=" background: #fafafa;">
		<div border="false" style="padding: 20px; background: #fff; border: 1px solid #ccc;">
			<form id="addForm" class="easyui-form" method="post"  data-options="novalidate:true">
				<table cellpadding=3>
					<tr>
						<td style="margin-bottom:20px">
							支&nbsp;&nbsp;付&nbsp;&nbsp;名&nbsp;&nbsp;称:
						</td>
						<td style="width:80%;">	
							<select class="easyui-combobox" data-options="editable:false,prompt:'请选择支付名称'" id="addPayName" 
								name="addPayName"  style="width:100%;height:30px;padding:5px;">
							</select>
						</td>
					</tr>
					<tr>
						<td style="margin-bottom:20px">
							商&nbsp;&nbsp;&nbsp;&nbsp;户&nbsp;&nbsp;&nbsp;&nbsp;名:
						</td>
						<td style="width:80%;">	
							<select class="easyui-combobox" data-options="editable:false,prompt:'请选择商户名'" id="addMerchantName" 
								name="addMerchantName"  style="width:100%;height:30px;padding:5px;">
							</select>
						</td>
					</tr>
					<tr>
						<td style="margin-bottom:20px">
							支付渠道代码:
						</td>
						<td style="width:80%;">	
							<select class="easyui-combobox" data-options="editable:false,prompt:'请选择支付渠道代码'" id="addPayChannelCode" 
								name="addPayChannelCode"  style="width:100%;height:30px;padding:5px;">
							</select>
						</td>
					</tr>
					<tr>
						<td style="margin-bottom:20px">费&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;率&nbsp;&nbsp;:</td>
						<td>
			                 <input id="addPayRate" class="easyui-textbox" style="width:98%;"
			                 	name="addPayRate"  required="true"  missingMessage="费率为三位小数，且千分位只能是1位1-9的数字" />
						</td>
					</tr>
				</table>
			</form>
		</div>
		<div border="false" style="text-align:center; height: 3%;margin-top:4%;">
			<a class="easyui-linkbutton" icon="icon-ok" href="javascript:void(0)" onclick="submitAddChannelRate();"> 确定</a>
			<a class="easyui-linkbutton" icon="icon-clear" href="javascript:void(0)" onclick="clearAddForm()" style="margin-left:30px;">清空</a> 
			<a class="easyui-linkbutton" icon="icon-cancel" href="javascript:void(0)"  onclick="closeAddWin();" style="margin-left:30px;">取消</a>
		</div>
	</div>
</body>
<script>
		//页面预加载
		$(function(){
			findChannelRates();
		});
		
		//设置添加费率窗口
        function addWin() {
            $('#addWin').window({
                title: '费率管理-添加',
                width: '30%',
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
            //var url = "${pageContext.request.contextPath}/paychannel/getChannelRate";
	      	window.location.reload();
        }
        
        //清空添加表单
        function clearAddForm(){
        	$('#addForm').form('clear');
        }	
		
		//打开添加费率窗口
        addWin();
        function openAddWin() {
        	//clearAddForm();
            $('#addWin').window('open');
            
            //加载所有支付名称，并且选中支付名称后触发根据该名称查询对应渠道编码的事件
            $('#addPayName').combobox({
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
			
			//对应的渠道代码
			 $('#addPayChannelCode').combobox({
				url:'${pageContext.request.contextPath}/paychannel/findPayChannelCode',
				valueField:'id',
				textField:'text'
			});
			
			var addPayChannelCode = $('#addPayChannelCode').combobox('getText');
			$('#addPayChannelCode').combobox('setText',addPayChannelCode);
			
        }
        
		//提交添加
		function submitAddChannelRate(){
			$('#addForm').form('submit',{
				onSubmit:function(){
					return $(this).form('enableValidation').form('validate');
				}
			});
			var addPayName = $('#addPayName').combobox('getText');
			var addMerchantID= $('#addMerchantName').combobox('getValue');
			var addPayChannelCode = $('#addPayChannelCode').combobox('getText');
			var payRate = $("#addPayRate").textbox('getValue'); 
			
			//若校验为true 提交
			if(checkAddChannelRate(addPayName,addMerchantID,addPayChannelCode,payRate)){
				$.ajax({
					type:"post",
					url:"/pay-platform-manager/paychannel/submitAddChannelRate",
					data:{"addPayName":addPayName,"addMerchantID":addMerchantID,"addPayChannelCode":addPayChannelCode,"payRate":payRate},
					dataType:"json",
					success:function (data){
						if(data.result == 1){
							$.messager.alert("系统提示","恭喜，添加成功!","info");
							clearAddForm();
							closeAddWin();
							var url = "${pageContext.request.contextPath}/paychannel/getChannelRate";
							//window.location.reload();
							reload(url);
						}else if(data.result == 2){
							clearAddForm();
							$.messager.alert("系统提示","该记录已被添加，请修改原纪录或重新填写!","error");	
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
		function checkAddChannelRate(addPayName,addMerchantID,addPayChannelCode,payRate){
			if(addPayName == "" || addPayName == null || addPayName == undefined){
					$.messager.alert("系统提示","请选择支付名称！","error");	
					return false;
			}
			if(addMerchantID == "" || addMerchantID == null || addMerchantID == undefined){
					$.messager.alert("系统提示","请选择商户名！","error");		
					return false;
			}
			if(addPayChannelCode == "" || addPayChannelCode == null || addPayChannelCode == undefined){
					$.messager.alert("系统提示","请选择支付渠道代码！","error");		
					return false;
			}
			if(!checkRate(payRate)){
				return false;
			};
			return true;
		}
		
        //设置修改费率窗口
        function updateWin() {
            $('#updateWin').window({
                title: '费率管理-修改',
                width: '23%',
                modal: true,
                shadow: true,
                closed: true,
                height: '60%',
                resizable:false
            });
        }
        
        //打开修改费率窗口
        updateWin();
        function openUpdateWin() {
            $('#updateWin').window('open');
        }
        
         //关闭修改费率窗口
        function closeUpdateWin() {
            $('#updateWin').window('close');
        }
        
        //修改费率
        function updateChannelRate(){  
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
	    };
      	
      	//费率格式校验
      	function checkRate(payRate){
      		var regex_payRate = /^[0]\.[0]{2}[1-9]$/;
      		if(payRate==null || payRate == "" || payRate == undefined){
      			msgShow('系统提示', '请输入费率！', 'warning');
      			return false;
      		}
      		if(regex_payRate.test(payRate) != true){
      			msgShow('系统提示', '输入费率格式不正确或费率值过大或过小！', 'warning');
      			return false;
      		}
      		return true;
      	}
      	
      	//提交修改
      	function submitRate(){
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
      	}
      	
		//弹出信息窗口 title:标题 msgString:提示信息 msgType:信息类型 [error,info,question,warning]
		function msgShow(title, msgString, msgType) {
			$.messager.alert(title, msgString, msgType);
		}

		//刪除
		function removeChannelRate(){
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
		}
		
		//重新加載
		function reload(url){
		$('#dg').datagrid('reload',{
            url: url, method: "post"
          }); 
		}
		
		//查询
		function findChannelRates(){
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
		}
		
		
	</script>
</html>