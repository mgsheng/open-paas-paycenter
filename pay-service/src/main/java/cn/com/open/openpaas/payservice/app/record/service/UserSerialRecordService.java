package cn.com.open.openpaas.payservice.app.record.service;

import java.util.Date;
import java.util.List;

import cn.com.open.openpaas.payservice.app.record.model.UserSerialRecord;

/**
 * 
 */
public interface UserSerialRecordService {

	void saveUserSerialRecord(UserSerialRecord userSerialRecord);
	List<UserSerialRecord>getSerialByTime(String startTime,String endTime,String appId);
}