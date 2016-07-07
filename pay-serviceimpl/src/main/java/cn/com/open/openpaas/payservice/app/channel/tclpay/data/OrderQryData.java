package cn.com.open.openpaas.payservice.app.channel.tclpay.data;

import java.util.HashMap;
import java.util.Map;

import cn.com.open.openpaas.payservice.app.channel.tclpay.config.HytParamKeys;
import cn.com.open.openpaas.payservice.app.channel.tclpay.utils.HytDateUtils;

/**
 * 名称：订单查询数据构造类
 * 版本：1.1 
 * 日期：2015-07 
 * 作者：深圳市前海汇银通支付科技有限公司技术管理部 
 * 版权：深圳市前海汇银通支付科技有限公司
 * 说明：以下代码只是为了方便商户测试而提供的样例代码，商户可以根据自己网站的需要，按照技术文档编写,并非一定要使用该代码。该代码仅供参考。
 * */
public class OrderQryData {
	public static Map<String, String> buildOrderQryDataMap(String name) {
		String orderTime = HytDateUtils.generateOrderTime();

		Map<String, String> orderQryDataMap = new HashMap<String, String>();
		orderQryDataMap.put(HytParamKeys.CHARSET, "00");
		orderQryDataMap.put(HytParamKeys.VERSION, "1.0");
		orderQryDataMap.put(HytParamKeys.SIGN_TYPE, "RSA");
		String requestId = orderTime;
		orderQryDataMap.put(HytParamKeys.REQUEST_ID, requestId);
		orderQryDataMap.put(HytParamKeys.MERCHANT_CODE, "800075500030008");
		orderQryDataMap.put(HytParamKeys.OUT_TRADE_NO, "2015025110244776");
		orderQryDataMap.put(HytParamKeys.ORDER_TIME, orderTime);
		orderQryDataMap.put(HytParamKeys.SERVICE, "OrderSearch");
		return orderQryDataMap;
	}
}
