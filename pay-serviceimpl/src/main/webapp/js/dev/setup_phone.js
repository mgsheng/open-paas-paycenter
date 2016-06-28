
var validateForm;
var validate = function() {
	return {
		//main function to initiate the module
		init : function(path) {
			validateForm = $('#form')
					.validate(
							{
								errorElement : 'div', //default input error message container
								errorClass : 'help-inline', // default input error message class
								focusInvalid : false, // do not focus the last invalid input
								rules : {
									phone : {
										required : true,
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
									},
									code:{
										required : true
									}
								},
								messages : {
									phone : {
										required : "请输入新手机号！",
										digits : "必须输入整数！",
										rangelength : "手机号码为11位",
										remote : "手机号已被占用，请重新输入！"
									},
									code:{
										required : "请输入验证码！"
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