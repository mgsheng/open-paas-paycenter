package cn.com.open.pay.platform.manager.order.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.com.open.pay.platform.manager.order.model.MerchantOrderInfo;
import cn.com.open.pay.platform.manager.order.model.UserSerialRecord;


/**
 * 
 */
public interface UserSerialRecordService {

//	void saveUserSerialRecord(UserSerialRecord userSerialRecord);
//	List<UserSerialRecord>getSerialByTime(String startTime,String endTime,String appId,Integer payType);
//	HashMap<String, Object> getTotalAmountByTime(String startTime,String endTime,String appId,Integer payType);
//	List<Map<String, Object>> getTotalAmount(String startTime,String endTime,String appId);
//	List<UserSerialRecord>getTotalSerialByTime(String startTime,String endTime,String appId);
	
	
	List<UserSerialRecord> findQueryRunning(UserSerialRecord userSerialRecord);

	int findRunningCount(UserSerialRecord userSerialRecord);
	
}