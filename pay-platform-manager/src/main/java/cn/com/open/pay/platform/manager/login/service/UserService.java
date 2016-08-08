package cn.com.open.pay.platform.manager.login.service;

import cn.com.open.pay.platform.manager.login.model.User;



/**
 * 
 */
public interface UserService {
	
	User findByUsername(String username);
	
}