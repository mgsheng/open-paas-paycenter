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
		<tr style="text-align: center; width: 20px" >
			<td style="text-align: right;">商户名：</td>
			<td>
				<input class="easyui-textbox" name="MERCHANT_NAME" id="MERCHANT_NAME" style="width:100%">
			</td>
			<td style="text-align: right; width: 130px">状态：</td>
			<td>
				<select class="easyui-combobox" data-options="editable:false" id="STATUS" name="STATUS" style="width:100%">
					<option value="">全部</option>
					<option value="1">正常</option>
					<option value="2">冻结</option>
					<option value="3">注销</option>
				</select>
			</td>
			<td style="text-align: right;width: 150px">操作员：</td>
			<td><input id="OPERATER" name="OPERATER" class="easyui-textbox" style="width:100%"></td>
		</tr>
		<tr>
			<td style="text-align: right;">商品名称：</td>
			<td><input id="PRODUCT_NAME" name="PRODUCT_NAME" class="easyui-textbox" style="width:100%"></td>
			<td style="text-align: right;">联系人：</td>
			<td><input id="CONTACT" name="CONTACT" class="easyui-textbox" style="width:100%"></td>
			
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
    
	
	<table id="dg" class="easyui-datagrid" title="商户管理" style="width:100%;height:540px"
			data-options="rownumbers:true,singleSelect:true,url:'',method:'get',toolbar:'#tb'">
		<thead>
				<tr>
					<th data-options="field:'id',align:'center'" hidden="true" style="width:15%;max-width:100%;">ID</th>
					<th data-options="field:'merchantName',align:'center'" style="width:15%;max-width:100%;">商&nbsp;&nbsp;户&nbsp;&nbsp;名</th>
					<th data-options="field:'statusName',align:'center'" style="width:18%;max-width:100%;">状态</th>
					<th data-options="field:'foundDate',align:'center'" style="width:18%;max-width:100%;">创建时间</th>
					<th data-options="field:'operater',align:'center'" style="width:18%;max-width:100%;">操作员</th>
					<th data-options="field:'notifyUrl',align:'center'" style="width:18%;max-width:100%;">服务器通知地址</th>
					<th data-options="field:'returnUrl',align:'center'" style="width:18%;max-width:100%;">页面返回地址</th>
					<th data-options="field:'payKey',align:'center'" style="width:18%;max-width:100%;">商户验证密钥</th>
					<th data-options="field:'productName',align:'center'" style="width:18%;max-width:100%;">商品名称</th>
					<th data-options="field:'contact',align:'center'" style="width:18%;max-width:100%;">联系人</th>
					<th data-options="field:'phone',align:'center'" style="width:18%;max-width:100%;">联系电话</th>
					<th data-options="field:'mobile',align:'center'" style="width:18%;max-width:100%;">手机号</th>
					<th data-options="field:'email',align:'center'" style="width:18%;max-width:100%;">邮箱</th>
					<th data-options="field:'dayNorm',align:'center'" style="width:18%;max-width:100%;">日限额</th>
					<th data-options="field:'monthNorm',align:'center'" style="width:18%;max-width:100%;">月限额</th>
					<th data-options="field:'singleNorm',align:'center'" style="width:18%;max-width:100%;">单笔限额</th>
					<th data-options="field:'memo',align:'center'" style="width:18%;max-width:100%;">备注</th>
					
				</tr>
		</thead>
	</table>
	<div id="tb" style="padding:2px 5px;">
	   <span style="margin-left: 75%;">
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		<a href="#" class="easyui-linkbutton" iconCls="icon-add" plain="true" id="add"></a>
		<a href="#" class="easyui-linkbutton" iconCls="icon-edit" plain="true" id="edit" onclick="editMessage();"></a>
		<a href="#" class="easyui-linkbutton" iconCls="icon-cut" plain="true" id="delete" onclick="removeit();"></a>
		</span>
	</div>
	<div id="w" class="easyui-window" title="商户添加" collapsible="false"
		minimizable="false" maximizable="false" icon="icon-save"
		style="width: 300px; height: 150px; padding: 5px;
        background: #fafafa;">
		<div class="easyui-layout" fit="true">
			<div border="false"
				style="padding: 5px; background: #fff; border: 1px solid #ccc;">
				<table cellpadding=3>
					<input id="tab" type="hidden" />
					<tr style="height: 40px">
						<td>商户名：</td>
						<td><input id="merchantName" name="merchantName" type="text" class="txt01" value=""/>
						</td>
					</tr>
					<tr style="height: 40px">
						<td>状态：</td>
						<td>
						<select class="easyui-combobox" data-options="editable:false" id="status" name="status" style="width:100%">
							<option value="1">正常</option>
							<option value="2">冻结</option>
							<option value="3">注销</option>
						</select>
						
						</td>
					</tr>
					<tr style="height: 40px">
						<td>服务器通知地址：</td>
						<td><input id="notifyUrl" name="notifyUrl" type="text" class="txt01" value=""/>
						</td>
					</tr>
					<tr style="height: 40px">
						<td>页面返回地址：</td>
						<td><input id="returnUrl" name="returnUrl" type="text" class="txt01" value=""/>
						</td>
					</tr>
					<tr style="height: 40px">
						<td>商品名称：</td>
						<td><input id="productName" name="productName" type="text" class="txt01" value=""/>
						</td>
					</tr>
					<tr style="height: 40px">
						<td>联系人：</td>
						<td><input id="contact" name="contact" type="text" class="txt01" value=""/>
						</td>
					</tr>
					<tr style="height: 40px">
						<td>联系电话：</td>
						<td><input id="phone" name="phone" type="text" class="txt01" value=""/>
						</td>
					</tr>
					<tr style="height: 40px">
						<td>手机号：</td>
						<td><input id="mobile" name="mobile" type="text" class="txt01" value=""/>
						</td>
					</tr>
					<tr style="height: 40px">
						<td>邮箱：</td>
						<td><input id="email" name="email" type="text" class="txt01" value=""/>
						</td>
					</tr>
					<tr style="height: 40px">
						<td>日限额：</td>
						<td><input id="dayNorm" name="dayNorm" type="text" class="txt01" value=""/>
						</td>
					</tr>
					<tr style="height: 40px">
						<td>月限额：</td>
						<td><input id="monthNorm" name="monthNorm" type="text" class="txt01" value=""/>
						</td>
					</tr>
					<tr style="height: 40px">
						<td>单笔限额：</td>
						<td><input id="singleNorm" name="singleNorm" type="text" class="txt01" value=""/>
						</td>
					</tr>
					<tr style="height: 40px">
						<td>备注：</td>
						<td><input id="memo" name="memo" type="text" class="txt01" value=""/>
						</td>
					</tr>
					<tr style="height: 40px">
						<td>唯一标示id：</td>
						<td><input id="id" name="id" type="text" class="txt01" value=""/>
						</td>
					</tr>
					<tr style="height: 40px">
						<td>操作员：</td>
						<td><input id="operater" name="operater" type="text" class="txt01" value=""/>
						</td>
					</tr>
					<tr style="height: 40px">
						<td>秘钥：</td>
						<td><input id="payKey" name="payKey" type="text" class="txt01" value=""/>
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
                title: '商户添加',
                width: 400,
                modal: true,
                shadow: true,
                closed: true,
                height: 740,
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
                	msgShow('系统提示', '商户保存成功！', 'info');
	                 $newpass.val('');
	                 $rePass.val('');
	                 close();
                }
            });
        }
        
      //修改
        function serverUpdate() {
        	var tab = $.trim($('#tab').val()) ;
    		var id = $.trim($('#id').val()) ;
    		var merchantName = $.trim($('#merchantName').val()) ;
    		var status = $("input[name='status']").val();
    		var notifyUrl = $.trim($('#notifyUrl').val()) ;
    		var returnUrl = $.trim($('#returnUrl').val()) ;
    		var productName = $.trim($('#productName').val()) ;
    		var contact = $.trim($('#contact').val()) ;
    		var phone = $.trim($('#phone').val()) ;
    		var mobile = $.trim($('#mobile').val()) ;
    		var email = $.trim($('#email').val()) ;
    		var dayNorm = $.trim($('#dayNorm').val()) ;
    		var monthNorm = $.trim($('#monthNorm').val()) ;
    		var singleNorm = $.trim($('#singleNorm').val()) ;
    		var memo = $.trim($('#memo').val()) ;
    		var operater = $.trim($('#operater').val()) ;
    		var payKey = $.trim($('#payKey').val()) ;
    		if(merchantName==""){
    			alert("商户名不能为空！");
    			return;
    		}
    		if(id==""){
    			alert("唯一标示id不能为空！");
    			return;
    		}
            var url= "";
            if(tab==''){
            	url=encodeURI('${pageContext.request.contextPath}/commercial/addMerchantInfo?id='+id+'&merchantName='+merchantName+'&status='+status+'&notifyUrl='+notifyUrl+'&returnUrl='+returnUrl+'&productName='+productName+'&contact='+contact+'&phone='+phone+'&mobile='+mobile+'&email='+email+'&dayNorm='+dayNorm+'&monthNorm='+monthNorm+'&singleNorm='+singleNorm+'&memo='+memo+'&operater='+operater+'&payKey='+payKey);
            }else{
            	url=encodeURI('${pageContext.request.contextPath}/commercial/updateMerchantInfo?id='+id+'&merchantName='+merchantName+'&status='+status+'&notifyUrl='+notifyUrl+'&returnUrl='+returnUrl+'&productName='+productName+'&contact='+contact+'&phone='+phone+'&mobile='+mobile+'&email='+email+'&dayNorm='+dayNorm+'&monthNorm='+monthNorm+'&singleNorm='+singleNorm+'&memo='+memo+'&operater='+operater+'&payKey='+payKey);
            }
             $.post(url, function(data) {
            	 if(data.returnMsg=='1'){
	                 msgShow('系统提示', '恭喜，添加成功！', 'info');
	                 close();
	                $('#w').window('close');
	                //刷新
				      var url='${pageContext.request.contextPath}/commercial /findCommercial';
				      reload(url,name);
                }else if(data.returnMsg=='2'){
                	msgShow('系统提示', '修改成功！', 'info');
	                 close();
	                $('#w').window('close');
	                //刷新
				      var url='${pageContext.request.contextPath}/commercial /findCommercial';
				      reload(url,name);
                }else{
                	msgShow('系统提示', '角色已存在！', 'info');
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
		        url: "${pageContext.request.contextPath}/commercial /findCommercial?name="+name,  
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
		    	 	$('#tab').val("");
					$('#id').val("");
					$('#merchantName').val("");
					$('#status').val("");
					$('#notifyUrl').val("");
					$('#returnUrl').val("");
					$('#productName').val("");
					$('#contact').val("");
					$('#phone').val("");
					$('#mobile').val("");
					$('#email').val("");
					$('#dayNorm').val("");
					$('#monthNorm').val("");
					$('#singleNorm').val("");
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
   					var merchantName = row.merchantName;
   					var status = row.status;
   					var notifyUrl = row.notifyUrl;
   					var returnUrl = row.returnUrl;
   					var productName = row.productName;
   					var contact = row.contact;
   					var phone = row.phone;
   					var mobile = row.mobile;
   					var email = row.email;
   					var dayNorm = row.dayNorm;
   					var monthNorm = row.monthNorm;
   					var singleNorm = row.singleNorm;
   					var memo = row.memo;
   					
   					$('#tab').val("1");
   					$('#id').val(id);
   					$('#merchantName').val(merchantName);
   					$('#status').val(status);
   					$('#notifyUrl').val(notifyUrl);
   					$('#returnUrl').val(returnUrl);
   					$('#productName').val(productName);
   					$('#contact').val(contact);
   					$('#phone').val(phone);
   					$('#mobile').val(mobile);
   					$('#email').val(email);
   					$('#dayNorm').val(dayNorm);
   					$('#monthNorm').val(monthNorm);
   					$('#singleNorm').val(singleNorm);
   					$('#memo').val(memo);
  					
   					$('#w').window({
		                title: '商户修改',
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
					   var url="${pageContext.request.contextPath}/commercial/removeCommercialID?id="+id;
			            $.post(url, function(data) {
			                if(data.result==true){
			                 	msgShow('系统提示', '恭喜，删除成功！', 'info');
			                 	var url='${pageContext.request.contextPath}/commercial/findCommercial';
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
			var MERCHANT_NAME=$("#MERCHANT_NAME").val();
			var STATUS = $("input[name='STATUS']").val();
			var OPERATER=$("#OPERATER").val();
			var PRODUCT_NAME=$("#PRODUCT_NAME").val();
			 var CONTACT=$("#CONTACT").val();
			 var url=encodeURI("${pageContext.request.contextPath}/commercial/findCommercial?MERCHANT_NAME="+MERCHANT_NAME+"&STATUS="+STATUS+"&OPERATER="+OPERATER+"&PRODUCT_NAME="+PRODUCT_NAME+"&CONTACT="+CONTACT);
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