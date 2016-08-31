package cn.com.open.pay.order.service.order.service;

import java.util.HashMap;
import java.util.List;

import cn.com.open.pay.order.service.order.model.MerchantOrderInfo;

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
	void updatePayWay(MerchantOrderInfo merchantOrderInfo);

	List<MerchantOrderInfo> findByPayAndNotifyStatus();

	void updateNotifyStatus(MerchantOrderInfo orderInfo);

	void updateOrderId(MerchantOrderInfo merchantOrderInfo);
	List<MerchantOrderInfo> findOrderByTime(String startTime,String endTime,String appId);
	HashMap<String, Object> getTotalAmountByTime(String startTime,String endTime,String appId);
	
}