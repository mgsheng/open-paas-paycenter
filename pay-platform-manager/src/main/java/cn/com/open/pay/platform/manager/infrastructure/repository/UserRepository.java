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
	 * 添加用户
	 * @param user_name		用户名
	 * @param real_name		真实姓名
	 * @param nickname		昵称
	 * @param sha_password		MD5加密密码
	 */
	void addUser(User users);
	
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