package cn.com.open.pay.order.service.merchant.service;

import cn.com.open.pay.order.service.merchant.model.MerchantInfo;

/**
 * 
 */
public interface MerchantInfoService {

	MerchantInfo findById(Integer marchantId);
	
}