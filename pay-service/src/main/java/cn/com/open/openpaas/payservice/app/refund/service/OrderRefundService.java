package cn.com.open.openpaas.payservice.app.refund.service;

import cn.com.open.openpaas.payservice.app.refund.model.OrderRefund;

public interface OrderRefundService {
	void saveOrderRefundInfo(OrderRefund orderRefund);
	OrderRefund findByMerchantOrderId(String channelOrderId,String appId);
	void updateOrderRefund(OrderRefund orderRefund);

}
