package cn.com.open.openpaas.payservice.app.infrastructure.repository;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import cn.com.open.openpaas.payservice.app.order.model.PayOrderDetail;


/**
 * 
 */
public interface PayOrderDetailRepository extends Repository {

	PayOrderDetail findByMerchantOrderId (@Param("merchantOrderId")String merchantOrderId,@Param("appId")String appId);
	void insert(PayOrderDetail payOrderDetail);
	void savePayOrderDetail(PayOrderDetail payOrderDetail);
	
}