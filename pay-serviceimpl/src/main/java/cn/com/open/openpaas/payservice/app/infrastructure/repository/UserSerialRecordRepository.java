package cn.com.open.openpaas.payservice.app.infrastructure.repository;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import cn.com.open.openpaas.payservice.app.record.model.UserSerialRecord;


/**
 * 
 */
public interface UserSerialRecordRepository extends Repository {

	void saveUserSerialRecord(UserSerialRecord userSerialRecord);
	List<UserSerialRecord> getSerialByTime(
			@Param("startTime")Date startTime,
			@Param("endTime")Date endTime,
			@Param("appId")String appId
	);

}