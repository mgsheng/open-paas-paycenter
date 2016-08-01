package cn.com.open.openpaas.payservice.app.order.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import cn.com.open.openpaas.payservice.app.order.model.MerchantOrderInfo;
import cn.com.open.openpaas.payservice.app.record.model.UserSerialRecord;

/**
 * 
 */
public interface MerchantOrderInfoService {

	MerchantOrderInfo findByMerchantOrderId(String outTradeNo);
	MerchantOrderInfo findByMerchantOrderId(String outTradeNo,String appId);

	void saveMerchantOrderInfo(MerchantOrderInfo merchantOrderInfo);

	MerchantOrderInfo findById(String orderId);

	void updateOrderInfo(MerchantOrderInfo merchantOrderInfo);
	void updateOrder(MerchantOrderInfo merchantOrderInfo);
	void updateNotifyTimes(Integer notifyTimes,String id);
	void updatePayStatus(Integer payStatus,String id);
	void updateSourceType(Integer sourceType,String id);

	List<MerchantOrderInfo> findByPayAndNotifyStatus();

	void updateNotifyStatus(MerchantOrderInfo orderInfo);

	void updateOrderId(MerchantOrderInfo merchantOrderInfo);
	List<MerchantOrderInfo> findOrderByTime(String startTime,String endTime,String appId);
	HashMap<String, Object> getTotalAmountByTime(String startTime,String endTime,String appId);
	
}