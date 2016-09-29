package cn.com.open.pay.platform.manager.infrastructure.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import cn.com.open.pay.platform.manager.paychannel.model.PayChannelDictionary;

/**
 * 支付渠道字典（渠道编码）
 * @author lvjq
 *
 */
public interface PayChannelDictionaryRepository  extends Repository{
	
	/**
	 * 查询所有渠道编码
	 * @return
	 */
	public List<PayChannelDictionary> findPayChannelCodeAll();
	
	/**
	 * 查询渠道编码
	 * @return
	 */
	public List<PayChannelDictionary> findPayChannelCode(PayChannelDictionary payChannelDictionary);
}
