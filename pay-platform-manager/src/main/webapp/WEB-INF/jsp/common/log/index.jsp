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

<div class="easyui-panel" title="查询条件" style="width:100%;height:140px;padding: 20px 30px; ">
	<form id="ff" method="post">
	<table >
		<tr style="padding-top:50px;">
			<td style="text-align: right;">操作人：</td>
			<td>
				<input class="easyui-textbox" name="operator" id="operator" style="width:100%">
			</td>
			
			<td style="text-align: right;width: 150px">操作：</td>
			<td><input id="operationContent" name="operationContent" class="easyui-textbox" style="width:100%"></td>
		</tr>
		
		<tr><td></td><td></td><td></td>
			<td style="text-align: right;"><a href="javascript:void(0)" class="easyui-linkbutton" onclick="onsearch()" style="width:80px">提交</a></td>
			<td>
				<a href="javascript:void(0)" class="easyui-linkbutton" onclick="clearForm()" style="width:80px">清空</a>
			</td>
		</tr>
		
		
		</table>
	
	</form>
	</div>

    
	
	<table id="dg" class="easyui-datagrid" title="日志管理" style="width:100%;height:540px"
			data-options="rownumbers:true,singleSelect:true,url:'',method:'get',toolbar:'#tb'">
		<thead>
				<tr>
					<th data-options="field:'id',align:'center'" hidden="true" style="width:15%;max-width:100%;">ID</th>
					<th data-options="field:'operator',align:'center'" style="width:15%;max-width:100%;">操作人</th>
					<th data-options="field:'operatorId',align:'center'" style="width:15%;max-width:100%;">操作人ID</th>
					<th data-options="field:'oneLevels',align:'center'" style="width:13%;max-width:100%;">一级操作</th>
					<th data-options="field:'towLevels',align:'center'" style="width:10%;max-width:100%;">二级操作</th>
					<th data-options="field:'operationContent',align:'center'" style="width:18%;max-width:100%;">操作</th>
					<th data-options="field:'operationAuthority',align:'center'" style="width:8%;max-width:100%;">操作权限</th>
					<th data-options="field:'foundDate',align:'center'" style="width:18%;max-width:100%;">操作时间</th>
					<th data-options="field:'operationDescribe',align:'center'" style="width:18%;max-width:100%;">操作描述</th>
					
				</tr>
		</thead>
	</table>
	<div id="tb" style="padding:2px 5px;">
	   <span style="margin-left: 75%;">
		 
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		<!--  --><a href="#" class="easyui-linkbutton" iconCls="icon-add" plain="true" id="add"></a> 
		<a href="#" class="easyui-linkbutton" iconCls="icon-edit" plain="true" id="edit" onclick="editMessage();"></a>
		<a href="#" class="easyui-linkbutton" iconCls="icon-cut" plain="true" id="delete" onclick="removeit();"></a>
		</span>
	</div>
	<div id="w" class="easyui-window" title="日志添加" collapsible="false"
		minimizable="false" maximizable="false" icon="icon-save"
		style="width: 300px; height: 150px; padding: 5px;
        background: #fafafa;">
		<div class="easyui-layout" fit="true">
			<div border="false"
				style="padding: 5px; background: #fff; border: 1px solid #ccc;">
				<table cellpadding=3>
					<input id="id" type="hidden" />
					
					<tr style="height: 40px">
						<td>一级操作：</td>
						<td>
						<input id="oneLevels" name="oneLevels" type="text" class="txt01" value=""/>
						</td>
					</tr>
					<tr style="height: 40px">
						<td>二级操作：</td>
						<td><input id="towLevels" name="towLevels" type="text" class="txt01" value=""/>
						</td>
					</tr>
					<tr style="height: 40px">
						<td>操作权限：</td>
						<td><input id="operationAuthority" name="operationAuthority" type="text" class="txt01" value=""/>
						</td>
					</tr>
					
					
				</table>
			</div>
			<div region="south" border="false"
				style="text-align:center; height: 30px; line-height: 30px;">
				<a id="btnEp" class="easyui-linkbutton" icon="icon-ok"
					href="javascript:void(0)"> 确定</a> <a id="btnCancel"
					class="easyui-linkbutton" icon="icon-cancel"
					href="javascript:void(0)">取消</a>
			</div>
		</div>
	</div>
</body>

<script>
         //设置登录窗口
        function openPwd() {
            $('#w').window({
                title: '渠道添加',
                width: 400,
                modal: true,
                shadow: true,
                closed: true,
                height: 400,
                resizable:false
            });
           
        }
        //关闭登录窗口
        function closePwd() {
            $('#w').window('close');
        }
        
      //添加
        function serverUpdate() {
        	var id = $.trim($('#id').val()) ;
    		
    		var oneLevels = $.trim($('#oneLevels').val()) ;
    		var towLevels = $.trim($('#towLevels').val()) ;
    		var operationAuthority = $.trim($('#operationAuthority').val()) ;
   
    	
            var url= "";
            if(id==''){
            	url=encodeURI('${pageContext.request.contextPath}/privilegeLog/addPrivilegeLog?id='+id+'&oneLevels='+oneLevels+'&towLevels='+towLevels+'&operationAuthority='+operationAuthority);
            }else{
            	url=encodeURI('${pageContext.request.contextPath}/privilegeLog/addPrivilegeLog?id='+id+'&oneLevels='+oneLevels+'&towLevels='+towLevels+'&operationAuthority='+operationAuthority);
            }
             $.post(url, function(data) {
            	 if(data.returnMsg=='1'){
	                 msgShow('系统提示', '恭喜，添加成功！', 'info');
	                 close();
	                $('#w').window('close');
	                //刷新
				      var url='${pageContext.request.contextPath}/irrigation/findIrrigation';
				      reload(url,name);
                }else if(data.returnMsg=='2'){
                	msgShow('系统提示', '修改成功！', 'info');
	                 close();
	                $('#w').window('close');
	                //刷新
				      var url='${pageContext.request.contextPath}/irrigation/findIrrigation';
				      reload(url,name);
                }else{
                	msgShow('系统提示', '渠道操作失败！', 'info');
	                 $newpass.val('');
	                 $rePass.val('');
	                 close();
                }
            });
        }
      
      
		//弹出信息窗口 title:标题 msgString:提示信息 msgType:信息类型 [error,info,question,warning]
		function msgShow(title, msgString, msgType) {
			$.messager.alert(title, msgString, msgType);
		}
       $(function(){  
       var name=$("#name").val();
        $('#dg').datagrid({
				collapsible:true,
				rownumbers:true,
				pagination:true,
		        url: "${pageContext.request.contextPath}/privilegeLog/findPrivilegeLog",  
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
		     openPwd();
		     $('#add').click(function() {
		    	 	$('#id').val("");
					$('#channelName').val("");
					$('#channelStatus').val("");
					$('#priority').val("");
					$('#paymentChannel').val("");
					$('#merId').val("");
					$('#keyValue').val("");
					$('#notifyUrl').val("");
					$('#other').val("");
					$('#paymentType').val("");
					$('#backUrl').val("");
					$('#type').val("");
					$('#sighType').val("");
					$('#inputCharset').val("");
					$('#memo').val("");
                $('#w').window('open');
            });
            $('#btnEp').click(function() {
            	serverUpdate();
            });
			$('#btnCancel').click(function(){closePwd();});
		    });
		  
        function getSelected(){
			var row = $('#dg').datagrid('getSelected');
			if (row){
				$.messager.alert('Info', row.itemid+":"+row.productid+":"+row.attr1);
			}
		}
        
        function editMessage(){
   			var row = $('#dg').datagrid('getSelected');
	   		if(row==null){
	   			msgShow('系统提示', '请选中要修改的数据', 'info');
	   		}
   			if (row){
   			$.messager.confirm('系统提示', '是否确定修改本条数据?', function(r){
   				if (r){
   					
   					var id = row.id;
   					var channelName = row.channelName;
   					var channelStatus = row.channelStatus;
   					var priority = row.priority;
   					var paymentChannel = row.paymentChannel;
   					var merId = row.merId;
   					var keyValue = row.keyValue;
   					var notifyUrl = row.notifyUrl;
   					var other = row.other;
   					var paymentType = row.paymentType;
   					var backUrl = row.backUrl;
   					var type = row.type;
   					var sighType = row.sighType;
   					var inputCharset = row.inputCharset;
   					var memo = row.memo;
   					
   					$('#id').val(id);
   					$('#channelName').val(channelName);
   					$('#channelStatus').val(channelStatus);
   					$('#priority').val(priority);
   					$('#paymentChannel').val(paymentChannel);
   					$('#merId').val(merId);
   					$('#keyValue').val(keyValue);
   					$('#notifyUrl').val(notifyUrl);
   					$('#other').val(other);
   					$('#paymentType').val(paymentType);
   					$('#backUrl').val(backUrl);
   					$('#type').val(type);
   					$('#sighType').val(sighType);
   					$('#inputCharset').val(inputCharset);
   					$('#memo').val(memo);
  					
   					$('#w').window({
		                title: '渠道修改',
		                width: 400,
		                modal: true,
		                shadow: true,
		                closed: true,
		                height: 700,
		                resizable:false
		            });
  					$('#w').window('open');
   				}
   			   });
   			}
   		}
		
		function removeit(){
		var row = $('#dg').datagrid('getSelected');
			if (row){
			$.messager.confirm('系统提示', '是否确定删除?', function(r){
				if (r){
					   var id=row.id;
					   var url="${pageContext.request.contextPath}/irrigation/removeDictTradeID?id="+id;
			            $.post(url, function(data) {
			                if(data.result==true){
			                 	msgShow('系统提示', '恭喜，删除成功！', 'info');
			                 	var url='${pageContext.request.contextPath}/irrigation/findCommercial';
					              reload(url);
			                }else{
			                  	msgShow('系统提示', '删除失败！', 'error');
			                }
			            });
				}
			   });
			}
		}
		function reload(url,name){
		$('#dg').datagrid('reload',{
            url: url, queryParams:{ name:name}, method: "post"
          }); 
		}
		
		function onsearch(){
			 var operator=$("#operator").val();
			 var operationContent=$("#operationContent").val();
			 var url=encodeURI("${pageContext.request.contextPath}/privilegeLog/findPrivilegeLog?operator="+operator+"&operationContent="+operationContent);
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
		
		function clearForm(){
			$('#ff').form('clear');
		}
	</script>
</html>