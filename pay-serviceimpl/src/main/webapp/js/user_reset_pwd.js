$(document).ready(function(){
	$(".btnbox").click(function(){
		var password=$("input[name='password']").val(); 
		var againpwd=$("input[name='againpwd']").val(); 
		if($.trim(password).length>15 || $.trim(password).length<6){
			$(".mess").text("密码必须由6-15位字母或数字组成！");
			return false;
		}
		if(againpwd!=password){
			$(".mess").text("重复密码与设置密码不同！");
			return false;
		}
		return true;
	});
});
