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

    
	
	<table id="dg" class="easyui-datagrid" title="权限角色管理" style="width:100%;height:540px"
			data-options="rownumbers:true,singleSelect:true,url:'',method:'get',toolbar:'#tb'">
		<thead>
			<tr>
				<th data-options="field:'id',width:200" hidden="true">ID</th>
				<th data-options="field:'name',width:400">名称</th>
				<th data-options="field:'statusName',width:240,align:'right'">状态</th>
				<th data-options="field:'create_Time',width:250,align:'right'">创建时间</th>
			</tr>
		</thead>
	</table>
	<div id="tb" style="padding:2px 5px;">
	   <span style="margin-left: 75%;">
		名称: 
		<input class="easyui-textbox" name="name" id="name" style="width:110px;">
		&nbsp;&nbsp;&nbsp;&nbsp;
		<a href="#" class="easyui-linkbutton" iconCls="icon-search " plain="true"  onclick="onsearch();" id="search"></a>
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
				style="padding: 10px; background: #fff; border: 1px solid #ccc;">
				<table cellpadding=3>
					<input id="id" type="hidden" />
					<tr style="height: 60px">
						<td>名称：</td>
						<td><input id="resourceName" type="text" class="txt01" value=""/>
						</td>
					</tr>
					
					<tr style="height: 20px">
						<td>状态：</td>
						<td>
			                <select  id="status" name="status" id="status" style="width:100%">
								<option value="1">启用</option>
								<option value="2">禁用</option>
							</select> 
						</td>
					</tr>
					
					
				</table>
								  
			
			<div class="easyui-panel" style="padding:5px;height: 80%;widows:300px;margin-top:5px;overflow-x:scroll;">
				  <ul id="deptree1"  style="height: 100%;width: 200px" class="easyui-tree" 
					 data-options="method:'get'"> 
			 	  </ul>
			</div>
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
                title: '角色添加',
                width: 400,
                modal: true,
                shadow: true,
                closed: true,
                height: 500,
                resizable:false
            });
            var id = $('#id').val();
            $('#deptree1').tree({ 
           	 lines:true,//显示虚线效果 
           	 animate: true,
           	  checkbox:true,
                 url: '${pageContext.request.contextPath}/managerRole/tree?id='+id,  
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
        	var id = $('#id').val();
            var $resourceName = $('#resourceName');
            var $status= $('#status');
            if ($resourceName.val() == '') {
                msgShow('系统提示', '请输入名称！', 'warning');
                return false;
            }
            var url= "";
            if(id==''){
            	url=encodeURI('${pageContext.request.contextPath}/managerRole/addRole?name='+$resourceName.val()+'&status='+$status.val()+'&id='+id+'&temp='+checkIds);
            }else{
            	url=encodeURI('${pageContext.request.contextPath}/managerRole/updateRole?name='+$resourceName.val()+'&status='+$status.val()+'&id='+id+'&temp='+checkIds);
            }
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
		        url: "${pageContext.request.contextPath}/managerRole/QueryRoleMessage?name="+name,  
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
		    	 document.getElementById("resourceName").value=""; 
		    	 document.getElementById("id").value=""; 
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
   					   var id=row.id;
   					   var name=row.name;
   					   var status=row.status;
   						document.getElementById("id").value=id; 
   						document.getElementById("resourceName").value=name; 
   						$("#status").get(0).selectedIndex = status-1;//index为索引值
   					   $.ajax({
   						    url:"${pageContext.request.contextPath}/managerRole/QueryRoleDetails?id="+id, 
	   						success: function(data) {
	   							var node;
	   				            $(data[0].list).each(function(){
	   				            	if(this.resources!=null && this.resources!=""){
	   				            		var resId=this.resources.split(",");
	   				            		for(var i=0;i<resId.length;i++){
	   				            			node =  $('#deptree1').tree("find",resId[i]);
		   				            		if(node!=null){
			   				            		$('#deptree1').tree('check', node.target);
			   				            		expand(node);//展开相应菜单
		   				            		}	   										
	   				            		}
	   				            	}else{
	   				            		node =  $('#deptree1').tree("find",this.moduleId);
	   				            		if(node!=null){
		   				            		$('#deptree1').tree('check', node.target);
		   				            		expand(node);//展开相应菜单
	   				            		}	   				            		
	   				            	}
	   							});
	   						}
   					   });
	  					//openPwd();
	  					$('#w').window({
			                title: '角色修改',
			                width: 400,
			                modal: true,
			                shadow: true,
			                closed: true,
			                height: 500,
			                resizable:false
			            });
	  					var id = $('#id').val();
	  		            $('#deptree1').tree({ 
	  		           	 lines:true,//显示虚线效果 
	  		           	 animate: true,
	  		           	  checkbox:true,
	  		                 url: '${pageContext.request.contextPath}/managerRole/tree1?id='+id,  
	  		             });
	  					$('#w').window('open');
   				}
   			   });
   			}
   		}
		
        function expand(node){
        	$('#deptree1').tree('expandTo', node.target);
        	if($('#deptree1').tree('getChildren', node.target)!=null){
        		$('#deptree1').tree('expand', node.target);
        	}
        }
        
		function removeit(){
		 var name=$("#name").val();
		var row = $('#dg').datagrid('getSelected');
			if (row){
			$.messager.confirm('系统提示', '是否确定删除?', function(r){
				if (r){
					   var id=row.id;
					   var url="${pageContext.request.contextPath}/managerRole/deleteRole?id="+id;
			            $.post(url, function(data) {
			                if(data.returnMsg=='1'){
			                 msgShow('系统提示', '恭喜，删除成功！', 'info');
			               //刷新
				              var url='${pageContext.request.contextPath}/managerRole/QueryRoleMessage';
				              reload(url,name);
			                }else{
			                  msgShow('系统提示', '删除失败！', 'info');
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
			 var name=$("#name").val();
			 var url=encodeURI("${pageContext.request.contextPath}/managerRole/QueryRoleMessage?name="+name);
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
		
	  
	</script>
</html>