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
<body id="pub">
	<div id="ep" class="easyui-panel" title="公共权限" style="width:95%;max-width:100%;padding:0.1% 2%;height:400%;background-color: #F4F4F4;">
		<div style="float:top;padding:1% 0.01%;">
			<a href="#" class="easyui-linkbutton" onclick="submitPub()" style="font-weight: bolder;margin-left:0;padding:2px;">
				<span style="font-weight:bold;margin-right:5px;margin-left:10px;">确认</span>
				<span class="icon-ok">&nbsp;&nbsp;&nbsp;&nbsp;</span>
			</a>
			<a href="#" class="easyui-linkbutton" onclick="getBack()" style="font-weight: bolder;margin-left:20px;padding:2px;">
				<span style="font-weight:bold;margin-right:5px;margin-left:10px;">取消</span>
				<span class="icon-cancel">&nbsp;&nbsp;&nbsp;&nbsp;</span>
			</a> 
		</div>
		<div class="easyui-panel" style="padding:30px;overflow-x:scroll;height: 80%;" data-options="toolbar:'#tb'">
			<ul id="tt" class="easyui-tree" style="height: 100%" data-options="url:'${pageContext.request.contextPath}/privilegePublic/tree2', 
				method:'get',animate:true,checkbox:true,lines:true"></ul>
		</div>
	</div>
	
</body>
<script type="text/javascript">
	//修改操作
		function updatePub() {
        	var checkIds='';
        	var bool=false;
        	var ui = $('#tt').tree('getChecked', ['checked','indeterminate']);
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
            var url = encodeURI('${pageContext.request.contextPath}/privilegePublic/updatePublic?temp='+checkIds);
             $.post(url, function(data) {
            	 if(data.result==true){
	                 msgShow('系统提示', '恭喜，修改成功！', 'info');
	                //刷新
				      var url='${pageContext.request.contextPath}/privilegePublic/index';
				      reLoad(url);
                }else{
                	msgShow('系统提示', '修改失败！', 'error');
	                //刷新
				      var url='${pageContext.request.contextPath}/privilegePublic/index';
				      reLoad(url);
                }
            });
        }
      
		
		//提交
		function submitPub(){
			$.messager.confirm('系统提示','确认修改吗？',function(r){
				if(r){
					updatePub();
				}
			});	
		}
		//刷新
		function reLoad(url){
			window.location.href=url;
		}
		//取消
		function getBack(){
			$.messager.confirm('系统提示','确认取消吗？',function(r){
				if(r){
					var url= "${pageContext.request.contextPath}/privilegePublic/index";
					reLoad(url);
				}
			});	
		}
		//弹出信息窗口 title:标题 msgString:提示信息 msgType:信息类型 [error,info,question,warning]
		function msgShow(title, msgString, msgType) {
			$.messager.alert(title, msgString, msgType);
		}
	</script>
</html>