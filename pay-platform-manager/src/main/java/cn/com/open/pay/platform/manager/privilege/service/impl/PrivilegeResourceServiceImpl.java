package cn.com.open.pay.platform.manager.privilege.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.com.open.pay.platform.manager.infrastructure.repository.PrivilegeResourceRepository;
import cn.com.open.pay.platform.manager.privilege.model.PrivilegeResource;
import cn.com.open.pay.platform.manager.privilege.service.PrivilegeResourceService;

@Service("privilegeResourceService")
public class PrivilegeResourceServiceImpl implements  PrivilegeResourceService {
	@Autowired
	private PrivilegeResourceRepository privilegeResourceRepository;

	@Override
	public List<PrivilegeResource> findByName(String name) {
		// TODO Auto-generated method stub
		return privilegeResourceRepository.findByName(name);
	}

	@Override
	public void savePrivilegeResource(PrivilegeResource privilegeResource) {
		// TODO Auto-generated method stub
		privilegeResourceRepository.savePrivilegeResource(privilegeResource);
	}

	@Override
	public void updatePrivilegeResource(PrivilegeResource privilegeResource) {
		// TODO Auto-generated method stub
		privilegeResourceRepository.updatePrivilegeResource(privilegeResource);
	}
	@Override
	public void deletePrivilegeResource(Integer id) {
		// TODO Auto-generated method stub
		privilegeResourceRepository.deletePrivilegeResource(id);
	}

	@Override
	public List<PrivilegeResource> getPageListByName(
			PrivilegeResource privilegeResource) {
		// TODO Auto-generated method stub
		return privilegeResourceRepository.getPageListByName(privilegeResource);
	}

	@Override
	public int findQueryCount(String name) {
		// TODO Auto-generated method stub
		return privilegeResourceRepository.findQueryCount(name);
	}

	@Override
	public List<PrivilegeResource> findAllResource() {
		// TODO Auto-generated method stub
		return privilegeResourceRepository.findAllResource();
	}
   
    
}