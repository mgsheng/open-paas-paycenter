package cn.com.open.openpaas.payservice.app.channel.tclpay.data;

import java.util.HashMap;
import java.util.Map;

import cn.com.open.openpaas.payservice.app.channel.tclpay.config.HytParamKeys;
import cn.com.open.openpaas.payservice.app.channel.tclpay.utils.HytDateUtils;


/**
 * 名称：扫码SDK支付请求数据构造类
 * 版本：1.1 
 * 日期：2015-07 
 * 作者：深圳市前海汇银通支付科技有限公司技术管理部 
 * 版权：深圳市前海汇银通支付科技有限公司
 * 说明：以下代码只是为了方便商户测试而提供的样例代码，商户可以根据自己网站的需要，按照技术文档编写,并非一定要使用该代码。该代码仅供参考。
 * */
public class SdkOrderData {

	public static Map<String, String> buildOrderDataMap(String name) {

		String orderTime = HytDateUtils.generateOrderTime();

		Map<String, String> orderDataMap = new HashMap<String, String>();
		orderDataMap.put(HytParamKeys.CHARSET, "00");
		orderDataMap.put(HytParamKeys.VERSION, "1.0");
		orderDataMap.put(HytParamKeys.SIGN_TYPE, "RSA");
		String requestId = orderTime;
		orderDataMap.put(HytParamKeys.REQUEST_ID, requestId);
		orderDataMap.put(HytParamKeys.MERCHANT_CODE, "800075500030008");
		orderDataMap.put(HytParamKeys.APP_NO, "");
		orderDataMap.put(HytParamKeys.MERCHANT_NAME, "����+");
		String outTradeNo = orderTime;
		orderDataMap.put(HytParamKeys.OUT_TRADE_NO, outTradeNo);
		orderDataMap.put(HytParamKeys.SERVICE, "SdkPayment");
		orderDataMap.put(HytParamKeys.CHANNEL_CODE, "UPOP");
		orderDataMap.put(HytParamKeys.CHANNEL_PRODUCT_CODE, "");
		orderDataMap.put(HytParamKeys.PRODUCT_NAME, name + "�ӿ���֤��Ʒ+");
		orderDataMap.put(HytParamKeys.SHOW_URL,
				"http://www.test.com/test/showproduct.jsp");
		orderDataMap.put(HytParamKeys.PRODUCT_ID, orderTime);
		orderDataMap.put(HytParamKeys.PRODUCT_DESC, "�ӿ���֤��Ʒ+");
		orderDataMap.put(HytParamKeys.ATTACH, "1010");
		orderDataMap.put(HytParamKeys.RETURN_URL,
				"http://www.test.com/test/notify.jsp");
		orderDataMap.put(HytParamKeys.NOTIFY_URL,
				"http://www.test.com/test/notify.jsp");
		orderDataMap.put(HytParamKeys.SPBILL_CREATE_IP, "127.0.0.1");
		orderDataMap.put(HytParamKeys.BUYER_ID, "156XXXXXX32");

		orderDataMap.put(HytParamKeys.ORDER_TIME, orderTime);
		orderDataMap.put(HytParamKeys.TOTAL_AMOUNT,
				"0".equals(orderTime.substring(orderTime.length() - 1)) ? "1"
						: orderTime.substring(orderTime.length() - 1));
		orderDataMap.put(HytParamKeys.CURRENCY, "CNY");
		orderDataMap.put(HytParamKeys.TIME_EXPIRE, "10080");

		orderDataMap.put(HytParamKeys.OUT_MSG_TYPE, "XML");
		return orderDataMap;
	}
	

	
}
