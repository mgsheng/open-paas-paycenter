package cn.com.open.openpaas.payservice.app.channel.alipay;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.Timer;
import java.util.TreeMap;

import net.sf.json.JSONObject;

import org.apache.commons.httpclient.util.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.com.open.openpaas.payservice.app.log.UnifyPayControllerLog;
import cn.com.open.openpaas.payservice.app.log.model.PayLogName;
import cn.com.open.openpaas.payservice.app.log.model.PayServiceLog;
import cn.com.open.openpaas.payservice.app.merchant.model.MerchantInfo;
import cn.com.open.openpaas.payservice.app.merchant.service.MerchantInfoService;
import cn.com.open.openpaas.payservice.app.order.model.MerchantOrderInfo;
import cn.com.open.openpaas.payservice.app.order.service.MerchantOrderInfoService;
import cn.com.open.openpaas.payservice.app.tools.DateTools;
import cn.com.open.openpaas.payservice.dev.PayserviceDev;


/**
 * 订单结果处理
 * @author dongminghao
 *
 */
public class AliOrderProThread implements Runnable {
	private static final Logger log = LoggerFactory.getLogger(AliOrderProThread.class);

	private MerchantOrderInfoService merchantOrderInfoService;
	private MerchantInfoService merchantInfoService;
	private MerchantOrderInfo merchantOrderInfo;
	private PayserviceDev payserviceDev;
	
	public AliOrderProThread(MerchantOrderInfo merchantOrderInfo,MerchantOrderInfoService merchantOrderInfoService,MerchantInfoService merchantInfoService, PayserviceDev payserviceDev){
		this.merchantOrderInfoService = merchantOrderInfoService;
		this.merchantInfoService=merchantInfoService;
		this.merchantOrderInfo=merchantOrderInfo;
		this.payserviceDev=payserviceDev;
	}
	
	@Override
	public void run() {
		if(merchantOrderInfoService == null){
			return;
		}
		long startTime = System.currentTimeMillis();
		String notifyUrl=merchantOrderInfo.getNotifyUrl();
		MerchantInfo merchantInfo = null;
		if(nullEmptyBlankJudge(notifyUrl)){
			merchantInfo=merchantInfoService.findById(merchantOrderInfo.getMerchantId());
			notifyUrl=merchantInfo.getNotifyUrl();
		}
		//拼接发送的加密信息
		SortedMap<Object,Object> sParaTemp = new TreeMap<Object,Object>();
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
		sParaTemp.put("totalFee", String.valueOf((int)(merchantOrderInfo.getPayAmount()*100)));
		sParaTemp.put("goodsId", merchantOrderInfo.getMerchantProductId());
		sParaTemp.put("goodsName",merchantOrderInfo.getMerchantProductName());
		sParaTemp.put("goodsDesc", merchantOrderInfo.getMerchantProductDesc());
		sParaTemp.put("parameter", merchantOrderInfo.getParameter1()+"payCharge="+String.valueOf((int)(merchantOrderInfo.getPayCharge()*100))+";");
		sParaTemp.put("userName", merchantOrderInfo.getSourceUserName());
		String mySign = PayUtil.createSign(AlipayConfig.input_charset,sParaTemp,merchantInfo.getPayKey());
		sParaTemp.put("secret", mySign);
		Boolean callBackSend=false;
		int count=0;
		String sendMsg="";
		
		//添加日志
		 PayServiceLog payServiceLog=new PayServiceLog();
		 payServiceLog.setAmount(String.valueOf(merchantOrderInfo.getAmount()*100));
		 payServiceLog.setAppId(merchantOrderInfo.getAppId());
		 payServiceLog.setChannelId(String.valueOf(merchantOrderInfo.getChannelId()));
		 payServiceLog.setCreatTime(DateTools.dateToString(new Date(), "yyyy-MM-dd HH:mm:ss"));
		 payServiceLog.setLogType(payserviceDev.getLog_type());
		 payServiceLog.setMerchantId(String.valueOf(merchantOrderInfo.getMerchantId()));
		 payServiceLog.setMerchantOrderId(String.valueOf(merchantOrderInfo.getMerchantOrderId()));
		 payServiceLog.setOrderId(merchantOrderInfo.getId());
		 payServiceLog.setPaymentId(String.valueOf(merchantOrderInfo.getPaymentId()));
		 payServiceLog.setPayOrderId(String.valueOf(merchantOrderInfo.getPayOrderId()));
		 payServiceLog.setProductDesc(merchantOrderInfo.getMerchantProductDesc());
		 payServiceLog.setProductName(merchantOrderInfo.getMerchantProductName());
		 payServiceLog.setRealAmount(String.valueOf(merchantOrderInfo.getPayAmount()*100));
		 payServiceLog.setSourceUid(merchantOrderInfo.getSourceUid());
		 payServiceLog.setUsername(merchantOrderInfo.getUserName());
		 payServiceLog.setLogName(PayLogName.NOTIFY_START);
		 payServiceLog.setStatus("ok");
		  UnifyPayControllerLog.log(startTime,payServiceLog,payserviceDev);
		// sendPost(merchantOrderInfo.getNotifyUrl(),parameters);
		  do { 
			      log.info("-----------------------------notify-start--------------------------");
		    	  String returnValue= sendPost(notifyUrl,sParaTemp);
		    	  count+=1;
		    	  if(returnValue!=null)
		    	  {
		    		  
		    		 /* if(merchantOrderInfo.getMerchantId()==Integer.parseInt(payserviceDev.getOes_merchantId())){
		    			  callBackSend= analysisOesValue(returnValue);
		    			  if(callBackSend){
							  merchantOrderInfo.setNotifyStatus(1);
							  merchantOrderInfoService.updateNotifyStatus(merchantOrderInfo);
							  payServiceLog.setLogName(PayLogName.NOTIFY_END);
							  UnifyPayControllerLog.log(startTime,payServiceLog,payserviceDev);
							  sendMsg="通知成功！";
						  }else{
							  if(count>1){
								  callBackSend=true;  
							  }
							merchantOrderInfo.setNotifyStatus(2);
						    merchantOrderInfo.setNotifyTimes(merchantOrderInfo.getNotifyTimes()+1);
							merchantOrderInfoService.updateNotifyStatus(merchantOrderInfo);  
							payServiceLog.setErrorCode("1");
							 payServiceLog.setLogName(PayLogName.NOTIFY_END);
							 payServiceLog.setStatus("error");
								UnifyPayControllerLog.log(startTime,payServiceLog,payserviceDev);
							sendMsg="通知失败！";
			    	   }
		    		  }else{*/
		    			  JSONObject reqjson = JSONObject.fromObject(returnValue);
						  callBackSend=analysisValue(reqjson);
						  if(callBackSend){
							  merchantOrderInfo.setNotifyStatus(1);
							  merchantOrderInfoService.updateNotifyStatus(merchantOrderInfo);
							  payServiceLog.setLogName(PayLogName.NOTIFY_END);
							  UnifyPayControllerLog.log(startTime,payServiceLog,payserviceDev);
							  sendMsg="通知成功！";
						  }else{
							  if(count>1){
								  callBackSend=true;  
							  }
							merchantOrderInfo.setNotifyStatus(2);
						    merchantOrderInfo.setNotifyTimes(merchantOrderInfo.getNotifyTimes()+1);
							merchantOrderInfoService.updateNotifyStatus(merchantOrderInfo);  
							payServiceLog.setErrorCode("1");
							 payServiceLog.setLogName(PayLogName.NOTIFY_END);
							 payServiceLog.setStatus("error");
								UnifyPayControllerLog.log(startTime,payServiceLog,payserviceDev);
							sendMsg="通知失败！";
			    	   } 
		    		//  }
				  }
		    	  else{
		    		  if(count>1){
						  callBackSend=true;  
					  }
					merchantOrderInfo.setNotifyStatus(2);
				    merchantOrderInfo.setNotifyTimes(merchantOrderInfo.getNotifyTimes()+1);
					merchantOrderInfoService.updateNotifyStatus(merchantOrderInfo);  
					//连接超时
					payServiceLog.setErrorCode("2");
					 payServiceLog.setLogName(PayLogName.NOTIFY_END);
				    payServiceLog.setStatus("error");
					UnifyPayControllerLog.log(startTime,payServiceLog,payserviceDev);
					sendMsg="通知失败！";
		    	  }
			} while (!callBackSend);
		  log.info("notifyStatus："+sendMsg);
		}
	  public static Boolean analysisValue(JSONObject obj ){
	    	String state = obj.getString("state");
			if(!state.equals("ok")){
				return false;
			}else{
				return true;
			}
	    }	
	  public static Boolean analysisOesValue(String obj ){
			if(obj.indexOf("SUCCESS")!=-1){
				return true;
			}else{
				return false;
			}
	    }
	  private static boolean nullEmptyBlankJudge(String str){
	        return null==str||str.isEmpty()||"".equals(str.trim());
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
