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
	public List<MerchantOrderOffline> findOfflineAll() {
		List<MerchantOrderOffline> merchantOrderOfflines=merchantOrderOfflineRepository.findAll();
		return merchantOrderOfflines;
	}

	@Override
	public int findOfflineAllCount(MerchantOrderOffline merchantOrderOffline) {
		int merchantOrderOfflineList = merchantOrderOfflineRepository.findQueryCount(merchantOrderOffline);
		return merchantOrderOfflineList;
	}


}