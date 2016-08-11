package cn.com.open.pay.platform.manager.login.service;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.com.open.pay.platform.manager.login.model.User;



/**
 * 
 */
public interface UserService {
	
	User findByUsername(String username);
	List<User> findByEmail(String account);
	List<User> findByPhone(String account);
	List<User> findByCardNo(String cardNo);
	List<User>getPayCount(String startTime,String endTime);
	List<User>findByMonth(String startTime,String endTime);
	List<User>findByWeek(String startTime,String endTime);
	List<User>findByYear(String startTime,String endTime);
	HashMap<String, Object> getTotalAmountByTime(String startTime, String endTime,String appId,String paymentId,String channelId);
	
}