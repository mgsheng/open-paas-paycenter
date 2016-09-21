package cn.com.open.pay.platform.manager.infrastructure.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import cn.com.open.pay.platform.manager.privilege.model.PrivilegeRole;

/**
 * 
 */
public interface PrivilegeRoleRepository extends Repository {
	/**
	 * 查询privilege_role表中所有角色
	 * @return
	 */
	public List<PrivilegeRole> findRoleAll();
	
	List<PrivilegeRole> findByRoleName(PrivilegeRole privilegeRole);

	int findQueryCount(PrivilegeRole privilegeRole);

	List<PrivilegeRole> findByRoleName(String name);

	void savePrivilegeRole(PrivilegeRole privilegeRole);

	List<PrivilegeRole> findByName(String name);

	void deletePrivilegeRole(int parseInt);

	void updatePrivilegeRole(PrivilegeRole privilegeRole);
}