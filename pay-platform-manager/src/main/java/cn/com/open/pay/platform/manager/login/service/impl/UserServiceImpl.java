package cn.com.open.pay.platform.manager.login.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.com.open.pay.platform.manager.infrastructure.repository.UserRepository;
import cn.com.open.pay.platform.manager.login.model.User;
import cn.com.open.pay.platform.manager.login.service.UserService;

/**
 * 
 */
@Service("userService")
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

	@Override
	public User findByUsername(String username) {
		// TODO Auto-generated method stub
		return userRepository.findByUsername(username);
	}

	@Override
	public List<User> findByEmail(String account) {
		// TODO Auto-generated method stub
		return userRepository.findByEmail(account);
	}

	@Override
	public List<User> findByPhone(String account) {
		// TODO Auto-generated method stub
		return userRepository.findByPhone(account);
	}

	@Override
	public List<User> findByCardNo(String cardNo) {
		// TODO Auto-generated method stub
		return userRepository.findByCardNo(cardNo);
	}
  

  
}