package cn.com.open.pay.platform.manager.infrastructure.repository;
import java.util.List;

import org.springframework.stereotype.Repository;

import cn.com.open.pay.platform.manager.paychannel.model.Rate;

/**
 * 渠道费率管理
 * @author lvjq
 *
 */
public interface PayChannelRateRepository extends Repository{

	/**
	 *  根据条件，查询所有符合要求的费率情况
	 * @param rate
	 * @return
	 */
	public List<Rate> findRateAll(Rate rate);
	
	/**
	 *  根据条件，查询所有符合要求的费率情况的数目
	 * @param rate
	 * @return
	 */
	public int findRateAllCount(Rate rate);
	
	/**
	 * 修改费率
	 * @param rate
	 * @return
	 */
	public void updateRate(Rate rate);
	
}
