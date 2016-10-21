package cn.com.open.pay.platform.manager.infrastructure.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import cn.com.open.pay.platform.manager.order.model.MerchantOrderInfo;

//import cn.com.open.openpaas.payservice.app.order.model.MerchantOrderInfo;


/**
 * 
 */
public interface MerchantOrderInfoRepository extends Repository {

	MerchantOrderInfo findByMerchantOrderId (String merchantOrderId);
	MerchantOrderInfo findByMidAndAppId (@Param("merchantOrderId")String merchantOrderId,@Param("appId")String appId);
	void saveMerchantOrderInfo(MerchantOrderInfo merchantOrderInfo);
	MerchantOrderInfo findById(String orderId);
	void updateOrderInfo(MerchantOrderInfo merchantOrderInfo);
	//更新订单状态处理，实收金额计算，手续费
	void updateOrder(MerchantOrderInfo merchantOrderInfo);
	//更新订单状态处理，实收金额计算，手续费
	void updateNotifyTimes(@Param("notifyTimes")Integer notifyTimes,@Param("id")String id);
	void updatePayStatus(@Param("payStatus")Integer payStatus,@Param("id")String id);
	void updateSourceType(@Param("sourceType")Integer sourceType, @Param("id")String id);
	List<MerchantOrderInfo> findByPayAndNotifyStatus();
	void updateNotifyStatus(MerchantOrderInfo orderInfo);
	void updateOrderId(MerchantOrderInfo merchantOrderInfo);
	void updatePayWay(MerchantOrderInfo merchantOrderInfo);
	List<MerchantOrderInfo> findOrderByTime(@Param("startTime")String startTime, @Param("endTime")String endTime,@Param("appId")String appId);
	HashMap<String, Object> getTotalAmountByTime(@Param("startTime")String startTime, @Param("endTime")String endTime,@Param("appId")String appId);
	
	List<Map<String, Object>> getPayCount(@Param("startTime")String startTime, @Param("endTime")String endTime,@Param("merchantId")String merchantId,@Param("paymentId")String paymentId,@Param("channelId")String channelId);
	List<Map<String, Object>> getPayAmount(@Param("startTime")String startTime, @Param("endTime")String endTime,@Param("merchantId")String merchantId,@Param("paymentId")String paymentId,@Param("channelId")String channelId);
	List<Map<String, Object>> getUserCount(@Param("startTime")String startTime, @Param("endTime")String endTime,@Param("merchantId")String merchantId,@Param("paymentId")String paymentId,@Param("channelId")String channelId);
	List<Map<String, Object>> payCharge(@Param("startTime")String startTime, @Param("endTime")String endTime,@Param("merchantId")String merchantId,@Param("paymentId")String paymentId,@Param("channelId")String channelId);
	
	HashMap<String, Object> getTotalPayCount(@Param("startTime")String startTime, @Param("endTime")String endTime,@Param("merchantId")String merchantId,@Param("paymentId")String paymentId,@Param("channelId")String channelId);
	HashMap<String, Object> getTotalPayAmount(@Param("startTime")String startTime, @Param("endTime")String endTime,@Param("merchantId")String merchantId,@Param("paymentId")String paymentId,@Param("channelId")String channelId);
	HashMap<String, Object> getTotalUserCount(@Param("startTime")String startTime, @Param("endTime")String endTime,@Param("merchantId")String merchantId,@Param("paymentId")String paymentId,@Param("channelId")String channelId);
	HashMap<String, Object> payTotalCharge(@Param("startTime")String startTime, @Param("endTime")String endTime,@Param("merchantId")String merchantId,@Param("paymentId")String paymentId,@Param("channelId")String channelId);
	
	
	List<MerchantOrderInfo> findQueryMerchant(
			MerchantOrderInfo merchantOrderInfo);
	List<MerchantOrderInfo> findDownloadMerchant(
			MerchantOrderInfo merchantOrderInfo);
	
	int findQueryCount(MerchantOrderInfo merchantOrderInfo);
	List<MerchantOrderInfo> getCensusAll(MerchantOrderInfo merchantOrderInfo);
	List<MerchantOrderInfo> getAllCount(MerchantOrderInfo merchantOrderInfo);
	List<MerchantOrderInfo> getCensusAllExport(
			MerchantOrderInfo merchantOrderInfo);
	
}