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
	<style type="text/css">
		.txt01{
			 background-color: #eee;
		}
	</style>
</head>
<body style="height:100%">
	<table id="dg" class="easyui-datagrid" title="渠道费率管理" style="height:130%;width:100%" 
			data-options="rownumbers:true,singleSelect:true,striped:true,fitColumns:true,method:'get',toolbar:'#tb'">
		<thead>
			<tr>
			    <th data-options="field:'id',align:'center'"  style="width:25%;" hidden="true">ID</th>
				<th data-options="field:'merchantID',align:'center'" style="width:25%;" >商户ID</th>
				<th data-options="field:'payChannelCode',align:'center'" style="width:25%;" >支付渠道代码</th>
				<th data-options="field:'payName',align:'center'" style="width:25%;" >支付名称</th>
				<th data-options="field:'payRate',align:'center'" style="width:25%;" >费率</th>
			</tr>
		</thead>
	</table>
	<div id="tb" style="padding:0.4% 3%; text-align: right;">
		支付名称: 
		<input class="easyui-textbox" name="payname" id="payname" style="width:8%;">
		&nbsp;&nbsp;&nbsp;&nbsp;
		<a href="#" class="easyui-linkbutton" iconCls="icon-search " plain="true" onclick="findRates();"></a>
		<a href="#" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="addRates();"></a>
		<a href="#" class="easyui-linkbutton" iconCls="icon-edit" plain="true" onclick="updateRate();"></a>
		<a href="#" class="easyui-linkbutton" iconCls="icon-cut" plain="true" onclick="removeByID();"></a>
	</div>
	<!-- 修改费率窗口 -->
	<div id="updateWin" class="easyui-window" title="费率管理" collapsible="false" minimizable="false" maximizable="false" 
		style="padding: 5px; background: #fafafa;">
		<div class="easyui-layout" >
			<div border="false" style="padding: 10px; background: #fff; border: 1px solid #ccc;">
				<table cellpadding=3>
					<tr hidden="true">
						<td>ID:</td>
						<td>
							<input id="id" type="hidden" readonly/>
						</td>
					</tr>
					<tr>
						<td>支&nbsp;&nbsp;付&nbsp;&nbsp;名&nbsp;&nbsp;称:</td>
						<td>
							<input id="payName" type="text" class="txt01" readonly/>
						</td>
					</tr>
					<tr>
						<td>商&nbsp;&nbsp;&nbsp;&nbsp;户&nbsp;&nbsp;&nbsp;&nbsp;ID:</td>
						<td>
			                 <input id="merchantID" type="text" class="txt01" readonly/>
						</td>
					</tr>
					<tr>
						<td>支付渠道代码:</td>
						<td>
							<input id="payChannelCode" type="text" class="txt01" readonly/>
						</td>
					</tr>
					<tr>
						<td>费&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;率&nbsp;&nbsp;:</td>
						<td>
			                 <input id="payRate" type="text" />
			                 <span style="color: red;font-size: 20px;">*</span>
						</td>
					</tr>
				</table>
			</div>
			<div border="false" style="text-align:center; height: 3%;margin-top:4%;">
				<a id="btnEp" class="easyui-linkbutton" icon="icon-ok" href="javascript:void(0)" onclick="submitRate();"> 确定</a> 
				<a id="btnCancel" class="easyui-linkbutton" icon="icon-cancel" href="javascript:void(0)"  onclick="closeUpdateWin();" style="margin-left:30px;">取消</a>
			</div>
		</div>
	</div>
</body>
<script>
        //设置修改费率窗口
        function updateWin() {
            $('#updateWin').window({
                title: '费率管理',
                width: '23%',
                modal: true,
                shadow: true,
                closed: true,
                height: '50%',
                resizable:false
            });
        }
        
        //打开修改费率窗口
        function openUpdateWin() {
            $('#updateWin').window('open');
        }
        
         //关闭修改费率窗口
        function closeUpdateWin() {
            $('#updateWin').window('close');
        }
        
        //修改费率
        function updateRate(){  
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
						$("#payRate").val(payRate);
						updateWin();
						openUpdateWin();
					}
		   		});
			}else{
				 msgShow('系统提示', '请选择要修改的行！', 'error');
			}
	    };
      	
      	//费率格式校验
      	function checkRate(payRate){
      		regex_payRate = / /;
      	}
      	
      	//提交修改
      	function submitRate(){
      		var row = $('#dg').datagrid('getSelected');
			if(row){	
				var id=row.id;
	            var payRate = $("#payRate").val().trim();
	            alert(payRate);
	            if (payRate == '') {
	                msgShow('系统提示', '请输入费率！', 'warning');
	                return false;
	            }
	            var url=encodeURI("${pageContext.request.contextPath}/paychannel/submitRate?id="+id+"&payRate="+payRate);
	            $.post(url, function(data) {
	                if(data.result==true){
		                 msgShow('系统提示', '修改成功！', 'info');
		                 closeUpdateWin();
      					 findRates();
	                }else{
		                 msgShow('系统提示', '修改失败,请稍候再试！', 'error');
		                 closeUpdateWin();
      					 findRates();
	                }
	            });
      		}
      	}
      	
		//弹出信息窗口 title:标题 msgString:提示信息 msgType:信息类型 [error,info,question,warning]
		function msgShow(title, msgString, msgType) {
			$.messager.alert(title, msgString, msgType);
		}

		//刪除 
		function removeByID(){
			var row = $('#dg').datagrid('getSelected');
			if (row){
				$.messager.confirm('系统提示', '是否确定删除?', function(r){
					if (r){
						   var id=row.id;
						   var url='${pageContext.request.contextPath}/paychannel/removeByID?id='+id;
				            $.post(url, function(data) {
				                if(data.returnMsg==true){
				                 	msgShow('系统提示', '恭喜，删除成功！', 'info');
				                }else{
				                  msgShow('系统提示', '删除失败！', 'info');
				                }
				            });
				            //刷新
				            findRates();
					}
				 });
			}
		}
		
		//重新加載
		function reload(url,name){
		$('#dg').datagrid('reload',{
            url: url, queryParams:{ name:name}, method: "post"
          }); 
		}
		
		//查询
		function findRates(){
		 	var payName=$("#payname").val();
		 	if(payName == null || payName == "" || payName == undefined){
		 	 	payName = null;
		 	}
		 	var url=encodeURI("${pageContext.request.contextPath}/paychannel/getRate?payName="+payName);
        	$('#dg').datagrid({
				collapsible:true,
				rownumbers:true,
				pagination:true,
		        url: url,  
		        pagination: true,
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
		
		//页面预加载
		$(function(){
			findRates();
		});
	</script>
</html>