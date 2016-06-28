
var validateForm;

jQuery.validator.addMethod("passwordV1", function(value, element) { 
	var length = value.length; 
	var password = /^[0-9]*$/ 
	return !password.test(value); 
	}, "密码不能为纯数字");
jQuery.validator.addMethod("passwordV2", function(value, element) { 
	var length = value.length; 
	var password = /^[A-Z0-9a-z_]*$/ 
	return password.test(value); 
	}, "密码必须为6-20位字母、数字或英文下划线符号");


var validate = function() {
	return {
		//main function to initiate the module
		init : function() {
			validateForm = $('#form')
					.validate(
							{
								errorElement : 'div', //default input error message container
								errorClass : 'help-inline', // default input error message class
								focusInvalid : false, // do not focus the last invalid input
								rules : {
									oldpassword: {
				    					required: true
									},
									password: {
										required: true,
										rangelength: [ 6, 20 ],
										passwordV1: true,
										passwordV2: true
									},
									conform_password: {
										required: true,
										equalTo: "#password"
									}
								},

								messages : {
									oldpassword: {
										required: "原密码不能为空！",
										rangelength: "密码为6～20位！",
					                },
					                password: {
					                	required: "新密码不能为空！",
					                    rangelength: "密码为6～20位！"
					                },
					                conform_password: {
					                	required: "确认密码不能为空！",
					                	equalTo: "两次输入密码不一样！"
					                }
								},

								invalidHandler : function(event, validator) { //display error alert on form submit   
									formvalid = false;
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
									form.submit();
								}
							});
		}
	};
}();