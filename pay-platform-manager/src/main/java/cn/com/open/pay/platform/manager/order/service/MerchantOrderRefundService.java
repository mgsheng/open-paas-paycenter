package cn.com.open.pay.platform.manager.order.service;

import java.util.List;

import cn.com.open.pay.platform.manager.order.model.MerchantOrderRefund;

/**
 * 
 */
public interface MerchantOrderRefundService {
	
	List<MerchantOrderRefund> findRefundAll(MerchantOrderRefund refund);
	
	int findRefundAllCount(MerchantOrderRefund merchantOrderRefund);

	boolean addOrderRefund(MerchantOrderRefund merchantOrderRefund);

	MerchantOrderRefund findByMerchantOrderId(String addMerchantOrderId);

	/*MerchantOrderOffline findByMerchantOrderId(String addMerchantOrderId);

	List<MerchantOrderOffline> findOfflineAllNoPage(MerchantOrderOffline offline);*/
	
}