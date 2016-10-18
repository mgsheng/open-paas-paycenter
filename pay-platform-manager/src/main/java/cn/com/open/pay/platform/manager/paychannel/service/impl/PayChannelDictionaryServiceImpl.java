package cn.com.open.pay.platform.manager.paychannel.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.com.open.pay.platform.manager.infrastructure.repository.PayChannelDictionaryRepository;
import cn.com.open.pay.platform.manager.paychannel.model.PayChannelDictionary;
import cn.com.open.pay.platform.manager.paychannel.service.PayChannelDictionaryService;

/**
 * 支付渠道字典（渠道编码）
 * @author lvjq
 *
 */
@Service("PayChannelDictionaryService")
public class PayChannelDictionaryServiceImpl implements PayChannelDictionaryService{

	@Autowired
	private PayChannelDictionaryRepository payChannelDictionaryRepository;
	
	@Override
	public PayChannelDictionary findNameById(String channelId) {
		PayChannelDictionary channel = payChannelDictionaryRepository.findNameById(channelId);
		return channel;
	}

	@Override
	public List<PayChannelDictionary> findPayChannelCodeAll() {
		// TODO Auto-generated method stub
		return payChannelDictionaryRepository.findPayChannelCodeAll();
	}

}
