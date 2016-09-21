package cn.com.open.pay.platform.manager.infrastructure.repository;
/**
 * 
 */
import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import cn.com.open.pay.platform.manager.login.model.User;

public interface UserRepository extends Repository {
	
	/**
	 * 查询指定用户的角色情况
	 * @param user
	 * @return String
	 */
	public String findUserRoles(User user);
	
	/**
	 * 查询User对象个数
	 * @param user
	 * @return User对象个数
	 */
	public int findUsersCount(User user);
	
	/**
	 * 查询所有该部门的User对象个数
	 * @param user
	 * @return User对象个数
	 */
	public int findDeptUsersCount(User user);
	
	/**
	 * 根据部门ID，部门名查询该部门所以用户信息
	 * @param username
	 */
	public List<User> findDeptUsers(User user);
	
	/**
	 * 根据用户id删除用户
	 * @param username
	 */
	void removeUserByID(Integer id);
	
	/**
	 * 添加用户
	 * @param user_name		用户名
	 * @param real_name		真实姓名
	 * @param nickname		昵称
	 * @param sha_password		MD5加密密码
	 */
	void addUser(User user);
	
	/**
	 * 根据用户名、真实姓名、昵称 查询用户，返回 User集合
	 * @param user
	 * @return
	 */
	List<User> findUsers(User user);
	
	User findByUsername(String username);
	List<User> findByEmail(String email);
	List<User> findByPhone(String phone);
	 void updateUser(User user);
	List<User> findByCardNo(String cardNo);
	HashMap<String, Object> getPayCount(@Param("startTime")String startTime, @Param("endTime")String endTime,@Param("appId")String appId,@Param("paymentId")String paymentId,@Param("channelId")String channelId);
	HashMap<String, Object> getPayAmount(@Param("startTime")String startTime, @Param("endTime")String endTime,@Param("appId")String appId,@Param("paymentId")String paymentId,@Param("channelId")String channelId);
	HashMap<String, Object> getUserCount(@Param("startTime")String startTime, @Param("endTime")String endTime,@Param("appId")String appId,@Param("paymentId")String paymentId,@Param("channelId")String channelId);
	HashMap<String, Object> getPay(@Param("startTime")String startTime, @Param("endTime")String endTime,@Param("appId")String appId,@Param("paymentId")String paymentId,@Param("channelId")String channelId);
}