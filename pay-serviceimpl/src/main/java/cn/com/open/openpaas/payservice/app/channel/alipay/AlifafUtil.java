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
import com.alipay.demo.trade.service.AlipayTradeService;
import com.alipay.demo.trade.service.impl.AlipayTradeServiceImpl;

public class AlifafUtil {
	 private static String name;
	 // 支付宝当面付2.0服务
	 private static AlipayTradeService tradeService;
	/* static {
			if(nullEmptyBlankJudge(name)){
				 Configs.init(name);	
			}else{
				 Configs.init("");
			}

	        *//** 使用Configs提供的默认参数
	         *  AlipayTradeService可以使用单例或者为静态成员对象，不需要反复new
	         *//*
	        tradeService = new AlipayTradeServiceImpl.ClientBuilder().build();

	    }*/
	 public String getName() {
	  return name;
	 }

	 public void setName(String name) {
	  this.name = name;
	 }

	 // 该类只能有一个实例
	 private AlifafUtil() {
	 } // 私有无参构造方法

	 // 该类必须自行创建
	 // 有2种方式
	 
	 private static AlifafUtil ts1 = null;

	 // 这个类必须自动向整个系统提供这个实例对象
	 public static AlifafUtil getAlifafUtil() {
	  if (ts1 == null) {
	   ts1 = new AlifafUtil();
	  }
	  return ts1;
	 }
	    // 测试当面付2.0生成支付二维码
	public String trade_precreate(MerchantOrderInfo merchantOrderInfo,DictTradeChannelService dictTradeChannelService) {
	        // (必填) 商户网站订单系统中唯一订单号，64个字符以内，只能包含字母、数字、下划线，
	        // 需保证商户系统端不能重复，建议通过数据库sequence生成，
	        //String outTradeNo = "tradeprecreate" + System.currentTimeMillis() + (long)(Math.random() * 10000000L);

	        // (必填) 订单标题，粗略描述用户的支付目的。如“喜士多（浦东店）消费”
	       // String subject = "喜士多（浦东店）消费";

	        // (必填) 订单总金额，单位为元，不能超过1亿元
	        // 如果同时传入了【打折金额】,【不可打折金额】,【订单总金额】三者,则必须满足如下条件:【订单总金额】=【打折金额】+【不可打折金额】
	        //String totalAmount = "0.01";

	        // (可选) 订单不可打折金额，可以配合商家平台配置折扣活动，如果酒水不参与打折，则将对应金额填写至此字段
	        // 如果该值未传入,但传入了【订单总金额】,【打折金额】,则该值默认为【订单总金额】-【打折金额】
	        //String undiscountableAmount = "0";

	        // 卖家支付宝账号ID，用于支持一个签约账号下支持打款到不同的收款账号，(打款到sellerId对应的支付宝账号)
	        // 如果该字段为空，则默认为与支付宝签约的商户的PID，也就是appid对应的PID
	        //String sellerId = "";

	        // 订单描述，可以对交易或商品进行一个详细地描述，比如填写"购买商品2件共15.00元"
	        //String body = "购买商品2件共15.00元";

	        // 商户操作员编号，添加此参数可以为商户操作员做销售统计
	        // operatorId = "test_operator_id";

	        // (必填) 商户门店编号，通过门店号和商家后台可以配置精准到门店的折扣信息，详询支付宝技术支持
	        //String storeId = "store_id";

	        // 业务扩展参数，目前可添加由支付宝分配的系统商编号(通过setSysServiceProviderId方法)，详情请咨询支付宝技术支持
	/*        ExtendParams extendParams = new ExtendParams();
	        extendParams.setSysServiceProviderId("2088801478647757");*/

	        // 支付超时，定义为120分钟
	        //String timeExpress = "120m";
	    	 String aliCode="";
	    	if(merchantOrderInfo!=null){
	    		 DictTradeChannel dictTradeChannels=dictTradeChannelService.findByMAI(String.valueOf(merchantOrderInfo.getMerchantId()),Channel.ALIFAF.getValue());
	    			String porpertiesName="";
	    		   if(dictTradeChannels!=null){
	    			   String other= dictTradeChannels.getOther();
	    			   String notifyUrl=dictTradeChannels.getNotifyUrl();
	        			Map<String, String> others = new HashMap<String, String>();
	        			others=getPartner(other); 
	        			porpertiesName=others.get("porpertiesName");
	        			if(!nullEmptyBlankJudge(porpertiesName)){
	        				 Configs.init(porpertiesName);	
	        			}else{
	        				 Configs.init("");
	        			}
	        	        /** 使用Configs提供的默认参数
	        	         *  AlipayTradeService可以使用单例或者为静态成员对象，不需要反复new
	        	         */
	        	        tradeService = new AlipayTradeServiceImpl.ClientBuilder().build();
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
	       	                AlipayTradePrecreateResponse response = result.getResponse();
	       	                //dumpResponse(response);
	       	                aliCode=response.getQrCode();
	       	                break;

	       	            case FAILED:
	       	                aliCode="";
	       	                break;

	       	            case UNKNOWN:
	       	                aliCode="";
	       	                break;

	       	            default:
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
		/**
		 * 检验字符串是否为空
		 * @param str
		 * @return
		 */
		 public static boolean nullEmptyBlankJudge(String str){
		        return null==str||str.isEmpty()||"".equals(str.trim());
		  }

}


