package cn.com.open.pay.platform.manager.paychannel.service;

import java.util.List;

import cn.com.open.pay.platform.manager.department.model.DictTradeChannel;
import cn.com.open.pay.platform.manager.department.model.MerchantInfo;
import cn.com.open.pay.platform.manager.paychannel.model.ChannelRate;
/**
 * 渠道费率管理
 * @author lvjq
 *
 */
public interface PayChannelRateService {
	
	/**
	 * 查询所有商户名称，商户号
	 * @return
	 */
	public List<MerchantInfo> findMerchantNamesAll();
	
	/**
	 * 查询所有支付渠道名称
	 * @return
	 */
	public List<DictTradeChannel> findPayChannelNamesAll();
	
	/**
	 * 根据id删除目标渠道费率记录
	 * @param rate
	 * @return
	 */
	public boolean removeChannelRate(ChannelRate rate);
	
	/**
	 *  根据条件，查询所有符合要求的费率情况
	 * @param rate
	 * @return
	 */
	public List<ChannelRate> findRateAll(ChannelRate rate);
	
	/**
	 *  根据条件，查询所有符合要求的费率情况的数目
	 * @param rate
	 * @return
	 */
	public int findRateAllCount(ChannelRate rate);
	
	/**
	 * 修改费率
	 * @param rate
	 * @return
	 */
	public boolean updateRate(ChannelRate rate);
	
}
