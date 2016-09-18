package cn.com.open.pay.platform.manager.privilege.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.com.open.pay.platform.manager.infrastructure.repository.ManagerRepository;
import cn.com.open.pay.platform.manager.infrastructure.repository.PrivilegeModuleRepository;
import cn.com.open.pay.platform.manager.privilege.model.Manager;
import cn.com.open.pay.platform.manager.privilege.service.ManagerService;
@Service("managerService")
public class ManagerServiceImpl implements ManagerService {
	@Autowired
	private ManagerRepository managerRepository;
	@Override
	public Manager getManagerById(Integer id) {
		// TODO Auto-generated method stub
		return managerRepository.findManagerById(id);
	}

}
