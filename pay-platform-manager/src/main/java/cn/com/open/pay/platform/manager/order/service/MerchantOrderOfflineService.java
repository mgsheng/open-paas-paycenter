package cn.com.open.pay.platform.manager.order.service;

import java.util.List;

import cn.com.open.pay.platform.manager.order.model.MerchantOrderOffline;

/**
 * 
 */
public interface MerchantOrderOfflineService {
	
	List<MerchantOrderOffline> findOfflineAll(MerchantOrderOffline offline);
	
	int findOfflineAllCount(MerchantOrderOffline merchantOrderOffline);

	boolean addOrderOffline(MerchantOrderOffline merchantOrderOffline);

	MerchantOrderOffline findByMerchantOrderId(String addMerchantOrderId);
	
}