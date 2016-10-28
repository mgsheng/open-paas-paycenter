package cn.com.open.openpaas.payservice.app.channel.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.com.open.openpaas.payservice.app.channel.model.ChannelRate;
import cn.com.open.openpaas.payservice.app.infrastructure.repository.ChannelRateRepository;

/**
 * 
 */
@Service("channelRateService")
public class ChannelRateServiceImpl implements ChannelRateService {

    @Autowired
    private ChannelRateRepository channelRateRepository;

	@Override
	public ChannelRate getChannelRate(String merid, String payChannelCode) {
		// TODO Auto-generated method stub
		return channelRateRepository.getChannelRate(merid, payChannelCode);
	}

	@Override
	public ChannelRate getChannelRate(String channelId, Integer sourceType) {
		// TODO Auto-generated method stub
		return channelRateRepository.getRateByChannel(channelId, sourceType);
	}



}