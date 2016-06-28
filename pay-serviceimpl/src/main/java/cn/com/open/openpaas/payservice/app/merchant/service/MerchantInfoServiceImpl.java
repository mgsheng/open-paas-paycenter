package cn.com.open.openpaas.payservice.app.merchant.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.com.open.openpaas.payservice.app.infrastructure.repository.MerchantInfoRepository;
import cn.com.open.openpaas.payservice.app.merchant.model.MerchantInfo;

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