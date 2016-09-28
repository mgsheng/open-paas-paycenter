<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<!DOCTYPE HTML>
<html>
	<head>
		<meta charset="UTF-8">
		<title>渠道信息列表</title>
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/themes/default/easyui.css">
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/themes/icon.css">
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/dataList.css">
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.min.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.easyui.min.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/highcharts/highcharts.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/highcharts/modules/exporting.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/locale/easyui-lang-zh_CN.js"></script>
	</head>
	<body >
		
				<div style="border:0px solid;border-radius:8px;margin-bottom:0px;width: 100%;max-width:100%;">
					<div class="top" style="width: 100%">
						<div class="easyui-panel" title="查询条件" style="width:100%;max-width:100%;padding:20px 25px;">
							<form id="fm" method="post" action="/department/findDepts" >
								<table cellpadding="5px">
									<tr>
										<td>
												<input class="easyui-textbox" name="deptname" id="deptname" label="渠道名：" 
													prompt="选填" style="width:200px"></input> 
										</td>
										<td>	
											<a href="javascript:void(0)" class="easyui-linkbutton" onclick="findDepts();" style="width: 120px;margin-left:30px;">
												<span style="font-weight:bold;margin-right:15px;margin-left:15px;">查询</span>
												<span class="icon-search">&nbsp;&nbsp;&nbsp;&nbsp;</span>
											</a>
										</td>
										<td>	
											<a href="#" class="easyui-linkbutton"  style="width: 120px;margin-left:30px;" onclick="openWinAdd();">
												<span style="font-weight:bold;margin-right:15px;margin-left:15px;"">添加渠道</span>
												<span class="icon-add">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span>
											</a>
										</td>
										<td>	
											<a href="javascript:void(0)" class="easyui-linkbutton" onclick="clearFormFind();" style="width:120px;margin-left:30px;">
												<span style="font-weight:bold;margin-right:15px;margin-left:15px;">清除</span>
												<span class="icon-clear">&nbsp;&nbsp;&nbsp;&nbsp;</span>
											</a>
										</td>	
										<td>	
											<a href="#" class="easyui-linkbutton" onclick="removeDeptByID();" style="width:120px;margin-left:30px;">
												<span style="font-weight:bold;margin-right:15px;margin-left:15px;">删除</span>
												<span class="icon-cut">&nbsp;&nbsp;&nbsp;&nbsp;</span>
											</a>
										</td>	
										<td>	
											<a href="javascript:void(0)" class="easyui-linkbutton" onclick="openWinUpdate();" style="width:120px;margin-left:30px;">
												<span style="font-weight:bold;margin-right:15px;margin-left:15px;">修改</span>
												<span class="icon-edit">&nbsp;&nbsp;&nbsp;&nbsp;</span>
											</a>
										</td>
											
									</tr>
								</table>
							</form>
						</div>
					</div>
				</div>
				<div class="botton" style="margin-top:0px;width:100%;height:300px">
					<table  id="dg"  class="easyui-datagrid" title="查询结果"  style="width:100%;max-width:100%;padding:20px 30px;"
						data-options="singleSelect:true,method:'get'">
						<thead>
							<tr>
								<th data-options="field:'id',align:'center'" hidden="true" style="width:15%;max-width:100%;">ID</th>
								<th data-options="field:'channelName',align:'center'" style="width:15%;max-width:100%;">渠道名称</th>
								<th data-options="field:'channelStatus',align:'center'" style="width:18%;max-width:100%;">渠道状态</th>
								<th data-options="field:'priority',align:'center'" style="width:18%;max-width:100%;">优先级</th>
								<th data-options="field:'createDate',align:'center'" style="width:18%;max-width:100%;">优先级</th>
								<th data-options="field:'mome',align:'center'" style="width:18%;max-width:100%;">备注</th>
								<th data-options="field:'merId',align:'center'" style="width:18%;max-width:100%;">商户id</th>
								<th data-options="field:'notifyUrl',align:'center'" style="width:18%;max-width:100%;">接口通知地址</th>
								<th data-options="field:'paymentType',align:'center'" style="width:18%;max-width:100%;">支付类型</th>
								<th data-options="field:'backurl',align:'center'" style="width:18%;max-width:100%;">商户返回地址</th>
								<th data-options="field:'sighType',align:'center'" style="width:18%;max-width:100%;">签名方式</th>
								<th data-options="field:'inputCharset',align:'center'" style="width:18%;max-width:100%;">字符编码格式</th>
								<th data-options="field:'paymentChannel',align:'center'" style="width:18%;max-width:100%;">支付渠道</th>
							</tr>
						</thead>
					</table>
				</div>
			
		
		<!-- 添加渠道窗口 -->
		<div id="addDept" class="easyui-window" title="渠道部门" collapsible="false" minimizable="false" maximizable="false" 
			icon="icon-save" style="background: #fafafa;">
			<div class="easyui-layout" fit="true">
				<div region="center" border="false" style="background: #fff; border: 1px solid #ccc;">
				<input id="id"  type="text" class="text" name="id" >
					<table cellpadding="10px" id="addForm"  style="border: 0px;margin:50px 20px;font-weight: bold;" data-options="novalidate:true">
						<tr>
							
							<td>渠道名称：</td>
							<td>
								<input id="channelName"  type="text" class="text" name="channelName" >
							</td>
							<td>渠道状态：</td>
							<td>
								<input id="channelStatus"  type="text" class="text" name="channelStatus" >
							</td>
						</tr>
						<tr>
							<td>渠道优先级：</td>
							<td>
								<input id="priority"  type="text" class="text" name="priority" >
							</td>
							<td>支付渠道：</td>
							<td>
								<input id="paymentChannel"  type="text" class="text" name="paymentChannel" >
							</td>
						</tr>
						<tr>
							<td>接口通知地址：</td>
							<td>
								<input id="notifyUrl"  type="text" class="text" name="notifyUrl" >
							</td>
							<td>支付类型：</td>
							<td>
								<input id="paymentType"  type="text" class="text" name="paymentType" >
							</td>
						</tr>
						<tr>
							<td>商户返回地址接口：</td>
							<td>
								<input id="backUrl"  type="text" class="text" name="backUrl" >
							</td>
							<td>type：</td>
							<td>
								<input id="type"  type="text" class="text" name="type" >
							</td>
						</tr>
						<tr>
							<td>签名方式：</td>
							<td>
								<input id="sighType"  type="text" class="text" name="sighType" >
							</td>
							<td>字符编码格式：</td>
							<td>
								<input id="inputCharset"  type="text" class="text" name="inputCharset" >
							</td>
						</tr>
						<tr>
							
							<td>备注：</td>
							<td>
								<input id="memo"  type="text" class="text" name="memo" >
							</td>
							
						</tr>
						
					</table>	
				</div>
				<div region="south" border="false" style="text-align:center; height: 50px; line-height: 50px;">
					<a href="javascript:void(0)" class="easyui-linkbutton" icon="icon-ok" onclick="submitForm()" style="margin:8px">提交</a>
					<a href="javascript:void(0)" class="easyui-linkbutton" icon="icon-clear"  onclick="clearFormAdd()" style="margin:8px">清空</a>
					<a href="javascript:void(0)" class="easyui-linkbutton" icon="icon-cancel" onclick="closeWinAdd()" style="margin:8px">取消</a>
				</div>
			</div>
		</div>
	</body>
	<script>
	
	 //设置添加部门窗口
    function winAdd() {
        $('#addDept').window({
            title: '添加部门',
            width: 800,
            modal: true,
            shadow: true,
            closed: true,
            height: 500,
            resizable:false
        });
    }
	// 提交（渠道信息）
	function submitForm(){
		var id = $.trim($('#id').val()) ;
		var channelName = $.trim($('#channelName').val()) ;
		var channelStatus = $.trim($('#channelStatus').val()) ;
		var priority = $.trim($('#priority').val()) ;
		var paymentChannel = $.trim($('#paymentChannel').val()) ;
		var notifyUrl = $.trim($('#notifyUrl').val()) ;
		var paymentType = $.trim($('#paymentType').val()) ;
		var backUrl = $.trim($('#backUrl').val()) ;
		var type = $.trim($('#type').val()) ;
		var sighType = $.trim($('#sighType').val()) ;
		var inputCharset = $.trim($('#inputCharset').val()) ;
		var memo = $.trim($('#memo').val()) ;
		// 提交信息前完成前端校验
		//var check_result = checked();
		//if(!check_result){
		//	return;
		//}
		var urlValue=null;
		if(id==""){
			urlValue="/pay-platform-manager/irrigation/addDictTrade";
		}else{
			urlValue="/pay-platform-manager/irrigation/updateDictTrade";
		}
		$.ajax({
			type:"post",
			url:urlValue,
			data:{"id":id,"channelName":channelName,"channelStatus":channelStatus,"priority":priority,"paymentChannel":paymentChannel,"notifyUrl":notifyUrl,"paymentType":paymentType,"backUrl":backUrl,"type":type,"sighType":sighType,"inputCharset":inputCharset,"memo":memo},
			//data:"{"',paymentChannel:'" + paymentChannel + "',notifyUrl:'"+notifyUrl+"',paymentType:'" + paymentType + "',backurl:'"+backurl+"',type:'" + type + "',sighType:'"+sighType+"',inputCharset:'" + inputCharset + "',memo:'"+memo+"'}", 
			dataType:"json",
			success:function (data){
				if(data.result == true){
					$.messager.alert("系统提示","恭喜，添加渠道成功!","info");
					var URL = '${pageContext.request.contextPath}/irrigation/findCommercial';
					var name = $('#deptname').val().trim();
					closeWinAdd();
					reload(URL,name);
				}else if(data.result == false){
					$.messager.alert("系统提示","该渠道名已被注册，请重新填写渠道名!","error");	
					clearFormAdd();
				}else{
					$.messager.alert("系统提示","添加渠道失败，请重新添加!","error");
					clearFormAdd();
				}
			},
			error:function(){
				$.messager.alert("系统提示","部门渠道异常，请刷新页面!","error");
			}
		});
	}
	//根据部门ID删除部门
	function removeDeptByID(){
		var row = $('#dg').datagrid('getSelected');
		if (row){
			$.messager.confirm('系统提示', '是否确定删除?', function(r){
				if (r){
					   var id=row.id;
					   var url='${pageContext.request.contextPath}/irrigation/removeDictTradeID?id='+id;
			            $.post(url, function(data) {
			                if(data.result==true){
			                 	msgShow('系统提示', '恭喜，删除成功！', 'info');
			                }else{
			                  	msgShow('系统提示', '删除失败！', 'error');
			                }
			            });
			              //刷新
			              var url='${pageContext.request.contextPath}/irrigation/findCommercial';
			              reload(url,name);
				}
		   });
		}else{
        	msgShow('系统提示', '请选择要删除的部门！', 'error');
        }
	}
	//打开修改部门窗口
	function openWinUpdate(){
		
		var row = $('#dg').datagrid('getSelected');
		if (row){
			var id = row.id;
			var channelName = row.channelName;
			var channelStatus = row.channelStatus;
			var priority = row.priority;
			var paymentChannel = row.paymentChannel;
			var notifyUrl = row.notifyUrl;
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
			$('#notifyUrl').val(notifyUrl);
			$('#paymentType').val(paymentType);
			$('#backUrl').val(backUrl);
			$('#type').val(type);
			$('#sighType').val(sighType);
			$('#inputCharset').val(inputCharset);
			$('#memo').val(memo);
			$('#addDept').window('open');
		}else{
			msgShow('系统提示', '请选择要修改的部门！', 'error');
		}
	};
	// 清空添加渠道表单
	function clearFormAdd(){
		$('#addForm').form('clear');
	}
	//打开添加渠道窗口
	function openWinAdd(){
		//打开添加渠道窗口之前先清空
		$('#id').val("");
		clearFormAdd();
		$('#addDept').window('open');
	};
	
	
	
	
	
	
	
	
	
	
	
	
	
		//添加tab页面
		function addPanel(deptName,deptId){
			if ($('#tt').tabs('exists', deptName)){
			 	$('#tt').tabs('select', deptName);
			} else {
				 var url = '${pageContext.request.contextPath}/department/toDeptUsers?id='+deptId+'&deptName='+deptName;
			 	 var content = '<iframe scrolling="auto" frameborder="0" src="'+url+'" style="width:100%;height:100%;"></iframe>';
				 $('#tt').tabs('add',{
					 title:'部门:'+deptName,
					 content:content,
					 closable:true,
					 cache:true
				 });
			}
		}	
		//移除tab页面
		function removePanel(){
			var tab = $('#tt').tabs('getSelected');
			if (tab){
				var index = $('#tt').tabs('getTabIndex', tab);
				$('#tt').tabs('close', index);
			}
		}
	
	
		//设置部门用户信息窗口
		 function winDeptUserList() {
            $('#deptUserList').window({
                title: '部门用户信息',
                width: 600,
                modal: true,
                shadow: true,
                closed: true,
                height: 400,
                resizable:false
            });
        }
		
		//打开部门用户信息窗口
		winDeptUserList();
		function openWinDUL(deptName,deptId){
			$('#deptUserList').window('open');
			addPanel(deptName,deptId);
		}
		
		 //设置修改部门窗口
        function winUpdate() {
            $('#upda').window({
                title: '部门信息',
                width: 800,
                modal: true,
                shadow: true,
                closed: true,
                height: 500,
                resizable:false
            });
        }
        
        
        
          // 清除查询表单
		function clearFormFind(){
			$('#fm').form('clear');
		}
		
		// 清空修改部门窗口
		function clearFormUpdate(){
			$('#tb').form('clear');
		};
		
		
	
		
		winUpdate();
		
		
		winAdd();
		
		
		//关闭修改部门窗口
		function closeWinDeptUserList(){
			$('#deptUserList').window('close');
		};
		
		//关闭修改部门窗口
		function closeWinUpdate(){
			$('#upda').window('close');
		};
		
		//关闭添加部门窗口
		function closeWinAdd(){
			$('#addDept').window('close');
		};
		
		//弹出信息窗口 title:标题 msgString:提示信息 msgType:信息类型 [error,info,question,warning]
		function msgShow(title, msgString, msgType) {
			$.messager.alert(title, msgString, msgType);
		}
	
		
		
		//列表重新加载
		function reload(url,name){
			$('#dg').datagrid('reload',{
	            url: url, queryParams:{ name:name}, method: "post"
	          }); 
		}
		
		// 查询部门方法
		function findDepts(){
			//部门名
			var dept_name = $('#deptname').val().trim();
			$('#dg').datagrid({
				collapsible:true,
				rownumbers:true,
				pagination:true,
		        url: "${pageContext.request.contextPath}/irrigation/findIrrigation?dept_name="+dept_name,  
		        pagination: true,//显示分页工具栏
		        onLoadSuccess:function(data){
                    if (data.total<1){
                       $.messager.alert("系统提示","没有符合查询条件的数据!","info");
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
        
        // 提交修改后的部门信息
         function updateDept() {
			var row = $('#dg').datagrid('getSelected');
			if(row){	
				var id=row.id;
	            var dept_name = $('#dept_name').val().trim();
	            var dept_name_check = $('#dept_name_check').val().trim();
	            if (dept_name == '') {
	                msgShow('系统提示', '请输入部门名！', 'warning');
	                return false;
	            }else if(dept_name_check==dept_name){
	            	msgShow('系统提示', '修改成功！', 'info');
	            	closeWinUpdate();
		            var url='${pageContext.request.contextPath}/department/findDepts';
			        reload(url,name);
	                return;
	            }
	            dept_name = $('#dept_name').val().trim();
	            var url=encodeURI('${pageContext.request.contextPath}/department/updateDept?dept_name='+dept_name+'&id='+id);
	            $.post(url, function(data) {
	                if(data.result==true){
		                 msgShow('系统提示', '修改成功！', 'info');
		                 closeWinUpdate();
		                 var url='${pageContext.request.contextPath}/department/findDepts';
			             reload(url,name);
	                }else if(data.result==false){
	                	 msgShow('系统提示', '该部门名已被注册！', 'error');
		                 closeWinUpdate();
		                 var url='${pageContext.request.contextPath}/department/findDepts';
			             reload(url,name);
	                }else{
		                 msgShow('系统提示', '修改失败！', 'error');
		                 closeWinUpdate();
	                }
	            });
            }else{
            	msgShow('系统提示', '请选择要修改的部门！', 'error');
            }
        }
        
        
		
		//前端校验
		function checked(){
			var regex_dept_Name=/^[\u4E00-\u9FA5A-Za-z0-9_]{2,20}$/;
			var dept_Name = $.trim($('#dept_Name').val()) ;
			if(dept_Name == "" || dept_Name == null || dept_Name == undefined || regex_dept_Name.test(dept_Name) != true){
					$.messager.alert("系统提示","部门名不能为空或格式不正确，请重新填写！\n用户名由2-20位汉字、字母、数字、下划线组成","error");	
					return false;
			}
			return true;
		}
		
		
		
		 // 查询部门用户方法
		function findDeptUsers(deptID){
			$('#deptTable').datagrid({
				collapsible:true,
				rownumbers:true,
				pagination:true,
		        url: "${pageContext.request.contextPath}/managerUser/findDeptUsers?deptID="+deptID,  
		        pagination: true,//显示分页工具栏
		        onLoadSuccess:function(data){
                    if (data.total<1){
                       $.messager.alert("系统提示","没有符合查询条件的数据!","info");
                  };
                }
		    }); 
		    alert(deptID);
			 //设置分页控件 
		    var p = $('#deptTable').datagrid('getPager'); 
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
			findDepts();
		});
	</script>
</html>