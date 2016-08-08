package cn.com.open.pay.platform.manager.infrastructure.repository;
/**
 * 
 */
import java.util.List;

import org.springframework.stereotype.Repository;

import cn.com.open.pay.platform.manager.login.model.User;

public interface UserRepository extends Repository {
	User findByUsername(String username);
	List<User> findByEmail(String email);

	List<User> findByPhone(String phone);
	
	List<User> findByCardNo(String cardNo);
	
}