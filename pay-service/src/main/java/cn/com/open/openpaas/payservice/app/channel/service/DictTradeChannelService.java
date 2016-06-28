package cn.com.open.openpaas.payservice.app.channel.service;

import java.util.List;

import cn.com.open.openpaas.payservice.app.channel.model.DictTradeChannel;

/**
 * 
 */
public interface DictTradeChannelService {

	List<DictTradeChannel> findByMerId(Integer marchantId);
	DictTradeChannel findByMAI(String merid,Integer id);
	
}