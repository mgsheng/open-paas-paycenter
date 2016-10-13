package cn.com.open.pay.platform.manager.paychannel.service;

import cn.com.open.pay.platform.manager.paychannel.model.PayChannelDictionary;

/**
 * 支付渠道字典（渠道编码）
 * @author lvjq
 *
 */
public interface PayChannelDictionaryService {

	PayChannelDictionary findNameById(String channelId);

}
