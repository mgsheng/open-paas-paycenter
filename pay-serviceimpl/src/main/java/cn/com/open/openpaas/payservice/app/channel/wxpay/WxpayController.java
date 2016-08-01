package cn.com.open.openpaas.payservice.app.channel.wxpay;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.com.open.openpaas.payservice.app.channel.alipay.MD5;
import cn.com.open.openpaas.payservice.app.tools.BaseControllerUtil;
import cn.com.open.openpaas.payservice.dev.PayserviceDev;
public class WxpayController extends BaseControllerUtil {
	private static final Logger log = LoggerFactory.getLogger(WxpayController.class);

	/**
	 * 获取签名
	 * @param payInfo
	 * @return
	 * @throws Exception
	 */
	 public static String getSign(WxpayInfo payInfo) throws Exception {
	  String signTemp = "appid="+payInfo.getAppid()
	   +"&attach="+payInfo.getAttach()
	   +"&body="+payInfo.getBody()
	   +"&device_info="+payInfo.getDevice_info()
	   +"&mch_id="+payInfo.getMch_id()
	   +"&nonce_str="+payInfo.getNonce_str()
	   +"¬ify_url="+payInfo.getNotify_url()
	   +"&openid="+payInfo.getOpenid()
	   +"&out_trade_no="+payInfo.getOut_trade_no()
	   +"&spbill_create_ip="+payInfo.getSpbill_create_ip()
	   +"&total_fee="+payInfo.getTotal_fee()
	   +"&trade_type="+payInfo.getTrade_type()
	   +"&key="+WxConfigure.key; //这个key注意
	   String sign = MD5.Md5(signTemp).toUpperCase();
	   return sign;
	 }
	 public static String weixin_pay(WxpayInfo payInfo,PayserviceDev payserviceDev) throws Exception {
			// 账号信息
	        String appid =payInfo.getAppid();  // appid
	        //String appsecret = PayConfigUtil.APP_SECRET; // appsecret
	        String mch_id = payInfo.getMch_id(); // 商业号
	        String key =payInfo.getWx_key(); // key

	        String currTime = WxPayCommonUtil.getCurrTime();
	        String strTime = currTime.substring(8, currTime.length());
	        String strRandom = WxPayCommonUtil.buildRandom(4) + "";
	        String nonce_str = strTime + strRandom;
	        
	        String order_price = String.valueOf(payInfo.getTotal_fee()); // 价格   注意：价格的单位是分
	        String body = payInfo.getBody();   // 商品名称
	        String out_trade_no = payInfo.getOut_trade_no(); // 订单号
	        // 获取发起电脑 ip
	        String spbill_create_ip = payInfo.getSpbill_create_ip();
	        // 回调接口 
	        String notify_url = payInfo.getNotify_url();
	        String trade_type = "NATIVE";
	        SortedMap<Object,Object> packageParams = new TreeMap<Object,Object>();
	        packageParams.put("appid", appid);
	        packageParams.put("mch_id", mch_id);
	        packageParams.put("nonce_str", nonce_str);
	        packageParams.put("body", body);
	        packageParams.put("out_trade_no", out_trade_no);
	        packageParams.put("total_fee", order_price);
	        packageParams.put("spbill_create_ip", spbill_create_ip);
	        packageParams.put("notify_url", notify_url);
	        packageParams.put("trade_type", trade_type);
	        String sign = WxPayCommonUtil.createSign("UTF-8", packageParams,key);
	        packageParams.put("sign", sign);
	        String requestXML = WxPayCommonUtil.getRequestXml(packageParams);
	       // System.out.println(requestXML);
	        String resXml = WxHttpUtil.postData(payserviceDev.getWx_ufdooer_url(), requestXML);
	        Map map = WxXMLUtil.doXMLParse(resXml);
	        //String return_code = (String) map.get("return_code");
	        //String prepay_id = (String) map.get("prepay_id");
	        String urlCode = (String) map.get("code_url");
	        System.out.println(urlCode);
	         log.info("urlCode:"+urlCode);
             return urlCode;
			
	}

}
