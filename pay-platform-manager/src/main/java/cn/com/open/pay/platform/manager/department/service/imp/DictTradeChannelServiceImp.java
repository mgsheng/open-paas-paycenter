package cn.com.open.pay.platform.manager.department.service.imp;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.com.open.pay.platform.manager.department.model.Department;
import cn.com.open.pay.platform.manager.department.model.DictTradeChannel;
import cn.com.open.pay.platform.manager.department.service.DictTradeChannelService;
import cn.com.open.pay.platform.manager.department.service.ManagerDepartmentService;
import cn.com.open.pay.platform.manager.infrastructure.repository.DictTradeChannelRepository;
import cn.com.open.pay.platform.manager.infrastructure.repository.ManagerDepartmentRepository;
import cn.com.open.pay.platform.manager.infrastructure.repository.UserRepository;
import cn.com.open.pay.platform.manager.login.model.User;
/**
 * 部门管理
 * @author 
 *
 */
@Service("DictTradeChannelService")
public class DictTradeChannelServiceImp implements  DictTradeChannelService{
//	@Autowired
//	private ManagerDepartmentRepository managerDepartmentRepository;
	@Autowired
	private DictTradeChannelRepository dictTradeChannelRepository;
	@Autowired
	private UserRepository UserRepository;
	
	
	/**
	 * 根据封装的Department对象中的属性查询符合条件的Department集合
	 * @return
	 */
	@Override
	public List<DictTradeChannel> findDepts(DictTradeChannel dictTradeChannel) {
		return dictTradeChannelRepository.findDepts(dictTradeChannel);
	}
	
	/**
	 * 统计
	 * @param DictTradeChannel
	 * @return
	 */
	@Override
	public int findQueryCount(DictTradeChannel dictTradeChannel) {
		return dictTradeChannelRepository.findQueryCount(dictTradeChannel);
	}
	
	/**
	 * 删除
	 * @return
	 */
	@Override
	public boolean removeDictTradeID(Integer id){
		try{
			dictTradeChannelRepository.removeDictTradeID(id);
			return true;
		}catch(Exception e){
			return false;
		}
	}
	/**
	 * 添加
	 * @return
	 */
	@Override
	public boolean addDictTrade(DictTradeChannel dictTradeChannel){
		try{
			dictTradeChannelRepository.insert(dictTradeChannel);
			return true;
		}catch(Exception e){
			return false;
		}
	}
	
	
	/**
	 * 根据ID修改部门信息
	 * @return
	 */
	@Override
	public boolean updateDictTrade(DictTradeChannel dictTradeChannel){
		try{
			dictTradeChannelRepository.updateDictTrade(dictTradeChannel);
			return true;
		}catch(Exception e){
			return false;
		}
	}
	
	
	
}
