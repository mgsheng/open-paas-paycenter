<!DOCTYPE html>
<html lang="en">
<head>

	<meta charset="utf-8" />

	<title>用户中心管理系统</title>

	<meta content="width=device-width, initial-scale=1.0" name="viewport" />

	<meta content="" name="description" />

	<meta content="" name="author" />

	<!-- BEGIN GLOBAL MANDATORY STYLES -->

	<link href="../../css/bootstrap.min.css" rel="stylesheet" type="text/css"/>

	<link href="../../css/font-awesome.min.css" rel="stylesheet" type="text/css"/>

	<link href="../../css/style-metro.css" rel="stylesheet" type="text/css"/>

	<link href="../../css/style.css" rel="stylesheet" type="text/css"/>

	<link href="../../css/wechat/public/loading.css" rel="stylesheet" type="text/css"/>

	<!-- END GLOBAL MANDATORY STYLES -->

	<!-- BEGIN PAGE LEVEL STYLES -->

	<link rel="stylesheet" type="text/css" href="../../css/select2_metro.css" />

	<link rel="stylesheet" href="../../css/DT_bootstrap.css" />

	<!-- END PAGE LEVEL STYLES -->

	<link rel="shortcut icon" href="../../img/open_logo.ico" />
	
	<link href="../../css/jquery-ui-1.8.17.custom.css" rel="stylesheet" />
	
	<link href="../../css/uniform.default.css" rel="stylesheet" type="text/css" />

</head>

<!-- END HEAD -->

<!-- BEGIN BODY -->

<body class="page-header-fixed">

	<!-- BEGIN PAGE CONTAINER-->        

	<div class="container-fluid">

		<!-- BEGIN PAGE HEADER-->

		<div class="row-fluid">

			<div class="span12">
				<!-- BEGIN PAGE TITLE & BREADCRUMB-->

				<h3 class="page-title">

					数据分析 <small>用户统计图</small>

				</h3>

				<ul class="breadcrumb">

					<li>

						<i class="icon-home"></i>

						<a href="#">数据分析</a>
						
						<i class="icon-angle-right"></i>

					</li>
					<li><a href="#">用户统计图</a></li>
					

				</ul>

				<!-- END PAGE TITLE & BREADCRUMB-->

			</div>

		</div>

		<!-- END PAGE HEADER-->

		<!-- BEGIN PAGE CONTENT-->

		<div class="row-fluid">

			<div class="span6 responsive" data-tablet="span12 fix-offset" data-desktop="span6" style="width: 100%">

				<!-- BEGIN EXAMPLE TABLE PORTLET-->

				<div class="portlet box grey"> 

					<div class="portlet-title">

						<div class="caption"><i class="icon-user"></i>用户统计图</div>

						<div class="actions">

						</div>

					</div>

					<div class="portlet-body">
						<div class="title">
							<table width="100%">
								<tr>
									<td align="right" width="100px">App名称：</td>
									<td>
										<input id="appSearchName" class="m-wrap small" type="text" readonly="readonly" onclick="showAppDiv()" style="cursor:pointer" />
										<a class="btn" href="javascript:showAppDiv()">选择</a>
									</td>
								</tr>
								<tr>	
									<td align="right">日期范围：</td>
									<td>
										<select id="dateSelect" onchange="loadDataChange()" style="width:60px;heigth:10px">
											<option value="yyyy-MM-ddd" selected="selected">天</option>
											<option value="yyyy-MM-ddw">周</option>
											<option value="yyyy-MM">月</option>
											<option value="yyyy">年</option>
										</select>&nbsp;&nbsp;
										<input name="startTime" id="startTime" class="WdateSearch" style="height:26px;cursor:pointer;margin-bottom: 10px" onfocus="WdatePicker({skin:'whyGreen',dateFmt:'yyyy-MM-dd',maxDate:'#F{$dp.$D(\'endTime\',{d:0});}'})" readonly="readonly" value="${startTime}"/>~<input name="endTime" id="endTime" class="WdateSearch" style="height:26px;cursor:pointer;margin-bottom: 10px" onfocus="WdatePicker({skin:'whyGreen',dateFmt:'yyyy-MM-dd',minDate:'#F{$dp.$D(\'startTime\',{d:0});}'})" readonly="readonly" value="${endTime}"/>
									</td>
								</tr>
								<tr>	
									<td align="right">曲线图：</td>
									<td>
										<input type="radio" name="qxRadio" id="xzchart" value="container" style="margin-left:0px" checked="checked"/><span style="cursor: pointer" onclick="showHideChart('xzchart','container')">新增用户曲线图</span>
										<input type="radio" name="qxRadio" id="uachart" value="containerActive" style="margin-left:0px" /><span style="cursor: pointer" onclick="showHideChart('uachart','containerActive')">活跃用户曲线图</span>
										<input type="radio" name="qxRadio" id="utchart" value="containerUser" style="margin-left:0px" /><span style="cursor: pointer" onclick="showHideChart('utchart','containerUser')">累积用户曲线图</span>
									</td>
								</tr>
								<tr>
									<td></td>
									<td  align="right">
										<a class="btn blue" href="javascript:showButton();">查询</a>&nbsp;&nbsp;
										<a c:if="${resourceCodesBtn.indexOf(\'exportchart')>=0}" href="javascript:exportHighcharts('pdf');" id="dcBtna" style="display:none" class="btn btn_default js_iconDel privilege_btn_export_chat">导出PDF</a>
										<form id="pdfform" action="/admin/userstatistics/exportchartuser.html" method="post">  
										  	<input type="hidden" name="svg" id="svg" />   
									   		<input type="hidden" name="type" id="type" /> 
										</form>
									</td>	
								</tr>
							</table>
						</div>
						<div id="container" style="width:1000px; height: 500px; margin: 0 auto;"></div>
						<div id="containerActive" style="display:none; width:1000px; height: 500px; margin: 0 auto;"></div>
						<div id="containerUser" style="display:none; width:1000px; height: 500px;margin: 0 auto;"></div>
					</div>
				</div>
				
				<!-- END EXAMPLE TABLE PORTLET-->
				
			</div>
		</div>

		<!-- END PAGE CONTENT-->

	</div>

	<!-- END PAGE CONTAINER-->

	<div class="div-loading"><i class="icon-loading"></i>&nbsp;数据加载中...</div>
	
	<!-- 选择app列表 -->
	<div id="select_app_div" style="display:none;" >
		<form id="update_account_form">
			<input type="hidden" id="appSelectIds" value="" />
			<table class="table table-striped table-bordered table-hover" id="page_app_show">
			</table>
			<div align="right"><a class="btn blue" href="javascript:selectApp();">选择</a></div>
			<table class="table table-striped table-bordered table-hover" id="page_app_select">
			</table>
			<div align="right"><a class="btn blue" href="javascript:delApp();">移除</a></div>
		</form>
	 </div>

	<!-- BEGIN JAVASCRIPTS(Load javascripts at bottom, this will reduce page load time) -->

	<!-- BEGIN CORE PLUGINS -->
	<script src="../../js/jquery-1.10.1.min.js" type="text/javascript"></script>
	<!-- IMPORTANT! Load jquery-ui-1.10.1.custom.min.js before bootstrap.min.js to fix bootstrap tooltip conflict with jquery ui tooltip -->
	<script src="../../js/jquery-ui-1.10.1.custom.min.js" type="text/javascript"></script>      
	<script src="../../js/bootstrap.min.js" type="text/javascript"></script>
	<!-- END CORE PLUGINS -->
	<!-- BEGIN PAGE LEVEL PLUGINS -->
	<script type="text/javascript" src="../../js/select2.min.js"></script>
	<script type="text/javascript" src="../../js/jquery.dataTables.js"></script>
	<script type="text/javascript" src="../../js/DT_bootstrap.js"></script>
	<script type="text/javascript" src="../../js/custom-boostrap-menu.js"></script> 
	<script type="text/javascript" src="../../js/calendar/WdatePicker.js"></script>  
	<script type="text/javascript" src="../../js/highcharts/highcharts.js"></script>
	<script type="text/javascript" src="../../js/highcharts/modules/exporting.js"></script>
	
	<!-- END PAGE LEVEL PLUGINS -->
	<!-- BEGIN PAGE LEVEL SCRIPTS -->
	<script src="../../js/table-managed.js"></script>
	<script src="../../js/app.js"></script>
	<script src="../../js/jquery.uniform.min.js" type="text/javascript"></script>
	<style type="text/css">
	.auto_hidden {
        width:204px;border-top: 1px solid #333;
        border-bottom: 1px solid #333;
        border-left: 1px solid #333;
        border-right: 1px solid #333;
        position:absolute;
        display:none;
    }
    .auto_show {
        width:204px;
        border-top: 1px solid #333;
        border-bottom: 1px solid #333;
        border-left: 1px solid #333;
        border-right: 1px solid #333;
        position:absolute;
        z-index:9999; /* 设置对象的层叠顺序 */
        display:block;
    }
    .auto_onmouseover{
        color:#ffffff;
        background-color:highlight;
        width:100%;
    }
    .auto_onmouseout{
        color:#000000;
        width:100%;
        background-color:#ffffff;
    }
    </style>
	<script>
		//ajax加载弹出层
		var htmlWidth = 0;
		var htmlHeight = 0;
		jQuery(document).ajaxStart(function(){
			htmlWidth = document.body.scrollWidth;
			htmlHeight = document.body.scrollHeight;
			jQuery('.div-loading').css('width',htmlWidth+"px");
			jQuery('.div-loading').css('height',htmlHeight+"px");
			jQuery('.div-loading').show();
		});
		jQuery(document).ajaxStop(function(){
			jQuery('.div-loading').hide();
		});
			
		var autoComplete;
		var appShowTable = null;//分页表定义
		var appSelectTable = null;//分页表定义
		jQuery(document).ready(function() {       
			App.init();
			selectMenu("menu_userstatisticschart",2);
            //加载app选择显示分页
            appPageShow();
            //加载app选中Ids的分页数据
			appPageSelect();
			chart();
            //曲线图单选框
			jQuery("input[name='qxRadio']").click(function(){
				jQuery("#container").hide();
				jQuery("#containerActive").hide();
				jQuery("#containerUser").hide();
				if(this.checked==true){
					jQuery("#"+this.value).show();
				}
			});
            //选择APP
			jQuery('#select_app_div').dialog({
				autoOpen:false,
				modal:true,
				draggable:false,
				resizable:false,
				width:700,
				title:'App列表选择查询'
			});
		});
		//弹出app选择框
		function showAppDiv(){
			jQuery('#select_app_div').dialog('open');
		}
		//日期下拉框变化时日期选择相应的变化
		function loadDataChange(){
			var dformat=jQuery('#dateSelect').val();
			if(jQuery('#dateSelect').val()=="yyyy-MM-ddd" || jQuery('#dateSelect').val()=="yyyy-MM-ddw"){
				dformat="yyyy-MM-dd"
			}	
			jQuery('#startTime').val("");
			jQuery('#endTime').val("");
			jQuery('#startTime').attr("onfocus","WdatePicker({skin:'whyGreen',dateFmt:'"+dformat+"',maxDate:'#F{$dp.$D(\\'endTime\\',{d:0});}'})");
			jQuery('#endTime').attr("onfocus","WdatePicker({skin:'whyGreen',dateFmt:'"+dformat+"',minDate:'#F{$dp.$D(\\'startTime\\',{d:0});}'})");
		}
		//曲线图显示与影藏
		function showHideChart(iid,did){
			jQuery("#"+iid).click();
			jQuery("#"+iid).click();
			jQuery("#container").hide();
			jQuery("#containerActive").hide();
			jQuery("#containerUser").hide();
			jQuery("#"+did).show();
		}
		//查询按钮
		function showButton(){
			if(jQuery('#appSelectIds').val()==""){
				alert("请选择需要查询的App");
			}
			else if(jQuery("#startTime").val()=="" || jQuery("#endTime").val()==""){
				alert("请选择两个日期范围");
			}
			else if(jQuery('#dateSelect').val()=="yyyy-MM-ddd" && getDateDiff(jQuery("#endTime").val(),jQuery("#startTime").val())>30){
				alert("天数的日期间隔不能超过30天")
			}
			else{
				chart();
			}
		}
		//计算两个日期相隔的天数
		function getDateDiff(startDate,endDate)
		{
			var startTime = new Date(Date.parse(startDate.replace(/-/g, "/"))).getTime();
			var endTime = new Date(Date.parse(endDate.replace(/-/g, "/"))).getTime();
			var dates = Math.abs((startTime - endTime))/(1000*60*60*24);
			return dates;
		} 
		//app查询分页数据
		function appPageShow(){
			appShowTable = $('#page_app_show').dataTable({
				"oLanguage": {
					"sLengthMenu":"条数：_MENU_",
					"sSearch": "App名称："
				},
				"bProcessing": true, 
				"bServerSide": true, 
				"bSort": false, 
				"aLengthMenu":[
					[5, 10],
					[5, 10] // change per page values here
				],
				"iDisplayLength": 5,
				"sServerMethod":"POST",
				"sAjaxSource": "/admin/userstatistics/appshowpage.json",
				//增加自定义参数
				"fnServerParams": function ( aoData ) { 
					aoData.push( 
						{"name":"appName", "value":$('#appText').val()},//增加自定义参数
						{"name":"notHasIds", "value":$('#appSelectIds').val()}//增加自定义参数
					);
				},
				"aoColumns":[
					{
						"sTitle": "<input type='checkbox' id='checkShowAll' onclick='jQuery(\"[name=appShowCheck]\").attr(\"checked\", this.checked)'/>",
						"mDataProp": "operate",
						"sClass" : "center",
						"sWidth" : "20px",
						"fnRender": function(obj){
							return '<input type="checkbox" id="appShowCheck" name="appShowCheck" value="'+obj.aData.id+'" />';
						}
					},{
						"sTitle": "app名称",
						"mDataProp": "name",
						"fnRender": function(obj){
							return '<span id="spAppSh'+obj.aData.id+'" >'+obj.aData.name+'</span>';
						}
					},{
						"sTitle": "登录类型",
						"mDataProp": "loginType"
					},{
						"sTitle": "类型",
						"mDataProp": "type"
					},{
						"sTitle": "创建人",
						"mDataProp": "user"
					}
				]
			});
			jQuery('#page_app_show_wrapper .dataTables_filter input').addClass("m-wrap small"); // modify table search input
			jQuery('#page_app_show_wrapper .dataTables_filter input').attr("id","appText");
		}
		//app选中Ids的分页数据
		function appPageSelect(){
			appSelectTable = $('#page_app_select').dataTable({
				"oLanguage": {
					"sLengthMenu":"条数：_MENU_",
					"sSearch": "App名称："
				},
				"bProcessing": true, 
				"bServerSide": true, 
				"bSort": false, 
				"aLengthMenu":[
					[5, 10],
					[5, 10] // change per page values here
				],
				"iDisplayLength": 5,
				"sServerMethod":"POST",
				"sAjaxSource": "/admin/userstatistics/appselectpage.json",
				//增加自定义参数
				"fnServerParams": function ( aoData ) { 
					aoData.push( 
						{"name":"appIds", "value":$('#appSelectIds').val()}//增加自定义参数
					);
				},
				"aoColumns":[
					{
						"sTitle": "<input type='checkbox' id='checkSelectAll' onclick='jQuery(\"[name=appSelectCheck]\").attr(\"checked\", this.checked)'/>",
						"mDataProp": "operate",
						"sClass" : "center",
						"sWidth" : "20px",
						"fnRender": function(obj){
							return '<input type="checkbox" id="appSelectCheck" name="appSelectCheck" value="'+obj.aData.id+'" />';
						}
					},{
						"sTitle": "app名称",
						"mDataProp": "name",
						"fnRender": function(obj){
							return '<span id="spAppSe'+obj.aData.id+'" >'+obj.aData.name+'</span>';
						}
					},{
						"sTitle": "登录类型",
						"mDataProp": "loginType"
					},{
						"sTitle": "类型",
						"mDataProp": "type"
					},{
						"sTitle": "创建人",
						"mDataProp": "user"
					}
				]
			});
			jQuery('#page_app_select_wrapper .dataTables_filter input').addClass("m-wrap small"); // modify table search input
			jQuery('#page_app_select_wrapper > .row-fluid:eq(0)').hide();
		}
		//曲线图
		function chart()
		{
			//去除Y轴的单位k、m
			Highcharts.setOptions({
				lang: {
			    	numericSymbols: []
			    }
			});
			$.post("/admin/userstatistics/chart.json",{
				dateSelect:jQuery('#dateSelect').val(),
				startTime:jQuery('#startTime').val(),
				endTime:jQuery('#endTime').val(),
				appIds:jQuery('#appSelectIds').val()
				},function(data){
					if(data.timeData!=null){
						var dataString=data.timeData;
					    $('#container').highcharts({
					        title: {
					            text: '新增用户曲线图',
					            x: -20 //center
						    },
					        xAxis: {
						        categories: dataString,
						        labels: {
						        	rotation: -45,
						        	align: 'right', 
						        	style: { 
						        		fontSize: '13px', 
						        		fontFamily: 'Verdana, sans-serif' 
						        	}
						        }
						    },
						    yAxis: {
						        title: {
						        	text: '人数(人)'
						        },
						        plotLines: [{
						            value: 0,
						            width: 1,
						            color: '#808080'
						        }]
						    },
						    exporting:{
						       	buttons: {
						        	contextButton: {
						            	enabled: false
						            }
						       	} 
					       	},
						    tooltip: {
						        valueSuffix: '人'
						    },
						    legend: {
						        layout: 'vertical',
						        align: 'right',
						        verticalAlign: 'middle',
						        borderWidth: 0
						    },
						    series: data.addUserTotalListMap
						});
						$('#containerActive').highcharts({
						    title: {
						        text: '活跃用户曲线图',
						        x: -20 //center
						    },
						    xAxis: {
						        categories: dataString,
						        labels: {
						        	rotation: -45,
						        	align: 'right', 
						        	style: { 
						        		fontSize: '13px', 
						        		fontFamily: 'Verdana, sans-serif' 
						        	}
						        }
						    },
						    yAxis: {
						        title: {
						            text: '人数(人)'
						        },
						    	plotLines: [{
						            value: 0,
						            width: 1,
						            color: '#808080'
						        }]
						    },
						    exporting:{
						       	buttons: {
						        	contextButton: {
						                enabled: false
						            }
						        }  
					       	},
						    tooltip: {
						        valueSuffix: '人'
						    },
						    legend: {
						        layout: 'vertical',
						        align: 'right',
						        verticalAlign: 'middle',
						        borderWidth: 0
						    },
						    series: data.userActiveTotalListMap
						});
						$('#containerUser').highcharts({
						    title: {
						    	text: '累积用户曲线图',
						        x: -20 //center
						    },
						    xAxis: {
						        categories: dataString,
						        labels: {
						        	rotation: -45,
						        	align: 'right', 
						        	style: { 
						        		fontSize: '13px', 
						        		fontFamily: 'Verdana, sans-serif' 
						        	}
						        }
						    },
						    yAxis: {
						        title: {
						            text: '人数(人)'
						        },
							    plotLines: [{
							        value: 0,
							        width: 1,
							        color: '#808080'
						        }]
						    },
						    exporting:{
						       	buttons: {
						        	contextButton: {
						                enabled: false
						            }
						        }  
					        },
						    tooltip: {
						        valueSuffix: '人'
						    },
						    legend: {
						        layout: 'vertical',
						        align: 'right',
						        verticalAlign: 'middle',
						        borderWidth: 0
						    },
						    series: data.userTotalListMap
						});
						
					}
					if(data.count>0){
						jQuery("#dcBtna").show();
					}
					else{
						jQuery("#dcBtna").hide();
					}
		        }
			);
		}
		//选择App
		function selectApp(){
			if(jQuery("input[name='appShowCheck']").length==0 || jQuery("input[name='appShowCheck']:checked").length==0){
				alert('请勾选需要查询的App！');
			}
			else{
				var selectNames=jQuery('#appSearchName').val();//显示选中的app名称
				var selectIds=jQuery('#appSelectIds').val();//选中的appIds
				var selectSz=selectIds.split(",");
				jQuery("input[name='appShowCheck']").each(function(){
					if(this.checked==true){
						if(selectIds==""){
							selectIds=jQuery(this).val();
							selectNames=jQuery("#spAppSh"+this.value).html();
						}	
						else{
							var isbool=false;
							for(var i=0;i<selectSz.length;i++){
								if(jQuery(this).val()==selectSz[i]){
									isbool=true;
								}
							}
							if(isbool==false){
								selectIds+=","+jQuery(this).val();
								selectNames+=","+jQuery("#spAppSh"+this.value).html();
							}
						}	
					}
				});
				jQuery('#appSearchName').val(selectNames);//显示选中的app名称
				jQuery('#appSelectIds').val(selectIds);//选中的appIds
				appShowTable.fnDraw();//刷新显示的app列表
				appSelectTable.fnDraw();//刷新选择的app列表
				jQuery("#checkShowAll").attr("checked", false);
			}
		}
		//移除App
		function delApp(){
			if(jQuery("input[name='appSelectCheck']").length==0 || jQuery("input[name='appSelectCheck']:checked").length==0){
				alert('请勾选需要移除的App！');
			}
			else{
				var selectNameSz=jQuery('#appSearchName').val().split(",");//原选中的app名称
				var selectIds=jQuery('#appSelectIds').val();//原选中的appIds
				var selectSz=selectIds.split(",");
				var newSelect="";//新选中的appIds
				var newSelectNames="";//新选中的app名称
				if(selectIds!=""){
					for(var i=0;i<selectSz.length;i++){
						var isbool=false;
						jQuery("input[name='appSelectCheck']").each(function(){
							if(this.checked==true && jQuery(this).val()==selectSz[i]){
								isbool=true;
							}
						});
						if(isbool==false){
							if(newSelect==""){
								newSelect=selectSz[i];
								newSelectNames=selectNameSz[i];
							}	
							else{
								newSelect+=","+selectSz[i];
								newSelectNames+=","+selectNameSz[i];
							}
						}
					}
				}
				jQuery('#appSearchName').val(newSelectNames);//显示选中的app名称
				jQuery('#appSelectIds').val(newSelect);//选中的appIds
				appShowTable.fnDraw();//刷新显示的app列表
				appSelectTable.fnDraw();//刷新选择的app列表
				jQuery("#checkSelectAll").attr("checked", false);
			}
		}
		//导出曲线图
		function exportHighcharts(type){
		    var chart_line = $("#container").highcharts();
		    var chart_active = $("#containerActive").highcharts();
		    var chart_user = $("#containerUser").highcharts();
		    var svg_line = chart_line.getSVG();  
		    var svg_active = chart_active.getSVG();
		    var svg_user = chart_user.getSVG();
		    var svg = svg_line+"_"+svg_active+"_"+svg_user;  
		    jQuery("#svg").val(svg);
		    jQuery("#type").val(type);
		    var formData=$("#pdfform").serialize();
			$.ajax({
				type: "POST",
				url: "/admin/userstatistics/exportchartuser.html",
				processData:true,
				data:formData,
				success: function(data){
					jQuery("#pdfform").prop("action", "/admin/userstatistics/exportchartuserdownload.html").submit();  
				},
				error:function(e) {
					alert("导出失败！");
				}
			});
		}
    </script>
</body>
<!-- END BODY -->
</html>