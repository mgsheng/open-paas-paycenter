package cn.com.open.openpaas.payservice.app.zxpt.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.com.open.openpaas.payservice.app.infrastructure.repository.PayZxptInfoRepository;
import cn.com.open.openpaas.payservice.app.zxpt.model.PayZxptInfo;

/**
 * 
 */
@Service("payZxptInfoService")
public class PayZxptInfoServiceImpl implements PayZxptInfoService {

    @Autowired
    private PayZxptInfoRepository payZxptInfoRepository;
    @Override
	public PayZxptInfo findByMerchantOrderId(String outTradeNo,String appId) {
		PayZxptInfo payZxptInfo = payZxptInfoRepository.findByMidAndAppId(outTradeNo,appId);
		return payZxptInfo;
	}

	@Override
	public Boolean savePayZxptInfo(PayZxptInfo payZxptInfo) {
		try {
			payZxptInfoRepository.savePayZxptInfo(payZxptInfo);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	@Override
	public void updateZxptInfo(PayZxptInfo payZxptInfo) {
		// TODO Auto-generated method stub
		payZxptInfoRepository.updateZxptInfo(payZxptInfo);
	}

	@Override
	public PayZxptInfo findById(String orderId) {
		// TODO Auto-generated method stub
		return payZxptInfoRepository.findById(orderId);
	}


}