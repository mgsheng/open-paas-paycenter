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
import cn.com.open.openpaas.payservice.app.log.UnifyPayControllerLog;
import cn.com.open.openpaas.payservice.app.log.model.PayLogName;
import cn.com.open.openpaas.payservice.app.log.model.PayServiceLog;
import cn.com.open.openpaas.payservice.app.merchant.model.MerchantInfo;
import cn.com.open.openpaas.payservice.app.merchant.service.MerchantInfoService;
import cn.com.open.openpaas.payservice.app.record.model.UserSerialRecord;
import cn.com.open.openpaas.payservice.app.record.service.UserSerialRecordService;
import cn.com.open.openpaas.payservice.app.tools.AmountUtil;
import cn.com.open.openpaas.payservice.app.tools.BaseControllerUtil;
import cn.com.open.openpaas.payservice.app.tools.DateTools;
import cn.com.open.openpaas.payservice.app.tools.StringTool;
import cn.com.open.openpaas.payservice.app.zookeeper.DistributedLock;
import cn.com.open.openpaas.payservice.dev.PayserviceDev;
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
	 @Autowired
	 private PayserviceDev payserviceDev;
	 
	

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
  	    log.info("=============costs-start=========");
  	   //日志添加
    	PayServiceLog payServiceLog=new PayServiceLog();
  	    payServiceLog.setAmount(amount);
  	    payServiceLog.setAppId(appId);
  	    payServiceLog.setCreatTime(DateTools.dateToString(new Date(), "yyyy-MM-dd HH:mm:ss"));
  	    payServiceLog.setLogType(payserviceDev.getLog_type());
  	    payServiceLog.setMerchantId(merchantId);
  	    payServiceLog.setMerchantOrderId(serialNo);
  	    payServiceLog.setRealAmount(amount);
  	    payServiceLog.setSourceUid(sourceId);
  	    payServiceLog.setUsername(userName);
  	    payServiceLog.setStatus("ok");
  	    payServiceLog.setLogName(PayLogName.COSTS_START);
    	Map<String ,Object> map=new HashMap<String,Object>();
    	
        if(!paraMandatoryCheck(Arrays.asList(serialNo,appId,amount,sourceId))){
        	map=paraMandaChkAndReturnMap(1, response,"必传参数中有空值");
        	payServiceLog.setErrorCode("1");
        	payServiceLog.setStatus("error");
        	payServiceLog.setLogName(PayLogName.COSTS_END);
        	UnifyPayControllerLog.log(startTime,payServiceLog,payserviceDev);	
        	writeErrorJson(response,map);
        	return ;
        }
        MerchantInfo merchantInfo=merchantInfoService.findById(Integer.parseInt(merchantId));
    	if(merchantInfo==null){
    		payServiceLog.setErrorCode("2");
        	payServiceLog.setStatus("error");
        	payServiceLog.setLogName(PayLogName.COSTS_END);
        	map=paraMandaChkAndReturnMap(2, response,"商户ID不存在");
        	 UnifyPayControllerLog.log(startTime,payServiceLog,payserviceDev);
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
			map=paraMandaChkAndReturnMap(3, response,"认证失败");
			payServiceLog.setErrorCode("3");
        	payServiceLog.setStatus("error");
        	payServiceLog.setLogName(PayLogName.COSTS_END);
        	 UnifyPayControllerLog.log(startTime,payServiceLog,payserviceDev);
			writeErrorJson(response,map);
        	return ;
		} 
    	
        if(!StringTool.isNumeric(amount)){
        	payServiceLog.setErrorCode("4");
        	payServiceLog.setStatus("error");
        	map=paraMandaChkAndReturnMap(4, response,"订单金额格式有误");
        	payServiceLog.setLogName(PayLogName.COSTS_END);
        	 UnifyPayControllerLog.log(startTime,payServiceLog,payserviceDev);
        	writeErrorJson(response,map);
        	return ;
        }
        UserAccountBalance  userAccountBalance=userAccountBalanceService.getBalanceInfo(sourceId, Integer.parseInt(appId));
        if(userAccountBalance==null){
        	payServiceLog.setErrorCode("5");
        	payServiceLog.setStatus("error");
        	map=paraMandaChkAndReturnMap(5, response,"账户不存在");
        	payServiceLog.setLogName(PayLogName.COSTS_END);
        	 UnifyPayControllerLog.log(startTime,payServiceLog,payserviceDev);
			writeErrorJson(response,map);
        	return ;	
        }else{
        	if(userAccountBalance.getBalance()!=null&&userAccountBalance.getBalance()>Double.parseDouble(amount)){
        	    userAccountBalance.setBalance(Double.parseDouble(amount));
        	    
        	     DistributedLock lock = null;
                 try {
           		  lock = new DistributedLock(payserviceDev.getZookeeper_config(),userAccountBalance.getSourceId()+userAccountBalance.getAppId());
           		  lock.lock();
           		  userAccountBalanceService.updateBalanceInfo(userAccountBalance);
       			
       		     } catch (Exception e) {
       			// TODO Auto-generated catch block
	       			e.printStackTrace();
	       		  }finally{
	       			  lock.unlock(); 
	       		  }
        	    
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
        		map.put("status","ok");
        		map.put("balance",Double.parseDouble(amount));
        		payServiceLog.setErrorCode("");
            	payServiceLog.setStatus("ok");
        		writeSuccessJson(response,map);
        		payServiceLog.setLogName(PayLogName.COSTS_END);
        	    UnifyPayControllerLog.log(startTime, payServiceLog, payserviceDev);
        		return ;
        	}else{
        		payServiceLog.setErrorCode("6");
            	payServiceLog.setStatus("error");
            	payServiceLog.setLogName(PayLogName.COSTS_END);
        		map=paraMandaChkAndReturnMap(6, response,"账户余额不足");
        		UnifyPayControllerLog.log(startTime,payServiceLog,payserviceDev);
            	writeErrorJson(response,map);
            	return ;
        	}
        	
        }
      
    }	
    
	

}