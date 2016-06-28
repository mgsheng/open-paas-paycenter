<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
       <meta name="viewport" content="width=device-width, initial-scale=1">
		<title>找回密码</title>
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/style3.css" />
		<link href="${pageContext.request.contextPath}/images/open_logo.ico" rel="Shortcut Icon" />
		<style type="text/css">
			.div_error{
				color: red;
			}
		</style>
	</head>
	<body class="page_setting pwd page_introduction">
		<%@ include file="/dev_head.jsp"%>
		<!--找回密码 star-->
		<div class="layout">
			<div class="w_1200">
				<div class="getbackpwd">
					<div class="stepbar">
						<div class="first"><span class="num">1</span>选择密码找回方式</div>
						<div class="second"><span class="num">2</span>验证身份</div>
						<div class="third  curr"><span class="num">3</span>修改密码</div>
					</div>
					<div class="main">
						<table id="main_table" cellpadding="0" cellspacing="0" class="tbl-findpwd">
							<tr>
								<td>
									<input type="password" id="pwd_new" class="input-len newpwd" placeholder="密码为6-20位字符组成，区分大小写且不能为纯数字" onkeyup=pwStrength(this.value) onblur=pwStrength(this.value) />
									<div id="pwd_new_error" class="div_error"></div>
								</td>
								<td>
									<table class="verification" cellpadding="0" cellspacing="0">
										<tr>
											<td width="33%" id="strength_L">弱</td>
											<td width="33%" id="strength_M">中</td>
											<td width="33%" id="strength_H">强</td>
										</tr>
									</table>  
								</td>
							</tr>
							<tr>
								<td colspan="2">
									<input type="password" id="pwd_re" class="input-len renewpwd" placeholder="再次输入新密码" />
									<div id="pwd_re_error" class=""></div>
								</td>
							</tr>
							<tr>
								<td colspan="2"><input type="button" class="btn-len complete" value="完成" /></td>
							</tr>
						</table>
					</div>
				</div>
			</div>
		</div>
		<!--找回密码 end-->
		<%@ include file="/dev_foot.jsp"%>
		<script type="text/javascript">
		jQuery(document).ready(function() {
			$(".complete").click(function () {
				jQuery('.input-len').removeClass('frm_error');
				jQuery('#pwd_new_error').html('');
				
				if ($("#pwd_new").val($("#pwd_new").val()) == "") {
					jQuery('#pwd_new').addClass('frm_error');
					jQuery('#pwd_new_error').html('请输入密码');
				}else if(checkTS($("#pwd_new").val())<=0){
				    jQuery('#pwd_new').addClass('frm_error');
					jQuery('#pwd_new_error').html('不能输入除下划线以为的特殊字符');
				}else if(checkCSZ($("#pwd_new").val())<=0){
				    jQuery('#pwd_new').addClass('frm_error');
					jQuery('#pwd_new_error').html('密码不能为纯数字');
				}
				else if(checkStrong($("#pwd_new").val())<2){
					jQuery('#pwd_new').addClass('frm_error');
					jQuery('#pwd_new_error').html('密码为6-20数字字母下划线组合，不能为纯数字');
				}
				else if ($("#pwd_re").val() == "") {
					jQuery('#pwd_re').addClass('frm_error');
					jQuery('#pwd_re_error').html('请输入密码');
				}
				else if ($("#pwd_new").val() != $("#pwd_re").val()) {
					jQuery('#pwd_re').addClass('frm_error');
					jQuery('#pwd_re_error').html('两次密码不一致');
				}
				else{
					$.post("${pageContext.request.contextPath}/dev/user/reset_password",
						{code:'${code}',phone:'${phone}',password:jQuery('#pwd_new').val(),type:'${type}'},
		   				function(data){
		   				//console.log(data);
		   					if(data.flag){
		   						var html = '';
		   						html += '<tr>';
								html += '<td colspan="2"><span class="fts-1">密码修改成功！</span></td>';
								html += '</tr>';
		   						jQuery('#main_table').html(html);
		   						alert("修改密码成功！，按\"确定\"去登录");
		   						 var system = {
							            win: false,
							            mac: false,
							            xll: false,
							            ipad:false
							        };
							        //检测平台
							        var p = navigator.platform;
							        system.win = p.indexOf("Win") == 0;
							        system.mac = p.indexOf("Mac") == 0;
							        system.x11 = (p == "X11") || (p.indexOf("Linux") == 0);
							        system.ipad = (navigator.userAgent.match(/iPad/i) != null)?true:false;
							        //跳转语句，如果是手机访问就自动跳转到wap.baidu.com页面
							        if (system.win || system.mac || system.xll||system.ipad) {
							          window.location.href='http://www.open.com.cn/login.aspx';
							        } else {
							          window.location.href='http://www.open.com.cn';
							        }
		   					}
		   					else if(data.errorCode=='time_out'){
	   							  jQuery('#pwd_new').addClass('frm_error');
					              jQuery('#pwd_new_error').html('验证码超时');
		   						window.location.href='${pageContext.request.contextPath}/findpwd';
		   					}
		   					else if(data.errorCode=='invalid_data'){
	   							  jQuery('#pwd_new').addClass('frm_error');
					              jQuery('#pwd_new_error').html('验证码无效');
		   						window.location.href='${pageContext.request.contextPath}/findpwd';
		   					}
		   				}
	   				);
				}
			});
		});
		function checkpassword(password){
		  var reg = /^\w+$/;
          if(reg.test(password)){
          jQuery('#pwd_new').addClass('frm_error');
		  jQuery('#pwd_new_error').html('');
          }
		}
		//判断输入密码的类型
		function CharMode(iN) {
			if (iN >= 48 && iN <= 57) //数字
			return 1;
			if (iN >= 65 && iN <= 90) //大写
			return 2;
			if (iN >= 97 && iN <= 122) //小写
			return 4;
			else
			return 8;
		}
		
		//bitTotal函数
		//计算密码模式
		function bitTotal(num) {
			modes = 0;
			for (i = 0; i < 4; i++) {
				if (num & 1) modes++;
				num >>>= 1;
			}
			return modes;
		}
		
		//返回强度级别
		function checkStrong(sPW) {
			if (sPW.length <6)
			return 0; //密码太短
			Modes = 0;
			//*  !"#$%&'()*+,-./ (ASCII码：33~47)
            // * :;<=>?@ (ASCII码：58~64) [\]^_` (ASCII码：91~96) {|}~
			for (i = 0; i < sPW.length; i++) {
				Modes |= CharMode(sPW.charCodeAt(i));
			}
			//console.log("Modes==="+bitTotal(Modes));
			return bitTotal(Modes);
		}
		//验证特殊字符
		function checkTS(sPW) {
			//*  !"#$%&'()*+,-./ (ASCII码：33~47)
            // * :;<=>?@ (ASCII码：58~64) [\]^_` (ASCII码：91~96) {|}~
            var returnVlaue=0;
			for (i = 0; i < sPW.length; i++) {
			    if(sPW.charCodeAt(i)>=33&&sPW.charCodeAt(i)<=47){
			    returnVlaue= 0;
			    break;
			    }else if(sPW.charCodeAt(i)>=58&&sPW.charCodeAt(i)<=64){
			    returnVlaue= 0;
			     break;
			    }else if(sPW.charCodeAt(i)>=91&&sPW.charCodeAt(i)<95){
			    returnVlaue= 0;
			     break;
			    }else if(sPW.charCodeAt(i)==96){
			    returnVlaue= 0;
			     break;
			    }else{
			    returnVlaue=1;
			    }
			}
			return returnVlaue;
		}
		//验证不能为纯数字
		function checkCSZ(sPW) {
			 var returnVlaue=0;
			for (i = 0; i < sPW.length; i++) {
			   if (sPW.charCodeAt(i) >= 48 && sPW.charCodeAt(i) <= 57) {
			    returnVlaue|=0;
			   }else{
			    returnVlaue|=1;
			   }
			   
			}
			return returnVlaue;
		}
		
		//显示颜色
		function pwStrength(pwd) {
			jQuery('.input-len').removeClass('frm_error');
			jQuery('#pwd_new_error').html('');
			O_color = "#eeeeee";
			L_color = "#FF0000";
			M_color = "#FF9900";
			H_color = "#33CC00";
			if (pwd == null || pwd == '') {
				Lcolor = Mcolor = Hcolor = O_color;
			}
			else {
				S_level = checkStrong(pwd);
				switch (S_level) {
					case 0:
					Lcolor = Mcolor = Hcolor = O_color;
					case 1:
					Lcolor = L_color;
					Mcolor = Hcolor = O_color;
					break;
					case 2:
					Lcolor = Mcolor = M_color;
					Hcolor = O_color;
					break;
					default:
					Lcolor = Mcolor = Hcolor = H_color;
				}
			}
			document.getElementById("strength_L").style.background = Lcolor;
			document.getElementById("strength_M").style.background = Mcolor;
			document.getElementById("strength_H").style.background = Hcolor;
			return;
		}
		</script>
	</body>
</html>