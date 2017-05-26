package cn.com.open.openpaas.payservice.app.infrastructure.repository;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import cn.com.open.openpaas.payservice.app.order.model.PayCardInfo;
import cn.com.open.openpaas.payservice.app.order.model.PayOrderDetail;


/**
 * 
 */
public interface PayCardInfoRepository extends Repository {

	void insert(PayCardInfo payCardInfo);
	void savePayCardInfo(PayCardInfo payCardInfo);
	PayCardInfo getCardInfo (@Param("userId")String userId,@Param("appId")String appId);
	void updateCardInfo(PayCardInfo payCardInfo);
	void updateCardStatus(@Param("status")Integer status,@Param("id")Integer id);
	
}