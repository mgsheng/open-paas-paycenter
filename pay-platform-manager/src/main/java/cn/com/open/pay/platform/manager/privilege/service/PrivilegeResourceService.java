package cn.com.open.pay.platform.manager.privilege.service;

import java.util.List;

import cn.com.open.pay.platform.manager.order.model.MerchantOrderInfo;
import cn.com.open.pay.platform.manager.privilege.model.PrivilegeResource;


public interface PrivilegeResourceService {
	List<PrivilegeResource> findByName(String name);
	List<PrivilegeResource> getPageListByName(PrivilegeResource privilegeResource);
	void savePrivilegeResource(PrivilegeResource privilegeResource);
	void updatePrivilegeResource(PrivilegeResource privilegeResource);
	void deletePrivilegeResource(Integer id);
	int findQueryCount(String name);
	List<PrivilegeResource> findAllResource();
    
}