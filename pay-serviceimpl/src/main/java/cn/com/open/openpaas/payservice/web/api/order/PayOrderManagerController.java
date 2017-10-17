package cn.com.open.openpaas.payservice.web.api.order;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.com.open.openpaas.payservice.app.order.model.PayOrderDetail;
import cn.com.open.openpaas.payservice.app.order.service.PayOrderDetailService;
import cn.com.open.openpaas.payservice.app.common.BaseControllerUtil;
import cn.com.open.openpaas.payservice.app.tools.DateTools;

@Controller
@RequestMapping("/pay/orderDetail/")
public class PayOrderManagerController extends  BaseControllerUtil {
	 @Autowired
	 private PayOrderDetailService payOrderDetailService;
	/**
	 * 订单详细信息存储
	 * @param request
	 * @param response
	 * @param model
	 * @throws UnsupportedEncodingException 
	 */
	 @RequestMapping(value = "save")
	public void orderDetailSave(HttpServletRequest request,HttpServletResponse response,Model model) throws UnsupportedEncodingException{
		String regNumber=request.getParameter("regNumber");
		String learningCenterCode=request.getParameter("learningCenterCode");
		String learningCenterName="";
		String learningStatus="";
		String studentName="";
		String province="";
		String costAttribution="";
		String exacct="";
		String payChannel="";
		String projectName="";
		String level="";
		String managerCenterName="";
		if (!nullEmptyBlankJudge(request.getParameter("learningCenterName"))){
			learningCenterName=new String(request.getParameter("learningCenterName").getBytes("iso-8859-1"),"utf-8");
    	}
		if (!nullEmptyBlankJudge(request.getParameter("province"))){
			province=new String(request.getParameter("province").getBytes("iso-8859-1"),"utf-8");
    	}
		if (!nullEmptyBlankJudge(request.getParameter("learningStatus"))){
			learningStatus=new String(request.getParameter("learningStatus").getBytes("iso-8859-1"),"utf-8");
    	}
		if (!nullEmptyBlankJudge(request.getParameter("studentName"))){
			studentName=new String(request.getParameter("studentName").getBytes("iso-8859-1"),"utf-8");
    	}
		if (!nullEmptyBlankJudge(request.getParameter("costAttribution"))){
			costAttribution=new String(request.getParameter("costAttribution").getBytes("iso-8859-1"),"utf-8");
    	}
		if (!nullEmptyBlankJudge(request.getParameter("exacct"))){
			exacct=new String(request.getParameter("exacct").getBytes("iso-8859-1"),"utf-8");
    	}
		if (!nullEmptyBlankJudge(request.getParameter("payChannel"))){
			payChannel=new String(request.getParameter("payChannel").getBytes("iso-8859-1"),"utf-8");
    	}
		if (!nullEmptyBlankJudge(request.getParameter("projectName"))){
			projectName=new String(request.getParameter("projectName").getBytes("iso-8859-1"),"utf-8");
    	}
		if (!nullEmptyBlankJudge(request.getParameter("level"))){
			level=new String(request.getParameter("level").getBytes("iso-8859-1"),"utf-8");
    	}
		if (!nullEmptyBlankJudge(request.getParameter("managerCenterName"))){
			managerCenterName=new String(request.getParameter("managerCenterName").getBytes("iso-8859-1"),"utf-8");
    	}
		String learningBatches=request.getParameter("learningBatches");
		String specialty=request.getParameter("specialty");
		
		
		String cardNo=request.getParameter("cardNo");
		String studentId=request.getParameter("studentId");
		
		String payAmount=request.getParameter("payAmount");
		String payCharge=request.getParameter("payCharge");
		String payDate=request.getParameter("payDate");
		String projectNumber=request.getParameter("projectNumber");
		
		String merchantOrderId=request.getParameter("merchantOrderId");
		String appId=request.getParameter("appId");
		Map<String ,Object> map=new HashMap<String,Object>();
			PayOrderDetail payOrderDetail=new PayOrderDetail();
			payOrderDetail.setAppId(appId);
			payOrderDetail.setRegNumber(regNumber);
			payOrderDetail.setCardNo(cardNo);
			payOrderDetail.setProvince(province);
			payOrderDetail.setCostAttribution(costAttribution);
			payOrderDetail.setExacct(exacct);
			payOrderDetail.setLearningBatches(learningBatches);
			payOrderDetail.setLearningCenterCode(learningCenterCode);
			payOrderDetail.setLearningCenterName(learningCenterName);
			payOrderDetail.setLearningStatus(learningStatus);
			payOrderDetail.setLevel(level);
			payOrderDetail.setMerchantOrderId(merchantOrderId);
			payOrderDetail.setPayAmount(payAmount);
			payOrderDetail.setPayChannel(payChannel);
			payOrderDetail.setPayCharge(payCharge);
			if(!nullEmptyBlankJudge(payDate)){
				payOrderDetail.setPayDate(DateTools.stringtoDate(payDate, DateTools.FORMAT_ONE));
			}
			payOrderDetail.setProjectName(projectName);
			payOrderDetail.setProjectNumber(projectNumber);
			payOrderDetail.setSpecialty(specialty);
			payOrderDetail.setStudentId(studentId);
			payOrderDetail.setStudentName(studentName);
			payOrderDetail.setManagerCenterName(managerCenterName);
			Boolean f=payOrderDetailService.savePayOrderDetail(payOrderDetail);
			if(f){
				map.put("status", "1");
			}else{
				map.put("status", "0");
			}
			writeErrorJson(response, map);
		
	}
	
	

}
