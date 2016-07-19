package cn.com.open.openpaas.payservice.app.payment.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.com.open.openpaas.payservice.app.channel.model.DictTradeChannel;
import cn.com.open.openpaas.payservice.app.infrastructure.repository.DictTradeChannelRepository;
import cn.com.open.openpaas.payservice.app.infrastructure.repository.DictTradePaymentRepository;
import cn.com.open.openpaas.payservice.app.payment.model.DictTradePayment;
import cn.com.open.openpaas.payservice.app.payment.service.DictTradePaymentService;

/**
 * 
 */
@Service("dictTradePaymentTypeService")
public class DictTradePaymentTypeServiceImpl implements DictTradePaymentService {

    @Autowired
    private DictTradePaymentRepository dictTradePaymentRepository;

	@Override
	public DictTradePayment findByPaymentName(String paymentName) {
		return null;
	}



}