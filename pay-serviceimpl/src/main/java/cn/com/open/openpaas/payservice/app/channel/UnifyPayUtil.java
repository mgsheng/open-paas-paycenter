package cn.com.open.openpaas.payservice.app.channel;

import java.math.BigDecimal;
import java.util.Date;
import cn.com.open.openpaas.payservice.app.balance.model.UserAccountBalance;
import cn.com.open.openpaas.payservice.app.balance.service.UserAccountBalanceService;
import cn.com.open.openpaas.payservice.app.channel.alipay.Channel;
import cn.com.open.openpaas.payservice.app.channel.alipay.PayChannelRate;
import cn.com.open.openpaas.payservice.app.channel.model.ChannelRate;
import cn.com.open.openpaas.payservice.app.channel.service.ChannelRateService;
import cn.com.open.openpaas.payservice.app.order.model.MerchantOrderInfo;
import cn.com.open.openpaas.payservice.app.record.model.UserSerialRecord;
import cn.com.open.openpaas.payservice.app.record.service.UserSerialRecordService;
import cn.com.open.openpaas.payservice.app.zookeeper.DistributedLock;
import cn.com.open.openpaas.payservice.dev.PayserviceDev;

public class UnifyPayUtil {
	
		public static String recordAndBalance(Double total_fee,
				MerchantOrderInfo merchantOrderInfo,UserSerialRecordService userSerialRecordService,UserAccountBalanceService userAccountBalanceService,PayserviceDev payserviceDev) {
			String userId=String.valueOf(merchantOrderInfo.getSourceUid());
			String rechargeMsg;
			//流水记录
			UserSerialRecord userSerialRecord=new UserSerialRecord();
			userSerialRecord.setAmount(total_fee);
			userSerialRecord.setAppId(Integer.parseInt(merchantOrderInfo.getAppId()));
			userSerialRecord.setSerialNo(merchantOrderInfo.getId());
			userSerialRecord.setSourceId(merchantOrderInfo.getSourceUid());
			userSerialRecord.setPayType(1);
			userSerialRecord.setCreateTime(new Date());
			userSerialRecord.setUserName(merchantOrderInfo.getUserName());
			userSerialRecordService.saveUserSerialRecord(userSerialRecord);
			//充值
			UserAccountBalance  userAccountBalance=userAccountBalanceService.findByUserId(userId);
			if(userAccountBalance!=null){
				userAccountBalance.setBalance(total_fee/100);
				 DistributedLock lock = null;
			     try {
				  lock = new DistributedLock(payserviceDev.getZookeeper_config(),userAccountBalance.getSourceId()+userAccountBalance.getAppId());
				  lock.lock();
				  userAccountBalanceService.updateBalanceInfo(userAccountBalance);
				  rechargeMsg="SUCCESS";
			     } catch (Exception e) {
					e.printStackTrace();
					 rechargeMsg="ERROR";
				  }finally{
					  lock.unlock(); 
				  }
			}else{
				userAccountBalance=new UserAccountBalance();
				userAccountBalance.setUserId(userId);
				userAccountBalance.setStatus(1);
				userAccountBalance.setType(1);
				userAccountBalance.setCreateTime(new Date());
				userAccountBalanceService.saveUserAccountBalance(userAccountBalance);
				rechargeMsg="SUCCESS";
			}
			return rechargeMsg;
		}
		/**
		 * 计算获取手续费(结果为元)
		 * @param merchantOrderInfo
		 * @return
		 */
		public static Double getPayCharge(MerchantOrderInfo merchantOrderInfo,ChannelRateService channelRateService){
			Double returnValue=0.0;
		    String payChannelCode="";
		    Double rete=0.0;
			if(merchantOrderInfo.getSourceType()==0){
				//直连支付宝
				if(merchantOrderInfo.getChannelId().equals(Channel.ALI.getValue())){
					//即时到账
					payChannelCode=PayChannelRate.ALIPAY.value;
				}
				else if(merchantOrderInfo.getChannelId().equals(Channel.EBANK.getValue())){
					//网银支付
					payChannelCode=PayChannelRate.ALIEBAKN.value;
				}else if(merchantOrderInfo.getChannelId().equals(Channel.WEIXIN.getValue())){
					//网银支付
					payChannelCode=PayChannelRate.WEIXIN.value;
				}	
			}else if(merchantOrderInfo.getSourceType()==1){
				//tcl支付
				payChannelCode=PayChannelRate.TCLPAY.value;
			}else if(merchantOrderInfo.getSourceType()==2){
				//易宝支付
				payChannelCode=PayChannelRate.YEEPAY.value;
			}else if(merchantOrderInfo.getSourceType()==3){
				//易宝支付
				payChannelCode=PayChannelRate.PAYMAX.value;
			}
			ChannelRate channelRate=channelRateService.getChannelRate(String.valueOf(merchantOrderInfo.getMerchantId()), payChannelCode);
			if(channelRate!=null){
				//Double amount=200.00;
				rete=merchantOrderInfo.getOrderAmount()* 100 * Double.parseDouble(channelRate.getPayRate());
				//rete=amount* Double.parseDouble(channelRate.getPayRate());
				BigDecimal b =new BigDecimal(rete/100);  
				returnValue =b.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();  
			}
			return returnValue;
		}
		/**
		 * 计算获取手续费(结果为元)
		 * @param merchantOrderInfo
		 * @return
		 */
		public static Double getPayCharge(MerchantOrderInfo merchantOrderInfo,ChannelRateService channelRateService,String channelId){
			Double returnValue=0.0;
		    Double rete=0.0;
			ChannelRate channelRate=channelRateService.getChannelRate(channelId, merchantOrderInfo.getSourceType());
			if(channelRate!=null){
				//Double amount=200.00;
				rete=merchantOrderInfo.getOrderAmount()* 100 * Double.parseDouble(channelRate.getPayRate());
				//rete=amount* Double.parseDouble(channelRate.getPayRate());
				BigDecimal b =new BigDecimal(rete/100);  
				returnValue =b.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();  
			}
			return returnValue;
		}

}
