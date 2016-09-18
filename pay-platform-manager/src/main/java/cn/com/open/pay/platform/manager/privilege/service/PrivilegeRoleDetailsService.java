package cn.com.open.pay.platform.manager.privilege.service;

import java.util.List;

import cn.com.open.pay.platform.manager.privilege.model.PrivilegeRoleDetails;


public interface PrivilegeRoleDetailsService {
   
	void savePrivilegeRole(PrivilegeRoleDetails privilegeRoleDetails);
	List<PrivilegeRoleDetails> findRoleDetailsByRoleId(Integer roleId);

	List<PrivilegeRoleDetails> QueryRoleDetails(PrivilegeRoleDetails privilegeRoleDetails);

	void deletePrivilegeRoleDetail(String id);
}