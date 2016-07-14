package cn.com.open.openpaas.payservice.app.record.service;

import java.util.Date;
import java.util.List;

import cn.com.open.openpaas.payservice.app.order.model.MerchantOrderInfo;

/**
 * 
 */
public interface UserSerialRecordService {

	MerchantOrderInfo findByMerchantOrderId(String outTradeNo);
	MerchantOrderInfo findByMerchantOrderId(String outTradeNo,String appId);

	void saveMerchantOrderInfo(MerchantOrderInfo merchantOrderInfo);

	MerchantOrderInfo findById(String orderId);

	void updateOrderInfo(MerchantOrderInfo merchantOrderInfo);
	void updateOrder(MerchantOrderInfo merchantOrderInfo);
	void updateNotifyTimes(Integer notifyTimes,String id);
	void updatePayStatus(Integer payStatus,String id);

	List<MerchantOrderInfo> findByPayAndNotifyStatus();

	void updateNotifyStatus(MerchantOrderInfo orderInfo);

	void updateOrderId(MerchantOrderInfo merchantOrderInfo);
	
}