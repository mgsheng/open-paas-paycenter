package cn.com.open.pay.platform.manager.infrastructure.repository;
/**
 * 
 */
import org.springframework.stereotype.Repository;

import cn.com.open.pay.platform.manager.login.model.User;

public interface UserRepository extends Repository {
	User findByUsername(String username);
	
}