package cn.com.open.openpaas.payservice.app.order.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.com.open.openpaas.payservice.app.infrastructure.repository.PayCardInfoRepository;
import cn.com.open.openpaas.payservice.app.infrastructure.repository.PayOrderDetailRepository;
import cn.com.open.openpaas.payservice.app.order.model.PayCardInfo;
import cn.com.open.openpaas.payservice.app.order.model.PayOrderDetail;

/**
 * 
 */
@Service("payCardInfoService")
public class PayCardInfoServiceImpl implements PayCardInfoService {

    @Autowired
    private PayCardInfoRepository payCardInfoRepository;

    
	@Override
	public PayCardInfo findByIdentityId(String identityId, String appId) {
		// TODO Auto-generated method stub
		return payCardInfoRepository.findByIdentityId(identityId, appId);
	}
	@Override
	public Boolean savePayCardInfo(PayCardInfo payCardInfo) {
		// TODO Auto-generated method stub
		try {
			payCardInfoRepository.savePayCardInfo(payCardInfo);
			return true;
		} catch (Exception e) {
			// TODO: handle exception
			return false;
		}
		
	}



}