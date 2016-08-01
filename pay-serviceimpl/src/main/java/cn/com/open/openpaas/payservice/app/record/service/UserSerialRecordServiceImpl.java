package cn.com.open.openpaas.payservice.app.record.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.com.open.openpaas.payservice.app.infrastructure.repository.UserSerialRecordRepository;
import cn.com.open.openpaas.payservice.app.record.model.UserSerialRecord;
import cn.com.open.openpaas.payservice.app.record.service.UserSerialRecordService;

/**
 * 
 */
@Service("userSerialRecordService")
public class UserSerialRecordServiceImpl implements UserSerialRecordService {

    @Autowired
    private UserSerialRecordRepository userSerialRecordRepository;

	@Override
	public void saveUserSerialRecord(UserSerialRecord userSerialRecord) {
		userSerialRecordRepository.saveUserSerialRecord(userSerialRecord);
	}

	@Override
	public List<UserSerialRecord> getSerialByTime(String startTime, String endTime,
			String appId , Integer payType) {
		return userSerialRecordRepository.getSerialByTime(startTime, endTime, appId,payType);
	}

	@Override
	public HashMap<String, Object> getTotalAmountByTime(String startTime,
			String endTime, String appId,Integer payType) {
		// TODO Auto-generated method stub
		return userSerialRecordRepository.getTotalAmountByTime(startTime, endTime, appId,payType);
	}

	@Override
	public List<Map<String, Object>> getTotalAmount(String startTime,
			String endTime, String appId) {
		// TODO Auto-generated method stub
		return userSerialRecordRepository.getTotalAmount(startTime, endTime, appId);
	}

	@Override
	public List<UserSerialRecord> getTotalSerialByTime(String startTime,
			String endTime, String appId) {
		// TODO Auto-generated method stub
		return userSerialRecordRepository.getTotalSerialByTime(startTime, endTime, appId);
	}





}