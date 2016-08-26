package cn.com.open.pay.platform.manager.infrastructure.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import cn.com.open.pay.platform.manager.order.model.UserSerialRecord;



/**
 * 
 */
public interface UserSerialRecordRepository extends Repository {


	List<UserSerialRecord> findQueryRunning(UserSerialRecord userSerialRecord);

	int findRunningCount(UserSerialRecord userSerialRecord);
}