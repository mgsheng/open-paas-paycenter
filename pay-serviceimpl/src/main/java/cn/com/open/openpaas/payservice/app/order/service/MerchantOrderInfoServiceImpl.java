package cn.com.open.openpaas.payservice.app.order.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.com.open.openpaas.payservice.app.infrastructure.repository.MerchantOrderInfoRepository;
import cn.com.open.openpaas.payservice.app.order.model.MerchantOrderInfo;
import cn.com.open.openpaas.payservice.app.record.model.UserSerialRecord;

/**
 * 
 */
@Service("merchantOrderInfoService")
public class MerchantOrderInfoServiceImpl implements MerchantOrderInfoService {

    @Autowired
    private MerchantOrderInfoRepository merchantOrderInfoRepository;

	public MerchantOrderInfo findByMerchantOrderId(String outTradeNo,String appId) {
		MerchantOrderInfo merchantOrderInfo = merchantOrderInfoRepository.findByMidAndAppId(outTradeNo,appId);
		return merchantOrderInfo;
	}

	public void saveMerchantOrderInfo(MerchantOrderInfo merchantOrderInfo) {
		merchantOrderInfoRepository.saveMerchantOrderInfo(merchantOrderInfo);
	}

	@Override
	public MerchantOrderInfo findById(String orderId) {
		MerchantOrderInfo merchantOrderInfo = merchantOrderInfoRepository.findById(orderId);
		return merchantOrderInfo;
	}

	@Override
	public void updateOrderInfo(MerchantOrderInfo merchantOrderInfo) {
		merchantOrderInfoRepository.updateOrderInfo(merchantOrderInfo);
	}

	@Override
	public void updateOrder(MerchantOrderInfo merchantOrderInfo) {
		merchantOrderInfoRepository.updateOrder(merchantOrderInfo);
		
	}

	@Override
	public void updateNotifyTimes(Integer notifyTimes, String id) {
		merchantOrderInfoRepository.updateNotifyTimes(notifyTimes, id);
		
	}

	@Override
	public List<MerchantOrderInfo> findByPayAndNotifyStatus() {
		List<MerchantOrderInfo> merchantOrderInfos=merchantOrderInfoRepository.findByPayAndNotifyStatus();
		return merchantOrderInfos;
	}

	@Override
	public void updateNotifyStatus(MerchantOrderInfo orderInfo) {
		merchantOrderInfoRepository.updateNotifyStatus(orderInfo);
	}

	@Override
	public void updateOrderId(MerchantOrderInfo merchantOrderInfo) {
		merchantOrderInfoRepository.updateOrderId(merchantOrderInfo);
	}

	@Override
	public MerchantOrderInfo findByMerchantOrderId(String outTradeNo) {
			MerchantOrderInfo merchantOrderInfo = merchantOrderInfoRepository.findByMerchantOrderId(outTradeNo);
			return merchantOrderInfo;
		}

	@Override
	public void updatePayStatus(Integer payStatus, String id) {
		merchantOrderInfoRepository.updatePayStatus(payStatus, id);
		
	}

	@Override
	public List<MerchantOrderInfo> findOrderByTime(String startTime, String endTime,
			String appId) {
		return merchantOrderInfoRepository.findOrderByTime(startTime, endTime, appId);
	}

	@Override
	public void updateSourceType(Integer sourceType, String id) {
		merchantOrderInfoRepository.updateSourceType(sourceType, id);
		
	}
	
	@Override
	public void updatePayWay(MerchantOrderInfo merchantOrderInfo) {
		merchantOrderInfoRepository.updatePayWay(merchantOrderInfo);
		
	}

	@Override
	public HashMap<String, Object> getTotalAmountByTime(String startTime,
			String endTime, String appId) {
		return merchantOrderInfoRepository.getTotalAmountByTime(startTime, endTime, appId);
	}


}