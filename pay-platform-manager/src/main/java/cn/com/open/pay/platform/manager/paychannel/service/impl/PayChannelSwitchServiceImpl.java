package cn.com.open.pay.platform.manager.paychannel.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.com.open.pay.platform.manager.infrastructure.repository.PayChannelDictionaryRepository;
import cn.com.open.pay.platform.manager.infrastructure.repository.PayChannelSwitchRepository;
import cn.com.open.pay.platform.manager.paychannel.model.PayChannelDictionary;
import cn.com.open.pay.platform.manager.paychannel.model.PayChannelSwitch;
import cn.com.open.pay.platform.manager.paychannel.service.PayChannelDictionaryService;
import cn.com.open.pay.platform.manager.paychannel.service.PayChannelSwitchService;

/**
 * 支付渠道字典（渠道编码）
 * @author lvjq
 *
 */
@Service("PayChannelSwitchService")
public class PayChannelSwitchServiceImpl implements PayChannelSwitchService{

	@Autowired
	private PayChannelSwitchRepository payChannelSwitchRepository;
	

	@Override
	public List<PayChannelSwitch> findPayChannelTypeAll() {
		// TODO Auto-generated method stub
		return payChannelSwitchRepository.findPayChannelTypeAll();
	}

}
