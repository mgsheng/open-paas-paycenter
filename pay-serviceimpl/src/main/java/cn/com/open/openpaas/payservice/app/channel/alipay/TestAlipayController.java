package cn.com.open.openpaas.payservice.app.channel.alipay;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.dom4j.DocumentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.com.open.openpaas.payservice.app.channel.model.DictTradeChannel;
import cn.com.open.openpaas.payservice.app.channel.service.DictTradeChannelService;
import cn.com.open.openpaas.payservice.app.tools.BaseControllerUtil;


/**
 * 
 */
@Controller
@RequestMapping("/alipay/")
public class TestAlipayController extends BaseControllerUtil {
	private static final Logger log = LoggerFactory.getLogger(TestAlipayController.class);
	   /**
     * 支付宝提供给商户的服务接入网关URL(新)
     */
    private static final String ALIPAY_GATEWAY_NEW = "https://mapi.alipay.com/gateway.do?";
    @Autowired
    private DictTradeChannelService dictTradeChannelService;

	/**
	 * 即时到账接口
	 * @param request
	 * @param response
	 * @throws IOException 
	 * @throws DocumentException 
	 * @throws MalformedURLException 
	 */
	@RequestMapping("dirctPay")
	public String dirctPay(HttpServletRequest request,HttpServletResponse response) throws MalformedURLException, DocumentException, IOException {
	    	String partner=request.getParameter("partner");
	    	String seller_id=request.getParameter("seller_id");
	        String key = request.getParameter("key");
	        String notify_url  = request.getParameter("notify_url");
	    	String return_url=request.getParameter("return_url");
	    	String sign_type=request.getParameter("sign_type");
	    	//String log_path=request.getParameter("log_path");
	    	String input_charset=request.getParameter("input_charset");
	    	String payment_type=request.getParameter("payment_type");
	    	String service=request.getParameter("service");
	    	String anti_phishing_key=request.getParameter("anti_phishing_key");
	    	String exter_invoke_ip=request.getParameter("exter_invoke_ip");
	    	String out_trade_no=request.getParameter("out_trade_no");
	    	String subject=request.getParameter("subjects");
	    	String total_fee=request.getParameter("total_fee");
	    	String body=request.getParameter("body");
	    	log.info("");
	    	
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
			sParaTemp.put("exter_invoke_ip",exter_invoke_ip);
			sParaTemp.put("out_trade_no", out_trade_no);
			sParaTemp.put("subject", subject);
			sParaTemp.put("total_fee", total_fee);
			sParaTemp.put("body", body);
		
			  Map<String, String> parameters = buildRequestPara(sParaTemp);
			   String result = "";// 返回的结果  
	            BufferedReader in = null;// 读取响应输入流  
	            PrintWriter out = null;  
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
	                               parameters.get(name)).append("&");  
	                    }  
	                    String temp_params = sb.toString();  
	                    params = temp_params.substring(0, temp_params.length() - 1);  
	            }  
	                
	           	 return "redirect:" + ALIPAY_GATEWAY_NEW+params;
			//sendPost(ALIPAY_GATEWAY_NEW,sParaTemp);
	         // sendGet(ALIPAY_GATEWAY_NEW,params);
	    	/*Map<String ,Object> map=new HashMap<String,Object>();
	       
	    	if(map.get("status")=="0"){
	    		writeErrorJson(response,map);
	    	}else{
	    		writeSuccessJson(response,map);
	    	}*/
	    }
	/**
	 * 返回支付宝调用地址
	 **/
	public String aliPayDirct(String marchantId,String out_trade_no,String subject,String total_fee,String body) throws MalformedURLException, DocumentException, IOException {
		DictTradeChannel dictTradeChannels=dictTradeChannelService.findByMAI(marchantId,1);
		String other= dictTradeChannels.getOther();
		Map<String, String> others = new HashMap<String, String>();
		others=getPartner(other);
    	String partner=others.get("partner");
    	String seller_id=others.get("partner"); 
        String notify_url =dictTradeChannels.getNotifyUrl();
    	String return_url=dictTradeChannels.getBackurl();
    	//String log_path=request.getParameter("log_path");
    	String input_charset=dictTradeChannels.getInputCharset();
    	String payment_type=dictTradeChannels.getPaymentType();
    	String service=AlipayConfig.service;
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
		sParaTemp.put("subject", subject);
		sParaTemp.put("total_fee", "0.01");
		sParaTemp.put("body", body);
		 Map<String, String> parameters = buildRequestPara(sParaTemp);
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
                               parameters.get(name)).append("&");  
                    }  
                    String temp_params = sb.toString();  
                    params = temp_params.substring(0, temp_params.length() - 1);  
            }  
           	 return "redirect:" + ALIPAY_GATEWAY_NEW+params;
    }
	//sign_type:MD5#payment_type:1#input_charset:utf-8#service:create_direct_pay_by_user#partner:2088801478647757
	public Map<String, String> getPartner1(String other){
		if(other==null&&"".equals(other)){
			return null;
		}else{
		String others []=other.split("#");
		Map<String, String> sParaTemp = new HashMap<String, String>();
		String partner="";
		String values="";
		for (int i=0;i<others.length;i++){
		   values=others[i];
		   int j=values.indexOf(":");
		   if(values.substring(0, j).equals("partner")){
			   partner=values.substring(j+1,values.length());  
		   }
		}
		sParaTemp.put("partner", partner);
		return sParaTemp;
		}
	}
    /**
     * 建立请求，以表单HTML形式构造（默认）
     * @param sParaTemp 请求参数数组
     * @param strMethod 提交方式。两个值可选：post、get
     * @param strButtonName 确认按钮显示文字
     * @return 提交表单HTML文本
     */
    public static String buildRequest(Map<String, String> sParaTemp, String strMethod, String strButtonName) {
        //待请求参数数组
        Map<String, String> sPara = buildRequestPara(sParaTemp);
        List<String> keys = new ArrayList<String>(sPara.keySet());

        StringBuffer sbHtml = new StringBuffer();

        sbHtml.append("<form id=\"alipaysubmit\" name=\"alipaysubmit\" action=\"" + ALIPAY_GATEWAY_NEW
                      + "_input_charset=" + AlipayConfig.input_charset + "\" method=\"" + strMethod
                      + "\">");

        for (int i = 0; i < keys.size(); i++) {
            String name = (String) keys.get(i);
            String value = (String) sPara.get(name);

            sbHtml.append("<input type=\"hidden\" name=\"" + name + "\" value=\"" + value + "\"/>");
        }

        //submit按钮控件请不要含有name属性
        sbHtml.append("<input type=\"submit\" value=\"" + strButtonName + "\" style=\"display:none;\"></form>");
        sbHtml.append("<script>document.forms['alipaysubmit'].submit();</script>");
        return sbHtml.toString();
    }
    /**
     * 生成要请求给支付宝的参数数组
     * @param sParaTemp 请求前的参数数组
     * @return 要请求的参数数组
     */
    private static Map<String, String> buildRequestPara(Map<String, String> sParaTemp) {
        //除去数组中的空值和签名参数
        Map<String, String> sPara = AlipayCore.paraFilter(sParaTemp);
        //生成签名结果
        String mysign = buildRequestMysign(sPara);

        //签名结果与签名方式加入请求提交参数组中
        sPara.put("sign", mysign);
        sPara.put("sign_type", AlipayConfig.sign_type);

        return sPara;
    }
    /**
     * 生成签名结果
     * @param sPara 要签名的数组
     * @return 签名结果字符串
     */
	public static String buildRequestMysign(Map<String, String> sPara) {
    	String prestr = AlipayCore.createLinkString(sPara); //把数组所有元素，按照“参数=参数值”的模式用“&”字符拼接成字符串
        String mysign = "";
        if(AlipayConfig.sign_type.equals("MD5") ) {
        	mysign = MD5.sign(prestr, AlipayConfig.key, AlipayConfig.input_charset);
        }
        return mysign;
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
        public static String sendPost(String url, Map<String, String> sPara) {  
            String result = "";// 返回的结果  
            BufferedReader in = null;// 读取响应输入流  
            PrintWriter out = null;  
            StringBuffer sb = new StringBuffer();// 处理请求参数  
            String params = "";// 编码之后的参数  
            Map<String, String> parameters = buildRequestPara(sPara);
            try {  
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
                               parameters.get(name)).append("&");  
                    }  
                    String temp_params = sb.toString();  
                    params = temp_params.substring(0, temp_params.length() - 1);  
                }  
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
                        .getInputStream(), "gb2312"));  
                String line;  
                // 读取返回的内容  
                while ((line = in.readLine()) != null) {  
                    result += line;  
                    
                }  
                System.out.println(result);
            } catch (Exception e) {  
                e.printStackTrace();  
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
        
        /**
         * 向指定URL发送GET方法的请求
         * 
         * @param url
         *            发送请求的URL
         * @param param
         *            请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
         * @return URL 所代表远程资源的响应结果
         */
        public static String sendGet(String url, String param) {
            String result = "";
            BufferedReader in = null;
            try {
                String urlNameString = url  + param;
                URL realUrl = new URL(urlNameString);
                // 打开和URL之间的连接
                URLConnection connection = realUrl.openConnection(); 
                // 设置通用的请求属性
                connection.setRequestProperty("accept", "*/*");
                connection.setRequestProperty("connection", "Keep-Alive");
                connection.setRequestProperty("user-agent",
                        "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
                // 建立实际的连接
                connection.connect();
                // 获取所有响应头字段
                Map<String, List<String>> map = connection.getHeaderFields();
                // 遍历所有的响应头字段
                for (String key : map.keySet()) {
                    System.out.println(key + "--->" + map.get(key));
                }
                // 定义 BufferedReader输入流来读取URL的响应
                in = new BufferedReader(new InputStreamReader(
                        connection.getInputStream()));
                String line;
                while ((line = in.readLine()) != null) {
                   result += line;
                }
                System.out.println(result);
            } catch (Exception e) {
                System.out.println("发送GET请求出现异常！" + e);
                e.printStackTrace();
            }
            // 使用finally块来关闭输入流
            finally {
                try {
                    if (in != null) {
                        in.close();
                    }
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
            }
            return result;
        }
 
}