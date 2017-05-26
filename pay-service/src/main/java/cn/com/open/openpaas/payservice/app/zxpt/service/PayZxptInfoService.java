package cn.com.open.openpaas.payservice.app.zxpt.service;

import cn.com.open.openpaas.payservice.app.zxpt.model.PayZxptInfo;

/**
 * 
 */
public interface PayZxptInfoService {

	Boolean savePayZxptInfo(PayZxptInfo payZxptInfo);
	PayZxptInfo findByMerchantOrderId(String merchantOrderId,String appId);
	void updateZxptInfo(PayZxptInfo payZxptInfo);
	PayZxptInfo findById(String orderId);

	
}