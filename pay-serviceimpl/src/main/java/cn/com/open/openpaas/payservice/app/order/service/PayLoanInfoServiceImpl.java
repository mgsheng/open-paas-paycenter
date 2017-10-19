package cn.com.open.openpaas.payservice.app.order.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.com.open.openpaas.payservice.app.infrastructure.repository.PayLoanInfoRepository;
import cn.com.open.openpaas.payservice.app.order.model.PayLoanInfo;

@Service
public class PayLoanInfoServiceImpl implements PayLoanInfoService{

	@Autowired
	private PayLoanInfoRepository payLoanInfoRepository;

	@Override
	public Boolean savePayLoanInfo(PayLoanInfo payLoanInfo) {
		// TODO Auto-generated method stub
		try {
			payLoanInfoRepository.insert(payLoanInfo);
			return true;
		} catch (Exception e) {
			// TODO: handle exception
			return false;
		}
	}
	

}
