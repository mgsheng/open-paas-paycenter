package cn.com.open.openpaas.payservice.web.api.order;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.com.open.openpaas.payservice.app.channel.alipay.PayType;
import cn.com.open.openpaas.payservice.app.merchant.model.MerchantInfo;
import cn.com.open.openpaas.payservice.app.merchant.service.MerchantInfoService;
import cn.com.open.openpaas.payservice.app.record.model.UserSerialRecord;
import cn.com.open.openpaas.payservice.app.record.service.UserSerialRecordService;
import cn.com.open.openpaas.payservice.app.tools.BaseControllerUtil;
import cn.com.open.openpaas.payservice.web.api.oauth.OauthSignatureValidateHandler;

/**
 * 
 */
@Controller
@RequestMapping("/unify/serial/")
public class QuerySerialRecordController extends BaseControllerUtil{
	private static final Logger log = LoggerFactory.getLogger(QuerySerialRecordController.class);
	
	 @Autowired
	 private UserSerialRecordService userSerialRecordService;
	 @Autowired
	 private MerchantInfoService merchantInfoService;
	
	 
	

	 /**
     * 查询流水记录
     * @throws Exception 
     */
    @RequestMapping("query")
    public void query(HttpServletRequest request,HttpServletResponse response,Model model) throws Exception {
    	String start_time=request.getParameter("start_time");
    	String appId=request.getParameter("app_id");
    	String end_time=request.getParameter("end_time");
    	String signature=request.getParameter("signature");
  	    String timestamp=request.getParameter("timestamp");
  	    String merchantId=request.getParameter("merchantId");
  	    String pay_type=request.getParameter("pay_type");
  	    String signatureNonce=request.getParameter("signatureNonce");
  	    log.info("=============query serial record start=========");
    	Map<String ,Object> map=new HashMap<String,Object>();
    	
        if(!paraMandatoryCheck(Arrays.asList(start_time,appId,end_time,pay_type))){
        	map=paraMandaChkAndReturnMap(1, response,"必传参数中有空值");
        	writeErrorJson(response,map);
        	return ;
        }
        MerchantInfo merchantInfo=merchantInfoService.findById(Integer.parseInt(merchantId));
    	if(merchantInfo==null){
        	map=paraMandaChkAndReturnMap(2, response,"商户ID不存在");
        	writeErrorJson(response,map);
        	return ;
        }
    	SortedMap<Object,Object> sParaTemp = new TreeMap<Object,Object>();
    	sParaTemp.put("app_id",appId);
   		sParaTemp.put("start_time",start_time);
   		sParaTemp.put("end_time", end_time);
   		sParaTemp.put("merchantId",merchantId);
   		sParaTemp.put("pay_type", pay_type);
   		sParaTemp.put("timestamp", timestamp);
   		sParaTemp.put("signatureNonce", signatureNonce);
   		String params=createSign(sParaTemp);
   	    Boolean hmacSHA1Verification=OauthSignatureValidateHandler.validateSignature(signature,params,merchantInfo.getPayKey());
		if(!hmacSHA1Verification){
			map=paraMandaChkAndReturnMap(3, response,"认证失败");
			writeErrorJson(response,map);
        	return ;
		}
		BigDecimal  totalAmount = null;
		List<UserSerialRecord> userSerialList=null;
		String total_amount="0";
		if(!pay_type.equals(String.valueOf(PayType.ALL.type))){
			userSerialList=userSerialRecordService.getSerialByTime(start_time, end_time, appId,Integer.parseInt(pay_type));
			HashMap<String, Object> totalAmountMap=userSerialRecordService.getTotalAmountByTime(start_time, end_time, appId,Integer.parseInt(pay_type));	
			//充值 消费
			if(totalAmountMap!=null){
				totalAmount=(BigDecimal) totalAmountMap.get("totalAmount");
				total_amount=totalAmount.stripTrailingZeros().toString();
			}
			map.clear();
			map.put("serial_count",String.valueOf(userSerialList.size()));
			map.put("total_amount", total_amount);
			map.put("status", "ok");
			map.put("userSerialList", userSerialList);
		}else{
			//全部
			userSerialList=userSerialRecordService.getTotalSerialByTime(start_time, end_time, appId);
			List<Map<String, Object>> getTotalAmount=userSerialRecordService.getTotalAmount(start_time, end_time, appId);
			String recharge_total_amount="0";
			String recharge_count="";
			String costs_total_amount="0";
			String costs_count="0";
			for(int i=0;i<getTotalAmount.size();i++){
			 if(getTotalAmount.get(i).get("payType").toString().equals(String.valueOf(PayType.PAY.type))){
				 recharge_total_amount= getTotalAmount.get(i).get("totalAmount").toString();
				 recharge_count=getTotalAmount.get(i).get("count").toString();
			 }else{
				 costs_total_amount= getTotalAmount.get(i).get("totalAmount").toString();
				 costs_count=getTotalAmount.get(i).get("count").toString();
			 }
				
			}
			map.clear();
			map.put("recharge_total_amount", recharge_total_amount);
			map.put("costs_total_amount", costs_total_amount);
			map.put("recharge_count", recharge_count);
			map.put("costs_count", costs_count);
			map.put("status", "ok");
			map.put("userSerialList", userSerialList);
		}
		
		writeErrorJson(response, map);
		
        }
      
    
	

}