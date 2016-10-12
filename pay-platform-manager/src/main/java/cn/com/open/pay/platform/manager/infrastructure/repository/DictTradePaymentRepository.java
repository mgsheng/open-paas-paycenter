package cn.com.open.pay.platform.manager.infrastructure.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import cn.com.open.pay.platform.manager.department.model.DictTradeChannel;
import cn.com.open.pay.platform.manager.department.model.DictTradePayment;


/**
 * 
 */
public interface DictTradePaymentRepository extends Repository {
	
	/**
	 * 查询所有支付银行名称
	 * @return
	 */
	public List<DictTradePayment> findPaymentNamesAll();
	
}
