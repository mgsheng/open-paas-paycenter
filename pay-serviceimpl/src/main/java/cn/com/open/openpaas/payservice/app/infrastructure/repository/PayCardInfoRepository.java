package cn.com.open.openpaas.payservice.app.infrastructure.repository;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import cn.com.open.openpaas.payservice.app.order.model.PayCardInfo;
import cn.com.open.openpaas.payservice.app.order.model.PayOrderDetail;


/**
 * 
 */
public interface PayCardInfoRepository extends Repository {

	PayCardInfo findByIdentityId (@Param("identityId")String identityId,@Param("appId")String appId);
	void insert(PayCardInfo payCardInfo);
	void savePayCardInfo(PayCardInfo payCardInfo);
	
}