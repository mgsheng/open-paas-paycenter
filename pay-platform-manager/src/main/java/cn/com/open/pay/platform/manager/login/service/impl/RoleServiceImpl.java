package cn.com.open.pay.platform.manager.login.service.impl;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.com.open.pay.platform.manager.infrastructure.repository.PrivilegeRoleRepository;
import cn.com.open.pay.platform.manager.login.service.RoleService;
import cn.com.open.pay.platform.manager.privilege.model.PrivilegeRole;

/**
 * 
 */
@Service("RoleService")
public class RoleServiceImpl implements RoleService {
    
    @Autowired
    private PrivilegeRoleRepository privilegeRoleRepository;
  
	@Override
	public List<PrivilegeRole> findByRoleName(PrivilegeRole privilegeRole) {
		// TODO Auto-generated method stub
		return privilegeRoleRepository.findByRoleName(privilegeRole);
	}
	@Override
	public int findQueryCount(PrivilegeRole privilegeRole) {
		int countNum = privilegeRoleRepository.findQueryCount(privilegeRole);
		return countNum;
	}
	@Override
	public List<PrivilegeRole> findByName(String name) {
		// TODO Auto-generated method stub
		List<PrivilegeRole>  privilegeRoleList = privilegeRoleRepository.findByName(name);
		return privilegeRoleList;
	}
	@Override
	public void savePrivilegeRole(PrivilegeRole privilegeRole) {
		// TODO Auto-generated method stub
		privilegeRoleRepository.savePrivilegeRole(privilegeRole);
		
	}
	@Override
	public void deletePrivilegeRole(int parseInt) {
		// TODO Auto-generated method stub
		privilegeRoleRepository.deletePrivilegeRole(parseInt);
	}
	@Override
	public void updatePrivilegeRole(PrivilegeRole privilegeRole) {
		// TODO Auto-generated method stub
		privilegeRoleRepository.updatePrivilegeRole(privilegeRole);
		
	}
	


  
}