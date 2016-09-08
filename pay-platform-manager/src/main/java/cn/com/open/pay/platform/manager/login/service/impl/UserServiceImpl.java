package cn.com.open.pay.platform.manager.login.service.impl;

import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.com.open.pay.platform.manager.infrastructure.repository.UserRepository;
import cn.com.open.pay.platform.manager.login.model.User;
import cn.com.open.pay.platform.manager.login.service.UserService;

@Service("userService")
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;
    
    /**
	 * 根据用户id删除用户
	 * @param username
	 */
    @Override
	public boolean removeUserByID(Integer id){
		try{
			userRepository.removeUserByID(id);
			return true;
		}catch(Exception e){
			return false;
		}
    }
	
    
    /**
	 * 添加用户
	 * @param user_name		用户名
	 * @param real_name		真实姓名
	 * @param nickname		昵称
	 * @param sha_password		MD5加密密码
	 */
	@Override
	public boolean addUser(User user) {
		try{
			userRepository.addUser(user);
			return true;
		}catch(Exception e){
			return false;
		}
	}

	/**
	 * 根据用户名、真实姓名、昵称 查询用户，返回 User集合
	 * @param user
	 * @return
	 */
	@Override
	public List<User> findUsers(User user) {
		return userRepository.findUsers(user);
	}
	
	
	@Override
	public User findByUsername(String username) {
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

	@Override
	public List<User> getPayCount(String startTime, String endTime) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<User> findByMonth(String startTime, String endTime) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<User> findByWeek(String startTime, String endTime) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<User> findByYear(String startTime, String endTime) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HashMap<String, Object> getTotalAmountByTime(String startTime,
			String endTime, String appId, String paymentId, String channelId) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Boolean updateUser(User user) {
		try{
			userRepository.updateUser(user);
			return true;
		}catch(Exception e){
			return false;
		}
	}
	


  
}