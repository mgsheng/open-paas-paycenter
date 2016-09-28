package cn.com.open.pay.platform.manager.infrastructure.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import cn.com.open.pay.platform.manager.department.model.DictTradeChannel;


/**
 * 
 */
public interface DictTradeChannelRepository extends Repository {
	
	/**
	 * 查询所有支付渠道名称
	 * @return
	 */
	public List<DictTradeChannel> findPayChannelNamesAll();
	
	/**
	 * 查询 返回list
	 * @return
	 */
	public List<DictTradeChannel> findDepts(DictTradeChannel dictTradeChannel);
	
	/**
	 * 统计
	 * @param deptName
	 * @return
	 */
	public int findQueryCount(DictTradeChannel dictTradeChannel);
	
	/**
	 * 删除
	 * @return
	 */
	public void removeDictTradeID(Integer id);
	
	/**
	 * 添加
	 * @return
	 */
	public void insert(DictTradeChannel dictTradeChannel);
	
	/**
	 * 根据ID修改渠道信息
	 * @return
	 */
	public void updateDictTrade(DictTradeChannel dictTradeChannel);

	
	
}
