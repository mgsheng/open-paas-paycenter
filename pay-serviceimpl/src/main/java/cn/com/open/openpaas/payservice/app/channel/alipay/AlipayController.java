package cn.com.open.openpaas.payservice.app.channel.alipay;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import net.sf.json.JSONObject;

import org.dom4j.DocumentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.com.open.openpaas.payservice.app.channel.model.DictTradeChannel;
import cn.com.open.openpaas.payservice.app.channel.service.DictTradeChannelService;
import cn.com.open.openpaas.payservice.dev.PayserviceDev;

public class AlipayController {
	private static final Logger log = LoggerFactory.getLogger(AlipayController.class);
	  public static Boolean analysisValue(JSONObject obj ){
	    	String state = obj.getString("state");
			if(!state.equals("ok")){
				return false;
			}else{
				return true;
			}
	    }	
	  private static boolean nullEmptyBlankJudge(String str){
	        return null==str||str.isEmpty()||"".equals(str.trim());
	    }
	  /** 
	     * 使用java正则表达式去掉多余的.与0 
	     * @param s 
	     * @return  
	     */  
	    public static String subZeroAndDot(String s){  
	        if(s.indexOf(".") > 0){  
	            s = s.replaceAll("0+?$", "");//去掉多余的0  
	            s = s.replaceAll("[.]$", "");//如最后一位是.则去掉  
	        }  
	        return s;  
	    }  
	/**
	 * 返回支付宝调用地址
	 **/
	public  static String getAliPayUrl(String marchantId,String out_trade_no,String subject,String total_fee,String body,DictTradeChannelService dictTradeChannelService,PayserviceDev payserviceDev) throws MalformedURLException, DocumentException, IOException {
		DictTradeChannel dictTradeChannels=dictTradeChannelService.findByMAI(marchantId,Channel.ALI.getValue());
		String other= dictTradeChannels.getOther();
		Map<String, String> others = new HashMap<String, String>();
		others=getPartner(other);
	
		//sign_type:MD5#payment_type:1#input_charset:utf-sign_type:MD5#payment_type:1#input_charset:utf-8#service:create_direct_pay_by_user#
		//partner:2088801478647757#payment_type:1#it_b_pay:90m
    	String partner=others.get("partner");
    	String seller_id=others.get("partner"); 
    	String it_b_pay=others.get("it_b_pay"); 
        String notify_url =dictTradeChannels.getNotifyUrl();
    	String return_url=dictTradeChannels.getBackurl();
    	//String log_path=request.getParameter("log_path");
    	String input_charset=dictTradeChannels.getInputCharset();
    	String payment_type=dictTradeChannels.getPaymentType();
    	String service=payserviceDev.getAli_service();
		//把请求参数打包成数组
		Map<String, String> sParaTemp = new HashMap<String, String>();
		sParaTemp.put("service", service);
        sParaTemp.put("partner", partner);
        sParaTemp.put("seller_id", seller_id);
        sParaTemp.put("_input_charset", input_charset);
		sParaTemp.put("payment_type", payment_type);
		sParaTemp.put("notify_url",notify_url );
		sParaTemp.put("return_url", return_url);
		sParaTemp.put("anti_phishing_key",AlipaySubmit.query_timestamp());
		//sParaTemp.put("exter_invoke_ip",exter_invoke_ip);
		sParaTemp.put("out_trade_no", out_trade_no);
		sParaTemp.put("it_b_pay", it_b_pay);
		//subject = URLEncoder.encode(subject,"UTF-8");
		sParaTemp.put("subject", subject);
		sParaTemp.put("total_fee",total_fee);
		//sParaTemp.put("body", body);
		 Map<String, String> parameters = buildRequestPara(sParaTemp,payserviceDev);
            StringBuffer sb = new StringBuffer();// 处理请求参数  
            String params = "";// 编码之后的参数  
                // 编码请求参数  
                if (parameters.size() == 1) {  
                    for (String name : parameters.keySet()) {  
                        sb.append(name).append("=").append(  
                                java.net.URLEncoder.encode(parameters.get(name),  
                                        "UTF-8"));  
                    }  
                    params = sb.toString();  
                } else {  
                    for (String name : parameters.keySet()) {  
                        sb.append(name).append("=").append(  
                        		java.net.URLEncoder.encode(parameters.get(name),  
                                        "UTF-8")).append("&");  
                    }  
                    String temp_params = sb.toString();  
                    params = temp_params.substring(0, temp_params.length() - 1);  
            }  
           	 return params;
    }
	
	/**
	 * 返回支付宝网银调用地址
	 **/
	public  static String getEBankPayUrl(String marchantId,String out_trade_no,String subject,String total_fee,String body,DictTradeChannelService dictTradeChannelService,PayserviceDev payserviceDev,String defaultbank) throws MalformedURLException, DocumentException, IOException {
		DictTradeChannel dictTradeChannels=dictTradeChannelService.findByMAI(marchantId,Channel.ALI.getValue());
		String other= dictTradeChannels.getOther();
		Map<String, String> others = new HashMap<String, String>();
		others=getPartner(other);
		
    	String partner=others.get("partner");
    	String seller_id=others.get("partner"); 
    	String it_b_pay=others.get("it_b_pay"); 
        String notify_url =dictTradeChannels.getNotifyUrl();
    	String return_url=dictTradeChannels.getBackurl();
    	//String log_path=request.getParameter("log_path");
    	String input_charset=dictTradeChannels.getInputCharset();
    	String payment_type=dictTradeChannels.getPaymentType();
    	String service=payserviceDev.getAli_service();
		//把请求参数打包成数组
		Map<String, String> sParaTemp = new HashMap<String, String>();
		sParaTemp.put("service", service);
        sParaTemp.put("partner", partner);
        sParaTemp.put("seller_id", seller_id);
        sParaTemp.put("_input_charset", input_charset);
		sParaTemp.put("payment_type", payment_type);
		sParaTemp.put("notify_url", notify_url);
		sParaTemp.put("return_url", return_url);
		sParaTemp.put("anti_phishing_key",AlipaySubmit.query_timestamp());
		//sParaTemp.put("exter_invoke_ip",exter_invoke_ip);
		sParaTemp.put("out_trade_no", out_trade_no);
		sParaTemp.put("defaultbank", defaultbank);
		sParaTemp.put("it_b_pay", it_b_pay);
		//subject = URLEncoder.encode(subject,"UTF-8");
		sParaTemp.put("subject", subject);
		sParaTemp.put("total_fee",total_fee);
		sParaTemp.put("body", body);
		 Map<String, String> parameters = buildRequestPara(sParaTemp,payserviceDev);
            StringBuffer sb = new StringBuffer();// 处理请求参数  
            String params = "";// 编码之后的参数  
                // 编码请求参数  
                if (parameters.size() == 1) {  
                    for (String name : parameters.keySet()) {  
                        sb.append(name).append("=").append(  
                                java.net.URLEncoder.encode(parameters.get(name),  
                                        "UTF-8"));  
                    }  
                    params = sb.toString();  
                } else {  
                    for (String name : parameters.keySet()) {  
                        sb.append(name).append("=").append(  
                        		java.net.URLEncoder.encode(parameters.get(name),  
                                        "UTF-8")).append("&");  
                    }  
                    String temp_params = sb.toString();  
                    params = temp_params.substring(0, temp_params.length() - 1);  
            }  
           	 return params;
    }


    /**
     * 生成要请求给支付宝的参数数组
     * @param sParaTemp 请求前的参数数组
     * @return 要请求的参数数组
     */
    private static Map<String, String> buildRequestPara(Map<String, String> sParaTemp,PayserviceDev payserviceDev) {
        //除去数组中的空值和签名参数
        Map<String, String> sPara = AlipayCore.paraFilter(sParaTemp);
        //生成签名结果
        String mysign = buildRequestMysign(sPara,payserviceDev);

        //签名结果与签名方式加入请求提交参数组中
        sPara.put("sign", mysign);
        sPara.put("sign_type", payserviceDev.getAli_sign_type());

        return sPara;
    }
    /**
     * 生成签名结果
     * @param sPara 要签名的数组
     * @return 签名结果字符串
     */
	public static String buildRequestMysign(Map<String, String> sPara,PayserviceDev payserviceDev) {
    	String prestr = AlipayCore.createLinkString(sPara); //把数组所有元素，按照“参数=参数值”的模式用“&”字符拼接成字符串
        String mysign = "";
        if(payserviceDev.getAli_sign_type().equals("MD5") ) {
        	mysign = MD5.sign(prestr,payserviceDev.getAli_key(), payserviceDev.getAli_input_charset());
        }
        return mysign;
    }
	//sign_type:MD5#payment_type:1#input_charset:utf-8#service:create_direct_pay_by_user#partner:2088801478647757
		public static Map<String, String> getPartner(String other){
			if(other==null&&"".equals(other)){
				return null;
			}else{
			String others []=other.split("#");
			Map<String, String> sParaTemp = new HashMap<String, String>();
			for (int i=0;i<others.length;i++){
				String values []=others[i].split(":");
				   sParaTemp.put(values[0], values[1]);  
			}
			
			return sParaTemp;
			}
		}

}
