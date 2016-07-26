package cn.com.open.openpaas.payservice.app.ditch.service;

import java.util.List;

import cn.com.open.openpaas.payservice.app.ditch.model.DictTradePaymentDitch;

/**
 * 
 */
public interface DictTradePaymentDitchService {


	List<DictTradePaymentDitch> findByAllDitch(String string);
	
}