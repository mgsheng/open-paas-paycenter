package cn.com.open.openpaas.payservice.app.channel.alipay;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.httpclient.util.DateUtil;
import org.dom4j.DocumentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.com.open.openpaas.payservice.app.channel.UnifyPayUtil;
import cn.com.open.openpaas.payservice.app.channel.model.DictTradeChannel;
import cn.com.open.openpaas.payservice.app.channel.service.ChannelRateService;
import cn.com.open.openpaas.payservice.app.channel.service.DictTradeChannelService;
import cn.com.open.openpaas.payservice.app.log.UnifyPayControllerLog;
import cn.com.open.openpaas.payservice.app.log.model.PayLogName;
import cn.com.open.openpaas.payservice.app.log.model.PayServiceLog;
import cn.com.open.openpaas.payservice.app.merchant.model.MerchantInfo;
import cn.com.open.openpaas.payservice.app.merchant.service.MerchantInfoService;
import cn.com.open.openpaas.payservice.app.order.model.MerchantOrderInfo;
import cn.com.open.openpaas.payservice.app.order.service.MerchantOrderInfoService;
import cn.com.open.openpaas.payservice.app.tools.BaseControllerUtil;
import cn.com.open.openpaas.payservice.app.tools.DateTools;
import cn.com.open.openpaas.payservice.app.tools.SendPostMethod;
import cn.com.open.openpaas.payservice.dev.PayserviceDev;


/**
 * 
 */
@Controller
@RequestMapping("/alipay/order/")
public class AliOrderCallbackController extends BaseControllerUtil {
	private static final Logger log = LoggerFactory.getLogger(AliOrderCallbackController.class);
	 @Autowired
	 private MerchantOrderInfoService merchantOrderInfoService;
	 @Autowired
	 private PayserviceDev payserviceDev;
	 @Autowired
	 private DictTradeChannelService dictTradeChannelService;
	 @Autowired
	 private MerchantInfoService merchantInfoService;
	 @Autowired
	 private ChannelRateService channelRateService;
	/**
	 * 支付宝订单回调接口
	 * @param request
	 * @param response
	 * @throws IOException 
	 * @throws DocumentException 
	 * @throws MalformedURLException 
	 */
	@RequestMapping("callBack")
	public String callBack(HttpServletRequest request,HttpServletResponse response, Model model) throws MalformedURLException, DocumentException, IOException {
		   log.info("-----------------------callBack  alipay/order-----------------------------------------");
		//获取支付宝GET过来反馈信息
		long startTime = System.currentTimeMillis();
		Map<String,String> params = new HashMap<String,String>();
		Map requestParams = request.getParameterMap();
		String backMsg="";
		for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext();) {
			String name = (String) iter.next();
			String[] values = (String[]) requestParams.get(name);
			String valueStr = "";
			for (int i = 0; i < values.length; i++) {
				valueStr = (i == values.length - 1) ? valueStr + values[i]
						: valueStr + values[i] + ",";
			}
			//乱码解决，这段代码在出现乱码时使用。如果mysign和sign不相等也可以使用这段代码转化
			valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
			params.put(name, valueStr);
		}
		//获取支付宝的通知返回参数，可参考技术文档中页面跳转同步通知参数列表(以下仅供参考)//
		//商户订单号
		String out_trade_no = new String(request.getParameter("out_trade_no").getBytes("ISO-8859-1"),"UTF-8");
		//支付宝交易号
		String trade_no = new String(request.getParameter("trade_no").getBytes("ISO-8859-1"),"UTF-8");
        Double total_fee=Double .valueOf(request.getParameter("total_fee"));
		//交易状态
		String trade_status = new String(request.getParameter("trade_status").getBytes("ISO-8859-1"),"UTF-8");
		String subject = new String(request.getParameter("subject").getBytes("ISO-8859-1"),"UTF-8");
		String body = "";
		if(!nullEmptyBlankJudge(request.getParameter("body"))){
			body=new String(request.getParameter("body").getBytes("ISO-8859-1"),"UTF-8");	
		}
		MerchantOrderInfo merchantOrderInfo=merchantOrderInfoService.findById(out_trade_no);
		
		log.info("ali callback out_trade_no========="+out_trade_no);
		
		 PayServiceLog payServiceLog=new PayServiceLog();
		 payServiceLog.setOrderId(out_trade_no);
		 payServiceLog.setPayOrderId(trade_no);
		 payServiceLog.setProductDesc(body);
		 payServiceLog.setProductName(subject);
		 payServiceLog.setAmount(String.valueOf(total_fee*100));
		if(merchantOrderInfo!=null&&merchantOrderInfo.getPayStatus()!=1){
			  DictTradeChannel dictTradeChannel=dictTradeChannelService.findByMAI(String.valueOf(merchantOrderInfo.getMerchantId()),Channel.ALI.getValue());
		//添加日志
		 
		 payServiceLog.setAppId(merchantOrderInfo.getAppId());
		 payServiceLog.setChannelId(String.valueOf(merchantOrderInfo.getChannelId()));
		 payServiceLog.setCreatTime(DateTools.dateToString(new Date(), "yyyy-MM-dd HH:mm:ss"));
		 payServiceLog.setLogType(payserviceDev.getLog_type());
		 payServiceLog.setMerchantId(String.valueOf(merchantOrderInfo.getMerchantId()));
		 payServiceLog.setMerchantOrderId(merchantOrderInfo.getMerchantOrderId());
		 payServiceLog.setPaymentId(String.valueOf(merchantOrderInfo.getPaymentId()));
		 payServiceLog.setRealAmount(String.valueOf(total_fee*100));
		 payServiceLog.setSourceUid(merchantOrderInfo.getSourceUid());
		 payServiceLog.setUsername(merchantOrderInfo.getUserName());
		 payServiceLog.setLogName(PayLogName.ALIPAY_RETURN_START);
         payServiceLog.setStatus("ok");
         UnifyPayControllerLog.log(startTime,payServiceLog,payserviceDev);
        String returnUrl=merchantOrderInfo.getReturnUrl();
 		MerchantInfo merchantInfo = null;
 		if(nullEmptyBlankJudge(returnUrl)){
 			merchantInfo=merchantInfoService.findById(merchantOrderInfo.getMerchantId());
 			returnUrl=merchantInfo.getReturnUrl();
 		}
		//计算得出通知验证结果
		boolean verify_result = AlipayNotify.verify(params,dictTradeChannel.getKeyValue(),dictTradeChannel.getInputCharset());
		//verify_result=false;
		if(verify_result){//验证成功
			//////////////////////////////////////////////////////////////////////////////////////////
			//请在这里加上商户的业务逻辑程序代码
			//——请根据您的业务逻辑来编写程序（以下代码仅作参考）——
			if(trade_status.equals("TRADE_FINISHED") || trade_status.equals("TRADE_SUCCESS")){
				//判断该笔订单是否在商户网站中已经做过处理
				//如果没有做过处理，根据订单号（out_trade_no）在商户网站的订单系统中查到该笔订单的详细，并执行商户的业务程序
				 payServiceLog.setLogName(PayLogName.ALIPAY_RETURN_END);
				 if(!nullEmptyBlankJudge(returnUrl)){
					 
					
					 //Map<String, String> dataMap=new HashMap<String, String>();
					 /*int payStatus=merchantOrderInfo.getPayStatus();
						Double payCharge=0.0;
						payCharge=UnifyPayUtil.getPayCharge(merchantOrderInfo,channelRateService);
						if(payStatus!=1){
							merchantOrderInfo.setPayStatus(1);
							merchantOrderInfo.setPayAmount(total_fee-payCharge);
							merchantOrderInfo.setAmount(total_fee);
							merchantOrderInfo.setPayCharge(payCharge);
							merchantOrderInfo.setDealDate(new Date());
							merchantOrderInfo.setPayOrderId(trade_no);
							merchantOrderInfoService.updateOrder(merchantOrderInfo);
							if(merchantOrderInfo!=null&&!nullEmptyBlankJudge(String.valueOf(merchantOrderInfo.getBusinessType()))&&"2".equals(String.valueOf(merchantOrderInfo.getBusinessType()))){
								rechargeMsg=UnifyPayUtil.recordAndBalance(total_fee*100,merchantOrderInfo,userSerialRecordService,userAccountBalanceService,payserviceDev);
							}
						}*/
					 String buf="";
					    SortedMap<String,String> sParaTemp = new TreeMap<String,String>();
						sParaTemp.put("orderId", merchantOrderInfo.getId());
				        sParaTemp.put("outTradeNo", merchantOrderInfo.getMerchantOrderId());
				        sParaTemp.put("merchantId", String.valueOf(merchantOrderInfo.getMerchantId()));
				        sParaTemp.put("paymentType", String.valueOf(merchantOrderInfo.getPaymentId()));
						sParaTemp.put("paymentChannel", String.valueOf(merchantOrderInfo.getChannelId()));
						sParaTemp.put("feeType", "CNY");
						sParaTemp.put("guid", merchantOrderInfo.getGuid());
						sParaTemp.put("appUid",String.valueOf(merchantOrderInfo.getSourceUid()));
						//sParaTemp.put("exter_invoke_ip",exter_invoke_ip);
						sParaTemp.put("timeEnd", DateUtil.formatDate(new Date(), "yyyyMMddHHmmss"));
						sParaTemp.put("totalFee", String.valueOf((int)(merchantOrderInfo.getOrderAmount()*100)));
						sParaTemp.put("goodsId", merchantOrderInfo.getMerchantProductId());
						sParaTemp.put("goodsName",merchantOrderInfo.getMerchantProductName());
						sParaTemp.put("goodsDesc", merchantOrderInfo.getMerchantProductDesc());
						sParaTemp.put("parameter", merchantOrderInfo.getParameter1()+"payCharge="+String.valueOf(merchantOrderInfo.getPayCharge()));
						sParaTemp.put("userName", merchantOrderInfo.getSourceUserName());
					    String mySign = PayUtil.callBackCreateSign(AlipayConfig.input_charset,sParaTemp,merchantInfo.getPayKey());
					    sParaTemp.put("secret", mySign);
					    buf =SendPostMethod.buildRequest(sParaTemp, "post", "ok", returnUrl);
					    model.addAttribute("res", buf);
	     			    return "pay/payMaxRedirect"; 
				 }else{
					 backMsg="success";
				 }
			}else{
				  payServiceLog.setErrorCode("2");
		          payServiceLog.setStatus("PAYFAIL");
		          payServiceLog.setLogName(PayLogName.ALIPAY_RETURN_END);
				 merchantOrderInfoService.updatePayInfo(2,String.valueOf(merchantOrderInfo.getId()),"PAYFAIL");
			}
		}else{
			//该页面可做页面美工编辑
			  payServiceLog.setErrorCode("4");
	          payServiceLog.setStatus("VERIFYERROR");
	          payServiceLog.setLogName(PayLogName.ALIPAY_RETURN_END);
	          UnifyPayControllerLog.log(startTime,payServiceLog,payserviceDev);
	          //merchantOrderInfoService.updatePayInfo(4,String.valueOf(merchantOrderInfo.getId()),"VERIFYERROR");
			backMsg="VERIFYERROR";
		}
		}else{
			 payServiceLog.setErrorCode("2");
	          payServiceLog.setStatus("error");
	          backMsg="error";
	          if(merchantOrderInfo!=null&&merchantOrderInfo.getPayStatus()==1)
				 {
	        	  String returnUrl=merchantOrderInfo.getReturnUrl();
	       		MerchantInfo merchantInfo = null;
	       		if(nullEmptyBlankJudge(returnUrl)){
	       			merchantInfo=merchantInfoService.findById(merchantOrderInfo.getMerchantId());
	       			returnUrl=merchantInfo.getReturnUrl();
	       		}
	       	 Double payCharge=0.0;
		     payCharge=UnifyPayUtil.getPayCharge(merchantOrderInfo,channelRateService);
				//判断该笔订单是否在商户网站中已经做过处理
				//如果没有做过处理，根据订单号（out_trade_no）在商户网站的订单系统中查到该笔订单的详细，并执行商户的业务程序
				 payServiceLog.setLogName(PayLogName.ALIPAY_RETURN_END);
				 if(!nullEmptyBlankJudge(returnUrl)){
					 String buf="";
					    SortedMap<String,String> sParaTemp = new TreeMap<String,String>();
						sParaTemp.put("orderId", merchantOrderInfo.getId());
				        sParaTemp.put("outTradeNo", merchantOrderInfo.getMerchantOrderId());
				        sParaTemp.put("merchantId", String.valueOf(merchantOrderInfo.getMerchantId()));
				        sParaTemp.put("paymentType", String.valueOf(merchantOrderInfo.getPaymentId()));
						sParaTemp.put("paymentChannel", String.valueOf(merchantOrderInfo.getChannelId()));
						sParaTemp.put("feeType", "CNY");
						sParaTemp.put("guid", merchantOrderInfo.getGuid());
						sParaTemp.put("appUid",String.valueOf(merchantOrderInfo.getSourceUid()));
						//sParaTemp.put("exter_invoke_ip",exter_invoke_ip);
						sParaTemp.put("timeEnd", DateUtil.formatDate(new Date(), "yyyyMMddHHmmss"));
						sParaTemp.put("totalFee", String.valueOf((int)(merchantOrderInfo.getOrderAmount()*100)));
						sParaTemp.put("goodsId", merchantOrderInfo.getMerchantProductId());
						sParaTemp.put("goodsName",merchantOrderInfo.getMerchantProductName());
						sParaTemp.put("goodsDesc", merchantOrderInfo.getMerchantProductDesc());
						sParaTemp.put("parameter", merchantOrderInfo.getParameter1()+"payCharge="+String.valueOf(merchantOrderInfo.getPayCharge()));
						sParaTemp.put("userName", merchantOrderInfo.getSourceUserName());
					    String mySign = PayUtil.callBackCreateSign(AlipayConfig.input_charset,sParaTemp,merchantInfo.getPayKey());
					    sParaTemp.put("secret", mySign);
					    buf =SendPostMethod.buildRequest(sParaTemp, "post", "ok", returnUrl);
					    model.addAttribute("res", buf);
	     			    return "pay/payMaxRedirect"; 
				 }
			
	        	  payServiceLog.setStatus("already processed");
				 }
	          payServiceLog.setLogName(PayLogName.ALIPAY_RETURN_END);
				
	          UnifyPayControllerLog.log(startTime,payServiceLog,payserviceDev);
			
		} 
		 model.addAttribute("backMsg", backMsg);
		 model.addAttribute("productName", merchantOrderInfo.getMerchantProductName());
		 return "pay/callBack";
	}
	 /** 
     * 发送POST请求 
     *  
     * @param url 
     *            目的地址 
     * @param parameters 
     *            请求参数，Map类型。 
     * @return 远程响应结果 
     */  
    public static String sendPost(String url, SortedMap<Object,Object> sParaTemp) {  
        String result = "";// 返回的结果  
        BufferedReader in = null;// 读取响应输入流  
        PrintWriter out = null;  
        StringBuffer sb = new StringBuffer();// 处理请求参数  
        String params="";
        try {  
		Set es = sParaTemp.entrySet();//所有参与传参的参数按照accsii排序（升序）
		Iterator it = es.iterator();
		while(it.hasNext()) {
			Map.Entry entry = (Map.Entry)it.next();
			String k = (String)entry.getKey();
			Object v = entry.getValue();
			if(null != v && !"".equals(v) 
					&& !"sign".equals(k) && !"key".equals(k)) {
				String value=URLEncoder.encode((String)v, "UTF-8");
				sb.append(k + "=" + value + "&");
			}
		  }
		   String temp_params = sb.toString(); 
		   params = temp_params.substring(0, temp_params.length() - 1);  
		   log.info("发送参数："+params);
            // 创建URL对象  
            java.net.URL connURL = new java.net.URL(url);  
            // 打开URL连接  
            java.net.HttpURLConnection httpConn = (java.net.HttpURLConnection) connURL  
                    .openConnection();  
            // 设置通用属性  
            httpConn.setRequestProperty("Accept", "*/*");  
            httpConn.setRequestProperty("Connection", "Keep-Alive");  
            httpConn.setRequestProperty("User-Agent",  
                    "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 6.1)");  
            // 设置POST方式  
            httpConn.setDoInput(true);  
            httpConn.setDoOutput(true);  
            // 获取HttpURLConnection对象对应的输出流  
            out = new PrintWriter(httpConn.getOutputStream());  
            // 发送请求参数  
            //out.write(params); 
            out.write(params);  
            // flush输出流的缓冲  
            out.flush();  
            // 定义BufferedReader输入流来读取URL的响应，设置编码方式  
            in = new BufferedReader(new InputStreamReader(httpConn  
                    .getInputStream(), "UTF-8"));  
            String line;  
            // 读取返回的内容  
            while ((line = in.readLine()) != null) {  
                result += line;  
                
            }  
            //System.out.println(result);
        } catch (Exception e) {  
            e.printStackTrace();  
            return null;
        } finally {  
            try {  
                if (out != null) {  
                    out.close();  
                }  
                if (in != null) {  
                    in.close();  
                }  
            } catch (IOException ex) {  
                ex.printStackTrace();  
            }  
        }  
        return result;  
    }  	

}