package cn.com.open.pay.platform.manager.infrastructure.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import cn.com.open.pay.platform.manager.department.model.Department;
import cn.com.open.pay.platform.manager.department.model.MerchantInfo;
import cn.com.open.pay.platform.manager.log.model.PrivilegeLog;

//import cn.com.open.openpaas.payservice.app.order.model.MerchantOrderInfo;


/**
 * 
 */
public interface PrivilegeLogRepository extends Repository {
	
	
	
	/**
	 *查询list
	 * @return
	 */
	public List<PrivilegeLog> findDepts(PrivilegeLog privilegeLog);

	
	/**
	 * 统计
	 * @param deptName
	 * @return
	 */
	public int findQueryCount(PrivilegeLog privilegeLog);
	
//	/**
//	 * 根据商户id 删除
//	 * @return
//	 */
//	public void removeCommercialID(Integer id);
	/**
	 * 添加
	 * @return
	 */
	public void insert(PrivilegeLog privilegeLog);
//	
//	/**
//	 * 根据ID修改部门信息
//	 * @return
//	 */
//	public void updateMerchantInfo(MerchantInfo merchantInfo);
//	
	
	
}
