package cn.com.open.openpaas.payservice.app.record.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.com.open.openpaas.payservice.app.record.model.UserSerialRecord;

/**
 * 
 */
public interface UserSerialRecordService {

	void saveUserSerialRecord(UserSerialRecord userSerialRecord);
	List<UserSerialRecord>getSerialByTime(String startTime,String endTime,String appId,Integer payType);
	HashMap<String, Object> getTotalAmountByTime(String startTime,String endTime,String appId,Integer payType);
	List<Map<String, Object>> getTotalAmount(String startTime,String endTime,String appId);
	List<UserSerialRecord>getTotalSerialByTime(String startTime,String endTime,String appId);
	
}