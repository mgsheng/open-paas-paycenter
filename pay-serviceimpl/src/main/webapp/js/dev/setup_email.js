
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
									}
								},
								messages : {
									email : {
										required : "请输入邮箱！",
										email : "必须输入正确格式的电子邮件！",
										remote : "邮箱已被占用，请重新输入！",
										maxlength: "邮箱长度不能超过50！"
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