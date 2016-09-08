package cn.com.open.pay.platform.manager.login.service;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.com.open.pay.platform.manager.login.model.User;

/**
 * 
 */
public interface UserService {
	
	/**
	 * 根据用户id删除用户
	 * @param username
	 */
	boolean removeUserByID(Integer id);
	
	/**
	 * 添加用户
	 * @param user_name		用户名
	 * @param real_name		真实姓名
	 * @param nickname		昵称
	 * @param sha_password		MD5加密密码
	 */
	boolean addUser(User user);
	
	/**
	 * 根据用户名、真实姓名、昵称 查询用户，返回 User集合
	 * @param user
	 * @return
	 */
	List<User> findUsers(User user);
	
	User findByUsername(String username);
	List<User> findByEmail(String account);
	List<User> findByPhone(String account);
	List<User> findByCardNo(String cardNo);
	List<User>getPayCount(String startTime,String endTime);
	List<User>findByMonth(String startTime,String endTime);
	List<User>findByWeek(String startTime,String endTime);
	List<User>findByYear(String startTime,String endTime);
	HashMap<String, Object> getTotalAmountByTime(String startTime, String endTime,String appId,String paymentId,String channelId);
	Boolean updateUser(User user);
	
}