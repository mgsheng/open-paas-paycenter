package cn.com.open.openpaas.payservice.app.channel.service;

import cn.com.open.openpaas.payservice.app.channel.model.ChannelRate;

/**
 * 
 */
public interface ChannelRateService {
	
	ChannelRate  getChannelRate(String merid,String payChannelCode);
	ChannelRate  getChannelRate(String channelId,Integer sourceType);
	
}