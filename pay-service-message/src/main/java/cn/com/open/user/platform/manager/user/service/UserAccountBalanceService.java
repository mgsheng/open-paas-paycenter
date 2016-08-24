package cn.com.open.user.platform.manager.user.service;

import cn.com.open.user.platform.manager.user.model.UserAccountBalance;

/**
 * 
 */
public interface UserAccountBalanceService {


	void saveUserAccountBalance(UserAccountBalance userAccountBalance);
	UserAccountBalance findByUserId(String userId);
	UserAccountBalance getBalanceInfo(String sourceId,Integer appId);
	public Boolean updateBalanceInfo(UserAccountBalance userAccountBalance);
}