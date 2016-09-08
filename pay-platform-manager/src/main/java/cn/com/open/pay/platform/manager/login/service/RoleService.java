package cn.com.open.pay.platform.manager.login.service;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.com.open.pay.platform.manager.login.model.User;
import cn.com.open.pay.platform.manager.privilege.model.PrivilegeRole;



/**
 * 
 */
public interface RoleService {
	
	List<PrivilegeRole> findByRoleName(PrivilegeRole privilegeRole);
	
	int findQueryCount(PrivilegeRole privilegeRole);

	List<PrivilegeRole> findByName(String name);

	void savePrivilegeRole(PrivilegeRole privilegeRole);

	void deletePrivilegeRole(int parseInt);

	void updatePrivilegeRole(PrivilegeRole privilegeRole);

	

	

	
}