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
    /**
	 * 添加用户
	 * @param user_name		用户名
	 * @param real_name		真实姓名
	 * @param nickname		昵称
	 * @param sha_password		MD5加密密码
	 */
	@Override
	public void addRole(PrivilegeRole privilegeRole) {
		System.out.println("-----------------UserServiceImpl-------addUser---------------");
		privilegeRoleRepository.addRole(privilegeRole);
		
	}
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
	

//	@Override
//	public List<User> findByEmail(String account) {
//		// TODO Auto-generated method stub
//		return roleRepository.findByEmail(account);
//	}
//
//	@Override
//	public List<User> findByPhone(String account) {
//		// TODO Auto-generated method stub
//		return roleRepository.findByPhone(account);
//	}
//
//	@Override
//	public List<User> findByCardNo(String cardNo) {
//		// TODO Auto-generated method stub
//		return roleRepository.findByCardNo(cardNo);
//	}
//
//	@Override
//	public List<User> getPayCount(String startTime, String endTime) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public List<User> findByMonth(String startTime, String endTime) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public List<User> findByWeek(String startTime, String endTime) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public List<User> findByYear(String startTime, String endTime) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public HashMap<String, Object> getTotalAmountByTime(String startTime,
//			String endTime, String appId, String paymentId, String channelId) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//	@Override
//	public Boolean updateUser(User user) {
//		try{
//			userRepository.updateUser(user);
//			return true;
//		}catch(Exception e){
//			return false;
//		}
//	}
//

  
}