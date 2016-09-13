package cn.com.open.pay.platform.manager.privilege.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.com.open.pay.platform.manager.infrastructure.repository.PrivilegeRoleDetailsRepository;
import cn.com.open.pay.platform.manager.privilege.model.PrivilegeRoleDetails;
import cn.com.open.pay.platform.manager.privilege.service.PrivilegeRoleDetailsService;

@Service("privilegeRoleDetailsService")
public class PrivilegeRoleDetailsServiceImpl implements PrivilegeRoleDetailsService {
   
	
    @Autowired
	 private PrivilegeRoleDetailsRepository privilegeRoleDetailsRepository;
	 
	@Override
	public void savePrivilegeRole(PrivilegeRoleDetails privilegeRoleDetails) {
		// TODO Auto-generated method stub
		privilegeRoleDetailsRepository.savePrivilegeRoleDetails(privilegeRoleDetails);
		
	}
}