package cn.com.open.pay.order.service.alipay;

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

import cn.com.open.pay.order.service.dev.PayserviceDev;


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
