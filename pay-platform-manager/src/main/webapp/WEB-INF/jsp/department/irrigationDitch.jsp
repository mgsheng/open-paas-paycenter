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
			<td style="text-align: right;">渠道名称：</td>
			<td>
				<input class="easyui-textbox" name="CHANNEL_NAME" id="CHANNEL_NAME" style="width:100%">
			</td>
			<td style="text-align: right;width: 130px">渠道状态：</td>
			<td>
				<select class="easyui-combobox" data-options="editable:false" id="CHANNEL_STATUS" name="CHANNEL_STATUS" style="width:100%">
					<option value="">全部</option>
					<option value="1">正常</option>
					<option value="2">锁定</option>
					<option value="3">关闭</option>
				</select>
			</td>
			<td style="text-align: right;width: 150px">商户id：</td>
			<td><input id="MERID" name="MERID" class="easyui-textbox" style="width:100%"></td>
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

    
	
	<table id="dg" class="easyui-datagrid" title="渠道管理" style="width:100%;height:540px"
			data-options="rownumbers:true,singleSelect:true,url:'',method:'get',toolbar:'#tb'">
		<thead>
				<tr>
					<th data-options="field:'id',align:'center'" hidden="true" style="width:15%;max-width:100%;">ID</th>
					<th data-options="field:'channelName',align:'center'" style="width:15%;max-width:100%;">渠道名称</th>
					<th data-options="field:'channelStatusName',align:'center'" style="width:18%;max-width:100%;">渠道状态</th>
					<th data-options="field:'priorityName',align:'center'" style="width:18%;max-width:100%;">优先级</th>
					<th data-options="field:'foundDate',align:'center'" style="width:18%;max-width:100%;">创建时间</th>
					<th data-options="field:'merId',align:'center'" style="width:18%;max-width:100%;">商户id</th>
					<th data-options="field:'keyValue',align:'center'" style="width:18%;max-width:100%;">商户秘钥</th>
					<th data-options="field:'notifyUrl',align:'center'" style="width:18%;max-width:100%;">接口通知地址</th>
					<th data-options="field:'other',align:'center'" style="width:18%;max-width:100%;">其他参数</th>
					<th data-options="field:'paymentType',align:'center'" style="width:18%;max-width:100%;">支付类型</th>
					<th data-options="field:'backurl',align:'center'" style="width:18%;max-width:100%;">商户返回地址</th>
					<th data-options="field:'sighType',align:'center'" style="width:18%;max-width:100%;">签名方式</th>
					<th data-options="field:'type',align:'center'" style="width:18%;max-width:100%;">type</th>
					<th data-options="field:'inputCharset',align:'center'" style="width:18%;max-width:100%;">字符编码格式</th>
					<th data-options="field:'paymentChannel',align:'center'" style="width:18%;max-width:100%;">支付渠道</th>
					<th data-options="field:'mome',align:'center'" style="width:18%;max-width:100%;">备注</th>
				</tr>
		</thead>
	</table>
	<div id="tb" style="padding:2px 5px;">
	   <span style="margin-left: 75%;">
		名称: 
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		<a href="#" class="easyui-linkbutton" iconCls="icon-add" plain="true" id="add"></a>
		<a href="#" class="easyui-linkbutton" iconCls="icon-edit" plain="true" id="edit" onclick="editMessage();"></a>
		<a href="#" class="easyui-linkbutton" iconCls="icon-cut" plain="true" id="delete" onclick="removeit();"></a>
		</span>
	</div>
	<div id="w" class="easyui-window" title="角色添加" collapsible="false"
		minimizable="false" maximizable="false" icon="icon-save"
		style="width: 300px; height: 150px; padding: 5px;
        background: #fafafa;">
		<div class="easyui-layout" fit="true">
			<div border="false"
				style="padding: 5px; background: #fff; border: 1px solid #ccc;">
				<table cellpadding=3>
					<input id="id" type="hidden" />
					<tr style="height: 40px">
						<td>渠道名称：</td>
						<td><input id="channelName" name="channelName" type="text" class="txt01" value=""/>
						</td>
					</tr>
					<tr style="height: 40px">
						<td>渠道状态：</td>
						<td>
						<select class="easyui-combobox" data-options="editable:false" id="channelStatus" name="channelStatus" style="width:100%">
							<option value="1">正常</option>
							<option value="2">锁定</option>
							<option value="3">关闭</option>
						</select>
						</td>
					</tr>
					<tr style="height: 40px">
						<td>渠道优先级：</td>
						<td>
						<select class="easyui-combobox" data-options="editable:false" id="priority" name="priority" style="width:100%">
							<option value="0">高</option>
							<option value="1">中</option>
							<option value="2">低</option>
						</select>
						</td>
					</tr>
					<tr style="height: 40px">
						<td>支付渠道：</td>
						<td><input id="paymentChannel" name="paymentChannel" type="text" class="txt01" value=""/>
						</td>
					</tr>
					<tr style="height: 40px">
						<td>商户ID：</td>
						<td><input id="merId" name="merId" type="text" class="txt01" value=""/>
						</td>
					</tr>
					<tr style="height: 40px">
						<td>商户秘钥：</td>
						<td><input id="keyValue" name="keyValue" type="text" class="txt01" value=""/>
						</td>
					</tr>
					<tr style="height: 40px">
						<td>其他参数：</td>
						<td><input id="other" name="other" type="text" class="txt01" value=""/>
						</td>
					</tr>
					<tr style="height: 40px">
						<td>接口通知地址：</td>
						<td><input id="notifyUrl" name="notifyUrl" type="text" class="txt01" value=""/>
						</td>
					</tr>
					<tr style="height: 40px">
						<td>支付类型：</td>
						<td><input id="paymentType" name="paymentType" type="text" class="txt01" value=""/>
						</td>
					</tr>
					<tr style="height: 40px">
						<td>商户返回地址接口：</td>
						<td><input id="backUrl" name="backUrl" type="text" class="txt01" value=""/>
						</td>
					</tr>
					<tr style="height: 40px">
						<td>type：</td>
						<td><input id="type" name="type" type="text" class="txt01" value=""/>
						</td>
					</tr>
					<tr style="height: 40px">
						<td>签名方式：</td>
						<td><input id="sighType" name="sighType" type="text" class="txt01" value=""/>
						</td>
					</tr>
					<tr style="height: 40px">
						<td>字符编码格式：</td>
						<td><input id="inputCharset" name="inputCharset" type="text" class="txt01" value=""/>
						</td>
					</tr>
					<tr style="height: 40px">
						<td>备注：</td>
						<td><input id="memo" name="memo" type="text" class="txt01" value=""/>
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
                height: 700,
                resizable:false
            });
           
        }
        //关闭登录窗口
        function closePwd() {
            $('#w').window('close');
        }
        //添加
        function serverLogin() {
        	
        	var checkIds='';
        	var bool=false;
        	var ui = $('#deptree1').tree('getChecked', ['checked','indeterminate']);
        	for(var i = 0;i<ui.length;i++){
       			if(i>0){
       				//模块节点(ismodule自定义参数=0标记的是模块)
       				if(ui[i].ismodule=="0"){
       					checkIds+=",,,";//模块与模块区分
       					bool=false;
       				}else if(bool){
       					checkIds+=",";//资源与资源区分
       				}else{
       					checkIds+=",,";//模块与资源区分
       					bool=true;
       				}
        		}
       		    //去掉带r标示的id（用于区分资源和模块id）
    			checkIds+=ui[i].id.replace('r','');
        	}
        	//alert(checkIds);
        	
        	
        	var id = $('#id').val();
            var $resourceName = $('#resourceName');
            var $status= $('#status');
            if ($resourceName.val() == '') {
                msgShow('系统提示', '请输入名称！', 'warning');
                return false;
            }
            var url=encodeURI('${pageContext.request.contextPath}/managerRole/addRole?name='+$resourceName.val()+'&status='+$status.val()+'&id='+id+'&temp='+checkIds);
            $.post(url, function(data) {
                if(data.returnMsg=='1'){
	                 msgShow('系统提示', '恭喜，添加成功！', 'info');
	                 close();
	                $('#w').window('close');
	                //刷新
				      var url='${pageContext.request.contextPath}/managerRole/QueryRoleMessage';
				      reload(url,name);
                }else if(data.returnMsg=='2'){
                	msgShow('系统提示', '修改成功！', 'info');
	                 close();
	                $('#w').window('close');
	                //刷新
				      var url='${pageContext.request.contextPath}/managerRole/QueryRoleMessage';
				      reload(url,name);
                }else{
                	msgShow('系统提示', '角色已存在！', 'info');
	                 $newpass.val('');
	                 $rePass.val('');
	                 close();
                }
            });
        }
        
      //修改
        function serverUpdate() {
        	var id = $.trim($('#id').val()) ;
    		var channelName = $.trim($('#channelName').val()) ;
    		var channelStatus = $("input[name='channelStatus']").val();
    		var merId = $.trim($('#merId').val()) ;
    		var keyValue = $.trim($('#keyValue').val()) ;
    		var priority = $("input[name='priority']").val();
    		var paymentChannel = $.trim($('#paymentChannel').val()) ;
    		var notifyUrl = $.trim($('#notifyUrl').val()) ;
    		var other = $.trim($('#other').val()) ;
    		var paymentType = $.trim($('#paymentType').val()) ;
    		var backUrl = $.trim($('#backUrl').val()) ;
    		var type = $.trim($('#type').val()) ;
    		var sighType = $.trim($('#sighType').val()) ;
    		var inputCharset = $.trim($('#inputCharset').val()) ;
    		var memo = $.trim($('#memo').val()) ;
    		if(channelName==""){
    			alert("渠道名不能为空！");
    		}
    		
            var url= "";
            if(id==''){
            	url=encodeURI('${pageContext.request.contextPath}/irrigation/addDictTrade?id='+id+'&channelName='+channelName+'&channelStatus='+channelStatus+'&merId='+merId+'&keyValue='+keyValue+'&priority='+priority+'&paymentChannel='+paymentChannel+'&notifyUrl='+notifyUrl+'&other='+other+'&paymentType='+paymentType+'&backUrl='+backUrl+'&type='+type+'&sighType='+sighType+'&inputCharset='+inputCharset+'&memo='+memo);
            }else{
            	url=encodeURI('${pageContext.request.contextPath}/irrigation/updateDictTrade?id='+id+'&channelName='+channelName+'&channelStatus='+channelStatus+'&merId='+merId+'&keyValue='+keyValue+'&priority='+priority+'&paymentChannel='+paymentChannel+'&notifyUrl='+notifyUrl+'&other='+other+'&paymentType='+paymentType+'&backUrl='+backUrl+'&type='+type+'&sighType='+sighType+'&inputCharset='+inputCharset+'&memo='+memo);
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
		        url: "${pageContext.request.contextPath}/irrigation/findIrrigation?name="+name,  
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
			 var CHANNEL_NAME=$("#CHANNEL_NAME").val();
			 var CHANNEL_STATUS = $("input[name='CHANNEL_STATUS']").val();
			 var MERID=$("#MERID").val();
			 var url=encodeURI("${pageContext.request.contextPath}/irrigation/findIrrigation?CHANNEL_NAME="+CHANNEL_NAME+"&CHANNEL_STATUS="+CHANNEL_STATUS+"&MERID="+MERID);
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