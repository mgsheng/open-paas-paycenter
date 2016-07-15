package cn.com.open.openpaas.payservice.app.infrastructure.repository;

import org.springframework.stereotype.Repository;

import cn.com.open.openpaas.payservice.app.record.model.UserSerialRecord;


/**
 * 
 */
public interface UserSerialRecordRepository extends Repository {

	void saveUserSerialRecord(UserSerialRecord userSerialRecord);
}