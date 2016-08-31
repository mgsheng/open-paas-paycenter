package cn.com.open.pay.order.service.infrastructure.repository;

import org.springframework.stereotype.Repository;

import cn.com.open.pay.order.service.merchant.model.MerchantInfo;


/**
 * 
 */
public interface MerchantInfoRepository extends Repository {
	MerchantInfo findById (Integer merchantId);
}