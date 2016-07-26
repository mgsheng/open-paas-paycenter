package cn.com.open.openpaas.payservice.app.infrastructure.repository;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import cn.com.open.openpaas.payservice.app.channel.model.DictTradeChannel;


/**
 * 
 */
public interface DictTradeChannelRepository extends Repository {

	List<DictTradeChannel> findByMerId(Integer merchantId);
	DictTradeChannel findByMAI(@Param("merid")String merid,@Param("paymentChannel")Integer id);

}