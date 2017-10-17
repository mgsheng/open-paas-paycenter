package cn.com.open.openpaas.payservice.app.channel.wxpay;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.dom4j.DocumentException;
import org.jdom.JDOMException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.com.open.openpaas.payservice.app.channel.alipay.Channel;
import cn.com.open.openpaas.payservice.app.channel.model.DictTradeChannel;
import cn.com.open.openpaas.payservice.app.channel.service.DictTradeChannelService;
import cn.com.open.openpaas.payservice.app.log.UnifyPayControllerLog;
import cn.com.open.openpaas.payservice.app.log.model.PayLogName;
import cn.com.open.openpaas.payservice.app.log.model.PayServiceLog;
import cn.com.open.openpaas.payservice.app.order.model.MerchantOrderInfo;
import cn.com.open.openpaas.payservice.app.order.service.MerchantOrderInfoService;
import cn.com.open.openpaas.payservice.app.common.BaseControllerUtil;
import cn.com.open.openpaas.payservice.app.tools.DateTools;
import cn.com.open.openpaas.payservice.dev.PayserviceDev;


/**
 * 
 */
@Controller
@RequestMapping("/wxpay/order/")
public class WxOrderCallbackController extends BaseControllerUtil {
	private static final Logger log = LoggerFactory.getLogger(WxOrderCallbackController.class);
	 @Autowired
	 private MerchantOrderInfoService merchantOrderInfoService;
	 @Autowired
	 private PayserviceDev payserviceDev;
	 @Autowired
	 private DictTradeChannelService dictTradeChannelService;
	/**
	 * 微信订单回调接口
	 * @param request
	 * @param response
	 * @throws IOException 
	 * @throws DocumentException 
	 * @throws MalformedURLException 
	 */
	@RequestMapping("callBack")
	public String dirctPay(HttpServletRequest request,HttpServletResponse response,Model model) throws MalformedURLException, DocumentException, IOException {
		//读取参数
		        log.info("------------------------------微信回调处理开始------------------------------");
		        long startTime = System.currentTimeMillis();
				InputStream inputStream ;
				StringBuffer sb = new StringBuffer();
				inputStream = request.getInputStream();
			    String resXml = "";
				String s ;
				
				BufferedReader in = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
				while ((s = in.readLine()) != null){
					sb.append(s);
				}
				in.close();
				inputStream.close();
				log.info("wxbackvalue:"+sb.toString());
				String xml=sb.toString();
				//String xml="<xml><appid><![CDATA[wx2d95304f95ff1eb1]]></appid><bank_type><![CDATA[CFT]]></bank_type><cash_fee><![CDATA[1]]></cash_fee><fee_type><![CDATA[CNY]]></fee_type><is_subscribe><![CDATA[N]]></is_subscribe><mch_id><![CDATA[1231995302]]></mch_id><nonce_str><![CDATA[1415251961]]></nonce_str><openid><![CDATA[oSUmyjqZ4Q5OJ_wLZ2bO0mjiJdfs]]></openid><out_trade_no><![CDATA[test201605201417_23]]></out_trade_no><result_code><![CDATA[SUCCESS]]></result_code><return_code><![CDATA[SUCCESS]]></return_code><sign><![CDATA[16B3B8F6DE2D0497E3BE6810822FC37D]]></sign><time_end><![CDATA[20160520141651]]></time_end><total_fee>1</total_fee><trade_type><![CDATA[NATIVE]]></trade_type><transaction_id><![CDATA[4008292001201605206070774215]]></transaction_id></xml>";
				//解析xml成map
				Map<String, String> m = new HashMap<String, String>();
				if(nullEmptyBlankJudge(sb.toString())){
					 resXml = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>"
			                    + "<return_msg><![CDATA[报文为空]]></return_msg>" + "</xml> ";
			    	log.info("通知签名验证失败");
				}else{
					//(需要修改)
					log.info("-----------------解析开始----------------------");
					try {
						m = WxXMLUtil.doXMLParse(xml);
					} catch (JDOMException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					//过滤空 设置 TreeMap
					SortedMap<Object,Object> packageParams = new TreeMap<Object,Object>();		
					Iterator it = m.keySet().iterator();
					while (it.hasNext()) {
						String parameter = (String) it.next();
						String parameterValue = m.get(parameter);
						
						String v = "";
						if(null != parameterValue) {
							v = parameterValue.trim();
						}
						packageParams.put(parameter, v);
					}
		        	String out_trade_no = (String)packageParams.get("out_trade_no");
		        	String transaction_id=(String)packageParams.get("transaction_id");
		        	
		        	String total_fee = (String)packageParams.get("total_fee");
		        	Double total_fees=Double.parseDouble(total_fee);
					// 账号信息(需要修改)
		        	MerchantOrderInfo merchantOrderInfo=merchantOrderInfoService.findById(out_trade_no);
		        	 DictTradeChannel dictTradeChannel=dictTradeChannelService.findByMAI(String.valueOf(merchantOrderInfo.getMerchantId()),Channel.WEIXIN.getValue());
				     String key = dictTradeChannel.getKeyValue(); // key
			        //log.info(packageParams);
			    
			    	 String backMsg="";
			    	if(merchantOrderInfo!=null){
			    		//添加日志
						 PayServiceLog payServiceLog=new PayServiceLog();
						 payServiceLog.setAmount(total_fee);
						 payServiceLog.setAppId(merchantOrderInfo.getAppId());
						 payServiceLog.setChannelId(String.valueOf(merchantOrderInfo.getChannelId()));
						 payServiceLog.setCreatTime(DateTools.dateToString(new Date(), "yyyyMMddHHmmss"));
						 payServiceLog.setLogType(payserviceDev.getLog_type());
						 payServiceLog.setMerchantId(String.valueOf(merchantOrderInfo.getMerchantId()));
						 payServiceLog.setMerchantOrderId(merchantOrderInfo.getMerchantOrderId());
						 payServiceLog.setOrderId(out_trade_no);
						 payServiceLog.setPaymentId(String.valueOf(merchantOrderInfo.getPaymentId()));
						 payServiceLog.setPayOrderId(transaction_id);
						 payServiceLog.setRealAmount(total_fee);
						 payServiceLog.setSourceUid(merchantOrderInfo.getSourceUid());
						 payServiceLog.setUsername(merchantOrderInfo.getUserName());
						 payServiceLog.setLogName(PayLogName.CALLBACK_START);
				         payServiceLog.setStatus("ok");
				         UnifyPayControllerLog.log(startTime,payServiceLog,payserviceDev);
				        
					    //判断签名是否正确
					    if(WxPayCommonUtil.isTenpaySign("UTF-8", packageParams,key)) {
					    	String rechargeMsg="";
					        //处理业务开始
					    	log.info("----------------------------处理业务开始------------------");
					        if("SUCCESS".equals((String)packageParams.get("result_code"))){
					       	 return "pay/callBack";
					        } else {
					        	payServiceLog.setErrorCode("4");
						        payServiceLog.setStatus("error");
						        payServiceLog.setLogName(PayLogName.CALLBACK_END);
						        UnifyPayControllerLog.log(startTime,payServiceLog,payserviceDev);
					        	log.info("支付失败,错误信息：" + packageParams.get("err_code"));
					            resXml = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>"
					                    + "<return_msg><![CDATA[报文为空]]></return_msg>" + "</xml> ";
					            backMsg="error";
					        }
					    } else{
					    	payServiceLog.setErrorCode("3");
					        payServiceLog.setStatus("error");
					        payServiceLog.setLogName(PayLogName.CALLBACK_END);
					        UnifyPayControllerLog.log(startTime,payServiceLog,payserviceDev);
					    	 resXml = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>"
					                    + "<return_msg><![CDATA[报文为空]]></return_msg>" + "</xml> ";
					    	log.info("通知签名验证失败");
					    	  backMsg="error";
					    }
			    	}else{
			    		log.info("支付失败,错误信息：" + packageParams.get("err_code"));
			            resXml = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>"
			                    + "<return_msg><![CDATA[订单查询失败]]></return_msg>" + "</xml> ";
			            backMsg="error";
			    	}
			    	 model.addAttribute("backMsg", backMsg);
			    	 model.addAttribute("productName", merchantOrderInfo.getMerchantProductName());
				   }
				    //------------------------------
			        //处理业务完毕
			        //------------------------------
				 return "pay/errorPayChannel";
				}
	
}