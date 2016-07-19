package cn.com.open.openpaas.payservice.app.channel.tclpay.data;

import java.util.HashMap;
import java.util.Map;

import cn.com.open.openpaas.payservice.app.channel.model.DictTradeChannel;
import cn.com.open.openpaas.payservice.app.channel.tclpay.config.HytConstants;
import cn.com.open.openpaas.payservice.app.channel.tclpay.config.HytParamKeys;
import cn.com.open.openpaas.payservice.app.channel.tclpay.sign.RSASign;
import cn.com.open.openpaas.payservice.app.channel.tclpay.utils.HytDateUtils;
import cn.com.open.openpaas.payservice.app.channel.tclpay.utils.HytPacketUtils;
import cn.com.open.openpaas.payservice.app.channel.tclpay.utils.HytUtils;
import cn.com.open.openpaas.payservice.app.order.model.MerchantOrderInfo;

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
	
	public static Map<String, String> buildGetOrderQryDataMap(MerchantOrderInfo merchantOrderInfo,DictTradeChannel dictTradeChannel) {
		String orderTime = HytDateUtils.generateOrderTime();
		Map<String, String> orderQryDataMap = new HashMap<String, String>();
		String other= dictTradeChannel.getOther();
		Map<String, String> others = new HashMap<String, String>();
		others=getPartner(other);
		String merchant_code=others.get("merchant_code");
		orderQryDataMap.put(HytParamKeys.CHARSET, "00");
		orderQryDataMap.put(HytParamKeys.VERSION, merchantOrderInfo.getMerchantVersion());
		orderQryDataMap.put(HytParamKeys.SIGN_TYPE, "RSA");
		orderQryDataMap.put(HytParamKeys.SERVICE, "OrderSearch");
		orderQryDataMap.put(HytParamKeys.REQUEST_ID, merchantOrderInfo.getPayOrderId());
		orderQryDataMap.put(HytParamKeys.MERCHANT_CODE, merchant_code);
		orderQryDataMap.put(HytParamKeys.ORDER_TIME, orderTime);
		orderQryDataMap.put(HytParamKeys.OUT_TRADE_NO, merchantOrderInfo.getMerchantOrderId());
		
		RSASign util = HytUtils.getRSASignObject();
		String reqData = HytPacketUtils.map2Str(orderQryDataMap);
		String merchant_sign = "";
			try {
				merchant_sign = util.sign(reqData, HytConstants.CHARSET_GBK);
			} catch (Exception e) {
				e.printStackTrace();
			}
		String merchant_cert = util.getCertInfo();
		orderQryDataMap.put(HytParamKeys.MERCHANT_CERT, merchant_cert);
		orderQryDataMap.put(HytParamKeys.MERCHANT_SIGN, merchant_sign);
		return orderQryDataMap;
	}
	
	

	public static Map<String, String> buildGetFileDownloadMap(
			MerchantOrderInfo orderInfo, DictTradeChannel dictTradeChannels) {
			Map<String,String> sParaTemp2 = new HashMap<String,String>();
			String other= dictTradeChannels.getOther();
			Map<String, String> others = new HashMap<String, String>();
			others=getPartner(other);
			String merchant_code=others.get("merchant_code");
			String acDate = HytDateUtils.getCurDateStr();
			sParaTemp2.put(HytParamKeys.CHARSET,"00");
			sParaTemp2.put(HytParamKeys.VERSION, "1.0");
			sParaTemp2.put(HytParamKeys.SIGN_TYPE,"RSA");
			sParaTemp2.put(HytParamKeys.SERVICE, "Statement");
			sParaTemp2.put(HytParamKeys.REQUEST_ID,  orderInfo.getPayOrderId());
			sParaTemp2.put(HytParamKeys.MERCHANT_CODE, merchant_code);
			sParaTemp2.put(HytParamKeys.AC_DATE, acDate);
			RSASign util = HytUtils.getRSASignObject();
		 String reqData = HytPacketUtils.map2Str(sParaTemp2);
		 String merchant_sign = "";
			try {
				merchant_sign = util.sign(reqData, HytConstants.CHARSET_GBK);
			} catch (Exception e) {
				e.printStackTrace();
			}
			String merchant_cert = util.getCertInfo();
			sParaTemp2.put(HytParamKeys.MERCHANT_CERT, merchant_cert);
			sParaTemp2.put(HytParamKeys.MERCHANT_SIGN, merchant_sign);
		return sParaTemp2;
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
