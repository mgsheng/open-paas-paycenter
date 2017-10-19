package cn.com.open.openpaas.payservice.app.infrastructure.repository;

import org.springframework.stereotype.Repository;

import cn.com.open.openpaas.payservice.app.order.model.PayLoanInfo;


/**
 * 
 */
public interface PayLoanInfoRepository extends Repository {

	void insert(PayLoanInfo payLoanInfo);
	
}