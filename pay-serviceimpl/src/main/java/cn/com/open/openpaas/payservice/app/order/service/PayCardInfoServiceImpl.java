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
	@Override
	public PayCardInfo getCardInfo(String userId, String appId) {
		// TODO Auto-generated method stub
		return payCardInfoRepository.getCardInfo(userId, appId);
	}
	@Override
	public void updateCardInfo(PayCardInfo payCardInfo) {
		payCardInfoRepository.updateCardInfo(payCardInfo);
	}
	@Override
	public void updateCardStatus(Integer payStatus, Integer id) {
		payCardInfoRepository.updateCardStatus(payStatus, id);
		
	}



}