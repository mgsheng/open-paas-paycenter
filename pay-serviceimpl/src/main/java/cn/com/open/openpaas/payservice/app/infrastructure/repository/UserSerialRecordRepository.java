package cn.com.open.openpaas.payservice.app.infrastructure.repository;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import cn.com.open.openpaas.payservice.app.record.model.UserSerialRecord;


/**
 * 
 */
public interface UserSerialRecordRepository extends Repository {

	void saveUserSerialRecord(UserSerialRecord userSerialRecord);
	List<UserSerialRecord> getSerialByTime(
			@Param("startTime")String startTime,
			@Param("endTime")String endTime,
			@Param("appId")String appId,
			@Param("payType")Integer payType
	);
	List<UserSerialRecord> getTotalSerialByTime(
			@Param("startTime")String startTime,
			@Param("endTime")String endTime,
			@Param("appId")String appId
			
	);
	HashMap<String, Object> getTotalAmountByTime(@Param("startTime")String startTime, @Param("endTime")String endTime,@Param("appId")String appId,@Param("payType")Integer payType);
	List<Map<String, Object>> getTotalAmount(@Param("startTime")String startTime, @Param("endTime")String endTime,@Param("appId")String appId);
}