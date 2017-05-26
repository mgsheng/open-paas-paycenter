package cn.com.open.openpaas.payservice.app.infrastructure.repository;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import cn.com.open.openpaas.payservice.app.order.model.MerchantOrderInfo;
import cn.com.open.openpaas.payservice.app.zxpt.model.PayZxptInfo;


/**
 * 
 */
public interface PayZxptInfoRepository extends Repository {

	PayZxptInfo findByMidAndAppId (@Param("merchantOrderId")String merchantOrderId,@Param("appId")String appId);
	void savePayZxptInfo(PayZxptInfo payZxptInfo);
	PayZxptInfo findById(@Param("orderId")String orderId);
	void updateZxptInfo(PayZxptInfo payZxptInfo);
	
	
}