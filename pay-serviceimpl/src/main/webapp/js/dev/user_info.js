
var uservalid = false;
function adminusercheck() {
	uservalid = true;
	$("#input_username").removeData("previousValue");
	$("#input_email").removeData("previousValue");
	$("#input_phone").removeData("previousValue");
	return true;
}

var validateInfo;
var validate = function() {
	return {
		//main function to initiate the module
		init : function(path) {
			validateInfo = $('#usrForm')
					.validate(
							{
								errorElement : 'div', //default input error message container
								errorClass : 'help-inline', // default input error message class
								focusInvalid : true, // do not focus the last invalid input
								rules : {
									realName : {
										required : true,
										maxlength: 25 
									},
									nickName : {
										required : true,
										maxlength: 100 
									},
									email : {
										required : true,
										email : true,
										remote : {
											url : path+"/dev/user/onlyEmail.json", //后台处理程序
											type : "post", //数据发送方式
											dataType : "json", //接受数据格式   
											data : { //要传递的数据
												id : function() {
													if ($("#input_id").length != 0) {
														return $("#input_id").val();
													}
													return 0;
												},
												name : function() {
													return $("#input_email").val();
												}
											}
										},
										maxlength: 50
									},
									phone : {
										digits : true,
										rangelength : [ 11, 11 ],
										remote : {
											url : path+"/dev/user/onlyPhone.json", //后台处理程序
											type : "post", //数据发送方式
											dataType : "json", //接受数据格式   
											data : { //要传递的数据
												id : function() {
													if ($("#input_id").length != 0) {
														return $("#input_id").val();
													}
													return 0;
												},
												name : function() {
													return $("#input_phone").val();
												}
											}
										}
									}
								},
								messages : {
									realName : {
										required : "请输入姓名！",
										maxlength: "姓名长度不能超过100！"
									},
									nickName : {
										required : "请输入昵称！",
										maxlength: "昵称长度不能超过100！"
									},
									email : {
										required : "请输入邮箱！",
										email : "必须输入正确格式的电子邮件！",
										remote : "邮箱已被占用，请重新输入！",
										maxlength: "邮箱长度不能超过50！"
									},
									phone : {
										digits : "必须输入整数！",
										rangelength : "手机号码为11位",
										remote : "手机号已被占用，请重新输入！"
									}
									
								},

								invalidHandler : function(event, validator) { //display error alert on form submit   
									uservalid = false;
								},

								highlight : function(element) { // hightlight error inputs
									$(element).addClass('frm_error'); // set error class to the control group
								},

								success : function(label) {
									label.parent().children('.frm_error').removeClass('frm_error');
								},

								errorPlacement : function(error, element) {
									error.addClass('help-small no-left-padding').insertAfter(
										element
									);
								},
								
								submitHandler : function(form) {
									if (uservalid) {
										form.submit();
									}
								}
							});
		}
	};
}();