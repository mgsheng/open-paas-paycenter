package cn.com.open.openpaas.payservice.app.channel.alipay;

import java.util.HashMap;
import java.util.Map;

import cn.com.open.openpaas.payservice.app.channel.model.DictTradeChannel;
import cn.com.open.openpaas.payservice.app.channel.service.DictTradeChannelService;
import cn.com.open.openpaas.payservice.app.order.model.MerchantOrderInfo;

public class AlipayPropetyFactory {

	  public static Map<String,AlipayUtil> alipayUtilMap=new HashMap<String,AlipayUtil>();
	  public static AlipayUtil getAlifafUtil(MerchantOrderInfo merchantOrderInfo,DictTradeChannelService dictTradeChannelService){
		if(merchantOrderInfo!=null){
   		 DictTradeChannel dictTradeChannels=dictTradeChannelService.findByMAI(String.valueOf(merchantOrderInfo.getMerchantId()),Channel.ALIFAF.getValue());
  	   String other= dictTradeChannels.getOther();
	   String notifyUrl=dictTradeChannels.getNotifyUrl();
		Map<String, String> others = new HashMap<String, String>();
		others=getPartner(other); 
	    String porpertiesName=others.get("porpertiesName");
	    AlipayUtil util=alipayUtilMap.get(porpertiesName);
	    if(util==null)
	    {
	    	 util=AlipayUtil.getAlipayUtil(porpertiesName);
	    	 alipayUtilMap.put(porpertiesName, util);
	    }
	    return util;
		}
		return null;
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
