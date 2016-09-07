package cn.com.open.pay.platform.manager.login.service;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.com.open.pay.platform.manager.login.model.User;
import cn.com.open.pay.platform.manager.privilege.model.PrivilegeRole;



/**
 * 
 */
public interface RoleService {
	/**
	 * 添加用户
	 * @param user_name		用户名
	 * @param real_name		真实姓名
	 * @param nickname		昵称
	 * @param sha_password		MD5加密密码
	 */
	void addRole(PrivilegeRole privilegeRole);
	
	List<PrivilegeRole> findByRoleName(PrivilegeRole privilegeRole);
	
	int findQueryCount(PrivilegeRole privilegeRole);
//	List<User> findByEmail(String account);
//	List<User> findByPhone(String account);
//	List<User> findByCardNo(String cardNo);
//	List<User>getPayCount(String startTime,String endTime);
//	List<User>findByMonth(String startTime,String endTime);
//	List<User>findByWeek(String startTime,String endTime);
//	List<User>findByYear(String startTime,String endTime);
//	HashMap<String, Object> getTotalAmountByTime(String startTime, String endTime,String appId,String paymentId,String channelId);
//	Boolean updateUser(User user);

	

	

	
}