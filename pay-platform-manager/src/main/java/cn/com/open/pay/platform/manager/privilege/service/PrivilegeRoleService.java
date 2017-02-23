package cn.com.open.pay.platform.manager.privilege.service;

import java.util.List;


import cn.com.open.pay.platform.manager.privilege.model.PrivilegeRole;


public interface PrivilegeRoleService {
   
	

	List<PrivilegeRole> findByRoleName(PrivilegeRole privilegeRole);
	
	int findQueryCount(PrivilegeRole privilegeRole);

	List<PrivilegeRole> findByName(String name);

	void savePrivilegeRole(PrivilegeRole privilegeRole);

	void deletePrivilegeRole(int parseInt);

	void updatePrivilegeRole(PrivilegeRole privilegeRole);
	
	List<PrivilegeRole> findByRoleIds(List<Integer> roleIds);
    
}