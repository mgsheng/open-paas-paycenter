package cn.com.open.pay.platform.manager.order.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.com.open.pay.platform.manager.infrastructure.repository.UserSerialRecordRepository;
import cn.com.open.pay.platform.manager.order.model.UserSerialRecord;
import cn.com.open.pay.platform.manager.order.service.UserSerialRecordService;

/**
 * 
 */
@Service("userSerialRecordService")
public class UserSerialRecordServiceImpl implements UserSerialRecordService {

    @Autowired
    private UserSerialRecordRepository userSerialRecordRepository;


	@Override
	public List<UserSerialRecord> findQueryRunning(UserSerialRecord userSerialRecord) {
		// TODO Auto-generated method stub
		return userSerialRecordRepository.findQueryRunning(userSerialRecord);
	}


	@Override
	public int findRunningCount(UserSerialRecord userSerialRecord) {
		// TODO Auto-generated method stub
		return userSerialRecordRepository.findRunningCount(userSerialRecord);
	}



}