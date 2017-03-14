package cn.com.open.openpaas.payservice.app.order.service;

import cn.com.open.openpaas.payservice.app.order.model.PayOrderDetail;

/**
 * 
 */
public interface PayOrderDetailService {

	PayOrderDetail findByMerchantOrderId(String outTradeNo,String appId);
	Boolean savePayOrderDetail(PayOrderDetail payOrderDetail);

	
}