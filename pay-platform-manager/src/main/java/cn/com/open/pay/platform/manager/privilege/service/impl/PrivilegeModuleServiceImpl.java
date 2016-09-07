package cn.com.open.pay.platform.manager.privilege.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.com.open.pay.platform.manager.infrastructure.repository.MerchantOrderInfoRepository;
import cn.com.open.pay.platform.manager.infrastructure.repository.PrivilegeModuleRepository;
import cn.com.open.pay.platform.manager.privilege.model.PrivilegeModule;
import cn.com.open.pay.platform.manager.privilege.service.PrivilegeModuleService;
@Service("privilegeModuleService")
public class PrivilegeModuleServiceImpl implements PrivilegeModuleService {
    @Autowired
	private PrivilegeModuleRepository privilegeModuleRepository;
	@Override
	public List<PrivilegeModule> findByName(String name) {
		return privilegeModuleRepository.findByName(name);
	}
	@Override
	public void savePrivilegeModule(PrivilegeModule privilegeModule) {
		privilegeModuleRepository.savePrivilegeModule(privilegeModule);
	}
	@Override
	public void updatePrivilegeModule(PrivilegeModule privilegeModule) {
		// TODO Auto-generated method stub
		privilegeModuleRepository.updatePrivilegeModule(privilegeModule);
	}
	@Override
	public void deletePrivilegeModule(Integer id) {
		// TODO Auto-generated method stub
		privilegeModuleRepository.deletePrivilegeModule(id);
	}
   
    
}