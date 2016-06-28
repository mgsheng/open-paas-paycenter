package cn.com.open.openpaas.payservice.app.refund.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.com.open.openpaas.payservice.app.infrastructure.repository.OrderRefundRepository;
import cn.com.open.openpaas.payservice.app.refund.model.OrderRefund;

/**
 * 
 */
@Service("orderRefundService")
public class OrderRefundServiceImpl implements OrderRefundService {

    @Autowired
    private OrderRefundRepository orderRefundRepository;

	@Override
	public void saveOrderRefundInfo(OrderRefund orderRefund) {
		orderRefundRepository.saveOrderRefundInfo(orderRefund);
		
	}

	@Override
	public OrderRefund findByMerchantOrderId(String outTradeNo,String appId) {
		return orderRefundRepository.findByMidAndAppId(outTradeNo, appId);
	}

	@Override
	public void updateOrderRefund(OrderRefund orderRefund) {
		orderRefundRepository.updateOrderRefund(orderRefund);
	}



}