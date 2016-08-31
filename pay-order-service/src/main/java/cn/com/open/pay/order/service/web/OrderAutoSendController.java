package cn.com.open.pay.order.service.web;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import net.sf.json.JSONObject;

import org.apache.commons.httpclient.util.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.com.open.pay.order.service.dev.PayserviceDev;
import cn.com.open.pay.order.service.log.PayLogName;
import cn.com.open.pay.order.service.log.PayServiceLog;
import cn.com.open.pay.order.service.log.UnifyPayControllerLog;
import cn.com.open.pay.order.service.merchant.model.MerchantInfo;
import cn.com.open.pay.order.service.merchant.service.MerchantInfoService;
import cn.com.open.pay.order.service.order.model.MerchantOrderInfo;
import cn.com.open.pay.order.service.order.service.MerchantOrderInfoService;
import cn.com.open.pay.order.service.tools.AlipayCore;
import cn.com.open.pay.order.service.tools.BaseControllerUtil;
import cn.com.open.pay.order.service.tools.DateTools;
import cn.com.open.pay.order.service.tools.PayUtil;
import cn.com.open.pay.order.service.tools.SysUtil;

/**
 * 
 */
@Controller
@RequestMapping("/alipay/")
public class OrderAutoSendController extends BaseControllerUtil{
	private static final Logger log = LoggerFactory.getLogger(OrderAutoSendController.class);
	
	 @Autowired
	 private MerchantOrderInfoService merchantOrderInfoService;
	 @Autowired
	 private MerchantInfoService merchantInfoService;
	 @Autowired
	 private PayserviceDev payserviceDev;
	 
	 /**
     * 订单自动补发接口
     * @return Json
     */
    public void orderAutoSend() {
    	log.info("~~~~~~~~~~~~~~~~~~~~~~orderAutoSend start~~~~~~~~~~~~~~~~~~~~~~~~");
    	String result = "";
    	 long startTime = System.currentTimeMillis();
    	//获取payStatus第三方支付状态为成功1且notifyStatus商户接收状态为未处理状态0 订单集合
	    List<MerchantOrderInfo> merchantOrderInfos=merchantOrderInfoService.findByPayAndNotifyStatus();
	    PayServiceLog payServiceLog=new PayServiceLog();
		 payServiceLog.setCreatTime(DateTools.dateToString(new Date(), "yyyy-MM-dd HH:mm:ss"));
		 payServiceLog.setLogType(payserviceDev.getLog_type());
		 payServiceLog.setLogName(PayLogName.ORDER_AUTO_START);
        payServiceLog.setStatus("ok");
        UnifyPayControllerLog.log(startTime,payServiceLog,payserviceDev);
	    for(MerchantOrderInfo orderInfo : merchantOrderInfos){
    		
	    	if(orderInfo.getNotifyTimes()==null || orderInfo.getNotifyTimes()<SysUtil.toInt(payserviceDev.getNotify_times())){
	    		MerchantInfo merchantInfo = merchantInfoService.findById(orderInfo.getMerchantId());
	    		//DictTradeChannel channel=dictTradeChannelService.findByMAI(orderInfo.getMerchantId()+"", orderInfo.getChannelId());
	    		SortedMap<Object,Object> params = new TreeMap<Object,Object>();
	    		params.put("orderId", orderInfo.getId());
				params.put("outTradeNo", orderInfo.getMerchantOrderId());
				params.put("merchantId", String.valueOf(orderInfo.getMerchantId()));
				params.put("paymentType", String.valueOf(orderInfo.getPaymentId()));
				params.put("paymentChannel", String.valueOf(orderInfo.getChannelId()));
				params.put("feeType", "CNY");
				params.put("guid", orderInfo.getGuid());
				params.put("appUid",String.valueOf(orderInfo.getSourceUid()));
				//sParaTemp.put("exter_invoke_ip",exter_invoke_ip);
				params.put("timeEnd", DateUtil.formatDate(new Date(), "yyyyMMddHHmmss"));
				params.put("totalFee", String.valueOf((int)(orderInfo.getPayAmount()*100)));
				params.put("goodsId", orderInfo.getMerchantProductId());
				params.put("goodsName",orderInfo.getMerchantProductName());
				params.put("goodsDesc", orderInfo.getMerchantProductDesc());
				params.put("parameter", orderInfo.getParameter1());
				params.put("userName", orderInfo.getSourceUserName());
			
	    		log.info("~~~~~~~~~orderAutoSend params："+AlipayCore.createLogString(params));
	    		
	    		 payServiceLog.setAmount(String.valueOf(orderInfo.getAmount()*100));
	    		 payServiceLog.setAppId(orderInfo.getAppId());
	    		 payServiceLog.setChannelId(String.valueOf(orderInfo.getChannelId()));
	    		 payServiceLog.setMerchantId(String.valueOf(orderInfo.getMerchantId()));
	    		 payServiceLog.setMerchantOrderId(String.valueOf(orderInfo.getMerchantOrderId()));
	    		 payServiceLog.setOrderId(orderInfo.getId());
	    		 payServiceLog.setPaymentId(String.valueOf(orderInfo.getPaymentId()));
	    		 payServiceLog.setPayOrderId(String.valueOf(orderInfo.getPayOrderId()));
	    		 payServiceLog.setProductDesc(orderInfo.getMerchantProductDesc());
	    		 payServiceLog.setProductName(orderInfo.getMerchantProductName());
	    		 payServiceLog.setRealAmount(String.valueOf(orderInfo.getPayAmount()*100));
	    		 payServiceLog.setSourceUid(orderInfo.getSourceUid());
	    		 payServiceLog.setUsername(orderInfo.getUserName());
	    		
	    		String secret=PayUtil.createSign(payserviceDev.getAli_input_charset(),params,merchantInfo.getPayKey());
	    		params.put("secret", secret);
	    		
	    		log.info("~~~~~~~~~orderAutoSend secret："+secret);
	    		
	    		//向业务方发送订单状态信息
	    		if(orderInfo.getNotifyUrl()!=null && !("").equals(orderInfo.getNotifyUrl())){
	    			result = sendOrderPost(orderInfo.getNotifyUrl(),params);
	    		}else{
	    			if(merchantInfo!=null){
	    				result = sendOrderPost(merchantInfo.getNotifyUrl(),params);
	    			}
	    		}
	    		if(result != null && !("").equals(result)){
	    			log.info("~~~~~~~~~~~~~~orderAutoSend result："+result+"~~~~~~~~~~~~~~~~~~~~");
	    			Map map=(Map) JSONObject.toBean(JSONObject.fromObject(result),Map.class);
	    			if("ok".equals(map.get("state"))){//商户处理成功
	    				orderInfo.setNotifyStatus(1);	
	    				payServiceLog.setLogName(PayLogName.ORDER_AUTO_END);
	       		        UnifyPayControllerLog.log(startTime,payServiceLog,payserviceDev);    
	    			}else{
	    			     orderInfo.setNotifyStatus(2);
	    			   //日志添加
	    				payServiceLog.setStatus("error");
	    				 payServiceLog.setErrorCode(map.get("errorCode").toString());
	    				payServiceLog.setLogName(PayLogName.ORDER_AUTO_END);
	       		        UnifyPayControllerLog.log(startTime,payServiceLog,payserviceDev);    
	    			}
    				orderInfo.setNotifyTimes();//方法中自动+1
    				orderInfo.setNotifyDate(new Date());
    				merchantOrderInfoService.updateNotifyStatus(orderInfo);//更新订单状态
	    		}
	    		else{
	    			orderInfo.setNotifyStatus(2);
	    			orderInfo.setNotifyTimes();//方法中自动+1
	    			merchantOrderInfoService.updateNotifyStatus(orderInfo);//更新订单状态
	    			//日志添加
	    			  payServiceLog.setStatus("error");
	    			  payServiceLog.setLogName(PayLogName.ORDER_AUTO_END);
	    		      UnifyPayControllerLog.log(startTime,payServiceLog,payserviceDev); 
	    		}
	    	}
	    }
	    log.info("~~~~~~~~~~~~~~~orderAutoSend end~~~~~~~~~~~~~~~~");
    }
	
}