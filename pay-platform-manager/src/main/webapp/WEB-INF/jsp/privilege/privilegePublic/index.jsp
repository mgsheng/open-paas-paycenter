<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<html>
<head>
	<meta charset="UTF-8">
	<title>公共权限</title>
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/themes/default/easyui.css">
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/themes/icon.css">
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/dataList.css">
	<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.min.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/js/highcharts/highcharts.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/js/highcharts/modules/exporting.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/js/locale/easyui-lang-zh_CN.js"></script>
</head>
<body>
	<div id="ep" class="easyui-panel" title="公共权限" style="width:100%;max-width:100%;padding:20px 30px;height:650px;">
		<div style="margin:20px 0;">
			<a href="#" class="easyui-linkbutton" onclick="getCheckedId()" style="font-weight: bolder;">显示选中选项</a> 
		</div>
		<div style="margin:10px 0">
			<a href="#" class="easyui-linkbutton" onclick="getCheckedAll()" style="font-weight: bolder;margin-left:20px">选中全部</a>
			<a href="#" class="easyui-linkbutton" onclick="getCancelAll()" style="font-weight: bolder;margin-left:20px">取消全部选中</a>
			<a href="#" class="easyui-linkbutton" onclick="submitPub()" style="font-weight: bolder;margin-left:20px">修改</a>
			<a href="#" class="easyui-linkbutton" onclick="getBack()" style="font-weight: bolder;margin-left:20px">返回</a> 
		</div>
		<div class="easyui-panel" style="padding:30px;overflow-x:scroll;height: 70%;">
			<ul id="tt" class="easyui-tree" style="height: 100%" data-options="url:'${pageContext.request.contextPath}/privilegePublic/tree2', 
				method:'get',animate:true,checkbox:true"></ul>
		</div>
	</div>
	
</body>
<script type="text/javascript">
	//加载时，解析privilege_public表中数据，用于显示已勾选的选项
	function loadTree(){
		$.ajax({  
	        type: 'POST',  
	        dataType : "json",  
	        url: "${pageContext.request.contextPath}/privilegePublic/indexjson",//请求路径  
	        data:{},
	        error: function () {//请求失败处理函数  
	        	msgShow('系统提示', '请求失败！', 'error');
	        },  
	        success:function(data){ //请求成功后处理函数。  
	        	alert(data);
	       		//自定义参数以便查询修改和详情页面赋值
	        	var node;
	            $(data.listMap).each(function(){
	            	if(this.resources!=null && this.resources!=""){
	            		var resId=this.resources.split(",");
	            		for(var i=0;i<resId.length;i++){
	            			node = $('#tt').tree('find', this.id);
	            			if(node!=null){
								if(!node.isParent){
									$('#iid').tree('checked',true);
								};
							};
	            		};
	            	}else{
	            		node = zTree.getNodeByParam("searchId",("m"+this.module));
	            		if(node!=null){
							$('#iid').tree('checked',true);
						};
	            	};
				});
	        }
	    }); 
	}
		//$(function(data){
		//	alert("--------first--------");
		//	loadTree();
		//});
		
		//提交
		function submitPub(){
			$.messager.confirm('系统提示','确认修改吗？',function(){
				updatePub();
			});	
		}
		//修改
		function updatePub(){
			var checkedId = getCheckedId();												
		   	var url= "${pageContext.request.contextPath}/privilegePublic/updatePublic?checkedId="+checkedId;
		   	alert("---------------------------------------------"+checkedId);
            $.post(url, function(data) {
                if(data.result==true){
                 	msgShow('系统提示', '恭喜，修改成功！', 'info');
                }else{
                  	msgShow('系统提示', '修改失败！', 'error');
                }
            });
              //刷新
             var url= "${pageContext.request.contextPath}/privilegePublic/index";
             reLoad(url);
		}
		//刷新
		function reLoad(url){
			//$('#ep').panel('open').panel('refresh');
			$('#ep').panel('open').panel('refresh',url);
		}
		//选中全部
		function getCheckedAll(){
			$('#tt').tree('check');
		}
		//取消全部选中
		function getCancelAll(){
			$('#tt').tree('uncheck');
		}
		//返回
		function getBack(){
			$.messager.confirm('系统提示','确认返回吗？',function(){
				var url= "${pageContext.request.contextPath}/privilegePublic/index";
				reLoad(url);
			});	
		}
		//获取选中id
		function getCheckedId(){
			var nodes = $('#tt').tree('getChecked');
			var nodeId = [];
			var s = '';
			for(var i=0; i<nodes.length; i++){
				if (s != '') s += ',';
				s += nodes[i].id;
				nodeId.push(nodes[i].id);
			}
			alert(nodeId);
			return nodeId;
		}
		
		//弹出信息窗口 title:标题 msgString:提示信息 msgType:信息类型 [error,info,question,warning]
		function msgShow(title, msgString, msgType) {
			$.messager.alert(title, msgString, msgType);
		}
	</script>
</html>