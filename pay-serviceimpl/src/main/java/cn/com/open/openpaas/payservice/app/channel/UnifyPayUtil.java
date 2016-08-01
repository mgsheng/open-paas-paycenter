package cn.com.open.openpaas.payservice.app.channel;

import java.util.Date;
import cn.com.open.openpaas.payservice.app.balance.model.UserAccountBalance;
import cn.com.open.openpaas.payservice.app.balance.service.UserAccountBalanceService;
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

}
