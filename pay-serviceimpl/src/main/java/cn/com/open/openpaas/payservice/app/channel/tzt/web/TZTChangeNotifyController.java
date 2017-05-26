package cn.com.open.openpaas.payservice.app.channel.tzt.web;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.dom4j.DocumentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.com.open.openpaas.payservice.app.balance.service.UserAccountBalanceService;
import cn.com.open.openpaas.payservice.app.channel.UnifyPayUtil;
import cn.com.open.openpaas.payservice.app.channel.alipay.AliOrderProThread;
import cn.com.open.openpaas.payservice.app.channel.model.DictTradeChannel;
import cn.com.open.openpaas.payservice.app.channel.service.ChannelRateService;
import cn.com.open.openpaas.payservice.app.channel.service.DictTradeChannelService;
import cn.com.open.openpaas.payservice.app.channel.tzt.TZTService;
import cn.com.open.openpaas.payservice.app.log.UnifyPayControllerLog;
import cn.com.open.openpaas.payservice.app.log.model.PayLogName;
import cn.com.open.openpaas.payservice.app.log.model.PayServiceLog;
import cn.com.open.openpaas.payservice.app.merchant.service.MerchantInfoService;
import cn.com.open.openpaas.payservice.app.order.model.MerchantOrderInfo;
import cn.com.open.openpaas.payservice.app.order.service.MerchantOrderInfoService;
import cn.com.open.openpaas.payservice.app.record.service.UserSerialRecordService;
import cn.com.open.openpaas.payservice.app.tools.BaseControllerUtil;
import cn.com.open.openpaas.payservice.app.tools.DateTools;
import cn.com.open.openpaas.payservice.app.tools.WebUtils;
import cn.com.open.openpaas.payservice.dev.PayserviceDev;


/**
 * 换卡超时通知
 */
@Controller
@RequestMapping("/tzt/change/notify")
public class TZTChangeNotifyController extends BaseControllerUtil {
	private static final Logger log = LoggerFactory.getLogger(TZTChangeNotifyController.class);
	 @Autowired
	 private MerchantOrderInfoService merchantOrderInfoService;
	 @Autowired
	 private MerchantInfoService merchantInfoService;
	 @Autowired
	 private PayserviceDev payserviceDev;
	/**
	 * 投资通换卡绑卡超时通知回调接口
	 * @param request
	 * @param response
	 * @throws IOException 
	 * @throws DocumentException 
	 * @throws MalformedURLException 
	 */
	@RequestMapping("callBack")
	public void changeCardCallBack(HttpServletRequest request,HttpServletResponse response,Map<String,Object> model) throws MalformedURLException, DocumentException, IOException {
		   log.info("-----------------------callBack  tzt/notify-----------------------------------------");
		   long startTime = System.currentTimeMillis();
		    String data					= formatString(request.getParameter("data"));
			String encryptkey			= formatString(request.getParameter("encryptkey"));
			
			Map<String, String>	result	= TZTService.decryptCallbackData(data, encryptkey);
			String requestno 		    = result.get("requestno");
			String yborderid            = result.get("yborderid");
			String status               = result.get("status");
			String customError			= result.get("customError");
            String backMsg="";
    		log.info("tzt notify requestno========="+requestno);
            MerchantOrderInfo merchantOrderInfo=merchantOrderInfoService.findById(requestno);
            PayServiceLog payServiceLog=new PayServiceLog();
	   		 payServiceLog.setOrderId(requestno);
	   		 payServiceLog.setPayOrderId(yborderid);
	   		 payServiceLog.setLogName(PayLogName.CHANGE_CARD_NOTIFY_START);
	         payServiceLog.setStatus("ok");
	         UnifyPayControllerLog.log(startTime,payServiceLog,payserviceDev);
	   		if(merchantOrderInfo!=null&&merchantOrderInfo.getPayStatus()!=1){
			if(!"".equals(customError)) {
				//验证失败
				backMsg="error";
				payServiceLog.setErrorCode("4");
		        payServiceLog.setStatus("error");
		        payServiceLog.setLogName(PayLogName.CHANGE_CARD_NOTIFY_END);
		    	merchantOrderInfoService.updatePayInfo(4,String.valueOf(merchantOrderInfo.getId()),"VERIFYERROR");
				return;
			} else {
				//验证成功
				if(status.equals("CHANGE_SUCCESS")){
					//通知第三方
					int notifyStatus=merchantOrderInfo.getNotifyStatus();
					int payStatus=merchantOrderInfo.getPayStatus();
					if(payStatus!=1){
						merchantOrderInfo.setPayStatus(1);
						merchantOrderInfo.setDealDate(new Date());
						merchantOrderInfo.setPayOrderId(yborderid);
						merchantOrderInfoService.updateOrder(merchantOrderInfo);
					}
					  log.info("-----------------------callBack:tzt:notify:start-----------------------------------------");
					if(notifyStatus!=1){
						 Thread thread = new Thread(new TztOrderProThread(merchantOrderInfo, merchantOrderInfoService,merchantInfoService,payserviceDev));
						   thread.run();	
					}
					 log.info("-----------------------callBack:tzt:notify:end-----------------------------------------");
					 
			  }
				backMsg="SUCCESS";
		      }
	   		}else{
				  payServiceLog.setErrorCode("2");
		          payServiceLog.setStatus("error");
		          backMsg="error";
		          if(merchantOrderInfo!=null&&merchantOrderInfo.getPayStatus()==1)
					 {
		        	  payServiceLog.setStatus("already processed");
		        	  backMsg="SUCCESS";
					 }
		          payServiceLog.setLogName(PayLogName.CHANGE_CARD_NOTIFY_END);
					
		          UnifyPayControllerLog.log(startTime,payServiceLog,payserviceDev);
	   		}

	   		WebUtils.writeJson(response, backMsg);
	}
}