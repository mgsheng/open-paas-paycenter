//查询用户问题
function findProblem(path) {
	jQuery.post(path+"/dev/user/userproblemlist.json",{
		},function(data){
			var htmlStr='<option value="">请选择</option>';
			if(data.problemData!=null && data.problemData.length>0){
				jQuery(data.problemData).each(function(){
					htmlStr+= '<option value="'+this.id+'">'+this.name+'</option>';
				});
			}
			jQuery('#problemId').html(htmlStr);
    	}
	);
}


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
									problemId: {
				    					required: true,
									},
									answer: {
				    					required: true,
				    					maxlength: 100
									}
								},

								messages : {
									problemId: {
										required: "请选择密保问题！"
									},
									answer: {
										required: "答案不能为空！",
										maxlength: "答案长度不能超过100！"
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