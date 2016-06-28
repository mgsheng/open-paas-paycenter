package cn.com.open.openpaas.payservice.app.merchant.service;

import cn.com.open.openpaas.payservice.app.merchant.model.MerchantInfo;

/**
 * 
 */
public interface MerchantInfoService {

	MerchantInfo findById(Integer marchantId);
	
}