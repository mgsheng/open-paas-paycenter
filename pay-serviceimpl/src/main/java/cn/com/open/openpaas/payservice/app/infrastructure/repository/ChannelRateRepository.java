package cn.com.open.openpaas.payservice.app.infrastructure.repository;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import cn.com.open.openpaas.payservice.app.channel.model.ChannelRate;


/**
 * 
 */
public interface ChannelRateRepository extends Repository {

	ChannelRate getChannelRate(@Param("merchantId")String merchantId,@Param("payChannelCode")String payChannelCode);

}