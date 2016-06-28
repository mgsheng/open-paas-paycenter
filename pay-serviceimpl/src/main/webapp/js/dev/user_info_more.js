jQuery(document).ready(function() {
	userDetailValidate.init('${pageContext.request.contextPath}');
	userWorkValidate.init('${pageContext.request.contextPath}');
	//修改保存后 ;弹出验证消息
	if('${bool}'=='false'){
		alert('${message}');
	}
	initUserDictSelect();
});
function loadDictSelectData(id,data){
	var htmlStr = "<option value=''>请选择</option>";
	if(data!=null && data.length>0){
		jQuery(data).each(function(){
			htmlStr+="<option value='"+this.name+"'>"+this.name+"</option>";
		});
		jQuery('#'+id+'').append(htmlStr);
	}
}

//用户字典复选框
function initUserDictSelect(){
	var htmlStr = '';
	//学历
	loadDictSelectData('input_education',dictStaticData.xldata);
	loadDictSelectData('input_studyLevel',dictStaticData.xldata);
	//婚姻状态
	loadDictSelectData('input_marriage',dictStaticData.hydata);
	//政治面貌
	loadDictSelectData('input_politics',dictStaticData.zzdata);
	//民族
	loadDictSelectData('input_nation',dictStaticData.mzdata);
}

//加载详细信息div
function loadUserDetailDiv(){
	if(jQuery('#userDetailDiv').css('display')=='block'){
		jQuery('#userDetailDiv').hide();
	}else{
		jQuery('#userDetailDiv').show();
	}
}
//显示详细信息修改
function showDetailForm(){
	jQuery('#userDetailInfo').hide();
	jQuery('#userDetailForm').show();
	jQuery('#btn_detail_update').show();
	jQuery('#btn_detail').attr('onclick','showDetailInfo();');
	jQuery('#btn_detail').html('返回');
	
}
//显示详细信息详情
function showDetailInfo(){
	jQuery('#userDetailInfo').show();
	jQuery('#userDetailForm').hide();
	jQuery('#btn_detail_update').hide();
	jQuery('#btn_detail').attr('onclick','showDetailForm();');
	jQuery('#btn_detail').html('修改');
}
//详细信息提交
function userDetailSubmit(){
	if(validateUserDetail.valid()){
		$("#userDetailForm").submit();
	}
}

//加载教育信息div
function loadUserEducationDiv(){
	if(jQuery('#userEducationDiv').css('display')=='block'){
		jQuery('#userEducationDiv').hide();
	}else{
		jQuery('#userEducationDiv').show();
	}
}
//显示教育信息修改
function showEducationForm(){
	jQuery('#userEducationInfo').hide();
	jQuery('#userEducationForm').show();
	jQuery('#btn_education_update').show();
	jQuery('#btn_education').attr('onclick','showEducationInfo();');
	jQuery('#btn_education').html('返回');
}
//显示教育信息详情
function showEducationInfo(){
	jQuery('#userEducationInfo').show();
	jQuery('#userEducationForm').hide();
	jQuery('#btn_education_update').hide();
	jQuery('#btn_education').attr('onclick','showEducationForm();');
	jQuery('#btn_education').html('修改');
}
//教育信息提交
function educationSubmit(){
	$("#userEducationForm").submit();
}

//加载工作信息div
function loadUserWorkDiv(){
	if(jQuery('#userWorkDiv').css('display')=='block'){
		jQuery('#userWorkDiv').hide();
	}else{
		jQuery('#userWorkDiv').show();
	}
}
//显示工作信息修改
function showWorkForm(){
	jQuery('#userWorkInfo').hide();
	jQuery('#userWorkForm').show();
	jQuery('#btn_work_update').show();
	jQuery('#btn_work').attr('onclick','showWorkInfo();');
	jQuery('#btn_work').html('返回');
}
//显示工作信息详情
function showWorkInfo(){
	jQuery('#userWorkInfo').show();
	jQuery('#userWorkForm').hide();
	jQuery('#btn_work_update').hide();
	jQuery('#btn_work').attr('onclick','showWorkForm();');
	jQuery('#btn_work').html('修改');
}
//工作信息提交
function userWorkSubmit(){
	if(validateUserWork.valid()){
		$("#userWorkForm").submit();
	}
}

//加载社交信息div
function loadUserSocialDiv(){
	if(jQuery('#userSocialDiv').css('display')=='block'){
		jQuery('#userSocialDiv').hide();
	}else{
		jQuery('#userSocialDiv').show();
	}
}
//显示社交信息修改
function showSocialForm(){
	jQuery('#userSocialInfo').hide();
	jQuery('#userSocialForm').show();
	jQuery('#btn_social_update').show();
	jQuery('#btn_social').attr('onclick','showSocialInfo();');
	jQuery('#btn_social').html('返回');
}
//显示社交信息详情
function showSocialInfo(){
	jQuery('#userSocialInfo').show();
	jQuery('#userSocialForm').hide();
	jQuery('#btn_social_update').hide();
	jQuery('#btn_social').attr('onclick','showSocialForm();');
	jQuery('#btn_social').html('修改');
}
//社交信息提交
function userSocialSubmit(){
	$("#userSocialForm").submit();
}

var validateUserDetail;
var userDetailValidate = function() {
	return {
		//main function to initiate the module
		init : function(path) {
			validateUserDetail = $('#userDetailForm')
					.validate(
							{
								errorElement : 'div', //default input error message container
								errorClass : 'help-inline', // default input error message class
								focusInvalid : false, // do not focus the last invalid input
								rules : {
									age : {
										digits : true
									}
								},
								messages : {
									age : {
										digits : "必须输入整数！"
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
									form.submit();
								}
							});
		}
	};
}();

var validateUserWork;
var userWorkValidate = function() {
	return {
		//main function to initiate the module
		init : function(path) {
			validateUserWork = $('#userWorkForm')
					.validate(
							{
								errorElement : 'div', //default input error message container
								errorClass : 'help-inline', // default input error message class
								focusInvalid : false, // do not focus the last invalid input
								rules : {
									workContent : {
										maxlength: 2000
									}
								},
								messages : {
									workContent : {
										maxlength: "简历字数不能超过2000！"
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
									form.submit();
								}
							});
		}
	};
}();
