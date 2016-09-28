package cn.com.open.pay.platform.manager.infrastructure.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import cn.com.open.pay.platform.manager.department.model.Department;
import cn.com.open.pay.platform.manager.department.model.DictTradeChannel;
import cn.com.open.pay.platform.manager.order.model.MerchantOrderInfo;

//import cn.com.open.openpaas.payservice.app.order.model.MerchantOrderInfo;


/**
 * 
 */
public interface DictTradeChannelRepository extends Repository {
	
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
	 * 查询所有部门
	 * @return
	 */
	public List<Department> findAllDepts();
	
	
	/**
	 * 根据ID修改部门信息
	 * @return
	 */
	public void updateDept(Department department);
	
	
	
	
	
	/**
	 * 根据部门名查询Department对象
	 * @return
	 */
	public Department findByDeptName(String deptName);
	
	
}
