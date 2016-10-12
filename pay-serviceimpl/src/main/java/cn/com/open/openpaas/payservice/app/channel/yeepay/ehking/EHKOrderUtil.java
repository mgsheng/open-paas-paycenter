package cn.com.open.openpaas.payservice.app.channel.yeepay.ehking;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.com.open.openpaas.payservice.app.order.model.MerchantOrderInfo;

import com.alibaba.fastjson15.JSONArray;
import com.alibaba.fastjson15.JSONObject;
import com.ehking.sdk.entity.BankCard;
import com.ehking.sdk.entity.CustomsInfo;
import com.ehking.sdk.entity.Payer;
import com.ehking.sdk.entity.ProductDetail;
import com.ehking.sdk.exception.HmacVerifyException;
import com.ehking.sdk.exception.RequestException;
import com.ehking.sdk.exception.ResponseException;
import com.ehking.sdk.exception.UnknownException;
import com.ehking.sdk.executer.ResultListenerAdpater;
import com.ehking.sdk.onlinepay.builder.OrderBuilder;
import com.ehking.sdk.onlinepay.executer.OnlinePayOrderExecuter;

public class EHKOrderUtil  {
	static final Logger LOGGER = LoggerFactory.getLogger(EHKOrderUtil.class);
       /**
        * 
        * @param merchantOrderInfo
        * @param others
        * @param productDetails
        * @return
        * @throws ServletException
        * @throws IOException
        */
	   public static String orderPay(MerchantOrderInfo merchantOrderInfo,Map<String, String> others,String productDetails) throws ServletException, IOException {
		
		String returnValue="";
		String merchantId = others.get("merchantId");
		String requestId = merchantOrderInfo.getId();
		String orderAmount =String.valueOf(merchantOrderInfo.getPayAmount());
		String orderCurrency = others.get("orderCurrency");
		String notifyUrl = others.get("notifyUrl");
		String callbackUrl = others.get("callbackUrl");
		String paymentModeCode = others.get("paymentModeCode");
		String clientIp=others.get("clientIp");
		String timeout=others.get("timeout");

		OrderBuilder builder = new OrderBuilder(merchantId);
		builder.setRequestId(requestId).setOrderAmount(orderAmount).setOrderCurrency(orderCurrency)
				.setNotifyUrl(notifyUrl).setCallbackUrl(callbackUrl)
				.setPaymentModeCode(paymentModeCode).setNotifyUrl(notifyUrl)
				.setClientIp(clientIp);
		builder.setTimeout(timeout);

		ProductDetail productDetail = new ProductDetail();
		String jsonPDStr = productDetails;
		
		LOGGER.info("productDetail++++++++++++++"+jsonPDStr);
		
		JSONArray productDetailArray = JSONObject.parseArray(jsonPDStr);
		for(Object o:productDetailArray) {
			productDetail = JSONObject.parseObject(o.toString(), ProductDetail.class);
			builder.addProductDetail(productDetail);
		}
		
		JSONObject requestData = builder.build();
		
		
		OnlinePayOrderExecuter executer = new OnlinePayOrderExecuter();
		
		try{
			executer.order(requestData,new ResultListenerAdpater(){
				/**
				 * 提交成功，不代表支付成功
				 */
				public void success(JSONObject jsonObject) {
					LOGGER.debug("success jsonObject:[" + jsonObject + "]");
					return;
					//out.println("提交成功</br>");
					//out.println(jsonObject);
				}
				public void redirect(JSONObject jsonObject, String redirectUrl) throws IOException{
					//resp.sendRedirect(redirectUrl);
					LOGGER.debug("redirectUrl:[" + redirectUrl + "]");
				}
			});
		}
		catch(ResponseException e){
			//out.println("响应异常</br>");
			//out.println(e.toString());
		}
		catch(HmacVerifyException e){
			//out.println("签名验证异常</br>");
			//out.println(e.toString());
		}
		catch(RequestException e){
			//out.println("请求异常</br>");
			//out.println(e.toString());
		}
		catch(UnknownException e){
			//out.println("未知异常</br>");
			//out.println(e.toString());
		}
		return "";
	}
}
