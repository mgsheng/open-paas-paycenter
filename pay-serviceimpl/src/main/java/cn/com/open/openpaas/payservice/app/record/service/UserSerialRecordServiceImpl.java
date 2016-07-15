package cn.com.open.openpaas.payservice.app.record.service;

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





}