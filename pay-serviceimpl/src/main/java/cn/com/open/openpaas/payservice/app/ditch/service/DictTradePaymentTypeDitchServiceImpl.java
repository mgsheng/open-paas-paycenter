package cn.com.open.openpaas.payservice.app.ditch.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.com.open.openpaas.payservice.app.ditch.model.DictTradePaymentDitch;
import cn.com.open.openpaas.payservice.app.infrastructure.repository.DictTradePaymentDitchRepository;
import cn.com.open.openpaas.payservice.app.ditch.service.DictTradePaymentDitchService;
/**
 * 
 */
@Service("dictTradePaymentTypeDitchService")
public class DictTradePaymentTypeDitchServiceImpl implements DictTradePaymentDitchService {

    @Autowired
    private DictTradePaymentDitchRepository dictTradePaymentDitchRepository;

	

	@Override
	public List<DictTradePaymentDitch> findByAllDitch(String status) {
		return dictTradePaymentDitchRepository.findByAllDitch(status);
	}



}