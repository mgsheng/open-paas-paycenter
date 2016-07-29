package cn.com.open.openpaas.payservice.app.record.service;

import java.util.Date;
import java.util.List;

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
			String appId) {
		return userSerialRecordRepository.getSerialByTime(startTime, endTime, appId);
	}





}