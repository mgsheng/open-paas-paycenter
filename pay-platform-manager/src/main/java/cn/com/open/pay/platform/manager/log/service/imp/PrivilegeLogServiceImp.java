package cn.com.open.pay.platform.manager.log.service.imp;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.com.open.pay.platform.manager.department.model.Department;
import cn.com.open.pay.platform.manager.department.model.MerchantInfo;
import cn.com.open.pay.platform.manager.department.service.ManagerDepartmentService;
import cn.com.open.pay.platform.manager.department.service.MerchantInfoService;
import cn.com.open.pay.platform.manager.infrastructure.repository.ManagerDepartmentRepository;
import cn.com.open.pay.platform.manager.infrastructure.repository.MerchantInfoRepository;
import cn.com.open.pay.platform.manager.infrastructure.repository.PrivilegeLogRepository;
import cn.com.open.pay.platform.manager.infrastructure.repository.UserRepository;
import cn.com.open.pay.platform.manager.log.model.PrivilegeLog;
import cn.com.open.pay.platform.manager.log.service.PrivilegeLogService;
import cn.com.open.pay.platform.manager.login.model.User;
/**
 * 日志管理
 * @author 
 *
 */
@Service("PrivilegeLogService")
public class PrivilegeLogServiceImp implements  PrivilegeLogService{
//	@Autowired
//	private ManagerDepartmentRepository managerDepartmentRepository;
	
	@Autowired
	private PrivilegeLogRepository privilegeLogRepository;
	
	
	/**
	 * 查询list
	 * @return
	 */
	@Override
	public List<PrivilegeLog> findDepts(PrivilegeLog privilegeLog) {
		return privilegeLogRepository.findDepts(privilegeLog);
	}
	
	/**
	 * 统计
	 * @param deptName
	 * @return
	 */
	@Override
	public int findQueryCount(PrivilegeLog privilegeLog) {
		return privilegeLogRepository.findQueryCount(privilegeLog);
	}

	@Override
	public void addPrivilegeLog(String operator, String operationContent,
			String oneLevels, String towLevels, String operationAuthority,
			String operationDescribe, String operatorId) {
		// TODO Auto-generated method stub
		Date createTime = new Date();
		PrivilegeLog privilegeLog=new PrivilegeLog();
		privilegeLog.setOperator(operator);
		privilegeLog.setOperationContent(operationContent);
		privilegeLog.setOneLevels(oneLevels);
		privilegeLog.setTowLevels(towLevels);
		if(operationAuthority!=""){
			privilegeLog.setOperationAuthority(Integer.parseInt(operationAuthority));	
		}
		
		privilegeLog.setOperationTime(createTime);
		privilegeLog.setOperationDescribe(operationDescribe);
		privilegeLog.setOperatorId(operatorId);
		privilegeLogRepository.insert(privilegeLog);
		
	}

	
	
	
}
