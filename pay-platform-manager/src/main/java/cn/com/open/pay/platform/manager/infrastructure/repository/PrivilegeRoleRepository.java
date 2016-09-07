package cn.com.open.pay.platform.manager.infrastructure.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import cn.com.open.pay.platform.manager.privilege.model.PrivilegeRole;

/**
 * 
 */
public interface PrivilegeRoleRepository extends Repository {

	void addRole(PrivilegeRole privilegeRole);

	List<PrivilegeRole> findByRoleName(PrivilegeRole privilegeRole);

	int findQueryCount(PrivilegeRole privilegeRole);
}