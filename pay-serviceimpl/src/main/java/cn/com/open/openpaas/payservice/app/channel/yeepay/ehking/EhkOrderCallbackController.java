package cn.com.open.openpaas.payservice.app.channel.yeepay.ehking;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URLEncoder;
import java.util.Date;
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

import cn.com.open.openpaas.payservice.app.channel.alipay.AlipayConfig;
import cn.com.open.openpaas.payservice.app.channel.alipay.PayUtil;
import cn.com.open.openpaas.payservice.app.log.UnifyPayControllerLog;
import cn.com.open.openpaas.payservice.app.log.model.PayLogName;
import cn.com.open.openpaas.payservice.app.log.model.PayServiceLog;
import cn.com.open.openpaas.payservice.app.merchant.model.MerchantInfo;
import cn.com.open.openpaas.payservice.app.merchant.service.MerchantInfoService;
import cn.com.open.openpaas.payservice.app.order.model.MerchantOrderInfo;
import cn.com.open.openpaas.payservice.app.order.service.MerchantOrderInfoService;
import cn.com.open.openpaas.payservice.app.common.BaseControllerUtil;
import cn.com.open.openpaas.payservice.app.tools.DateTools;
import cn.com.open.openpaas.payservice.app.tools.SendPostMethod;
import cn.com.open.openpaas.payservice.dev.PayserviceDev;


/**
 * 
 */
@Controller
@RequestMapping("/ehk/order/")
public class EhkOrderCallbackController extends BaseControllerUtil {
	private static final Logger log = LoggerFactory.getLogger(EhkOrderCallbackController.class);
	 @Autowired
	 private MerchantOrderInfoService merchantOrderInfoService;
	 @Autowired
	 private PayserviceDev payserviceDev;
	 @Autowired
	 private MerchantInfoService merchantInfoService;
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
		String backMsg="";
		//获取支付宝的通知返回参数，可参考技术文档中页面跳转同步通知参数列表(以下仅供参考)//
		//商户订单号
		String out_trade_no = new String(request.getParameter("requestId").getBytes("ISO-8859-1"),"UTF-8");
		//String status = new String(request.getParameter("status").getBytes("ISO-8859-1"),"UTF-8");
		log.info("ehk callback orderId======================="+ out_trade_no);
		MerchantOrderInfo merchantOrderInfo=merchantOrderInfoService.findById(out_trade_no);
		
		 PayServiceLog payServiceLog=new PayServiceLog();
		if(merchantOrderInfo!=null&&merchantOrderInfo.getPayStatus()!=1){
		//添加日志
		 payServiceLog.setAmount(String.valueOf(merchantOrderInfo.getOrderAmount()*100));
		 payServiceLog.setAppId(merchantOrderInfo.getAppId());
		 payServiceLog.setChannelId(String.valueOf(merchantOrderInfo.getChannelId()));
		 payServiceLog.setCreatTime(DateTools.dateToString(new Date(), "yyyyMMddHHmmss"));
		 payServiceLog.setLogType(payserviceDev.getLog_type());
		 payServiceLog.setMerchantId(String.valueOf(merchantOrderInfo.getMerchantId()));
		 payServiceLog.setMerchantOrderId(merchantOrderInfo.getMerchantOrderId());
		 payServiceLog.setOrderId(out_trade_no);
		 payServiceLog.setPaymentId(String.valueOf(merchantOrderInfo.getPaymentId()));
		 payServiceLog.setRealAmount(String.valueOf(merchantOrderInfo.getOrderAmount()*100));
		 payServiceLog.setSourceUid(merchantOrderInfo.getSourceUid());
		 payServiceLog.setUsername(merchantOrderInfo.getUserName());
		 payServiceLog.setLogName(PayLogName.EHK_RETURN_START);
         payServiceLog.setStatus("ok");
         UnifyPayControllerLog.log(startTime,payServiceLog,payserviceDev);
        String returnUrl=merchantOrderInfo.getReturnUrl();
 		MerchantInfo merchantInfo = null;
 		merchantInfo=merchantInfoService.findById(merchantOrderInfo.getMerchantId());
 		if(nullEmptyBlankJudge(returnUrl)){
 			returnUrl=merchantInfo.getReturnUrl();
 		}
			//////////////////////////////////////////////////////////////////////////////////////////
			//请在这里加上商户的业务逻辑程序代码
			//——请根据您的业务逻辑来编写程序（以下代码仅作参考）——
			//if(status.equals("SUCCESS")){
				//判断该笔订单是否在商户网站中已经做过处理
				//如果没有做过处理，根据订单号（out_trade_no）在商户网站的订单系统中查到该笔订单的详细，并执行商户的业务程序
				 payServiceLog.setLogName(PayLogName.EHK_RETURN_END);
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
						sParaTemp.put("timeEnd", DateTools.dateToString(new Date(), "yyyyMMddHHmmss"));
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
//			}else if(status.equals("FAILED")||status.equals("ERROR")){
//				//失败或者错误
//				  payServiceLog.setErrorCode("2");
//		          payServiceLog.setStatus("error");
//		          payServiceLog.setLogName(PayLogName.EHK_RETURN_END);
//		          UnifyPayControllerLog.log(startTime,payServiceLog,payserviceDev);
//			}
		}else{
			//该页面可做页面美工编辑
			  payServiceLog.setErrorCode("2");
			  payServiceLog.setStatus("error");
			  backMsg="error";
			  if(merchantOrderInfo!=null&&merchantOrderInfo.getPayStatus()==1)
				 {
				     payServiceLog.setStatus("already processed");
				     backMsg="success";
				 }
	          payServiceLog.setLogName(PayLogName.EHK_RETURN_END);
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