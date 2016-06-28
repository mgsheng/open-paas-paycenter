var validateForm;
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
									username: {
				    					required: true,
										rangelength: [ 1, 16 ]
									},
									password: {
										required: true,
										rangelength: [ 6, 20 ]
									}
								},

								messages : {
									username: {
										required: "用户名不能为空！",
										rangelength: "密码为1～16位！",
					                },
					                password: {
					                	required: "密码不能为空！",
					                    rangelength: "密码为6～20位！"
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