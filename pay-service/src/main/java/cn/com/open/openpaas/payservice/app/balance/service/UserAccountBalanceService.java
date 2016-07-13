package cn.com.open.openpaas.payservice.app.balance.service;

import cn.com.open.openpaas.payservice.app.balance.model.UserAccountBalance;

/**
 * 
 */
public interface UserAccountBalanceService {


	void saveUserAccountBalance(UserAccountBalance userAccountBalance);
	UserAccountBalance findByUserId(String userId);
	UserAccountBalance getBalanceInfo(String sourceId,Integer appId);
	void updateBalanceInfo(UserAccountBalance userAccountBalance);
}