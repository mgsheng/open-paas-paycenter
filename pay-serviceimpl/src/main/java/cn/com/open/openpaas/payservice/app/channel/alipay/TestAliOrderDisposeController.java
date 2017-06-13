package cn.com.open.openpaas.payservice.app.channel.alipay;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.commons.httpclient.util.DateUtil;
import org.dom4j.DocumentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.com.open.openpaas.payservice.app.merchant.model.MerchantInfo;
import cn.com.open.openpaas.payservice.app.order.model.MerchantOrderInfo;
import cn.com.open.openpaas.payservice.app.order.service.MerchantOrderInfoService;
import cn.com.open.openpaas.payservice.app.tools.BaseControllerUtil;
import cn.com.open.openpaas.payservice.app.tools.DateTools;
import cn.com.open.openpaas.payservice.app.tools.WebUtils;


/**
 * 
 */
@Controller
@RequestMapping("/alipay/order/")
public class TestAliOrderDisposeController extends BaseControllerUtil {
	private static final Logger log = LoggerFactory.getLogger(TestAliOrderDisposeController.class);
	 @Autowired
	 private MerchantOrderInfoService merchantOrderInfoService;
	/**
	 * 支付宝订单回调接口
	 * @param request
	 * @param response
	 * @throws IOException 
	 * @throws DocumentException 
	 * @throws MalformedURLException 
	 */
	@RequestMapping("dispose")
	public void AlidirctPay(HttpServletRequest request,HttpServletResponse response,String outTradeNo,Double payAmount,Double payCharge) throws MalformedURLException, DocumentException, IOException {
		String backMsg=request.getParameter("backMsg");
	/*	MerchantOrderInfo merchantOrderInfo=merchantOrderInfoService.findByMerchantOrderId(outTradeNo);
		merchantOrderInfo.setPayStatus(1.0);
		merchantOrderInfo.setPayAmount(payAmount);
		merchantOrderInfo.setPayCharge(payCharge);
		merchantOrderInfoService.saveMerchantOrderInfo(merchantOrderInfo);
		
		//拼接发送的加密信息
		Map<String, String> sParaTemp = new HashMap<String, String>();
		sParaTemp.put("orderId", merchantOrderInfo.getId());
        sParaTemp.put("outTradeNo", merchantOrderInfo.getChannelOrderId());
        sParaTemp.put("merchantId", String.valueOf(merchantOrderInfo.getMerchantId()));
        sParaTemp.put("paymentType", String.valueOf(merchantOrderInfo.getPaymentId()));
		sParaTemp.put("paymentChannel", String.valueOf(merchantOrderInfo.getChannelId()));
		sParaTemp.put("feeType", "");
		sParaTemp.put("guid", merchantOrderInfo.getGuid());
		sParaTemp.put("appUid",String.valueOf(merchantOrderInfo.getSourceUid()));
		//sParaTemp.put("exter_invoke_ip",exter_invoke_ip);
		sParaTemp.put("timeEnd", DateTools.dateToString(new Date(), "yyyyMMddHHmmss"));
		sParaTemp.put("totalFee", String.valueOf(merchantOrderInfo.getAmount()));
		sParaTemp.put("goodsId", "");
		sParaTemp.put("goodsName", "");
		sParaTemp.put("goodsDesc", "");
		sParaTemp.put("parameter", "");
		 Map<String, String> parameters = buildRequestPara(sParaTemp);
		 sendPost(merchantOrderInfo.getNotifyUrl(),parameters);*/
		//System.out.println("订单处理:"+backMsg);
	 }
	/**
	 * 订单结果处理
	 * @param request
	 * @param response
	 * @throws IOException 
	 * @throws DocumentException 
	 * @throws MalformedURLException 
	 */
	public Map<String, Object>  AlidirctPay(String outTradeNo,Double payAmount,Double payCharge,String goodsId,String goodsName,String goodsDesc) throws MalformedURLException, DocumentException, IOException {
		MerchantOrderInfo merchantOrderInfo=merchantOrderInfoService.findByMerchantOrderId(outTradeNo);
		merchantOrderInfo.setPayStatus(1);
		merchantOrderInfo.setPayAmount(payAmount);
		merchantOrderInfo.setPayCharge(payCharge);
		merchantOrderInfoService.updateOrder(merchantOrderInfo);
		//拼接发送的加密信息
		Map<String, String> sParaTemp = new HashMap<String, String>();
		sParaTemp.put("orderId", merchantOrderInfo.getId());
        sParaTemp.put("outTradeNo", merchantOrderInfo.getChannelOrderId());
        sParaTemp.put("merchantId", String.valueOf(merchantOrderInfo.getMerchantId()));
        sParaTemp.put("paymentType", String.valueOf(merchantOrderInfo.getPaymentId()));
		sParaTemp.put("paymentChannel", String.valueOf(merchantOrderInfo.getChannelId()));
		sParaTemp.put("feeType", "");
		sParaTemp.put("guid", merchantOrderInfo.getGuid());
		sParaTemp.put("appUid",String.valueOf(merchantOrderInfo.getSourceUid()));
		//sParaTemp.put("exter_invoke_ip",exter_invoke_ip);
		sParaTemp.put("timeEnd", DateTools.dateToString(new Date(), "yyyyMMddHHmmss"));
		sParaTemp.put("totalFee", String.valueOf(merchantOrderInfo.getAmount()));
		sParaTemp.put("goodsId", goodsId);
		sParaTemp.put("goodsName", goodsName);
		sParaTemp.put("goodsDesc", goodsDesc);
		sParaTemp.put("parameter", "");
		
		 Map<String, String> parameters = buildRequestPara(sParaTemp);
		 //sendPost(merchantOrderInfo.getNotifyUrl(),parameters);
			Map<String, Object> map = new LinkedHashMap<String, Object>();
			String returnValue= sendPost(merchantOrderInfo.getNotifyUrl(),parameters);
			 if(nullEmptyBlankJudge(returnValue)){
				 map.put("state", "error");
				 map.put("errorCode", "1");
				 map.put("errorMsg", "返回值为空");
				}else{
				  JSONObject reqjson = JSONObject.fromObject(returnValue);
				  String state = reqjson.getString("state");
				  String errorCode=reqjson.getString("errorCode");
				  String errorMsg=reqjson.getString("errorMsg");
					if(!state.equals("ok")){
						 map.put("state", "ok");
						
					}else{
						map.put("state", "error");
					}
					 map.put("errorCode", errorCode);
					 map.put("errorMsg", errorMsg);
		        }
			 return map;
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
        sPara.put("secret", mysign);
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
            //System.out.println(result);
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
}