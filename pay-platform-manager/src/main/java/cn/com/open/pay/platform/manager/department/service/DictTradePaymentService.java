package cn.com.open.pay.platform.manager.department.service;

import java.util.List;

import cn.com.open.pay.platform.manager.department.model.Department;
import cn.com.open.pay.platform.manager.department.model.DictTradeChannel;
import cn.com.open.pay.platform.manager.department.model.DictTradePayment;
import cn.com.open.pay.platform.manager.login.model.User;
/**
 * 渠道管理
 * @author 
 *
 */
public interface DictTradePaymentService {
	
	/**
	 * 查询 返回list
	 * @return
	 */
	public List<DictTradePayment> findPaymentNamesAll();

	public DictTradePayment findNameById(String bankCode);

	public List<DictTradePayment> findPaymentNamesByType(DictTradePayment payment);

}
