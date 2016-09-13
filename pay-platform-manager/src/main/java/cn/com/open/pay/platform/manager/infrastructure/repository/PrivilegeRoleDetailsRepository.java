package cn.com.open.pay.platform.manager.infrastructure.repository;

import org.springframework.stereotype.Repository;

import cn.com.open.pay.platform.manager.privilege.model.PrivilegeRoleDetails;

/**
 * 
 */
public interface PrivilegeRoleDetailsRepository extends Repository {

	void savePrivilegeRoleDetails(PrivilegeRoleDetails privilegeRoleDetails);
}