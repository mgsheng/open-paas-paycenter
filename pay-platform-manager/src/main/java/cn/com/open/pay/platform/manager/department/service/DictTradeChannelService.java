package cn.com.open.pay.platform.manager.department.service;

import java.util.List;

import cn.com.open.pay.platform.manager.department.model.Department;
import cn.com.open.pay.platform.manager.department.model.DictTradeChannel;
import cn.com.open.pay.platform.manager.login.model.User;
/**
 * 渠道管理
 * @author 
 *
 */
public interface DictTradeChannelService {
	
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
	public boolean removeDictTradeID(Integer id);
	
	/**
	 * 添加
	 * @return
	 */
	public boolean addDictTrade(DictTradeChannel dictTradeChannel);
	
	/**
	 * 根据ID修改渠道信息
	 * @return
	 */
	public boolean updateDictTrade(DictTradeChannel dictTradeChannel);
	
	
	
}
