package cn.com.open.openpaas.payservice.app.order.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.com.open.openpaas.payservice.app.infrastructure.repository.PayOrderDetailRepository;
import cn.com.open.openpaas.payservice.app.order.model.PayOrderDetail;

/**
 * 
 */
@Service("payOrderDetailService")
public class PayOrderDetailServiceImpl implements PayOrderDetailService {

    @Autowired
    private PayOrderDetailRepository payOrderDetailRepository;

	@Override
	public PayOrderDetail findByMerchantOrderId(String merchantOrderId, String appId) {
		// TODO Auto-generated method stub
		return payOrderDetailRepository.findByMerchantOrderId(merchantOrderId, appId);
	}
	@Override
	public Boolean savePayOrderDetail(PayOrderDetail payOrderDetail) {
		try {
			payOrderDetailRepository.savePayOrderDetail(payOrderDetail);
			return  true;
		} catch (Exception e) {
			return false;
		}
		
		
	}



}