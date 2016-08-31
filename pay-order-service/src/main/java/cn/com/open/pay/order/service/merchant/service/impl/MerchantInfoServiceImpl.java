package cn.com.open.pay.order.service.merchant.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.com.open.pay.order.service.infrastructure.repository.MerchantInfoRepository;
import cn.com.open.pay.order.service.merchant.model.MerchantInfo;
import cn.com.open.pay.order.service.merchant.service.MerchantInfoService;

/**
 * 
 */
@Service("merchantInfoService")
public class MerchantInfoServiceImpl implements MerchantInfoService {

    @Autowired
    private MerchantInfoRepository merchantInfoRepository;

	@Override
	public MerchantInfo findById(Integer marchantId) {
		MerchantInfo merchantInfo = merchantInfoRepository.findById(marchantId);
		return merchantInfo;
	}

}