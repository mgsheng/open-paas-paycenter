package cn.com.open.pay.order.service.statement.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import cn.com.open.pay.order.service.statement.model.PayOrderStatement;

/**
 * 
 */
public interface PayOrderStatementService {

	PayOrderStatement findById(Integer marchantId);
	 void batchInsert(List<PayOrderStatement> list); 
	void saveCSV(String filePath,String tableName);
	List<Map<String, Object>> getOrderIdByTime(String startTime, String endTime);
	List<Map<String, Object>> getOrderIdByStatus(Integer status);
}