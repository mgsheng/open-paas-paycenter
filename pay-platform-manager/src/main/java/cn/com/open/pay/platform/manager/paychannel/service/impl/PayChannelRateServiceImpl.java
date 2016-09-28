package cn.com.open.pay.platform.manager.paychannel.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.com.open.pay.platform.manager.infrastructure.repository.PayChannelRateRepository;
import cn.com.open.pay.platform.manager.paychannel.model.ChannelRate;
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
