package cn.com.open.pay.platform.manager.login.service;

import java.util.List;

import cn.com.open.pay.platform.manager.login.model.User;



/**
 * 
 */
public interface UserService {
	
	User findByUsername(String username);
	List<User> findByEmail(String account);
	List<User> findByPhone(String account);
	List<User> findByCardNo(String cardNo);
	
}