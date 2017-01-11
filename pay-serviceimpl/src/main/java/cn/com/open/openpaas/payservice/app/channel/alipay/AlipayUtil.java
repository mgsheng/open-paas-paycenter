package cn.com.open.openpaas.payservice.app.channel.alipay;

import cn.com.open.openpaas.payservice.app.order.model.MerchantOrderInfo;

import com.alipay.api.response.AlipayTradePrecreateResponse;
import com.alipay.demo.trade.config.Configs;
import com.alipay.demo.trade.model.builder.AlipayTradePrecreateRequestBuilder;
import com.alipay.demo.trade.model.result.AlipayF2FPrecreateResult;
import com.alipay.demo.trade.service.AlipayMonitorService;
import com.alipay.demo.trade.service.AlipayTradeService;
import com.alipay.demo.trade.service.impl.AlipayMonitorServiceImpl;
import com.alipay.demo.trade.service.impl.AlipayTradeServiceImpl;
import com.alipay.demo.trade.service.impl.AlipayTradeWithHBServiceImpl;

public class AlipayUtil {
	private static AlipayTradeService tradeService;

	// 支付宝当面付2.0服务（集成了交易保障接口逻辑）
	private static AlipayTradeService tradeWithHBService;

	// 支付宝交易保障接口服务
	private static AlipayMonitorService monitorService;

	private AlipayUtil(String fileName) {
		Configs.init(fileName);
		tradeService = new AlipayTradeServiceImpl.ClientBuilder().build();

		// 支付宝当面付2.0服务（集成了交易保障接口逻辑）
		tradeWithHBService = new AlipayTradeWithHBServiceImpl.ClientBuilder()
				.build();

		monitorService = new AlipayMonitorServiceImpl.ClientBuilder()
				.setCharset("GBK").setFormat("json").build();
	}

	protected static AlipayUtil getAlipayUtil(String fileName) {
		AlipayUtil util = new AlipayUtil(fileName);
		return util;
	}

	public String trade_precreate(MerchantOrderInfo merchantOrderInfo) {
		String aliCode = "";
		tradeService = new AlipayTradeServiceImpl.ClientBuilder().build();
		AlipayTradePrecreateRequestBuilder builder = new AlipayTradePrecreateRequestBuilder()
				.setSubject(merchantOrderInfo.getMerchantProductName())
				.setTotalAmount(
						String.valueOf(merchantOrderInfo.getOrderAmount()))
				.setOutTradeNo(merchantOrderInfo.getId())
				// .setSellerId(others.get("sellerId"))
				.setBody(merchantOrderInfo.getMerchantProductDesc());
		// .setStoreId(others.get("store_id"))
		// .setNotifyUrl(notifyUrl)
		// .setExtendParams(extendParams)
		// .setTimeoutExpress(others.get("timeExpress"));

		// .setGoodsDetailList(goodsDetailList);

		AlipayF2FPrecreateResult result = tradeService.tradePrecreate(builder);
		switch (result.getTradeStatus()) {
		case SUCCESS:
			AlipayTradePrecreateResponse response = result.getResponse();
			// dumpResponse(response);
			aliCode = response.getQrCode();
			break;

		case FAILED:
			aliCode = "";
			break;

		case UNKNOWN:
			aliCode = "";
			break;

		default:
			aliCode = "";
			break;
		}
		return aliCode;
	}

}
