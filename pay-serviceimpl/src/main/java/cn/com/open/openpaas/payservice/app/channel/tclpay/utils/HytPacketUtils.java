package cn.com.open.openpaas.payservice.app.channel.tclpay.utils;

import java.io.UnsupportedEncodingException;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import cn.com.open.openpaas.payservice.app.channel.tclpay.config.HytConstants;

/**
 * 名称：Hyt报文工具换
 * 版本：1.1 
 * 日期：2015-07 
 * 作者：深圳市前海汇银通支付科技有限公司技术管理部 
 * 版权：深圳市前海汇银通支付科技有限公司
 * 说明：以下代码只是为了方便商户测试而提供的样例代码，商户可以根据自己网站的需要，按照技术文档编写,并非一定要使用该代码。该代码仅供参考。
 * */

public class HytPacketUtils {

	@SuppressWarnings("rawtypes")
	public static String map2Str(Map<String, String> dataMap) {
		StringBuffer strBuf = new StringBuffer();
		SortedMap<String, String> sortedDataMap = new TreeMap<String, String>(
				dataMap);
		Set<Entry<String, String>> es = sortedDataMap.entrySet();
		Iterator<Entry<String, String>> it = es.iterator();
		while (it.hasNext()) {
			Map.Entry entry = (Map.Entry) it.next();
			String key = (String) entry.getKey();
			String value = (String) entry.getValue();
			if (HytStringUtils.isEmpty(value)) {
				continue;
			} else {
						strBuf.append(key + HytConstants.SYMBOL_EQUAL +value+HytConstants.SYMBOL_AND);
			}
		}
		return strBuf.substring(0, strBuf.length() - 1);
	}

}
