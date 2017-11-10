package cn.com.open.openpaas.payservice.app.infrastructure.repository;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import cn.com.open.openpaas.payservice.app.order.model.PayLoanInfo;


/**
 * 
 */
public interface PayLoanInfoRepository extends Repository {

	void insert(PayLoanInfo payLoanInfo);
	public PayLoanInfo findByOrderId(@Param("orderId")String orderId); 
	public Boolean updateStatus(@Param("id")Integer id,@Param("status")Integer status);
	
}