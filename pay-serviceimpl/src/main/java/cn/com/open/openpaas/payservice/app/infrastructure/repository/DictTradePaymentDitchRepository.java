package cn.com.open.openpaas.payservice.app.infrastructure.repository;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import cn.com.open.openpaas.payservice.app.ditch.model.DictTradePaymentDitch;


/**
 * 
 */
public interface DictTradePaymentDitchRepository extends Repository {

	List<DictTradePaymentDitch> findByAllDitch(@Param("paymentStatus")String paymentStatus);

}