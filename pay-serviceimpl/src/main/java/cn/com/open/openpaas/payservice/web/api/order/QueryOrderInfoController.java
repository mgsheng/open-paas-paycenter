package cn.com.open.openpaas.payservice.web.api.order;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
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

import cn.com.open.openpaas.payservice.app.balance.model.UserAccountBalance;
import cn.com.open.openpaas.payservice.app.balance.service.UserAccountBalanceService;
import cn.com.open.openpaas.payservice.app.log.UnifyCostsControllerLog;
import cn.com.open.openpaas.payservice.app.log.UnifyPayControllerLog;
import cn.com.open.openpaas.payservice.app.log.model.PayLogName;
import cn.com.open.openpaas.payservice.app.log.model.PayServiceLog;
import cn.com.open.openpaas.payservice.app.merchant.model.MerchantInfo;
import cn.com.open.openpaas.payservice.app.merchant.service.MerchantInfoService;
import cn.com.open.openpaas.payservice.app.order.model.MerchantOrderInfo;
import cn.com.open.openpaas.payservice.app.order.service.MerchantOrderInfoService;
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
@RequestMapping("/unify/order")
public class QueryOrderInfoController extends BaseControllerUtil{
	private static final Logger log = LoggerFactory.getLogger(QueryOrderInfoController.class);
	
	 @Autowired
	 private MerchantInfoService merchantInfoService;
	 @Autowired
	 private MerchantOrderInfoService merchantOrderInfoService;
	
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
  	    String signatureNonce=request.getParameter("signatureNonce");
  	    log.info("=============query order info start=========");
    	Map<String ,Object> map=new HashMap<String,Object>();
    	
        if(!paraMandatoryCheck(Arrays.asList(start_time,appId,end_time))){
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
   		sParaTemp.put("timestamp", timestamp);
   		sParaTemp.put("signatureNonce", signatureNonce);
   		String params=createSign(sParaTemp);
   	    Boolean hmacSHA1Verification=OauthSignatureValidateHandler.validateSignature(signature,params,merchantInfo.getPayKey());
		if(!hmacSHA1Verification){
			map=paraMandaChkAndReturnMap(3, response,"认证失败");
			writeErrorJson(response,map);
        	return ;
		} 
		//Date startTime=DateTools.stringtoDate(start_time, "yyyy-MM-dd HH:mm:ss");
		//Date endTime=DateTools.stringtoDate(start_time, "yyyy-MM-dd HH:mm:ss");
		List<MerchantOrderInfo> merchantOrderInfoList=merchantOrderInfoService.findOrderByTime(start_time, end_time, appId);
		HashMap<String, Object> totalAmountMap=merchantOrderInfoService.getTotalAmountByTime(start_time, end_time, appId);
		BigDecimal  totalAmount=(BigDecimal) totalAmountMap.get("totalAmount");
		
		map.clear();
		map.put("order_count", merchantOrderInfoList.size());
		map.put("total_amount", totalAmount.stripTrailingZeros().toString());
		map.put("status", "ok");
		map.put("merchantOrderInfoList", merchantOrderInfoList);
		writeErrorJson(response, map);
        }
      
    
	

}