package cn.com.open.openpaas.payservice.app.order.service;

import cn.com.open.openpaas.payservice.app.order.model.PayCardInfo;

/**
 * 
 */
public interface PayCardInfoService {

	PayCardInfo findByIdentityId(String identityId,String appId);
	Boolean savePayCardInfo(PayCardInfo payCardInfo);

	
}