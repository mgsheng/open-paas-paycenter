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
		<a href="#" class="easyui-linkbutton" iconCls="icon-search " plain="true" id="search"></a>
		<a href="#" class="easyui-linkbutton" iconCls="icon-add" plain="true" id="add"></a>
		<a href="#" class="easyui-linkbutton" iconCls="icon-edit" plain="true" id="edit"></a>
		<a href="#" class="easyui-linkbutton" iconCls="icon-cut" plain="true" id="delete" onclick="removeit();"></a>
		</span>
	</div>
	<!--添加资源-->
	<div id="w" class="easyui-window" title="资源添加" collapsible="false"
		minimizable="false" maximizable="false" icon="icon-save"
		style="width: 300px; height: 150px; padding: 5px;
        background: #fafafa;">
		<div class="easyui-layout" fit="true">
			<div region="center" border="false"
				style="padding: 10px; background: #fff; border: 1px solid #ccc;">
				<table cellpadding=3>
				
					<tr>
						<td>名称：</td>
						<td><input id="resourceName" type="text" class="txt01" />
						</td>
					</tr>
					
					<tr>
						<td>状态：</td>
						<td>
			                 <select class="easyui-combobox" data-options="editable:false" id="status" name="status" style="width:100%">
								<option value="1">启用</option>
								<option value="0">禁用</option>
							</select>
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
                title: '资源',
                width: 300,
                modal: true,
                shadow: true,
                closed: true,
                height: 260,
                resizable:false
            });
        }
        //关闭登录窗口
        function closePwd() {
            $('#w').window('close');
        }
        //添加
        function serverLogin() {
            var $resourceName = $('#resourceName');
            var $status= $('#status');
            if ($resourceName.val() == '') {
                msgShow('系统提示', '请输入名称！', 'warning');
                return false;
            }
            var url=encodeURI('${pageContext.request.contextPath}/managerRole/addRole?name='+$resourceName.val()+'&status='+$status.val());
            $.post(url, function(data) {
                if(data.returnMsg=='1'){
                 msgShow('系统提示', '恭喜，添加成功！', 'info');
                 close();
                $('#w').window('close');
                }else{
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
                $('#w').window('open');
            });
            $('#btnEp').click(function() {
                serverLogin();
            });

			$('#btnCancel').click(function(){closePwd();});
		    });
		  
        function getSelected(){
			var row = $('#dg').datagrid('getSelected');
			if (row){
				$.messager.alert('Info', row.itemid+":"+row.productid+":"+row.attr1);
			}
		}
		
		function removeit(){
		 var name=$("#name").val();
		var row = $('#dg').datagrid('getSelected');
			if (row){
			$.messager.confirm('系统提示', '是否确定删除?', function(r){
				if (r){
					   var id=row.id;
					   var url='${pageContext.request.contextPath}/managerRole/delete?id='+id;
			            $.post(url, function(data) {
			                if(data.returnMsg=='1'){
			                 msgShow('系统提示', '恭喜，删除成功！', 'info');
			                }else{
			                  msgShow('系统提示', '删除失败！', 'info');
			                }
			            });
			              //刷新
			              var url='${pageContext.request.contextPath}/resource/findModuel';
			              reload(url,name);
				}
			   });
		   
			}
		  
		}
		function reload(url,name){
		$('#dg').datagrid('reload',{
            url: url, queryParams:{ name:name}, method: "post"
          }); 
		}
		
	</script>
</html>