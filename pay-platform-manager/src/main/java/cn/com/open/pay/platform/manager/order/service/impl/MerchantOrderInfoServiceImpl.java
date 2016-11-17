package cn.com.open.pay.platform.manager.order.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

//import cn.com.open.openpaas.payservice.app.infrastructure.repository.MerchantOrderInfoRepository;
//import cn.com.open.openpaas.payservice.app.order.model.MerchantOrderInfo;
//import cn.com.open.openpaas.payservice.app.record.model.UserSerialRecord;
import cn.com.open.pay.platform.manager.infrastructure.repository.MerchantOrderInfoRepository;
import cn.com.open.pay.platform.manager.order.model.MerchantOrderInfo;
import cn.com.open.pay.platform.manager.order.service.MerchantOrderInfoService;

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

	@Override
	public List<MerchantOrderInfo> findQueryMerchant(
			MerchantOrderInfo merchantOrderInfo) {
		List<MerchantOrderInfo> merchantOrderInfoList = merchantOrderInfoRepository.findQueryMerchant(merchantOrderInfo);
		return merchantOrderInfoList;
	}
	
	@Override
	public List<MerchantOrderInfo> findDownloadMerchant(
			MerchantOrderInfo merchantOrderInfo) {
		List<MerchantOrderInfo> merchantOrderInfoList = merchantOrderInfoRepository.findDownloadMerchant(merchantOrderInfo);
		return merchantOrderInfoList;
	}
	

	@Override
	public List<Map<String, Object>> getPayCount(String startTime,
			String endTime, String appId, String paymentId, String channelId,String sourceType) {
		// TODO Auto-generated method stub
		return merchantOrderInfoRepository.getPayCount(startTime, endTime, appId, paymentId, channelId,sourceType);
	}

	@Override
	public List<Map<String, Object>> getPayAmount(String startTime,
			String endTime, String appId, String paymentId, String channelId,String sourceType) {
		// TODO Auto-generated method stub
		return merchantOrderInfoRepository.getPayAmount(startTime, endTime, appId, paymentId, channelId,sourceType);
	}

	@Override
	public List<Map<String, Object>> getUserCount(String startTime,
			String endTime, String appId, String paymentId, String channelId,String sourceType) {
		// TODO Auto-generated method stub
		return merchantOrderInfoRepository.getUserCount(startTime, endTime, appId, paymentId, channelId,sourceType);
	}

	@Override
	public List<Map<String, Object>> payCharge(String startTime, String endTime,
			String appId, String paymentId, String channelId,String sourceType) {
		// TODO Auto-generated method stub
		return merchantOrderInfoRepository.payCharge(startTime, endTime, appId, paymentId, channelId,sourceType);
	}

	@Override
	public HashMap<String, Object> getTotalPayCount(String startTime,
			String endTime, String appId, String paymentId, String channelId,String sourceType) {
		// TODO Auto-generated method stub
		return merchantOrderInfoRepository.getTotalPayCount(startTime, endTime, appId, paymentId, channelId,sourceType);
	}

	@Override
	public HashMap<String, Object> getTotalPayAmount(String startTime,
			String endTime, String appId, String paymentId, String channelId,String sourceType) {
		// TODO Auto-generated method stub
		return merchantOrderInfoRepository.getTotalPayAmount(startTime, endTime, appId, paymentId, channelId,sourceType);
	}

	@Override
	public HashMap<String, Object> getTotalUserCount(String startTime,
			String endTime, String appId, String paymentId, String channelId,String sourceType) {
		// TODO Auto-generated method stub
		return merchantOrderInfoRepository.getTotalUserCount(startTime, endTime, appId, paymentId, channelId,sourceType);
	}

	@Override
	public HashMap<String, Object> payTotalCharge(String startTime,
			String endTime, String appId, String paymentId, String channelId,String sourceType) {
		// TODO Auto-generated method stub
		return merchantOrderInfoRepository.payTotalCharge(startTime, endTime, appId, paymentId, channelId,sourceType);
	}

	@Override
	public int findQueryCount(MerchantOrderInfo merchantOrderInfo) {
		// TODO Auto-generated method stub
	int merchantOrderInfoList = merchantOrderInfoRepository.findQueryCount(merchantOrderInfo);
	return merchantOrderInfoList;
	
	}

	@Override
	public List<MerchantOrderInfo> getCensusAll(
			MerchantOrderInfo merchantOrderInfo) {
		// TODO Auto-generated method stub
		return merchantOrderInfoRepository.getCensusAll(merchantOrderInfo);
	}

	@Override
	public int getAllCount(MerchantOrderInfo merchantOrderInfo) {
		// TODO Auto-generated method stub
		List<MerchantOrderInfo> merchantOrderInfoList = merchantOrderInfoRepository.getAllCount(merchantOrderInfo);
		return merchantOrderInfoList.size();
	}

	@Override
	public List<MerchantOrderInfo> getCensusAllExport(
			MerchantOrderInfo merchantOrderInfo) {
		// TODO Auto-generated method stub
		return merchantOrderInfoRepository.getCensusAllExport(merchantOrderInfo);
	}

	@Override
	public List<MerchantOrderInfo> findChannelRevenue(MerchantOrderInfo orderInfo) {
		// TODO Auto-generated method stub
		return merchantOrderInfoRepository.findChannelRevenue(orderInfo);
	}

	@Override
	public int findChannelRevenueCount(MerchantOrderInfo orderInfo) {
		List<MerchantOrderInfo> lists=merchantOrderInfoRepository.findChannelRevenueQueryCount(orderInfo);
		if(lists==null){
			return 0;
		}else{
			return lists.size();
		}
	}

	@Override
	public List<MerchantOrderInfo> findChannelRevenueNoPage(
			MerchantOrderInfo orderInfo) {
		List<MerchantOrderInfo> lists=merchantOrderInfoRepository.findChannelRevenueQueryCount(orderInfo);
		return lists;
	}

	@Override
	public List<MerchantOrderInfo> findBankPayment(MerchantOrderInfo orderInfo) {
		// TODO Auto-generated method stub
		return merchantOrderInfoRepository.findBankPayment(orderInfo);
	}

	@Override
	public int findBankPaymentCount(MerchantOrderInfo orderInfo) {
		// TODO Auto-generated method stub
		List<MerchantOrderInfo> lists=merchantOrderInfoRepository.findBankPaymentQueryCount(orderInfo);
		if(lists==null){
			return 0;
		}else{
			return lists.size();
		}
	}

	@Override
	public List<MerchantOrderInfo> findBankPaymentNoPage(
			MerchantOrderInfo orderInfo) {
		// TODO Auto-generated method stub
		List<MerchantOrderInfo> lists=merchantOrderInfoRepository.findBankPaymentQueryCount(orderInfo);
		return lists;
	}

	@Override
	public MerchantOrderInfo findCount(MerchantOrderInfo merchantOrderInfo) {
		MerchantOrderInfo orderInfo=merchantOrderInfoRepository.findCount(merchantOrderInfo);
		return orderInfo;
	}


}