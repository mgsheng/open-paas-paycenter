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
	<div class="easyui-panel" title="模块管理" style="width:100%;max-width:100%;padding:20px 30px;height:540px;">
	<div style="padding:2px 5px; text-align: right;">
		<a href="#" class="easyui-linkbutton" iconCls="icon-add" plain="true" id="add"></a>
		<a href="#" class="easyui-linkbutton" iconCls="icon-edit" plain="true" id="edit"></a>
		<a href="#" class="easyui-linkbutton" iconCls="icon-cut" plain="true" id="delete" ></a>
	</div>
	<div class="easyui-panel" style="padding:5px;height: 95%;overflow-x:scroll;">
		  <ul id="deptree"  style="height: 100%"class="easyui-tree" 
			 data-options="method:'get',url:'${pageContext.request.contextPath}/module/tree'"> 
	 </ul>
	</div>
	</div>
	<!--添加模块-->
	<div id="wmodule" class="easyui-window" title="资源添加" collapsible="false"
		minimizable="false" maximizable="false" icon="icon-save"
		style="width: 300px; height: 150px; padding: 5px;
        background: #fafafa;">
		<div class="easyui-layout" fit="true">
			<div region="center" border="false"
				style="padding: 10px; background: #fff; border: 1px solid #ccc;">
				<form id="resourceFrom" style="padding-top: 10px;" action="">
				<table cellpadding=6>
					<tr>
					
						<td width="50px">父节点：</td>
						<td>
						<div  id="parentName"></div>
						<input id="id" name="id" type="hidden" value=""/>
						<input type="hidden" id="parentId" name="parentId" value=""/>
						</td>
						<td rowspan="6" valign="top" width="50px">资源：</td>
						<td rowspan="6" valign="top">
						<div  id="resourcesHtml">
						</div>
						<input type="hidden" id="resources" value="" />
						</td>
					</tr>
					<tr>
						<td>名称：</td>
						<td><input id="moduleName" name="name" type="text" class="txt01" />
						</td>
					</tr>
					<tr>
						<td>URL：</td>
						<td><input id="url" type="text" name="url"class="txt01" />
						</td>
					</tr>
					<tr>
						<td>code：</td>
						<td><input id="code" type="text" name="code" class="txt01" />
						</td>
					</tr>
					<tr>
						<td>排序：</td>
						<td><input id="display_order" name="displayOrder"type="text" class="txt01" />
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
				</form>
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
	   //设置=窗口
        function openPwd() {
            $('#wmodule').window({
                title: '模块',
                width: 600,
                modal: true,
                shadow: true,
                closed: true,
                top:150,
                left:400,
                height: 400,
                resizable:false
            });
        }
        //关闭窗口
        function closePwd() {
            $('#wmodule').window('close');
        }
        //添加資源
        function serverLogin() {
            var name = $('#moduleName').val();
            var code = $('#code').val();
            var moduleUrl= $('#url').val();
            var displayOrder= $('#display_order').val();
            var status= $('#status').val();
            var parentId=$('#parentId').val();
            var id=$('#id').val();
            if (name == '') {
                msgShow('系统提示', '请输入名称！', 'warning');
                return false;
            }
            if (code == '') {
                msgShow('系统提示', '请输入code！', 'warning');
                return false;
            }
            if (moduleUrl == '') {
                msgShow('系统提示', '请输入URL！', 'warning');
                return false;
            }
            if (displayOrder == '') {
                msgShow('系统提示', '请输入排序！', 'warning');
                return false;
            }
              var aa= $(":checkbox").serialize();
                 var optionArray = aa.split("&");
	             var resources="";
				for(var i = 0;i<optionArray.length-1;i++){
				    var opt = optionArray[i];
				    var optArray = opt.split("=");
				    resources+=optArray[1]+",";                           
				}
				if(resources!=""){
				resources=resources.substring(0, resources.length-1);
				}
			var url="";
		    if(id==""){
		    url=  encodeURI('${pageContext.request.contextPath}/module/add?name='+name+'&code='+code+'&parentId='+parentId+'&status='+status+'&displayOrder='+displayOrder+'&url='+moduleUrl+'&resources='+resources);
		    }else{
		   url= encodeURI('${pageContext.request.contextPath}/module/add?name='+name+'&code='+code+'&parentId='+parentId+'&status='+status+'&displayOrder='+displayOrder+'&url='+moduleUrl+'&resources='+resources+'&id='+id);
		    }
           //解析data===parentId=&resources=1&resources=3&resources=5&resources=7&name=aa&url=aa%2Faa%2Faa&code=aa&displayOrder=2&status=1
             $.post(url, function(data) {
                if(data.returnMsg=='1'){
                 msgShow('系统提示', '恭喜，添加成功！', 'info');
                 close();
                $('#wmodule').window('close');
                 $(window.top.document).find("#deptree").tree('reload');
                }else if(data.returnMsg=='2'){
                 msgShow('系统提示', '恭喜，修改成功！', 'info');
                 close();
                 $('#wmodule').window('close');
                 $(window.top.document).find("#deptree").tree('reload');
                }else{
                 msgShow('系统提示', '系统错误！', 'info');
                 close();
                }
            }); 
            
        }
		//弹出信息窗口 title:标题 msgString:提示信息 msgType:信息类型 [error,info,question,warning]
		function msgShow(title, msgString, msgType) {
			$.messager.alert(title, msgString, msgType);
		}
		
	    $(function(){  
		     openPwd();
		     //添加
		     $('#add').click(function() {
		     var node = $('#deptree').tree('getSelected');
			if (node){
			 var s = node.text;
			 $("#parentName").html(s);
			 $("#parentId").val(node.id);
			   $('#wmodule').window('open');
		     }else{
		       msgShow('系统提示', '请选择需要添加的节点！', 'info');
		     }
              
            });
            //编辑
            $('#edit').click(function() {
		     var node = $('#deptree').tree('getSelected');
			if (node){
			 var s = node.text;
			 var id=node.id;
			 var url=encodeURI('${pageContext.request.contextPath}/module/detail?id='+id);
			 $.post(url, function(data) {
                if(data.returnMsg=='1'){
	                reset();
	                updateModel(data);
                    openPwd();
	  				$('#wmodule').window('open');
                }else{
                 msgShow('系统提示', '系统错误！', 'info');
                 close();
                }
            }); 
            }else{
            msgShow('系统提示', '请选择需要添加的节点！', 'info');
            }  
            });
            //删除
             $('#delete').click(function() {
		     var node = $('#deptree').tree('getSelected');
		     var id=node.id;
			 if (node){
			  var url=encodeURI('${pageContext.request.contextPath}/module/delete?id='+id);
           //解析data===parentId=&resources=1&resources=3&resources=5&resources=7&name=aa&url=aa%2Faa%2Faa&code=aa&displayOrder=2&status=1
             $.post(url, function(data) {
                if(data.returnMsg=='1'){
                 msgShow('系统提示', '恭喜，删除成功！', 'info');
                 close();
                 $(window.top.document).find("#deptree").tree('reload');
                }else{
                 msgShow('系统提示', '删除失败！', 'info');
                 close();
                }
            }); 
		     }else{
		       msgShow('系统提示', '请选择需要添加的节点！', 'info');
		     }
              
            });
            
            
            
            $('#btnEp').click(function() {
                serverLogin();
            });

			$('#btnCancel').click(function(){closePwd();});
			var list = '${resourceList}';
	        var data = jQuery.parseJSON(list);
			var html = "";
			$(data).each(function(index,val){
					html+="<div style='float:left;width:50%'>";
					html+="<input class='m-wrap placeholder-no-fix' type='checkbox' name='resources' id='resource_"+val.id+"' value='"+val.id+"'/>";
					html+="<span style='cursor:pointer' onclick='jQuery(\"#resource_"+val.id+"\").click();'>"+val.name+"</span>";
					html+="</div>";
			});
			$("#resourcesHtml").html(html);
			
		    });
		    //清空
			function reset(){
				jQuery('.control-group').removeClass('error');
				jQuery('#id').val('0');
				jQuery('#parentName').val('');
				jQuery('#parentId').val('');
				jQuery('#name').val('');
				jQuery('#url').val('');
				jQuery('#code').val('');
				jQuery('#displayOrder').val('1');
				document.getElementById("status").selectedIndex=0;
				jQuery('#resources').val('');
				jQuery("input[name='resources']").each(function(){
					if(this.checked == true){
						$(this).parent().removeClass("checked");
						$(this).attr("checked",false);
					}
				});
			}
		     function updateModel(data){
				reset();
				if(data.id==null || data.id==0){
					return;
				}
				//赋值
				var id = data.id;
				var parentId = data.parentId;
				var name = data.name;
				var url = data.url;
				var code = data.code;
				var displayOrder = data.displayOrder;
				var status = data.status;
				var resources = data.resources;
				jQuery('#id').val(id);
				jQuery('#parentId').val(parentId);
				if(parentId==0){
					jQuery('#parentName').html('根节点');
				}
				else{
					jQuery('#parentName').html(data.parentName);
				}
				jQuery('#parentId').val(parentId);
				jQuery('#moduleName').val(name);
				jQuery('#url').val(url);
				jQuery('#code').val(code);
				jQuery('#display_order').val(displayOrder);
				jQuery('#status').attr('value',status);
				jQuery('#resources').val(resources);
				if(resources!=null && resources!=""){
					var resource=resources.split(",");
					for(var i=0;i<resource.length;i++){
						$("#resource_"+resource[i]).parent().addClass("checked");
						$("#resource_"+resource[i]).attr("checked",true);
					}
				}
			}
	
</script>
</html>