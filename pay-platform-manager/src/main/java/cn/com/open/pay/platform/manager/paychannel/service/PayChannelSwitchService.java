package cn.com.open.pay.platform.manager.paychannel.service;

import java.util.List;

import cn.com.open.pay.platform.manager.paychannel.model.PayChannelDictionary;
import cn.com.open.pay.platform.manager.paychannel.model.PayChannelSwitch;

/**
 * 支付渠道字典（渠道编码）
 * @author lvjq
 *
 */
public interface PayChannelSwitchService {


	List<PayChannelSwitch> findPayChannelTypeAll();

	PayChannelSwitch findNameById(String channelId);

}
