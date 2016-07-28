package cn.com.open.openpaas.payservice.app.infrastructure.repository;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import cn.com.open.openpaas.payservice.app.order.model.MerchantOrderInfo;


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
	List<MerchantOrderInfo> findByPayAndNotifyStatus();
	void updateNotifyStatus(MerchantOrderInfo orderInfo);
	void updateOrderId(MerchantOrderInfo merchantOrderInfo);
	List<MerchantOrderInfo> findOrderByTime(@Param("startTime")Date startTime, @Param("startTime")Date endTime,@Param("appId")String appId);

}