<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<!DOCTYPE HTML>
<html>
<head id="Head1">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>奥鹏教育</title>
<link href="${pageContext.request.contextPath}/css/default.css"
	rel="stylesheet" type="text/css" />
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/css/themes/default/easyui.css" />
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/css/themes/icon.css" />
<script type="text/javascript"
	src="${pageContext.request.contextPath}/js/jquery-1.4.4.min.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/js/jquery.easyui.min.1.2.2.js"></script>
	
<script type="text/javascript"
	src="${pageContext.request.contextPath}/js/locale/easyui-lang-zh_CN.js"></script>
<script type="text/javascript"
	src='${pageContext.request.contextPath}/js/outlook2.js'> </script>
<script type="text/javascript">
	 var _menus = {"menus":[
						{"menuid":"1","icon":"icon-sys","menuname":"经营分析",
							"menus":[
									{"menuid":"15","menuname":"交易数据","icon":"icon-set","url":"${pageContext.request.contextPath}/user/stats"},
									{"menuid":"16","menuname":"渠道费率维护","icon":"icon-nav","url":"${pageContext.request.contextPath}/paychannel/rate"},
									{"menuid":"17","menuname":"线下收费维护","icon":"icon-nav","url":"${pageContext.request.contextPath}/manage/offlineOrderPages"},
									{"menuid":"18","menuname":"退费单据维护","icon":"icon-nav","url":"${pageContext.request.contextPath}/manage/refundOrderPages"}
								]
						},{"menuid":"8","icon":"icon-sys","menuname":"订单查询",
							"menus":[{"menuid":"21","menuname":"交易明细","icon":"icon-nav","url":"${pageContext.request.contextPath}/manage/skipPages"},
									{"menuid":"22","menuname":"用户订单查询","icon":"icon-nav","url":"${pageContext.request.contextPath}/manage/userOrderPages"},
									{"menuid":"23","menuname":"账户流水记录查询","icon":"icon-nav","url":"${pageContext.request.contextPath}/running/accountRunningPages"}
								]
						},{"menuid":"2","icon":"icon-sys","menuname":"财务报表",
							"menus":[
									{"menuid":"40","menuname":"渠道营收","icon":"icon-nav","url":"${pageContext.request.contextPath}/paychannel/channelRevenue"},
									{"menuid":"41","menuname":"银行缴费","icon":"icon-nav","url":"${pageContext.request.contextPath}/payment/bankPayment"},
									{"menuid":"42","menuname":"缴费方式","icon":"icon-nav","url":"${pageContext.request.contextPath}/payment/otherPayment"}
								]
						},{"menuid":"","icon":"icon-sys","menuname":"用户管理",
							"menus":[
										   {"menuid":"24","menuname":"用户信息列表","icon":"icon-man","url":"${pageContext.request.contextPath}/managerUser/userList"}							]
						},{"menuid":"","icon":"icon-sys","menuname":"权限管理",
							"menus":[{"menuid":"26","menuname":"资源管理","icon":"icon-nav","url":"${pageContext.request.contextPath}/resource/index"},
									 {"menuid":"27","menuname":"模块管理","icon":"icon-nav","url":"${pageContext.request.contextPath}/module/index"},
									 {"menuid":"28","menuname":"角色管理","icon":"icon-role","url":"${pageContext.request.contextPath}/managerRole/roleMessage"},
									 {"menuid":"29","menuname":"公共权限","icon":"icon-set","url":"${pageContext.request.contextPath}/privilegePublic/index"}
								   ]
						},{"menuid":"","icon":"icon-sys","menuname":"部门管理",
							"menus":[
										   {"menuid":"30","menuname":"部门信息列表","icon":"icon-role","url":"${pageContext.request.contextPath}/department/departmentList"},
										   {"menuid":"31","menuname":"商户管理","icon":"icon-role","url":"${pageContext.request.contextPath}/commercial/commercial"},
										   {"menuid":"32","menuname":"渠道管理","icon":"icon-role","url":"${pageContext.request.contextPath}/irrigation/irrigation"}			]
						},{"menuid":"","icon":"icon-sys","menuname":"公共管理",
							"menus":[
										   {"menuid":"33","menuname":"日志管理","icon":"icon-role","url":"${pageContext.request.contextPath}/privilegeLog/privilegeLog"}			]
						}
				]};
        //设置登录窗口
        function openPwd() {
            $('#w').window({
                title: '修改密码',
                width: 300,
                modal: true,
                shadow: true,
                closed: true,
                height: 160,
                resizable:false
            });
        }
        //关闭登录窗口
        function closePwd() {
            $('#w').window('close');
        }

        

        //修改密码
        function serverLogin() {
            var $newpass = $('#txtNewPass');
            var $rePass = $('#txtRePass');

            if ($newpass.val() == '') {
                msgShow('系统提示', '请输入密码！', 'warning');
                return false;
            }
            if ($rePass.val() == '') {
                msgShow('系统提示', '请在一次输入密码！', 'warning');
                return false;
            }

            if ($newpass.val() != $rePass.val()) {
                msgShow('系统提示', '两次密码不一至！请重新输入', 'warning');
                return false;
            }
            var userName="${userName}";
            $.post('${pageContext.request.contextPath}//user/updatePassword?newpass=' + $newpass.val()+'&userName='+userName, function(msg) {
                msgShow('系统提示', '恭喜，密码修改成功！', 'info');
                $newpass.val('');
                $rePass.val('');
                close();
                $('#w').window('close');
            });
            
        }

        $(function() {

            openPwd();

            $('#editpass').click(function() {
                $('#w').window('open');
            });

            $('#btnEp').click(function() {
                serverLogin();
            });

			$('#btnCancel').click(function(){closePwd();});

            $('#loginOut').click(function() {
                $.messager.confirm('系统提示', '您确定要退出本次登录吗?', function(r) {

                    if (r) {
                      <%
                      session.invalidate();
                      %>
                        location.href = '${pageContext.request.contextPath}/index.jsp';
                    }
                });
            });
        });
		
    </script>

</head>
<body class="easyui-layout" style="overflow-y: hidden" scroll="no">
	<noscript>
		<div
			style=" position:absolute; z-index:100000; height:2046px;top:0px;left:0px; width:100%; background:white; text-align:center;">
			<img
				src="${pageContext.request.contextPath}/images/CategorizeMenu.png"
				alt='抱歉，请开启脚本支持！' />
		</div>
	</noscript>
	<div region="north" split="true" border="false"
		style="overflow: hidden; height: 50px;
        background: url(images/layout-browser-hd-bg.gif) #7f99be repeat-x center 50%;
        line-height: 20px;color: #fff; font-family: Verdana, 微软雅黑,黑体">
		<span style="float:right; padding-right:20px;padding-top: 20px;"class="head">
		
		           欢迎您<span id="realName"> ${realName}</span> <a href="#" id="editpass">修改密码</a> 
			<a href="#" id="loginOut">安全退出</a>
		</span> 
		<span style="padding-left:10px; font-size: 16px;  ">
		<img  src="${pageContext.request.contextPath}/images/open_logo.png"
			width="140" height="45" align="absmiddle" />
		</span>
	</div>
	<div region="south" split="true"
		style="height: 30px; background: #D2E0F2; ">
		<div class="footer">
		           版权所有：奥鹏教育 Copyright ©2003-2015 open.com.cn
			ALL rights reserved  登记序号：京ICP备12003892号-3 京ICP证150086号
			公安机关备案号：110102005577号-4</div>
	</div>
	<div region="west" hide="true" split="true" title="导航菜单"
		style="width:180px;" id="west">
		<div id="nav" class="easyui-accordion" fit="true" border="false">
			<!--  导航内容 -->
			
		</div>

	</div>
	<div id="mainPanle" region="center"
		style="background: #eee; overflow-y:hidden">
		<div id="tabs" class="easyui-tabs" fit="true" border="false">
			<div title="首页" style="padding:20px;overflow:hidden; color:red; ">
				 <img src="${pageContext.request.contextPath}/images/welcome_1.png" style="display: block;width: 40%;margin:20px auto;">
			</div>
		</div>
	</div>


	<!--修改密码窗口-->
	<div id="w" class="easyui-window" title="修改密码" collapsible="false"
		minimizable="false" maximizable="false" icon="icon-save"
		style="width: 300px; height: 150px; padding: 5px;
        background: #fafafa;">
		<div class="easyui-layout" fit="true">
			<div region="center" border="false"
				style="padding: 10px; background: #fff; border: 1px solid #ccc;">
				<table cellpadding=3>
					<tr>
						<td>新密码：</td>
						<td><input id="txtNewPass" type="text" class="txt01" />
						</td>
					</tr>
					<tr>
						<td>确认密码：</td>
						<td><input id="txtRePass" type="text" class="txt01" />
						</td>
					</tr>
				</table>
			</div>
			<div region="south" border="false"
				style="text-align: right; height: 30px; line-height: 30px;">
				<a id="btnEp" class="easyui-linkbutton" icon="icon-ok"
					href="javascript:void(0)"> 确定</a> <a id="btnCancel"
					class="easyui-linkbutton" icon="icon-cancel"
					href="javascript:void(0)">取消</a>
			</div>
		</div>
	</div>

	<div id="mm" class="easyui-menu" style="width:150px;">
		<div id="mm-tabupdate">刷新</div>
		<div class="menu-sep"></div>
		<div id="mm-tabclose">关闭</div>
		<div id="mm-tabcloseall">全部关闭</div>
		<div id="mm-tabcloseother">除此之外全部关闭</div>
		<div class="menu-sep"></div>
		<div id="mm-tabcloseright">当前页右侧全部关闭</div>
		<div id="mm-tabcloseleft">当前页左侧全部关闭</div>
		<div class="menu-sep"></div>
		<div id="mm-exit">退出</div>
	</div>


</body>
</html>