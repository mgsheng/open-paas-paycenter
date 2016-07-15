package cn.com.open.openpaas.payservice.web.api.order;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
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

import cn.com.open.openpaas.payservice.app.balance.model.UserAccountBalance;
import cn.com.open.openpaas.payservice.app.balance.service.UserAccountBalanceService;
import cn.com.open.openpaas.payservice.app.log.UnifyCostsControllerLog;
import cn.com.open.openpaas.payservice.app.merchant.model.MerchantInfo;
import cn.com.open.openpaas.payservice.app.merchant.service.MerchantInfoService;
import cn.com.open.openpaas.payservice.app.record.model.UserSerialRecord;
import cn.com.open.openpaas.payservice.app.record.service.UserSerialRecordService;
import cn.com.open.openpaas.payservice.app.tools.AmountUtil;
import cn.com.open.openpaas.payservice.app.tools.BaseControllerUtil;
import cn.com.open.openpaas.payservice.app.tools.StringTool;
import cn.com.open.openpaas.payservice.web.api.oauth.OauthSignatureValidateHandler;

/**
 * 
 */
@Controller
@RequestMapping("/unify/")
public class UnifyCostsController extends BaseControllerUtil{
	private static final Logger log = LoggerFactory.getLogger(UnifyCostsController.class);
	
	 @Autowired
	 private UserSerialRecordService userSerialRecordService;
	 @Autowired
	 private MerchantInfoService merchantInfoService;
	 @Autowired
	 private UserAccountBalanceService userAccountBalanceService;
	 
	

	 /**
     * 请求统一扣费
     * @throws Exception 
     */
    @RequestMapping("costs")
    public void unifyPay(HttpServletRequest request,HttpServletResponse response,Model model) throws Exception {
    	long startTime=System.currentTimeMillis();
    	String serialNo=request.getParameter("serial_no");
    	String appId=request.getParameter("app_id");
    	String amount=request.getParameter("amount");
    	String sourceId=request.getParameter("source_id");
    	String userName=request.getParameter("user_name");
    	String signature=request.getParameter("signature");
  	    String timestamp=request.getParameter("timestamp");
  	    String signatureNonce=request.getParameter("signatureNonce");
  	    String merchantId = request.getParameter("merchantId");
  	    log.info("=============扣费开始=========");
  	    log.info("流水号："+serialNo);
    	Map<String ,Object> map=new HashMap<String,Object>();
    	
        if(!paraMandatoryCheck(Arrays.asList(serialNo,appId,amount,sourceId))){
        	map=paraMandaChkAndReturnMap(3, response,"必传参数中有空值");
        	UnifyCostsControllerLog.log(startTime,serialNo, sourceId,merchantId, AmountUtil.changeF2Y(amount), map);
        	writeErrorJson(response,map);
        	return ;
        }
        MerchantInfo merchantInfo=merchantInfoService.findById(Integer.parseInt(merchantId));
    	if(merchantInfo==null){
        	map=paraMandaChkAndReturnMap(3, response,"商户ID不存在");
        	UnifyCostsControllerLog.log(startTime,serialNo, sourceId, merchantId,AmountUtil.changeF2Y(amount), map);
        	writeErrorJson(response,map);
        	return ;
        }
    	SortedMap<Object,Object> sParaTemp = new TreeMap<Object,Object>();
    	sParaTemp.put("app_id",appId);
   		sParaTemp.put("serial_no",serialNo);
   		sParaTemp.put("source_id", sourceId);
   		sParaTemp.put("merchantId",merchantId);
   		sParaTemp.put("amount",amount);
   		sParaTemp.put("user_name", userName);
   		sParaTemp.put("timestamp", timestamp);
   		sParaTemp.put("signatureNonce", signatureNonce);
   		String params=createSign(sParaTemp);
   	    Boolean hmacSHA1Verification=OauthSignatureValidateHandler.validateSignature(signature,params,merchantInfo.getPayKey());
		if(!hmacSHA1Verification){
			map=paraMandaChkAndReturnMap(1, response,"认证失败");
			UnifyCostsControllerLog.log(startTime,serialNo, sourceId, merchantId,AmountUtil.changeF2Y(amount), map);
			writeErrorJson(response,map);
        	return ;
		} 
    	
        if(!StringTool.isNumeric(amount)){
        	map=paraMandaChkAndReturnMap(3, response,"订单金额格式有误");
        	UnifyCostsControllerLog.log(startTime,serialNo, sourceId, merchantId,AmountUtil.changeF2Y(amount), map);
        	writeErrorJson(response,map);
        	return ;
        }
        UserAccountBalance  userAccountBalance=userAccountBalanceService.getBalanceInfo(sourceId, Integer.parseInt(appId));
        if(userAccountBalance==null){
        	map=paraMandaChkAndReturnMap(2, response,"账户不存在");
			UnifyCostsControllerLog.log(startTime,serialNo, sourceId, merchantId,AmountUtil.changeF2Y(amount), map);
			writeErrorJson(response,map);
        	return ;	
        }else{
        	if(userAccountBalance.getBalance()!=null&&userAccountBalance.getBalance()>Double.parseDouble(amount)){
        	    Double newAmount=userAccountBalance.getBalance()-Double.parseDouble(amount);
        	    userAccountBalance.setBalance(newAmount);
        	    userAccountBalanceService.updateBalanceInfo(userAccountBalance);
        	    UserSerialRecord userSerialRecord=new UserSerialRecord();
        	    userSerialRecord.setAmount(Double.parseDouble(amount));
        	    userSerialRecord.setAppId(Integer.parseInt(appId));
        	    userSerialRecord.setSerialNo(serialNo);
        	    userSerialRecord.setSourceId(sourceId);
        	    userSerialRecord.setPayType(2);
        	    userSerialRecord.setCreateTime(new Date());
        	    userSerialRecord.setUserName(userName);
        	    userSerialRecordService.saveUserSerialRecord(userSerialRecord);
        		map.clear();
        		map.put("status","1");
        		map.put("balance",newAmount);
        		writeSuccessJson(response,map);
        	}else{
        		map=paraMandaChkAndReturnMap(4, response,"账户余额不足");
            	UnifyCostsControllerLog.log(startTime,serialNo, sourceId, merchantId,AmountUtil.changeF2Y(amount), map);
            	writeErrorJson(response,map);
            	return ;
        	}
        	
        }
        
    }	
    
	

}