package cn.com.open.openpaas.payservice.app.channel.alipay;

import java.util.HashMap;
import java.util.Map;

import cn.com.open.openpaas.payservice.app.channel.model.DictTradeChannel;
import cn.com.open.openpaas.payservice.app.channel.service.DictTradeChannelService;
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

	public String trade_precreate(MerchantOrderInfo merchantOrderInfo ,DictTradeChannelService dictTradeChannelService) {
		String aliCode = "";
		tradeService = new AlipayTradeServiceImpl.ClientBuilder().build();
		if(merchantOrderInfo!=null){
   		 DictTradeChannel dictTradeChannels=dictTradeChannelService.findByMAI(String.valueOf(merchantOrderInfo.getMerchantId()),Channel.ALIFAF.getValue());
   		   if(dictTradeChannels!=null){
   			   String other= dictTradeChannels.getOther();
   			   String notifyUrl=dictTradeChannels.getNotifyUrl();
       			Map<String, String> others = new HashMap<String, String>();
       			others=getPartner(other); 
       			 AlipayTradePrecreateRequestBuilder builder = new AlipayTradePrecreateRequestBuilder()
      	                .setSubject(merchantOrderInfo.getMerchantProductName())
      	                .setTotalAmount(String.valueOf(merchantOrderInfo.getOrderAmount()))
      	                .setOutTradeNo(merchantOrderInfo.getId())
      	                .setSellerId(others.get("sellerId"))
      	                .setBody(merchantOrderInfo.getMerchantProductDesc())
      	                .setStoreId(others.get("store_id"))
      	                .setNotifyUrl(notifyUrl)
      	              //  .setExtendParams(extendParams)
      	                .setTimeoutExpress(others.get("timeExpress"));
      	       
      	                //.setGoodsDetailList(goodsDetailList);

      	        AlipayF2FPrecreateResult result = tradeService.tradePrecreate(builder);
      	        switch (result.getTradeStatus()) {
      	            case SUCCESS:
      	            	System.out.println("支付宝预下单成功: )");
      	                AlipayTradePrecreateResponse response = result.getResponse();
      	                //dumpResponse(response);
      	                aliCode=response.getQrCode();
      	                break;

      	            case FAILED:
      	            	System.out.println("支付宝预下单失败!!!");
      	                aliCode="";
      	                break;

      	            case UNKNOWN:
      	            	System.out.println("系统异常，预下单状态未知!!!");
      	                aliCode="";
      	                break;

      	            default:
      	            	System.out.println("不支持的交易状态，交易返回异常!!!");
      	                aliCode="";
      	                break;
      	        }
   		   }
   	    }
		
		return aliCode;
	}
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
