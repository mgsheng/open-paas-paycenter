package cn.com.open.openpaas.payservice.app.order.service;

import cn.com.open.openpaas.payservice.app.order.model.PayCardInfo;

/**
 * 
 */
public interface PayCardInfoService {

	Boolean savePayCardInfo(PayCardInfo payCardInfo);
	PayCardInfo getCardInfo(String userId,String appId);
	void updateCardInfo(PayCardInfo payCardInfo);
    void updateCardStatus(Integer status,Integer id);

	
}