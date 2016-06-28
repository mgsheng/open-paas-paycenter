<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!--详细信息-->
<div class="basic_info_title">
	<a id="btn_detail" class="revise_info" href="javascript:void(0);" onclick="showDetailForm();">修改</a>
	<a id="btn_detail_update" class="revise_info" href="javascript:void(0);" onclick="userDetailSubmit();" style="margin-right: 10px;display: none;">保存</a>
	详细信息
</div>
<ul id="userDetailInfo" class="basic_info_text clearfix">
	<li>
		<span>性&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;别：</span>
		<c:if test="${userDetail_sex == 0}">
			男
		</c:if>
		<c:if test="${userDetail_sex == 1}">
			女
		</c:if>
	</li>
	<li><span>民&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;族：</span>${userDetail_nation}</li>
	<li><span>政治面貌：</span>${userDetail_politics}</li>
	<li><span>出生日期：</span>${userDetail_birthday}</li>
	<li><span>年&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;龄：</span>${userDetail_age}</li>
	<li><span>婚姻状态：</span>${userDetail_marriage}</li>
	<li><span>学&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;历：</span>${userDetail_education}</li>
	<li><span>职&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;业：</span>${userDetail_occupation}</li>
	<li><span>省&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;市：</span>${userDetail_province}</li>
	<li><span>城&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;市：</span>${userDetail_city}</li>
	<li><span>区&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;域：</span>${userDetail_region}</li>
	<li></li>
</ul>
<form id="userDetailForm" style="display: none;" action="${pageContext.request.contextPath}/dev/user/user_info" method="post">
	<input type="hidden" id="input_appUid" name="appUid" value="${appUid}" />
	<input type="hidden" name="dataType" value="userDetail" />
	<ul class="basic_info_text clearfix">
		<li>
			<span>性&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;别：</span>
			<select id="input_sex" name="sex" value="${userDetail_sex}">
				<c:if test="${userDetail_sex == 0}">
				   <option value="0" selected="selected">男</option>
				   <option value="1">女</option>
				</c:if>
				<c:if test="${userDetail_sex == 1}">
					<option value="0">男</option>
				   <option value="1" selected="selected">女</option>
				</c:if>
				<c:if test="${userDetail_sex == null || userDetail_sex == ''}">
					<option value="0">男</option>
				   <option value="1">女</option>
				</c:if>
			</select>
		</li>
		<li><span>民&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;族：</span><select id="input_nation" name="nation"></select> </li>
		<li><span>政治面貌：</span><select id="input_politics" name="politics"></select></li>
		<li><span>出生日期：</span><input id="input_birthday" type="text"  name="birthday" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd'})" readonly="readonly" value="${userDetail_birthday}"/></li>
		<li><span>年&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;龄：</span><input id="input_age" type="text" name="age" value="${userDetail_age}" maxlength="3" /></li>
		<li><span>婚姻状态：</span><select id="input_marriage" name="marriage"></select></li>
		<li><span>学&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;历：</span><select id="input_education" name="education"></select></li>
		<li><span>职&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;业：</span><input id="input_occupation" type="text" name="occupation" value="${userDetail_occupation}" maxlength="25" /></li>
		<li id="city_select"><span>省&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;市：</span><select id="input_province" class="prov" name="province"></select></li>
		<li><span>城&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;市：</span><select id="input_city" class="city" disabled="disabled" style="display:none;" name="city"></select></li>
		<li><span>区&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;域：</span><select id="input_region" class="dist" disabled="disabled" style="display:none;" name="region" ></select></li>
		<li></li>
	</ul>
</form>
<!--详细信息-->
<!--教育信息-->
<div class="basic_info_title">
	<a id="btn_education" class="revise_info" href="javascript:void(0);" onclick="showEducationForm();">修改</a>
	<a id="btn_education_update" class="revise_info" href="javascript:void(0);" onclick="educationSubmit();" style="margin-right: 10px;display: none;">保存</a>
	教育信息
</div>
<!--默认基本资料-->
<ul id="userEducationInfo" class="basic_info_text clearfix">
	<li><span>学&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;校：</span>${userEducation_school}</li>
	<li><span>专&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;业：</span>${userEducation_major}</li>
	<li><span>学&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;历：</span>${userEducation_studyLevel}</li>
	<li><span>开始时间：</span>${userEducation_startDate}</li>
	<li><span>毕业时间：</span>${userEducation_endDate}</li>
	<li><span>学习描述：</span>${userEducation_description}</li>
	<li><span>学校地址：</span>${userEducation_address}</li>
	<li></li>
</ul>
<form id="userEducationForm" style="display: none;" action="${pageContext.request.contextPath}/dev/user/user_info" method="post">
	<input type="hidden" id="input_appUid" name="appUid" value="${appUid}" />
	<input type="hidden" name="dataType" value="userEducation" />
	<ul class="basic_info_text clearfix">
		<li><span>学&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;校：</span><input id="input_school" type="text" name="school" value="${userEducation_school}" maxlength="25" /></li>
		<li><span>专&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;业：</span><input id="input_major" type="text" name="major" value="${userEducation_major}" maxlength="25" /></li>
		<li><span>学&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;历：</span><select id="input_studyLevel" name="studyLevel"></select></li>
		<li><span>开始时间：</span><input id="input_userEducation_startDate" type="text" name="startDate"  onfocus="WdatePicker({skin:'whyGreen',maxDate:'#F{$dp.$D(\'input_userEducation_endDate\',{d:0});}',dateFmt:'yyyy-MM-dd'})" readonly="readonly" value="${userEducation_startDate}"/></li>
		<li><span>毕业时间：</span><input id="input_userEducation_endDate" type="text" name="endDate" onfocus="WdatePicker({skin:'whyGreen',minDate:'#F{$dp.$D(\'input_userEducation_startDate\',{d:0});}',dateFmt:'yyyy-MM-dd'})" readonly="readonly" value="${userEducation_endDate}"/></li>
		<li><span>学习描述：</span><input id="input_description" type="text" name="description" value="${userEducation_description}" maxlength="200" /></li>
		<li><span>学校地址：</span><input id="input_address" type="text" name="address" value="${userEducation_address}" maxlength="200" /></li>
		<li></li>
	</ul>
</form>
<!--教育信息-->
<!--工作信息-->
<div class="basic_info_title">
	<a id="btn_work" class="revise_info" href="javascript:void(0);" onclick="showWorkForm();">修改</a>
	<a id="btn_work_update" class="revise_info" href="javascript:void(0);" onclick="userWorkSubmit();" style="margin-right: 10px;display: none;">保存</a>
	工作信息
</div>
<ul id="userWorkInfo" class="basic_info_text clearfix">
	<li><span>公&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;司：</span>${userWork_company}</li>
	<li><span>职&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;位：</span>${userWork_position}</li>
	<li><span>岗位职责：</span>${userWork_responsibility}</li>
	<li><span>开始时间：</span>${userWork_beginDate}</li>
	<li><span>结束时间：</span>${userWork_endDate}</li>
	<li><span>工作描述：</span>${userWork_description}</li>
	<li><span>公司地址：</span>${userWork_address}</li>
	<li><span>简&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;历：</span>${userWork_workContent}</li>
	<li></li>
</ul>
<form id="userWorkForm" style="display: none;" action="${pageContext.request.contextPath}/dev/user/user_info" method="post">
	<input type="hidden" id="input_appUid" name="appUid" value="${appUid}" />
	<input type="hidden" name="dataType" value="userWork" />
	<ul class="basic_info_text clearfix">
		<li><span>公&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;司：</span><input id="input_company" type="text" name="company" value="${userWork_company}" maxlength="25" /></li>
		<li><span>职&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;位：</span><input id="input_position" type="text" name="position" value="${userWork_position}" maxlength="25" /></li>
		<li><span>岗位职责：</span><input id="input_responsibility" type="text" name="responsibility" value="${userWork_responsibility}" maxlength="128" /></li>
		<li><span>开始时间：</span><input id="input_userWork_beginDate" type="text" name="beginDate"  onfocus="WdatePicker({skin:'whyGreen',maxDate:'#F{$dp.$D(\'input_userWork_endDate\',{d:0});}',dateFmt:'yyyy-MM-dd'})" readonly="readonly" value="${userWork_beginDate}"/></li>
		<li><span>结束时间：</span><input id="input_userWork_endDate" type="text" name="endDate" onfocus="WdatePicker({skin:'whyGreen',minDate:'#F{$dp.$D(\'input_userWork_beginDate\',{d:0});}',dateFmt:'yyyy-MM-dd'})" readonly="readonly" value="${userWork_endDate}"/></li>
		<li><span>工作描述：</span><input id="input_description" type="text" name="description" value="${userWork_description}" maxlength="200" /></li>
		<li><span>公司地址：</span><input id="input_address" type="text" name="address" value="${userWork_address}" maxlength="200" /></li>
		<li><span>简&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;历：</span><textarea id="input_workContent" name="workContent" rows="1" cols="10" style="width: 137px;height: 20px;">${userWork_workContent}</textarea></li>
		<li></li>
	</ul>
</form>
<!--工作信息-->
<!--社交信息-->
<div class="basic_info_title">
	<a id="btn_social" class="revise_info" href="javascript:void(0);" onclick="showSocialForm();">修改</a>
	<a id="btn_social_update" class="revise_info" href="javascript:void(0);" onclick="userSocialSubmit();" style="margin-right: 10px;display: none;">保存</a>
	社交信息
</div>
<ul id="userSocialInfo" class="basic_info_text clearfix">
	<li><span>QQ：</span>${userSocial_qq}</li>
	<li><span>微&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;信：</span>${userSocial_weixin}</li>
	<li><span>微&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;博：</span>${userSocial_weibo}</li>
	<li><span>博&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;客：</span>${userSocial_blog}</li>
	<li><span>其&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;他：</span>${userSocial_others}</li>
	<li></li>
</ul>
<form id="userSocialForm" style="display: none;" action="${pageContext.request.contextPath}/dev/user/user_info" method="post">
	<input type="hidden" id="input_appUid" name="appUid" value="${appUid}" />
	<input type="hidden" name="dataType" value="userSocial" />
	<ul class="basic_info_text clearfix">
		<li><span>QQ：</span><input id="input_qq" type="text" name="qq" value="${userSocial_qq}" maxlength="25" /></li>
		<li><span>微&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;信：</span><input id="input_weixin" type="text" name="weixin" value="${userSocial_weixin}" maxlength="25" /></li>
		<li><span>微&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;博：</span><input id="input_weibo" type="text" name="weibo" value="${userSocial_weibo}" maxlength="25" /></li>
		<li><span>博&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;客：</span><input id="input_blog" type="text" name="blog" value="${userSocial_blog}" maxlength="25" /></li>
		<li><span>其&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;他：</span><input id="input_others" type="text" name="others" value="${userSocial_others}" maxlength="25" /></li>
		<li></li>
	</ul>
</form>
<!--社交信息-->