package cn.com.open.openpaas.payservice.app.infrastructure.repository;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import cn.com.open.openpaas.payservice.app.refund.model.OrderRefund;


/**
 * 
 */
public interface OrderRefundRepository extends Repository {
	
	void saveOrderRefundInfo(OrderRefund orderRefund);
	OrderRefund findByMidAndAppId (@Param("merchantOrderId")String merchantOrderId,@Param("appId")String appId);
	void updateOrderRefund(OrderRefund orderRefund);
}