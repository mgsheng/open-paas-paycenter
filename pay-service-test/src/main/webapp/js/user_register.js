$(document).ready(function(){
	$(".btnbox").click(function(){
		var username=$("input[name='username']").val(); 
		var password=$("input[name='password']").val(); 
		var againpwd=$("input[name='againpwd']").val(); 
		var email=$("input[name='email']").val(); 
		var phone=$("input[name='phone']").val();
		if(username.length==0 || password.length==0 || againpwd.length==0 || email.lenght==0 || phone.lenght==0){
			$(".mess").text("请将信息填写完整！");
			return false;
		}else{
			if(username.length>15){
				$(".mess").text("用户名长度不能超过15位！");
				return false;
			}
			if($.trim(password).length>15 || $.trim(password).length<6){
				$(".mess").text("密码必须由6-15位字母或数字组成！");
				return false;
			}
			if(againpwd!=password){
				$(".mess").text("重复密码与设置密码不同！");
				return false;
			}
			var reg1 = /^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+(\..*)$/;
			if(!reg1.test(email)){
				$(".mess").text("请填写正确邮箱！");
				return false;
			}	
			var phone1=/^([1])([1-9]{1}[0-9]{9})?$/;
			if(phone.length!=11 || !phone.match(phone1)){
				$(".mess").text("请填写正确联系方式！");
				return false;
			}
			if(document.getElementById("agree").checked==false){
				$(".mess").text("您还没同意用户协议及服务条款！");
				return false;
			}
			return true;
		}
	});
});
