package cn.com.open.pay.platform.manager.infrastructure.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import cn.com.open.pay.platform.manager.order.model.MerchantOrderOffline;
import cn.com.open.pay.platform.manager.order.model.MerchantOrderRefund;

//import cn.com.open.openpaas.payservice.app.order.model.MerchantOrderInfo;


/**
 * 
 */
public interface MerchantOrderRefundRepository extends Repository {

	int findQueryCount(MerchantOrderRefund merchantOrderRefund);

	List<MerchantOrderRefund> findAllByPage(MerchantOrderRefund refund);

	void insert(MerchantOrderRefund merchantOrderRefund);

	MerchantOrderRefund findByMerchantOrderId(String addMerchantOrderId);
	
}