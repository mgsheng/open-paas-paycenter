package cn.com.open.openpaas.payservice.web.api.order;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;


import net.sf.json.JSONObject;

import org.apache.commons.httpclient.util.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.com.open.openpaas.payservice.app.channel.alipay.AlipayConfig;
import cn.com.open.openpaas.payservice.app.channel.alipay.AlipayCore;
import cn.com.open.openpaas.payservice.app.channel.alipay.PayUtil;
import cn.com.open.openpaas.payservice.app.channel.model.DictTradeChannel;
import cn.com.open.openpaas.payservice.app.channel.service.DictTradeChannelService;
import cn.com.open.openpaas.payservice.app.merchant.model.MerchantInfo;
import cn.com.open.openpaas.payservice.app.merchant.service.MerchantInfoService;
import cn.com.open.openpaas.payservice.app.order.model.MerchantOrderInfo;
import cn.com.open.openpaas.payservice.app.order.service.MerchantOrderInfoService;
import cn.com.open.openpaas.payservice.app.tools.BaseControllerUtil;
import cn.com.open.openpaas.payservice.app.tools.PropertiesTool;
import cn.com.open.openpaas.payservice.app.tools.SysUtil;
import cn.com.open.openpaas.payservice.dev.PayserviceDev;

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
    	//获取payStatus第三方支付状态为成功1且notifyStatus商户接收状态为未处理状态0 订单集合
	    List<MerchantOrderInfo> merchantOrderInfos=merchantOrderInfoService.findByPayAndNotifyStatus();
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
	    		
	    		String secret=PayUtil.createSign(payserviceDev.getAli_input_charset(),params,merchantInfo.getPayKey());
	    		params.put("secret", secret);
	    		
	    		log.info("~~~~~~~~~orderAutoSend secret："+secret);
	    		
	    		//向业务方发送订单状态信息
	    		if(orderInfo.getNotifyUrl()!=null && !("").equals(orderInfo.getNotifyUrl())){
	    			result = sendPost(orderInfo.getNotifyUrl(),params);
	    		}else{
	    			if(merchantInfo!=null){
	    				result = sendPost(merchantInfo.getNotifyUrl(),params);
	    			}
	    		}
	    		if(result != null && !("").equals(result)){
	    			log.info("~~~~~~~~~~~~~~orderAutoSend result："+result+"~~~~~~~~~~~~~~~~~~~~");
	    			Map map=(Map) JSONObject.toBean(JSONObject.fromObject(result),Map.class);
	    			if("ok".equals(map.get("state"))){//商户处理成功
	    				orderInfo.setNotifyStatus(1);
	    			}else{
	    				orderInfo.setNotifyStatus(2);
	    			}
    				orderInfo.setNotifyTimes();//方法中自动+1
    				orderInfo.setNotifyDate(new Date());
    				merchantOrderInfoService.updateNotifyStatus(orderInfo);//更新订单状态
	    		}
	    		else{
	    			orderInfo.setNotifyStatus(2);
	    			orderInfo.setNotifyTimes();//方法中自动+1
	    			merchantOrderInfoService.updateNotifyStatus(orderInfo);//更新订单状态
	    		}
	    	}
	    }
	    log.info("~~~~~~~~~~~~~~~orderAutoSend end~~~~~~~~~~~~~~~~");
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
            System.out.println(result);
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