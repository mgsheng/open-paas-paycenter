package cn.com.open.pay.platform.manager.privilege.service;

import java.util.List;

import cn.com.open.pay.platform.manager.order.model.MerchantOrderInfo;
import cn.com.open.pay.platform.manager.privilege.model.PrivilegeModule;


public interface PrivilegeModuleService {
	List<PrivilegeModule> findByName(String name);
	void savePrivilegeModule(PrivilegeModule privilegeModule);
	void updatePrivilegeModule(PrivilegeModule privilegeModule);
	void deletePrivilegeModule(Integer id);
   
    
}