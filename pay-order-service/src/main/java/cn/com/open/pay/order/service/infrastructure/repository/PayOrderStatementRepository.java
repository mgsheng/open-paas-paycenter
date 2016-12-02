package cn.com.open.pay.order.service.infrastructure.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;
import org.springframework.stereotype.Repository;
import cn.com.open.pay.order.service.statement.model.PayOrderStatement;

/**
 * 
 */
public interface PayOrderStatementRepository extends Repository {
	  PayOrderStatement findById (Integer merchantId);
	  void batchInsert(List<PayOrderStatement> list); 
	  @SelectProvider(type=DataFileSqlProvider.class,method="getInsertDataByCSVFileSql")  
	  public void insertDataByCSVFile(@Param("filepath") String filepath, @Param("tableName") String tableName);  
	  List<Map<String, Object>> getOrderIdByTime(@Param("startTime")String startTime, @Param("endTime")String endTime);
}