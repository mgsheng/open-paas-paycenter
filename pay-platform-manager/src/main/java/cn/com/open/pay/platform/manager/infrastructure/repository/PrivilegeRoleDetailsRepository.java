package cn.com.open.pay.platform.manager.infrastructure.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import cn.com.open.pay.platform.manager.privilege.model.PrivilegeRoleDetails;

/**
 * 
 */
public interface PrivilegeRoleDetailsRepository extends Repository {

	void savePrivilegeRoleDetails(PrivilegeRoleDetails privilegeRoleDetails);
	List<PrivilegeRoleDetails> findRoleDetailsByRoleId(Integer roleId);

	List<PrivilegeRoleDetails> QueryRoleDetails(PrivilegeRoleDetails privilegeRoleDetails);

	void deletePrivilegeRoleDetail(String id);
}