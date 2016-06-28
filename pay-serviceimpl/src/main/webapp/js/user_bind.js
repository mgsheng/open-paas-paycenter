$(document).ready(function(){
	$(".btnbox").click(function(){
		var username=$("input[name='username']").val(); 
		var password=$("input[name='password']").val();
		if($("input[name='againpwd']").size()==0){
			if(username.length==0 || password.length==0){
				$(".mess").text("请将信息填写完整！");
				return false;
			}
			return true;
		}else{
			var againpwd=$("input[name='againpwd']").val();
			if(username.length==0 || password.length==0 || againpwd.length==0){
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
				return true;
			}
		}
	});
});
