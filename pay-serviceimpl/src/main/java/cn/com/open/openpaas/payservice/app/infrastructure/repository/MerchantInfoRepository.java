package cn.com.open.openpaas.payservice.app.infrastructure.repository;

import org.springframework.stereotype.Repository;

import cn.com.open.openpaas.payservice.app.merchant.model.MerchantInfo;


/**
 * 
 */
public interface MerchantInfoRepository extends Repository {
	MerchantInfo findById (Integer merchantId);
}