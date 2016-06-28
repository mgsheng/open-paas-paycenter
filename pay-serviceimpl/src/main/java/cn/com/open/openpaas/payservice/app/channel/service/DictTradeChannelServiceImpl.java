package cn.com.open.openpaas.payservice.app.channel.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.com.open.openpaas.payservice.app.channel.model.DictTradeChannel;
import cn.com.open.openpaas.payservice.app.infrastructure.repository.DictTradeChannelRepository;

/**
 * 
 */
@Service("dictTradeChannelService")
public class DictTradeChannelServiceImpl implements DictTradeChannelService {

    @Autowired
    private DictTradeChannelRepository dictTradeChannelRepository;

	@Override
	public List<DictTradeChannel> findByMerId(Integer merchantId) {
		List<DictTradeChannel> dictTradeChannels = dictTradeChannelRepository.findByMerId(merchantId);
		return dictTradeChannels;
	}

	@Override
	public DictTradeChannel findByMAI(String merchantId, Integer id) {
		return dictTradeChannelRepository.findByMAI(merchantId, id);
	}


}