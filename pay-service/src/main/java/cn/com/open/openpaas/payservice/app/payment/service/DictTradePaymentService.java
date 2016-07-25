package cn.com.open.openpaas.payservice.app.payment.service;

import java.util.List;

import cn.com.open.openpaas.payservice.app.channel.model.DictTradeChannel;
import cn.com.open.openpaas.payservice.app.payment.model.DictTradePayment;

/**
 * 
 */
public interface DictTradePaymentService {

	DictTradePayment findByPaymentName(String paymentName);

	List<DictTradePayment> findByAllMessage(String string);
	
}