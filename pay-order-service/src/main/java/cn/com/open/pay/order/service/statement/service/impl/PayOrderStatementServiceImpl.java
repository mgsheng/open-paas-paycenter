package cn.com.open.pay.order.service.statement.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.com.open.pay.order.service.infrastructure.repository.PayOrderStatementRepository;
import cn.com.open.pay.order.service.statement.model.PayOrderStatement;
import cn.com.open.pay.order.service.statement.service.PayOrderStatementService;

/**
 * 
 */
@Service("payOrderStatementService")
public class PayOrderStatementServiceImpl implements PayOrderStatementService {

    @Autowired
    private PayOrderStatementRepository payOrderStatementRepository;

	@Override
	public PayOrderStatement findById(Integer marchantId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void batchInsert(List<PayOrderStatement> list) {
		payOrderStatementRepository.batchInsert(list);
	}

	@Override
	public void saveCSV(String filePath, String tableName) {
		payOrderStatementRepository.insertDataByCSVFile(filePath, tableName);
	}


	@Override
	public List<Map<String, Object>> getOrderIdByTime(String startTime,
			String endTime) {
		// TODO Auto-generated method stub
		return payOrderStatementRepository.getOrderIdByTime(startTime, endTime);
	}

	@Override
	public List<Map<String, Object>> getOrderIdByStatus(Integer status) {
		// TODO Auto-generated method stub
		return payOrderStatementRepository.getOrderIdByStatus(status);
	}


}