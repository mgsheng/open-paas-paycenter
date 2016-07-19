package cn.com.open.openpaas.payservice.app.infrastructure.repository;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import cn.com.open.openpaas.payservice.app.payment.model.DictTradePayment;


/**
 * 
 */
public interface DictTradePaymentRepository extends Repository {
	DictTradePayment findByPaymentName(@Param("paymentName")String paymentName);

}