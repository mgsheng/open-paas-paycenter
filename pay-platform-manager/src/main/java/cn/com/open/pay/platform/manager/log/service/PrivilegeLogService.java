package cn.com.open.pay.platform.manager.log.service;

import java.util.List;

import cn.com.open.pay.platform.manager.department.model.Department;
import cn.com.open.pay.platform.manager.department.model.MerchantInfo;
import cn.com.open.pay.platform.manager.log.model.PrivilegeLog;
import cn.com.open.pay.platform.manager.login.model.User;
/**
 * 日志管理
 * @author 
 *
 */
public interface PrivilegeLogService {
	
	
	/**
	 * 查询list
	 * @return
	 */
	public List<PrivilegeLog> findDepts(PrivilegeLog privilegeLog);
	
	/**
	 * 统计
	 * @param deptName
	 * @return
	 */
	public int findQueryCount(PrivilegeLog privilegeLog);

	/**
	 * 添加日志
	 * @param operator 操作人
	 * @param operationContent 操作
	 * @param oneLevels 一级操作
	 * @param towLevels 二级操作
	 * @param operationAuthority 操作权限
	 * @param operationDescribe 操作描述
	 * @param operatorId 操作人Id
	 */
	public void addPrivilegeLog(String operator, String operationContent,
			String oneLevels, String towLevels, String operationAuthority,
			String operationDescribe, String operatorId);

	
	
//	/**
//	 * 添加
//	 * @return
//	 */
//	public boolean addMerchantInfo(MerchantInfo merchantInfo);
//	
//	/**
//	 * 根据商户id 删除
//	 * @return
//	 */
//	public boolean removeCommercialID(Integer id);
//	
//	/**
//	 * 根据ID修改部门信息
//	 * @return
//	 */
//	public boolean updateMerchantInfo(MerchantInfo merchantInfo);
//	
	
	
}
