package cn.com.open.openpaas.payservice.app.channel.yeepay.ehking;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.alibaba.fastjson15.JSONArray;
import com.alibaba.fastjson15.JSONObject;
import com.ehking.sdk.entity.BankCard;
import com.ehking.sdk.entity.Payer;
import com.ehking.sdk.entity.ProductDetail;
import com.ehking.sdk.exception.HmacVerifyException;
import com.ehking.sdk.exception.RequestException;
import com.ehking.sdk.exception.ResponseException;
import com.ehking.sdk.exception.UnknownException;
import com.ehking.sdk.executer.ResultListenerAdpater;
import com.ehking.sdk.onlinepay.builder.OrderBuilder;
import com.ehking.sdk.onlinepay.executer.OnlinePayOrderExecuter;

import cn.com.open.openpaas.payservice.app.channel.alipay.Channel;
import cn.com.open.openpaas.payservice.app.channel.alipay.PaymentType;
import cn.com.open.openpaas.payservice.app.channel.model.DictTradeChannel;
import cn.com.open.openpaas.payservice.app.channel.service.DictTradeChannelService;
import cn.com.open.openpaas.payservice.app.common.BaseControllerUtil;
import cn.com.open.openpaas.payservice.app.redis.service.RedisClientTemplate;
import cn.com.open.openpaas.payservice.app.redis.service.RedisConstant;
import cn.com.open.openpaas.payservice.app.tools.WebUtils;
import cn.com.open.openpaas.payservice.dev.PayserviceDev;

/**
 * 
 */
@Controller
@RequestMapping("/ehk/order/")
public class EHKOrderController extends BaseControllerUtil{
	static final Logger LOGGER = LoggerFactory.getLogger(EHKOrderController.class);
	 @Autowired
	private DictTradeChannelService dictTradeChannelService;
	 @Autowired
	 private PayserviceDev payserviceDev;
	 @Autowired
	 private RedisClientTemplate redisClient;
	 

	@RequestMapping("pay")
	public void pay(HttpServletRequest req, final HttpServletResponse resp,Model model) throws ServletException, IOException {
		 String id=req.getParameter("id");
		 String merid=req.getParameter("merid");
		 String paymentType=req.getParameter("paymentType");
		 
		 DictTradeChannel dictTradeChannels=null;
         if(!nullEmptyBlankJudge(paymentType)&&paymentType.equals(PaymentType.EHK_WEIXIN_PAY.getValue())){
        	  dictTradeChannels=dictTradeChannelService.findByMAI(merid,Channel.EHK_WEIXIN_PAY.getValue());
		 }else if(!nullEmptyBlankJudge(paymentType)&&paymentType.equals(PaymentType.EHK_ALI_PAY.getValue())){
			 dictTradeChannels=dictTradeChannelService.findByMAI(merid,Channel.EHK_ALI_PAY.getValue()); 
		 }else{
			 dictTradeChannels=dictTradeChannelService.findByMAI(merid,Channel.EHK_BANK.getValue()); 
		 }
		 Map<String, String> others =null;
   	     if(dictTradeChannels!=null){
   		   String other= dictTradeChannels.getOther();
		  	others = new HashMap<String, String>();
		  	others=getPartner(other);
		   // EHKOrderUtil.doPost(merchantOrderInfo, others, productDetails);
   	     }
//		String merchantId = req.getParameter("merchantId");
//		String requestId = id;
//		String orderAmount = req.getParameter("orderAmount");
//		String orderCurrency = req.getParameter("orderCurrency");
//		String notifyUrl = req.getParameter("notifyUrl");
//		String callbackUrl = req.getParameter("callbackUrl");
//		String paymentModeCode = req.getParameter("paymentModeCode");
//		String clientIp=req.getParameter("clientIp");
   	     //merchantId:120140078#orderCurrency:CNY#paymentModeCode:SCANCODE-WEIXIN_PAY-P2P#clientIp:127.0.0.1#timeout:10
		String merchantId = others.get("merchantId");//120140078
		final String requestId = id;
		String orderAmount =req.getParameter("payAmount");
		String orderCurrency = others.get("orderCurrency");
		String notifyUrl =dictTradeChannels.getNotifyUrl();
		String callbackUrl = dictTradeChannels.getBackurl();
		
		String paymentModeCode = others.get("paymentModeCode");
		if(!nullEmptyBlankJudge(paymentType)&&!paymentType.equals(PaymentType.EHK_WEIXIN_PAY.getValue())&&!paymentType.equals(PaymentType.EHK_BANK.getValue())&&!paymentType.equals(PaymentType.EHK_ALI_PAY.getValue())){
			paymentModeCode=paymentType;
		 }
		String clientIp=req.getParameter("clientIp");
		if(nullEmptyBlankJudge(clientIp)){
			clientIp=others.get("clientIp");
		}
		String timeout=others.get("timeout");
		//超时时间
		String jsonPDStr = req.getParameter("productDetails");
		
		OrderBuilder builder = new OrderBuilder(merchantId);
		builder.setRequestId(requestId).setOrderAmount(orderAmount).setOrderCurrency(orderCurrency)
				.setNotifyUrl(notifyUrl).setCallbackUrl(callbackUrl)
				.setPaymentModeCode(paymentModeCode).setNotifyUrl(notifyUrl);
         if(!nullEmptyBlankJudge(paymentType)&&paymentType.equals(PaymentType.EHK_WEIXIN_PAY.getValue())){
        	 builder.setClientIp(clientIp);
		 } if(!nullEmptyBlankJudge(paymentType)&&paymentType.equals(PaymentType.EHK_ALI_PAY.getValue())){
        	 builder.setClientIp(clientIp);
		 }
		builder.setTimeout(timeout);
		ProductDetail productDetail = new ProductDetail();
		
		
		LOGGER.info("productDetail++++++++++++++"+jsonPDStr);
		
		JSONArray productDetailArray = JSONObject.parseArray(jsonPDStr);
		for(Object o:productDetailArray) {
			productDetail = JSONObject.parseObject(o.toString(), ProductDetail.class);
			builder.addProductDetail(productDetail);
		}
		Payer payer = new Payer();
		payer.setName(StringUtils.defaultIfEmpty(req.getParameter("payerName"), null));
		payer.setPhoneNum(StringUtils.defaultIfEmpty(req.getParameter("payerName"), null));
		payer.setBankCardNum(StringUtils.defaultIfEmpty(req.getParameter("bankCardNum"), null));
		payer.setIdNum(StringUtils.defaultIfEmpty(req.getParameter("idNum"), null));
		payer.setIdType(StringUtils.defaultIfEmpty(req.getParameter("idType"), null));
		payer.setEmail(StringUtils.defaultIfEmpty(req.getParameter("email"), null));

		builder.setPayer(payer);
		
		BankCard bankCard = new BankCard();
		bankCard.setCardNo(StringUtils.defaultIfEmpty(req.getParameter("cardNo"), null));
		bankCard.setCvv2(StringUtils.defaultIfEmpty(req.getParameter("cvv2"), null));
		bankCard.setExpiryDate(StringUtils.defaultIfEmpty(req.getParameter("expiryDate"), null));
		bankCard.setIdNo(StringUtils.defaultIfEmpty(req.getParameter("idNo"), null));
		bankCard.setMobileNo(StringUtils.defaultIfEmpty(req.getParameter("mobileNo"), null));
		bankCard.setName(StringUtils.defaultIfEmpty(req.getParameter("name"), null));
		
		builder.setBankCard(bankCard);
		JSONObject requestData = builder.build();
		
		final PrintWriter out = resp.getWriter();
		
		OnlinePayOrderExecuter executer = new OnlinePayOrderExecuter();
		
		try{
			executer.order(requestData,new ResultListenerAdpater(){
				/**
				 * 提交成功，不代表支付成功
				 */
				public void success(JSONObject jsonObject) {
					///LOGGER.debug("success jsonObject:[" + jsonObject + "]");
					//out.println("提交成功</br>");
					//System.out.println(jsonObject.toJSONString());
					
					 Map<String, Object> map=new HashMap<String,Object>();
				    	   map.put("status", "ok");
				    	   String scanCode=jsonObject.getString("scanCode");
				    	   //scanCode= MD5.Md5(scanCode);
//				    	   scanCode = scanCode.replaceAll(new String("\r"), "");
//				    	   scanCode = scanCode.replaceAll(new String("\n"), "");
//				    	   scanCode=scanCode.replaceAll("\\+", "%2B");
				    	  // System.out.println(scanCode);
//				    	     OrderInfoDto verifyDto=new  OrderInfoDto();
//				    	      verifyDto.setOrderId(requestId);
//				    		  verifyDto.setScanCode(scanCode);
				    		  redisClient.setObjectByTime(RedisConstant.ORDER_ID+requestId, scanCode, 60*10);
				    	   map.put("urlCode",payserviceDev.getServer_host()+"ehk/order/getCode?requestId="+requestId);
				    	   writeSuccessJson(resp,map);
				}
				
				public void redirect(JSONObject jsonObject, String redirectUrl) throws IOException{
					resp.sendRedirect(redirectUrl);
					LOGGER.debug("redirectUrl:[" + redirectUrl + "]");
				}
			});
		}
		catch(ResponseException e){
			out.println("响应异常</br>");
			out.println(e.toString());
		}
		catch(HmacVerifyException e){
			out.println("签名验证异常</br>");
			out.println(e.toString());
		}
		catch(RequestException e){
			out.println("请求异常</br>");
			out.println(e.toString());
		}
		catch(UnknownException e){
			out.println("未知异常</br>");
			out.println(e.toString());
		}		
	}

	  /**
     * 跳转到wxpay页面
     */
    @RequestMapping(value = "getCode", method = RequestMethod.GET)
    public void getCode(HttpServletRequest request,HttpServletResponse response, Model model){
    	String requestId=request.getParameter("requestId");
    	//OrderInfoDto dto=(OrderInfoDto) redisClient.getObject(RedisConstant.ORDER_ID+requestId);
    	String scanCode="";
    	Map<String, String> map=new HashMap<String, String>();
    	  if(redisClient.getObject(RedisConstant.ORDER_ID+requestId)!=null){
    		  scanCode=redisClient.getObject(RedisConstant.ORDER_ID+requestId).toString(); 
    	      Base64Image.convertByteToImage(scanCode, response);
    	  }else{
    		  map.put("status","error");
    		  map.put("errorMsg","获取二维码超时,请重新下单");
    		  WebUtils.writeErrorJson(response, map);
    	  }
      return ;    	   
    }
}
