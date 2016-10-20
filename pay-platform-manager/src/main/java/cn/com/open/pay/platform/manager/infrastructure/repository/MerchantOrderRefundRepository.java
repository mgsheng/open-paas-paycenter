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

	//List<MerchantOrderOffline> findAllNoPage(MerchantOrderOffline offline);
	
	int findQueryCount(MerchantOrderRefund merchantOrderRefund);

	/*void addOrderOffline(MerchantOrderOffline merchantOrderOffline);

	MerchantOrderOffline findByMerchantOrderId(String addMerchantOrderId);*/

	List<MerchantOrderRefund> findAllByPage(MerchantOrderRefund refund);
	
}