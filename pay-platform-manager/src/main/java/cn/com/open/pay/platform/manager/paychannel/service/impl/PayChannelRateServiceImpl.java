package cn.com.open.pay.platform.manager.paychannel.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.com.open.pay.platform.manager.department.model.DictTradeChannel;
import cn.com.open.pay.platform.manager.department.model.MerchantInfo;
import cn.com.open.pay.platform.manager.infrastructure.repository.DictTradeChannelRepository;
import cn.com.open.pay.platform.manager.infrastructure.repository.MerchantInfoRepository;
import cn.com.open.pay.platform.manager.infrastructure.repository.PayChannelDictionaryRepository;
import cn.com.open.pay.platform.manager.infrastructure.repository.PayChannelRateRepository;
import cn.com.open.pay.platform.manager.paychannel.model.ChannelRate;
import cn.com.open.pay.platform.manager.paychannel.model.PayChannelDictionary;
import cn.com.open.pay.platform.manager.paychannel.service.PayChannelRateService;
/**
 * 渠道费率管理
 * @author lvjq
 *
 */
@Service("PayChannelRateService")
public class PayChannelRateServiceImpl implements PayChannelRateService {
	
	@Autowired
	private PayChannelRateRepository payChannelRateRepository;
	@Autowired
	private DictTradeChannelRepository dictTradeChannelRepository;
	@Autowired
	private MerchantInfoRepository merchantInfoRepository;
	
	@Autowired
	private PayChannelDictionaryRepository payChannelDictionaryRepository;
	
	/**
	 * 查询所有渠道编码，渠道名称
	 * @return
	 */
	public List<PayChannelDictionary> findPayChannelCodeAll(){
		return payChannelDictionaryRepository.findPayChannelCodeAll();
	}
	
	/**
	 * 添加支付渠道费率前，先根据条件查询数据库是否已经存在该记录
	 * @param rate
	 * @return
	 */
	@Override
	public List<ChannelRate> findChannelRate(ChannelRate rate){
		return payChannelRateRepository.findChannelRate(rate);
	}
	
	/**
	 * 添加支付渠道费率
	 * @param rate
	 * @return
	 */
	@Override
	public boolean addPayChannelRate(ChannelRate rate){
		try{
			payChannelRateRepository.addPayChannelRate(rate);
			return true;
		}catch(Exception e){
			return false;
		}
	}
	
	
	/**
	 * 查询渠道编码
	 * @return
	 */
	@Override
	public List<PayChannelDictionary> findPayChannelCode(PayChannelDictionary payChannelDictionary){
		return payChannelDictionaryRepository.findPayChannelCode(payChannelDictionary);
	}
	
	/**
	 * 查询所有商户名称，商户号
	 * @return
	 */
	@Override
	public List<MerchantInfo> findMerchantNamesAll(){
		return merchantInfoRepository.findMerchantNamesAll();
	}
	
	/**
	 * 根据id删除目标渠道费率记录
	 * @param rate
	 * @return
	 */
	@Override
	public boolean removeChannelRate(ChannelRate rate){
		try{
			payChannelRateRepository.removeChannelRate(rate);
			return true;
		}catch(Exception e){
			return false;
		}
	}
	
	/**
	 *  根据条件，查询所有符合要求的费率情况
	 * @param rate
	 * @return
	 */
	@Override
	public List<ChannelRate> findRateAll(ChannelRate rate){
		return payChannelRateRepository.findRateAll(rate);
	}
	
	/**
	 *  根据条件，查询所有符合要求的费率情况的数目
	 * @param rate
	 * @return
	 */
	@Override
	public int findRateAllCount(ChannelRate rate){
		return payChannelRateRepository.findRateAllCount(rate);
	}
	
	/**
	 * 修改费率
	 * @param rate
	 * @return
	 */
	public boolean updateRate(ChannelRate rate){
		try{
			payChannelRateRepository.updateRate(rate);
			return true;
		}catch(Exception e){
			return false;
		}
	}
	
}
