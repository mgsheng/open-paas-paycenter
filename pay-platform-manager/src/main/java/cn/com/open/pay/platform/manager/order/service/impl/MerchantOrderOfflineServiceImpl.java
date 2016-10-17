package cn.com.open.pay.platform.manager.order.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.com.open.pay.platform.manager.infrastructure.repository.MerchantOrderOfflineRepository;
import cn.com.open.pay.platform.manager.order.model.MerchantOrderInfo;
import cn.com.open.pay.platform.manager.order.model.MerchantOrderOffline;
import cn.com.open.pay.platform.manager.order.service.MerchantOrderOfflineService;

/**
 * 
 */
@Service("merchantOrderOfflineService")
public class MerchantOrderOfflineServiceImpl implements MerchantOrderOfflineService {

    @Autowired
    private MerchantOrderOfflineRepository merchantOrderOfflineRepository;

	@Override
	public List<MerchantOrderOffline> findOfflineAll(MerchantOrderOffline offline) {
		List<MerchantOrderOffline> merchantOrderOfflines=merchantOrderOfflineRepository.findAllByPage(offline);
		return merchantOrderOfflines;
	}

	@Override
	public int findOfflineAllCount(MerchantOrderOffline merchantOrderOffline) {
		int merchantOrderOfflineList = merchantOrderOfflineRepository.findQueryCount(merchantOrderOffline);
		return merchantOrderOfflineList;
	}

	@Override
	public boolean addOrderOffline(MerchantOrderOffline merchantOrderOffline) {
		try{
			merchantOrderOfflineRepository.addOrderOffline(merchantOrderOffline);
			return true;
		}catch(Exception e){
			return false;
		}
	}

	@Override
	public MerchantOrderOffline findByMerchantOrderId(String addMerchantOrderId) {
		MerchantOrderOffline offline=merchantOrderOfflineRepository.findByMerchantOrderId(addMerchantOrderId);
		return offline;
	}

	@Override
	public List<MerchantOrderOffline> findOfflineAllNoPage(MerchantOrderOffline offline) {
		List<MerchantOrderOffline> merchantOrderOfflines=merchantOrderOfflineRepository.findAllNoPage(offline);
		return merchantOrderOfflines;
	}
}