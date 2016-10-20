package cn.com.open.pay.platform.manager.order.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.com.open.pay.platform.manager.infrastructure.repository.MerchantOrderOfflineRepository;
import cn.com.open.pay.platform.manager.infrastructure.repository.MerchantOrderRefundRepository;
import cn.com.open.pay.platform.manager.order.model.MerchantOrderInfo;
import cn.com.open.pay.platform.manager.order.model.MerchantOrderOffline;
import cn.com.open.pay.platform.manager.order.model.MerchantOrderRefund;
import cn.com.open.pay.platform.manager.order.service.MerchantOrderOfflineService;
import cn.com.open.pay.platform.manager.order.service.MerchantOrderRefundService;

/**
 * 
 */
@Service("merchantOrderRefundService")
public class MerchantOrderRefundServiceImpl implements MerchantOrderRefundService {

    @Autowired
    private MerchantOrderRefundRepository merchantOrderRefundRepository;

	@Override
	public List<MerchantOrderRefund> findRefundAll(MerchantOrderRefund refund) {
		List<MerchantOrderRefund> merchantOrderRefunds=merchantOrderRefundRepository.findAllByPage(refund);
		return merchantOrderRefunds;
	}

	@Override
	public int findRefundAllCount(MerchantOrderRefund merchantOrderRefund) {
		int merchantOrderRefundList = merchantOrderRefundRepository.findQueryCount(merchantOrderRefund);
		return merchantOrderRefundList;
	}

	/*@Override
	public boolean addOrderOffline(MerchantOrderOffline merchantOrderOffline) {
		try{
			merchantOrderOfflineRepository.addOrderOffline(merchantOrderOffline);
			return true;
		}catch(Exception e){
			return false;
		}
	}*/

	/*@Override
	public MerchantOrderOffline findByMerchantOrderId(String addMerchantOrderId) {
		MerchantOrderOffline offline=merchantOrderOfflineRepository.findByMerchantOrderId(addMerchantOrderId);
		return offline;
	}*/

	/*@Override
	public List<MerchantOrderOffline> findOfflineAllNoPage(MerchantOrderOffline offline) {
		List<MerchantOrderOffline> merchantOrderOfflines=merchantOrderOfflineRepository.findAllNoPage(offline);
		return merchantOrderOfflines;
	}*/
}