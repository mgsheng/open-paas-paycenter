package cn.com.open.openpaas.payservice.app.channel.tclpay.data;

import java.util.HashMap;
import java.util.Map;

import cn.com.open.openpaas.payservice.app.channel.tclpay.config.HytParamKeys;
import cn.com.open.openpaas.payservice.app.channel.tclpay.utils.HytDateUtils;

/**
 * 名称：获取商户对账文件数据构造类
 * 版本：1.1 
 * 日期：2015-07 
 * 作者：深圳市前海汇银通支付科技有限公司技术管理部 
 * 版权：深圳市前海汇银通支付科技有限公司
 * 说明：以下代码只是为了方便商户测试而提供的样例代码，商户可以根据自己网站的需要，按照技术文档编写,并非一定要使用该代码。该代码仅供参考。
 * */
public class StatementData {
	public static Map<String, String> buildStatementDataMap(String name) {
		String orderTime = HytDateUtils.generateOrderTime();

		Map<String, String> statementDataMap = new HashMap<String, String>();
		statementDataMap.put(HytParamKeys.CHARSET, "00");
		statementDataMap.put(HytParamKeys.VERSION, "1.0");
		statementDataMap.put(HytParamKeys.SIGN_TYPE, "RSA");
		String requestId = orderTime;
		statementDataMap.put(HytParamKeys.REQUEST_ID, requestId);
		statementDataMap.put(HytParamKeys.MERCHANT_CODE, "800075500030008");
		statementDataMap.put(HytParamKeys.AC_DATE, "20150723");
		statementDataMap.put(HytParamKeys.SERVICE, "Statement");
		return statementDataMap;
	}
}
