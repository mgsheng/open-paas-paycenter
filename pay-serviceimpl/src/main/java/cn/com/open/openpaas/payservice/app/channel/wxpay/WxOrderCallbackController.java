package cn.com.open.openpaas.payservice.app.channel.wxpay;

import java.io.BufferedOutputStream;
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
import org.springframework.web.bind.annotation.RequestMapping;

import cn.com.open.openpaas.payservice.app.balance.model.UserAccountBalance;
import cn.com.open.openpaas.payservice.app.balance.service.UserAccountBalanceService;
import cn.com.open.openpaas.payservice.app.channel.alipay.AliOrderProThread;
import cn.com.open.openpaas.payservice.app.merchant.service.MerchantInfoService;
import cn.com.open.openpaas.payservice.app.order.model.MerchantOrderInfo;
import cn.com.open.openpaas.payservice.app.order.service.MerchantOrderInfoService;
import cn.com.open.openpaas.payservice.app.tools.BaseControllerUtil;
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
	 private MerchantInfoService merchantInfoService;
	 @Autowired
	 private PayserviceDev payserviceDev;
	 @Autowired
	 private UserAccountBalanceService userAccountBalanceService;
	/**
	 * 微信订单回调接口
	 * @param request
	 * @param response
	 * @throws IOException 
	 * @throws DocumentException 
	 * @throws MalformedURLException 
	 */
	@RequestMapping("callBack")
	public void dirctPay(HttpServletRequest request,HttpServletResponse response,Map<String,Object> model) throws MalformedURLException, DocumentException, IOException {
		//读取参数
		        log.info("------------------------------微信回调处理开始------------------------------");
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
					// 账号信息(需要修改)
			        String key = payserviceDev.getWx_key(); // key
			        //log.info(packageParams);
				    //判断签名是否正确
				    if(WxPayCommonUtil.isTenpaySign("UTF-8", packageParams,key)) {
				    	String rechargeMsg="";
				        //------------------------------
				        //处理业务开始
				        //------------------------------
				    	log.info("----------------------------处理业务开始------------------");
				        if("SUCCESS".equals((String)packageParams.get("result_code"))){
				        	// 这里是支付成功
				            //////////执行自己的业务逻辑////////////////
				        	log.info("----------------支付成功执行业务逻辑--------------------------");
				        	String mch_id = (String)packageParams.get("mch_id");
				        	String openid = (String)packageParams.get("openid");
				        	String is_subscribe = (String)packageParams.get("is_subscribe");
				        	String out_trade_no = (String)packageParams.get("out_trade_no");
				        	String transaction_id=(String)packageParams.get("transaction_id");
				        	
				        	String total_fee = (String)packageParams.get("total_fee");
				        	Double total_fees=Double.parseDouble(total_fee);
				        	
				        	log.info("mch_id:"+mch_id);
				        	log.info("openid:"+openid);
				        	log.info("is_subscribe:"+is_subscribe);
				        	log.info("out_trade_no:"+out_trade_no);
				        	log.info("total_fee:"+total_fees);
				            
				            //////////执行自己的业务逻辑////////////////
				        	MerchantOrderInfo merchantOrderInfo=merchantOrderInfoService.findByMerchantOrderId(out_trade_no);
				        	if(merchantOrderInfo!=null&&!nullEmptyBlankJudge(String.valueOf(merchantOrderInfo.getBusinessType()))&&"2".equals(String.valueOf(merchantOrderInfo.getBusinessType()))){
								String userId=String.valueOf(merchantOrderInfo.getSourceUid());
								UserAccountBalance  userAccountBalance=userAccountBalanceService.findByUserId(userId);
								if(userAccountBalance!=null){
									userAccountBalance.setBalance(Double.parseDouble(total_fee)/100+userAccountBalance.getBalance());
									Boolean updatestatus=userAccountBalanceService.updateBalanceInfo(userAccountBalance);
									if(updatestatus){
										rechargeMsg="SUCCESS";	
									}else{
										rechargeMsg="ERROR";
									}
								}else{
									userAccountBalance=new UserAccountBalance();
									userAccountBalance.setUserId(userId);
									userAccountBalance.setStatus(1);
									userAccountBalance.setType(1);
									userAccountBalance.setCreateTime(new Date());
									userAccountBalanceService.saveUserAccountBalance(userAccountBalance);
									rechargeMsg="SUCCESS";
								}
								
							}
				        	if(merchantOrderInfo!=null){
				        		int notifyStatus=merchantOrderInfo.getNotifyStatus();
								int payStatus=merchantOrderInfo.getPayStatus();
								Double payCharge=0.0;
								if(payStatus!=1){
									merchantOrderInfo.setPayStatus(1);
									merchantOrderInfo.setPayAmount((total_fees-payCharge)/100);
									merchantOrderInfo.setAmount(total_fees/100);
									merchantOrderInfo.setPayCharge(0.0);
									merchantOrderInfo.setDealDate(new Date());
									merchantOrderInfo.setPayOrderId(transaction_id);
									merchantOrderInfoService.updateOrder(merchantOrderInfo);
								}
								if(notifyStatus!=1){
									 Thread thread = new Thread(new AliOrderProThread(merchantOrderInfo, merchantOrderInfoService,merchantInfoService,rechargeMsg));
									   thread.run();	
								}
								log.info("支付成功");
					            //通知微信.异步确认成功.必写.不然会一直通知后台.八次之后就认为交易失败了.
					            resXml = "<xml>" + "<return_code><![CDATA[SUCCESS]]></return_code>"
					                    + "<return_msg><![CDATA[OK]]></return_msg>" + "</xml> ";
				        	}else{
				        		log.info("支付失败,错误信息：" + packageParams.get("err_code"));
					            resXml = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>"
					                    + "<return_msg><![CDATA[报文为空]]></return_msg>" + "</xml> ";
				        	}
				        } else {
				        	log.info("支付失败,错误信息：" + packageParams.get("err_code"));
				            resXml = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>"
				                    + "<return_msg><![CDATA[报文为空]]></return_msg>" + "</xml> ";
				        }
				      
				    } else{
				    	 resXml = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>"
				                    + "<return_msg><![CDATA[报文为空]]></return_msg>" + "</xml> ";
				    	log.info("通知签名验证失败");
				    }
					
				   }
				    //------------------------------
			        //处理业务完毕
			        //------------------------------
			        BufferedOutputStream out = new BufferedOutputStream(
			                response.getOutputStream());
			        out.write(resXml.getBytes());
			        out.flush();
			        out.close();
				}
	
}